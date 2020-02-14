package net.potion.world.storage;

import net.potion.entity.player.IPlayerFileData;
import net.potion.entity.player.EntityPlayer;
import net.potion.world.WorldInfo;
import net.potion.world.WorldProvider;
import net.potion.world.chunk.IChunkLoader;

import java.io.File;
import java.util.List;

public interface ISaveHandler {
    WorldInfo loadWorldInfo();

    void func_22091_b();

    IChunkLoader func_22092_a(WorldProvider var1);

    void saveWorldInfoAndPlayer(WorldInfo var1, List<EntityPlayer> var2);

    void func_22094_a(WorldInfo var1);

    IPlayerFileData func_22090_d();

    void func_22093_e();

    File func_28111_b(String var1);
}
