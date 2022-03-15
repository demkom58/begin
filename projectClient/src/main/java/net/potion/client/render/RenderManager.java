package net.potion.client.render;

import net.potion.block.Block;
import net.potion.client.GameSettings;
import net.potion.client.model.*;
import net.potion.client.render.entity.*;
import net.potion.entity.*;
import net.potion.entity.item.EntityBoat;
import net.potion.entity.item.EntityItem;
import net.potion.entity.monster.*;
import net.potion.entity.passive.*;
import net.potion.entity.player.EntityPlayer;
import net.potion.entity.projectile.EntityArrow;
import net.potion.entity.projectile.EntityEgg;
import net.potion.entity.projectile.EntityFireball;
import net.potion.entity.projectile.EntitySnowball;
import net.potion.item.Item;
import net.potion.item.ItemRenderer;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class RenderManager {
    public static RenderManager instance = new RenderManager();
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public RenderEngine renderEngine;
    public ItemRenderer itemRenderer;
    public World worldObj;
    public EntityLiving livingPlayer;
    public float playerViewY;
    public float playerViewX;
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private Map<Class<? extends Entity>, Render> entityRenderMap = new HashMap<>();
    private FontRenderer fontRenderer;

    private RenderManager() {
        this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
        this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
        this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
        this.entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
        this.entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(), 0.5F));
        this.entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
        this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
        this.entityRenderMap.put(EntitySkeleton.class, new RenderBiped(new ModelSkeleton(), 0.5F));
        this.entityRenderMap.put(EntityZombie.class, new RenderBiped(new ModelZombie(), 0.5F));
        this.entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
        this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
        this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
        this.entityRenderMap.put(EntityGhast.class, new RenderGhast());
        this.entityRenderMap.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
        this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
        this.entityRenderMap.put(Entity.class, new RenderEntity());
        this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
        this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
        this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Item.SNOWBALL.getIconFromDamage(0)));
        this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(Item.EGG.getIconFromDamage(0)));
        this.entityRenderMap.put(EntityFireball.class, new RenderFireball());
        this.entityRenderMap.put(EntityItem.class, new RenderItem());
        this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
        this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
        this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
        this.entityRenderMap.put(EntityBoat.class, new RenderBoat());
        this.entityRenderMap.put(EntityFish.class, new RenderFish());
        this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt());

        for (Render var2 : this.entityRenderMap.values()) {
            var2.setRenderManager(this);
        }

    }

    public Render getEntityClassRenderObject(Class<? extends Entity> clazz) {
        Render render = this.entityRenderMap.get(clazz);

        if (render == null && clazz != Entity.class) {
            render = this.getEntityClassRenderObject((Class<? extends Entity>) clazz.getSuperclass());
            this.entityRenderMap.put(clazz, render);
        }

        return render;
    }

    public Render getEntityRenderObject(Entity entity) {
        return this.getEntityClassRenderObject(entity.getClass());
    }

    public void cacheActiveRenderInfo(World world, RenderEngine renderEngine, FontRenderer fontRenderer, EntityLiving entity, GameSettings options, float delta) {
        this.worldObj = world;
        this.renderEngine = renderEngine;
        this.options = options;
        this.livingPlayer = entity;
        this.fontRenderer = fontRenderer;
        if (entity.isSleeping()) {
            int blockId = world.getBlockId(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ));
            if (blockId == Block.BED.blockID) {
                int metadata = world.getBlockMetadata(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ));
                int var9 = metadata & 3;
                this.playerViewY = (float) (var9 * 90 + 180);
                this.playerViewX = 0.0F;
            }
        } else {
            this.playerViewY = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta;
            this.playerViewX = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * delta;
        }

        this.viewerPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) delta;
        this.viewerPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) delta;
        this.viewerPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) delta;
    }

    public void renderEntity(Entity entity, float delta) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) delta;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) delta;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) delta;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * delta;

        float brightness = entity.getEntityBrightness(delta);
        GL11.glColor3f(brightness, brightness, brightness);

        this.renderEntityWithPosYaw(entity, x - renderPosX, y - renderPosY, z - renderPosZ, yaw, delta);
    }

    public void renderEntityWithPosYaw(Entity entity, double x, double y, double z, float yaw, float delta) {
        Render render = this.getEntityRenderObject(entity);
        if (render != null) {
            render.doRender(entity, x, y, z, yaw, delta);
            render.doRenderShadowAndFire(entity, x, y, z, yaw, delta);
        }

    }

    public void setWorld(World world) {
        this.worldObj = world;
    }

    public double getDistanceToCamera(double var1, double var3, double var5) {
        double var7 = var1 - this.viewerPosX;
        double var9 = var3 - this.viewerPosY;
        double var11 = var5 - this.viewerPosZ;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
}
