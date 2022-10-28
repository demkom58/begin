package net.potion.inventory;

import net.potion.entity.player.EntityPlayer;
import net.potion.item.ItemStack;

public class ContainerChest extends Container {
    private final IInventory inventory;
    private final int rows;

    public ContainerChest(IInventory var1, IInventory var2) {
        this.inventory = var2;
        this.rows = var2.getSizeInventory() / 9;
        int var3 = (this.rows - 4) * 18;

        for (int var4 = 0; var4 < this.rows; ++var4) {
            for (int var5 = 0; var5 < 9; ++var5) {
                this.addSlot(new Slot(var2, var5 + var4 * 9, 8 + var5 * 18, 18 + var4 * 18));
            }
        }

        for (int var6 = 0; var6 < 3; ++var6) {
            for (int var8 = 0; var8 < 9; ++var8) {
                this.addSlot(new Slot(var1, var8 + var6 * 9 + 9, 8 + var8 * 18, 103 + var6 * 18 + var3));
            }
        }

        for (int var7 = 0; var7 < 9; ++var7) {
            this.addSlot(new Slot(var1, var7, 8 + var7 * 18, 161 + var3));
        }

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer var1) {
        return this.inventory.canInteractWith(var1);
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        ItemStack var2 = null;
        Slot var3 = this.slots.get(var1);
        if (var3 != null && var3.hasStack()) {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            if (var1 < this.rows * 9) {
                this.method2(var4, this.rows * 9, this.slots.size(), true);
            } else {
                this.method2(var4, 0, this.rows * 9, false);
            }

            if (var4.stackSize == 0) {
                var3.putStack(null);
            } else {
                var3.onSlotChanged();
            }
        }

        return var2;
    }
}
