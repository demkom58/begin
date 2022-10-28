package net.potion.entity;

import net.potion.material.Material;

public enum EnumCreatureType {
    MONSTER(IMob.class, 70, Material.AIR, false),
    CREATURE(EntityAnimal.class, 15, Material.AIR, true),
    WATER_CREATURE(EntityWaterMob.class, 5, Material.WATER, true);

    private final Class<?> creatureClass;
    private final int maxNumberOfCreature;
    private final Material creatureMaterial;
    private final boolean peacefulCreature;

    EnumCreatureType(Class<?> creatureClass, int maxNumberOfCreature, Material creatureMaterial, boolean peacefulCreature) {
        this.creatureClass = creatureClass;
        this.maxNumberOfCreature = maxNumberOfCreature;
        this.creatureMaterial = creatureMaterial;
        this.peacefulCreature = peacefulCreature;
    }

    public Class<?> getCreatureClass() {
        return this.creatureClass;
    }

    public int getMaxNumberOfCreature() {
        return this.maxNumberOfCreature;
    }

    public Material getCreatureMaterial() {
        return this.creatureMaterial;
    }

    public boolean isPeacefulCreature() {
        return this.peacefulCreature;
    }
}
