package dev.tensor.misc.imp.xray;

import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Block;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 03-27-2026
 */

public final class BlockList implements Globals {

    private final ArrayList<Integer> blockListDefault;
    private final ArrayList<Integer> blockList;

    public BlockList(ArrayList<Integer> blockList) {
        this.blockListDefault = blockList;
        this.blockList = blockList;
    }

    public ArrayList<Integer> getBlockList() {
        return this.blockList;
    }

    public void addToBlockList(Block block) {
        this.addToBlockList(block.blockID, false);
    }

    public void addToBlockList(int blockID, boolean checkReal) {

        boolean isReal = false;

        if (!checkReal) {
            isReal = true;
        } else if (Block.blocksList[blockID] != null) {
            isReal = true;
        }

        if (isReal && !this.blockList.contains(blockID)) {
            this.blockList.add(blockID);
        }
    }

    public void removeFromBlockList(Block block) {
        this.removeFromBlockList(block.blockID);
    }

    public void removeFromBlockList(int blockID) {
        if (this.blockList.contains(blockID)) {
            this.blockList.remove(blockID);
        }
    }

    public void clearBlockList() {
        this.blockList.clear();
    }

    public void resetBlockList() {
        this.clearBlockList();
        this.blockList.addAll(this.blockListDefault);
    }

    public boolean containsBlock(Block block, boolean opacity)  {
        return this.containsBlock(block.blockID, opacity);
    }

    public boolean containsBlock(int blockID, boolean opacity) {

        if (opacity) {

            if (blockID == Block.waterStill.blockID || blockID == Block.waterMoving.blockID || blockID == Block.lavaStill.blockID || blockID == Block.lavaMoving.blockID) {
                return false;
            }
        }

        return this.blockList.contains(blockID);
    }

    public void loadBlockListFromString(String string, boolean checkReal) throws Exception {
        this.clearBlockList();

        String[] pooledString = string.split("_");

        for (int i = 0; i < pooledString.length; i++) {
            int blockID  = Integer.parseInt(pooledString[i]);
            this.addToBlockList(blockID, checkReal);
        }
    }

    public String saveBlockListToString() {
        AtomicReference<String> list = new AtomicReference<>("");

        if (this.blockList.isEmpty()) {
            return "null";
        }

        this.blockList.forEach(integer -> list.set(list.get() + integer + "_"));
        list.set(list.get().substring(0, list.get().length() - 1));

        return list.get();
    }

    private static final ArrayList<Integer> DIAMOND = new ArrayList<>();

    public static ArrayList<Integer> DIAMOND() {
        if (DIAMOND.isEmpty()) {
            DIAMOND.add(Block.oreDiamond.blockID);
            DIAMOND.add(Block.blockDiamond.blockID);
            DIAMOND.add(Block.waterStill.blockID);
            DIAMOND.add(Block.waterMoving.blockID);
            DIAMOND.add(Block.lavaStill.blockID);
            DIAMOND.add(Block.lavaMoving.blockID);
        }

        return DIAMOND;
    }

    private static final ArrayList<Integer> ORES = new ArrayList<>();

    public static ArrayList<Integer> ORES() {
        if (ORES.isEmpty()) {
            ORES.add(Block.oreCoal.blockID);
            ORES.add(Block.oreIron.blockID);
            ORES.add(Block.blockSteel.blockID);
            ORES.add(Block.oreGold.blockID);
            ORES.add(Block.blockGold.blockID);
            ORES.add(Block.oreDiamond.blockID);
            ORES.add(Block.blockDiamond.blockID);
            ORES.add(Block.oreRedstone.blockID);
            ORES.add(Block.oreRedstoneGlowing.blockID);
            ORES.add(Block.oreLapis.blockID);
            ORES.add(Block.blockLapis.blockID);
            ORES.add(Block.waterStill.blockID);
            ORES.add(Block.waterMoving.blockID);
            ORES.add(Block.lavaStill.blockID);
            ORES.add(Block.lavaMoving.blockID);
        }

        return ORES;
    }

    private static final ArrayList<Integer> GLOWSTONE = new ArrayList<>();

    public static ArrayList<Integer> GLOWSTONE() {
        if (GLOWSTONE.isEmpty()) {
            GLOWSTONE.add(Block.glowStone.blockID);
        }

        return GLOWSTONE;
    }

    private static final ArrayList<Integer> CUSTOM = new ArrayList<>();

    public static ArrayList<Integer> CUSTOM() {
        if (CUSTOM.isEmpty()) {
            CUSTOM.add(Block.sapling.blockID);
            CUSTOM.add(Block.waterStill.blockID);
            CUSTOM.add(Block.waterMoving.blockID);
            CUSTOM.add(Block.lavaStill.blockID);
            CUSTOM.add(Block.lavaMoving.blockID);
            CUSTOM.add(Block.oreCoal.blockID);
            CUSTOM.add(Block.oreIron.blockID);
            CUSTOM.add(Block.blockSteel.blockID);
            CUSTOM.add(Block.oreGold.blockID);
            CUSTOM.add(Block.blockGold.blockID);
            CUSTOM.add(Block.oreDiamond.blockID);
            CUSTOM.add(Block.blockDiamond.blockID);
            CUSTOM.add(Block.oreRedstone.blockID);
            CUSTOM.add(Block.oreRedstoneGlowing.blockID);
            CUSTOM.add(Block.oreLapis.blockID);
            CUSTOM.add(Block.glowStone.blockID);
            CUSTOM.add(Block.sponge.blockID);
            CUSTOM.add(Block.glass.blockID);
            CUSTOM.add(Block.dispenser.blockID);
            CUSTOM.add(Block.musicBlock.blockID);
            CUSTOM.add(Block.blockBed.blockID);
            CUSTOM.add(Block.railPowered.blockID);
            CUSTOM.add(Block.railDetector.blockID);
            CUSTOM.add(Block.pistonStickyBase.blockID);
            CUSTOM.add(Block.web.blockID);
            CUSTOM.add(Block.pistonBase.blockID);
            CUSTOM.add(Block.pistonExtension.blockID);
            CUSTOM.add(Block.cloth.blockID);
            CUSTOM.add(Block.pistonMoving.blockID);
            CUSTOM.add(Block.tnt.blockID);
            CUSTOM.add(Block.obsidian.blockID);
            CUSTOM.add(Block.torchWood.blockID);
            CUSTOM.add(Block.fire.blockID);
            CUSTOM.add(Block.mobSpawner.blockID);
            CUSTOM.add(Block.chest.blockID);
            CUSTOM.add(Block.redstoneWire.blockID);
            CUSTOM.add(Block.workbench.blockID);
            CUSTOM.add(Block.crops.blockID);
            CUSTOM.add(Block.tilledField.blockID);
            CUSTOM.add(Block.stoneOvenIdle.blockID);
            CUSTOM.add(Block.stoneOvenActive.blockID);
            CUSTOM.add(Block.signPost.blockID);
            CUSTOM.add(Block.doorWood.blockID);
            CUSTOM.add(Block.ladder.blockID);
            CUSTOM.add(Block.rail.blockID);
            CUSTOM.add(Block.signWall.blockID);
            CUSTOM.add(Block.lever.blockID);
            CUSTOM.add(Block.pressurePlateStone.blockID);
            CUSTOM.add(Block.doorSteel.blockID);
            CUSTOM.add(Block.pressurePlatePlanks.blockID);
            CUSTOM.add(Block.torchRedstoneIdle.blockID);
            CUSTOM.add(Block.torchRedstoneActive.blockID);
            CUSTOM.add(Block.button.blockID);
            CUSTOM.add(Block.cactus.blockID);
            CUSTOM.add(Block.reed.blockID);
            CUSTOM.add(Block.jukebox.blockID);
            CUSTOM.add(Block.pumpkin.blockID);
            CUSTOM.add(Block.portal.blockID);
            CUSTOM.add(Block.pumpkinLantern.blockID);
            CUSTOM.add(Block.cake.blockID);
            CUSTOM.add(Block.redstoneRepeaterIdle.blockID);
            CUSTOM.add(Block.redstoneRepeaterActive.blockID);
            CUSTOM.add(Block.trapdoor.blockID);
        }

        return CUSTOM;
    }
}
