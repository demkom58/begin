package net.minecraft;

public class Achievement extends StatBase {
    public final int displayColumn;
    public final int displayRow;
    public final Achievement parentAchievement;
    public final ItemStack theItemStack;
    private final String achievementDescription;
    private boolean isSpecial;

    public Achievement(int var1, String var2, int var3, int var4, Item var5, Achievement var6) {
        this(var1, var2, var3, var4, new ItemStack(var5), var6);
    }

    public Achievement(int var1, String var2, int var3, int var4, Block var5, Achievement var6) {
        this(var1, var2, var3, var4, new ItemStack(var5), var6);
    }

    public Achievement(int var1, String var2, int var3, int var4, ItemStack var5, Achievement var6) {
        super(5242880 + var1, StatCollector.translateToLocal("achievement." + var2));
        this.theItemStack = var5;
        this.achievementDescription = StatCollector.translateToLocal("achievement." + var2 + ".desc");
        this.displayColumn = var3;
        this.displayRow = var4;
        if (var3 < AchievementList.minDisplayColumn) {
            AchievementList.minDisplayColumn = var3;
        }

        if (var4 < AchievementList.minDisplayRow) {
            AchievementList.minDisplayRow = var4;
        }

        if (var3 > AchievementList.maxDisplayColumn) {
            AchievementList.maxDisplayColumn = var3;
        }

        if (var4 > AchievementList.maxDisplayRow) {
            AchievementList.maxDisplayRow = var4;
        }

        this.parentAchievement = var6;
    }

    public Achievement func_27059_a() {
        this.field_27058_g = true;
        return this;
    }

    public Achievement func_27060_b() {
        this.isSpecial = true;
        return this;
    }

    public Achievement func_27061_c() {
        super.func_27053_d();
        AchievementList.achievementList.add(this);
        return this;
    }

    // $FF: synthetic method
    // $FF: bridge method
    public StatBase func_27053_d() {
        return this.func_27061_c();
    }

    // $FF: synthetic method
    // $FF: bridge method
    public StatBase func_27052_e() {
        return this.func_27059_a();
    }
}
