package net.potion.world.storage;

import net.potion.entity.player.EntityPlayer;
import net.potion.world.chunk.IChunkLoader;
import net.potion.world.WorldInfo;
import net.potion.world.WorldProvider;
import net.potion.world.WorldProviderHell;

import java.io.File;
import java.util.List;

public class SaveOldDir extends SaveHandler {
    public SaveOldDir(File saveDirectory, String worldName, boolean createPlayerDirectory) {
        super(saveDirectory, worldName, createPlayerDirectory);
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider provider) {
        File saveDirectory = this.getSaveDirectory();
        if (provider instanceof WorldProviderHell) {
            File dimFile = new File(saveDirectory, "DIM-1");
            dimFile.mkdirs();
            return new RegionChunkLoader(dimFile);
        }

        return new RegionChunkLoader(saveDirectory);
    }

    @Override
    public void saveWorldInfoAndPlayer(WorldInfo worldInfo, List<EntityPlayer> players) {
        worldInfo.setSaveVersion(19132);
        super.saveWorldInfoAndPlayer(worldInfo, players);
    }
}
