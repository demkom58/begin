package net.minecraft.client.render.entity;

import net.hypnosis.render.Tessellator;
import net.hypnosis.util.math.MathConstants;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.hypnosis.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderPlayer extends RenderLiving {
    private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};
    private ModelBiped modelBipedMain;
    private ModelBiped modelArmorChestplate;
    private ModelBiped modelArmor;

    public RenderPlayer() {
        super(new ModelBiped(0.0F), 0.5F);
        this.modelBipedMain = (ModelBiped) this.mainModel;
        this.modelArmorChestplate = new ModelBiped(1.0F);
        this.modelArmor = new ModelBiped(0.5F);
    }

    protected boolean setArmorModel(EntityPlayer player, int slot, float var3) {
        ItemStack armorStack = player.inventory.armorItemInSlot(3 - slot);
        if (armorStack == null)
            return false;

        Item item = armorStack.getItem();
        if (!(item instanceof ItemArmor))
            return false;

        ItemArmor itemArmor = (ItemArmor) item;
        this.loadTexture("/armor/" + armorFilenamePrefix[itemArmor.renderIndex] + "_" + (slot == 2 ? 2 : 1) + ".png");

        ModelBiped modelBiped = slot == 2 ? this.modelArmor : this.modelArmorChestplate;
        modelBiped.bipedHead.showModel = slot == 0;
        modelBiped.bipedHeadwear.showModel = slot == 0;
        modelBiped.bipedBody.showModel = slot == 1 || slot == 2;
        modelBiped.bipedRightArm.showModel = slot == 1;
        modelBiped.bipedLeftArm.showModel = slot == 1;
        modelBiped.bipedRightLeg.showModel = slot == 2 || slot == 3;
        modelBiped.bipedLeftLeg.showModel = slot == 2 || slot == 3;

        this.setRenderPassModel(modelBiped);
        return true;
    }

    public void renderPlayer(EntityPlayer player, double x, double y, double z, float yaw, float delta) {
        ItemStack itemInHand = player.inventory.getCurrentItem();
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = itemInHand != null;
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = player.isSneaking();

        double modelY = y - (double) player.yOffset;
        if (player.isSneaking() && !(player instanceof EntityPlayerSP))
            modelY -= 0.125D;

        super.doRenderLiving(player, x, modelY, z, yaw, delta);
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = false;
    }

    protected void renderName(EntityPlayer player, double x, double y, double z) {
        if (!MinecraftClient.isGuiEnabled() || player == this.renderManager.livingPlayer)
            return;

        float var8 = 1.6F;
        float scale = 0.016666668F * var8;

        float distanceToEntity = player.getDistanceToEntity(this.renderManager.livingPlayer);
        float displayDistance = player.isSneaking() ? 32.0F : 64.0F;

        if (distanceToEntity >= displayDistance)
            return;

        String username = player.username;
        if (!player.isSneaking()) {
            if (player.isSleeping())
                this.renderLivingLabel(player, username, x, y - 1.5D, z, 64);
            else
                this.renderLivingLabel(player, username, x, y, z, 64);
            return;
        }

        FontRenderer fontRenderer = this.getFontRenderer();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.0F, (float) y + 2.3F, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef(0.0F, 0.25F / scale, 0.0F);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tess.startDrawingQuads();
        int stringWidth = fontRenderer.getStringWidth(username) / 2;
        tess.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tess.addVertex(-stringWidth - 1, -1.0D, 0.0D);
        tess.addVertex(-stringWidth - 1, 8.0D, 0.0D);
        tess.addVertex(stringWidth + 1, 8.0D, 0.0D);
        tess.addVertex(stringWidth + 1, -1.0D, 0.0D);
        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
        fontRenderer.drawString(username, -fontRenderer.getStringWidth(username) / 2, 0, 553648127);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    protected void renderSpecials(EntityPlayer player, float delta) {
        ItemStack armorStack = player.inventory.armorItemInSlot(3);
        if (armorStack != null && armorStack.getItem().shiftedIndex < 256) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            if (RenderBlocks.renderItemIn3d(Block.BLOCKS_LIST[armorStack.itemID].getRenderType())) {
                GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

                float s = 0.625F;
                GL11.glScalef(s, -s, s);
            }

            this.renderManager.itemRenderer.renderItem(player, armorStack);
            GL11.glPopMatrix();
        }

        if (player.username.equals("deadmau5") && this.loadDownloadableImageTexture(player.skinUrl, null)) {
            for (int renderPass = 0; renderPass < 2; ++renderPass) {
                float var5 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * delta - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * delta);
                float var6 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * delta;
                GL11.glPushMatrix();
                GL11.glRotatef(var5, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (float) (renderPass * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-var6, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-var5, 0.0F, 1.0F, 0.0F);

                float s = 1.3333334F;
                GL11.glScalef(s, s, s);
                this.modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        if (this.loadDownloadableImageTexture(player.playerCloakUrl, null)) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double var20 = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double) delta - (player.prevPosX + (player.posX - player.prevPosX) * (double) delta);
            double var26 = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double) delta - (player.prevPosY + (player.posY - player.prevPosY) * (double) delta);
            double var8 = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double) delta - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double) delta);
            float var10 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * delta;
            double var11 = MathHelper.sin(var10 * MathConstants.PI / 180.0F);
            double var13 = -MathHelper.cos(var10 * MathConstants.PI / 180.0F);
            float var15 = (float) var26 * 10.0F;

            if (var15 < -6.0F)
                var15 = -6.0F;

            if (var15 > 32.0F)
                var15 = 32.0F;


            float var16 = (float) (var20 * var11 + var8 * var13) * 100.0F;
            float var17 = (float) (var20 * var13 - var8 * var11) * 100.0F;

            if (var16 < 0.0F)
                var16 = 0.0F;

            float var18 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * delta;
            var15 = var15 + MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * delta) * 6.0F) * 32.0F * var18;
            if (player.isSneaking())
                var15 += 25.0F;

            GL11.glRotatef(6.0F + var16 / 2.0F + var15, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var17 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-var17 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }

        ItemStack itemInHand = player.inventory.getCurrentItem();
        if (itemInHand != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (player.fishEntity != null)
                itemInHand = new ItemStack(Item.STICK);

            if (itemInHand.itemID < 256 && RenderBlocks.renderItemIn3d(Block.BLOCKS_LIST[itemInHand.itemID].getRenderType())) {
                float scale = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                scale *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(scale, -scale, scale);
            } else if (Item.ITEMS_LIST[itemInHand.itemID].isFull3D()) {
                float var22 = 0.625F;
                if (Item.ITEMS_LIST[itemInHand.itemID].shouldRotateAroundWhenRendering()) {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(var22, -var22, var22);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
                float var23 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(var23, var23, var23);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            this.renderManager.itemRenderer.renderItem(player, itemInHand);
            GL11.glPopMatrix();
        }

    }

    protected void renderPlayerScale(EntityPlayer entity, float var2) {
        float scale = 0.9375F;
        GL11.glScalef(scale, scale, scale);
    }

    public void drawFirstPersonHand() {
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        this.modelBipedMain.bipedRightArm.render(0.0625F);
    }

    protected void renderPlayerSleep(EntityPlayer entity, double x, double y, double z) {
        if (entity.isEntityAlive() && entity.isSleeping()) {
            super.setRenderPosition(entity, x + (double) entity.renderOffsetX, y + (double) entity.renderOffsetY, z + (double) entity.renderOffsetZ);
        } else {
            super.setRenderPosition(entity, x, y, z);
        }

    }

    protected void rotatePlayer(EntityPlayer entity, float var2, float var3, float var4) {
        if (entity.isEntityAlive() && entity.isSleeping()) {
            GL11.glRotatef(entity.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.getDeathMaxRotation(entity), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
        } else {
            super.rotateCorpse(entity, var2, var3, var4);
        }

    }

    @Override
    protected void passSpecialRender(EntityLiving entity, double var2, double var4, double var6) {
        this.renderName((EntityPlayer) entity, var2, var4, var6);
    }

    @Override
    protected void preRenderCallback(EntityLiving entity, float var2) {
        this.renderPlayerScale((EntityPlayer) entity, var2);
    }

    @Override
    protected boolean shouldRenderPass(EntityLiving entity, int var2, float var3) {
        return this.setArmorModel((EntityPlayer) entity, var2, var3);
    }

    @Override
    protected void renderEquippedItems(EntityLiving entity, float delta) {
        this.renderSpecials((EntityPlayer) entity, delta);
    }

    @Override
    protected void rotateCorpse(EntityLiving entity, float var2, float var3, float var4) {
        this.rotatePlayer((EntityPlayer) entity, var2, var3, var4);
    }

    @Override
    protected void setRenderPosition(EntityLiving entity, double x, double y, double z) {
        this.renderPlayerSleep((EntityPlayer) entity, x, y, z);
    }

    @Override
    public void doRenderLiving(EntityLiving entity, double x, double y, double z, float yaw, float delta) {
        this.renderPlayer((EntityPlayer) entity, x, y, z, yaw, delta);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.renderPlayer((EntityPlayer) entity, x, y, z, yaw, delta);
    }
}
