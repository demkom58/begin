package net.potion.client.render.entity;

import net.potion.client.model.ModelBase;
import net.potion.entity.EntityLiving;
import net.potion.entity.passive.EntitySheep;
import org.lwjgl.opengl.GL11;

public class RenderSheep extends RenderLiving {
    public RenderSheep(ModelBase var1, ModelBase var2, float var3) {
        super(var1, var3);
        this.setRenderPassModel(var2);
    }

    protected boolean setWoolColorAndRender(EntitySheep var1, int var2, float var3) {
        if (var2 == 0 && !var1.getSheared()) {
            this.loadTexture("/mob/sheep_fur.png");
            float var4 = var1.getEntityBrightness(var3);
            int var5 = var1.getFleeceColor();
            GL11.glColor3f(var4 * EntitySheep.fleeceColorTable[var5][0], var4 * EntitySheep.fleeceColorTable[var5][1], var4 * EntitySheep.fleeceColorTable[var5][2]);
            return true;
        } else {
            return false;
        }
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    protected boolean shouldRenderPass(EntityLiving entity, int var2, float var3) {
        return this.setWoolColorAndRender((EntitySheep) entity, var2, var3);
    }
}
