package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerPlayer extends Container {
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    public boolean isLocalPlayer;

    public ContainerPlayer(InventoryPlayer inv) {
        this(inv, true);
    }

    public ContainerPlayer(InventoryPlayer inv, boolean localPlayer) {
        this.craftMatrix = new InventoryCrafting(this, 2, 2);
        this.craftResult = new InventoryCraftResult();
        this.isLocalPlayer = localPlayer;
        this.addSlot(new SlotCrafting(inv.player, this.craftMatrix, this.craftResult, 0, 144, 36));

        for (int x = 0; x < 2; ++x) {
            for (int y = 0; y < 2; ++y) {
                this.addSlot(new Slot(this.craftMatrix, y + x * 2, 88 + y * 18, 26 + x * 18));
            }
        }

        for (int y = 0; y < 4; ++y) {
            this.addSlot(new SlotArmor(this, inv, inv.getSizeInventory() - 1 - y, 8, 8 + y * 18, y));
        }

        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 9; ++y) {
                this.addSlot(new Slot(inv, y + (x + 1) * 9, 8 + y * 18, 84 + x * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(inv, x, 8 + x * 18, 142));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory var1) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix));
    }

    @Override
    public void onCraftGuiClosed(EntityPlayer player) {
        super.onCraftGuiClosed(player);

        for (int x = 0; x < 4; ++x) {
            ItemStack stack = this.craftMatrix.getStackInSlot(x);
            if (stack != null) {
                player.dropPlayerItem(stack);
                this.craftMatrix.setInventorySlotContents(x, null);
            }
        }

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        Slot var3 = this.slots.get(var1);
        if (var3 == null || !var3.hasStack()) {
            return null;
        }

        ItemStack var4 = var3.getStack();
        ItemStack var2 = var4.copy();
        if (var1 == 0) {
            this.method2(var4, 9, 45, true);
        } else if (var1 >= 9 && var1 < 36) {
            this.method2(var4, 36, 45, false);
        } else if (var1 >= 36 && var1 < 45) {
            this.method2(var4, 9, 36, false);
        } else {
            this.method2(var4, 9, 45, false);
        }

        if (var4.stackSize == 0) {
            var3.putStack(null);
        } else {
            var3.onSlotChanged();
        }

        if (var4.stackSize == var2.stackSize) {
            return null;
        }

        var3.onPickupFromSlot(var4);

        return var2;
    }
}
