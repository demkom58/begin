package net.minecraft.client.loading;

import net.hypnosis.monitor.Window;
import net.hypnosis.render.Tessellator;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ClientLoadGui extends Gui {
    private final Window window;
    private final GameSettings gameSettings;
    private final RenderEngine renderEngine;
    private final FontRenderer fontRenderer;
    private final LoadingModel loadingModel;

    public ClientLoadGui(Window window, GameSettings gameSettings, RenderEngine renderEngine, FontRenderer fontRenderer, LoadingModel loadingModel) {
        this.window = window;
        this.gameSettings = gameSettings;
        this.renderEngine = renderEngine;
        this.fontRenderer = fontRenderer;
        this.loadingModel = loadingModel;
    }

    public void update() {
        if (window.isCloseRequested())
            System.exit(0);

        final ScaledResolution res = new ScaledResolution(gameSettings, window.getWidth(), window.getHeight());

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, res.width, res.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
        tess.startDrawingQuads();
        tess.setColorOpaque_I(16777215);
        tess.addVertexWithUV(0.0D, window.getHeight(), 0.0D, 0.0D, 0.0D);
        tess.addVertexWithUV(window.getWidth(), window.getHeight(), 0.0D, 0.0D, 0.0D);
        tess.addVertexWithUV(window.getWidth(), 0.0D, 0.0D, 0.0D, 0.0D);
        tess.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        tess.draw();

        short width = 256;
        short height = 256;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tess.setColorOpaque_I(16777215);
        this.drawTess((res.getScaledWidth() - width) / 2, (res.getScaledHeight() - height) / 2, 0, 0, width, height);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        final String percent = "%" + loadingModel.getPercent();
        drawString(fontRenderer, percent,
                (res.getScaledWidth() - fontRenderer.getStringWidth(percent)) / 2,
                res.getScaledHeight() - 35,
                0xAAFFAA
        );

        final String title = loadingModel.getTitle();
        drawString(fontRenderer, title,
                (res.getScaledWidth() - fontRenderer.getStringWidth(title)) / 2,
                res.getScaledHeight() - 20,
                0xAAFFAA
        );

        window.update();
    }

    public void drawTess(int x, int y, int u, int v, int var5, int var6) {
        float uMul = 0.00390625F;
        float vMul = 0.00390625F;
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertexWithUV(x, y + var6, 0.0D, (float) (u) * uMul, (float) (v + var6) * vMul);
        tess.addVertexWithUV(x + var5, y + var6, 0.0D, (float) (u + var5) * uMul, (float) (v + var6) * vMul);
        tess.addVertexWithUV(x + var5, y, 0.0D, (float) (u + var5) * uMul, (float) (v) * vMul);
        tess.addVertexWithUV(x, y, 0.0D, (float) (u) * uMul, (float) (v) * vMul);
        tess.draw();
    }

}
