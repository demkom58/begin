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

        GL11.glClearColor(256F, 256F, 256F, 256F);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, res.width, res.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);

        this.drawLogo(res);
        this.drawProgressBar(res);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        final String percent = "%" + String.format("%.1f", loadingModel.getPercent());
        drawString(fontRenderer, percent,
                (res.getScaledWidth() - fontRenderer.getStringWidth(percent)) / 2,
                res.getScaledHeight() - 45,
                0x323E96
        );

        final String title = loadingModel.getTitle();
        drawString(fontRenderer, title,
                (res.getScaledWidth() - fontRenderer.getStringWidth(title)) / 2,
                res.getScaledHeight() - 30,
                0x323E96
        );

        window.update();
    }

    public void drawLogo(ScaledResolution res) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final short width = 256;
        final short height = 256;

        int x = (res.getScaledWidth() - width) / 2, y = (res.getScaledHeight() - height) / 2, u = 0, v = 0;

        float uMul = 0.00390625F;
        float vMul = 0.00390625F;
        Tessellator tess = Tessellator.INSTANCE;
        tess.setColorOpaque_I(0xFFFFFF);
        tess.startDrawingQuads();
        tess.addVertexWithUV(x, y + height, 0.0D, (float) (u) * uMul, (float) (v + height) * vMul);
        tess.addVertexWithUV(x + width, y + height, 0.0D, (float) (u + width) * uMul, (float) (v + height) * vMul);
        tess.addVertexWithUV(x + width, y, 0.0D, (float) (u + width) * uMul, (float) (v) * vMul);
        tess.addVertexWithUV(x, y, 0.0D, (float) (u) * uMul, (float) (v) * vMul);

        tess.draw();
    }

    public void drawProgressBar(ScaledResolution res) {
        int progressWidth = (int) loadingModel.getPercent(res.getScaledWidth());

        GL11.glColor3f(0.36f, 0.42f, 0.87f);
        int x = 0, y = res.getScaledHeight() - 15, height = 10;

        final Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertex(x, y, 0);
        tess.addVertex(x + progressWidth, y, 0);
        tess.addVertex(x + progressWidth, y + height, 0);
        tess.addVertex(x, y + height, 0);
        tess.draw();
    }

}
