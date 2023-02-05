package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.material.Material;

public class BlockCloth extends Block {
    public BlockCloth() {
        super(35, 64, Material.CLOTH);
    }

    public static int method1(int var0) {
        return ~var0 & 15;
    }

    public static int method2(int var0) {
        return ~var0 & 15;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (metadata == 0) {
            return this.blockIndexInTexture;
        }

        metadata = ~(metadata & 15);
        return 113 + ((metadata & 8) >> 3) + (metadata & 7) * 16;
    }

    @Override
    protected int damageDropped(int var1) {
        return var1;
    }
}
