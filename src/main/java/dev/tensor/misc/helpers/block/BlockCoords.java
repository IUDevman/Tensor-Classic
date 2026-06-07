package dev.tensor.misc.helpers.block;

import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.render.RenderStatic;
import net.minecraft.src.Block;

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
    private final AtomicReference<boolean[]> neighborMap = new AtomicReference<>();

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

    private boolean[] loadNeighborMap(int x, int y, int z) {
        boolean[] returnResult = new boolean[6];

        if (this.isThisADudBlock()) {
            return new boolean[]{true, true, true, true, true, true};
        }

        int metadata = this.getWorld().getBlockMetadata(x, y, z);

        int[][] neighbors = this.neighborMap(x, y, z);
        for (int i = 0; i < neighbors.length; i++) {
            int[] neighbor = neighbors[i];

            int neighborBlockId = this.getWorld().getBlockId(neighbor[0], neighbor[1], neighbor[2]);

            boolean face = neighborBlockId != this.block.blockID;

            if (!face && (this.block.blockID == 64 || this.block.blockID == 65 || this.block.blockID == 71 || this.block.blockID == 96)) {
                int neighborMetadata = this.getWorld().getBlockMetadata(neighbor[0], neighbor[1], neighbor[2]);

                //todo: fix doors:
                if (neighborMetadata != metadata) {
                    face = true;
                }
            }

            returnResult[i] = face;
        }

        return this.adjustForID(returnResult);
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
        if (ourBlockID == 26 || ourBlockID == 81 || ourBlockID == 83) {
            faces[0] = true;
            faces[1] = true;
            faces[4] = true;
            faces[5] = true;
        }

        //blocks that should not merge the top/bottom
        if (ourBlockID == 44 || ourBlockID == 60 || ourBlockID == 78 || ourBlockID == 96) {
            faces[2] = true;
            faces[3] = true;
        }

        return faces;
    }

    private boolean isThisADudBlock() {
        int ourBlockID = this.block.blockID;

        if (ourBlockID == 6
                || (ourBlockID > 29 && ourBlockID < 33)
                || ourBlockID == 34
                || (ourBlockID > 35 && ourBlockID < 41)
                || ourBlockID == 50
                || ourBlockID == 55
                || ourBlockID == 59
                || ourBlockID == 63
                || ourBlockID == 68
                || ourBlockID == 69
                || ourBlockID == 70
                || ourBlockID == 72
                || ourBlockID == 75
                || ourBlockID == 76
                || ourBlockID == 77
                || ourBlockID == 92) {

            return true;
        }

        return false;
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
        boolean[] returnMap =  this.neighborMap.get();

        if (returnMap == null) {
            this.updateNeighborMap();
            return this.neighborMap.get();
        }
        
        return returnMap;
    }

    public void updateNeighborMap() {
        this.neighborMap.set(this.loadNeighborMap(this.x, this.y, this.z));
    }

    public boolean isInFrustrum() {
        return RenderStatic.isBoxInFrustum(this.x, this.y, this.z, this.quickFrustrumBox[0], this.quickFrustrumBox[1], this.quickFrustrumBox[2]);
    }
}
