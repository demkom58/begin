package net.potion.client.render.entity;

import net.potion.entity.EntityLiving;
import net.potion.world.WorldRenderer;

import java.util.Comparator;

public class RenderSorter implements Comparator<WorldRenderer> {
    private EntityLiving baseEntity;

    public RenderSorter(EntityLiving var1) {
        this.baseEntity = var1;
    }

    public int doCompare(WorldRenderer var1, WorldRenderer var2) {
        boolean var3 = var1.isInFrustum;
        boolean var4 = var2.isInFrustum;
        if (var3 && !var4) {
            return 1;
        } else if (var4 && !var3) {
            return -1;
        } else {
            double var5 = var1.distanceToEntitySquared(this.baseEntity);
            double var7 = var2.distanceToEntitySquared(this.baseEntity);
            if (var5 < var7) {
                return 1;
            } else if (var5 > var7) {
                return -1;
            } else {
                return var1.chunkIndex < var2.chunkIndex ? 1 : -1;
            }
        }
    }

    @Override
    public int compare(WorldRenderer var1, WorldRenderer var2) {
        return this.doCompare(var1, var2);
    }
}
