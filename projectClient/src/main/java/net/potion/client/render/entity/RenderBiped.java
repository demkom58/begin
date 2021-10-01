package net.potion.client.render.entity;

import net.potion.block.Block;
import net.potion.client.model.ModelBiped;
import net.potion.entity.EntityLiving;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderBiped extends RenderLiving {
    protected ModelBiped modelBipedMain;

    public RenderBiped(ModelBiped var1, float var2) {
        super(var1, var2);
        this.modelBipedMain = var1;
    }

    @Override
    protected void renderEquippedItems(EntityLiving var1, float var2) {
        ItemStack var3 = var1.getHeldItem();
        if (var3 != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
            if (var3.itemID < 256 && RenderBlocks.renderItemIn3d(Block.BLOCKS_LIST[var3.itemID].getRenderType())) {
                float var6 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                var6 = var6 * 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(var6, -var6, var6);
            } else if (Item.ITEMS_LIST[var3.itemID].isFull3D()) {
                float var4 = 0.625F;
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(var4, -var4, var4);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
                float var5 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(var5, var5, var5);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            this.renderManager.itemRenderer.renderItem(var1, var3);
            GL11.glPopMatrix();
        }

    }
}
