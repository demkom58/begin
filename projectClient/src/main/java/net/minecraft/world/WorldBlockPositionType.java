package net.minecraft.world;

class WorldBlockPositionType {
    final WorldClient world;
    int posX;
    int posY;
    int posZ;
    int acceptCountdown;
    int blockId;
    int metadata;

    public WorldBlockPositionType(WorldClient world, int x, int y, int z, int blockId, int metadata) {
        this.world = world;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.acceptCountdown = 80;
        this.blockId = blockId;
        this.metadata = metadata;
    }
}
