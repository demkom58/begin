package net.potion.client.render.entity;

import net.potion.client.model.ModelBase;
import net.potion.entity.Entity;
import net.potion.entity.passive.EntityChicken;
import net.potion.entity.EntityLiving;
import net.potion.util.MathHelper;

public class RenderChicken extends RenderLiving {
    public RenderChicken(ModelBase var1, float var2) {
        super(var1, var2);
    }

    public void renderChicken(EntityChicken var1, double var2, double var4, double var6, float var8, float var9) {
        super.doRenderLiving(var1, var2, var4, var6, var8, var9);
    }

    protected float getWingRotation(EntityChicken var1, float var2) {
        float var3 = var1.field_756_e + (var1.field_752_b - var1.field_756_e) * var2;
        float var4 = var1.field_757_d + (var1.destPos - var1.field_757_d) * var2;
        return (MathHelper.sin(var3) + 1.0F) * var4;
    }

    @Override
    protected float handleRotationFloat(EntityLiving entity, float value) {
        return this.getWingRotation((EntityChicken) entity, value);
    }

    @Override
    public void doRenderLiving(EntityLiving entity, double x, double y, double z, float yaw, float delta) {
        this.renderChicken((EntityChicken) entity, x, y, z, yaw, delta);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.renderChicken((EntityChicken) entity, x, y, z, yaw, delta);
    }
}
