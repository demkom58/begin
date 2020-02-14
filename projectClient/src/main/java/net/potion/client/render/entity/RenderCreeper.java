package net.potion.client.render.entity;

import net.potion.client.model.ModelBase;
import net.potion.client.model.ModelCreeper;
import net.potion.entity.EntityLiving;
import net.potion.entity.monster.EntityCreeper;
import net.potion.util.MathHelper;
import org.lwjgl.opengl.ARBVertexBlend;
import org.lwjgl.opengl.GL11;

public class RenderCreeper extends RenderLiving {
    private ModelBase modelCreeper = new ModelCreeper(2.0F);

    public RenderCreeper() {
        super(new ModelCreeper(), 0.5F);
    }

    protected void updateCreeperScale(EntityCreeper creeper, float flashTime) {
        float time = creeper.setCreeperFlashTime(flashTime);
        float size = 1.0F + MathHelper.sin(time * 100.0F) * time * 0.01F;

        if (time < 0.0F)
            time = 0.0F;

        if (time > 1.0F)
            time = 1.0F;

        time *= time;
        time *= time;

        float width = (1.0F + time * 0.4F) * size;
        float height = (1.0F + time * 0.1F) / size;

        GL11.glScalef(width, height, width);
    }

    protected int updateCreeperColorMultiplier(EntityCreeper creeper, float var2, float flashTime) {
        float time = creeper.setCreeperFlashTime(flashTime);
        if ((int) (time * 10.0F) % 2 == 0)
            return 0;

        int var6 = (int) (time * 0.2F * 255.0F);
        if (var6 < 0)
            var6 = 0;

        if (var6 > 255)
            var6 = 255;

        short var7 = 255;
        short var8 = 255;
        short var9 = 255;
        return var6 << 24 | var7 << 16 | var8 << 8 | var9;
    }

    protected boolean func_27006_a(EntityCreeper creeper, int var2, float var3) {
        if (!creeper.getPowered())
            return false;

        if (var2 == 1) {
            float var4 = (float) creeper.ticksExisted + var3;
            this.loadTexture("/armor/power.png");
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            float var5 = var4 * 0.01F;
            float var6 = var4 * 0.01F;
            GL11.glTranslatef(var5, var6, 0.0F);
            this.setRenderPassModel(this.modelCreeper);
            GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
            GL11.glEnable(GL11.GL_BLEND);
            float var7 = 0.5F;
            GL11.glColor4f(var7, var7, var7, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            return true;
        }

        if (var2 == 2) {
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
        }


        return false;
    }

    protected boolean func_27007_b(EntityCreeper entity, int var2, float var3) {
        return false;
    }

    @Override
    protected void preRenderCallback(EntityLiving entity, float var2) {
        this.updateCreeperScale((EntityCreeper) entity, var2);
    }

    @Override
    protected int getColorMultiplier(EntityLiving entity, float var2, float var3) {
        return this.updateCreeperColorMultiplier((EntityCreeper) entity, var2, var3);
    }

    @Override
    protected boolean shouldRenderPass(EntityLiving entity, int var2, float var3) {
        return this.func_27006_a((EntityCreeper) entity, var2, var3);
    }

    @Override
    protected boolean inheritRenderPass(EntityLiving entity, int var2, float var3) {
        return this.func_27007_b((EntityCreeper) entity, var2, var3);
    }

}
