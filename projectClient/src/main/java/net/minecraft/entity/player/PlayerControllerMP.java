package net.minecraft.entity.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetClientHandler;
import net.minecraft.network.packet.*;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class PlayerControllerMP extends PlayerController {
    private int currentBlockX = -1;
    private int currentBlockY = -1;
    private int currentblockZ = -1;
    private float curBlockDamageMP = 0.0F;
    private float prevBlockDamageMP = 0.0F;
    private float field_9441_h = 0.0F;
    private int blockHitDelay = 0;
    private boolean isHittingBlock = false;
    private NetClientHandler netClientHandler;
    private int currentPlayerItem = 0;

    public PlayerControllerMP(Minecraft var1, NetClientHandler var2) {
        super(var1);
        this.netClientHandler = var2;
    }

    public void flipPlayer(EntityPlayer var1) {
        var1.rotationYaw = -180.0F;
    }

    public boolean sendBlockRemoved(int x, int y, int z, int var4) {
        int blockId = this.mc.theWorld.getBlockId(x, y, z);
        boolean removed = super.sendBlockRemoved(x, y, z, var4);
        ItemStack equippedItem = this.mc.thePlayer.getCurrentEquippedItem();

        if (equippedItem != null) {
            equippedItem.onDestroyBlock(blockId, x, y, z, this.mc.thePlayer);
            if (equippedItem.stackSize == 0) {
                equippedItem.func_1097_a(this.mc.thePlayer);
                this.mc.thePlayer.destroyCurrentEquippedItem();
            }
        }

        return removed;
    }

    public void clickBlock(int x, int y, int z, int sideHit) {
        if (!this.isHittingBlock || x != this.currentBlockX || y != this.currentBlockY || z != this.currentblockZ) {
            this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, x, y, z, sideHit));
            int blockId = this.mc.theWorld.getBlockId(x, y, z);

            if (blockId > 0 && this.curBlockDamageMP == 0.0F)
                Block.BLOCKS_LIST[blockId].onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);

            if (blockId > 0 && Block.BLOCKS_LIST[blockId].blockStrength(this.mc.thePlayer) >= 1.0F) {
                this.sendBlockRemoved(x, y, z, sideHit);
            } else {
                this.isHittingBlock = true;
                this.currentBlockX = x;
                this.currentBlockY = y;
                this.currentblockZ = z;
                this.curBlockDamageMP = 0.0F;
                this.prevBlockDamageMP = 0.0F;
                this.field_9441_h = 0.0F;
            }
        }
    }

    public void resetBlockRemoving() {
        this.curBlockDamageMP = 0.0F;
        this.isHittingBlock = false;
    }

    public void sendBlockRemoving(int x, int y, int z, int sideHit) {
        MovingObjectPosition mOver = this.mc.objectMouseOver;
        this.clickBlock(mOver.blockX, mOver.blockY, mOver.blockZ, mOver.sideHit);

        if (!this.isHittingBlock)
            return;

        this.syncCurrentPlayItem();
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return;
        }

        if (x == this.currentBlockX && y == this.currentBlockY && z == this.currentblockZ) {
            int blockId = this.mc.theWorld.getBlockId(x, y, z);
            if (blockId == 0) {
                this.isHittingBlock = false;
                return;
            }

            Block block = Block.BLOCKS_LIST[blockId];
            this.curBlockDamageMP += block.blockStrength(this.mc.thePlayer);
            if (this.field_9441_h % 4.0F == 0.0F && block != null) {
                this.mc.soundManager.playSound(
                        block.stepSound.func_1145_d(),
                        (float) x + 0.5F,
                        (float) y + 0.5F,
                        (float) z + 0.5F,
                        (block.stepSound.getVolume() + 1.0F) / 8.0F,
                        block.stepSound.getPitch() * 0.5F
                );
            }

            ++this.field_9441_h;
            if (this.curBlockDamageMP >= 1.0F) {
                this.isHittingBlock = false;
                this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, x, y, z, sideHit));
                this.sendBlockRemoved(x, y, z, sideHit);
                this.curBlockDamageMP = 0.0F;
                this.prevBlockDamageMP = 0.0F;
                this.field_9441_h = 0.0F;
                this.blockHitDelay = 5;
            }
        } else {
            this.clickBlock(x, y, z, sideHit);
        }
    }

    public void setPartialTime(float delta) {
        if (this.curBlockDamageMP <= 0.0F) {
            this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
            this.mc.renderGlobal.damagePartialTime = 0.0F;
        } else {
            float var2 = this.prevBlockDamageMP + (this.curBlockDamageMP - this.prevBlockDamageMP) * delta;
            this.mc.ingameGUI.damageGuiPartialTime = var2;
            this.mc.renderGlobal.damagePartialTime = var2;
        }

    }

    public float getBlockReachDistance() {
        return 4.0F;
    }

    public void func_717_a(World world) {
        super.func_717_a(world);
    }

    public void updateController() {
        this.syncCurrentPlayItem();
        this.prevBlockDamageMP = this.curBlockDamageMP;
        this.mc.soundManager.playRandomMusicIfReady();
    }

    private void syncCurrentPlayItem() {
        int currentItem = this.mc.thePlayer.inventory.currentItem;
        if (currentItem != this.currentPlayerItem) {
            this.currentPlayerItem = currentItem;
            this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(this.currentPlayerItem));
        }

    }

    public boolean sendPlaceBlock(EntityPlayer player, World world, ItemStack stack, int x, int y, int z, int direction) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new Packet15Place(x, y, z, direction, player.inventory.getCurrentItem()));
        return super.sendPlaceBlock(player, world, stack, x, y, z, direction);
    }

    public boolean sendUseItem(EntityPlayer player, World world, ItemStack stack) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new Packet15Place(-1, -1, -1, 255, player.inventory.getCurrentItem()));
        return super.sendUseItem(player, world, stack);
    }

    public EntityPlayer createPlayer(World world) {
        return new EntityClientPlayerMP(this.mc, world, this.mc.session, this.netClientHandler);
    }

    public void attackEntity(EntityPlayer player, Entity entity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new Packet7UseEntity(player.entityId, entity.entityId, 1));
        player.attackTargetEntityWithCurrentItem(entity);
    }

    public void interactWithEntity(EntityPlayer player, Entity entity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new Packet7UseEntity(player.entityId, entity.entityId, 0));
        player.useCurrentItemOnEntity(entity);
    }

    public ItemStack func_27174_a(int windowsId, int invSlot, int mouseClick, boolean var4, EntityPlayer var5) {
        short var6 = var5.craftingInventory.func_20111_a(var5.inventory);
        ItemStack stack = super.func_27174_a(windowsId, invSlot, mouseClick, var4, var5);
        this.netClientHandler.addToSendQueue(new Packet102WindowClick(windowsId, invSlot, mouseClick, var4, stack, var6));
        return stack;
    }

    public void func_20086_a(int var1, EntityPlayer player) {
        if (var1 != -9999) {
        }
    }
}
