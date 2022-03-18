package net.potion.world.storage;

import net.potion.entity.player.EntityPlayer;
import net.potion.world.WorldInfo;
import net.potion.world.WorldProvider;
import net.potion.world.WorldProviderHell;
import net.potion.world.chunk.IOldChunkLoader;

import java.io.File;
import java.util.List;

public class SaveOldDir extends SaveHandler {
    public SaveOldDir(File saveDirectory, String worldName, boolean createPlayerDirectory) {
        super(saveDirectory, worldName, createPlayerDirectory);
    }

    @Override
    public IOldChunkLoader getChunkLoader(WorldProvider provider) {
        File saveDirectory = this.getWorldDir();
        if (provider instanceof WorldProviderHell) {
            File dimFile = new File(saveDirectory, "DIM-1");
            dimFile.mkdirs();
            return new RegionChunkLoader(dimFile);
        }

        return new RegionChunkLoader(saveDirectory);
    }

    @Override
    public void saveWorldInfoAndPlayer(WorldInfo worldInfo, List<EntityPlayer> players) {
        worldInfo.setVersion(19132);
        super.saveWorldInfoAndPlayer(worldInfo, players);
    }

    @Override
    public void clearCache() {
        RegionFileCache.clear();
    }
}
