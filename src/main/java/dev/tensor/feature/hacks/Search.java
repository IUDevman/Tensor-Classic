package dev.tensor.feature.hacks;

import dev.tensor.backend.events.RenderBlockEvent;
import dev.tensor.misc.block.BlockCoords;
import dev.tensor.misc.block.BlockList;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
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
 * <p>
 * TODO:
 * 1. Fill setting
 * 2. Make tile entities work?
 * 3. Make colors work
 */

@Hack.Info(name = "Search", category = Category.Render)
public final class Search extends Hack {

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Chest) {

        @Override
        public void setValue(Enum<?> value) {
            super.setValue(value);

            Search search = this.getHackManager().getHack(Search.class);

            if (!this.failsNullCheck() && search.isEnabled()) {
                search.resetSearch();
            }
        }
    };
    public final BlockListSetting custom = new BlockListSetting("Custom Block List", new BlockList(BlockList.CUSTOM())) {

        @Override
        public void hasChanged() {

            Search search = this.getHackManager().getHack(Search.class);

            if (!this.failsNullCheck() && search.isEnabled() && search.mode.getValue().equals(Mode.Custom)) {
                search.resetSearch();
            }
        }
    };
    public final EnumSetting render = new EnumSetting("Render", Render.Outline);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 100, 0, 255);
    public final ColorSetting chestColor = new ColorSetting("Chest Color", Color.ORANGE);
    public final ColorSetting searchColor = new ColorSetting("Search Color", Color.CYAN);

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

    @Override
    public void onClientTick() {
        this.blockCoords.forEach(blockCoords1 -> {

            if (this.getPlayer().getDistanceSq(blockCoords1.x, blockCoords1.y, blockCoords1.z) >= 65536) {
                this.blockCoords.remove(blockCoords1);
            }
        });
    }

    @Override
    public void onRender3D() {
        this.blockCoords.forEach(this::renderForBlock);
    }

    private boolean shouldRenderForBlock(int blockID) {

        switch ((Mode) this.mode.getValue()) {
            case Glowstone:
                return blockID == Block.glowStone.blockID;
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

        Color color = blockCoords.block.blockID == Block.chest.blockID ? this.chestColor.getValue() : this.searchColor.getValue();

        switch ((Render) this.render.getValue()) {
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
        Chest,
        Diamond,
        Utility,
        Custom
    }

    private enum Render {
        Outline,
        Fill,
        Both
    }
}
