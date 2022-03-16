package net.potion.client.gui;

import net.hypnosis.render.Tessellator;
import net.potion.stats.StatCrafting;
import net.potion.stats.StatList;

import java.util.ArrayList;

class GuiSlotStatsItem extends GuiSlotStats {
    // $FF: synthetic field
    final GuiStats guiStats;

    public GuiSlotStatsItem(GuiStats guiStats) {
        super(guiStats);
        this.guiStats = guiStats;
        this.statCraftings = new ArrayList<>();

        for (StatCrafting statCraft : StatList.field3) {
            boolean var4 = false;
            int var5 = statCraft.getRecipeId();

            if (GuiStats.func_27142_c(guiStats).getStatsValue(statCraft) > 0) {
                var4 = true;
            } else if (StatList.field7[var5] != null && GuiStats.func_27142_c(guiStats).getStatsValue(StatList.field7[var5]) > 0) {
                var4 = true;
            } else if (StatList.field5[var5] != null && GuiStats.func_27142_c(guiStats).getStatsValue(StatList.field5[var5]) > 0) {
                var4 = true;
            }

            if (var4) {
                this.statCraftings.add(statCraft);
            }
        }

        this.comparator = new SorterStatsItem(this, guiStats);
    }

    @Override
    protected void func_27260_a(int var1, int var2, Tessellator tess) {
        super.func_27260_a(var1, var2, tess);
        if (this.field_27268_b == 0) {
            GuiStats.func_27128_a(this.guiStats, var1 + 115 - 18 + 1, var2 + 1 + 1, 72, 18);
        } else {
            GuiStats.func_27128_a(this.guiStats, var1 + 115 - 18, var2 + 1, 72, 18);
        }

        if (this.field_27268_b == 1) {
            GuiStats.func_27128_a(this.guiStats, var1 + 165 - 18 + 1, var2 + 1 + 1, 18, 18);
        } else {
            GuiStats.func_27128_a(this.guiStats, var1 + 165 - 18, var2 + 1, 18, 18);
        }

        if (this.field_27268_b == 2) {
            GuiStats.func_27128_a(this.guiStats, var1 + 215 - 18 + 1, var2 + 1 + 1, 36, 18);
        } else {
            GuiStats.func_27128_a(this.guiStats, var1 + 215 - 18, var2 + 1, 36, 18);
        }

    }

    @Override
    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
        StatCrafting var6 = this.func_27264_b(var1);
        int var7 = var6.getRecipeId();
        GuiStats.func_27148_a(this.guiStats, var2 + 40, var3, var7);
        this.func_27265_a((StatCrafting) StatList.field7[var7], var2 + 115, var3, var1 % 2 == 0);
        this.func_27265_a((StatCrafting) StatList.field5[var7], var2 + 165, var3, var1 % 2 == 0);
        this.func_27265_a(var6, var2 + 215, var3, var1 % 2 == 0);
    }

    @Override
    protected String func_27263_a(int var1) {
        if (var1 == 1)
            return "stat.crafted";

        return var1 == 2 ? "stat.used" : "stat.depleted";
    }
}
