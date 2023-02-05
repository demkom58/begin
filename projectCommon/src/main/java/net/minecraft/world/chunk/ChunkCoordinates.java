package net.minecraft.world.chunk;

public class ChunkCoordinates implements Comparable<ChunkCoordinates> {
    public int x;
    public int y;
    public int z;

    public ChunkCoordinates() {
    }

    public ChunkCoordinates(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ChunkCoordinates(ChunkCoordinates coord) {
        this.x = coord.x;
        this.y = coord.y;
        this.z = coord.z;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ChunkCoordinates cCoord)) {
            return false;
        }

        return this.x == cCoord.x && this.y == cCoord.y && this.z == cCoord.z;
    }

    public int hashCode() {
        return this.x + this.z << 8 + this.y << 16;
    }

    public int compareChunkCoordinate(ChunkCoordinates cCoord) {
        if (this.y == cCoord.y) {
            return this.z == cCoord.z ? this.x - cCoord.x : this.z - cCoord.z;
        }

        return this.y - cCoord.y;
    }

    public double getSqDistanceTo(int x, int y, int z) {
        int dX = this.x - x;
        int dY = this.y - y;
        int dZ = this.z - z;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    @Override
    public int compareTo(ChunkCoordinates coord) {
        return this.compareChunkCoordinate(coord);
    }
}
