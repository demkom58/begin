package net.minecraft.stats;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;

public class StatCrafting extends StatBase {
    private final int recipeId;

    public StatCrafting(int id, String name, int recipeId) {
        super(id, name);
        this.recipeId = recipeId;
    }

    @Side(CodeSide.CLIENT)
    public int getRecipeId() {
        return this.recipeId;
    }
}
