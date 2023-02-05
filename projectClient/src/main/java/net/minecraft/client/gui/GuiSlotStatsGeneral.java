package net.minecraft.client.gui;

import net.hypnosis.render.Tessellator;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

class GuiSlotStatsGeneral extends GuiSlot {
    // $FF: synthetic field
    final GuiStats field_27276_a;

    public GuiSlotStatsGeneral(GuiStats var1) {
        super(GuiStats.getClient(var1), var1.width, var1.height, 32, var1.height - 64, 10);
        this.field_27276_a = var1;
        this.func_27258_a(false);
    }

    @Override
    protected int getSize() {
        return StatList.field2.size();
    }

    @Override
    protected void elementClicked(int var1, boolean var2) {
    }

    @Override
    protected boolean isSelected(int var1) {
        return false;
    }

    @Override
    protected int getContentHeight() {
        return this.getSize() * 10;
    }

    @Override
    protected void drawBackground() {
        this.field_27276_a.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator tess) {
        StatBase base = StatList.field2.get(var1);
        this.field_27276_a.drawString(GuiStats.getFontRenderer(this.field_27276_a), base.statName, var2 + 2, var3 + 1, var1 % 2 == 0 ? 16777215 : 9474192);
        String var7 = base.func_27084_a(GuiStats.getStatFileWriter(this.field_27276_a).getStatsValue(base));
        this.field_27276_a.drawString(GuiStats.getFontRenderer(this.field_27276_a), var7, var2 + 2 + 213 - GuiStats.getFontRenderer(this.field_27276_a).getStringWidth(var7), var3 + 1, var1 % 2 == 0 ? 16777215 : 9474192);
    }
}
