package net.potion.client.gui;

import net.potion.client.PotionClient;
import net.potion.client.render.FontRenderer;
import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean enabled2;
    protected int width;
    protected int height;

    public GuiButton(int id, int x, int y, String text) {
        this(id, x, y, 200, 20, text);
    }

    public GuiButton(int id, int x, int y, int width, int height, String text) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.enabled2 = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.displayString = text;
    }

    protected int getHoverState(boolean var1) {
        byte var2 = 1;
        if (!this.enabled) {
            var2 = 0;
        } else if (var1) {
            var2 = 2;
        }

        return var2;
    }

    public void drawButton(PotionClient var1, int var2, int var3) {
        if (this.enabled2) {
            FontRenderer var4 = var1.fontRenderer;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.renderEngine.getTexture("/gui/gui.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var5 = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
            int var6 = this.getHoverState(var5);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var6 * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var6 * 20, this.width / 2, this.height);
            this.mouseDragged(var1, var2, var3);
            if (!this.enabled) {
                this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
            } else if (var5) {
                this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
            } else {
                this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
            }

        }
    }

    protected void mouseDragged(PotionClient potion, int var2, int var3) {
    }

    public void mouseReleased(int var1, int var2) {
    }

    public boolean mousePressed(PotionClient potion, int x, int y) {
        return this.enabled && x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
    }
}
