package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.IChunkLoader;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;

import java.io.File;
import java.util.List;

public class SaveOldDir extends SaveHandler {
    public SaveOldDir(File saveDirectory, String worldName, boolean createPlayerDirectory) {
        super(saveDirectory, worldName, createPlayerDirectory);
    }

    public IChunkLoader getChunkLoader(WorldProvider provider) {
        File saveDirectory = this.getSaveDirectory();
        if (provider instanceof WorldProviderHell) {
            File dimFile = new File(saveDirectory, "DIM-1");
            dimFile.mkdirs();
            return new McRegionChunkLoader(dimFile);
        }

        return new McRegionChunkLoader(saveDirectory);
    }

    public void saveWorldInfoAndPlayer(WorldInfo worldInfo, List<EntityPlayer> players) {
        worldInfo.setSaveVersion(19132);
        super.saveWorldInfoAndPlayer(worldInfo, players);
    }
}
