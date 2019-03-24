package net.minecraft.entity.player;

import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerFileData {
    void writePlayerData(EntityPlayer var1);

    void readPlayerData(EntityPlayer var1);
}
