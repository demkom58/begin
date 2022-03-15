package net.potion.inventory;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.ICrafting;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Container {
    public List<ItemStack> itemStacks = new ArrayList<>();
    public List<Slot> slots = new ArrayList<>();
    public int windowId = 0;
    protected List<ICrafting> crafters = new ArrayList<>();
    private short field1 = 0;
    @Side(CodeSide.SERVER)
    private Set<EntityPlayer> viewers = new HashSet<>();

    protected void addSlot(Slot var1) {
        var1.slotNumber = this.slots.size();
        this.slots.add(var1);
        this.itemStacks.add(null);
    }

    @Side(CodeSide.SERVER)
    public void addViewer(ICrafting var1) {
        if (this.crafters.contains(var1)) {
            throw new IllegalArgumentException("Listener already listening");
        } else {
            this.crafters.add(var1);
            var1.updateCraftingInventory(this, this.getItemStacks());
            this.updateCraftingMatrix();
        }
    }

    public List<ItemStack> getItemStacks() {
        List<ItemStack> stacks = new ArrayList<>();

        for (int i = 0; i < this.slots.size(); ++i) {
            stacks.add(this.slots.get(i).getStack());
        }

        return stacks;
    }

    public void updateCraftingMatrix() {
        for (int var1 = 0; var1 < this.slots.size(); ++var1) {
            ItemStack var2 = this.slots.get(var1).getStack();
            ItemStack var3 = this.itemStacks.get(var1);
            if (!ItemStack.areItemStacksEqual(var3, var2)) {
                var3 = var2 == null ? null : var2.copy();
                this.itemStacks.set(var1, var3);

                for (int var4 = 0; var4 < this.crafters.size(); ++var4) {
                    this.crafters.get(var4).updateCraftingInventorySlot(this, var1, var3);
                }
            }
        }

    }

    public Slot findSlot(IInventory inventory, int slotIdx) {
        for (int i = 0; i < this.slots.size(); ++i) {
            Slot slot = this.slots.get(i);
            if (slot.isHere(inventory, slotIdx)) {
                return slot;
            }
        }

        return null;
    }

    public Slot getSlot(int var1) {
        return this.slots.get(var1);
    }

    public ItemStack getStackInSlot(int var1) {
        Slot var2 = this.slots.get(var1);
        return var2 != null ? var2.getStack() : null;
    }

    public ItemStack method1(int var1, int var2, boolean var3, EntityPlayer var4) {
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
                ItemStack var7 = this.getStackInSlot(var1);
                if (var7 != null) {
                    int var8 = var7.stackSize;
                    var5 = var7.copy();
                    Slot var9 = this.slots.get(var1);
                    if (var9 != null && var9.getStack() != null) {
                        int var10 = var9.getStack().stackSize;
                        if (var10 < var8) {
                            this.method1(var1, var2, var3, var4);
                        }
                    }
                }
            } else {
                Slot var12 = this.slots.get(var1);
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

    @Side(CodeSide.SERVER)
    public boolean getCanCraft(EntityPlayer var1) {
        return !this.viewers.contains(var1);
    }

    @Side(CodeSide.SERVER)
    public void setCanCraft(EntityPlayer var1, boolean var2) {
        if (var2) {
            this.viewers.remove(var1);
        } else {
            this.viewers.add(var1);
        }

    }

    public void putStackInSlot(int var1, ItemStack var2) {
        this.getSlot(var1).putStack(var2);
    }

    public void putStacksInSlots(ItemStack[] var1) {
        for (int var2 = 0; var2 < var1.length; ++var2) {
            this.getSlot(var2).putStack(var1[var2]);
        }

    }

    @Side(CodeSide.CLIENT)
    public void func_20112_a(int var1, int var2) {
    }

    @Side(CodeSide.CLIENT)
    public short func_20111_a(InventoryPlayer var1) {
        ++this.field1;
        return this.field1;
    }

    @Side(CodeSide.CLIENT)
    public void func_20113_a(short var1) {
    }

    @Side(CodeSide.CLIENT)
    public void func_20110_b(short var1) {
    }

    public abstract boolean isUsableByPlayer(EntityPlayer var1);

    protected void method2(ItemStack var1, int var2, int var3, boolean var4) {
        int var5 = var2;
        if (var4) {
            var5 = var3 - 1;
        }

        if (var1.isStackable()) {
            while (var1.stackSize > 0 && (!var4 && var5 < var3 || var4 && var5 >= var2)) {
                Slot var6 = this.slots.get(var5);
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
                Slot var10 = this.slots.get(var5);
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
