package net.potion.entity.player;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;

@Side(CodeSide.SERVER)
public interface IPlayerFileData {
    @Side(CodeSide.SERVER)
    void writePlayerData(EntityPlayer var1);

    @Side(CodeSide.SERVER)
    void readPlayerData(EntityPlayer var1);
}
