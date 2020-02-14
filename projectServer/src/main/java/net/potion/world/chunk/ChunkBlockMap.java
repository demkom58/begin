package net.potion.world.chunk;

import net.potion.block.Block;

public class ChunkBlockMap {
    private static byte[] map = new byte[256];

    static {
        try {
            for (int i = 0; i < 256; ++i) {
                byte id = (byte) i;
                if (id != 0 && Block.BLOCKS_LIST[id & 255] == null) {
                    id = 0;
                }

                map[i] = id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void fix(byte[] data) {
        for (int i = 0; i < data.length; ++i) {
            data[i] = map[data[i] & 255];
        }

    }
}
