package net.potion.client.gui;

import net.hypnosis.render.Tessellator;
import net.potion.client.render.FontRenderer;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.storage.SaveFormatData;

import java.util.Date;

class GuiWorldSlot extends GuiSlot {
    private final GuiSelectWorld parentWorldGui;

    public GuiWorldSlot(GuiSelectWorld selectWorld) {
        super(selectWorld.potion, selectWorld.width, selectWorld.height, 32, selectWorld.height - 64, 36);
        this.parentWorldGui = selectWorld;
    }

    @Override
    protected int getSize() {
        return GuiSelectWorld.getSize(this.parentWorldGui).size();
    }

    @Override
    protected void elementClicked(int var1, boolean var2) {
        GuiSelectWorld.setSelectedWorld(this.parentWorldGui, var1);
        boolean var3 = GuiSelectWorld.getSelectedWorld(this.parentWorldGui) >= 0
                && GuiSelectWorld.getSelectedWorld(this.parentWorldGui) < this.getSize();
        GuiSelectWorld.getSelectButton(this.parentWorldGui).enabled = var3;
        GuiSelectWorld.getRenameButton(this.parentWorldGui).enabled = var3;
        GuiSelectWorld.getDeleteButton(this.parentWorldGui).enabled = var3;
        if (var2 && var3) {
            this.parentWorldGui.selectWorld(var1);
        }

    }

    @Override
    protected boolean isSelected(int var1) {
        return var1 == GuiSelectWorld.getSelectedWorld(this.parentWorldGui);
    }

    @Override
    protected int getContentHeight() {
        return GuiSelectWorld.getSize(this.parentWorldGui).size() * 36;
    }

    @Override
    protected void drawBackground() {
        this.parentWorldGui.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator tessellator) {
        SaveFormatData comparator = GuiSelectWorld.getSize(this.parentWorldGui).get(var1);

        String displayName = comparator.getDisplayName();
        if (displayName == null || MathHelper.stringNullOrLengthZero(displayName))
            displayName = GuiSelectWorld.getScreenWorld(this.parentWorldGui) + " " + (var1 + 1);

        String fileName = comparator.getFileName();
        fileName = fileName + " (" + GuiSelectWorld.getDateFormatter(this.parentWorldGui).format(new Date(comparator.getLastTimePlayed()));

        long sizeOnDisk = comparator.getSizeOnDisk();
        fileName = fileName + ", " + (float) (sizeOnDisk / 1024L * 100L / 1024L) / 100.0F + " MB)";

        String var11 = "";
        if (comparator.isInvalidVersion()) {
            var11 = GuiSelectWorld.getScreenConversion(this.parentWorldGui) + " " + var11;
        }

        final FontRenderer fontRenderer = this.parentWorldGui.fontRenderer;
        this.parentWorldGui.drawString(fontRenderer, displayName, var2 + 2, var3 + 1, 16777215);
        this.parentWorldGui.drawString(fontRenderer, fileName, var2 + 2, var3 + 12, 8421504);
        this.parentWorldGui.drawString(fontRenderer, var11, var2 + 2, var3 + 12 + 10, 8421504);
    }
}
