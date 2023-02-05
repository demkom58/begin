package net.minecraft.stats;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;

@Side(CodeSide.CLIENT)
public interface IStatStringFormat {
    String formatString(String var1);
}
