package net.potion.entity.player;

import net.potion.block.Block;
import net.potion.client.PotionClient;
import net.potion.entity.Entity;
import net.potion.item.ItemStack;
import net.potion.world.World;

public class PlayerController {
    protected final PotionClient potion;
    public boolean ghost = false;

    public PlayerController(PotionClient potion) {
        this.potion = potion;
    }

    public void func_717_a(World world) {
    }

    public void clickBlock(int var1, int var2, int var3, int var4) {
        this.potion.theWorld.onBlockHit(this.potion.thePlayer, var1, var2, var3, var4);
        this.sendBlockRemoved(var1, var2, var3, var4);
    }

    public boolean sendBlockRemoved(int x, int y, int z, int var4) {
        World world = this.potion.theWorld;
        Block block = Block.BLOCKS_LIST[world.getBlockId(x, y, z)];
        world.playEffects(2001, x, y, z, block.blockID + world.getBlockMetadata(x, y, z) * 256);
        int metadata = world.getBlockMetadata(x, y, z);
        boolean changed = world.setBlockWithNotify(x, y, z, 0);

        if (block != null && changed)
            block.onBlockDestroyedByPlayer(world, x, y, z, metadata);

        return changed;
    }

    public void sendBlockRemoving(int x, int y, int z, int sideHit) {
    }

    public void resetBlockRemoving() {
    }

    public void setPartialTime(float var1) {
    }

    public float getBlockReachDistance() {
        return 5.0F;
    }

    public boolean sendUseItem(EntityPlayer var1, World var2, ItemStack var3) {
        int var4 = var3.stackSize;
        ItemStack var5 = var3.useItemRightClick(var2, var1);
        if (var5 != var3 || var5 != null && var5.stackSize != var4) {
            var1.inventory.mainInventory[var1.inventory.currentItem] = var5;
            if (var5.stackSize == 0) {
                var1.inventory.mainInventory[var1.inventory.currentItem] = null;
            }

            return true;
        } else {
            return false;
        }
    }

    public void flipPlayer(EntityPlayer var1) {
    }

    public void updateController() {
    }

    public boolean shouldDrawHUD() {
        return true;
    }

    public void func_6473_b(EntityPlayer var1) {
    }

    public boolean sendPlaceBlock(EntityPlayer var1, World var2, ItemStack var3, int var4, int var5, int var6, int var7) {
        int var8 = var2.getBlockId(var4, var5, var6);
        if (var8 > 0 && Block.BLOCKS_LIST[var8].blockActivated(var2, var4, var5, var6, var1)) {
            return true;
        } else {
            return var3 != null && var3.useItem(var1, var2, var4, var5, var6, var7);
        }
    }

    public EntityPlayer createPlayer(World var1) {
        return new EntityPlayerSP(this.potion, var1, this.potion.session, var1.worldProvider.worldType);
    }

    public void interactWithEntity(EntityPlayer var1, Entity var2) {
        var1.useCurrentItemOnEntity(var2);
    }

    public void attackEntity(EntityPlayer var1, Entity var2) {
        var1.attackTargetEntityWithCurrentItem(var2);
    }

    public ItemStack func_27174_a(int var1, int var2, int var3, boolean var4, EntityPlayer var5) {
        return var5.craftingInventory.method1(var2, var3, var4, var5);
    }

    public void func_20086_a(int var1, EntityPlayer var2) {
        var2.craftingInventory.onCraftGuiClosed(var2);
        var2.craftingInventory = var2.inventorySlots;
    }
}
