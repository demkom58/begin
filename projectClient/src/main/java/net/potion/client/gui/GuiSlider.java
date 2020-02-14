package net.potion.client.gui;

import net.potion.client.PotionClient;
import org.lwjgl.opengl.GL11;

public class GuiSlider extends GuiButton {
    public float sliderValue = 1.0F;
    public boolean dragging = false;
    private EnumOption idFloat = null;

    public GuiSlider(int var1, int var2, int var3, EnumOption var4, String var5, float var6) {
        super(var1, var2, var3, 150, 20, var5);
        this.idFloat = var4;
        this.sliderValue = var6;
    }

    @Override
    protected int getHoverState(boolean var1) {
        return 0;
    }

    @Override
    protected void mouseDragged(PotionClient potion, int var2, int var3) {
        if (!this.enabled2)
            return;

        if (this.dragging) {
            this.sliderValue = (float) (var2 - (this.xPosition + 4)) / (float) (this.width - 8);
            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }

            potion.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
            this.displayString = potion.gameSettings.getKeyBinding(this.idFloat);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
        this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
    }

    @Override
    public boolean mousePressed(PotionClient potion, int x, int y) {
        if (super.mousePressed(potion, x, y)) {
            this.sliderValue = (float) (x - (this.xPosition + 4)) / (float) (this.width - 8);
            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }

            potion.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
            this.displayString = potion.gameSettings.getKeyBinding(this.idFloat);
            this.dragging = true;
            return true;
        }

        return false;
    }

    @Override
    public void mouseReleased(int var1, int var2) {
        this.dragging = false;
    }
}
