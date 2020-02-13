package net.minecraft.client.render.entity;

import net.minecraft.block.Block;
import net.minecraft.client.render.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTNTPrimed;
import org.lwjgl.opengl.GL11;

public class RenderTNTPrimed extends Render {
    private RenderBlocks blockRenderer = new RenderBlocks();

    public RenderTNTPrimed() {
        this.shadowSize = 0.5F;
    }

    public void func_153_a(EntityTNTPrimed tntPrimed, double var2, double var4, double var6, float var8, float var9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2, (float) var4, (float) var6);
        if ((float) tntPrimed.fuse - var9 + 1.0F < 10.0F) {
            float var10 = 1.0F - ((float) tntPrimed.fuse - var9 + 1.0F) / 10.0F;
            if (var10 < 0.0F) {
                var10 = 0.0F;
            }

            if (var10 > 1.0F) {
                var10 = 1.0F;
            }

            var10 = var10 * var10;
            var10 = var10 * var10;
            float var11 = 1.0F + var10 * 0.3F;
            GL11.glScalef(var11, var11, var11);
        }

        float d = (1.0F - ((float) tntPrimed.fuse - var9 + 1.0F) / 100.0F) * 0.8F;
        this.loadTexture("/terrain.png");
        this.blockRenderer.renderBlockOnInventory(Block.TNT, 0, tntPrimed.getEntityBrightness(var9));
        if (tntPrimed.fuse / 5 % 2 == 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, d);
            this.blockRenderer.renderBlockOnInventory(Block.TNT, 0, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.func_153_a((EntityTNTPrimed) entity, x, y, z, yaw, delta);
    }
}
