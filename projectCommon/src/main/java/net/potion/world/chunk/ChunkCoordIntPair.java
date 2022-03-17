package net.potion.world.chunk;

public class ChunkCoordIntPair {
    public final int chunkXPos;
    public final int chunkZPos;

    public ChunkCoordIntPair(int x, int z) {
        this.chunkXPos = x;
        this.chunkZPos = z;
    }

    public static int chunkXZ2Int(int x, int z) {
        return (x < 0 ? Integer.MIN_VALUE : 0) | (x & 32767) << 16
                | (z < 0 ? '\u8000' : 0) | z & 32767;
    }

    public int hashCode() {
        return chunkXZ2Int(this.chunkXPos, this.chunkZPos);
    }

    @Override
    public boolean equals(Object var1) {
        ChunkCoordIntPair pair = (ChunkCoordIntPair) var1;
        return pair.chunkXPos == this.chunkXPos && pair.chunkZPos == this.chunkZPos;
    }

}
