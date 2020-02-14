package net.potion.client.render.entity;

import net.potion.client.model.ModelBase;
import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.passive.EntityWolf;

public class RenderWolf extends RenderLiving {
    public RenderWolf(ModelBase var1, float var2) {
        super(var1, var2);
    }

    public void renderWolf(EntityWolf var1, double var2, double var4, double var6, float var8, float var9) {
        super.doRenderLiving(var1, var2, var4, var6, var8, var9);
    }

    protected float func_25004_a(EntityWolf var1, float var2) {
        return var1.setTailRotation();
    }

    protected void func_25006_b(EntityWolf var1, float var2) {
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    protected void preRenderCallback(EntityLiving var1, float var2) {
        this.func_25006_b((EntityWolf) var1, var2);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    protected float handleRotationFloat(EntityLiving entity, float value) {
        return this.func_25004_a((EntityWolf) entity, value);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRenderLiving(EntityLiving entity, double x, double y, double z, float yaw, float delta) {
        this.renderWolf((EntityWolf) entity, x, y, z, yaw, delta);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.renderWolf((EntityWolf) entity, x, y, z, yaw, delta);
    }
}
