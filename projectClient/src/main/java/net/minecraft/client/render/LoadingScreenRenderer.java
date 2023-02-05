package net.minecraft.client.render;

import net.hypnosis.monitor.Window;
import net.hypnosis.render.Tessellator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.IProgressUpdatable;
import net.minecraft.util.MinecraftError;
import org.lwjgl.opengl.ARBVertexBlend;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer implements IProgressUpdatable {
    private String field_1004_a = "";
    private MinecraftClient client;
    private String currentlyDisplayedText = "";
    private long field_1006_d = System.currentTimeMillis();
    private boolean field_1005_e = false;

    public LoadingScreenRenderer(MinecraftClient client) {
        this.client = client;
    }

    public void printText(String var1) {
        this.field_1005_e = false;
        this.func_597_c(var1);
    }

    @Override
    public void display(String var1) {
        this.field_1005_e = true;
        this.func_597_c(this.currentlyDisplayedText);
    }

    public void func_597_c(String var1) {
        if (!this.client.running) {
            if (!this.field_1005_e) {
                throw new MinecraftError();
            }
            return;
        }

        this.currentlyDisplayedText = var1;
        final Window window = this.client.window;
        ScaledResolution res = new ScaledResolution(this.client.gameSettings, window.getWidth(), window.getHeight());
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, res.width, res.height, 0.0D, 100.0D, 300.0D);
        GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
    }

    @Override
    public void displayLoadingString(String var1) {
        if (!this.client.running) {
            if (!this.field_1005_e) {
                throw new MinecraftError();
            }
            return;
        }

        this.field_1006_d = 0L;
        this.field_1004_a = var1;
        this.setLoadingProgress(-1);
        this.field_1006_d = 0L;
    }

    @Override
    public void setLoadingProgress(int var1) {
        if (!this.client.running) {
            if (!this.field_1005_e) {
                throw new MinecraftError();
            }
            return;
        }

        long mls = System.currentTimeMillis();
        if (mls - this.field_1006_d >= 20L) {
            this.field_1006_d = mls;

            final Window window = this.client.window;
            ScaledResolution res = new ScaledResolution(this.client.gameSettings, window.getWidth(), window.getHeight());
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();

            GL11.glClear(256);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, res.width, res.height, 0.0D, 100.0D, 300.0D);
            GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -200.0F);
            GL11.glClear(16640);
            Tessellator tess = Tessellator.INSTANCE;
            int bgId = this.client.renderEngine.getTexture("/gui/background.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, bgId);
            float var9 = 32.0F;
            tess.startDrawingQuads();
            tess.setColorOpaque_I(4210752);
            tess.addVertexWithUV(0.0D, height, 0.0D, 0.0D, (float) height / var9);
            tess.addVertexWithUV(width, height, 0.0D, (float) width / var9, (float) height / var9);
            tess.addVertexWithUV(width, 0.0D, 0.0D, (float) width / var9, 0.0D);
            tess.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
            tess.draw();
            if (var1 >= 0) {
                byte var10 = 100;
                byte var11 = 2;
                int var12 = width / 2 - var10 / 2;
                int var13 = height / 2 + 16;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tess.startDrawingQuads();
                tess.setColorOpaque_I(8421504);
                tess.addVertex(var12, var13, 0.0D);
                tess.addVertex(var12, var13 + var11, 0.0D);
                tess.addVertex(var12 + var10, var13 + var11, 0.0D);
                tess.addVertex(var12 + var10, var13, 0.0D);
                tess.setColorOpaque_I(8454016);
                tess.addVertex(var12, var13, 0.0D);
                tess.addVertex(var12, var13 + var11, 0.0D);
                tess.addVertex(var12 + var1, var13 + var11, 0.0D);
                tess.addVertex(var12 + var1, var13, 0.0D);
                tess.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            this.client.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (width - this.client.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, height / 2 - 4 - 16, 16777215);
            this.client.fontRenderer.drawStringWithShadow(this.field_1004_a, (width - this.client.fontRenderer.getStringWidth(this.field_1004_a)) / 2, height / 2 - 4 + 8, 16777215);
            client.window.update();

            try {
                Thread.yield();
            } catch (Exception ignored) {
            }

        }
    }
}
