package net.potion.client.gui;

import net.potion.achievement.AchievementList;
import net.potion.client.render.RenderHelper;
import net.potion.client.render.RenderManager;
import net.potion.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class GuiInventory extends GuiContainer {
    private float xSize_lo;
    private float ySize_lo;

    public GuiInventory(EntityPlayer player) {
        super(player.inventorySlots);
        this.inputable = false;
        player.addStat(AchievementList.openInventory, 1);
    }

    @Override
    public void initGui() {
        this.buttons.clear();
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Crafting", 86, 16, 4210752);
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        super.drawScreen(var1, var2, partialTicks);
        this.xSize_lo = (float) var1;
        this.ySize_lo = (float) var2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1) {
        int var2 = this.potion.renderEngine.getTexture("/gui/inventory.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.potion.renderEngine.bindTexture(var2);
        int var3 = (this.width - this.xSize) / 2;
        int var4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var3, var4, 0, 0, this.xSize, this.ySize);
        GL11.glEnable(GL15.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (var3 + 51), (float) (var4 + 75), 50.0F);
        float var5 = 30.0F;
        GL11.glScalef(-var5, var5, var5);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float yawOffset = this.potion.thePlayer.renderYawOffset;
        float yaw = this.potion.thePlayer.rotationYaw;
        float pitch = this.potion.thePlayer.rotationPitch;
        float var9 = (float) (var3 + 51) - this.xSize_lo;
        float var10 = (float) (var4 + 75 - 50) - this.ySize_lo;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float) Math.atan(var10 / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        this.potion.thePlayer.renderYawOffset = (float) Math.atan(var9 / 40.0F) * 20.0F;
        this.potion.thePlayer.rotationYaw = (float) Math.atan(var9 / 40.0F) * 40.0F;
        this.potion.thePlayer.rotationPitch = -((float) Math.atan(var10 / 40.0F)) * 20.0F;
        this.potion.thePlayer.entityBrightness = 1.0F;
        GL11.glTranslatef(0.0F, this.potion.thePlayer.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(this.potion.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        this.potion.thePlayer.entityBrightness = 0.0F;
        this.potion.thePlayer.renderYawOffset = yawOffset;
        this.potion.thePlayer.rotationYaw = yaw;
        this.potion.thePlayer.rotationPitch = pitch;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL15.GL_RESCALE_NORMAL);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.potion.displayGuiScreen(new GuiAchievements(this.potion.statFileWriter));
        }

        if (button.id == 1) {
            this.potion.displayGuiScreen(new GuiStats(this, this.potion.statFileWriter));
        }

    }
}
