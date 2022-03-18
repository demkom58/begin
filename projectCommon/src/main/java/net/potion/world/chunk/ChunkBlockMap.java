package net.potion.world.chunk;

import net.potion.block.Block;

public class ChunkBlockMap {
    private static final byte[] BLOCK_ID_MAP = new byte[256];

    static {
        try {
            for (int i = 0; i < 256; ++i) {
                byte id = (byte) i;
                if (id != 0 && Block.BLOCKS_LIST[id & 255] == null) {
                    id = 0;
                }

                BLOCK_ID_MAP[i] = id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void fix(byte[] data) {
        for (int i = 0; i < data.length; ++i) {
            data[i] = BLOCK_ID_MAP[data[i] & 255];
        }
    }

    public static void fix(int[][][] data) {
        for (int x = 0; x < data.length; x++) {
            int[][] yl = data[x];

            for (int y = 0; y < yl.length; y++) {
                int[] zl = yl[y];

                for (int z = 0; z < zl.length; z++) {
                    zl[z] = BLOCK_ID_MAP[zl[z] & 255];
                }
            }
        }
    }
}
