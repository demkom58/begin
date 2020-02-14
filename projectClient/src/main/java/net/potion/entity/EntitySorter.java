package net.potion.entity;

import net.potion.world.WorldRenderer;

import java.util.Comparator;

public class EntitySorter implements Comparator<WorldRenderer> {
    private double x;
    private double y;
    private double z;

    public EntitySorter(Entity entity) {
        this.x = -entity.posX;
        this.y = -entity.posY;
        this.z = -entity.posZ;
    }

    public int sortByDistanceToEntity(WorldRenderer ren1, WorldRenderer ren2) {
        double var3 = (double) ren1.posXPlus + this.x;
        double var5 = (double) ren1.posYPlus + this.y;
        double var7 = (double) ren1.posZPlus + this.z;

        double var9 = (double) ren2.posXPlus + this.x;
        double var11 = (double) ren2.posYPlus + this.y;
        double var13 = (double) ren2.posZPlus + this.z;

        return (int) ((var3 * var3 + var5 * var5 + var7 * var7 - (var9 * var9 + var11 * var11 + var13 * var13)) * 1024.0D);
    }

    @Override
    public int compare(WorldRenderer ren1, WorldRenderer ren2) {
        return this.sortByDistanceToEntity(ren1, ren2);
    }
}
