package dev.tensor.misc.helpers.block;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 04-17-2026
 */

public final class BlockPos {

    public final int x;
    public final int y;
    public final int z;

    private final boolean faces;

    private final AtomicReference<boolean[]> neighborMap = new AtomicReference<>(new boolean[]{true, true, true, true, true, true});

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.faces = false;
    }

    public BlockPos(int x, int y, int z, boolean faces) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.faces = faces;
    }

    public boolean hasNoFaces() {
        if (!this.faces) {
            return false;
        }

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

    public void updateNeighborMapFromArrayList(ArrayList<BlockPos> list) {
        if (!this.faces) {
            return;
        }

        boolean[] returnResult = new boolean[6];

        int[][] neighbors = this.neighborMap(x, y, z);
        for (int i = 0; i < neighbors.length; i++) {
            int[] neighbor = neighbors[i];

            returnResult[i] = !this.isNeighbor(neighbor[0], neighbor[1], neighbor[2], list);
        }

        this.neighborMap.set(returnResult);
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

    private boolean isNeighbor(int x, int y, int z, ArrayList<BlockPos> list) {
        return list.stream().filter(blockPos -> blockPos.x == x && blockPos.y == y && blockPos.z == z).findFirst().orElse(null) != null;
    }
}
