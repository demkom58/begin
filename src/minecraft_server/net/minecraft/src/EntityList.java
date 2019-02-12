package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class EntityList {
   private static Map<String, Class> stringToClassMapping = new HashMap<>();
   private static Map<Class, String> classToStringMapping = new HashMap<>();
   private static Map<Integer, Class> IDtoClassMapping = new HashMap<>();
   private static Map<Class, Integer> classToIDMapping = new HashMap<>();

   private static void addMapping(Class clazz, String string, int id) {
      stringToClassMapping.put(string, clazz);
      classToStringMapping.put(clazz, string);
      IDtoClassMapping.put(id, clazz);
      classToIDMapping.put(clazz, id);
   }

   public static Entity createEntityInWorld(String var0, World var1) {
      Entity entity = null;

      try {
         Class clszz = stringToClassMapping.get(var0);
         if (clszz != null) {
            entity = (Entity)clszz.getConstructor(World.class).newInstance(var1);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return entity;
   }

   public static Entity createEntityFromNBT(NBTTagCompound compound, World world) {
      Entity entity = null;

      try {
         Class clazz = stringToClassMapping.get(compound.getString("id"));
         if (clazz != null) {
            entity = (Entity)clazz.getConstructor(World.class).newInstance(world);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (entity != null) {
         entity.readFromNBT(compound);
      } else {
         System.out.println("Skipping Entity with id " + compound.getString("id"));
      }

      return entity;
   }

   public static int getEntityID(Entity entity) {
      return classToIDMapping.get(entity.getClass());
   }

   public static String getEntityString(Entity entity) {
      return classToStringMapping.get(entity.getClass());
   }

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
}
