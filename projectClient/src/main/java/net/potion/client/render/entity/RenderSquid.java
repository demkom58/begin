package net.potion.client.render.entity;

import net.potion.client.model.ModelBase;
import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.passive.EntitySquid;
import org.lwjgl.opengl.GL11;

public class RenderSquid extends RenderLiving {
    public RenderSquid(ModelBase var1, float var2) {
        super(var1, var2);
    }

    public void func_21008_a(EntitySquid var1, double var2, double var4, double var6, float var8, float var9) {
        super.doRenderLiving(var1, var2, var4, var6, var8, var9);
    }

    protected void func_21007_a(EntitySquid var1, float var2, float var3, float var4) {
        float var5 = var1.field2 + (var1.field1 - var1.field2) * var4;
        float var6 = var1.field4 + (var1.field3 - var1.field4) * var4;
        GL11.glTranslatef(0.0F, 0.5F, 0.0F);
        GL11.glRotatef(180.0F - var3, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, -1.2F, 0.0F);
    }

    protected void func_21005_a(EntitySquid var1, float var2) {
    }

    protected float func_21006_b(EntitySquid var1, float var2) {
        return var1.field8 + (var1.field7 - var1.field8) * var2;
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    protected void preRenderCallback(EntityLiving var1, float var2) {
        this.func_21005_a((EntitySquid) var1, var2);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    protected float handleRotationFloat(EntityLiving entity, float value) {
        return this.func_21006_b((EntitySquid) entity, value);
    }

    @Override
    protected void rotateCorpse(EntityLiving entity, float var2, float var3, float var4) {
        this.func_21007_a((EntitySquid) entity, var2, var3, var4);
    }

    @Override
    public void doRenderLiving(EntityLiving entity, double x, double y, double z, float yaw, float delta) {
        this.func_21008_a((EntitySquid) entity, x, y, z, yaw, delta);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.func_21008_a((EntitySquid) entity, x, y, z, yaw, delta);
    }
}
