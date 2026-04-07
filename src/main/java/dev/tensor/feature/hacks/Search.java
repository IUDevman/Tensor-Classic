package dev.tensor.feature.hacks;

import dev.tensor.backend.events.RenderBlockEvent;
import dev.tensor.backend.events.ViewBobEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.helpers.block.BlockCoords;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.BlockListSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Block;
import net.minecraft.src.RenderManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hoosiers
 * @since 03-29-2026
 */

@Hack.Info(name = "Search", category = Category.Render)
public final class Search extends Hack implements Render3DObject {

    @Override
    public int getRenderOrder() {
        return RenderOrder.SEARCH;
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
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onRenderBlockEvent(RenderBlockEvent event) {

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
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onViewBobEvent(ViewBobEvent event) {
        if (!this.tracers.getValue().equals(TracerMode.None)) {
            event.cancel();
        }
    }

    @Override
    public void onClientTick() {
        this.blockCoords.forEach(blockCoords1 -> {
            if (this.getWorld().getBlockId(blockCoords1.x, blockCoords1.y, blockCoords1.z) != blockCoords1.block.blockID) {
                this.blockCoords.remove(blockCoords1);
            } else if (this.getPlayer().getDistanceSq(blockCoords1.x, blockCoords1.y, blockCoords1.z) >= 65536) {
                this.blockCoords.remove(blockCoords1);
            }
        });
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        this.blockCoords.forEach(this::renderForBlock);
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

    private void renderForBlock(BlockCoords blockCoords) {
        double rx = blockCoords.x - RenderManager.renderPosX;
        double ry = blockCoords.y - RenderManager.renderPosY;
        double rz = blockCoords.z - RenderManager.renderPosZ;

        Color color = this.searchColor.getValue();

        if (blockCoords.block.blockID == Block.chest.blockID) {
            color = this.chestColor.getValue();
        } else if (blockCoords.block.blockID == Block.portal.blockID) {
            color = this.portalColor.getValue();
        }

        if (!this.tracers.getValue().equals(TracerMode.None)) {

            if (this.tracers.getValue().equals(TracerMode.Custom) && this.customTracers.containsBlock(blockCoords.block)) {
                this.drawTracer(rx + 0.5, ry + 0.5, rz + 0.5, this.tracerWidth.getValue(), color, this.tracerAlpha.getValue());
            } else if (this.tracers.getValue().equals(TracerMode.All)) {
                this.drawTracer(rx + 0.5, ry + 0.5, rz + 0.5, this.tracerWidth.getValue(), color, this.tracerAlpha.getValue());
            } else if (this.tracers.getValue().equals(TracerMode.Portal) && blockCoords.block.blockID == Block.portal.blockID) {
                this.drawTracer(rx + 0.5, ry + 0.5, rz + 0.5, this.tracerWidth.getValue(), color, this.tracerAlpha.getValue());
            }
        }

        switch ((Render) this.render.getValue()) {
            case None:
                break;
            case Outline:
                this.drawOutlineBox(rx, ry, rz, 1, this.lineWidth.getValue(), color);
                break;
            case Fill:
                this.drawFillBox(rx, ry, rz, 1, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(rx, ry, rz, 1, new Color(color.getRed(), color.getGreen(), color.getBlue(), this.fillAlpha.getValue()));
                this.drawOutlineBox(rx, ry, rz, 1, this.lineWidth.getValue(), color);
                break;
        }
    }

    public void resetSearch() {
        this.blockCoords.clear();
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
