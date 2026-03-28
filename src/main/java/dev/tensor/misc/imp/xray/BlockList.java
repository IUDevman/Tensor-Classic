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

    private static final ArrayList<Integer> UTILITY = new ArrayList<>();

    public static ArrayList<Integer> UTILITY() {
        if (UTILITY.isEmpty()) {
            UTILITY.add(Block.sapling.blockID);
            UTILITY.add(Block.waterStill.blockID);
            UTILITY.add(Block.waterMoving.blockID);
            UTILITY.add(Block.lavaStill.blockID);
            UTILITY.add(Block.lavaMoving.blockID);
            UTILITY.add(Block.blockSteel.blockID);
            UTILITY.add(Block.blockGold.blockID);
            UTILITY.add(Block.blockDiamond.blockID);
            UTILITY.add(Block.glowStone.blockID);
            UTILITY.add(Block.sponge.blockID);
            UTILITY.add(Block.glass.blockID);
            UTILITY.add(Block.dispenser.blockID);
            UTILITY.add(Block.musicBlock.blockID);
            UTILITY.add(Block.blockBed.blockID);
            UTILITY.add(Block.railPowered.blockID);
            UTILITY.add(Block.railDetector.blockID);
            UTILITY.add(Block.pistonStickyBase.blockID);
            UTILITY.add(Block.web.blockID);
            UTILITY.add(Block.pistonBase.blockID);
            UTILITY.add(Block.pistonExtension.blockID);
            UTILITY.add(Block.cloth.blockID);
            UTILITY.add(Block.pistonMoving.blockID);
            UTILITY.add(Block.tnt.blockID);
            UTILITY.add(Block.obsidian.blockID);
            UTILITY.add(Block.torchWood.blockID);
            UTILITY.add(Block.fire.blockID);
            UTILITY.add(Block.mobSpawner.blockID);
            UTILITY.add(Block.chest.blockID);
            UTILITY.add(Block.redstoneWire.blockID);
            UTILITY.add(Block.workbench.blockID);
            UTILITY.add(Block.crops.blockID);
            UTILITY.add(Block.tilledField.blockID);
            UTILITY.add(Block.stoneOvenIdle.blockID);
            UTILITY.add(Block.stoneOvenActive.blockID);
            UTILITY.add(Block.signPost.blockID);
            UTILITY.add(Block.doorWood.blockID);
            UTILITY.add(Block.ladder.blockID);
            UTILITY.add(Block.rail.blockID);
            UTILITY.add(Block.signWall.blockID);
            UTILITY.add(Block.lever.blockID);
            UTILITY.add(Block.pressurePlateStone.blockID);
            UTILITY.add(Block.doorSteel.blockID);
            UTILITY.add(Block.pressurePlatePlanks.blockID);
            UTILITY.add(Block.torchRedstoneIdle.blockID);
            UTILITY.add(Block.torchRedstoneActive.blockID);
            UTILITY.add(Block.button.blockID);
            UTILITY.add(Block.cactus.blockID);
            UTILITY.add(Block.reed.blockID);
            UTILITY.add(Block.jukebox.blockID);
            UTILITY.add(Block.pumpkin.blockID);
            UTILITY.add(Block.portal.blockID);
            UTILITY.add(Block.pumpkinLantern.blockID);
            UTILITY.add(Block.cake.blockID);
            UTILITY.add(Block.redstoneRepeaterIdle.blockID);
            UTILITY.add(Block.redstoneRepeaterActive.blockID);
            UTILITY.add(Block.trapdoor.blockID);
        }

        return UTILITY;
    }

    private static final ArrayList<Integer> CUSTOM = new ArrayList<>();

    public static ArrayList<Integer> CUSTOM() {
        return CUSTOM;
    }
}
