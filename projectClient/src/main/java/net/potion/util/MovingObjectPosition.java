package net.potion.util;

import net.potion.entity.Entity;
import org.joml.Vector3d;

public class MovingObjectPosition {
    public EnumMovingObjectType typeOfHit;
    public int blockX;
    public int blockY;
    public int blockZ;
    public int sideHit;
    public Vector3d hitVec;
    public Entity entityHit;

    public MovingObjectPosition(int var1, int var2, int var3, int var4, Vector3d var5) {
        this.typeOfHit = EnumMovingObjectType.TILE;
        this.blockX = var1;
        this.blockY = var2;
        this.blockZ = var3;
        this.sideHit = var4;
        this.hitVec = new Vector3d(var5.x, var5.y, var5.z);
    }

    public MovingObjectPosition(Entity var1) {
        this.typeOfHit = EnumMovingObjectType.ENTITY;
        this.entityHit = var1;
        this.hitVec = new Vector3d(var1.posX, var1.posY, var1.posZ);
    }
}
