package net.minecraft.client.gui;

import net.minecraft.block.Block;
import net.minecraft.network.packet.Packet130UpdateSign;
import net.minecraft.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

public class GuiEditSign extends GuiScreen {
    private static final String allowedCharacters = ChatAllowedCharacters.ALLOWED_CHARACTERS;
    protected String screenTitle = "Edit sign message:";
    private TileEntitySign entitySign;
    private int updateCounter;
    private int editLine = 0;

    public GuiEditSign(TileEntitySign var1) {
        this.entitySign = var1;
    }

    public void initGui() {
        this.buttons.clear();
//        TODO: Keyboard.enableRepeatEvents(true);
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
    }

    public void onGuiClosed() {
//        TODO: Keyboard.enableRepeatEvents(false);
        if (this.mc.theWorld.multiplayerWorld) {
            this.mc.getSendQueue().addToSendQueue(new Packet130UpdateSign(this.entitySign.xCoord, this.entitySign.yCoord, this.entitySign.zCoord, this.entitySign.signText));
        }

    }

    public void updateScreen() {
        ++this.updateCounter;
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 0) {
            this.entitySign.onInventoryChanged();
            this.mc.displayGuiScreen(null);
        }
    }

    protected void keyTyped(char ch, int key) {
        if (key == 200) {
            this.editLine = this.editLine - 1 & 3;
        }

        if (key == 208 || key == 28) {
            this.editLine = this.editLine + 1 & 3;
        }

        if (key == 14 && this.entitySign.signText[this.editLine].length() > 0) {
            this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine].substring(0, this.entitySign.signText[this.editLine].length() - 1);
        }

        if (allowedCharacters.indexOf(ch) >= 0 && this.entitySign.signText[this.editLine].length() < 15) {
            this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine] + ch;
        }

    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (this.width / 2), 0.0F, 50.0F);
        float var4 = 93.75F;
        GL11.glScalef(-var4, -var4, -var4);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        Block blockType = this.entitySign.getBlockType();
        if (blockType == Block.SIGN) {
            float var6 = (float) (this.entitySign.getBlockMetadata() * 360) / 16.0F;
            GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        } else {
            int var8 = this.entitySign.getBlockMetadata();
            float var7 = 0.0F;
            if (var8 == 2) {
                var7 = 180.0F;
            }

            if (var8 == 4) {
                var7 = 90.0F;
            }

            if (var8 == 5) {
                var7 = -90.0F;
            }

            GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }

        if (this.updateCounter / 6 % 2 == 0) {
            this.entitySign.lineBeingEdited = this.editLine;
        }

        TileEntityRenderer.instance.renderTileEntityAt(this.entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
        this.entitySign.lineBeingEdited = -1;
        GL11.glPopMatrix();
        super.drawScreen(var1, var2, var3);
    }
}
