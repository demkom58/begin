package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public interface IWorldAccess {
    void markBlockAndNeighborsNeedsUpdate(int x, int y, int z);

    void markBlockRangeNeedsUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ);

    void playSound(String soundCategory, double x, double y, double z, float volume, float pitch);

    void spawnParticle(String particleName, double x, double y, double z, double motionX, double motionY, double motionZ);

    void obtainEntitySkin(Entity entity);

    void releaseEntitySkin(Entity entity);

    void updateAllRenderers();

    void playRecord(String recordName, int x, int y, int z);

    void doNothingWithTileEntity(int x, int y, int z, TileEntity tile);

    void playEffect(EntityPlayer player, int effectId, int x, int y, int z, int subData);
}
