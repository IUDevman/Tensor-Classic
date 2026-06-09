package dev.tensor.misc.helpers.block;

import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.render.RenderStatic;
import net.minecraft.src.Block;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 03-29-2026
 */

public final class BlockCoords implements Globals {

    public final Block block;
    public final int x;
    public final int y;
    public final int z;
    private final int[] quickFrustrumBox;
    private final AtomicReference<boolean[]> neighborMap = new AtomicReference<>(new boolean[]{false, false, false, false, false, false});

    public BlockCoords(Block block, int x, int y, int z) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;

        this.quickFrustrumBox = new int[]{x + 1, y + 1, z + 1};
    }

    public BlockCoords(Block block, int x, int y, int z, boolean findNeighbors) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;

        this.quickFrustrumBox = new int[]{x + 1, y + 1, z + 1};

        if (findNeighbors) {
            this.neighborMap.set(this.loadNeighborMap(x, y, z));
        }
    }

    public BlockCoords(boolean nuker, Block block, int x, int y, int z) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;

        this.quickFrustrumBox = new int[]{x + 1, y + 1, z + 1};

        if (nuker) {
            this.neighborMap.set(new boolean[]{true, true, true, true, true, true});
        }
    }

    private boolean[] loadNeighborMap(int x, int y, int z) {
        boolean[] returnResult = new boolean[6];

        if (this.isThisADudBlock()) {
            return new boolean[]{true, true, true, true, true, true};
        }

        int metadata = this.getWorld().getBlockMetadata(x, y, z);

        int[][] neighbors = this.neighborMap(x, y, z);
        for (int i = 0; i < neighbors.length; i++) {
            int[] neighbor = neighbors[i];

            returnResult[i] = this.shouldDrawFace(metadata, i, neighbor, this.getWorld().getBlockId(neighbor[0], neighbor[1], neighbor[2]));
        }

        return this.adjustForID(returnResult);
    }

    private boolean[] loadNeighborMap(int x, int y, int z, ArrayList<BlockCoords> list) {
        boolean[] returnResult = new boolean[6];

        if (this.isThisADudBlock()) {
            return new boolean[]{true, true, true, true, true, true};
        }

        int metadata = this.getWorld().getBlockMetadata(x, y, z);

        int[][] neighbors = this.neighborMap(x, y, z);
        for (int i = 0; i < neighbors.length; i++) {
            int[] neighbor = neighbors[i];

            BlockCoords blockCoords = list.stream().filter(blockCoords1 -> this.hasSameCoordinates(neighbor[0], neighbor[1], neighbor[2], blockCoords1)).findFirst().orElse(null);

            if (blockCoords == null) {
                returnResult[i] = true;
                continue;
            }

            returnResult[i] = this.shouldDrawFace(metadata, i, neighbor, blockCoords.block.blockID);
        }

        return this.adjustForID(returnResult);
    }

    private boolean shouldDrawFace(int selfMetadata, int i, int[] neighbor, int neighborBlockId) {
        boolean face = neighborBlockId != this.block.blockID;

        if (!face && this.block.blockID == Block.ladder.blockID) {
            int neighborMetadata = this.getWorld().getBlockMetadata(neighbor[0], neighbor[1], neighbor[2]);

            if (neighborMetadata != selfMetadata) {
                face = true;
            }

        } else if (!face && this.block.blockID == Block.trapdoor.blockID) {
            int neighborMetadata = this.getWorld().getBlockMetadata(neighbor[0], neighbor[1], neighbor[2]);

            if (selfMetadata >= 4 && neighborMetadata < 4) {
                face = true;

            } else if (selfMetadata < 4 && neighborMetadata >= 4) {
                face = true;

            } else if (selfMetadata != neighborMetadata && (i == 2 || i == 3)) {
                face = true;

            } else if (selfMetadata == 4 && neighborMetadata > 4) {
                face = true;

            } else if (neighborMetadata == 4 && selfMetadata > 4) {
                face = true;
            } else if (selfMetadata < 4 && selfMetadata == neighborMetadata && (i == 2 || i == 3)) {
                face = true;
            }

        } else if (!face && (this.block.blockID == Block.doorWood.blockID || this.block.blockID == Block.doorSteel.blockID)) {
            if (i != 2 && i != 3) {
                int neighborMetadata = this.getWorld().getBlockMetadata(neighbor[0], neighbor[1], neighbor[2]);

                int testMetadata = selfMetadata;

                if (testMetadata >= 8) {
                    testMetadata -= 8;
                }

                if (neighborMetadata >= 8) {
                    neighborMetadata -= 8;
                }

                if (testMetadata != neighborMetadata && testMetadata + 3 != neighborMetadata && testMetadata - 3 != neighborMetadata) {
                    face = true;
                }
            }
        }

        return face;
    }

    private int[][] neighborMap(int x, int y, int z) {
        return new int[][]{
                {x - 1, y, z},
                {x + 1, y, z},
                {x, y - 1, z},
                {x, y + 1, z},
                {x, y, z - 1},
                {x, y, z + 1}
        };
    }

    private boolean[] adjustForID(boolean[] faces) {
        int ourBlockID = this.block.blockID;

        //blocks that should only merge the top/bottom
        if (ourBlockID == Block.cactus.blockID || ourBlockID == Block.reed.blockID) {
            faces[0] = true;
            faces[1] = true;
            faces[4] = true;
            faces[5] = true;
        }

        //blocks that should not merge the top/bottom
        if (ourBlockID == Block.blockBed.blockID || ourBlockID == Block.stairSingle.blockID || ourBlockID == Block.redstoneWire.blockID || ourBlockID == Block.tilledField.blockID || ourBlockID == Block.snow.blockID) {
            faces[2] = true;
            faces[3] = true;
        }

        return faces;
    }

    private boolean isThisADudBlock() {
        int ourBlockID = this.block.blockID;

        if (ourBlockID == Block.sapling.blockID
                || (ourBlockID > Block.pistonStickyBase.blockID && ourBlockID < Block.pistonBase.blockID)
                || ourBlockID == Block.pistonExtension.blockID
                || (ourBlockID > Block.cloth.blockID && ourBlockID < Block.blockGold.blockID)
                || ourBlockID == Block.torchWood.blockID
                || ourBlockID == Block.crops.blockID
                || ourBlockID == Block.signPost.blockID
                || ourBlockID == Block.signWall.blockID
                || ourBlockID == Block.lever.blockID
                || ourBlockID == Block.pressurePlateStone.blockID
                || ourBlockID == Block.pressurePlatePlanks.blockID
                || ourBlockID == Block.torchRedstoneIdle.blockID
                || ourBlockID == Block.torchRedstoneActive.blockID
                || ourBlockID == Block.button.blockID
                || ourBlockID == Block.cake.blockID) {

            return true;
        }

        return false;
    }

    private boolean hasSameCoordinates(int selfX, int selfY, int selfZ, BlockCoords toCompare) {
        if (selfX != toCompare.x) {
            return false;
        }

        if (selfY != toCompare.y) {
            return false;
        }

        if (selfZ != toCompare.z) {
            return false;
        }

        return true;
    }

    public boolean hasNoFaces() {
        for (boolean face : this.neighborMap.get()) {
            if (face) {
                return false;
            }
        }

        return true;
    }

    public boolean[] getNeighborMap() {
        return this.neighborMap.get();
    }

    public void updateNeighborMap() {
        this.neighborMap.set(this.loadNeighborMap(this.x, this.y, this.z));
    }

    public void updateNeighborMap(ArrayList<BlockCoords> list) {
        this.neighborMap.set(this.loadNeighborMap(this.x, this.y, this.z, list));
    }

    public boolean isInFrustrum() {
        return RenderStatic.isBoxInFrustum(this.x, this.y, this.z, this.quickFrustrumBox[0], this.quickFrustrumBox[1], this.quickFrustrumBox[2]);
    }
}
