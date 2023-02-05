package net.minecraft.entity.player;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PlayerControllerSP extends PlayerController {
    private int currentBlockX = -1;
    private int currentBlockY = -1;
    private int currentblockZ = -1;
    private float curBlockDamage = 0.0F;
    private float prevBlockDamage = 0.0F;
    private float field_1069_h = 0.0F;
    private int blockHitWait = 0;

    public PlayerControllerSP(MinecraftClient var1) {
        super(var1);
    }

    @Override
    public void flipPlayer(EntityPlayer var1) {
        var1.rotationYaw = -180.0F;
    }

    @Override
    public boolean sendBlockRemoved(int x, int y, int z, int var4) {
        int var5 = this.client.theWorld.getBlockId(x, y, z);
        int var6 = this.client.theWorld.getBlockMetadata(x, y, z);
        boolean var7 = super.sendBlockRemoved(x, y, z, var4);
        ItemStack var8 = this.client.thePlayer.getCurrentEquippedItem();
        boolean var9 = this.client.thePlayer.canHarvestBlock(Block.BLOCKS_LIST[var5]);
        if (var8 != null) {
            var8.onDestroyBlock(var5, x, y, z, this.client.thePlayer);
            if (var8.stackSize == 0) {
                var8.onItemDestroyedByUse(this.client.thePlayer);
                this.client.thePlayer.destroyCurrentEquippedItem();
            }
        }

        if (var7 && var9) {
            Block.BLOCKS_LIST[var5].harvestBlock(this.client.theWorld, this.client.thePlayer, x, y, z, var6);
        }

        return var7;
    }

    @Override
    public void clickBlock(int var1, int var2, int var3, int var4) {
        this.client.theWorld.onBlockHit(this.client.thePlayer, var1, var2, var3, var4);
        int var5 = this.client.theWorld.getBlockId(var1, var2, var3);
        if (var5 > 0 && this.curBlockDamage == 0.0F) {
            Block.BLOCKS_LIST[var5].onBlockClicked(this.client.theWorld, var1, var2, var3, this.client.thePlayer);
        }

        if (var5 > 0 && Block.BLOCKS_LIST[var5].blockStrength(this.client.thePlayer) >= 1.0F) {
            this.sendBlockRemoved(var1, var2, var3, var4);
        }

    }

    @Override
    public void resetBlockRemoving() {
        this.curBlockDamage = 0.0F;
        this.blockHitWait = 0;
    }

    @Override
    public void sendBlockRemoving(int x, int y, int z, int sideHit) {
        if (this.blockHitWait > 0) {
            --this.blockHitWait;
            return;
        }

        if (x == this.currentBlockX && y == this.currentBlockY && z == this.currentblockZ) {
            int blockId = this.client.theWorld.getBlockId(x, y, z);
            if (blockId == 0)
                return;

            Block block = Block.BLOCKS_LIST[blockId];
            this.curBlockDamage += block.blockStrength(this.client.thePlayer);
            if (this.field_1069_h % 4.0F == 0.0F && block != null) {
                this.client.soundManager.playSound(
                        block.stepSound.getFormattedName(),
                        (float) x + 0.5F,
                        (float) y + 0.5F,
                        (float) z + 0.5F,
                        (block.stepSound.getVolume() + 1.0F) / 8.0F,
                        block.stepSound.getPitch() * 0.5F
                );
            }

            ++this.field_1069_h;
            if (this.curBlockDamage >= 1.0F) {
                this.sendBlockRemoved(x, y, z, sideHit);
                this.curBlockDamage = 0.0F;
                this.prevBlockDamage = 0.0F;
                this.field_1069_h = 0.0F;
                this.blockHitWait = 5;
            }
            return;
        }

        this.curBlockDamage = 0.0F;
        this.prevBlockDamage = 0.0F;
        this.field_1069_h = 0.0F;
        this.currentBlockX = x;
        this.currentBlockY = y;
        this.currentblockZ = z;
    }

    @Override
    public void setPartialTime(float var1) {
        if (this.curBlockDamage <= 0.0F) {
            this.client.ingameGUI.damageGuiPartialTime = 0.0F;
            this.client.renderGlobal.damagePartialTime = 0.0F;
        } else {
            float var2 = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * var1;
            this.client.ingameGUI.damageGuiPartialTime = var2;
            this.client.renderGlobal.damagePartialTime = var2;
        }

    }

    @Override
    public float getBlockReachDistance() {
        return 4.0F;
    }

    @Override
    public void func_717_a(World world) {
        super.func_717_a(world);
    }

    @Override
    public void updateController() {
        this.prevBlockDamage = this.curBlockDamage;
        this.client.soundManager.playRandomMusicIfReady();
    }
}
