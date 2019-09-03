package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class GuiSlot {
    protected final int top;
    protected final int bottom;
    protected final int posZ;
    private final Minecraft mc;
    private final int width;
    private final int height;
    private final int right;
    private final int left;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    private float initialClickY = -2.0F;
    private float scrollMultiplier;
    private float amountScrolled;
    private int selectedElement = -1;
    private long lastClicked = 0L;
    private boolean field_25123_p = true;
    private boolean field_27262_q;
    private int field_27261_r;

    public GuiSlot(Minecraft var1, int var2, int var3, int var4, int var5, int var6) {
        this.mc = var1;
        this.width = var2;
        this.height = var3;
        this.top = var4;
        this.bottom = var5;
        this.posZ = var6;
        this.left = 0;
        this.right = var2;
    }

    public void func_27258_a(boolean var1) {
        this.field_25123_p = var1;
    }

    protected void func_27259_a(boolean var1, int var2) {
        this.field_27262_q = var1;
        this.field_27261_r = var2;
        if (!var1) {
            this.field_27261_r = 0;
        }

    }

    protected abstract int getSize();

    protected abstract void elementClicked(int var1, boolean var2);

    protected abstract boolean isSelected(int var1);

    protected int getContentHeight() {
        return this.getSize() * this.posZ + this.field_27261_r;
    }

    protected abstract void drawBackground();

    protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

    protected void func_27260_a(int var1, int var2, Tessellator var3) {
    }

    protected void func_27255_a(int var1, int var2) {
    }

    protected void func_27257_b(int var1, int var2) {
    }

    public int func_27256_c(int var1, int var2) {
        int var3 = this.width / 2 - 110;
        int var4 = this.width / 2 + 110;
        int var5 = var2 - this.top - this.field_27261_r + (int) this.amountScrolled - 4;
        int var6 = var5 / this.posZ;
        return var1 >= var3 && var1 <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize() ? var6 : -1;
    }

    public void registerScrollButtons(List var1, int var2, int var3) {
        this.scrollUpButtonID = var2;
        this.scrollDownButtonID = var3;
    }

    private void bindAmountScrolled() {
        int var1 = this.getContentHeight() - (this.bottom - this.top - 4);
        if (var1 < 0) {
            var1 /= 2;
        }

        if (this.amountScrolled < 0.0F) {
            this.amountScrolled = 0.0F;
        }

        if (this.amountScrolled > (float) var1) {
            this.amountScrolled = (float) var1;
        }

    }

    public void actionPerformed(GuiButton var1) {
        if (var1.enabled) {
            if (var1.id == this.scrollUpButtonID) {
                this.amountScrolled -= (float) (this.posZ * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            } else if (var1.id == this.scrollDownButtonID) {
                this.amountScrolled += (float) (this.posZ * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            }

        }
    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawBackground();
        int var4 = this.getSize();
        int var5 = this.width / 2 + 124;
        int var6 = var5 + 6;
        if (Mouse.isButtonDown(0)) {
            if (this.initialClickY == -1.0F) {
                boolean var7 = true;
                if (var2 >= this.top && var2 <= this.bottom) {
                    int var8 = this.width / 2 - 110;
                    int var9 = this.width / 2 + 110;
                    int var10 = var2 - this.top - this.field_27261_r + (int) this.amountScrolled - 4;
                    int var11 = var10 / this.posZ;
                    if (var1 >= var8 && var1 <= var9 && var11 >= 0 && var10 >= 0 && var11 < var4) {
                        boolean var12 = var11 == this.selectedElement && System.currentTimeMillis() - this.lastClicked < 250L;
                        this.elementClicked(var11, var12);
                        this.selectedElement = var11;
                        this.lastClicked = System.currentTimeMillis();
                    } else if (var1 >= var8 && var1 <= var9 && var10 < 0) {
                        this.func_27255_a(var1 - var8, var2 - this.top + (int) this.amountScrolled - 4);
                        var7 = false;
                    }

                    if (var1 >= var5 && var1 <= var6) {
                        this.scrollMultiplier = -1.0F;
                        int var22 = this.getContentHeight() - (this.bottom - this.top - 4);
                        if (var22 < 1) {
                            var22 = 1;
                        }

                        int var13 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getContentHeight());
                        if (var13 < 32) {
                            var13 = 32;
                        }

                        if (var13 > this.bottom - this.top - 8) {
                            var13 = this.bottom - this.top - 8;
                        }

                        this.scrollMultiplier /= (float) (this.bottom - this.top - var13) / (float) var22;
                    } else {
                        this.scrollMultiplier = 1.0F;
                    }

                    if (var7) {
                        this.initialClickY = (float) var2;
                    } else {
                        this.initialClickY = -2.0F;
                    }
                } else {
                    this.initialClickY = -2.0F;
                }
            } else if (this.initialClickY >= 0.0F) {
                this.amountScrolled -= ((float) var2 - this.initialClickY) * this.scrollMultiplier;
                this.initialClickY = (float) var2;
            }
        } else {
            this.initialClickY = -1.0F;
        }

        this.bindAmountScrolled();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var17 = 32.0F;
        tess.startDrawingQuads();
        tess.setColorOpaque_I(2105376);
        tess.addVertexWithUV(this.left, this.bottom, 0.0D, (float) this.left / var17, (float) (this.bottom + (int) this.amountScrolled) / var17);
        tess.addVertexWithUV(this.right, this.bottom, 0.0D, (float) this.right / var17, (float) (this.bottom + (int) this.amountScrolled) / var17);
        tess.addVertexWithUV(this.right, this.top, 0.0D, (float) this.right / var17, (float) (this.top + (int) this.amountScrolled) / var17);
        tess.addVertexWithUV(this.left, this.top, 0.0D, (float) this.left / var17, (float) (this.top + (int) this.amountScrolled) / var17);
        tess.draw();
        int var18 = this.width / 2 - 92 - 16;
        int var19 = this.top + 4 - (int) this.amountScrolled;
        if (this.field_27262_q) {
            this.func_27260_a(var18, var19, tess);
        }

        for (int i = 0; i < var4; ++i) {
            int var23 = var19 + i * this.posZ + this.field_27261_r;
            int var25 = this.posZ - 4;
            if (var23 <= this.bottom && var23 + var25 >= this.top) {
                if (this.field_25123_p && this.isSelected(i)) {
                    int var14 = this.width / 2 - 110;
                    int var15 = this.width / 2 + 110;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tess.startDrawingQuads();
                    tess.setColorOpaque_I(8421504);
                    tess.addVertexWithUV(var14, var23 + var25 + 2, 0.0D, 0.0D, 1.0D);
                    tess.addVertexWithUV(var15, var23 + var25 + 2, 0.0D, 1.0D, 1.0D);
                    tess.addVertexWithUV(var15, var23 - 2, 0.0D, 1.0D, 0.0D);
                    tess.addVertexWithUV(var14, var23 - 2, 0.0D, 0.0D, 0.0D);
                    tess.setColorOpaque_I(0);
                    tess.addVertexWithUV(var14 + 1, var23 + var25 + 1, 0.0D, 0.0D, 1.0D);
                    tess.addVertexWithUV(var15 - 1, var23 + var25 + 1, 0.0D, 1.0D, 1.0D);
                    tess.addVertexWithUV(var15 - 1, var23 - 1, 0.0D, 1.0D, 0.0D);
                    tess.addVertexWithUV(var14 + 1, var23 - 1, 0.0D, 0.0D, 0.0D);
                    tess.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                this.drawSlot(i, var18, var23, var25, tess);
            }
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte var21 = 4;
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tess.startDrawingQuads();
        tess.setColorRGBA_I(0, 0);
        tess.addVertexWithUV(this.left, this.top + var21, 0.0D, 0.0D, 1.0D);
        tess.addVertexWithUV(this.right, this.top + var21, 0.0D, 1.0D, 1.0D);
        tess.setColorRGBA_I(0, 255);
        tess.addVertexWithUV(this.right, this.top, 0.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(this.left, this.top, 0.0D, 0.0D, 0.0D);
        tess.draw();
        tess.startDrawingQuads();
        tess.setColorRGBA_I(0, 255);
        tess.addVertexWithUV(this.left, this.bottom, 0.0D, 0.0D, 1.0D);
        tess.addVertexWithUV(this.right, this.bottom, 0.0D, 1.0D, 1.0D);
        tess.setColorRGBA_I(0, 0);
        tess.addVertexWithUV(this.right, this.bottom - var21, 0.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(this.left, this.bottom - var21, 0.0D, 0.0D, 0.0D);
        tess.draw();
        int var24 = this.getContentHeight() - (this.bottom - this.top - 4);
        if (var24 > 0) {
            int var26 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            if (var26 < 32) {
                var26 = 32;
            }

            if (var26 > this.bottom - this.top - 8) {
                var26 = this.bottom - this.top - 8;
            }

            int var27 = (int) this.amountScrolled * (this.bottom - this.top - var26) / var24 + this.top;
            if (var27 < this.top) {
                var27 = this.top;
            }

            tess.startDrawingQuads();
            tess.setColorRGBA_I(0, 255);
            tess.addVertexWithUV(var5, this.bottom, 0.0D, 0.0D, 1.0D);
            tess.addVertexWithUV(var6, this.bottom, 0.0D, 1.0D, 1.0D);
            tess.addVertexWithUV(var6, this.top, 0.0D, 1.0D, 0.0D);
            tess.addVertexWithUV(var5, this.top, 0.0D, 0.0D, 0.0D);
            tess.draw();
            tess.startDrawingQuads();
            tess.setColorRGBA_I(8421504, 255);
            tess.addVertexWithUV(var5, var27 + var26, 0.0D, 0.0D, 1.0D);
            tess.addVertexWithUV(var6, var27 + var26, 0.0D, 1.0D, 1.0D);
            tess.addVertexWithUV(var6, var27, 0.0D, 1.0D, 0.0D);
            tess.addVertexWithUV(var5, var27, 0.0D, 0.0D, 0.0D);
            tess.draw();
            tess.startDrawingQuads();
            tess.setColorRGBA_I(12632256, 255);
            tess.addVertexWithUV(var5, var27 + var26 - 1, 0.0D, 0.0D, 1.0D);
            tess.addVertexWithUV(var6 - 1, var27 + var26 - 1, 0.0D, 1.0D, 1.0D);
            tess.addVertexWithUV(var6 - 1, var27, 0.0D, 1.0D, 0.0D);
            tess.addVertexWithUV(var5, var27, 0.0D, 0.0D, 0.0D);
            tess.draw();
        }

        this.func_27257_b(var1, var2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void overlayBackground(int var1, int var2, int var3, int var4) {
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var6 = 32.0F;
        tess.startDrawingQuads();
        tess.setColorRGBA_I(4210752, var4);
        tess.addVertexWithUV(0.0D, var2, 0.0D, 0.0D, (float) var2 / var6);
        tess.addVertexWithUV(this.width, var2, 0.0D, (float) this.width / var6, (float) var2 / var6);
        tess.setColorRGBA_I(4210752, var3);
        tess.addVertexWithUV(this.width, var1, 0.0D, (float) this.width / var6, (float) var1 / var6);
        tess.addVertexWithUV(0.0D, var1, 0.0D, 0.0D, (float) var1 / var6);
        tess.draw();
    }
}
