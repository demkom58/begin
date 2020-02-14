package net.potion.client.render.entity;

import net.potion.client.render.Render;
import net.hypnosis.render.Tessellator;
import net.potion.entity.Entity;
import net.potion.entity.projectile.EntityFireball;
import net.potion.item.Item;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class RenderFireball extends Render {
    public void func_4012_a(EntityFireball var1, double var2, double var4, double var6, float var8, float var9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2, (float) var4, (float) var6);
        GL11.glEnable(GL15.GL_RESCALE_NORMAL);
        float var10 = 2.0F;
        GL11.glScalef(var10 / 1.0F, var10 / 1.0F, var10 / 1.0F);
        int var11 = Item.SNOWBALL.getIconFromDamage(0);
        this.loadTexture("/gui/items.png");
        Tessellator var12 = Tessellator.INSTANCE;
        float var13 = (float) (var11 % 16 * 16) / 256.0F;
        float var14 = (float) (var11 % 16 * 16 + 16) / 256.0F;
        float var15 = (float) (var11 / 16 * 16) / 256.0F;
        float var16 = (float) (var11 / 16 * 16 + 16) / 256.0F;
        float var17 = 1.0F;
        float var18 = 0.5F;
        float var19 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var12.startDrawingQuads();
        var12.setNormal(0.0F, 1.0F, 0.0F);
        var12.addVertexWithUV(0.0F - var18, 0.0F - var19, 0.0D, var13, var16);
        var12.addVertexWithUV(var17 - var18, 0.0F - var19, 0.0D, var14, var16);
        var12.addVertexWithUV(var17 - var18, 1.0F - var19, 0.0D, var14, var15);
        var12.addVertexWithUV(0.0F - var18, 1.0F - var19, 0.0D, var13, var15);
        var12.draw();
        GL11.glDisable(GL15.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.func_4012_a((EntityFireball) entity, x, y, z, yaw, delta);
    }
}
