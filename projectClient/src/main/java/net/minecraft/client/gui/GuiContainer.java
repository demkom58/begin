package net.minecraft.client.gui;

import net.hypnosis.input.keyboard.Keyboard;
import net.minecraft.client.input.keyboard.CraftKeyboard;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringTranslate;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public abstract class GuiContainer extends GuiScreen {
    private static RenderItem itemRenderer = new RenderItem();
    public Container inventorySlots;
    protected int xSize = 176;
    protected int ySize = 166;

    public GuiContainer(Container var1) {
        this.inventorySlots = var1;
    }

    public void initGui() {
        super.initGui();
        this.mc.thePlayer.craftingInventory = this.inventorySlots;
    }

    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(var3);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var4, (float) var5, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL15.GL_RESCALE_NORMAL);
        Slot var6 = null;

        for (int var7 = 0; var7 < this.inventorySlots.slots.size(); ++var7) {
            Slot var8 = (Slot) this.inventorySlots.slots.get(var7);
            this.drawSlotInventory(var8);
            if (this.getIsMouseOverSlot(var8, var1, var2)) {
                var6 = var8;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int var9 = var8.xDisplayPosition;
                int var10 = var8.yDisplayPosition;
                this.drawGradientRect(var9, var10, var9 + 16, var10 + 16, -2130706433, -2130706433);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        InventoryPlayer var12 = this.mc.thePlayer.inventory;
        if (var12.getItemStack() != null) {
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, var12.getItemStack(), var1 - var4 - 8, var2 - var5 - 8);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var12.getItemStack(), var1 - var4 - 8, var2 - var5 - 8);
        }

        GL11.glDisable(GL15.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.drawGuiContainerForegroundLayer();
        if (var12.getItemStack() == null && var6 != null && var6.getHasStack()) {
            String var13 = ("" + StringTranslate.getInstance().translateNamedKey(var6.getStack().getItemName())).trim();
            if (var13.length() > 0) {
                int var14 = var1 - var4 + 12;
                int var15 = var2 - var5 - 12;
                int var11 = this.fontRenderer.getStringWidth(var13);
                this.drawGradientRect(var14 - 3, var15 - 3, var14 + var11 + 3, var15 + 8 + 3, -1073741824, -1073741824);
                this.fontRenderer.drawStringWithShadow(var13, var14, var15, -1);
            }
        }

        GL11.glPopMatrix();
        super.drawScreen(var1, var2, var3);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    protected void drawGuiContainerForegroundLayer() {
    }

    protected abstract void drawGuiContainerBackgroundLayer(float var1);

    private void drawSlotInventory(Slot var1) {
        int var2 = var1.xDisplayPosition;
        int var3 = var1.yDisplayPosition;
        ItemStack var4 = var1.getStack();
        if (var4 == null) {
            int var5 = var1.getBackgroundIconIndex();
            if (var5 >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
                this.drawTexturedModalRect(var2, var3, var5 % 16 * 16, var5 / 16 * 16, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                return;
            }
        }

        itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, var4, var2, var3);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var4, var2, var3);
    }

    private Slot getSlotAtPosition(int x, int y) {
        for (int i = 0; i < this.inventorySlots.slots.size(); ++i) {
            Slot slot = (Slot) this.inventorySlots.slots.get(i);
            if (this.getIsMouseOverSlot(slot, x, y))
                return slot;
        }

        return null;
    }

    private boolean getIsMouseOverSlot(Slot slot, int x, int y) {
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        x = x - var4;
        y = y - var5;
        return x >= slot.xDisplayPosition - 1 && x < slot.xDisplayPosition + 16 + 1 && y >= slot.yDisplayPosition - 1 && y < slot.yDisplayPosition + 16 + 1;
    }

    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        if (button == 0 || button == 1) {
            Slot slot = this.getSlotAtPosition(x, y);
            int var5 = (this.width - this.xSize) / 2;
            int var6 = (this.height - this.ySize) / 2;
            boolean var7 = x < var5 || y < var6 || x >= var5 + this.xSize || y >= var6 + this.ySize;
            int var8 = -1;
            if (slot != null) {
                var8 = slot.slotNumber;
            }

            if (var7) {
                var8 = -999;
            }

            if (var8 != -1) {
                final CraftKeyboard keyboard = mc.keyboard;
                boolean var9 = var8 != -999 && (keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT));
                this.mc.playerController.func_27174_a(this.inventorySlots.windowId, var8, button, var9, this.mc.thePlayer);
            }
        }

    }

    protected void mouseMovedOrUp(int x, int y, int button) {
        if (button == 0) {
        }

    }

    @Override
    public void charTyped(char ch, int key) {
        super.charTyped(ch, key);
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
        if (keycode == GLFW.GLFW_KEY_ESCAPE || keycode == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.thePlayer.closeScreen();
        }
    }

    public void onGuiClosed() {
        if (this.mc.thePlayer != null) {
            this.mc.playerController.func_20086_a(this.inventorySlots.windowId, this.mc.thePlayer);
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void updateScreen() {
        super.updateScreen();
        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
            this.mc.thePlayer.closeScreen();
        }

    }
}
