package net.potion.client.render;

import net.hypnosis.render.Tessellator;
import net.potion.block.Block;
import net.potion.client.model.ModelBase;
import net.potion.client.model.ModelBiped;
import net.potion.client.render.entity.RenderBlocks;
import net.potion.entity.Entity;
import net.potion.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;
import org.lwjgl.opengl.GL11;

public abstract class Render {
    protected RenderManager renderManager;
    protected float shadowSize = 0.0F;
    protected float shadowOpaque = 1.0F;
    private ModelBase modelBase = new ModelBiped();
    private RenderBlocks renderBlocks = new RenderBlocks();

    public static void renderOffsetAABB(AxisAlignedBB axis, double x, double y, double z) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tess.startDrawingQuads();
        tess.setTranslationD(x, y, z);
        tess.setNormal(0.0F, 0.0F, -1.0F);
        tess.addVertex(axis.minX, axis.maxY, axis.minZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.minZ);
        tess.addVertex(axis.maxX, axis.minY, axis.minZ);
        tess.addVertex(axis.minX, axis.minY, axis.minZ);
        tess.setNormal(0.0F, 0.0F, 1.0F);
        tess.addVertex(axis.minX, axis.minY, axis.maxZ);
        tess.addVertex(axis.maxX, axis.minY, axis.maxZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.maxZ);
        tess.addVertex(axis.minX, axis.maxY, axis.maxZ);
        tess.setNormal(0.0F, -1.0F, 0.0F);
        tess.addVertex(axis.minX, axis.minY, axis.minZ);
        tess.addVertex(axis.maxX, axis.minY, axis.minZ);
        tess.addVertex(axis.maxX, axis.minY, axis.maxZ);
        tess.addVertex(axis.minX, axis.minY, axis.maxZ);
        tess.setNormal(0.0F, 1.0F, 0.0F);
        tess.addVertex(axis.minX, axis.maxY, axis.maxZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.maxZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.minZ);
        tess.addVertex(axis.minX, axis.maxY, axis.minZ);
        tess.setNormal(-1.0F, 0.0F, 0.0F);
        tess.addVertex(axis.minX, axis.minY, axis.maxZ);
        tess.addVertex(axis.minX, axis.maxY, axis.maxZ);
        tess.addVertex(axis.minX, axis.maxY, axis.minZ);
        tess.addVertex(axis.minX, axis.minY, axis.minZ);
        tess.setNormal(1.0F, 0.0F, 0.0F);
        tess.addVertex(axis.maxX, axis.minY, axis.minZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.minZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.maxZ);
        tess.addVertex(axis.maxX, axis.minY, axis.maxZ);
        tess.setTranslationD(0.0D, 0.0D, 0.0D);
        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void renderAABB(AxisAlignedBB bb) {
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertex(bb.minX, bb.maxY, bb.minZ);
        tess.addVertex(bb.maxX, bb.maxY, bb.minZ);
        tess.addVertex(bb.maxX, bb.minY, bb.minZ);
        tess.addVertex(bb.minX, bb.minY, bb.minZ);
        tess.addVertex(bb.minX, bb.minY, bb.maxZ);
        tess.addVertex(bb.maxX, bb.minY, bb.maxZ);
        tess.addVertex(bb.maxX, bb.maxY, bb.maxZ);
        tess.addVertex(bb.minX, bb.maxY, bb.maxZ);
        tess.addVertex(bb.minX, bb.minY, bb.minZ);
        tess.addVertex(bb.maxX, bb.minY, bb.minZ);
        tess.addVertex(bb.maxX, bb.minY, bb.maxZ);
        tess.addVertex(bb.minX, bb.minY, bb.maxZ);
        tess.addVertex(bb.minX, bb.maxY, bb.maxZ);
        tess.addVertex(bb.maxX, bb.maxY, bb.maxZ);
        tess.addVertex(bb.maxX, bb.maxY, bb.minZ);
        tess.addVertex(bb.minX, bb.maxY, bb.minZ);
        tess.addVertex(bb.minX, bb.minY, bb.maxZ);
        tess.addVertex(bb.minX, bb.maxY, bb.maxZ);
        tess.addVertex(bb.minX, bb.maxY, bb.minZ);
        tess.addVertex(bb.minX, bb.minY, bb.minZ);
        tess.addVertex(bb.maxX, bb.minY, bb.minZ);
        tess.addVertex(bb.maxX, bb.maxY, bb.minZ);
        tess.addVertex(bb.maxX, bb.maxY, bb.maxZ);
        tess.addVertex(bb.maxX, bb.minY, bb.maxZ);
        tess.draw();
    }

    public abstract void doRender(Entity entity, double x, double y, double z, float yaw, float delta);

    protected void loadTexture(String var1) {
        RenderEngine renderEngine = this.renderManager.renderEngine;
        renderEngine.bindTexture(renderEngine.getTexture(var1));
    }

    protected boolean loadDownloadableImageTexture(String var1, String var2) {
        RenderEngine renderEngine = this.renderManager.renderEngine;
        int texture = renderEngine.getTextureForDownloadableImage(var1, var2);
        if (texture >= 0) {
            renderEngine.bindTexture(texture);
            return true;
        }

        return false;
    }

    private void renderEntityOnFire(Entity var1, double var2, double var4, double var6, float var8) {
        GL11.glDisable(GL11.GL_LIGHTING);
        int var9 = Block.FIRE.blockIndexInTexture;
        int var10 = (var9 & 15) << 4;
        int var11 = var9 & 240;
        float var12 = (float) var10 / 256.0F;
        float var13 = ((float) var10 + 15.99F) / 256.0F;
        float var14 = (float) var11 / 256.0F;
        float var15 = ((float) var11 + 15.99F) / 256.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2, (float) var4, (float) var6);
        float var16 = var1.width * 1.4F;
        GL11.glScalef(var16, var16, var16);
        this.loadTexture("/terrain.png");
        float var18 = 0.5F;
        float var19 = 0.0F;
        float var20 = var1.height / var16;
        float var21 = (float) (var1.posY - var1.boundingBox.minY);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float) ((int) var20) * 0.02F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var22 = 0.0F;
        int var23 = 0;

        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();

        while (var20 > 0.0F) {
            if (var23 % 2 == 0) {
                var12 = (float) var10 / 256.0F;
                var13 = ((float) var10 + 15.99F) / 256.0F;
                var14 = (float) var11 / 256.0F;
                var15 = ((float) var11 + 15.99F) / 256.0F;
            } else {
                var12 = (float) var10 / 256.0F;
                var13 = ((float) var10 + 15.99F) / 256.0F;
                var14 = (float) (var11 + 16) / 256.0F;
                var15 = ((float) (var11 + 16) + 15.99F) / 256.0F;
            }

            if (var23 / 2 % 2 == 0) {
                float var24 = var13;
                var13 = var12;
                var12 = var24;
            }

            tess.addVertexWithUV(var18 - var19, 0.0F - var21, var22, var13, var15);
            tess.addVertexWithUV(-var18 - var19, 0.0F - var21, var22, var12, var15);
            tess.addVertexWithUV(-var18 - var19, 1.4F - var21, var22, var12, var14);
            tess.addVertexWithUV(var18 - var19, 1.4F - var21, var22, var13, var14);
            var20 -= 0.45F;
            var21 -= 0.45F;
            var18 *= 0.9F;
            var22 += 0.03F;
            ++var23;
        }

        tess.draw();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void renderShadow(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderEngine renderEngine = this.renderManager.renderEngine;
        renderEngine.bindTexture(renderEngine.getTexture("%clamp%/misc/shadow.png"));

        World world = this.getWorldFromRenderManager();
        GL11.glDepthMask(false);

        float var12 = this.shadowSize;
        double var13 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double) var9;
        double var15 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double) var9 + (double) var1.getShadowSize();
        double var17 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double) var9;

        int var19 = MathHelper.floor(var13 - (double) var12);
        int var20 = MathHelper.floor(var13 + (double) var12);
        int var21 = MathHelper.floor(var15 - (double) var12);
        int var22 = MathHelper.floor(var15);
        int var23 = MathHelper.floor(var17 - (double) var12);
        int var24 = MathHelper.floor(var17 + (double) var12);
        double var25 = var2 - var13;
        double var27 = var4 - var15;
        double var29 = var6 - var17;
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();

        for (int var32 = var19; var32 <= var20; ++var32) {
            for (int var33 = var21; var33 <= var22; ++var33) {
                for (int var34 = var23; var34 <= var24; ++var34) {
                    int var35 = world.getBlockId(var32, var33 - 1, var34);
                    if (var35 > 0 && world.getBlockLightValue(var32, var33, var34) > 3) {
                        this.renderShadowOnBlock(Block.BLOCKS_LIST[var35], var2, var4 + (double) var1.getShadowSize(), var6, var32, var33, var34, var8, var12, var25, var27 + (double) var1.getShadowSize(), var29);
                    }
                }
            }
        }

        tess.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.worldObj;
    }

    private void renderShadowOnBlock(Block block, double var2, double var4, double var6,
                                     int var8, int var9, int var10, float var11, float var12,
                                     double var13, double var15, double var17) {
        if (!block.isNormalCube())
            return;

        Tessellator tess = Tessellator.INSTANCE;
        double var20 = ((double) var11 - (var4 - ((double) var9 + var15)) / 2.0D) * 0.5D * (double) this.getWorldFromRenderManager().getLightBrightness(var8, var9, var10);
        if (var20 < 0.0D)
            return;

        if (var20 > 1.0D)
            var20 = 1.0D;

        tess.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float) var20);

        double var22 = (double) var8 + block.minX + var13;
        double var24 = (double) var8 + block.maxX + var13;
        double var26 = (double) var9 + block.minY + var15 + 0.015625D;
        double var28 = (double) var10 + block.minZ + var17;
        double var30 = (double) var10 + block.maxZ + var17;

        float var32 = (float) ((var2 - var22) / 2.0D / (double) var12 + 0.5D);
        float var33 = (float) ((var2 - var24) / 2.0D / (double) var12 + 0.5D);
        float var34 = (float) ((var6 - var28) / 2.0D / (double) var12 + 0.5D);
        float var35 = (float) ((var6 - var30) / 2.0D / (double) var12 + 0.5D);

        tess.addVertexWithUV(var22, var26, var28, var32, var34);
        tess.addVertexWithUV(var22, var26, var30, var32, var35);
        tess.addVertexWithUV(var24, var26, var30, var33, var35);
        tess.addVertexWithUV(var24, var26, var28, var33, var34);
    }

    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float delta) {
        if (this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F) {
            double var10 = this.renderManager.getDistanceToCamera(entity.posX, entity.posY, entity.posZ);
            float var12 = (float) ((1.0D - var10 / 256.0D) * (double) this.shadowOpaque);
            if (var12 > 0.0F)
                this.renderShadow(entity, x, y, z, var12, delta);
        }

        if (entity.isBurning())
            this.renderEntityOnFire(entity, x, y, z, delta);
    }

    public FontRenderer getFontRenderer() {
        return this.renderManager.getFontRenderer();
    }
}
