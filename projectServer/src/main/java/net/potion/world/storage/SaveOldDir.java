package net.potion.world.storage;

import net.potion.entity.player.EntityPlayer;
import net.potion.entity.player.PlayerNBTManager;
import net.potion.world.WorldInfo;
import net.potion.world.WorldProvider;
import net.potion.world.WorldProviderHell;
import net.potion.world.chunk.IChunkLoader;

import java.io.File;
import java.util.List;

public class SaveOldDir extends PlayerNBTManager {
    public SaveOldDir(File var1, String var2, boolean var3) {
        super(var1, var2, var3);
    }

    @Override
    public IChunkLoader func_22092_a(WorldProvider var1) {
        File var2 = this.getWorldDir();
        if (var1 instanceof WorldProviderHell) {
            File var3 = new File(var2, "DIM-1");
            var3.mkdirs();
            return new RegionChunkLoader(var3);
        } else {
            return new RegionChunkLoader(var2);
        }
    }

    @Override
    public void saveWorldInfoAndPlayer(WorldInfo var1, List<EntityPlayer> var2) {
        var1.setVersion(19132);
        super.saveWorldInfoAndPlayer(var1, var2);
    }

    @Override
    public void func_22093_e() {
        RegionFileCache.clear();
    }
}
