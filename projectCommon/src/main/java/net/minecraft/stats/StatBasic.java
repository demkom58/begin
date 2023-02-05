package net.minecraft.stats;

public class StatBasic extends StatBase {
    public StatBasic(int var1, String var2, IStatType var3) {
        super(var1, var2, var3);
    }

    public StatBasic(int var1, String var2) {
        super(var1, var2);
    }

    @Override
    public StatBase registerAchievement() {
        super.registerAchievement();
        StatList.field2.add(this);
        return this;
    }
}
