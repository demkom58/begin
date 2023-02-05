package net.minecraft.item;

public enum MaterialGrade {
    WOOD(0, 59, 2.0F, 0),
    STONE(1, 131, 4.0F, 1),
    IRON(2, 250, 6.0F, 2),
    EMERALD(3, 1561, 8.0F, 3),
    GOLD(0, 32, 12.0F, 0);

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiencyOnProperMaterial;
    private final int damageVsEntity;

    MaterialGrade(int harvestLevel, int maxUses, float efficiency, int attackDamage) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiencyOnProperMaterial = efficiency;
        this.damageVsEntity = attackDamage;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public float getEfficiencyOnProperMaterial() {
        return this.efficiencyOnProperMaterial;
    }

    public int getDamageVsEntity() {
        return this.damageVsEntity;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }
}
