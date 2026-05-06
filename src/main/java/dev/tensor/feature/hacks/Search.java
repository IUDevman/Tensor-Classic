package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.constant.ViewBobEvent;
import dev.tensor.backend.events.variable.RenderBlockEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.block.BlockCoords;
import dev.tensor.misc.helpers.block.BlockList;
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
 * @since 03-29-2026
 */

@Hack.Info(name = "Search", category = Category.Render)
public final class Search extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.Search;
    }

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Chest) {

        @Override
        public void setValue(Enum<?> value) {
            super.setValue(value);

            if (!this.failsNullCheck()) {
                Search search = this.getHackManager().getHack(Search.class);

                if (search.isEnabled()) {
                    search.resetSearch();
                }
            }
        }
    };
    public final BlockListSetting custom = new BlockListSetting("Custom Block List", new BlockList(BlockList.CUSTOM())) {

        @Override
        public void hasChanged() {

            if (!this.failsNullCheck()) {
                Search search = this.getHackManager().getHack(Search.class);

                if (search.isEnabled() && search.mode.getValue().equals(Mode.Custom)) {
                    search.resetSearch();
                }
            }
        }
    };
    public final BooleanSetting frustrum = new BooleanSetting("Frustrum Renders", true);
    public final EnumSetting render = new EnumSetting("Render", Render.Outline);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 100, 0, 255);
    public final EnumSetting tracers = new EnumSetting("Tracers", TracerMode.None);
    public final BlockListSetting customTracers = new BlockListSetting("Custom Tracers List", new BlockList(BlockList.CUSTOM()));
    public final IntegerSetting tracerWidth = new IntegerSetting("Tracers Width", 1, 1, 5);
    public final IntegerSetting tracerAlpha = new IntegerSetting("Tracers Alpha", 255, 0, 255);
    public final ColorSetting searchColor = new ColorSetting("Search Color", Color.CYAN);
    public final ColorSetting chestColor = new ColorSetting("Chest Color", Color.ORANGE);
    public final ColorSetting portalColor = new ColorSetting("Portal Color", new Color(203, 109, 227, 255));

    private final BlockList UTILITY = new BlockList(BlockList.UTILITY_SEARCH());

    private final ArrayList<BlockCoords> blockCoords = new ArrayList<>();

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull) {
            this.resetSearch();
        } else {
            this.blockCoords.clear();
        }
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull) {
            this.resetSearch();
        } else {
            this.blockCoords.clear();
            RenderStatic.tensor_playerFrustrum = null;
        }
    }

    @SuppressWarnings("unused")
    EventAction<RenderBlockEvent> renderBlockEventAction = this.addEventAction(new EventAction<>(RenderBlockEvent.class, event -> {
        BlockCoords blockCoords1 = event.blockCoords;

        if (this.shouldRenderForBlock(blockCoords1.block.blockID)) {

            AtomicBoolean shouldAdd = new AtomicBoolean(true);

            for (int i = 0; i < this.blockCoords.size(); i++) {

                BlockCoords blockCoords2 = this.blockCoords.get(i);

                if (blockCoords2.equals(blockCoords1)) {
                    shouldAdd.set(false);
                    break;

                } else if (blockCoords2.x == blockCoords1.x) {

                    if (blockCoords2.y == blockCoords1.y) {

                        if (blockCoords2.z == blockCoords1.z) {
                            shouldAdd.set(false);
                            break;
                        }
                    }
                }
            }

            if (shouldAdd.get()) {
                this.blockCoords.add(blockCoords1);
            }
        }
    }));

    @SuppressWarnings("unused")
    EventAction<ViewBobEvent> viewBobEventAction = this.addEventAction(new EventAction<>(ViewBobEvent.class, event -> {
        if (!this.tracers.getValue().equals(TracerMode.None)) {
            event.cancel();
        }
    }));

    @SuppressWarnings("unused")
    EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(new EventAction<>(ChangeWorldEvent.class, event -> {
        this.blockCoords.clear();
        RenderStatic.tensor_playerFrustrum = null;
    }));

    @Override
    public void onClientTick() {
        ArrayList<BlockCoords> toRemove = new ArrayList<>();

        this.blockCoords.forEach(blockCoords1 -> {
            if (this.getWorld().getBlockId(blockCoords1.x, blockCoords1.y, blockCoords1.z) != blockCoords1.block.blockID) {
                toRemove.add(blockCoords1);
            } else if (this.getDistanceSqWithFreecam(blockCoords1.x, blockCoords1.y, blockCoords1.z) >= 65536) {
                toRemove.add(blockCoords1);
            }
        });

        this.blockCoords.removeAll(toRemove);
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (this.frustrum.getValue()) {
            if (RenderStatic.tensor_playerFrustrum == null) {
                RenderStatic.tensor_playerFrustrum = new Frustrum();
            }

            RenderStatic.tensor_playerFrustrum.setPosition(RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ);

            switch ((TracerMode) this.tracers.getValue()) {
                case None:
                    this.blockCoords.forEach(blockCoords1 -> {
                        if (blockCoords1.isInFrustrum(RenderStatic.tensor_playerFrustrum)) {
                            this.renderForBlock(blockCoords1, false);
                        }
                    });
                    break;
                case All:
                    this.blockCoords.forEach(blockCoords1 -> this.renderForBlock(blockCoords1, true));
                    break;
                case Portal:
                    this.blockCoords.forEach(blockCoords1 -> {
                        if (blockCoords1.block.blockID == Block.portal.blockID) {
                            this.renderForBlock(blockCoords1, true);
                        } else if (blockCoords1.isInFrustrum(RenderStatic.tensor_playerFrustrum)) {
                            this.renderForBlock(blockCoords1, false);
                        }
                    });
                    break;
                case Custom:
                    this.blockCoords.forEach(blockCoords1 -> {
                        if (this.customTracers.containsBlock(blockCoords1.block.blockID)) {
                            this.renderForBlock(blockCoords1, true);
                        } else if (blockCoords1.isInFrustrum(RenderStatic.tensor_playerFrustrum)) {
                            this.renderForBlock(blockCoords1, false);
                        }
                    });
            }

        } else {
            this.blockCoords.forEach(blockCoords1 -> this.renderForBlock(blockCoords1, false));
        }
    }

    private boolean shouldRenderForBlock(int blockID) {

        switch ((Mode) this.mode.getValue()) {
            case Glowstone:
                return blockID == Block.glowStone.blockID;
            case Portal:
                return blockID == Block.portal.blockID;
            case Chest:
                return blockID == Block.chest.blockID;
            case Diamond:
                return blockID == Block.oreDiamond.blockID || blockID == Block.blockDiamond.blockID;
            case Utility:
                return this.UTILITY.containsBlock(blockID);
            case Custom:
                return this.custom.containsBlock(blockID);
            default:
                return false;
        }
    }

    private void renderForBlock(BlockCoords blockCoords, boolean frustrum) {
        blockCoords.block.setBlockBoundsBasedOnState(this.getWorld(), blockCoords.x, blockCoords.y, blockCoords.z);

        double rx = blockCoords.x - RenderManager.renderPosX + blockCoords.block.minX;
        double ry = blockCoords.y - RenderManager.renderPosY + blockCoords.block.minY;
        double rz = blockCoords.z - RenderManager.renderPosZ + blockCoords.block.minZ;

        double dx = (blockCoords.block.maxX - blockCoords.block.minX);
        double dy = (blockCoords.block.maxY - blockCoords.block.minY);
        double dz = (blockCoords.block.maxZ - blockCoords.block.minZ);

        double mx = dx + rx;
        double my = dy + ry;
        double mz = dz + rz;

        if (blockCoords.block instanceof BlockLadder) {
            int metadata = this.getWorld().getBlockMetadata(blockCoords.x, blockCoords.y, blockCoords.z);

            if (metadata == 2) {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.9D;

                dx = 1D;
                dy = 1D;
                dz = 0.1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 3) {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 1D;
                dy = 1D;
                dz = 0.1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 4) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.9D;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 0.1D;
                dy = 1D;
                dz = 1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 5) {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 0.1D;
                dy = 1D;
                dz = 1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 1D;
                dy = 1D;
                dz = 1D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;
            }
        }

        if (blockCoords.block instanceof BlockTorch || blockCoords.block instanceof BlockRedstoneTorch) {
            int metadata = this.getWorld().getBlockMetadata(blockCoords.x, blockCoords.y, blockCoords.z);

            if (metadata == 1) {
                rx = blockCoords.x - RenderManager.renderPosX;
                ry = blockCoords.y - RenderManager.renderPosY + 0.2D;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.35D;

                dx = 0.3D;
                dy = 0.6D;
                dz = 0.3D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 2) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.7D;
                ry = blockCoords.y - RenderManager.renderPosY + 0.2D;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.35D;

                dx = 0.3D;
                dy = 0.6D;
                dz = 0.3D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 3) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.35D;
                ry = blockCoords.y - RenderManager.renderPosY + 0.2D;
                rz = blockCoords.z - RenderManager.renderPosZ;

                dx = 0.3D;
                dy = 0.6D;
                dz = 0.3D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 4) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.35D;
                ry = blockCoords.y - RenderManager.renderPosY + 0.2D;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.7D;

                dx = 0.3D;
                dy = 0.6D;
                dz = 0.3D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;

            } else if (metadata == 5) {
                rx = blockCoords.x - RenderManager.renderPosX + 0.4D;
                ry = blockCoords.y - RenderManager.renderPosY;
                rz = blockCoords.z - RenderManager.renderPosZ + 0.4D;

                dx = 0.2D;
                dy = 0.6D;
                dz = 0.2D;

                mx = dx + rx;
                my = dy + ry;
                mz = dz + rz;
            }
        }

        Color color = this.searchColor.getValue();

        if (blockCoords.block.blockID == Block.chest.blockID) {
            color = this.chestColor.getValue();
        } else if (blockCoords.block.blockID == Block.portal.blockID) {
            color = this.portalColor.getValue();
        }

        if (!this.tracers.getValue().equals(TracerMode.None)) {

            if (this.tracers.getValue().equals(TracerMode.Custom) && this.customTracers.containsBlock(blockCoords.block)) {
                this.drawTracer(rx + (dx / 2), ry + (dy / 2), rz + (dz / 2), this.tracerWidth.getValue(), color, this.tracerAlpha.getValue());
            } else if (this.tracers.getValue().equals(TracerMode.All)) {
                this.drawTracer(rx + (dx / 2), ry + (dy / 2), rz + (dz / 2), this.tracerWidth.getValue(), color, this.tracerAlpha.getValue());
            } else if (this.tracers.getValue().equals(TracerMode.Portal) && blockCoords.block.blockID == Block.portal.blockID) {
                this.drawTracer(rx + (dx / 2), ry + (dy / 2), rz + (dz / 2), this.tracerWidth.getValue(), color, this.tracerAlpha.getValue());
            }
        }

        if (frustrum && !blockCoords.isInFrustrum(RenderStatic.tensor_playerFrustrum)) {
            return;
        }

        switch ((Render) this.render.getValue()) {
            case None:
                break;
            case Outline:
                this.drawOutlineBox(rx, ry, rz, mx, my, mz, this.lineWidth.getValue(), color);
                break;
            case Fill:
                this.drawFillBox(rx, ry, rz, mx, my, mz, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(rx, ry, rz, mx, my, mz, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                this.drawOutlineBox(rx, ry, rz, mx, my, mz, this.lineWidth.getValue(), color);
                break;
        }
    }

    public void resetSearch() {
        this.blockCoords.clear();
        RenderStatic.tensor_playerFrustrum = null;
        this.resetWorld();
    }

    private enum Mode {
        Glowstone,
        Portal,
        Chest,
        Diamond,
        Utility,
        Custom
    }

    private enum TracerMode {
        None,
        All,
        Portal,
        Custom,
    }

    private enum Render {
        Outline,
        Fill,
        Both,
        None
    }
}
