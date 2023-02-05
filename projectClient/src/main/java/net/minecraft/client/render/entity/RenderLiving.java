package net.minecraft.client.render.entity;

import net.hypnosis.render.Tessellator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.hypnosis.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class RenderLiving extends Render {
    protected ModelBase mainModel;
    protected ModelBase renderPassModel;

    public RenderLiving(ModelBase mainModel, float shadowSize) {
        this.mainModel = mainModel;
        this.shadowSize = shadowSize;
    }

    public void setRenderPassModel(ModelBase var1) {
        this.renderPassModel = var1;
    }

    public void doRenderLiving(EntityLiving entity, double x, double y, double z, float yaw, float delta) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.mainModel.onGround = this.renderSwingProgress(entity, delta);
        if (this.renderPassModel != null)
            this.renderPassModel.onGround = this.mainModel.onGround;

        this.mainModel.isRiding = entity.isRiding();
        if (this.renderPassModel != null)
            this.renderPassModel.isRiding = this.mainModel.isRiding;

        try {
            float corYawOf = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * delta;
            float corYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta;
            float corPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * delta;

            this.setRenderPosition(entity, x, y, z);
            float var13 = this.handleRotationFloat(entity, delta);
            this.rotateCorpse(entity, var13, corYawOf, delta);
            float var14 = 0.0625F;
            GL11.glEnable(GL15.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(entity, delta);
            GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
            float var15 = entity.field5 + (entity.field6 - entity.field5) * delta;
            float var16 = entity.field7 - entity.field6 * (1.0F - delta);
            if (var15 > 1.0F) {
                var15 = 1.0F;
            }

            this.loadDownloadableImageTexture(entity.skinUrl, entity.getEntityTexture());
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.setLivingAnimations(entity, var16, var15, delta);
            this.mainModel.render(var16, var15, var13, corYaw - corYawOf, corPitch, var14);

            for (int renderPass = 0; renderPass < 4; ++renderPass) {
                if (this.shouldRenderPass(entity, renderPass, delta)) {
                    this.renderPassModel.render(var16, var15, var13, corYaw - corYawOf, corPitch, var14);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            this.renderEquippedItems(entity, delta);
            float entityBrightness = entity.getEntityBrightness(delta);
            int colorMultiplier = this.getColorMultiplier(entity, entityBrightness, delta);
            if ((colorMultiplier >> 24 & 255) > 0 || entity.hurtTime > 0 || entity.deathTime > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(514);
                if (entity.hurtTime > 0 || entity.deathTime > 0) {
                    GL11.glColor4f(entityBrightness, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(var16, var15, var13, corYaw - corYawOf, corPitch, var14);

                    for (int renderPass = 0; renderPass < 4; ++renderPass) {
                        if (this.inheritRenderPass(entity, renderPass, delta)) {
                            GL11.glColor4f(entityBrightness, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(var16, var15, var13, corYaw - corYawOf, corPitch, var14);
                        }
                    }
                }

                if ((colorMultiplier >> 24 & 255) > 0) {
                    float r = (float) (colorMultiplier >> 16 & 255) / 255.0F;
                    float g = (float) (colorMultiplier >> 8 & 255) / 255.0F;
                    float b = (float) (colorMultiplier & 255) / 255.0F;
                    float a = (float) (colorMultiplier >> 24 & 255) / 255.0F;
                    GL11.glColor4f(r, g, b, a);
                    this.mainModel.render(var16, var15, var13, corYaw - corYawOf, corPitch, var14);

                    for (int renderPass = 0; renderPass < 4; ++renderPass) {
                        if (this.inheritRenderPass(entity, renderPass, delta)) {
                            GL11.glColor4f(r, g, b, a);
                            this.renderPassModel.render(var16, var15, var13, corYaw - corYawOf, corPitch, var14);
                        }
                    }
                }

                GL11.glDepthFunc(515);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL15.GL_RESCALE_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        this.passSpecialRender(entity, x, y, z);
    }

    protected void setRenderPosition(EntityLiving entity, double x, double y, double z) {
        GL11.glTranslatef((float) x, (float) y, (float) z);
    }

    protected void rotateCorpse(EntityLiving entity, float var2, float var3, float var4) {
        GL11.glRotatef(180.0F - var3, 0.0F, 1.0F, 0.0F);
        if (entity.deathTime > 0) {
            float var5 = MathHelper.sqrt(((float) entity.deathTime + var4 - 1.0F) / 20.0F * 1.6F);
            if (var5 > 1.0F)
                var5 = 1.0F;

            GL11.glRotatef(var5 * this.getDeathMaxRotation(entity), 0.0F, 0.0F, 1.0F);
        }

    }

    protected float renderSwingProgress(EntityLiving entity, float delta) {
        return entity.getSwingProgress(delta);
    }

    protected float handleRotationFloat(EntityLiving entity, float value) {
        return (float) entity.ticksExisted + value;
    }

    protected void renderEquippedItems(EntityLiving var1, float var2) {
    }

    protected boolean inheritRenderPass(EntityLiving entityL, int var2, float var3) {
        return this.shouldRenderPass(entityL, var2, var3);
    }

    protected boolean shouldRenderPass(EntityLiving entity, int var2, float var3) {
        return false;
    }

    protected float getDeathMaxRotation(EntityLiving var1) {
        return 90.0F;
    }

    protected int getColorMultiplier(EntityLiving var1, float var2, float var3) {
        return 0;
    }

    protected void preRenderCallback(EntityLiving var1, float var2) {
    }

    protected void passSpecialRender(EntityLiving var1, double var2, double var4, double var6) {
        if (MinecraftClient.isDebugInfoEnabled()) {
            this.renderLivingLabel(var1, Integer.toString(var1.entityId), var2, var4, var6, 64);
        }

    }

    protected void renderLivingLabel(EntityLiving entity, String text, double x, double y, double z, int maxDistance) {
        float distance = entity.getDistanceToEntity(this.renderManager.livingPlayer);
        if (distance > (float) maxDistance)
            return;

        FontRenderer fontRenderer = this.getFontRenderer();
        float var12 = 1.6F;
        float var13 = 0.016666668F * var12;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.0F, (float) y + 2.3F, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-var13, -var13, var13);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        byte height = 0;
        if (text.equals("deadmau5"))
            height = -10;

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Tessellator tess = Tessellator.INSTANCE;
        int stringWidth = fontRenderer.getStringWidth(text) / 2;

        tess.startDrawingQuads();
        tess.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tess.addVertex(-stringWidth - 1, -1 + height, 0.0D);
        tess.addVertex(-stringWidth - 1, 8 + height, 0.0D);
        tess.addVertex(stringWidth + 1, 8 + height, 0.0D);
        tess.addVertex(stringWidth + 1, -1 + height, 0.0D);
        tess.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, height, 553648127);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, height, -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.doRenderLiving((EntityLiving) entity, x, y, z, yaw, delta);
    }
}
