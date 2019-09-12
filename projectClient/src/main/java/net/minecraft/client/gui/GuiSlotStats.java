package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.stats.StatCrafting;
import net.minecraft.util.StringTranslate;

import java.util.Comparator;
import java.util.List;

abstract class GuiSlotStats extends GuiSlot {
    // $FF: synthetic field
    private final GuiStats guiStats;
    protected int field_27268_b;
    protected List<StatCrafting> statCraftings;
    protected Comparator<StatCrafting> comparator;
    protected int field_27271_e;
    protected int field_27270_f;

    protected GuiSlotStats(GuiStats guiStats) {
        super(GuiStats.func_27143_f(guiStats), guiStats.width, guiStats.height, 32, guiStats.height - 64, 20);
        this.guiStats = guiStats;
        this.field_27268_b = -1;
        this.field_27271_e = -1;
        this.field_27270_f = 0;
        this.func_27258_a(false);
        this.func_27259_a(true, 20);
    }

    protected void elementClicked(int var1, boolean var2) {
    }

    protected boolean isSelected(int var1) {
        return false;
    }

    protected void drawBackground() {
        this.guiStats.drawDefaultBackground();
    }

    protected void func_27260_a(int var1, int var2, Tessellator tess) {
        if (!mc.mouse.isButtonPressed(0)) {
            this.field_27268_b = -1;
        }

        if (this.field_27268_b == 0) {
            GuiStats.func_27128_a(this.guiStats, var1 + 115 - 18, var2 + 1, 0, 0);
        } else {
            GuiStats.func_27128_a(this.guiStats, var1 + 115 - 18, var2 + 1, 0, 18);
        }

        if (this.field_27268_b == 1) {
            GuiStats.func_27128_a(this.guiStats, var1 + 165 - 18, var2 + 1, 0, 0);
        } else {
            GuiStats.func_27128_a(this.guiStats, var1 + 165 - 18, var2 + 1, 0, 18);
        }

        if (this.field_27268_b == 2) {
            GuiStats.func_27128_a(this.guiStats, var1 + 215 - 18, var2 + 1, 0, 0);
        } else {
            GuiStats.func_27128_a(this.guiStats, var1 + 215 - 18, var2 + 1, 0, 18);
        }

        if (this.field_27271_e != -1) {
            short var4 = 79;
            byte var5 = 18;
            if (this.field_27271_e == 1) {
                var4 = 129;
            } else if (this.field_27271_e == 2) {
                var4 = 179;
            }

            if (this.field_27270_f == 1) {
                var5 = 36;
            }

            GuiStats.func_27128_a(this.guiStats, var1 + var4, var2 + 1, var5, 0);
        }

    }

    protected void func_27255_a(int var1, int var2) {
        this.field_27268_b = -1;
        if (var1 >= 79 && var1 < 115) {
            this.field_27268_b = 0;
        } else if (var1 >= 129 && var1 < 165) {
            this.field_27268_b = 1;
        } else if (var1 >= 179 && var1 < 215) {
            this.field_27268_b = 2;
        }

        if (this.field_27268_b >= 0) {
            this.func_27266_c(this.field_27268_b);
            GuiStats.func_27149_g(this.guiStats).soundManager.playSoundFX("random.click", 1.0F, 1.0F);
        }

    }

    protected final int getSize() {
        return this.statCraftings.size();
    }

    protected final StatCrafting func_27264_b(int index) {
        return this.statCraftings.get(index);
    }

    protected abstract String func_27263_a(int var1);

    protected void func_27265_a(StatCrafting var1, int var2, int var3, boolean var4) {
        if (var1 != null) {
            String var5 = var1.func_27084_a(GuiStats.func_27142_c(this.guiStats).writeStat(var1));
            this.guiStats.drawString(GuiStats.func_27133_h(this.guiStats), var5, var2 - GuiStats.func_27137_i(this.guiStats).getStringWidth(var5), var3 + 5, var4 ? 16777215 : 9474192);
        } else {
            String var6 = "-";
            this.guiStats.drawString(GuiStats.func_27132_j(this.guiStats), var6, var2 - GuiStats.func_27134_k(this.guiStats).getStringWidth(var6), var3 + 5, var4 ? 16777215 : 9474192);
        }

    }

    protected void func_27257_b(int var1, int var2) {
        if (var2 >= this.top && var2 <= this.bottom) {
            int var3 = this.func_27256_c(var1, var2);
            int var4 = this.guiStats.width / 2 - 92 - 16;
            if (var3 >= 0) {
                if (var1 < var4 + 40 || var1 > var4 + 40 + 20) {
                    return;
                }

                StatCrafting var11 = this.func_27264_b(var3);
                this.func_27267_a(var11, var1, var2);
            } else {
                String var5 = "";
                if (var1 >= var4 + 115 - 18 && var1 <= var4 + 115) {
                    var5 = this.func_27263_a(0);
                } else if (var1 >= var4 + 165 - 18 && var1 <= var4 + 165) {
                    var5 = this.func_27263_a(1);
                } else {
                    if (var1 < var4 + 215 - 18 || var1 > var4 + 215) {
                        return;
                    }

                    var5 = this.func_27263_a(2);
                }

                var5 = ("" + StringTranslate.getInstance().translateKey(var5)).trim();
                if (var5.length() > 0) {
                    int var6 = var1 + 12;
                    int var7 = var2 - 12;
                    int var8 = GuiStats.func_27139_l(this.guiStats).getStringWidth(var5);
                    GuiStats.func_27129_a(this.guiStats, var6 - 3, var7 - 3, var6 + var8 + 3, var7 + 8 + 3, -1073741824, -1073741824);
                    GuiStats.func_27144_m(this.guiStats).drawStringWithShadow(var5, var6, var7, -1);
                }
            }

        }
    }

    protected void func_27267_a(StatCrafting var1, int var2, int var3) {
        if (var1 != null) {
            Item item = Item.ITEMS_LIST[var1.func_25072_b()];
            String itemName = StringTranslate.getInstance().translateNamedKey(item.getItemName()).trim();
            if (itemName.length() > 0) {
                int var6 = var2 + 12;
                int var7 = var3 - 12;
                int var8 = GuiStats.func_27127_n(this.guiStats).getStringWidth(itemName);
                GuiStats.func_27135_b(this.guiStats, var6 - 3, var7 - 3, var6 + var8 + 3, var7 + 8 + 3, -1073741824, -1073741824);
                GuiStats.func_27131_o(this.guiStats).drawStringWithShadow(itemName, var6, var7, -1);
            }

        }
    }

    protected void func_27266_c(int var1) {
        if (var1 != this.field_27271_e) {
            this.field_27271_e = var1;
            this.field_27270_f = -1;
        } else if (this.field_27270_f == -1) {
            this.field_27270_f = 1;
        } else {
            this.field_27271_e = -1;
            this.field_27270_f = 0;
        }

        this.statCraftings.sort(this.comparator);
    }
}
