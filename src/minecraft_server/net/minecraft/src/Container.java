package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Container {
    public List<ItemStack> inventoryItemStacks = new ArrayList<>();
    public List<Slot> inventorySlots = new ArrayList<>();
    public int windowId = 0;
    private short field_20132_a = 0;
    protected List<ICrafting> crafters = new ArrayList<>();
    private Set<EntityPlayer> field_20131_b = new HashSet<>();

    protected void addSlot(Slot slot) {
        slot.id = this.inventorySlots.size();
        this.inventorySlots.add(slot);
        this.inventoryItemStacks.add(null);
    }

    public void onCraftGuiOpened(ICrafting var1) {
        if (this.crafters.contains(var1)) {
            throw new IllegalArgumentException("Listener already listening");
        } else {
            this.crafters.add(var1);
            var1.updateCraftingInventory(this, this.func_28127_b());
            this.updateCraftingMatrix();
        }
    }

    public List<ItemStack> func_28127_b() {
        List<ItemStack> stacks = new ArrayList<>();

        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            stacks.add(this.inventorySlots.get(i).getStack());
        }

        return stacks;
    }

    public void updateCraftingMatrix() {
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            ItemStack var2 = this.inventorySlots.get(i).getStack();
            ItemStack var3 = this.inventoryItemStacks.get(i);
            if (!ItemStack.areItemStacksEqual(var3, var2)) {
                var3 = var2 == null ? null : var2.copy();
                this.inventoryItemStacks.set(i, var3);

                for (int var4 = 0; var4 < this.crafters.size(); ++var4) {
                    this.crafters.get(var4).updateCraftingInventorySlot(this, i, var3);
                }
            }
        }

    }

    public Slot func_20127_a(IInventory var1, int var2) {
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            Slot slot = this.inventorySlots.get(i);
            if (slot.isHere(var1, var2)) {
                return slot;
            }
        }

        return null;
    }

    public Slot getSlot(int var1) {
        return this.inventorySlots.get(var1);
    }

    public ItemStack func_27086_a(int var1) {
        Slot slot = this.inventorySlots.get(var1);
        return slot != null ? slot.getStack() : null;
    }

    public ItemStack func_27085_a(int var1, int var2, boolean var3, EntityPlayer var4) {
        ItemStack var5 = null;
        if (var2 == 0 || var2 == 1) {
            InventoryPlayer var6 = var4.inventory;
            if (var1 == -999) {
                if (var6.getItemStack() != null && var1 == -999) {
                    if (var2 == 0) {
                        var4.dropPlayerItem(var6.getItemStack());
                        var6.setItemStack(null);
                    }

                    if (var2 == 1) {
                        var4.dropPlayerItem(var6.getItemStack().splitStack(1));
                        if (var6.getItemStack().stackSize == 0) {
                            var6.setItemStack(null);
                        }
                    }
                }
            } else if (var3) {
                ItemStack var7 = this.func_27086_a(var1);
                if (var7 != null) {
                    int var8 = var7.stackSize;
                    var5 = var7.copy();
                    Slot var9 = this.inventorySlots.get(var1);
                    if (var9 != null && var9.getStack() != null) {
                        int var10 = var9.getStack().stackSize;
                        if (var10 < var8) {
                            this.func_27085_a(var1, var2, var3, var4);
                        }
                    }
                }
            } else {
                Slot var12 = this.inventorySlots.get(var1);
                if (var12 != null) {
                    var12.onSlotChanged();
                    ItemStack var13 = var12.getStack();
                    ItemStack var14 = var6.getItemStack();
                    if (var13 != null) {
                        var5 = var13.copy();
                    }

                    if (var13 == null) {
                        if (var14 != null && var12.isItemValid(var14)) {
                            int var15 = var2 == 0 ? var14.stackSize : 1;
                            if (var15 > var12.getSlotStackLimit()) {
                                var15 = var12.getSlotStackLimit();
                            }

                            var12.putStack(var14.splitStack(var15));
                            if (var14.stackSize == 0) {
                                var6.setItemStack(null);
                            }
                        }
                    } else if (var14 == null) {
                        int var16 = var2 == 0 ? var13.stackSize : (var13.stackSize + 1) / 2;
                        ItemStack var11 = var12.decrStackSize(var16);
                        var6.setItemStack(var11);
                        if (var13.stackSize == 0) {
                            var12.putStack(null);
                        }

                        var12.onPickupFromSlot(var6.getItemStack());
                    } else if (var12.isItemValid(var14)) {
                        if (var13.itemID != var14.itemID || var13.getHasSubtypes() && var13.getItemDamage() != var14.getItemDamage()) {
                            if (var14.stackSize <= var12.getSlotStackLimit()) {
                                var12.putStack(var14);
                                var6.setItemStack(var13);
                            }
                        } else {
                            int var17 = var2 == 0 ? var14.stackSize : 1;
                            if (var17 > var12.getSlotStackLimit() - var13.stackSize) {
                                var17 = var12.getSlotStackLimit() - var13.stackSize;
                            }

                            if (var17 > var14.getMaxStackSize() - var13.stackSize) {
                                var17 = var14.getMaxStackSize() - var13.stackSize;
                            }

                            var14.splitStack(var17);
                            if (var14.stackSize == 0) {
                                var6.setItemStack(null);
                            }

                            var13.stackSize += var17;
                        }
                    } else if (var13.itemID == var14.itemID && var14.getMaxStackSize() > 1 && (!var13.getHasSubtypes() || var13.getItemDamage() == var14.getItemDamage())) {
                        int var18 = var13.stackSize;
                        if (var18 > 0 && var18 + var14.stackSize <= var14.getMaxStackSize()) {
                            var14.stackSize += var18;
                            var13.splitStack(var18);
                            if (var13.stackSize == 0) {
                                var12.putStack(null);
                            }

                            var12.onPickupFromSlot(var6.getItemStack());
                        }
                    }
                }
            }
        }

        return var5;
    }

    public void onCraftGuiClosed(EntityPlayer var1) {
        InventoryPlayer var2 = var1.inventory;
        if (var2.getItemStack() != null) {
            var1.dropPlayerItem(var2.getItemStack());
            var2.setItemStack(null);
        }

    }

    public void onCraftMatrixChanged(IInventory var1) {
        this.updateCraftingMatrix();
    }

    public boolean getCanCraft(EntityPlayer var1) {
        return !this.field_20131_b.contains(var1);
    }

    public void setCanCraft(EntityPlayer var1, boolean var2) {
        if (var2) {
            this.field_20131_b.remove(var1);
        } else {
            this.field_20131_b.add(var1);
        }

    }

    public abstract boolean canInteractWith(EntityPlayer var1);

    protected void func_28126_a(ItemStack var1, int var2, int var3, boolean var4) {
        int var5 = var2;
        if (var4) {
            var5 = var3 - 1;
        }

        if (var1.func_21132_c()) {
            while (var1.stackSize > 0 && (!var4 && var5 < var3 || var4 && var5 >= var2)) {
                Slot var6 = this.inventorySlots.get(var5);
                ItemStack var7 = var6.getStack();
                if (var7 != null && var7.itemID == var1.itemID && (!var1.getHasSubtypes() || var1.getItemDamage() == var7.getItemDamage())) {
                    int var8 = var7.stackSize + var1.stackSize;
                    if (var8 <= var1.getMaxStackSize()) {
                        var1.stackSize = 0;
                        var7.stackSize = var8;
                        var6.onSlotChanged();
                    } else if (var7.stackSize < var1.getMaxStackSize()) {
                        var1.stackSize -= var1.getMaxStackSize() - var7.stackSize;
                        var7.stackSize = var1.getMaxStackSize();
                        var6.onSlotChanged();
                    }
                }

                if (var4) {
                    --var5;
                } else {
                    ++var5;
                }
            }
        }

        if (var1.stackSize > 0) {
            if (var4) {
                var5 = var3 - 1;
            } else {
                var5 = var2;
            }

            while (!var4 && var5 < var3 || var4 && var5 >= var2) {
                Slot var10 = this.inventorySlots.get(var5);
                ItemStack var11 = var10.getStack();
                if (var11 == null) {
                    var10.putStack(var1.copy());
                    var10.onSlotChanged();
                    var1.stackSize = 0;
                    break;
                }

                if (var4) {
                    --var5;
                } else {
                    ++var5;
                }
            }
        }

    }
}
