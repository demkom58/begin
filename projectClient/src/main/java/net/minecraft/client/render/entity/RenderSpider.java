package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySpider;
import org.lwjgl.opengl.GL11;

public class RenderSpider extends RenderLiving {
    public RenderSpider() {
        super(new ModelSpider(), 1.0F);
        this.setRenderPassModel(new ModelSpider());
    }

    protected float setSpiderDeathMaxRotation(EntitySpider var1) {
        return 180.0F;
    }

    protected boolean setSpiderEyeBrightness(EntitySpider var1, int var2, float var3) {
        if (var2 != 0)
            return false;

        this.loadTexture("/mob/spider_eyes.png");
        float var4 = (1.0F - var1.getEntityBrightness(1.0F)) * 0.5F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
        return true;
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    protected float getDeathMaxRotation(EntityLiving var1) {
        return this.setSpiderDeathMaxRotation((EntitySpider) var1);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    protected boolean shouldRenderPass(EntityLiving entity, int var2, float var3) {
        return this.setSpiderEyeBrightness((EntitySpider) entity, var2, var3);
    }
}
