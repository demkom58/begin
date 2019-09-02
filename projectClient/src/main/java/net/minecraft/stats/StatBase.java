package net.minecraft.stats;

import net.minecraft.achievement.AchievementMap;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StatBase {
    public static IStatType field_27087_i = new StatTypeSimple();
    public static IStatType field_27086_j = new StatTypeTime();
    public static IStatType field_27085_k = new StatTypeDistance();
    private static NumberFormat field_26903_b = NumberFormat.getIntegerInstance(Locale.US);
    private static DecimalFormat field_26904_c = new DecimalFormat("########0.00");
    public final int statId;
    public final String statName;
    private final IStatType field_26902_a;
    public boolean field_27088_g;
    public String statGuid;

    public StatBase(int statId, String statName, IStatType var3) {
        this.field_27088_g = false;
        this.statId = statId;
        this.statName = statName;
        this.field_26902_a = var3;
    }

    public StatBase(int var1, String var2) {
        this(var1, var2, field_27087_i);
    }

    // $FF: synthetic method
    static NumberFormat func_27083_i() {
        return field_26903_b;
    }

    // $FF: synthetic method
    static DecimalFormat func_27081_j() {
        return field_26904_c;
    }

    public StatBase func_27082_h() {
        this.field_27088_g = true;
        return this;
    }

    public StatBase registerStat() {
        if (StatList.field_25169_C.containsKey(this.statId))
            throw new RuntimeException("Duplicate stat id: \""
                    + StatList.field_25169_C.get(this.statId).statName
                    + "\" and \"" + this.statName + "\" at id " + this.statId);

        StatList.field_25188_a.add(this);
        StatList.field_25169_C.put(this.statId, this);
        this.statGuid = AchievementMap.getGuid(this.statId);
        return this;
    }

    public boolean func_25067_a() {
        return false;
    }

    public String func_27084_a(int var1) {
        return this.field_26902_a.func_27192_a(var1);
    }

    public String toString() {
        return this.statName;
    }
}
