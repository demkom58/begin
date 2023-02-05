package net.minecraft.entity.item;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.achievement.AchievementList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.material.Material;
import net.minecraft.nbt.TagCompound;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityItem extends Entity {
    public ItemStack item;
    public int age = 0;
    public int delayBeforeCanPickup;
    @Side(CodeSide.CLIENT)
    public float field1 = (float) (Math.random() * Math.PI * 2.0D);
    private int ticks;
    private int health = 5;

    public EntityItem(World world, double x, double y, double z, ItemStack stack) {
        super(world);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.item = stack;
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D);
    }

    public EntityItem(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.delayBeforeCanPickup > 0)
            --this.delayBeforeCanPickup;

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        if (this.world.getBlockMaterial(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) == Material.LAVA) {
            this.motionY = 0.20000000298023224D;
            this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.world.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        float moveMul = 0.98F;
        if (this.onGround) {
            moveMul = 0.58800006F;
            int onBlockId = this.world.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.posZ));
            if (onBlockId > 0)
                moveMul = Block.BLOCKS_LIST[onBlockId].slipperiness * 0.98F;
        }

        this.motionX *= moveMul;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= moveMul;
        if (this.onGround)
            this.motionY *= -0.5D;

        ++this.ticks;
        ++this.age;

        if (this.age >= 6000) {
            this.setEntityDead();
        }

    }

    @Override
    public boolean handleWaterMovement() {
        return this.world.handleMaterialAcceleration(this.boundingBox, Material.WATER, this);
    }

    @Override
    protected void dealFireDamage(int damage) {
        this.attackEntityFrom(null, damage);
    }

    @Override
    public boolean attackEntityFrom(Entity entity, int damage) {
        this.setBeenAttacked();
        this.health -= damage;
        if (this.health <= 0)
            this.setEntityDead();

        return false;
    }

    @Override
    public void writeEntityToNBT(TagCompound compound) {
        compound.setShort("Health", (byte) this.health);
        compound.setShort("Age", (short) this.age);
        compound.setCompoundTag("Item", this.item.writeToNBT(new TagCompound()));
    }

    @Override
    public void readEntityFromNBT(TagCompound compound) {
        this.health = compound.getShort("Health") & 255;
        this.age = compound.getShort("Age");
        TagCompound var2 = compound.getCompoundTag("Item");
        this.item = new ItemStack(var2);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (this.world.localWorld)
            return;

        int stackSize = this.item.stackSize;
        if (this.delayBeforeCanPickup == 0 && player.inventory.addItemStackToInventory(this.item)) {
            if (this.item.itemID == Block.WOOD.blockID)
                player.triggerAchievement(AchievementList.mineWood);

            if (this.item.itemID == Item.LEATHER.shiftedIndex)
                player.triggerAchievement(AchievementList.killCow);

            this.world.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.onItemPickup(this, stackSize);
            if (this.item.stackSize <= 0)
                this.setEntityDead();
        }
    }
}
