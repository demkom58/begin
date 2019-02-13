package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.MinecraftServer;

public class EntityTracker {
   private Set<EntityTrackerEntry> trackedEntitySet = new HashSet<>();
   private MCHash trackedEntityHashTable = new MCHash();
   private MinecraftServer mcServer;
   private int maxTrackingDistanceThreshold;
   private int field_28113_e;

   public EntityTracker(MinecraftServer var1, int var2) {
      this.mcServer = var1;
      this.field_28113_e = var2;
      this.maxTrackingDistanceThreshold = var1.configManager.getMaxTrackingDistance();
   }

   public void trackEntity(Entity var1) {
      if (var1 instanceof EntityPlayerMP) {
         this.trackEntity(var1, 512, 2);
         EntityPlayerMP var2 = (EntityPlayerMP)var1;

         for(EntityTrackerEntry var4 : this.trackedEntitySet) {
            if (var4.trackedEntity != var2) {
               var4.updatePlayerEntity(var2);
            }
         }
      } else if (var1 instanceof EntityFish) {
         this.trackEntity(var1, 64, 5, true);
      } else if (var1 instanceof EntityArrow) {
         this.trackEntity(var1, 64, 20, false);
      } else if (var1 instanceof EntityFireball) {
         this.trackEntity(var1, 64, 10, false);
      } else if (var1 instanceof EntitySnowball) {
         this.trackEntity(var1, 64, 10, true);
      } else if (var1 instanceof EntityEgg) {
         this.trackEntity(var1, 64, 10, true);
      } else if (var1 instanceof EntityItem) {
         this.trackEntity(var1, 64, 20, true);
      } else if (var1 instanceof EntityMinecart) {
         this.trackEntity(var1, 160, 5, true);
      } else if (var1 instanceof EntityBoat) {
         this.trackEntity(var1, 160, 5, true);
      } else if (var1 instanceof EntitySquid) {
         this.trackEntity(var1, 160, 3, true);
      } else if (var1 instanceof IAnimals) {
         this.trackEntity(var1, 160, 3);
      } else if (var1 instanceof EntityTNTPrimed) {
         this.trackEntity(var1, 160, 10, true);
      } else if (var1 instanceof EntityFallingSand) {
         this.trackEntity(var1, 160, 20, true);
      } else if (var1 instanceof EntityPainting) {
         this.trackEntity(var1, 160, Integer.MAX_VALUE, false);
      }

   }

   public void trackEntity(Entity var1, int var2, int var3) {
      this.trackEntity(var1, var2, var3, false);
   }

   public void trackEntity(Entity var1, int var2, int var3, boolean var4) {
      if (var2 > this.maxTrackingDistanceThreshold) {
         var2 = this.maxTrackingDistanceThreshold;
      }

      if (this.trackedEntityHashTable.containsItem(var1.entityId)) {
         throw new IllegalStateException("Entity is already tracked!");
      } else {
         EntityTrackerEntry var5 = new EntityTrackerEntry(var1, var2, var3, var4);
         this.trackedEntitySet.add(var5);
         this.trackedEntityHashTable.addKey(var1.entityId, var5);
         var5.updatePlayerEntities(this.mcServer.getWorldManager(this.field_28113_e).playerEntities);
      }
   }

   public void untrackEntity(Entity var1) {
      if (var1 instanceof EntityPlayerMP) {
         EntityPlayerMP var2 = (EntityPlayerMP)var1;

         for(EntityTrackerEntry var4 : this.trackedEntitySet) {
            var4.removeFromTrackedPlayers(var2);
         }
      }

      EntityTrackerEntry var5 = (EntityTrackerEntry)this.trackedEntityHashTable.removeObject(var1.entityId);
      if (var5 != null) {
         this.trackedEntitySet.remove(var5);
         var5.sendDestroyEntityPacketToTrackedPlayers();
      }

   }

   public void updateTrackedEntities() {
      ArrayList<Entity> var1 = new ArrayList<>();

      for(EntityTrackerEntry var3 : this.trackedEntitySet) {
         var3.updatePlayerList(this.mcServer.getWorldManager(this.field_28113_e).playerEntities);
         if (var3.playerEntitiesUpdated && var3.trackedEntity instanceof EntityPlayerMP) {
            var1.add(var3.trackedEntity);
         }
      }

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         EntityPlayerMP var7 = (EntityPlayerMP)var1.get(var6);

         for(EntityTrackerEntry var5 : this.trackedEntitySet) {
            if (var5.trackedEntity != var7) {
               var5.updatePlayerEntity(var7);
            }
         }
      }

   }

   public void sendPacketToTrackedPlayers(Entity var1, Packet var2) {
      EntityTrackerEntry var3 = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(var1.entityId);
      if (var3 != null) {
         var3.sendPacketToTrackedPlayers(var2);
      }

   }

   public void sendPacketToTrackedPlayersAndTrackedEntity(Entity var1, Packet var2) {
      EntityTrackerEntry var3 = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(var1.entityId);
      if (var3 != null) {
         var3.sendPacketToTrackedPlayersAndTrackedEntity(var2);
      }

   }

   public void removeTrackedPlayerSymmetric(EntityPlayerMP var1) {
      for(EntityTrackerEntry var3 : this.trackedEntitySet) {
         var3.removeTrackedPlayerSymmetric(var1);
      }

   }
}
