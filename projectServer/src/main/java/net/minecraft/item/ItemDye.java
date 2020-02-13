package net.minecraft.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCloth;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.world.World;

public class ItemDye extends Item {
    public static final String[] dyeColors = new String[]{"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
    public static final int[] field_31023_bk = new int[]{1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 2651799, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye(int var1) {
        super(var1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
        if (var1.getItemDamage() == 15) {
            int var8 = var3.getBlockId(var4, var5, var6);
            if (var8 == Block.SAPLING.blockID) {
                if (!var3.singleplayerWorld) {
                    ((BlockSapling) Block.SAPLING).growTree(var3, var4, var5, var6, var3.rand);
                    --var1.stackSize;
                }

                return true;
            }

            if (var8 == Block.CROPS.blockID) {
                if (!var3.singleplayerWorld) {
                    ((BlockCrops) Block.CROPS).fertilize(var3, var4, var5, var6);
                    --var1.stackSize;
                }

                return true;
            }

            if (var8 == Block.GRASS.blockID) {
                if (!var3.singleplayerWorld) {
                    --var1.stackSize;

                    for (int var9 = 0; var9 < 128; ++var9) {
                        int var10 = var4;
                        int var11 = var5 + 1;
                        int var12 = var6;
                        int var13 = 0;

                        while (true) {
                            if (var13 >= var9 / 16) {
                                if (var3.getBlockId(var10, var11, var12) == 0) {
                                    if (ITEM_RAND.nextInt(10) != 0) {
                                        var3.setBlockAndMetadataWithNotify(var10, var11, var12, Block.TALLGRASS.blockID, 1);
                                    } else if (ITEM_RAND.nextInt(3) != 0) {
                                        var3.setBlockWithNotify(var10, var11, var12, Block.PLANT_YELLOW.blockID);
                                    } else {
                                        var3.setBlockWithNotify(var10, var11, var12, Block.PLANT_RED.blockID);
                                    }
                                }
                                break;
                            }

                            var10 += ITEM_RAND.nextInt(3) - 1;
                            var11 += (ITEM_RAND.nextInt(3) - 1) * ITEM_RAND.nextInt(3) / 2;
                            var12 += ITEM_RAND.nextInt(3) - 1;
                            if (var3.getBlockId(var10, var11 - 1, var12) != Block.GRASS.blockID || var3.isBlockNormalCube(var10, var11, var12)) {
                                break;
                            }

                            ++var13;
                        }
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void saddleEntity(ItemStack var1, EntityLiving var2) {
        if (var2 instanceof EntitySheep) {
            EntitySheep var3 = (EntitySheep) var2;
            int var4 = BlockCloth.func_21033_c(var1.getItemDamage());
            if (!var3.func_21069_f_() && var3.getFleeceColor() != var4) {
                var3.setFleeceColor(var4);
                --var1.stackSize;
            }
        }

    }
}
