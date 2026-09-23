package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.ChunkChangeEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.render.RenderStatic;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.*;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hoosiers
 * @since 05-17-2026
 */

@Hack.Info(name = "NewChunks", category = Category.Render, description = "Renders a box around chunks that are newly generated.")
public final class NewChunks extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.NewChunks;
    }

    public final GeneralButtonSetting clearLogged = new GeneralButtonSetting("Clear Logged Chunks", GeneralButtonSetting.ButtonType.CLICK_ACTION, "Clear rendered new chunks.") {

        @Override
        public void onChange() {
            NewChunks newChunks = this.getHackManager().getHack(NewChunks.class);

            if (newChunks != null) {
                newChunks.clearLoggedChunks();

                if (newChunks.isEnabled() && !this.failsNullCheck()) {
                    this.sendClientMessageWithHeader("NewChunks: Cleared logged chunks!");
                }
            }
        }
    };
    public final IntegerSetting renderY = new IntegerSetting("Render at Y", 0, 0, 128, "Y Level", "How far up should the render be.");
    public final DoubleSetting thickness = new DoubleSetting("Y Thickness", 0.1, 0.1, 3.0, 1, "Blocks", "How thick should the render be.");
    public final BooleanSetting limitRenderDistance = new BooleanSetting("Check Render Distance", true, "Enable to only render new chunks within a defined distance from the player. Modify this distance in the setting below.");
    public final IntegerSetting renderDistance = new IntegerSetting("Render Distance", 256, 32, 256, "Blocks", "Maximum distance to render new chunks.");
    public final BooleanSetting deleteFarAway = new BooleanSetting("Delete Far Away", false, "Deletes new chunks that are far away from the player. Modified by the Delete Distance setting.");
    public final IntegerSetting deleteDistance = new IntegerSetting("Delete Distance", 512, 256, 1024, "Blocks", "The distance new chunks have to be to be deleted.");
    public final BooleanSetting deleteOnDisable = new BooleanSetting("Delete On Disable", true, "Clear saved new chunks when the hack is disabled.");
    public final BooleanSetting frustrum = new BooleanSetting("Frustrum Renders", true, "Performance boost. Only renders new chunks if they are visible on the screen.");
    public final EnumSetting renderMode = new EnumSetting("Render Mode", RenderMode.Outline, new String[]{
            "This is how the render will look.",
            "Outline® Draws a box around the edges.",
            "Fill® Draws a shaded box.",
            "Both® Draws a shaded box with outlined edges."
    });
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5, "None", "How thick the outline line should be.");
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 50, 0, 255, "None", "How transparent the render should be. Lower values = more transparent.");
    public final ColorSetting renderColor = new ColorSetting("Color", Color.YELLOW, "Sets the color of the render.");

    /**
     * Switching from an ArrayList to a CopyOnWriteArrayList fixes some rare exceptions in the chunkChangeEventAction and onRender3D() methods.
     * Not 100% sure still how the exceptions happen, but I think it has something to do with threading.
     * Theory:
     * The method we plug into to call inbound packets is on another thread.
     * So, the event being called occurs at unpredictable times and while the other methods are being processed.
     * So, if onClientTick() or onRender3D() is accessing/modifying the chunksToRender list, an exception is thrown.
     * Would have never known since the EventHandler is pseudo-inherently thread safe for calling events.
     */
    private final CopyOnWriteArrayList<int[]> chunksToRender = new CopyOnWriteArrayList<>();

    private final int BLOCK_AIR = 0;

    @Override
    public void onDisable(boolean isNull) {
        if (this.deleteOnDisable.getValue()) {
            this.clearLoggedChunks();
        }
    }

    @Override
    public void onClientTick() {
        if (this.deleteFarAway.getValue() && !this.chunksToRender.isEmpty()) {
            this.chunksToRender.parallelStream().forEach(ints -> {

                if (!this.isPositionWithinXZRange(ints[0] * 16, ints[1] * 16, this.deleteDistance.getValue())) {
                    this.chunksToRender.remove(ints);
                }
            });
        }
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (this.frustrum.getValue()) {
            RenderStatic.setFrustrumPosition(RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ);

            this.chunksToRender.forEach(ints -> {

                int x = ints[0] * 16;
                int y = this.renderY.getValue();
                int z = ints[1] * 16;

                if (RenderStatic.isBoxInFrustum(x, y, z, x + 16, y + this.thickness.getValue(), z + 16)) {
                    this.renderChunk(ints);
                }
            });

        } else {
            this.chunksToRender.forEach(this::renderChunk);
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<ChunkChangeEvent> chunkChangeEventAction = this.addHackEventAction(new EventAction<>(ChunkChangeEvent.class, event -> {
        if (event.type.equals(ChunkChangeEvent.Type.SINGLE)) {
            Packet53BlockChange packet = event.packet53BlockChange;

            if (packet == null) {
                return;
            }

            int type = packet.type;

            if (!hasAcceptedType(type)) {
                return;
            }

            int x = packet.xPosition;
            int y = packet.yPosition;
            int z = packet.zPosition;

            int[] chunk = new int[]{x >> 4, z >> 4};

            if (this.triggersNewChunk(x, y, z, chunk, type)) {
                this.chunksToRender.add(chunk);
            }

            return;
        }

        Packet52MultiBlockChange packet = event.packet52MultiBlockChange;

        if (packet == null || packet.typeArray == null || packet.coordinateArray == null) {
            return;
        }

        if (packet.typeArray.length < 1) {
            return;
        }

        int chunkX = packet.xPosition << 4;
        int chunkZ = packet.zPosition << 4;

        for (int i = 0; i < packet.typeArray.length; i++) {

            int type = packet.typeArray[i];

            if (!hasAcceptedType(type)) {
                continue;
            }

            if (packet.coordinateArray.length < i + 1) {
                continue;
            }

            int x = chunkX + (packet.coordinateArray[i] >> 12 & 15);
            int y = packet.coordinateArray[i] & 255;
            int z = chunkZ + (packet.coordinateArray[i] >> 8 & 15);

            int[] chunk = new int[]{packet.xPosition, packet.zPosition};

            if (this.triggersNewChunk(x, y, z, chunk, type)) {
                this.chunksToRender.add(chunk);
                break; /* We break here because the chunk is only added to the list once, so we don't care about the other changes. */
            }
        }
    }));

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addHackEventAction(new EventAction<>(ChangeWorldEvent.class, event -> this.clearLoggedChunks()));

    //Ways to tell if a chunk is newly generated: sand/gravel falling and water/lava flowing.
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean hasAcceptedType(int type) {
        if (type == this.BLOCK_AIR) {
            return true;
        }

        return type >= Block.waterMoving.blockID && type <= Block.lavaStill.blockID;
    }

    private boolean triggersNewChunk(int x, int y, int z, int[] chunk, int type) {
        AtomicBoolean hasDuplicate = new AtomicBoolean(false);

        this.chunksToRender.forEach(ints -> {
            if (!hasDuplicate.get() && ints[0] == chunk[0] && ints[1] == chunk[1]) {
                hasDuplicate.set(true);
            }
        });

        if (hasDuplicate.get()) {
            return false;
        }

        if (this.isTargetWithinDistanceOfEntity(this.getPlayer(), x, z, 9)) {
            return false;
        }

        AtomicBoolean isWithinPlayerRange = new AtomicBoolean(false);

        this.getFilteredLoadedEntities().parallelStream().forEach(entity -> {

            if (!isWithinPlayerRange.get() && entity instanceof EntityOtherPlayerMP && this.isTargetWithinDistanceOfEntity(entity, x, z, 9)) {
                isWithinPlayerRange.set(true);
            }
        });

        if (isWithinPlayerRange.get()) {
            return false;
        }

        int blockId = this.getWorld().getBlockId(x, y, z);

        //Previous blockID is sand or gravel, has fallen, so new blockID "type" is air.
        if (type == this.BLOCK_AIR && (blockId == Block.sand.blockID || blockId == Block.gravel.blockID)) {
            return true;
        }

        //Previous blockID is air, water or lava has flowed, so new blockID "type" is water or lava.
        return type >= Block.waterMoving.blockID && type <= Block.lavaStill.blockID && blockId == this.BLOCK_AIR;
    }

    @SuppressWarnings("SameParameterValue")
    private boolean isTargetWithinDistanceOfEntity(Entity entity, int x, int z, double range) {
        double entityX = entity.posX;
        double entityZ = entity.posZ;

        if (entity == this.getPlayer()) {
            HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

            if (freecam != null && freecam.isEnabled()) {
                entityX = freecam.oldX;
                entityZ = freecam.oldZ;
            }
        }

        double rangeSq = this.squaredD(range);

        return this.squaredD(x - entityX) <= rangeSq && this.squaredD(z - entityZ) <= rangeSq;
    }

    private void renderChunk(int[] chunk) {
        int actualX = chunk[0] * 16;
        int actualZ = chunk[1] * 16;

        if (this.limitRenderDistance.getValue() && !this.isPositionWithinXZRange(actualX, actualZ, this.renderDistance.getValue())) {
            return;
        }

        double minX = actualX - RenderManager.renderPosX;
        double minY = this.renderY.getValue() - RenderManager.renderPosY;
        double minZ = actualZ - RenderManager.renderPosZ;

        double maxX = minX + 16;
        double maxY = minY + this.thickness.getValue();
        double maxZ = minZ + 16;

        Color color = this.renderColor.getValueRainbow();

        switch ((RenderMode) this.renderMode.getValue()) {
            case Outline:
                this.drawOutlineBox(minX, minY, minZ, maxX, maxY, maxZ, this.lineWidth.getValue(), color);
                break;
            case Fill:
                this.drawFillBox(minX, minY, minZ, maxX, maxY, maxZ, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(minX, minY, minZ, maxX, maxY, maxZ, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                this.drawOutlineBox(minX, minY, minZ, maxX, maxY, maxZ, this.lineWidth.getValue(), color);
                break;
        }
    }

    public void clearLoggedChunks() {
        this.chunksToRender.clear();
    }

    private enum RenderMode {
        Outline,
        Fill,
        Both
    }
}
