package net.potion.client.gui;

import net.hypnosis.render.Tessellator;
import net.potion.client.render.texture.TexturePackBase;
import org.lwjgl.opengl.GL11;

import java.util.List;

class GuiTexturePackSlot extends GuiSlot {
    // $FF: synthetic field
    final GuiTexturePacks parentTexturePackGui;

    public GuiTexturePackSlot(GuiTexturePacks var1) {
        super(var1.potion, var1.width, var1.height, 32, var1.height - 55 + 4, 36);
        this.parentTexturePackGui = var1;
    }

    @Override
    protected int getSize() {
        List<TexturePackBase> packs = this.parentTexturePackGui.potion.texturePackList.availableTexturePacks();
        return packs.size();
    }

    @Override
    protected void elementClicked(int var1, boolean var2) {
        List<TexturePackBase> packs = this.parentTexturePackGui.potion.texturePackList.availableTexturePacks();
        this.parentTexturePackGui.potion.texturePackList.setTexturePack(packs.get(var1));
        this.parentTexturePackGui.potion.renderEngine.refreshTextures();
    }

    @Override
    protected boolean isSelected(int var1) {
        List<TexturePackBase> packs = this.parentTexturePackGui.potion.texturePackList.availableTexturePacks();
        return this.parentTexturePackGui.potion.texturePackList.selectedTexturePack == packs.get(var1);
    }

    @Override
    protected int getContentHeight() {
        return this.getSize() * 36;
    }

    @Override
    protected void drawBackground() {
        this.parentTexturePackGui.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator tess) {
        TexturePackBase packBase = this.parentTexturePackGui.potion.texturePackList.availableTexturePacks().get(var1);
        packBase.bindThumbnailTexture(this.parentTexturePackGui.potion);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tess.startDrawingQuads();
        tess.setColorOpaque_I(16777215);
        tess.addVertexWithUV(var2, var3 + var4, 0.0D, 0.0D, 1.0D);
        tess.addVertexWithUV(var2 + 32, var3 + var4, 0.0D, 1.0D, 1.0D);
        tess.addVertexWithUV(var2 + 32, var3, 0.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(var2, var3, 0.0D, 0.0D, 0.0D);
        tess.draw();
        this.parentTexturePackGui.drawString(this.parentTexturePackGui.fontRenderer, packBase.texturePackFileName, var2 + 32 + 2, var3 + 1, 16777215);
        this.parentTexturePackGui.drawString(this.parentTexturePackGui.fontRenderer, packBase.firstDescriptionLine, var2 + 32 + 2, var3 + 12, 8421504);
        this.parentTexturePackGui.drawString(this.parentTexturePackGui.fontRenderer, packBase.secondDescriptionLine, var2 + 32 + 2, var3 + 12 + 10, 8421504);
    }
}
