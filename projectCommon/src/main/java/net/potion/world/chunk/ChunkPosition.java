package net.potion.world.chunk;

public class ChunkPosition {
    public final int x;
    public final int y;
    public final int z;

    public ChunkPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Object i) {
        if (!(i instanceof ChunkPosition cPos)) {
            return false;
        }

        return cPos.x == this.x && cPos.y == this.y && cPos.z == this.z;
    }

    public int hashCode() {
        return this.x * 8976890 + this.y * 981131 + this.z;
    }
}
