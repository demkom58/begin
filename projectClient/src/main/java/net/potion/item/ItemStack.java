package net.potion.item;

import net.potion.block.Block;
import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.player.EntityPlayer;
import net.potion.nbt.TagCompound;
import net.potion.stats.StatList;
import net.potion.world.World;

public final class ItemStack {
    public int stackSize;
    public int animationsToGo;
    public int itemID;
    private int itemDamage;

    public ItemStack(Block var1) {
        this(var1, 1);
    }

    public ItemStack(Block var1, int var2) {
        this(var1.blockID, var2, 0);
    }

    public ItemStack(Block var1, int var2, int var3) {
        this(var1.blockID, var2, var3);
    }

    public ItemStack(Item var1) {
        this(var1.shiftedIndex, 1, 0);
    }

    public ItemStack(Item var1, int var2) {
        this(var1.shiftedIndex, var2, 0);
    }

    public ItemStack(Item var1, int var2, int var3) {
        this(var1.shiftedIndex, var2, var3);
    }

    public ItemStack(int itemID, int stackSize, int itemDamage) {
        this.stackSize = 0;
        this.itemID = itemID;
        this.stackSize = stackSize;
        this.itemDamage = itemDamage;
    }

    public ItemStack(TagCompound var1) {
        this.stackSize = 0;
        this.readFromNBT(var1);
    }

    public static boolean areItemStacksEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null && stack2 == null)
            return true;

        return (stack1 != null && stack2 != null) && stack1.isItemStackEqual(stack2);
    }

    public static ItemStack copyItemStack(ItemStack var0) {
        return var0 == null ? null : var0.copy();
    }

    public ItemStack splitStack(int stackSize) {
        this.stackSize -= stackSize;
        return new ItemStack(this.itemID, stackSize, this.itemDamage);
    }

    public Item getItem() {
        return Item.ITEMS_LIST[this.itemID];
    }

    public int getIconIndex() {
        return this.getItem().getIconIndex(this);
    }

    public boolean useItem(EntityPlayer player, World world, int var3, int var4, int var5, int var6) {
        boolean used = this.getItem().onItemUse(this, player, world, var3, var4, var5, var6);

        if (used)
            player.addStat(StatList.field_25172_A[this.itemID], 1);

        return used;
    }

    public float getStrVsBlock(Block var1) {
        return this.getItem().getStrVsBlock(this, var1);
    }

    public ItemStack useItemRightClick(World var1, EntityPlayer var2) {
        return this.getItem().onItemRightClick(this, var1, var2);
    }

    public TagCompound writeToNBT(TagCompound var1) {
        var1.setShort("id", (short) this.itemID);
        var1.setByte("Count", (byte) this.stackSize);
        var1.setShort("Damage", (short) this.itemDamage);
        return var1;
    }

    public void readFromNBT(TagCompound var1) {
        this.itemID = var1.getShort("id");
        this.stackSize = var1.getByte("Count");
        this.itemDamage = var1.getShort("Damage");
    }

    public int getMaxStackSize() {
        return this.getItem().getItemStackLimit();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }

    public boolean isItemStackDamageable() {
        return Item.ITEMS_LIST[this.itemID].getMaxDamage() > 0;
    }

    public boolean getHasSubtypes() {
        return Item.ITEMS_LIST[this.itemID].getHasSubtypes();
    }

    public boolean isItemDamaged() {
        return this.isItemStackDamageable() && this.itemDamage > 0;
    }

    public int getItemDamageForDisplay() {
        return this.itemDamage;
    }

    public int getItemDamage() {
        return this.itemDamage;
    }

    public void setItemDamage(int var1) {
        this.itemDamage = var1;
    }

    public int getMaxDamage() {
        return Item.ITEMS_LIST[this.itemID].getMaxDamage();
    }

    public void damageItem(int var1, Entity var2) {
        if (this.isItemStackDamageable()) {
            this.itemDamage += var1;
            if (this.itemDamage > this.getMaxDamage()) {
                if (var2 instanceof EntityPlayer) {
                    ((EntityPlayer) var2).addStat(StatList.field_25170_B[this.itemID], 1);
                }

                --this.stackSize;
                if (this.stackSize < 0) {
                    this.stackSize = 0;
                }

                this.itemDamage = 0;
            }

        }
    }

    public void hitEntity(EntityLiving var1, EntityPlayer var2) {
        boolean var3 = Item.ITEMS_LIST[this.itemID].hitEntity(this, var1, var2);
        if (var3) {
            var2.addStat(StatList.field_25172_A[this.itemID], 1);
        }

    }

    public void onDestroyBlock(int var1, int var2, int var3, int var4, EntityPlayer var5) {
        boolean var6 = Item.ITEMS_LIST[this.itemID].onBlockDestroyed(this, var1, var2, var3, var4, var5);
        if (var6) {
            var5.addStat(StatList.field_25172_A[this.itemID], 1);
        }

    }

    public int getDamageVsEntity(Entity entity) {
        return Item.ITEMS_LIST[this.itemID].getDamageVsEntity(entity);
    }

    public boolean canHarvestBlock(Block block) {
        return Item.ITEMS_LIST[this.itemID].canHarvestBlock(block);
    }

    public void onItemDestroyedByUse(EntityPlayer player) {
    }

    public void useItemOnEntity(EntityLiving entity) {
        Item.ITEMS_LIST[this.itemID].saddleEntity(this, entity);
    }

    public ItemStack copy() {
        return new ItemStack(this.itemID, this.stackSize, this.itemDamage);
    }

    private boolean isItemStackEqual(ItemStack stack) {
        if (this.stackSize != stack.stackSize)
            return false;

        if (this.itemID != stack.itemID)
            return false;

        return this.itemDamage == stack.itemDamage;
    }

    public boolean isItemEqual(ItemStack stack) {
        return this.itemID == stack.itemID && this.itemDamage == stack.itemDamage;
    }

    public String getItemName() {
        return Item.ITEMS_LIST[this.itemID].getItemNameIS(this);
    }

    public String toString() {
        return this.stackSize + "x" + Item.ITEMS_LIST[this.itemID].getItemName() + "@" + this.itemDamage;
    }

    public void updateAnimation(World var1, Entity var2, int var3, boolean var4) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }

        Item.ITEMS_LIST[this.itemID].onUpdate(this, var1, var2, var3, var4);
    }

    public void onCrafting(World var1, EntityPlayer var2) {
        var2.addStat(StatList.field_25158_z[this.itemID], this.stackSize);
        Item.ITEMS_LIST[this.itemID].onCreated(this, var1, var2);
    }

    public boolean isStackEqual(ItemStack var1) {
        return this.itemID == var1.itemID && this.stackSize == var1.stackSize && this.itemDamage == var1.itemDamage;
    }
}
