package dev.tensor.feature.hacks;

import dev.tensor.backend.events.RenderBlockEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.block.BlockCoords;
import dev.tensor.misc.block.BlockList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BlockListSetting;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Hoosiers
 * @since 03-29-2026
 *
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
    public final ColorSetting chestColor = new ColorSetting("Chest Color", Color.ORANGE);
    public final ColorSetting searchColor = new ColorSetting("Search Color", Color.CYAN);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 100, 0, 255);

    private final BlockList ORES = new BlockList(BlockList.ORES_SEARCH());
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
        this.startBlockGL();

        this.blockCoords.forEach(blockCoords1 -> {

            //Todo: move to own method - renders outlines
            double rx = blockCoords1.x - RenderManager.renderPosX;
            double ry = blockCoords1.y - RenderManager.renderPosY;
            double rz = blockCoords1.z - RenderManager.renderPosZ;

            GL11.glLineWidth(lineWidth.getValue());
            GL11.glColor4f(this.chestColor.getValue().getRed() / 255F, this.chestColor.getValue().getGreen() / 255F, this.chestColor.getValue().getBlue() / 255F, this.chestColor.getValue().getAlpha() / 255F);

            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(rx, ry, rz, rx + 1, ry + 1, rz + 1);

            Tessellator.instance.startDrawing(GL11.GL_LINE_STRIP);
            Tessellator.instance.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            Tessellator.instance.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            Tessellator.instance.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            Tessellator.instance.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            Tessellator.instance.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawing(GL11.GL_LINE_STRIP);
            Tessellator.instance.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            Tessellator.instance.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            Tessellator.instance.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            Tessellator.instance.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            Tessellator.instance.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawing(GL11.GL_LINES);
            Tessellator.instance.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            Tessellator.instance.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            Tessellator.instance.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            Tessellator.instance.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            Tessellator.instance.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            Tessellator.instance.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            Tessellator.instance.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            Tessellator.instance.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            Tessellator.instance.draw();
        });

        this.finishBlockGL();
    }

    private boolean shouldRenderForBlock(int blockID) {

        switch ((Mode) this.mode.getValue()) {
            case Ores:
                return this.ORES.containsBlock(blockID);
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

    private void startBlockGL() {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDepthMask(false);
    }

    private void finishBlockGL() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    /*
    private void renderForBlock(Block block) {

        AxisAlignedBB axisAlignedBB = AxisAlignedBB.getBoundingBox(block.minX, block.minY, block.minZ, block.maxX, block.maxY, block.maxZ);

        Color color = block.blockID == Block.chest.blockID ? this.chestColor.getValue() : this.searchColor.getValue();


        switch ((Render) this.render.getValue()) {
            case Outline:
                this.renderBlockBoundingBox(axisAlignedBB, color, this.lineWidth.getValue());
                break;
            case Fill:
                this.renderBlockShadedBox(axisAlignedBB, color, this.fillAlpha.getValue());
                break;
            case Both:
                this.renderBlockShadedBox(axisAlignedBB, color, this.fillAlpha.getValue());
                this.renderBlockBoundingBox(axisAlignedBB, color, this.lineWidth.getValue());
                break;
        }
    }

    private void renderBlockBoundingBox(AxisAlignedBB aa, Color color, int lineWidth) {

    }

    private void renderBlockShadedBox(AxisAlignedBB bb, Color color, int opacityDrop) {

    }
     */

    public void resetSearch() {
        this.blockCoords.clear();
        this.resetWorld();
    }

    private enum Mode {
        Ores,
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
