package net.potion.inventory;

import net.potion.achievement.AchievementList;
import net.potion.block.Block;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;

public class SlotCrafting extends Slot {
    private final IInventory craftMatrix;
    private EntityPlayer field_25004_e;

    public SlotCrafting(EntityPlayer var1, IInventory var2, IInventory var3, int var4, int var5, int var6) {
        super(var3, var4, var5, var6);
        this.field_25004_e = var1;
        this.craftMatrix = var2;
    }

    @Override
    public boolean isItemValid(ItemStack var1) {
        return false;
    }

    @Override
    public void onPickupFromSlot(ItemStack var1) {
        var1.func_28142_b(this.field_25004_e.worldObj, this.field_25004_e);
        if (var1.itemID == Block.WORKBENCH.blockID) {
            this.field_25004_e.addStat(AchievementList.buildWorkBench, 1);
        } else if (var1.itemID == Item.PICKAXE_WOOD.shiftedIndex) {
            this.field_25004_e.addStat(AchievementList.buildPickaxe, 1);
        } else if (var1.itemID == Block.FURNACE.blockID) {
            this.field_25004_e.addStat(AchievementList.buildFurnace, 1);
        } else if (var1.itemID == Item.HOE_WOOD.shiftedIndex) {
            this.field_25004_e.addStat(AchievementList.buildHoe, 1);
        } else if (var1.itemID == Item.BREAD.shiftedIndex) {
            this.field_25004_e.addStat(AchievementList.makeBread, 1);
        } else if (var1.itemID == Item.CAKE.shiftedIndex) {
            this.field_25004_e.addStat(AchievementList.bakeCake, 1);
        } else if (var1.itemID == Item.PICKAXE_STONE.shiftedIndex) {
            this.field_25004_e.addStat(AchievementList.buildBetterPickaxe, 1);
        } else if (var1.itemID == Item.SWORD_WOOD.shiftedIndex) {
            this.field_25004_e.addStat(AchievementList.buildSword, 1);
        }

        for (int var2 = 0; var2 < this.craftMatrix.getSizeInventory(); ++var2) {
            ItemStack var3 = this.craftMatrix.getStackInSlot(var2);
            if (var3 != null) {
                this.craftMatrix.decrStackSize(var2, 1);
                if (var3.getItem().hasContainerItem()) {
                    this.craftMatrix.setInventorySlotContents(var2, new ItemStack(var3.getItem().getContainerItem()));
                }
            }
        }

    }
}
