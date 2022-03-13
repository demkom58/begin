package net.potion.entity.item;

import net.potion.achievement.AchievementList;
import net.potion.block.Block;
import net.potion.entity.Entity;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.nbt.TagCompound;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;

public class EntityItem extends Entity {
    public ItemStack item;
    public int age = 0;
    public int delayBeforeCanPickup;
    public float field_432_ae = (float) (Math.random() * 3.141592653589793D * 2.0D);
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

    public EntityItem(World var1) {
        super(var1);
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
        if (this.worldObj.getBlockMaterial(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) == Material.LAVA) {
            this.motionY = 0.20000000298023224D;
            this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.func_28005_g(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float moveMul = 0.98F;
        if (this.onGround) {
            moveMul = 0.58800006F;
            int onBlockId = this.worldObj.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.minY) - 1, MathHelper.floor(this.posZ));
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

        if (this.age >= 6000)
            this.setEntityDead();

    }

    @Override
    public boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.WATER, this);
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
        if (this.worldObj.singleplayerWorld)
            return;

        int stackSize = this.item.stackSize;
        if (this.delayBeforeCanPickup == 0 && player.inventory.addItemStackToInventory(this.item)) {
            if (this.item.itemID == Block.WOOD.blockID)
                player.func_27017_a(AchievementList.mineWood);

            if (this.item.itemID == Item.LEATHER.shiftedIndex)
                player.func_27017_a(AchievementList.killCow);

            this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.onItemPickup(this, stackSize);
            if (this.item.stackSize <= 0)
                this.setEntityDead();
        }

    }
}
