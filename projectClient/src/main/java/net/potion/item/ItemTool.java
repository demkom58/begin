package net.potion.item;

import net.potion.block.Block;
import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;

public class ItemTool extends Item {
    protected EnumToolMaterial toolMaterial;
    private Block[] blocksEffectiveAgainst;
    private float efficiencyOnProperMaterial = 4.0F;
    private int damageVsEntity;

    protected ItemTool(int var1, int var2, EnumToolMaterial var3, Block[] var4) {
        super(var1);
        this.toolMaterial = var3;
        this.blocksEffectiveAgainst = var4;
        this.maxStackSize = 1;
        this.setMaxDamage(var3.getMaxUses());
        this.efficiencyOnProperMaterial = var3.getEfficiencyOnProperMaterial();
        this.damageVsEntity = var2 + var3.getDamageVsEntity();
    }

    @Override
    public float getStrVsBlock(ItemStack var1, Block var2) {
        for (int var3 = 0; var3 < this.blocksEffectiveAgainst.length; ++var3) {
            if (this.blocksEffectiveAgainst[var3] == var2) {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    @Override
    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3) {
        var1.damageItem(2, var3);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack var1, int var2, int var3, int var4, int var5, EntityLiving var6) {
        var1.damageItem(1, var6);
        return true;
    }

    @Override
    public int getDamageVsEntity(Entity var1) {
        return this.damageVsEntity;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }
}
