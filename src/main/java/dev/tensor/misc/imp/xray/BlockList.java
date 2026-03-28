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

    private final ArrayList<String> blockListDefault;
    private final ArrayList<String> blockList;

    public BlockList(ArrayList<String> blockList) {
        this.blockListDefault = blockList;
        this.blockList = blockList;
    }

    public void addToBlockList(Block block) {
        this.addToBlockList(block.blockID, false);
    }

    public void addToBlockList(int blockID, boolean checkReal) {

        String blockID1 = String.valueOf(blockID);

        boolean isReal = false;

        if (!checkReal) {
            isReal = true;
        } else if (Block.blocksList[blockID] != null) {
            isReal = true;
        }

        if (isReal && !this.blockList.contains(blockID1)) {
            this.blockList.add(blockID1);
        }
    }

    public void removeFromBlockList(Block block) {
        this.removeFromBlockList(block.blockID);
    }

    public void removeFromBlockList(int blockID) {
        String blockID1 = String.valueOf(blockID);

        if (this.blockList.contains(blockID1)) {
            this.blockList.remove(blockID1);
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

        return this.blockList.contains(String.valueOf(blockID));
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

        this.blockList.forEach(string -> list.set(list.get() + string + "_"));
        list.set(list.get().substring(0, list.get().length() - 1));

        return list.get();
    }

    private static final ArrayList<String> DIAMOND = new ArrayList<>();

    public static ArrayList<String> DIAMOND() {
        if (DIAMOND.isEmpty()) {
            DIAMOND.add(String.valueOf(Block.oreDiamond.blockID));
            DIAMOND.add(String.valueOf(Block.blockDiamond.blockID));
            DIAMOND.add(String.valueOf(Block.waterStill.blockID));
            DIAMOND.add(String.valueOf(Block.waterMoving.blockID));
            DIAMOND.add(String.valueOf(Block.lavaStill.blockID));
            DIAMOND.add(String.valueOf(Block.lavaMoving.blockID));
        }

        return DIAMOND;
    }

    private static final ArrayList<String> ORES = new ArrayList<>();

    public static ArrayList<String> ORES() {
        if (ORES.isEmpty()) {
            ORES.add(String.valueOf(Block.oreCoal.blockID));
            ORES.add(String.valueOf(Block.oreIron.blockID));
            ORES.add(String.valueOf(Block.blockSteel.blockID));
            ORES.add(String.valueOf(Block.oreGold.blockID));
            ORES.add(String.valueOf(Block.blockGold.blockID));
            ORES.add(String.valueOf(Block.oreDiamond.blockID));
            ORES.add(String.valueOf(Block.blockDiamond.blockID));
            ORES.add(String.valueOf(Block.oreRedstone.blockID));
            ORES.add(String.valueOf(Block.oreRedstoneGlowing.blockID));
            ORES.add(String.valueOf(Block.oreLapis.blockID));
            ORES.add(String.valueOf(Block.blockLapis.blockID));
            ORES.add(String.valueOf(Block.waterStill.blockID));
            ORES.add(String.valueOf(Block.waterMoving.blockID));
            ORES.add(String.valueOf(Block.lavaStill.blockID));
            ORES.add(String.valueOf(Block.lavaMoving.blockID));
        }

        return ORES;
    }

    private static final ArrayList<String> GLOWSTONE = new ArrayList<>();

    public static ArrayList<String> GLOWSTONE() {
        if (GLOWSTONE.isEmpty()) {
            GLOWSTONE.add(String.valueOf(Block.glowStone.blockID));
        }

        return GLOWSTONE;
    }

    private static final ArrayList<String> UTILITY = new ArrayList<>();

    public static ArrayList<String> UTILITY() {
        if (UTILITY.isEmpty()) {
            UTILITY.add(String.valueOf(Block.sapling.blockID));
            UTILITY.add(String.valueOf(Block.waterStill.blockID));
            UTILITY.add(String.valueOf(Block.waterMoving.blockID));
            UTILITY.add(String.valueOf(Block.lavaStill.blockID));
            UTILITY.add(String.valueOf(Block.lavaMoving.blockID));
            UTILITY.add(String.valueOf(Block.blockSteel.blockID));
            UTILITY.add(String.valueOf(Block.blockGold.blockID));
            UTILITY.add(String.valueOf(Block.blockDiamond.blockID));
            UTILITY.add(String.valueOf(Block.glowStone.blockID));
            UTILITY.add(String.valueOf(Block.sponge.blockID));
            UTILITY.add(String.valueOf(Block.glass.blockID));
            UTILITY.add(String.valueOf(Block.dispenser.blockID));
            UTILITY.add(String.valueOf(Block.musicBlock.blockID));
            UTILITY.add(String.valueOf(Block.blockBed.blockID));
            UTILITY.add(String.valueOf(Block.railPowered.blockID));
            UTILITY.add(String.valueOf(Block.railDetector.blockID));
            UTILITY.add(String.valueOf(Block.pistonStickyBase.blockID));
            UTILITY.add(String.valueOf(Block.web.blockID));
            UTILITY.add(String.valueOf(Block.pistonBase.blockID));
            UTILITY.add(String.valueOf(Block.pistonExtension.blockID));
            UTILITY.add(String.valueOf(Block.cloth.blockID));
            UTILITY.add(String.valueOf(Block.pistonMoving.blockID));
            UTILITY.add(String.valueOf(Block.tnt.blockID));
            UTILITY.add(String.valueOf(Block.obsidian.blockID));
            UTILITY.add(String.valueOf(Block.torchWood.blockID));
            UTILITY.add(String.valueOf(Block.fire.blockID));
            UTILITY.add(String.valueOf(Block.mobSpawner.blockID));
            UTILITY.add(String.valueOf(Block.chest.blockID));
            UTILITY.add(String.valueOf(Block.redstoneWire.blockID));
            UTILITY.add(String.valueOf(Block.workbench.blockID));
            UTILITY.add(String.valueOf(Block.crops.blockID));
            UTILITY.add(String.valueOf(Block.tilledField.blockID));
            UTILITY.add(String.valueOf(Block.stoneOvenIdle.blockID));
            UTILITY.add(String.valueOf(Block.stoneOvenActive.blockID));
            UTILITY.add(String.valueOf(Block.signPost.blockID));
            UTILITY.add(String.valueOf(Block.doorWood.blockID));
            UTILITY.add(String.valueOf(Block.ladder.blockID));
            UTILITY.add(String.valueOf(Block.rail.blockID));
            UTILITY.add(String.valueOf(Block.signWall.blockID));
            UTILITY.add(String.valueOf(Block.lever.blockID));
            UTILITY.add(String.valueOf(Block.pressurePlateStone.blockID));
            UTILITY.add(String.valueOf(Block.doorSteel.blockID));
            UTILITY.add(String.valueOf(Block.pressurePlatePlanks.blockID));
            UTILITY.add(String.valueOf(Block.torchRedstoneIdle.blockID));
            UTILITY.add(String.valueOf(Block.torchRedstoneActive.blockID));
            UTILITY.add(String.valueOf(Block.button.blockID));
            UTILITY.add(String.valueOf(Block.cactus.blockID));
            UTILITY.add(String.valueOf(Block.reed.blockID));
            UTILITY.add(String.valueOf(Block.jukebox.blockID));
            UTILITY.add(String.valueOf(Block.pumpkin.blockID));
            UTILITY.add(String.valueOf(Block.portal.blockID));
            UTILITY.add(String.valueOf(Block.pumpkinLantern.blockID));
            UTILITY.add(String.valueOf(Block.cake.blockID));
            UTILITY.add(String.valueOf(Block.redstoneRepeaterIdle.blockID));
            UTILITY.add(String.valueOf(Block.redstoneRepeaterActive.blockID));
            UTILITY.add(String.valueOf(Block.trapdoor.blockID));
        }

        return UTILITY;
    }

    private static final ArrayList<String> CUSTOM = new ArrayList<>();

    public static ArrayList<String> CUSTOM() {
        return CUSTOM;
    }
}
