package net.minecraft.stats;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;

final class StatTypeSimple implements IStatType {
    @Side(CodeSide.CLIENT)
    @Override
    public String func_27192_a(int var1) {
        return StatBase.func_27083_i().format(var1);
    }
}
