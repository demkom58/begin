package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityCow;

public class RenderCow extends RenderLiving {
    public RenderCow(ModelBase var1, float var2) {
        super(var1, var2);
    }

    public void renderCow(EntityCow var1, double var2, double var4, double var6, float var8, float var9) {
        super.doRenderLiving(var1, var2, var4, var6, var8, var9);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRenderLiving(EntityLiving entity, double x, double y, double z, float yaw, float delta) {
        this.renderCow((EntityCow) entity, x, y, z, yaw, delta);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.renderCow((EntityCow) entity, x, y, z, yaw, delta);
    }
}
