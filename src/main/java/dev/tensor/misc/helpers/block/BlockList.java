package dev.tensor.misc.helpers.block;

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
        this.blockListDefault = new ArrayList<>(blockList);
        this.blockList = new ArrayList<>(blockList);
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

        this.blockList.remove(blockID1);
    }

    public void clearBlockList() {
        this.blockList.clear();
    }

    public void resetBlockList() {
        this.clearBlockList();
        this.blockList.addAll(this.blockListDefault);
    }

    public boolean containsBlock(Block block) {
        return this.containsBlock(block.blockID);
    }

    public boolean containsBlock(int blockID) {

        return this.blockList.contains(String.valueOf(blockID));
    }

    public void loadBlockListFromString(String string, boolean checkReal) throws Exception {
        this.clearBlockList();

        if (string.equalsIgnoreCase("null")) {
            return;
        }

        String[] pooledString = string.split("_");

        for (int i = 0; i < pooledString.length; i++) {
            int blockID = Integer.parseInt(pooledString[i]);
            this.addToBlockList(blockID, checkReal);
        }
    }

    public String saveBlockListToString() {
        if (this.blockList.isEmpty()) {
            return "null";
        }

        AtomicReference<String> list = new AtomicReference<>("");

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

        return new ArrayList<>(DIAMOND);
    }

    private static final ArrayList<String> ORES_SEARCH = new ArrayList<>();

    public static ArrayList<String> ORES_SEARCH() {
        if (ORES_SEARCH.isEmpty()) {
            ORES_SEARCH.add(String.valueOf(Block.oreCoal.blockID));
            ORES_SEARCH.add(String.valueOf(Block.oreIron.blockID));
            ORES_SEARCH.add(String.valueOf(Block.blockSteel.blockID));
            ORES_SEARCH.add(String.valueOf(Block.oreGold.blockID));
            ORES_SEARCH.add(String.valueOf(Block.blockGold.blockID));
            ORES_SEARCH.add(String.valueOf(Block.oreDiamond.blockID));
            ORES_SEARCH.add(String.valueOf(Block.blockDiamond.blockID));
            ORES_SEARCH.add(String.valueOf(Block.oreRedstone.blockID));
            ORES_SEARCH.add(String.valueOf(Block.oreRedstoneGlowing.blockID));
            ORES_SEARCH.add(String.valueOf(Block.oreLapis.blockID));
            ORES_SEARCH.add(String.valueOf(Block.blockLapis.blockID));
        }

        return new ArrayList<>(ORES_SEARCH);
    }


    private static final ArrayList<String> ORES = new ArrayList<>();

    public static ArrayList<String> ORES() {
        if (ORES.isEmpty()) {
            ORES.add(String.valueOf(Block.waterStill.blockID));
            ORES.add(String.valueOf(Block.waterMoving.blockID));
            ORES.add(String.valueOf(Block.lavaStill.blockID));
            ORES.add(String.valueOf(Block.lavaMoving.blockID));

            ORES.addAll(ORES_SEARCH());
        }

        return new ArrayList<>(ORES);
    }

    private static final ArrayList<String> GLOWSTONE = new ArrayList<>();

    public static ArrayList<String> GLOWSTONE() {
        if (GLOWSTONE.isEmpty()) {
            GLOWSTONE.add(String.valueOf(Block.glowStone.blockID));
        }

        return new ArrayList<>(GLOWSTONE);
    }

    private static final ArrayList<String> UTILITY_SEARCH = new ArrayList<>();

    public static ArrayList<String> UTILITY_SEARCH() {
        if (UTILITY_SEARCH.isEmpty()) {
            UTILITY_SEARCH.add(String.valueOf(Block.blockSteel.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.blockGold.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.blockDiamond.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.blockLapis.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.dispenser.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.musicBlock.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.blockBed.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.tnt.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.mobSpawner.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.chest.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.workbench.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.stoneOvenIdle.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.stoneOvenActive.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.jukebox.blockID));
            UTILITY_SEARCH.add(String.valueOf(Block.portal.blockID));
        }

        return new ArrayList<>(UTILITY_SEARCH);
    }

    private static final ArrayList<String> UTILITY = new ArrayList<>();

    public static ArrayList<String> UTILITY() {
        if (UTILITY.isEmpty()) {
            UTILITY.add(String.valueOf(Block.sapling.blockID));
            UTILITY.add(String.valueOf(Block.waterStill.blockID));
            UTILITY.add(String.valueOf(Block.waterMoving.blockID));
            UTILITY.add(String.valueOf(Block.lavaStill.blockID));
            UTILITY.add(String.valueOf(Block.lavaMoving.blockID));
            UTILITY.add(String.valueOf(Block.glowStone.blockID));
            UTILITY.add(String.valueOf(Block.sponge.blockID));
            UTILITY.add(String.valueOf(Block.glass.blockID));
            UTILITY.add(String.valueOf(Block.railPowered.blockID));
            UTILITY.add(String.valueOf(Block.railDetector.blockID));
            UTILITY.add(String.valueOf(Block.pistonStickyBase.blockID));
            UTILITY.add(String.valueOf(Block.web.blockID));
            UTILITY.add(String.valueOf(Block.pistonBase.blockID));
            UTILITY.add(String.valueOf(Block.pistonExtension.blockID));
            UTILITY.add(String.valueOf(Block.cloth.blockID));
            UTILITY.add(String.valueOf(Block.pistonMoving.blockID));
            UTILITY.add(String.valueOf(Block.obsidian.blockID));
            UTILITY.add(String.valueOf(Block.torchWood.blockID));
            UTILITY.add(String.valueOf(Block.fire.blockID));
            UTILITY.add(String.valueOf(Block.redstoneWire.blockID));
            UTILITY.add(String.valueOf(Block.crops.blockID));
            UTILITY.add(String.valueOf(Block.tilledField.blockID));
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
            UTILITY.add(String.valueOf(Block.pumpkin.blockID));
            UTILITY.add(String.valueOf(Block.pumpkinLantern.blockID));
            UTILITY.add(String.valueOf(Block.cake.blockID));
            UTILITY.add(String.valueOf(Block.redstoneRepeaterIdle.blockID));
            UTILITY.add(String.valueOf(Block.redstoneRepeaterActive.blockID));
            UTILITY.add(String.valueOf(Block.trapdoor.blockID));

            UTILITY.addAll(UTILITY_SEARCH());
        }

        return new ArrayList<>(UTILITY);
    }

    private static final ArrayList<String> BREAK_INSTANT = new ArrayList<>();

    public static ArrayList<String> BREAK_INSTANT() {
        if (BREAK_INSTANT.isEmpty()) {
            BREAK_INSTANT.add(String.valueOf(Block.sapling.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.deadBush.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.tallGrass.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.plantRed.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.plantYellow.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.mushroomRed.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.mushroomBrown.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.torchWood.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.fire.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.crops.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.torchRedstoneActive.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.torchRedstoneIdle.blockID));
            BREAK_INSTANT.add(String.valueOf(Block.reed.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.plantRed.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.plantYellow.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.mushroomRed.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.mushroomBrown.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.fire.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.crops.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.lever.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.pressurePlatePlanks.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.pressurePlateStone.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.doorSteel.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.doorWood.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.trapdoor.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.cactus.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.reed.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.cake.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.portal.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.button.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.fence.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.ladder.blockID));
        }

        return new ArrayList<>(BREAK_INSTANT);
    }

    private static final ArrayList<String> NUKER_BLOCKLIST = new ArrayList<>();

    public static ArrayList<String> NUKER_BLOCKLIST() {
        if (NUKER_BLOCKLIST.isEmpty()) {
            NUKER_BLOCKLIST.add(String.valueOf(Block.bedrock.blockID));
            NUKER_BLOCKLIST.add(String.valueOf(Block.waterStill.blockID));
            NUKER_BLOCKLIST.add(String.valueOf(Block.waterMoving.blockID));
            NUKER_BLOCKLIST.add(String.valueOf(Block.lavaStill.blockID));
            NUKER_BLOCKLIST.add(String.valueOf(Block.lavaMoving.blockID));
            NUKER_BLOCKLIST.add(String.valueOf(Block.portal.blockID));
        }

        return new ArrayList<>(NUKER_BLOCKLIST);
    }

    private static final ArrayList<String> SCAFFOLD_BLOCKLIST = new ArrayList<>();

    public static ArrayList<String> SCAFFOLD_BLOCKLIST() {
        if (SCAFFOLD_BLOCKLIST.isEmpty()) {
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.waterStill.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.waterMoving.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.lavaStill.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.lavaMoving.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.torchWood.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.torchRedstoneIdle.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.torchRedstoneActive.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.sapling.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.blockBed.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.rail.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.railDetector.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.railPowered.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.web.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.tallGrass.blockID));
            SCAFFOLD_BLOCKLIST.add(String.valueOf(Block.deadBush.blockID));
        }

        return new ArrayList<>(SCAFFOLD_BLOCKLIST);
    }

    private static final ArrayList<String> CUSTOM = new ArrayList<>();

    public static ArrayList<String> CUSTOM() {
        return new ArrayList<>(CUSTOM);
    }
}
