package net.minecraft.client.gui;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;

import java.util.Comparator;

class SorterStatsBlock implements Comparator<StatCrafting> {
    private final GuiStats guiStats;
    private final GuiSlotStatsBlock statsBlock;

    SorterStatsBlock(GuiSlotStatsBlock statsBlock, GuiStats guiStats) {
        this.statsBlock = statsBlock;
        this.guiStats = guiStats;
    }

    public int func_27297_a(StatCrafting var1, StatCrafting var2) {
        int var3 = var1.getRecipeId();
        int var4 = var2.getRecipeId();
        StatBase var5 = null;
        StatBase var6 = null;
        if (this.statsBlock.field_27271_e == 2) {
            var5 = StatList.mineBlockStatArray[var3];
            var6 = StatList.mineBlockStatArray[var4];
        } else if (this.statsBlock.field_27271_e == 0) {
            var5 = StatList.field5[var3];
            var6 = StatList.field5[var4];
        } else if (this.statsBlock.field_27271_e == 1) {
            var5 = StatList.field6[var3];
            var6 = StatList.field6[var4];
        }

        if (var5 != null || var6 != null) {
            if (var5 == null) {
                return 1;
            }

            if (var6 == null) {
                return -1;
            }

            int var7 = GuiStats.getStatFileWriter(this.statsBlock.field_27274_a).getStatsValue(var5);
            int var8 = GuiStats.getStatFileWriter(this.statsBlock.field_27274_a).getStatsValue(var6);
            if (var7 != var8) {
                return (var7 - var8) * this.statsBlock.field_27270_f;
            }
        }

        return var3 - var4;
    }

    @Override
    public int compare(StatCrafting var1, StatCrafting var2) {
        return this.func_27297_a(var1, var2);
    }
}
