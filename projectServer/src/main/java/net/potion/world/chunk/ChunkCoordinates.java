package net.potion.world.chunk;

public class ChunkCoordinates implements Comparable {
    public int posX;
    public int posY;
    public int posZ;

    public ChunkCoordinates() {
    }

    public ChunkCoordinates(int posX, int posY, int posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public ChunkCoordinates(ChunkCoordinates coord) {
        this.posX = coord.posX;
        this.posY = coord.posY;
        this.posZ = coord.posZ;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ChunkCoordinates)) {
            return false;
        }

        ChunkCoordinates coordinates = (ChunkCoordinates) obj;
        return this.posX == coordinates.posX && this.posY == coordinates.posY && this.posZ == coordinates.posZ;
    }

    public int hashCode() {
        return this.posX + this.posZ << 8 + this.posY << 16;
    }

    public int compareChunkCoordinate(ChunkCoordinates coord) {
        if (this.posY == coord.posY) {
            return this.posZ == coord.posZ ? this.posX - coord.posX : this.posZ - coord.posZ;
        }

        return this.posY - coord.posY;
    }

    public double getSqDistanceTo(int posX, int posY, int posZ) {
        int var4 = this.posX - posX;
        int var5 = this.posY - posY;
        int var6 = this.posZ - posZ;
        return Math.sqrt(var4 * var4 + var5 * var5 + var6 * var6);
    }

    @Override
    public int compareTo(Object o) {
        return this.compareChunkCoordinate((ChunkCoordinates) o);
    }

}
