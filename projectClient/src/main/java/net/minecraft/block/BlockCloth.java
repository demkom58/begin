package net.minecraft.block;

import net.minecraft.material.Material;

public class BlockCloth extends Block {
    public BlockCloth() {
        super(35, 64, Material.CLOTH);
    }

    public static int func_21034_c(int var0) {
        return ~var0 & 15;
    }

    public static int func_21035_d(int var0) {
        return ~var0 & 15;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (metadata == 0) {
            return this.blockIndexInTexture;
        } else {
            metadata = ~(metadata & 15);
            return 113 + ((metadata & 8) >> 3) + (metadata & 7) * 16;
        }
    }

    @Override
    protected int damageDropped(int var1) {
        return var1;
    }
}
