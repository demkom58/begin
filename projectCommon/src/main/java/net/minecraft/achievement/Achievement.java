package net.minecraft.achievement;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCollector;

public class Achievement extends StatBase {
    public final int displayColumn;
    public final int displayRow;
    public final Achievement parentAchievement;
    public final ItemStack itemStack;
    private final String achievementDescription;
    private IStatStringFormat statStringFormatter;
    private boolean isSpecial;

    public Achievement(int var1, String var2, int var3, int var4, Item var5, Achievement var6) {
        this(var1, var2, var3, var4, new ItemStack(var5), var6);
    }

    public Achievement(int var1, String var2, int var3, int var4, Block var5, Achievement var6) {
        this(var1, var2, var3, var4, new ItemStack(var5), var6);
    }

    public Achievement(int var1, String var2, int displayColumn, int displayRow, ItemStack itemStack, Achievement var6) {
        super(5242880 + var1, StatCollector.translateToLocal("achievement." + var2));
        this.itemStack = itemStack;
        this.achievementDescription = StatCollector.translateToLocal("achievement." + var2 + ".desc");
        this.displayColumn = displayColumn;
        this.displayRow = displayRow;
        if (displayColumn < AchievementList.minDisplayColumn) {
            AchievementList.minDisplayColumn = displayColumn;
        }

        if (displayRow < AchievementList.minDisplayRow) {
            AchievementList.minDisplayRow = displayRow;
        }

        if (displayColumn > AchievementList.maxDisplayColumn) {
            AchievementList.maxDisplayColumn = displayColumn;
        }

        if (displayRow > AchievementList.maxDisplayRow) {
            AchievementList.maxDisplayRow = displayRow;
        }

        this.parentAchievement = var6;
    }

    @Override
    public Achievement setClientSide() {
        this.clientSide = true;
        return this;
    }

    public Achievement setSpecial() {
        this.isSpecial = true;
        return this;
    }

    @Override
    public Achievement registerAchievement() {
        super.registerAchievement();
        AchievementList.achievementList.add(this);
        return this;
    }

    @Side(CodeSide.CLIENT)
    public String getDescription() {
        return this.statStringFormatter != null ? this.statStringFormatter.formatString(this.achievementDescription) : this.achievementDescription;
    }

    @Side(CodeSide.CLIENT)
    public Achievement setStatStringFormatter(IStatStringFormat var1) {
        this.statStringFormatter = var1;
        return this;
    }

    public boolean getSpecial() {
        return this.isSpecial;
    }

    @Override
    public boolean func_25067_a() {
        return true;
    }
}
