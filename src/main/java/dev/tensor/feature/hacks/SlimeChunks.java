package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.LoadChunkEvent;
import dev.tensor.backend.mixins.accessors.ChunkProviderAccessor;
import dev.tensor.backend.mixins.accessors.ChunkProviderClientAccessor;
import dev.tensor.misc.event.EventAction;
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

@Hack.Info(name = "SlimeChunks", category = Category.Render)
public final class SlimeChunks extends Render3DHack {

    public static long STATIC_MULTIPLAYER_WORLD_SEED = -1;

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.SlimeChunks;
    }

    public final GeneralButtonSetting clearLogged = new GeneralButtonSetting("Clear Logged Chunks", GeneralButtonSetting.ButtonType.CLICK_ACTION) {

        @Override
        public void onChange() {
            SlimeChunks slimeChunks = this.getHackManager().getHack(SlimeChunks.class);

            if (slimeChunks != null) {
                slimeChunks.clearLoggedChunks(slimeChunks.isEnabled());

                if (slimeChunks.isEnabled() && !this.failsNullCheck()) {
                    this.sendClientMessageWithHeader("SlimeChunks: Cleared logged chunks!");
                }
            }
        }
    };
    public final IntegerSetting renderY = new IntegerSetting("Render at Y", 0, 0, 128);
    public final DoubleSetting thickness = new DoubleSetting("Y Thickness", 0.1, 0.1, 3.0, 1);
    public final BooleanSetting limitRenderDistance = new BooleanSetting("Check Render Distance", true);
    public final IntegerSetting renderDistance = new IntegerSetting("Render Distance", 256, 32, 256);
    public final BooleanSetting deleteFarAway = new BooleanSetting("Delete Far Away", false);
    public final IntegerSetting deleteDistance = new IntegerSetting("Delete Distance", 512, 256, 1024);
    public final BooleanSetting deleteOnDisable = new BooleanSetting("Delete On Disable", true);
    public final BooleanSetting frustrum = new BooleanSetting("Frustrum Renders", true);
    public final EnumSetting renderMode = new EnumSetting("Render Mode", RenderMode.Fill);
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
