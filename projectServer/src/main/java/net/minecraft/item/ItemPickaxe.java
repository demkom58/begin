package net.minecraft.item;

import net.minecraft.material.Material;
import net.minecraft.block.Block;

public class ItemPickaxe extends ItemTool {
    private static Block[] blocksEffectiveAgainst = new Block[]{Block.COBBLESTONE, Block.STAIR_DOUBLE, Block.STAIR_SINGLE, Block.STONE, Block.SAND_STONE, Block.COBBLESTONE_MOSSY, Block.ORE_IRON, Block.BLOCK_IRON, Block.ORE_COAL, Block.BLOCK_GOLD, Block.ORE_GOLD, Block.ORE_DIAMOND, Block.BLOCK_DIAMOND, Block.ICE, Block.BLOOD_STONE, Block.ORE_LAPIS, Block.BLOCK_LAPIS};

    protected ItemPickaxe(int var1, EnumToolMaterial var2) {
        super(var1, 2, var2, blocksEffectiveAgainst);
    }

    @Override
    public boolean canHarvestBlock(Block var1) {
        if (var1 == Block.OBSIDIAN) {
            return this.toolMaterial.getHarvestLevel() == 3;
        } else if (var1 != Block.BLOCK_DIAMOND && var1 != Block.ORE_DIAMOND) {
            if (var1 != Block.BLOCK_GOLD && var1 != Block.ORE_GOLD) {
                if (var1 != Block.BLOCK_IRON && var1 != Block.ORE_IRON) {
                    if (var1 != Block.BLOCK_LAPIS && var1 != Block.ORE_LAPIS) {
                        if (var1 != Block.ORE_REDSTONE && var1 != Block.ORE_REDSTONE_GLOWING) {
                            if (var1.blockMaterial == Material.ROCK) {
                                return true;
                            } else {
                                return var1.blockMaterial == Material.IRON;
                            }
                        } else {
                            return this.toolMaterial.getHarvestLevel() >= 2;
                        }
                    } else {
                        return this.toolMaterial.getHarvestLevel() >= 1;
                    }
                } else {
                    return this.toolMaterial.getHarvestLevel() >= 1;
                }
            } else {
                return this.toolMaterial.getHarvestLevel() >= 2;
            }
        } else {
            return this.toolMaterial.getHarvestLevel() >= 2;
        }
    }
}
