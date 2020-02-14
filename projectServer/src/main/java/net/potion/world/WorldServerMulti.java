package net.potion.world;

import net.potion.world.storage.ISaveHandler;
import net.potion.server.PotionServer;

public class WorldServerMulti extends WorldServer {
    public WorldServerMulti(PotionServer var1, ISaveHandler var2, String var3, int var4, long var5, WorldServer var7) {
        super(var1, var2, var3, var4, var5);
        this.mapStorage = var7.mapStorage;
    }
}
