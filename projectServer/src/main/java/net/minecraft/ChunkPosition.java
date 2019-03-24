package net.minecraft;

public class ChunkPosition {
    public final int x;
    public final int y;
    public final int z;

    public ChunkPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ChunkPosition)) {
            return false;
        }

        ChunkPosition position = (ChunkPosition) obj;
        return position.x == this.x && position.y == this.y && position.z == this.z;
    }

    public int hashCode() {
        return this.x * 8976890 + this.y * 981131 + this.z;
    }
}
