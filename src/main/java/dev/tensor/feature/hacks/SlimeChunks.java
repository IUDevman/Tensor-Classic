package dev.tensor.feature.hacks;

import dev.tensor.TensorClassic;
import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.LoadChunkEvent;
import dev.tensor.backend.mixins.accessors.ChunkProviderAccessor;
import dev.tensor.backend.mixins.accessors.ChunkProviderClientAccessor;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.render.RenderStatic;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.*;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hoosiers
 * @since 09-19-2026
 */

@Hack.Info(name = "SlimeChunks", category = Category.Render, description = "Renders a box around chunks that can spawn slimes.")
public final class SlimeChunks extends Render3DHack {

    private static long STATIC_MULTIPLAYER_WORLD_SEED = -1;

    public static void updateMultiplayerWorldSeed(long newSeed) {
        long oldSeed = STATIC_MULTIPLAYER_WORLD_SEED;
        STATIC_MULTIPLAYER_WORLD_SEED = newSeed;

        if (TensorClassic.getLogger() != null) {
            TensorClassic.getLogger().log("Set Tensor multiplayer world seed from '" + (oldSeed == -1 ? "NONE" : oldSeed) + "' to '" + STATIC_MULTIPLAYER_WORLD_SEED + "'!", TensorLogger.Level.INFO);
        }
    }

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.SlimeChunks;
    }

    public final GeneralButtonSetting clearLogged = new GeneralButtonSetting("Clear Logged Chunks", GeneralButtonSetting.ButtonType.CLICK_ACTION, "Clear rendered slime chunks.") {

        @Override
        public void onChange() {
            SlimeChunks slimeChunks = this.getHackManager().getHack(SlimeChunks.class);

            if (slimeChunks != null) {
                boolean shouldReset = slimeChunks.isEnabled() && !this.failsNullCheck();

                slimeChunks.clearLoggedChunks(shouldReset);

                if (shouldReset) {
                    this.sendClientMessageWithHeader("SlimeChunks: Cleared logged chunks!");
                }
            }
        }
    };
    public final IntegerSetting renderY = new IntegerSetting("Render at Y", 0, 0, 128);
    public final DoubleSetting thickness = new DoubleSetting("Y Thickness", 0.1, 0.1, 3.0, 1);
    public final BooleanSetting limitRenderDistance = new BooleanSetting("Check Render Distance", true, "Enable to only render slime chunks if within a defined distance to the player");
    public final IntegerSetting renderDistance = new IntegerSetting("Render Distance", 256, 32, 256);
    public final BooleanSetting deleteFarAway = new BooleanSetting("Delete Far Away", false, "Enable to automatically remove slime chunks that are far away from the player. Define this distance with the Delete Distance setting.");
    public final IntegerSetting deleteDistance = new IntegerSetting("Delete Distance", 512, 256, 1024);
    public final BooleanSetting deleteOnDisable = new BooleanSetting("Delete On Disable", true, "Clears the list of slime chunks when the hack is disabled.");
    public final BooleanSetting frustrum = new BooleanSetting("Frustrum Renders", true, "Speed boost. Only renders slime chunks if they are seen on screen.");
    public final EnumSetting renderMode = new EnumSetting("Render Mode", RenderMode.Fill, new String[]{
            "This is how the render will look.",
            "Outline® Draws a box around the edges.",
            "Fill® Draws a shaded box.",
            "Both® Draws a shaded box with outlined edges."
    });
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 50, 0, 255);
    public final ColorSetting renderColor = new ColorSetting("Color", Color.GREEN);

    private final CopyOnWriteArrayList<int[]> chunksToRender = new CopyOnWriteArrayList<>();

    @Override
    public void onEnable(boolean isNull) {
        if (isNull) {
            return;
        }

        this.setUpLoadedChunks();
    }

    @Override
    public void onDisable(boolean isNull) {
        if (this.deleteOnDisable.getValue()) {
            this.clearLoggedChunks(false);
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
    private final EventAction<LoadChunkEvent> loadChunkEventAction = this.addHackEventAction(new EventAction<>(LoadChunkEvent.class, event -> {
        if (event.chunk == null) {
            return;
        }

        if (this.getWorld().worldProvider.isHellWorld) {
            return;
        }

        if (this.triggersSlimeChunk(event.chunk)) {
            this.chunksToRender.add(new int[]{event.chunk.xPosition, event.chunk.zPosition});
        }
    }));

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addHackEventAction(new EventAction<>(ChangeWorldEvent.class, event -> this.clearLoggedChunks(true)));

    private boolean triggersSlimeChunk(Chunk chunk) {
        if (!this.isSlimeChunk(chunk.xPosition, chunk.zPosition)) {
            return false;
        }

        AtomicBoolean alreadyLoaded = new AtomicBoolean(false);

        this.chunksToRender.forEach(ints -> {
            if (!alreadyLoaded.get() && ints[0] == chunk.xPosition && ints[1] == chunk.zPosition) {
                alreadyLoaded.set(true);
            }
        });

        return !alreadyLoaded.get();
    }

    private boolean isSlimeChunk(int chunkX, int chunkZ) {
        Random random = new Random(this.getWorldSeed() + ((long) chunkX * chunkX * 0x4c1906) + (chunkX * 0x5ac0dbL) + (long) chunkZ * chunkZ * 0x4307a7L + (chunkZ * 0x5f24fL) ^ 0x3ad8025f);

        return random.nextInt(10) == 0;
    }

    /**
     * NOTE: So Minecraft is retarded, apparently.
     * All beta 1.7.3 servers send the world seed in Packet1Login, but the client doesn't do anything with it.
     * Tensor intercepts the packet to put the seed in STATIC_MULTIPLAYER_WORLD_SEED.
     *
     * This might actually not be true, I may be the retarded one? - Hoosiers
     *
     * @see dev.tensor.backend.mixins.NetworkManagerMixin !!!
     */
    private long getWorldSeed() {
        if (!this.isMultiplayer()) {
            return this.getWorld().getRandomSeed();
        }

        if (STATIC_MULTIPLAYER_WORLD_SEED == -1) {
            return 108181935L;
        }

        return STATIC_MULTIPLAYER_WORLD_SEED;
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

    @SuppressWarnings("unchecked")
    private void setUpLoadedChunks() {
        if (this.getWorld().worldProvider.isHellWorld) {
            return;
        }

        IChunkProvider provider = this.getWorld().getIChunkProvider();

        if (provider instanceof ChunkProviderClient) {
            ChunkProviderClient multiplayerProvider = (ChunkProviderClient) provider;

            ((ChunkProviderClientAccessor) multiplayerProvider).tensor_getChunkMapping().forEach((key, value) -> {

                if (!(value instanceof Chunk)) {
                    return;
                }

                Chunk toTest = (Chunk) value;

                if (this.triggersSlimeChunk(toTest)) {
                    this.chunksToRender.add(new int[]{toTest.xPosition, toTest.zPosition});
                }
            });

            return;
        }

        if (this.isMultiplayer()) {
            return;
        }

        if (provider instanceof ChunkProvider) {
            ChunkProvider singleplayerProvider = (ChunkProvider) provider;

            ((ChunkProviderAccessor) singleplayerProvider).tensor_getChunkMap().forEach((key, value) -> {
                if (!(value instanceof Chunk)) {
                    return;
                }

                Chunk toTest = (Chunk) value;

                if (this.triggersSlimeChunk(toTest)) {
                    this.chunksToRender.add(new int[]{toTest.xPosition, toTest.zPosition});
                }
            });
        }
    }

    public void clearLoggedChunks(boolean enabled) {
        this.chunksToRender.clear();

        if (!enabled) {
            return;
        }

        this.setUpLoadedChunks();
    }

    private enum RenderMode {
        Outline,
        Fill,
        Both
    }
}
