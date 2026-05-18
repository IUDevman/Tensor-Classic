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
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hoosiers
 * @since 05-17-2026
 */

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@Hack.Info(name = "NewChunks", category = Category.Render)
public final class NewChunks extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.NewChunks;
    }

    public final IntegerSetting renderY = new IntegerSetting("Render at Y", 0, 0, 128);
    public final DoubleSetting thickness = new DoubleSetting("Y Thickness", 1.0, 0.1, 3.0, 1);
    public final BooleanSetting limitRenderDistance = new BooleanSetting("Check Render Distance", true);
    public final IntegerSetting renderDistance = new IntegerSetting("Render Distance", 256, 32, 256);
    public final BooleanSetting deleteFarAway = new BooleanSetting("Delete Far Away", false);
    public final IntegerSetting deleteDistance = new IntegerSetting("Delete Distance", 512, 256, 1024);
    public final BooleanSetting deleteOnDisable = new BooleanSetting("Delete On Disable", true);
    public final BooleanSetting frustrum = new BooleanSetting("Frustrum Renders", true);
    public final EnumSetting renderMode = new EnumSetting("Render Mode", RenderMode.Outline);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 50, 0, 255);
    public final ColorSetting renderColor = new ColorSetting("Color", Color.YELLOW);

    private final ArrayList<int[]> chunksToRender = new ArrayList<>();

    @Override
    public void onDisable(boolean isNull) {
        if (this.deleteOnDisable.getValue()) {
            this.chunksToRender.clear();
        }

        RenderStatic.tensor_playerFrustrum = null;
    }

    @Override
    public void onClientTick() {
        if (this.deleteFarAway.getValue() && !this.chunksToRender.isEmpty()) {
            ArrayList<int[]> toRemove = new ArrayList<>();

            this.chunksToRender.forEach(ints -> {

                if (!this.isChunkWithinDistance(ints[0] * 16, ints[1] * 16, this.deleteDistance.getValue())) {
                    toRemove.add(ints);
                }
            });

            this.chunksToRender.removeAll(toRemove);
        }
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (this.frustrum.getValue()) {
            if (RenderStatic.tensor_playerFrustrum == null) {
                RenderStatic.tensor_playerFrustrum = new Frustrum();
            }

            RenderStatic.tensor_playerFrustrum.setPosition(RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ);

            this.chunksToRender.forEach(ints -> {

                int x = ints[0] * 16;
                int y = this.renderY.getValue();
                int z = ints[1] * 16;

                if (RenderStatic.tensor_playerFrustrum.isBoxInFrustum(x, y, z, x + 16, y + this.thickness.getValue(), z + 16)) {
                    this.renderChunk(ints);
                }
            });

        } else {
            this.chunksToRender.forEach(this::renderChunk);
        }
    }

    @SuppressWarnings("unused")
    EventAction<ChunkChangeEvent> chunkChangeEventAction = this.addEventAction(new EventAction<>(ChunkChangeEvent.class, event -> {

        if (event.type.equals(ChunkChangeEvent.Type.Single)) {
            Packet53BlockChange packet = event.packet53BlockChange;

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

            int x = chunkX + (packet.coordinateArray[i] >> 12 & 15);
            int y = packet.coordinateArray[i] & 255;
            int z = chunkZ + (packet.coordinateArray[i] >> 8 & 15);

            int[] chunk = new int[]{packet.xPosition, packet.zPosition};

            if (this.triggersNewChunk(x, y, z, chunk, type)) {
                this.chunksToRender.add(chunk);
                break;
            }
        }
    }));

    @SuppressWarnings("unused")
    EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(new EventAction<>(ChangeWorldEvent.class, event -> {
        this.chunksToRender.clear();
        RenderStatic.tensor_playerFrustrum = null;
    }));

    private boolean hasAcceptedType(int type) {
        if (type == 0) {
            return true;
        }

        return type >= 8 && type <= 11;
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

        this.getFilteredLoadedEntities().forEach(entity -> {

            if (!isWithinPlayerRange.get() && entity instanceof EntityOtherPlayerMP && this.isTargetWithinDistanceOfEntity(entity, x, z, 9)) {
                isWithinPlayerRange.set(true);
            }
        });

        if (isWithinPlayerRange.get()) {
            return false;
        }

        int blockId = this.getWorld().getBlockId(x, y, z);

        if (type == 0 && (blockId == Block.sand.blockID || blockId == Block.gravel.blockID)) {
            return true;
        }

        return type >= Block.waterMoving.blockID && type <= Block.lavaStill.blockID && blockId == 0;
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

        if (this.limitRenderDistance.getValue() && !this.isChunkWithinDistance(actualX, actualZ, this.renderDistance.getValue())) {
            return;
        }

        double minX = actualX - RenderManager.renderPosX;
        double minY = this.renderY.getValue() - RenderManager.renderPosY;
        double minZ = actualZ - RenderManager.renderPosZ;

        double maxX = minX + 16;
        double maxY = minY + this.thickness.getValue();
        double maxZ = minZ + 16;

        Color color = this.renderColor.getValue();

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

    private boolean isChunkWithinDistance(int chunkX, int chunkZ, int distance) {
        double distanceSq = this.squaredD(distance);

        double playerX = this.getPlayer().posX;
        double playerZ = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {
            playerX = freecam.oldX;
            playerZ = freecam.oldZ;
        }

        if (this.squaredD(playerX - chunkX) > distanceSq) {
            return false;
        }

        return !(this.squaredD(playerZ - chunkZ) > distanceSq);
    }

    private enum RenderMode {
        Outline,
        Fill,
        Both
    }
}
