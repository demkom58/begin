package net.minecraft.entity;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class EntityList {
    private static final Map<String, Class<? extends Entity>> stringToClassMapping = new HashMap<>();
    private static final Map<Class<? extends Entity>, String> classToStringMapping = new HashMap<>();
    private static final Int2ObjectMap<Class<? extends Entity>> IDtoClassMapping = new Int2ObjectArrayMap<>();
    private static final Object2IntMap<Class<? extends Entity>> classToIDMapping = new Object2IntArrayMap<>();

    static {
        addMapping(EntityArrow.class, "Arrow", 10);
        addMapping(EntitySnowball.class, "Snowball", 11);
        addMapping(EntityItem.class, "Item", 1);
        addMapping(EntityPainting.class, "Painting", 9);
        addMapping(EntityLiving.class, "Mob", 48);
        addMapping(EntityMob.class, "Monster", 49);
        addMapping(EntityCreeper.class, "Creeper", 50);
        addMapping(EntitySkeleton.class, "Skeleton", 51);
        addMapping(EntitySpider.class, "Spider", 52);
        addMapping(EntityGiantZombie.class, "Giant", 53);
        addMapping(EntityZombie.class, "Zombie", 54);
        addMapping(EntitySlime.class, "Slime", 55);
        addMapping(EntityGhast.class, "Ghast", 56);
        addMapping(EntityPigZombie.class, "PigZombie", 57);
        addMapping(EntityPig.class, "Pig", 90);
        addMapping(EntitySheep.class, "Sheep", 91);
        addMapping(EntityCow.class, "Cow", 92);
        addMapping(EntityChicken.class, "Chicken", 93);
        addMapping(EntitySquid.class, "Squid", 94);
        addMapping(EntityWolf.class, "Wolf", 95);
        addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
        addMapping(EntityFallingSand.class, "FallingSand", 21);
        addMapping(EntityMinecart.class, "Minecart", 40);
        addMapping(EntityBoat.class, "Boat", 41);
    }

    private static void addMapping(Class<? extends Entity> var0, String var1, int var2) {
        stringToClassMapping.put(var1, var0);
        classToStringMapping.put(var0, var1);
        IDtoClassMapping.put(var2, var0);
        classToIDMapping.put(var0, var2);
    }

    public static Entity createEntityInWorld(String var0, World var1) {
        Entity var2 = null;

        try {
            Class<? extends Entity> var3 = stringToClassMapping.get(var0);
            if (var3 != null) {
                var2 = var3.getConstructor(World.class).newInstance(var1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return var2;
    }

    public static Entity createEntityFromNBT(TagCompound var0, World var1) {
        Entity var2 = null;

        try {
            Class<? extends Entity> var3 = stringToClassMapping.get(var0.getString("id"));
            if (var3 != null) {
                var2 = var3.getConstructor(World.class).newInstance(var1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (var2 != null) {
            var2.readFromNBT(var0);
        } else {
            System.out.println("Skipping Entity with id " + var0.getString("id"));
        }

        return var2;
    }

    public static Entity createEntity(int var0, World var1) {
        Entity var2 = null;

        try {
            Class<? extends Entity> var3 = IDtoClassMapping.get(var0);
            if (var3 != null) {
                var2 = var3.getConstructor(World.class).newInstance(var1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (var2 == null) {
            System.out.println("Skipping Entity with id " + var0);
        }

        return var2;
    }

    public static int getEntityID(Entity var0) {
        return classToIDMapping.get(var0.getClass());
    }

    public static String getEntityString(Entity var0) {
        return classToStringMapping.get(var0.getClass());
    }
}
