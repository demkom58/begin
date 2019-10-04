package net.minecraft.client.gui;

import net.minecraft.client.render.FontRenderer;
import net.hypnosis.render.Tessellator;
import org.lwjgl.opengl.GL11;

public class Gui {
    protected float zLevel = 0.0F;

    protected void func_27100_a(int var1, int var2, int var3, int color) {
        if (var2 < var1) {
            int var5 = var1;
            var1 = var2;
            var2 = var5;
        }

        this.drawRect(var1, var3, var2 + 1, var3 + 1, color);
    }

    protected void func_27099_b(int var1, int var2, int var3, int color) {
        if (var3 < var2) {
            int var5 = var2;
            var2 = var3;
            var3 = var5;
        }

        this.drawRect(var1, var2 + 1, var1 + 1, var3, color);
    }

    protected void drawRect(int var1, int var2, int var3, int var4, int color) {
        if (var1 < var3) {
            int var6 = var1;
            var1 = var3;
            var3 = var6;
        }

        if (var2 < var4) {
            int var11 = var2;
            var2 = var4;
            var4 = var11;
        }

        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, a);
        tess.startDrawingQuads();
        tess.addVertex(var1, var4, 0.0D);
        tess.addVertex(var3, var4, 0.0D);
        tess.addVertex(var3, var2, 0.0D);
        tess.addVertex(var1, var2, 0.0D);
        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void drawGradientRect(int var1, int var2, int var3, int var4, int startColor, int endColor) {
        float sa = (float) (startColor >> 24 & 255) / 255.0F;
        float sr = (float) (startColor >> 16 & 255) / 255.0F;
        float sg = (float) (startColor >> 8 & 255) / 255.0F;
        float sb = (float) (startColor & 255) / 255.0F;

        float ea = (float) (endColor >> 24 & 255) / 255.0F;
        float er = (float) (endColor >> 16 & 255) / 255.0F;
        float eg = (float) (endColor >> 8 & 255) / 255.0F;
        float eb = (float) (endColor & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.setColorRGBA_F(sr, sg, sb, sa);
        tess.addVertex(var3, var2, 0.0D);
        tess.addVertex(var1, var2, 0.0D);
        tess.setColorRGBA_F(er, eg, eb, ea);
        tess.addVertex(var1, var4, 0.0D);
        tess.addVertex(var3, var4, 0.0D);
        tess.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void drawCenteredString(FontRenderer renderer, String text, int x, int y, int color) {
        renderer.drawStringWithShadow(text, x - renderer.getStringWidth(text) / 2, y, color);
    }

    public void drawString(FontRenderer renderer, String text, int x, int y, int color) {
        renderer.drawStringWithShadow(text, x, y, color);
    }

    public void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertexWithUV(var1 + 0, var2 + var6, this.zLevel, (float) (var3 + 0) * var7, (float) (var4 + var6) * var8);
        tess.addVertexWithUV(var1 + var5, var2 + var6, this.zLevel, (float) (var3 + var5) * var7, (float) (var4 + var6) * var8);
        tess.addVertexWithUV(var1 + var5, var2 + 0, this.zLevel, (float) (var3 + var5) * var7, (float) (var4 + 0) * var8);
        tess.addVertexWithUV(var1 + 0, var2 + 0, this.zLevel, (float) (var3 + 0) * var7, (float) (var4 + 0) * var8);
        tess.draw();
    }
}
