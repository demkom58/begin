package net.potion.world.chunk;

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

    public ChunkCoordinates(ChunkCoordinates coordinates) {
        this.x = coordinates.x;
        this.y = coordinates.y;
        this.z = coordinates.z;
    }

    public boolean equals(Object var1) {
        if (!(var1 instanceof ChunkCoordinates)) {
            return false;
        } else {
            ChunkCoordinates var2 = (ChunkCoordinates) var1;
            return this.x == var2.x && this.y == var2.y && this.z == var2.z;
        }
    }

    public int hashCode() {
        return this.x + this.z << 8 + this.y << 16;
    }

    public int compareChunkCoordinate(ChunkCoordinates var1) {
        if (this.y == var1.y) {
            return this.z == var1.z ? this.x - var1.x : this.z - var1.z;
        } else {
            return this.y - var1.y;
        }
    }

    public double getSqDistanceTo(int var1, int var2, int var3) {
        int var4 = this.x - var1;
        int var5 = this.y - var2;
        int var6 = this.z - var3;
        return Math.sqrt(var4 * var4 + var5 * var5 + var6 * var6);
    }

    @Override
    public int compareTo(ChunkCoordinates coordinates) {
        return this.compareChunkCoordinate(coordinates);
    }
}
