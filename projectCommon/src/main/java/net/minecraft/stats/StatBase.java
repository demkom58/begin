package net.minecraft.stats;

import net.minecraft.achievement.AchievementMap;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StatBase {
    public static IStatType statSimple = new StatTypeSimple();
    public static IStatType statTime = new StatTypeTime();
    public static IStatType statDistance = new StatTypeDistance();

    private static NumberFormat usNumber = NumberFormat.getIntegerInstance(Locale.US);
    private static DecimalFormat decimalFormat = new DecimalFormat("########0.00");

    public final int statId;
    public final String statName;
    private final IStatType type;
    public boolean clientSide;
    public String statGuid;

    public StatBase(int statId, String statName, IStatType type) {
        this.clientSide = false;
        this.statId = statId;
        this.statName = statName;
        this.type = type;
    }

    public StatBase(int statId, String statName) {
        this(statId, statName, statSimple);
    }

    // $FF: synthetic method
    static NumberFormat func_27083_i() {
        return usNumber;
    }

    // $FF: synthetic method
    static DecimalFormat func_27081_j() {
        return decimalFormat;
    }

    public StatBase setClientSide() {
        this.clientSide = true;
        return this;
    }

    public StatBase registerAchievement() {
        if (StatList.id2statMap.containsKey(this.statId))
            throw new RuntimeException("Duplicate stat id: \""
                    + StatList.id2statMap.get(this.statId).statName
                    + "\" and \"" + this.statName + "\" at id " + this.statId);

        StatList.field1.add(this);
        StatList.id2statMap.put(this.statId, this);
        this.statGuid = AchievementMap.getGuid(this.statId);
        return this;
    }

    public boolean func_25067_a() {
        return false;
    }

    public String func_27084_a(int var1) {
        return this.type.func_27192_a(var1);
    }

    public String toString() {
        return this.statName;
    }
}
