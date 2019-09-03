package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.texture.TexturePackBase;
import org.lwjgl.opengl.GL11;

import java.util.List;

class GuiTexturePackSlot extends GuiSlot {
    // $FF: synthetic field
    final GuiTexturePacks parentTexturePackGui;

    public GuiTexturePackSlot(GuiTexturePacks var1) {
        super(GuiTexturePacks.func_22124_a(var1), var1.width, var1.height, 32, var1.height - 55 + 4, 36);
        this.parentTexturePackGui = var1;
    }

    protected int getSize() {
        List<TexturePackBase> packs = GuiTexturePacks.func_22126_b(this.parentTexturePackGui).texturePackList.availableTexturePacks();
        return packs.size();
    }

    protected void elementClicked(int var1, boolean var2) {
        List<TexturePackBase> packs = GuiTexturePacks.func_22119_c(this.parentTexturePackGui).texturePackList.availableTexturePacks();
        GuiTexturePacks.func_22122_d(this.parentTexturePackGui).texturePackList.setTexturePack(packs.get(var1));
        GuiTexturePacks.func_22117_e(this.parentTexturePackGui).renderEngine.refreshTextures();
    }

    protected boolean isSelected(int var1) {
        List<TexturePackBase> packs = GuiTexturePacks.func_22118_f(this.parentTexturePackGui).texturePackList.availableTexturePacks();
        return GuiTexturePacks.func_22116_g(this.parentTexturePackGui).texturePackList.selectedTexturePack == packs.get(var1);
    }

    protected int getContentHeight() {
        return this.getSize() * 36;
    }

    protected void drawBackground() {
        this.parentTexturePackGui.drawDefaultBackground();
    }

    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator tess) {
        TexturePackBase packBase = GuiTexturePacks.func_22121_h(this.parentTexturePackGui).texturePackList.availableTexturePacks().get(var1);
        packBase.bindThumbnailTexture(GuiTexturePacks.func_22123_i(this.parentTexturePackGui));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tess.startDrawingQuads();
        tess.setColorOpaque_I(16777215);
        tess.addVertexWithUV(var2, var3 + var4, 0.0D, 0.0D, 1.0D);
        tess.addVertexWithUV(var2 + 32, var3 + var4, 0.0D, 1.0D, 1.0D);
        tess.addVertexWithUV(var2 + 32, var3, 0.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(var2, var3, 0.0D, 0.0D, 0.0D);
        tess.draw();
        this.parentTexturePackGui.drawString(GuiTexturePacks.func_22127_j(this.parentTexturePackGui), packBase.texturePackFileName, var2 + 32 + 2, var3 + 1, 16777215);
        this.parentTexturePackGui.drawString(GuiTexturePacks.func_22120_k(this.parentTexturePackGui), packBase.firstDescriptionLine, var2 + 32 + 2, var3 + 12, 8421504);
        this.parentTexturePackGui.drawString(GuiTexturePacks.func_22125_l(this.parentTexturePackGui), packBase.secondDescriptionLine, var2 + 32 + 2, var3 + 12 + 10, 8421504);
    }
}
