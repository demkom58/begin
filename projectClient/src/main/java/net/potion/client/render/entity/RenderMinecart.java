package net.potion.client.render.entity;

import net.potion.block.Block;
import net.potion.client.model.ModelBase;
import net.potion.client.model.ModelMinecart;
import net.potion.client.render.Render;
import net.potion.entity.Entity;
import net.potion.entity.EntityMinecart;
import net.potion.util.MathHelper;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

public class RenderMinecart extends Render {
    protected ModelBase modelMinecart;

    public RenderMinecart() {
        this.shadowSize = 0.5F;
        this.modelMinecart = new ModelMinecart();
    }

    public void func_152_a(EntityMinecart var1, double var2, double var4, double var6, float var8, float var9) {
        GL11.glPushMatrix();
        double var10 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double) var9;
        double var12 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double) var9;
        double var14 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double) var9;
        double var16 = 0.30000001192092896D;
        Vector3d var18 = var1.func_514_g(var10, var12, var14);
        float var19 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;
        if (var18 != null) {
            Vector3d var20 = var1.func_515_a(var10, var12, var14, var16);
            Vector3d var21 = var1.func_515_a(var10, var12, var14, -var16);
            if (var20 == null) {
                var20 = var18;
            }

            if (var21 == null) {
                var21 = var18;
            }

            var2 += var18.x - var10;
            var4 += (var20.y + var21.y) / 2.0D - var12;
            var6 += var18.z - var14;
            Vector3d var22 = new Vector3d(var21).add(-var20.x, -var20.y, -var20.z);
            if (var22.length() != 0.0D) {
                var22 = MathHelper.normalizeOrZero(new Vector3d(var22));
                var8 = (float) (Math.atan2(var22.z, var22.x) * 180.0D / 3.141592653589793D);
                var19 = (float) (Math.atan(var22.y) * 73.0D);
            }
        }

        GL11.glTranslatef((float) var2, (float) var4, (float) var6);
        GL11.glRotatef(180.0F - var8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var19, 0.0F, 0.0F, 1.0F);
        float var23 = (float) var1.minecartTimeSinceHit - var9;
        float var24 = (float) var1.minecartCurrentDamage - var9;
        if (var24 < 0.0F) {
            var24 = 0.0F;
        }

        if (var23 > 0.0F) {
            GL11.glRotatef(MathHelper.sin(var23) * var23 * var24 / 10.0F * (float) var1.minecartRockDirection, 1.0F, 0.0F, 0.0F);
        }

        if (var1.minecartType != 0) {
            this.loadTexture("/terrain.png");
            float var26 = 0.75F;
            GL11.glScalef(var26, var26, var26);
            GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            if (var1.minecartType == 1) {
                (new RenderBlocks()).renderBlockOnInventory(Block.CHEST, 0, var1.getEntityBrightness(var9));
            } else if (var1.minecartType == 2) {
                (new RenderBlocks()).renderBlockOnInventory(Block.FURNACE, 0, var1.getEntityBrightness(var9));
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
            GL11.glScalef(1.0F / var26, 1.0F / var26, 1.0F / var26);
        }

        this.loadTexture("/item/cart.png");
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelMinecart.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.func_152_a((EntityMinecart) entity, x, y, z, yaw, delta);
    }
}
