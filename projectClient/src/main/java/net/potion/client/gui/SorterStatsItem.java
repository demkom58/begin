package net.potion.client.gui;

import net.potion.stats.StatBase;
import net.potion.stats.StatCrafting;
import net.potion.stats.StatList;

import java.util.Comparator;

class SorterStatsItem implements Comparator<StatCrafting> {
    private final GuiStats guiStats;
    private final GuiSlotStatsItem statsItem;

    SorterStatsItem(GuiSlotStatsItem statsItem, GuiStats guiStats) {
        this.statsItem = statsItem;
        this.guiStats = guiStats;
    }

    public int func_27371_a(StatCrafting var1, StatCrafting var2) {
        int var3 = var1.getRecipeId();
        int var4 = var2.getRecipeId();
        StatBase var5 = null;
        StatBase var6 = null;
        if (this.statsItem.field_27271_e == 0) {
            var5 = StatList.field7[var3];
            var6 = StatList.field7[var4];
        } else if (this.statsItem.field_27271_e == 1) {
            var5 = StatList.field5[var3];
            var6 = StatList.field5[var4];
        } else if (this.statsItem.field_27271_e == 2) {
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

            int var7 = GuiStats.func_27142_c(this.statsItem.guiStats).getStatsValue(var5);
            int var8 = GuiStats.func_27142_c(this.statsItem.guiStats).getStatsValue(var6);
            if (var7 != var8) {
                return (var7 - var8) * this.statsItem.field_27270_f;
            }
        }

        return var3 - var4;
    }

    @Override
    public int compare(StatCrafting a, StatCrafting b) {
        return this.func_27371_a(a, b);
    }
}
