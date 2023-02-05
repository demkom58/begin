package net.minecraft.inventory;

import net.minecraft.achievement.AchievementList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotCrafting extends Slot {
    private final IInventory craftMatrix;
    private final EntityPlayer thePlayer;

    public SlotCrafting(EntityPlayer var1, IInventory var2, IInventory var3, int var4, int var5, int var6) {
        super(var3, var4, var5, var6);
        this.thePlayer = var1;
        this.craftMatrix = var2;
    }

    @Override
    public boolean isItemValid(ItemStack var1) {
        return false;
    }

    @Override
    public void onPickupFromSlot(ItemStack stack) {
        stack.onCrafting(this.thePlayer.world, this.thePlayer);
        if (stack.itemID == Block.WORKBENCH.blockID) {
            this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
        } else if (stack.itemID == Item.PICKAXE_WOOD.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
        } else if (stack.itemID == Block.FURNACE.blockID) {
            this.thePlayer.addStat(AchievementList.buildFurnace, 1);
        } else if (stack.itemID == Item.HOE_WOOD.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.buildHoe, 1);
        } else if (stack.itemID == Item.BREAD.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.makeBread, 1);
        } else if (stack.itemID == Item.CAKE.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.bakeCake, 1);
        } else if (stack.itemID == Item.PICKAXE_STONE.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
        } else if (stack.itemID == Item.SWORD_WOOD.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.buildSword, 1);
        }

        for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i) {
            ItemStack itemStack = this.craftMatrix.getStackInSlot(i);
            if (itemStack == null) {
                continue;
            }

            this.craftMatrix.decrStackSize(i, 1);
            if (itemStack.getItem().hasContainerItem()) {
                this.craftMatrix.setInventorySlotContents(i, new ItemStack(itemStack.getItem().getContainerItem()));
            }
        }

    }
}
