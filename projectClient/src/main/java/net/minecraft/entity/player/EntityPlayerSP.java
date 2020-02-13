package net.minecraft.entity.player;

import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.AchievementList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.gui.*;
import net.minecraft.client.input.keyboard.MovementInput;
import net.minecraft.client.input.mouse.MouseFilter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPickupFX;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.TagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityPlayerSP extends EntityPlayer {
    public MovementInput movementInput;
    protected Minecraft mc;
    private MouseFilter field_21903_bJ = new MouseFilter();
    private MouseFilter field_21904_bK = new MouseFilter();
    private MouseFilter field_21902_bL = new MouseFilter();

    public EntityPlayerSP(Minecraft var1, World var2, Session var3, int var4) {
        super(var2);
        this.mc = var1;
        this.dimension = var4;
        if (var3 != null && var3.username != null && var3.username.length() > 0) {
            this.skinUrl = "http://s3.amazonaws.com/MinecraftSkins/" + var3.username + ".png";
        }

        this.username = var3.username;
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        super.moveEntity(x, y, z);
    }

    @Override
    public void updatePlayerActionState() {
        super.updatePlayerActionState();
        this.moveStrafing = this.movementInput.moveStrafe;
        this.moveForward = this.movementInput.moveForward;
        this.isJumping = this.movementInput.jump;
    }

    @Override
    public void onLivingUpdate() {
        if (!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
            this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
        }

        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if (!this.worldObj.multiplayerWorld && this.ridingEntity != null) {
                this.mountEntity(null);
            }

            if (this.mc.currentScreen != null) {
                this.mc.displayGuiScreen(null);
            }

            if (this.timeInPortal == 0.0F) {
                this.mc.soundManager.playSoundFX("portal.trigger", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            }

            this.timeInPortal += 0.0125F;
            if (this.timeInPortal >= 1.0F) {
                this.timeInPortal = 1.0F;
                if (!this.worldObj.multiplayerWorld) {
                    this.timeUntilPortal = 10;
                    this.mc.soundManager.playSoundFX("portal.travel", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
                    this.mc.usePortal();
                }
            }

            this.inPortal = false;
        } else {
            if (this.timeInPortal > 0.0F) {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F) {
                this.timeInPortal = 0.0F;
            }
        }

        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }

        this.movementInput.updatePlayerMoveState(this);
        if (this.movementInput.sneak && this.ySize < 0.2F) {
            this.ySize = 0.2F;
        }

        this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double) this.width * 0.35D);
        super.onLivingUpdate();
    }

    public void resetPlayerKeyState() {
        this.movementInput.resetKeyState();
    }

    public void handleKeyPress(int var1, boolean var2) {
        this.movementInput.checkKeyForMovementInput(var1, var2);
    }

    @Override
    public void writeEntityToNBT(TagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Score", this.score);
    }

    @Override
    public void readEntityFromNBT(TagCompound var1) {
        super.readEntityFromNBT(var1);
        this.score = var1.getInteger("Score");
    }

    @Override
    public void closeScreen() {
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }

    @Override
    public void displayGUIEditSign(TileEntitySign var1) {
        this.mc.displayGuiScreen(new GuiEditSign(var1));
    }

    @Override
    public void displayGUIChest(IInventory var1) {
        this.mc.displayGuiScreen(new GuiChest(this.inventory, var1));
    }

    @Override
    public void displayWorkbenchGUI(int var1, int var2, int var3) {
        this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj, var1, var2, var3));
    }

    @Override
    public void displayGUIFurnace(TileEntityFurnace var1) {
        this.mc.displayGuiScreen(new GuiFurnace(this.inventory, var1));
    }

    @Override
    public void displayGUIDispenser(TileEntityDispenser var1) {
        this.mc.displayGuiScreen(new GuiDispenser(this.inventory, var1));
    }

    @Override
    public void onItemPickup(Entity var1, int var2) {
        this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, var1, this, -0.5F));
    }

    public int getPlayerArmorValue() {
        return this.inventory.getTotalArmorValue();
    }

    public void sendChatMessage(String var1) {
    }

    @Override
    public boolean isSneaking() {
        return this.movementInput.sneak && !this.sleeping;
    }

    public void setHealth(int health) {
        int newHealth = this.health - health;
        if (newHealth <= 0) {
            this.health = health;
            if (newHealth < 0)
                this.heartsLife = this.heartsHalvesLife / 2;

            return;
        }

        this.naturalArmorRating = newHealth;
        this.prevHealth = this.health;
        this.heartsLife = this.heartsHalvesLife;
        this.damageEntity(newHealth);
        this.hurtTime = this.maxHurtTime = 10;
    }

    @Override
    public void respawnPlayer() {
        this.mc.respawn(false, 0);
    }

    @Override
    public void func_6420_o() {
    }

    @Override
    public void addChatMessage(String var1) {
        this.mc.ingameGUI.addChatMessageTranslate(var1);
    }

    @Override
    public void addStat(StatBase statBase, int addition) {
        if (statBase == null)
            return;

        if (statBase.func_25067_a()) {
            Achievement var3 = (Achievement) statBase;
            if (var3.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(var3.parentAchievement)) {
                if (!this.mc.statFileWriter.hasAchievementUnlocked(var3)) {
                    this.mc.guiAchievement.queueTakenAchievement(var3);
                }

                this.mc.statFileWriter.addStat(statBase, addition);
            }
        } else {
            this.mc.statFileWriter.addStat(statBase, addition);
        }
    }

    private boolean isBlockTranslucent(int var1, int var2, int var3) {
        return this.worldObj.isBlockNormalCube(var1, var2, var3);
    }

    @Override
    protected boolean pushOutOfBlocks(double var1, double var3, double var5) {
        int var7 = MathHelper.floor(var1);
        int var8 = MathHelper.floor(var3);
        int var9 = MathHelper.floor(var5);
        double var10 = var1 - (double) var7;
        double var12 = var5 - (double) var9;
        if (this.isBlockTranslucent(var7, var8, var9) || this.isBlockTranslucent(var7, var8 + 1, var9)) {
            boolean var14 = !this.isBlockTranslucent(var7 - 1, var8, var9) && !this.isBlockTranslucent(var7 - 1, var8 + 1, var9);
            boolean var15 = !this.isBlockTranslucent(var7 + 1, var8, var9) && !this.isBlockTranslucent(var7 + 1, var8 + 1, var9);
            boolean var16 = !this.isBlockTranslucent(var7, var8, var9 - 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 - 1);
            boolean var17 = !this.isBlockTranslucent(var7, var8, var9 + 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 + 1);
            byte var18 = -1;
            double var19 = 9999.0D;
            if (var14 && var10 < var19) {
                var19 = var10;
                var18 = 0;
            }

            if (var15 && 1.0D - var10 < var19) {
                var19 = 1.0D - var10;
                var18 = 1;
            }

            if (var16 && var12 < var19) {
                var19 = var12;
                var18 = 4;
            }

            if (var17 && 1.0D - var12 < var19) {
                var19 = 1.0D - var12;
                var18 = 5;
            }

            float var21 = 0.1F;
            if (var18 == 0) {
                this.motionX = -var21;
            }

            if (var18 == 1) {
                this.motionX = var21;
            }

            if (var18 == 4) {
                this.motionZ = -var21;
            }

            if (var18 == 5) {
                this.motionZ = var21;
            }
        }

        return false;
    }
}
