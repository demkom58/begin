package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.IChunkLoader;

import java.io.File;
import java.util.List;

public class SaveOldDir extends SaveHandler {
    public SaveOldDir(File saveDirectory, String worldName, boolean createPlayerDirectory) {
        super(saveDirectory, worldName, createPlayerDirectory);
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider provider) {
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
