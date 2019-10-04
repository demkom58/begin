package net.minecraft.client.gui;

import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.InventoryPlayer;
import net.minecraft.tileentity.TileEntityDispenser;
import org.lwjgl.opengl.GL11;

public class GuiDispenser extends GuiContainer {
    public GuiDispenser(InventoryPlayer var1, TileEntityDispenser var2) {
        super(new ContainerDispenser(var1, var2));
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Dispenser", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1) {
        int textureId = this.mc.renderEngine.getTexture("/gui/trap.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(textureId);
        int var3 = (this.width - this.xSize) / 2;
        int var4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var3, var4, 0, 0, this.xSize, this.ySize);
    }
}
