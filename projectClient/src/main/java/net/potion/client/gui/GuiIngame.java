package net.potion.client.gui;

import net.hypnosis.monitor.Window;
import net.hypnosis.render.Tessellator;
import net.potion.block.Block;
import net.potion.client.ChatLine;
import net.potion.client.PotionClient;
import net.potion.client.render.FontRenderer;
import net.potion.client.render.RenderHelper;
import net.potion.client.render.ScaledResolution;
import net.potion.client.render.entity.RenderItem;
import net.potion.inventory.InventoryPlayer;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.util.CommonUtil;
import net.hypnosis.util.math.MathHelper;
import net.potion.util.StringTranslate;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuiIngame extends Gui {
    private static RenderItem itemRenderer = new RenderItem();
    public String field_933_a = null;
    public float damageGuiPartialTime;
    float prevVignetteBrightness = 1.0F;
    private List<ChatLine> chatMessageList = new ArrayList<>();
    private Random rand = new Random();
    private PotionClient potion;
    private int updateCounter = 0;
    private String recordPlaying = "";
    private int recordPlayingUpFor = 0;
    private boolean field_22065_l = false;

    public GuiIngame(PotionClient potion) {
        this.potion = potion;
    }

    public void renderGameOverlay(float partialTicks, boolean var2, int var3, int var4) {
        Window window = this.potion.window;
        ScaledResolution var5 = new ScaledResolution(this.potion.gameSettings, window.getWidth(), window.getHeight());
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        FontRenderer fontRenderer = this.potion.fontRenderer;
        this.potion.entityRenderer.func_905_b();
        GL11.glEnable(GL11.GL_BLEND);
        if (PotionClient.isFancyGraphicsEnabled()) {
            this.renderVignette(this.potion.thePlayer.getEntityBrightness(partialTicks), var6, var7);
        }

        ItemStack var9 = this.potion.thePlayer.inventory.armorItemInSlot(3);
        if (!this.potion.gameSettings.thirdPersonView && var9 != null && var9.itemID == Block.PUMPKIN.blockID) {
            this.renderPumpkinBlur(var6, var7);
        }

        float var10 = this.potion.thePlayer.prevTimeInPortal + (this.potion.thePlayer.timeInPortal - this.potion.thePlayer.prevTimeInPortal) * partialTicks;
        if (var10 > 0.0F) {
            this.renderPortalOverlay(var10, var6, var7);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("/gui/gui.png"));
        InventoryPlayer var11 = this.potion.thePlayer.inventory;
        this.zLevel = -90.0F;
        this.drawTexturedModalRect(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
        this.drawTexturedModalRect(var6 / 2 - 91 - 1 + var11.currentItem * 20, var7 - 22 - 1, 0, 22, 24, 22);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("/gui/icons.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
        this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);
        boolean var12 = this.potion.thePlayer.heartsLife / 3 % 2 == 1;
        if (this.potion.thePlayer.heartsLife < 10) {
            var12 = false;
        }

        int health = this.potion.thePlayer.health;
        int prevHealth = this.potion.thePlayer.prevHealth;
        this.rand.setSeed(this.updateCounter * 312871);
        if (this.potion.playerController.shouldDrawHUD()) {
            int var15 = this.potion.thePlayer.getPlayerArmorValue();

            for (int i = 0; i < 10; ++i) {
                int var17 = var7 - 32;
                if (var15 > 0) {
                    int var18 = var6 / 2 + 91 - i * 8 - 9;
                    if (i * 2 + 1 < var15) {
                        this.drawTexturedModalRect(var18, var17, 34, 9, 9, 9);
                    }

                    if (i * 2 + 1 == var15) {
                        this.drawTexturedModalRect(var18, var17, 25, 9, 9, 9);
                    }

                    if (i * 2 + 1 > var15) {
                        this.drawTexturedModalRect(var18, var17, 16, 9, 9, 9);
                    }
                }

                byte var40 = 0;
                if (var12) {
                    var40 = 1;
                }

                int var19 = var6 / 2 - 91 + i * 8;
                if (health <= 4) {
                    var17 += this.rand.nextInt(2);
                }

                this.drawTexturedModalRect(var19, var17, 16 + var40 * 9, 0, 9, 9);
                if (var12) {
                    if (i * 2 + 1 < prevHealth) {
                        this.drawTexturedModalRect(var19, var17, 70, 0, 9, 9);
                    }

                    if (i * 2 + 1 == prevHealth) {
                        this.drawTexturedModalRect(var19, var17, 79, 0, 9, 9);
                    }
                }

                if (i * 2 + 1 < health) {
                    this.drawTexturedModalRect(var19, var17, 52, 0, 9, 9);
                }

                if (i * 2 + 1 == health) {
                    this.drawTexturedModalRect(var19, var17, 61, 0, 9, 9);
                }
            }

            if (this.potion.thePlayer.isInsideOfMaterial(Material.WATER)) {
                int var29 = (int) Math.ceil((double) (this.potion.thePlayer.air - 2) * 10.0D / 300.0D);
                int var34 = (int) Math.ceil((double) this.potion.thePlayer.air * 10.0D / 300.0D) - var29;

                for (int i = 0; i < var29 + var34; ++i) {
                    if (i < var29) {
                        this.drawTexturedModalRect(var6 / 2 - 91 + i * 8, var7 - 32 - 9, 16, 18, 9, 9);
                    } else {
                        this.drawTexturedModalRect(var6 / 2 - 91 + i * 8, var7 - 32 - 9, 25, 18, 9, 9);
                    }
                }
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL15.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        for (int i = 0; i < 9; ++i) {
            int var30 = var6 / 2 - 90 + i * 20 + 2;
            int var35 = var7 - 16 - 3;
            this.renderInventorySlot(i, var30, var35, partialTicks);
        }

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL15.GL_RESCALE_NORMAL);
        if (this.potion.thePlayer.func_22060_M() > 0) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int var25 = this.potion.thePlayer.func_22060_M();
            float var31 = (float) var25 / 100.0F;
            if (var31 > 1.0F) {
                var31 = 1.0F - (float) (var25 - 100) / 10.0F;
            }

            int var36 = (int) (220.0F * var31) << 24 | 1052704;
            this.drawRect(0, 0, var6, var7, var36);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (this.potion.gameSettings.showDebugInfo) {
            GL11.glPushMatrix();
            if (PotionClient.hasPaidCheckTime > 0L) {
                GL11.glTranslatef(0.0F, 32.0F, 0.0F);
            }

            fontRenderer.drawStringWithShadow("Potion in-dev 0.0.1 (" + this.potion.debug + ")", 2, 2, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(this.potion.getDebugInfoRenders(), 2, 12, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(this.potion.getDebugInfoEntities(), 2, 22, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(this.potion.func_6245_o(), 2, 32, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(this.potion.func_21002_o(), 2, 42, 0xFFFFFF);

            long maxMemory = Runtime.getRuntime().maxMemory();
            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            long usedMemory = totalMemory - freeMemory;

            String usedMemoryInfo = "Used memory: " + usedMemory * 100L / maxMemory + "% (" + usedMemory / 1024L / 1024L + "MB) of " + maxMemory / 1024L / 1024L + "MB";
            this.drawString(fontRenderer, usedMemoryInfo, var6 - fontRenderer.getStringWidth(usedMemoryInfo) - 2, 2, 0xE0E0E0);

            String allocatedMemoryInfo = "Allocated memory: " + totalMemory * 100L / maxMemory + "% (" + totalMemory / 1024L / 1024L + "MB)";
            this.drawString(fontRenderer, allocatedMemoryInfo, var6 - fontRenderer.getStringWidth(allocatedMemoryInfo) - 2, 12, 0xE0E0E0);

            this.drawString(fontRenderer, "x: " + this.potion.thePlayer.posX, 2, 64, 0xE0E0E0);
            this.drawString(fontRenderer, "y: " + this.potion.thePlayer.posY, 2, 72, 0xE0E0E0);
            this.drawString(fontRenderer, "z: " + this.potion.thePlayer.posZ, 2, 80, 0xE0E0E0);

            int directionId = (MathHelper.floor((double) (this.potion.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
            this.drawString(fontRenderer, "look: " + CommonUtil.getDirectionName(directionId) + " (f: " + directionId + ")", 2, 88, 0xE0E0E0);
            GL11.glPopMatrix();
        }

        if (this.recordPlayingUpFor > 0) {
            float var27 = (float) this.recordPlayingUpFor - partialTicks;
            int var32 = (int) (var27 * 256.0F / 20.0F);
            if (var32 > 255) {
                var32 = 255;
            }

            if (var32 > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) (var6 / 2), (float) (var7 - 48), 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int var38 = 0xFFFFFF;
                if (this.field_22065_l) {
                    var38 = Color.HSBtoRGB(var27 / 50.0F, 0.7F, 0.6F) & 0xFFFFFF;
                }

                fontRenderer.drawString(this.recordPlaying, -fontRenderer.getStringWidth(this.recordPlaying) / 2, -4, var38 + (var32 << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }

        byte var28 = 10;
        boolean var33 = false;
        if (this.potion.currentScreen instanceof GuiChat) {
            var28 = 20;
            var33 = true;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, (float) (var7 - 48), 0.0F);

        for (int i = 0; i < this.chatMessageList.size() && i < var28; ++i) {
            if (this.chatMessageList.get(i).updateCounter < 200 || var33) {
                double var42 = (double) this.chatMessageList.get(i).updateCounter / 200.0D;
                var42 = 1.0D - var42;
                var42 = var42 * 10.0D;
                if (var42 < 0.0D) {
                    var42 = 0.0D;
                }

                if (var42 > 1.0D) {
                    var42 = 1.0D;
                }

                var42 = var42 * var42;
                int var20 = (int) (255.0D * var42);
                if (var33) {
                    var20 = 255;
                }

                if (var20 > 0) {
                    byte var47 = 2;
                    int var22 = -i * 9;
                    String message = this.chatMessageList.get(i).message;
                    this.drawRect(var47, var22 - 1, var47 + 320, var22 + 8, var20 / 2 << 24);
                    GL11.glEnable(GL11.GL_BLEND);
                    fontRenderer.drawStringWithShadow(message, var47, var22, 0xFFFFFF + (var20 << 24));
                }
            }
        }

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderPumpkinBlur(int var1, int var2) {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertexWithUV(0.0D, var2, -90.0D, 0.0D, 1.0D);
        tess.addVertexWithUV(var1, var2, -90.0D, 1.0D, 1.0D);
        tess.addVertexWithUV(var1, 0.0D, -90.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tess.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderVignette(float var1, int var2, int var3) {
        var1 = 1.0F - var1;
        if (var1 < 0.0F) {
            var1 = 0.0F;
        }

        if (var1 > 1.0F) {
            var1 = 1.0F;
        }

        this.prevVignetteBrightness = (float) ((double) this.prevVignetteBrightness + (double) (var1 - this.prevVignetteBrightness) * 0.01D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
        GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("%blur%/misc/vignette.png"));
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertexWithUV(0.0D, var3, -90.0D, 0.0D, 1.0D);
        tess.addVertexWithUV(var2, var3, -90.0D, 1.0D, 1.0D);
        tess.addVertexWithUV(var2, 0.0D, -90.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tess.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void renderPortalOverlay(float var1, int var2, int var3) {
        if (var1 < 1.0F) {
            var1 = var1 * var1;
            var1 = var1 * var1;
            var1 = var1 * 0.8F + 0.2F;
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("/terrain.png"));
        float var4 = (float) (Block.PORTAL.blockIndexInTexture % 16) / 16.0F;
        float var5 = (float) (Block.PORTAL.blockIndexInTexture / 16) / 16.0F;
        float var6 = (float) (Block.PORTAL.blockIndexInTexture % 16 + 1) / 16.0F;
        float var7 = (float) (Block.PORTAL.blockIndexInTexture / 16 + 1) / 16.0F;
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertexWithUV(0.0D, var3, -90.0D, var4, var7);
        tess.addVertexWithUV(var2, var3, -90.0D, var6, var7);
        tess.addVertexWithUV(var2, 0.0D, -90.0D, var6, var5);
        tess.addVertexWithUV(0.0D, 0.0D, -90.0D, var4, var5);
        tess.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderInventorySlot(int var1, int var2, int var3, float var4) {
        ItemStack stack = this.potion.thePlayer.inventory.mainInventory[var1];
        if (stack != null) {
            float var6 = (float) stack.animationsToGo - var4;
            if (var6 > 0.0F) {
                GL11.glPushMatrix();
                float var7 = 1.0F + var6 / 5.0F;
                GL11.glTranslatef((float) (var2 + 8), (float) (var3 + 12), 0.0F);
                GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float) (-(var2 + 8)), (float) (-(var3 + 12)), 0.0F);
            }

            itemRenderer.renderItemIntoGUI(this.potion.fontRenderer, this.potion.renderEngine, stack, var2, var3);
            if (var6 > 0.0F) {
                GL11.glPopMatrix();
            }

            itemRenderer.renderItemOverlayIntoGUI(this.potion.fontRenderer, this.potion.renderEngine, stack, var2, var3);
        }
    }

    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }

        ++this.updateCounter;

        for (int i = 0; i < this.chatMessageList.size(); ++i) {
            ++this.chatMessageList.get(i).updateCounter;
        }

    }

    public void clearChatMessages() {
        this.chatMessageList.clear();
    }

    public void addChatMessage(String message) {
        while (this.potion.fontRenderer.getStringWidth(message) > 320) {
            int i;
            for (i = 1; i < message.length() && this.potion.fontRenderer.getStringWidth(message.substring(0, i + 1)) <= 320; ++i) {
            }

            this.addChatMessage(message.substring(0, i));
            message = message.substring(i);
        }

        this.chatMessageList.add(0, new ChatLine(message));

        while (this.chatMessageList.size() > 50) {
            this.chatMessageList.remove(this.chatMessageList.size() - 1);
        }

    }

    public void setRecordPlayingMessage(String var1) {
        this.recordPlaying = "Now playing: " + var1;
        this.recordPlayingUpFor = 60;
        this.field_22065_l = true;
    }

    public void addChatMessageTranslate(String localeMessage) {
        StringTranslate translate = StringTranslate.getInstance();
        String message = translate.translateKey(localeMessage);
        this.addChatMessage(message);
    }
}
