package net.minecraft.client.render.entity;

import net.hypnosis.render.Tessellator;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityRenderer;
import net.minecraft.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3D;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderBlocks {
    public static boolean fancyGrass = true;
    public boolean field_31088_b = true;
    private IBlockAccess blockAccess;
    private int overrideBlockTexture = -1;
    private boolean flipTexture = false;
    private boolean renderAllFaces = false;
    private int field_31087_g = 0;
    private int field_31086_h = 0;
    private int field_31085_i = 0;
    private int field_31084_j = 0;
    private int field_31083_k = 0;
    private int field_31082_l = 0;
    private boolean enableAO;
    private float lightValueOwn;
    private float aoLightValueXNeg;
    private float aoLightValueYNeg;
    private float aoLightValueZNeg;
    private float aoLightValueXPos;
    private float aoLightValueYPos;
    private float aoLightValueZPos;
    private float field_22377_m;
    private float field_22376_n;
    private float field_22375_o;
    private float field_22374_p;
    private float field_22373_q;
    private float field_22372_r;
    private float field_22371_s;
    private float field_22370_t;
    private float field_22369_u;
    private float field_22368_v;
    private float field_22367_w;
    private float field_22366_x;
    private float field_22365_y;
    private float field_22364_z;
    private float field_22362_A;
    private float field_22360_B;
    private float field_22358_C;
    private float field_22356_D;
    private float field_22354_E;
    private float field_22353_F;
    private int field_22352_G = 1;
    private float colorRedTopLeft;
    private float colorRedBottomLeft;
    private float colorRedBottomRight;
    private float colorRedTopRight;
    private float colorGreenTopLeft;
    private float colorGreenBottomLeft;
    private float colorGreenBottomRight;
    private float colorGreenTopRight;
    private float colorBlueTopLeft;
    private float colorBlueBottomLeft;
    private float colorBlueBottomRight;
    private float colorBlueTopRight;
    private boolean field_22339_T;
    private boolean field_22338_U;
    private boolean field_22337_V;
    private boolean field_22336_W;
    private boolean field_22335_X;
    private boolean field_22334_Y;
    private boolean field_22333_Z;
    private boolean field_22363_aa;
    private boolean field_22361_ab;
    private boolean field_22359_ac;
    private boolean field_22357_ad;
    private boolean field_22355_ae;

    public RenderBlocks(IBlockAccess blockAccess) {
        this.blockAccess = blockAccess;
    }

    public RenderBlocks() {
    }

    public static boolean renderItemIn3d(int var0) {
        if (var0 == 0) {
            return true;
        } else if (var0 == 13) {
            return true;
        } else if (var0 == 10) {
            return true;
        } else if (var0 == 11) {
            return true;
        } else {
            return var0 == 16;
        }
    }

    public void renderBlockUsingTexture(Block block, int x, int y, int z, int overrideBlockTexture) {
        this.overrideBlockTexture = overrideBlockTexture;
        this.renderBlockByRenderType(block, x, y, z);
        this.overrideBlockTexture = -1;
    }

    public void func_31075_a(Block block, int x, int y, int z) {
        this.renderAllFaces = true;
        this.renderBlockByRenderType(block, x, y, z);
        this.renderAllFaces = false;
    }

    public boolean renderBlockByRenderType(Block block, int x, int y, int z) {
        int renderType = block.getRenderType();
        block.setBlockBoundsBasedOnState(this.blockAccess, x, y, z);
        if (renderType == 0) {
            return this.renderStandardBlock(block, x, y, z);
        } else if (renderType == 4) {
            return this.renderBlockFluids(block, x, y, z);
        } else if (renderType == 13) {
            return this.renderBlockCactus(block, x, y, z);
        } else if (renderType == 1) {
            return this.renderBlockReed(block, x, y, z);
        } else if (renderType == 6) {
            return this.renderBlockCrops(block, x, y, z);
        } else if (renderType == 2) {
            return this.renderBlockTorch(block, x, y, z);
        } else if (renderType == 3) {
            return this.renderBlockFire(block, x, y, z);
        } else if (renderType == 5) {
            return this.renderBlockRedstoneWire(block, x, y, z);
        } else if (renderType == 8) {
            return this.renderBlockLadder(block, x, y, z);
        } else if (renderType == 7) {
            return this.renderBlockDoor(block, x, y, z);
        } else if (renderType == 9) {
            return this.renderBlockMinecartTrack((BlockRail) block, x, y, z);
        } else if (renderType == 10) {
            return this.renderBlockStairs(block, x, y, z);
        } else if (renderType == 11) {
            return this.renderBlockFence(block, x, y, z);
        } else if (renderType == 12) {
            return this.renderBlockLever(block, x, y, z);
        } else if (renderType == 14) {
            return this.renderBlockBed(block, x, y, z);
        } else if (renderType == 15) {
            return this.renderBlockRepeater(block, x, y, z);
        } else if (renderType == 16) {
            return this.func_31074_b(block, x, y, z, false);
        } else {
            return renderType == 17 && this.func_31080_c(block, x, y, z, true);
        }
    }

    private boolean renderBlockBed(Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        int metadata = this.blockAccess.getBlockMetadata(x, y, z);
        int var7 = BlockBed.getDirectionFromMetadata(metadata);
        boolean var8 = BlockBed.isBlockFootOfBed(metadata);
        float var9 = 0.5F;
        float var10 = 1.0F;
        float var11 = 0.8F;
        float var12 = 0.6F;
        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        tess.setColorOpaque_F(var9 * brightness, var9 * brightness, var9 * brightness);
        int var26 = block.getBlockTexture(this.blockAccess, x, y, z, 0);
        int var27 = (var26 & 15) << 4;
        int var28 = var26 & 240;
        double var29 = (float) var27 / 256.0F;
        double var31 = ((double) (var27 + 16) - 0.01D) / 256.0D;
        double var33 = (float) var28 / 256.0F;
        double var35 = ((double) (var28 + 16) - 0.01D) / 256.0D;
        double var37 = (double) x + block.minX;
        double var39 = (double) x + block.maxX;
        double var41 = (double) y + block.minY + 0.1875D;
        double var43 = (double) z + block.minZ;
        double var45 = (double) z + block.maxZ;
        tess.addVertexWithUV(var37, var41, var45, var29, var35);
        tess.addVertexWithUV(var37, var41, var43, var29, var33);
        tess.addVertexWithUV(var39, var41, var43, var31, var33);
        tess.addVertexWithUV(var39, var41, var45, var31, var35);
        float var64 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
        tess.setColorOpaque_F(var10 * var64, var10 * var64, var10 * var64);
        var27 = block.getBlockTexture(this.blockAccess, x, y, z, 1);
        var28 = (var27 & 15) << 4;
        int var73 = var27 & 240;
        double var30 = (float) var28 / 256.0F;
        double var32 = ((double) (var28 + 16) - 0.01D) / 256.0D;
        double var34 = (float) var73 / 256.0F;
        double var36 = ((double) (var73 + 16) - 0.01D) / 256.0D;
        double var38 = var30;
        double var40 = var32;
        double var42 = var34;
        double var44 = var34;
        double var46 = var30;
        double var48 = var32;
        double var50 = var36;
        double var52 = var36;
        if (var7 == 0) {
            var40 = var30;
            var42 = var36;
            var46 = var32;
            var52 = var34;
        } else if (var7 == 2) {
            var38 = var32;
            var44 = var36;
            var48 = var30;
            var50 = var34;
        } else if (var7 == 3) {
            var38 = var32;
            var44 = var36;
            var48 = var30;
            var50 = var34;
            var40 = var30;
            var42 = var36;
            var46 = var32;
            var52 = var34;
        }

        double var54 = (double) x + block.minX;
        double var56 = (double) x + block.maxX;
        double var58 = (double) y + block.maxY;
        double var60 = (double) z + block.minZ;
        double var62 = (double) z + block.maxZ;
        tess.addVertexWithUV(var56, var58, var62, var46, var50);
        tess.addVertexWithUV(var56, var58, var60, var38, var42);
        tess.addVertexWithUV(var54, var58, var60, var40, var44);
        tess.addVertexWithUV(var54, var58, var62, var48, var52);
        int var65 = ModelBed.field_22280_a[var7];
        if (var8) {
            var65 = ModelBed.field_22280_a[ModelBed.field_22279_b[var7]];
        }

        var27 = 4;
        switch (var7) {
            case 0:
                var27 = 5;
                break;
            case 1:
                var27 = 3;
            case 2:
            default:
                break;
            case 3:
                var27 = 2;
        }

        if (var65 != 2 && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2))) {
            float var69 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
            if (block.minZ > 0.0D) {
                var69 = brightness;
            }

            tess.setColorOpaque_F(var11 * var69, var11 * var69, var11 * var69);
            this.flipTexture = var27 == 2;
            this.renderEastFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 2));
        }

        if (var65 != 3 && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3))) {
            float var70 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
            if (block.maxZ < 1.0D) {
                var70 = brightness;
            }

            tess.setColorOpaque_F(var11 * var70, var11 * var70, var11 * var70);
            this.flipTexture = var27 == 3;
            this.renderWestFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 3));
        }

        if (var65 != 4 && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4))) {
            float var71 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
            if (block.minX > 0.0D) {
                var71 = brightness;
            }

            tess.setColorOpaque_F(var12 * var71, var12 * var71, var12 * var71);
            this.flipTexture = var27 == 4;
            this.renderNorthFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 4));
        }

        if (var65 != 5 && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5))) {
            float var72 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
            if (block.maxX < 1.0D) {
                var72 = brightness;
            }

            tess.setColorOpaque_F(var12 * var72, var12 * var72, var12 * var72);
            this.flipTexture = var27 == 5;
            this.renderSouthFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 5));
        }

        this.flipTexture = false;
        return true;
    }

    public boolean renderBlockTorch(Block block, int x, int y, int z) {
        int metadata = this.blockAccess.getBlockMetadata(x, y, z);
        Tessellator tess = Tessellator.INSTANCE;
        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            brightness = 1.0F;
        }

        tess.setColorOpaque_F(brightness, brightness, brightness);
        double var8 = 0.4000000059604645D;
        double var10 = 0.5D - var8;
        double var12 = 0.20000000298023224D;
        if (metadata == 1) {
            this.renderTorchAtAngle(block, (double) x - var10, (double) y + var12, z, -var8, 0.0D);
        } else if (metadata == 2) {
            this.renderTorchAtAngle(block, (double) x + var10, (double) y + var12, z, var8, 0.0D);
        } else if (metadata == 3) {
            this.renderTorchAtAngle(block, x, (double) y + var12, (double) z - var10, 0.0D, -var8);
        } else if (metadata == 4) {
            this.renderTorchAtAngle(block, x, (double) y + var12, (double) z + var10, 0.0D, var8);
        } else {
            this.renderTorchAtAngle(block, x, y, z, 0.0D, 0.0D);
        }

        return true;
    }

    private boolean renderBlockRepeater(Block block, int x, int y, int z) {
        int metadata = this.blockAccess.getBlockMetadata(x, y, z);
        int var6 = metadata & 3;
        int var7 = (metadata & 12) >> 2;
        this.renderStandardBlock(block, x, y, z);
        Tessellator tess = Tessellator.INSTANCE;
        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            brightness = (brightness + 1.0F) * 0.5F;
        }

        tess.setColorOpaque_F(brightness, brightness, brightness);
        double var10 = -0.1875D;
        double var12 = 0.0D;
        double var14 = 0.0D;
        double var16 = 0.0D;
        double var18 = 0.0D;
        switch (var6) {
            case 0:
                var18 = -0.3125D;
                var14 = BlockRedstoneRepeater.field_22024_a[var7];
                break;
            case 1:
                var16 = 0.3125D;
                var12 = -BlockRedstoneRepeater.field_22024_a[var7];
                break;
            case 2:
                var18 = 0.3125D;
                var14 = -BlockRedstoneRepeater.field_22024_a[var7];
                break;
            case 3:
                var16 = -0.3125D;
                var12 = BlockRedstoneRepeater.field_22024_a[var7];
        }

        this.renderTorchAtAngle(block, (double) x + var12, (double) y + var10, (double) z + var14, 0.0D, 0.0D);
        this.renderTorchAtAngle(block, (double) x + var16, (double) y + var10, (double) z + var18, 0.0D, 0.0D);
        int var20 = block.getBlockTextureFromSide(1);
        int var21 = (var20 & 15) << 4;
        int var22 = var20 & 240;
        double var23 = (float) var21 / 256.0F;
        double var25 = ((float) var21 + 15.99F) / 256.0F;
        double var27 = (float) var22 / 256.0F;
        double var29 = ((float) var22 + 15.99F) / 256.0F;
        float var31 = 0.125F;
        float var32 = (float) (x + 1);
        float var33 = (float) (x + 1);
        float var34 = (float) (x + 0);
        float var35 = (float) (x + 0);
        float var36 = (float) (z + 0);
        float var37 = (float) (z + 1);
        float var38 = (float) (z + 1);
        float var39 = (float) (z + 0);
        float var40 = (float) y + var31;
        if (var6 == 2) {
            var32 = var33 = (float) (x + 0);
            var34 = var35 = (float) (x + 1);
            var36 = var39 = (float) (z + 1);
            var37 = var38 = (float) (z + 0);
        } else if (var6 == 3) {
            var32 = var35 = (float) (x + 0);
            var33 = var34 = (float) (x + 1);
            var36 = var37 = (float) (z + 0);
            var38 = var39 = (float) (z + 1);
        } else if (var6 == 1) {
            var32 = var35 = (float) (x + 1);
            var33 = var34 = (float) (x + 0);
            var36 = var37 = (float) (z + 1);
            var38 = var39 = (float) (z + 0);
        }

        tess.addVertexWithUV(var35, var40, var39, var23, var27);
        tess.addVertexWithUV(var34, var40, var38, var23, var29);
        tess.addVertexWithUV(var33, var40, var37, var25, var29);
        tess.addVertexWithUV(var32, var40, var36, var25, var27);
        return true;
    }

    public void func_31078_d(Block block, int x, int y, int z) {
        this.renderAllFaces = true;
        this.func_31074_b(block, x, y, z, true);
        this.renderAllFaces = false;
    }

    private boolean func_31074_b(Block block, int x, int y, int z, boolean var5) {
        int var6 = this.blockAccess.getBlockMetadata(x, y, z);
        boolean var7 = var5 || (var6 & 8) != 0;
        int var8 = BlockPistonBase.func_31044_d(var6);
        if (var7) {
            switch (var8) {
                case 0:
                    this.field_31087_g = 3;
                    this.field_31086_h = 3;
                    this.field_31085_i = 3;
                    this.field_31084_j = 3;
                    block.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;
                case 1:
                    block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                    break;
                case 2:
                    this.field_31085_i = 1;
                    this.field_31084_j = 2;
                    block.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                    break;
                case 3:
                    this.field_31085_i = 2;
                    this.field_31084_j = 1;
                    this.field_31083_k = 3;
                    this.field_31082_l = 3;
                    block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                    break;
                case 4:
                    this.field_31087_g = 1;
                    this.field_31086_h = 2;
                    this.field_31083_k = 2;
                    this.field_31082_l = 1;
                    block.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;
                case 5:
                    this.field_31087_g = 2;
                    this.field_31086_h = 1;
                    this.field_31083_k = 1;
                    this.field_31082_l = 2;
                    block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }

            this.renderStandardBlock(block, x, y, z);
            this.field_31087_g = 0;
            this.field_31086_h = 0;
            this.field_31085_i = 0;
            this.field_31084_j = 0;
            this.field_31083_k = 0;
            this.field_31082_l = 0;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        } else {
            switch (var8) {
                case 0:
                    this.field_31087_g = 3;
                    this.field_31086_h = 3;
                    this.field_31085_i = 3;
                    this.field_31084_j = 3;
                case 1:
                default:
                    break;
                case 2:
                    this.field_31085_i = 1;
                    this.field_31084_j = 2;
                    break;
                case 3:
                    this.field_31085_i = 2;
                    this.field_31084_j = 1;
                    this.field_31083_k = 3;
                    this.field_31082_l = 3;
                    break;
                case 4:
                    this.field_31087_g = 1;
                    this.field_31086_h = 2;
                    this.field_31083_k = 2;
                    this.field_31082_l = 1;
                    break;
                case 5:
                    this.field_31087_g = 2;
                    this.field_31086_h = 1;
                    this.field_31083_k = 1;
                    this.field_31082_l = 2;
            }

            this.renderStandardBlock(block, x, y, z);
            this.field_31087_g = 0;
            this.field_31086_h = 0;
            this.field_31085_i = 0;
            this.field_31084_j = 0;
            this.field_31083_k = 0;
            this.field_31082_l = 0;
        }

        return true;
    }

    private void func_31076_a(double var1, double var3, double var5, double var7, double var9, double var11, float var13, double var14) {
        int var16 = 108;
        if (this.overrideBlockTexture >= 0) {
            var16 = this.overrideBlockTexture;
        }

        int var17 = (var16 & 15) << 4;
        int var18 = var16 & 240;
        Tessellator tess = Tessellator.INSTANCE;
        double var20 = (float) (var17 + 0) / 256.0F;
        double var22 = (float) (var18 + 0) / 256.0F;
        double var24 = ((double) var17 + var14 - 0.01D) / 256.0D;
        double var26 = ((double) ((float) var18 + 4.0F) - 0.01D) / 256.0D;
        tess.setColorOpaque_F(var13, var13, var13);
        tess.addVertexWithUV(var1, var7, var9, var24, var22);
        tess.addVertexWithUV(var1, var5, var9, var20, var22);
        tess.addVertexWithUV(var3, var5, var11, var20, var26);
        tess.addVertexWithUV(var3, var7, var11, var24, var26);
    }

    private void func_31081_b(double var1, double var3, double var5, double var7, double var9, double var11, float var13, double var14) {
        int var16 = 108;
        if (this.overrideBlockTexture >= 0) {
            var16 = this.overrideBlockTexture;
        }

        int var17 = (var16 & 15) << 4;
        int var18 = var16 & 240;
        Tessellator tess = Tessellator.INSTANCE;
        double var20 = (float) (var17 + 0) / 256.0F;
        double var22 = (float) (var18 + 0) / 256.0F;
        double var24 = ((double) var17 + var14 - 0.01D) / 256.0D;
        double var26 = ((double) ((float) var18 + 4.0F) - 0.01D) / 256.0D;
        tess.setColorOpaque_F(var13, var13, var13);
        tess.addVertexWithUV(var1, var5, var11, var24, var22);
        tess.addVertexWithUV(var1, var5, var9, var20, var22);
        tess.addVertexWithUV(var3, var7, var9, var20, var26);
        tess.addVertexWithUV(var3, var7, var11, var24, var26);
    }

    private void func_31077_c(double var1, double var3, double var5, double var7, double var9, double var11, float var13, double var14) {
        int var16 = 108;
        if (this.overrideBlockTexture >= 0) {
            var16 = this.overrideBlockTexture;
        }

        int var17 = (var16 & 15) << 4;
        int var18 = var16 & 240;
        Tessellator tess = Tessellator.INSTANCE;
        double var20 = (float) (var17 + 0) / 256.0F;
        double var22 = (float) (var18 + 0) / 256.0F;
        double var24 = ((double) var17 + var14 - 0.01D) / 256.0D;
        double var26 = ((double) ((float) var18 + 4.0F) - 0.01D) / 256.0D;
        tess.setColorOpaque_F(var13, var13, var13);
        tess.addVertexWithUV(var3, var5, var9, var24, var22);
        tess.addVertexWithUV(var1, var5, var9, var20, var22);
        tess.addVertexWithUV(var1, var7, var11, var20, var26);
        tess.addVertexWithUV(var3, var7, var11, var24, var26);
    }

    public void func_31079_a(Block block, int x, int y, int z, boolean var5) {
        this.renderAllFaces = true;
        this.func_31080_c(block, x, y, z, var5);
        this.renderAllFaces = false;
    }

    private boolean func_31080_c(Block block, int x, int y, int z, boolean var5) {
        int var6 = this.blockAccess.getBlockMetadata(x, y, z);
        int var7 = BlockPistonExtension.func_31050_c(var6);
        float var11 = block.getBlockBrightness(this.blockAccess, x, y, z);
        float var12 = var5 ? 1.0F : 0.5F;
        double var13 = var5 ? 16.0D : 8.0D;
        switch (var7) {
            case 0:
                this.field_31087_g = 3;
                this.field_31086_h = 3;
                this.field_31085_i = 3;
                this.field_31084_j = 3;
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                this.renderStandardBlock(block, x, y, z);
                this.func_31076_a((float) x + 0.375F, (float) x + 0.625F, (float) y + 0.25F, (float) y + 0.25F + var12, (float) z + 0.625F, (float) z + 0.625F, var11 * 0.8F, var13);
                this.func_31076_a((float) x + 0.625F, (float) x + 0.375F, (float) y + 0.25F, (float) y + 0.25F + var12, (float) z + 0.375F, (float) z + 0.375F, var11 * 0.8F, var13);
                this.func_31076_a((float) x + 0.375F, (float) x + 0.375F, (float) y + 0.25F, (float) y + 0.25F + var12, (float) z + 0.375F, (float) z + 0.625F, var11 * 0.6F, var13);
                this.func_31076_a((float) x + 0.625F, (float) x + 0.625F, (float) y + 0.25F, (float) y + 0.25F + var12, (float) z + 0.625F, (float) z + 0.375F, var11 * 0.6F, var13);
                break;
            case 1:
                block.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                this.renderStandardBlock(block, x, y, z);
                this.func_31076_a((float) x + 0.375F, (float) x + 0.625F, (float) y - 0.25F + 1.0F - var12, (float) y - 0.25F + 1.0F, (float) z + 0.625F, (float) z + 0.625F, var11 * 0.8F, var13);
                this.func_31076_a((float) x + 0.625F, (float) x + 0.375F, (float) y - 0.25F + 1.0F - var12, (float) y - 0.25F + 1.0F, (float) z + 0.375F, (float) z + 0.375F, var11 * 0.8F, var13);
                this.func_31076_a((float) x + 0.375F, (float) x + 0.375F, (float) y - 0.25F + 1.0F - var12, (float) y - 0.25F + 1.0F, (float) z + 0.375F, (float) z + 0.625F, var11 * 0.6F, var13);
                this.func_31076_a((float) x + 0.625F, (float) x + 0.625F, (float) y - 0.25F + 1.0F - var12, (float) y - 0.25F + 1.0F, (float) z + 0.625F, (float) z + 0.375F, var11 * 0.6F, var13);
                break;
            case 2:
                this.field_31085_i = 1;
                this.field_31084_j = 2;
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                this.renderStandardBlock(block, x, y, z);
                this.func_31081_b((float) x + 0.375F, (float) x + 0.375F, (float) y + 0.625F, (float) y + 0.375F, (float) z + 0.25F, (float) z + 0.25F + var12, var11 * 0.6F, var13);
                this.func_31081_b((float) x + 0.625F, (float) x + 0.625F, (float) y + 0.375F, (float) y + 0.625F, (float) z + 0.25F, (float) z + 0.25F + var12, var11 * 0.6F, var13);
                this.func_31081_b((float) x + 0.375F, (float) x + 0.625F, (float) y + 0.375F, (float) y + 0.375F, (float) z + 0.25F, (float) z + 0.25F + var12, var11 * 0.5F, var13);
                this.func_31081_b((float) x + 0.625F, (float) x + 0.375F, (float) y + 0.625F, (float) y + 0.625F, (float) z + 0.25F, (float) z + 0.25F + var12, var11, var13);
                break;
            case 3:
                this.field_31085_i = 2;
                this.field_31084_j = 1;
                this.field_31083_k = 3;
                this.field_31082_l = 3;
                block.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                this.renderStandardBlock(block, x, y, z);
                this.func_31081_b((float) x + 0.375F, (float) x + 0.375F, (float) y + 0.625F, (float) y + 0.375F, (float) z - 0.25F + 1.0F - var12, (float) z - 0.25F + 1.0F, var11 * 0.6F, var13);
                this.func_31081_b((float) x + 0.625F, (float) x + 0.625F, (float) y + 0.375F, (float) y + 0.625F, (float) z - 0.25F + 1.0F - var12, (float) z - 0.25F + 1.0F, var11 * 0.6F, var13);
                this.func_31081_b((float) x + 0.375F, (float) x + 0.625F, (float) y + 0.375F, (float) y + 0.375F, (float) z - 0.25F + 1.0F - var12, (float) z - 0.25F + 1.0F, var11 * 0.5F, var13);
                this.func_31081_b((float) x + 0.625F, (float) x + 0.375F, (float) y + 0.625F, (float) y + 0.625F, (float) z - 0.25F + 1.0F - var12, (float) z - 0.25F + 1.0F, var11, var13);
                break;
            case 4:
                this.field_31087_g = 1;
                this.field_31086_h = 2;
                this.field_31083_k = 2;
                this.field_31082_l = 1;
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                this.renderStandardBlock(block, x, y, z);
                this.func_31077_c((float) x + 0.25F, (float) x + 0.25F + var12, (float) y + 0.375F, (float) y + 0.375F, (float) z + 0.625F, (float) z + 0.375F, var11 * 0.5F, var13);
                this.func_31077_c((float) x + 0.25F, (float) x + 0.25F + var12, (float) y + 0.625F, (float) y + 0.625F, (float) z + 0.375F, (float) z + 0.625F, var11, var13);
                this.func_31077_c((float) x + 0.25F, (float) x + 0.25F + var12, (float) y + 0.375F, (float) y + 0.625F, (float) z + 0.375F, (float) z + 0.375F, var11 * 0.6F, var13);
                this.func_31077_c((float) x + 0.25F, (float) x + 0.25F + var12, (float) y + 0.625F, (float) y + 0.375F, (float) z + 0.625F, (float) z + 0.625F, var11 * 0.6F, var13);
                break;
            case 5:
                this.field_31087_g = 2;
                this.field_31086_h = 1;
                this.field_31083_k = 1;
                this.field_31082_l = 2;
                block.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                this.renderStandardBlock(block, x, y, z);
                this.func_31077_c((float) x - 0.25F + 1.0F - var12, (float) x - 0.25F + 1.0F, (float) y + 0.375F, (float) y + 0.375F, (float) z + 0.625F, (float) z + 0.375F, var11 * 0.5F, var13);
                this.func_31077_c((float) x - 0.25F + 1.0F - var12, (float) x - 0.25F + 1.0F, (float) y + 0.625F, (float) y + 0.625F, (float) z + 0.375F, (float) z + 0.625F, var11, var13);
                this.func_31077_c((float) x - 0.25F + 1.0F - var12, (float) x - 0.25F + 1.0F, (float) y + 0.375F, (float) y + 0.625F, (float) z + 0.375F, (float) z + 0.375F, var11 * 0.6F, var13);
                this.func_31077_c((float) x - 0.25F + 1.0F - var12, (float) x - 0.25F + 1.0F, (float) y + 0.625F, (float) y + 0.375F, (float) z + 0.625F, (float) z + 0.625F, var11 * 0.6F, var13);
        }

        this.field_31087_g = 0;
        this.field_31086_h = 0;
        this.field_31085_i = 0;
        this.field_31084_j = 0;
        this.field_31083_k = 0;
        this.field_31082_l = 0;
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    public boolean renderBlockLever(Block block, int x, int y, int z) {
        int metadata = this.blockAccess.getBlockMetadata(x, y, z);
        int var6 = metadata & 7;
        boolean var7 = (metadata & 8) > 0;
        Tessellator tess = Tessellator.INSTANCE;
        boolean var9 = this.overrideBlockTexture >= 0;
        if (!var9) {
            this.overrideBlockTexture = Block.COBBLESTONE.blockIndexInTexture;
        }

        float var10 = 0.25F;
        float var11 = 0.1875F;
        float var12 = 0.1875F;
        if (var6 == 5) {
            block.setBlockBounds(0.5F - var11, 0.0F, 0.5F - var10, 0.5F + var11, var12, 0.5F + var10);
        } else if (var6 == 6) {
            block.setBlockBounds(0.5F - var10, 0.0F, 0.5F - var11, 0.5F + var10, var12, 0.5F + var11);
        } else if (var6 == 4) {
            block.setBlockBounds(0.5F - var11, 0.5F - var10, 1.0F - var12, 0.5F + var11, 0.5F + var10, 1.0F);
        } else if (var6 == 3) {
            block.setBlockBounds(0.5F - var11, 0.5F - var10, 0.0F, 0.5F + var11, 0.5F + var10, var12);
        } else if (var6 == 2) {
            block.setBlockBounds(1.0F - var12, 0.5F - var10, 0.5F - var11, 1.0F, 0.5F + var10, 0.5F + var11);
        } else if (var6 == 1) {
            block.setBlockBounds(0.0F, 0.5F - var10, 0.5F - var11, var12, 0.5F + var10, 0.5F + var11);
        }

        this.renderStandardBlock(block, x, y, z);
        if (!var9) {
            this.overrideBlockTexture = -1;
        }

        float var13 = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            var13 = 1.0F;
        }

        tess.setColorOpaque_F(var13, var13, var13);
        int var14 = block.getBlockTextureFromSide(0);
        if (this.overrideBlockTexture >= 0) {
            var14 = this.overrideBlockTexture;
        }

        int var15 = (var14 & 15) << 4;
        int var16 = var14 & 240;
        float var17 = (float) var15 / 256.0F;
        float var18 = ((float) var15 + 15.99F) / 256.0F;
        float var19 = (float) var16 / 256.0F;
        float var20 = ((float) var16 + 15.99F) / 256.0F;
        Vec3D[] var21 = new Vec3D[8];
        float var22 = 0.0625F;
        float var23 = 0.0625F;
        float var24 = 0.625F;
        var21[0] = Vec3D.createVector(-var22, 0.0D, -var23);
        var21[1] = Vec3D.createVector(var22, 0.0D, -var23);
        var21[2] = Vec3D.createVector(var22, 0.0D, var23);
        var21[3] = Vec3D.createVector(-var22, 0.0D, var23);
        var21[4] = Vec3D.createVector(-var22, var24, -var23);
        var21[5] = Vec3D.createVector(var22, var24, -var23);
        var21[6] = Vec3D.createVector(var22, var24, var23);
        var21[7] = Vec3D.createVector(-var22, var24, var23);

        for (int var25 = 0; var25 < 8; ++var25) {
            if (var7) {
                var21[var25].zCoord -= 0.0625D;
                var21[var25].rotateAroundX(0.69813174F);
            } else {
                var21[var25].zCoord += 0.0625D;
                var21[var25].rotateAroundX(-0.69813174F);
            }

            if (var6 == 6) {
                var21[var25].rotateAroundY(1.5707964F);
            }

            if (var6 < 5) {
                var21[var25].yCoord -= 0.375D;
                var21[var25].rotateAroundX(1.5707964F);
                if (var6 == 4) {
                    var21[var25].rotateAroundY(0.0F);
                }

                if (var6 == 3) {
                    var21[var25].rotateAroundY(3.1415927F);
                }

                if (var6 == 2) {
                    var21[var25].rotateAroundY(1.5707964F);
                }

                if (var6 == 1) {
                    var21[var25].rotateAroundY(-1.5707964F);
                }

                var21[var25].xCoord += (double) x + 0.5D;
                var21[var25].yCoord += (float) y + 0.5F;
                var21[var25].zCoord += (double) z + 0.5D;
            } else {
                var21[var25].xCoord += (double) x + 0.5D;
                var21[var25].yCoord += (float) y + 0.125F;
                var21[var25].zCoord += (double) z + 0.5D;
            }
        }

        Vec3D var30 = null;
        Vec3D var26 = null;
        Vec3D var27 = null;
        Vec3D var28 = null;

        for (int var29 = 0; var29 < 6; ++var29) {
            if (var29 == 0) {
                var17 = (float) (var15 + 7) / 256.0F;
                var18 = ((float) (var15 + 9) - 0.01F) / 256.0F;
                var19 = (float) (var16 + 6) / 256.0F;
                var20 = ((float) (var16 + 8) - 0.01F) / 256.0F;
            } else if (var29 == 2) {
                var17 = (float) (var15 + 7) / 256.0F;
                var18 = ((float) (var15 + 9) - 0.01F) / 256.0F;
                var19 = (float) (var16 + 6) / 256.0F;
                var20 = ((float) (var16 + 16) - 0.01F) / 256.0F;
            }

            if (var29 == 0) {
                var30 = var21[0];
                var26 = var21[1];
                var27 = var21[2];
                var28 = var21[3];
            } else if (var29 == 1) {
                var30 = var21[7];
                var26 = var21[6];
                var27 = var21[5];
                var28 = var21[4];
            } else if (var29 == 2) {
                var30 = var21[1];
                var26 = var21[0];
                var27 = var21[4];
                var28 = var21[5];
            } else if (var29 == 3) {
                var30 = var21[2];
                var26 = var21[1];
                var27 = var21[5];
                var28 = var21[6];
            } else if (var29 == 4) {
                var30 = var21[3];
                var26 = var21[2];
                var27 = var21[6];
                var28 = var21[7];
            } else if (var29 == 5) {
                var30 = var21[0];
                var26 = var21[3];
                var27 = var21[7];
                var28 = var21[4];
            }

            tess.addVertexWithUV(var30.xCoord, var30.yCoord, var30.zCoord, var17, var20);
            tess.addVertexWithUV(var26.xCoord, var26.yCoord, var26.zCoord, var18, var20);
            tess.addVertexWithUV(var27.xCoord, var27.yCoord, var27.zCoord, var18, var19);
            tess.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord, var17, var19);
        }

        return true;
    }

    public boolean renderBlockFire(Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        int var6 = block.getBlockTextureFromSide(0);
        if (this.overrideBlockTexture >= 0) {
            var6 = this.overrideBlockTexture;
        }

        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        tess.setColorOpaque_F(brightness, brightness, brightness);
        int var8 = (var6 & 15) << 4;
        int var9 = var6 & 240;
        double var10 = (float) var8 / 256.0F;
        double var12 = ((float) var8 + 15.99F) / 256.0F;
        double var14 = (float) var9 / 256.0F;
        double var16 = ((float) var9 + 15.99F) / 256.0F;
        float var18 = 1.4F;
        if (!this.blockAccess.isBlockNormalCube(x, y - 1, z) && !Block.FIRE.canBlockCatchFire(this.blockAccess, x, y - 1, z)) {
            float var60 = 0.2F;
            float var20 = 0.0625F;
            if ((x + y + z & 1) == 1) {
                var10 = (float) var8 / 256.0F;
                var12 = ((float) var8 + 15.99F) / 256.0F;
                var14 = (float) (var9 + 16) / 256.0F;
                var16 = ((float) var9 + 15.99F + 16.0F) / 256.0F;
            }

            if ((x / 2 + y / 2 + z / 2 & 1) == 1) {
                double var62 = var12;
                var12 = var10;
                var10 = var62;
            }

            if (Block.FIRE.canBlockCatchFire(this.blockAccess, x - 1, y, z)) {
                tess.addVertexWithUV((float) x + var60, (float) y + var18 + var20, z + 1, var12, var14);
                tess.addVertexWithUV(x + 0, (float) (y + 0) + var20, z + 1, var12, var16);
                tess.addVertexWithUV(x + 0, (float) (y + 0) + var20, z + 0, var10, var16);
                tess.addVertexWithUV((float) x + var60, (float) y + var18 + var20, z + 0, var10, var14);
                tess.addVertexWithUV((float) x + var60, (float) y + var18 + var20, z + 0, var10, var14);
                tess.addVertexWithUV(x + 0, (float) (y + 0) + var20, z + 0, var10, var16);
                tess.addVertexWithUV(x + 0, (float) (y + 0) + var20, z + 1, var12, var16);
                tess.addVertexWithUV((float) x + var60, (float) y + var18 + var20, z + 1, var12, var14);
            }

            if (Block.FIRE.canBlockCatchFire(this.blockAccess, x + 1, y, z)) {
                tess.addVertexWithUV((float) (x + 1) - var60, (float) y + var18 + var20, z + 0, var10, var14);
                tess.addVertexWithUV(x + 1 - 0, (float) (y + 0) + var20, z + 0, var10, var16);
                tess.addVertexWithUV(x + 1 - 0, (float) (y + 0) + var20, z + 1, var12, var16);
                tess.addVertexWithUV((float) (x + 1) - var60, (float) y + var18 + var20, z + 1, var12, var14);
                tess.addVertexWithUV((float) (x + 1) - var60, (float) y + var18 + var20, z + 1, var12, var14);
                tess.addVertexWithUV(x + 1 - 0, (float) (y + 0) + var20, z + 1, var12, var16);
                tess.addVertexWithUV(x + 1 - 0, (float) (y + 0) + var20, z + 0, var10, var16);
                tess.addVertexWithUV((float) (x + 1) - var60, (float) y + var18 + var20, z + 0, var10, var14);
            }

            if (Block.FIRE.canBlockCatchFire(this.blockAccess, x, y, z - 1)) {
                tess.addVertexWithUV(x + 0, (float) y + var18 + var20, (float) z + var60, var12, var14);
                tess.addVertexWithUV(x + 0, (float) (y + 0) + var20, z + 0, var12, var16);
                tess.addVertexWithUV(x + 1, (float) (y + 0) + var20, z + 0, var10, var16);
                tess.addVertexWithUV(x + 1, (float) y + var18 + var20, (float) z + var60, var10, var14);
                tess.addVertexWithUV(x + 1, (float) y + var18 + var20, (float) z + var60, var10, var14);
                tess.addVertexWithUV(x + 1, (float) (y + 0) + var20, z + 0, var10, var16);
                tess.addVertexWithUV(x + 0, (float) (y + 0) + var20, z + 0, var12, var16);
                tess.addVertexWithUV(x + 0, (float) y + var18 + var20, (float) z + var60, var12, var14);
            }

            if (Block.FIRE.canBlockCatchFire(this.blockAccess, x, y, z + 1)) {
                tess.addVertexWithUV(x + 1, (float) y + var18 + var20, (float) (z + 1) - var60, var10, var14);
                tess.addVertexWithUV(x + 1, (float) (y + 0) + var20, z + 1 - 0, var10, var16);
                tess.addVertexWithUV(x + 0, (float) (y + 0) + var20, z + 1 - 0, var12, var16);
                tess.addVertexWithUV(x + 0, (float) y + var18 + var20, (float) (z + 1) - var60, var12, var14);
                tess.addVertexWithUV(x + 0, (float) y + var18 + var20, (float) (z + 1) - var60, var12, var14);
                tess.addVertexWithUV(x + 0, (float) (y + 0) + var20, z + 1 - 0, var12, var16);
                tess.addVertexWithUV(x + 1, (float) (y + 0) + var20, z + 1 - 0, var10, var16);
                tess.addVertexWithUV(x + 1, (float) y + var18 + var20, (float) (z + 1) - var60, var10, var14);
            }

            if (Block.FIRE.canBlockCatchFire(this.blockAccess, x, y + 1, z)) {
                double var63 = (double) x + 0.5D + 0.5D;
                double var65 = (double) x + 0.5D - 0.5D;
                double var67 = (double) z + 0.5D + 0.5D;
                double var69 = (double) z + 0.5D - 0.5D;
                double var71 = (double) x + 0.5D - 0.5D;
                double var73 = (double) x + 0.5D + 0.5D;
                double var75 = (double) z + 0.5D - 0.5D;
                double var35 = (double) z + 0.5D + 0.5D;
                var10 = (float) var8 / 256.0F;
                var12 = ((float) var8 + 15.99F) / 256.0F;
                var14 = (float) var9 / 256.0F;
                var16 = ((float) var9 + 15.99F) / 256.0F;
                ++y;
                var18 = -0.2F;
                if ((x + y + z & 1) == 0) {
                    tess.addVertexWithUV(var71, (float) y + var18, z + 0, var12, var14);
                    tess.addVertexWithUV(var63, y + 0, z + 0, var12, var16);
                    tess.addVertexWithUV(var63, y + 0, z + 1, var10, var16);
                    tess.addVertexWithUV(var71, (float) y + var18, z + 1, var10, var14);
                    var10 = (float) var8 / 256.0F;
                    var12 = ((float) var8 + 15.99F) / 256.0F;
                    var14 = (float) (var9 + 16) / 256.0F;
                    var16 = ((float) var9 + 15.99F + 16.0F) / 256.0F;
                    tess.addVertexWithUV(var73, (float) y + var18, z + 1, var12, var14);
                    tess.addVertexWithUV(var65, y + 0, z + 1, var12, var16);
                    tess.addVertexWithUV(var65, y + 0, z + 0, var10, var16);
                    tess.addVertexWithUV(var73, (float) y + var18, z + 0, var10, var14);
                } else {
                    tess.addVertexWithUV(x + 0, (float) y + var18, var35, var12, var14);
                    tess.addVertexWithUV(x + 0, y + 0, var69, var12, var16);
                    tess.addVertexWithUV(x + 1, y + 0, var69, var10, var16);
                    tess.addVertexWithUV(x + 1, (float) y + var18, var35, var10, var14);
                    var10 = (float) var8 / 256.0F;
                    var12 = ((float) var8 + 15.99F) / 256.0F;
                    var14 = (float) (var9 + 16) / 256.0F;
                    var16 = ((float) var9 + 15.99F + 16.0F) / 256.0F;
                    tess.addVertexWithUV(x + 1, (float) y + var18, var75, var12, var14);
                    tess.addVertexWithUV(x + 1, y + 0, var67, var12, var16);
                    tess.addVertexWithUV(x + 0, y + 0, var67, var10, var16);
                    tess.addVertexWithUV(x + 0, (float) y + var18, var75, var10, var14);
                }
            }
        } else {
            double var19 = (double) x + 0.5D + 0.2D;
            double var21 = (double) x + 0.5D - 0.2D;
            double var23 = (double) z + 0.5D + 0.2D;
            double var25 = (double) z + 0.5D - 0.2D;
            double var27 = (double) x + 0.5D - 0.3D;
            double var29 = (double) x + 0.5D + 0.3D;
            double var31 = (double) z + 0.5D - 0.3D;
            double var33 = (double) z + 0.5D + 0.3D;
            tess.addVertexWithUV(var27, (float) y + var18, z + 1, var12, var14);
            tess.addVertexWithUV(var19, y + 0, z + 1, var12, var16);
            tess.addVertexWithUV(var19, y + 0, z + 0, var10, var16);
            tess.addVertexWithUV(var27, (float) y + var18, z + 0, var10, var14);
            tess.addVertexWithUV(var29, (float) y + var18, z + 0, var12, var14);
            tess.addVertexWithUV(var21, y + 0, z + 0, var12, var16);
            tess.addVertexWithUV(var21, y + 0, z + 1, var10, var16);
            tess.addVertexWithUV(var29, (float) y + var18, z + 1, var10, var14);
            var10 = (float) var8 / 256.0F;
            var12 = ((float) var8 + 15.99F) / 256.0F;
            var14 = (float) (var9 + 16) / 256.0F;
            var16 = ((float) var9 + 15.99F + 16.0F) / 256.0F;
            tess.addVertexWithUV(x + 1, (float) y + var18, var33, var12, var14);
            tess.addVertexWithUV(x + 1, y + 0, var25, var12, var16);
            tess.addVertexWithUV(x + 0, y + 0, var25, var10, var16);
            tess.addVertexWithUV(x + 0, (float) y + var18, var33, var10, var14);
            tess.addVertexWithUV(x + 0, (float) y + var18, var31, var12, var14);
            tess.addVertexWithUV(x + 0, y + 0, var23, var12, var16);
            tess.addVertexWithUV(x + 1, y + 0, var23, var10, var16);
            tess.addVertexWithUV(x + 1, (float) y + var18, var31, var10, var14);
            var19 = (double) x + 0.5D - 0.5D;
            var21 = (double) x + 0.5D + 0.5D;
            var23 = (double) z + 0.5D - 0.5D;
            var25 = (double) z + 0.5D + 0.5D;
            var27 = (double) x + 0.5D - 0.4D;
            var29 = (double) x + 0.5D + 0.4D;
            var31 = (double) z + 0.5D - 0.4D;
            var33 = (double) z + 0.5D + 0.4D;
            tess.addVertexWithUV(var27, (float) y + var18, z + 0, var10, var14);
            tess.addVertexWithUV(var19, y + 0, z + 0, var10, var16);
            tess.addVertexWithUV(var19, y + 0, z + 1, var12, var16);
            tess.addVertexWithUV(var27, (float) y + var18, z + 1, var12, var14);
            tess.addVertexWithUV(var29, (float) y + var18, z + 1, var10, var14);
            tess.addVertexWithUV(var21, y + 0, z + 1, var10, var16);
            tess.addVertexWithUV(var21, y + 0, z + 0, var12, var16);
            tess.addVertexWithUV(var29, (float) y + var18, z + 0, var12, var14);
            var10 = (float) var8 / 256.0F;
            var12 = ((float) var8 + 15.99F) / 256.0F;
            var14 = (float) var9 / 256.0F;
            var16 = ((float) var9 + 15.99F) / 256.0F;
            tess.addVertexWithUV(x + 0, (float) y + var18, var33, var10, var14);
            tess.addVertexWithUV(x + 0, y + 0, var25, var10, var16);
            tess.addVertexWithUV(x + 1, y + 0, var25, var12, var16);
            tess.addVertexWithUV(x + 1, (float) y + var18, var33, var12, var14);
            tess.addVertexWithUV(x + 1, (float) y + var18, var31, var10, var14);
            tess.addVertexWithUV(x + 1, y + 0, var23, var10, var16);
            tess.addVertexWithUV(x + 0, y + 0, var23, var12, var16);
            tess.addVertexWithUV(x + 0, (float) y + var18, var31, var12, var14);
        }

        return true;
    }

    public boolean renderBlockRedstoneWire(Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        int metadata = this.blockAccess.getBlockMetadata(x, y, z);
        int var7 = block.getBlockTextureFromSideAndMetadata(1, metadata);
        if (this.overrideBlockTexture >= 0) {
            var7 = this.overrideBlockTexture;
        }

        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        float var9 = (float) metadata / 15.0F;
        float var10 = var9 * 0.6F + 0.4F;
        if (metadata == 0) {
            var10 = 0.3F;
        }

        float var11 = var9 * var9 * 0.7F - 0.5F;
        float var12 = var9 * var9 * 0.6F - 0.7F;
        if (var11 < 0.0F) {
            var11 = 0.0F;
        }

        if (var12 < 0.0F) {
            var12 = 0.0F;
        }

        tess.setColorOpaque_F(brightness * var10, brightness * var11, brightness * var12);
        int var13 = (var7 & 15) << 4;
        int var14 = var7 & 240;
        double var15 = (float) var13 / 256.0F;
        double var17 = ((float) var13 + 15.99F) / 256.0F;
        double var19 = (float) var14 / 256.0F;
        double var21 = ((float) var14 + 15.99F) / 256.0F;
        boolean var26 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x - 1, y, z, 1) || !this.blockAccess.isBlockNormalCube(x - 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x - 1, y - 1, z, -1);
        boolean var27 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x + 1, y, z, 3) || !this.blockAccess.isBlockNormalCube(x + 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x + 1, y - 1, z, -1);
        boolean var28 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y, z - 1, 2) || !this.blockAccess.isBlockNormalCube(x, y, z - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y - 1, z - 1, -1);
        boolean var29 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y, z + 1, 0) || !this.blockAccess.isBlockNormalCube(x, y, z + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y - 1, z + 1, -1);
        if (!this.blockAccess.isBlockNormalCube(x, y + 1, z)) {
            if (this.blockAccess.isBlockNormalCube(x - 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x - 1, y + 1, z, -1)) {
                var26 = true;
            }

            if (this.blockAccess.isBlockNormalCube(x + 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x + 1, y + 1, z, -1)) {
                var27 = true;
            }

            if (this.blockAccess.isBlockNormalCube(x, y, z - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y + 1, z - 1, -1)) {
                var28 = true;
            }

            if (this.blockAccess.isBlockNormalCube(x, y, z + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y + 1, z + 1, -1)) {
                var29 = true;
            }
        }

        float var31 = (float) (x + 0);
        float var32 = (float) (x + 1);
        float var33 = (float) (z + 0);
        float var34 = (float) (z + 1);
        byte var35 = 0;
        if ((var26 || var27) && !var28 && !var29) {
            var35 = 1;
        }

        if ((var28 || var29) && !var27 && !var26) {
            var35 = 2;
        }

        if (var35 != 0) {
            var15 = (float) (var13 + 16) / 256.0F;
            var17 = ((float) (var13 + 16) + 15.99F) / 256.0F;
            var19 = (float) var14 / 256.0F;
            var21 = ((float) var14 + 15.99F) / 256.0F;
        }

        if (var35 == 0) {
            if (var27 || var28 || var29 || var26) {
                if (!var26) {
                    var31 += 0.3125F;
                }

                if (!var26) {
                    var15 += 0.01953125D;
                }

                if (!var27) {
                    var32 -= 0.3125F;
                }

                if (!var27) {
                    var17 -= 0.01953125D;
                }

                if (!var28) {
                    var33 += 0.3125F;
                }

                if (!var28) {
                    var19 += 0.01953125D;
                }

                if (!var29) {
                    var34 -= 0.3125F;
                }

                if (!var29) {
                    var21 -= 0.01953125D;
                }
            }

            tess.addVertexWithUV(var32, (float) y + 0.015625F, var34, var17, var21);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var33, var17, var19);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var33, var15, var19);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var34, var15, var21);
            tess.setColorOpaque_F(brightness, brightness, brightness);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var34, var17, var21 + 0.0625D);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var33, var17, var19 + 0.0625D);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var33, var15, var19 + 0.0625D);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var34, var15, var21 + 0.0625D);
        } else if (var35 == 1) {
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var34, var17, var21);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var33, var17, var19);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var33, var15, var19);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var34, var15, var21);
            tess.setColorOpaque_F(brightness, brightness, brightness);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var34, var17, var21 + 0.0625D);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var33, var17, var19 + 0.0625D);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var33, var15, var19 + 0.0625D);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var34, var15, var21 + 0.0625D);
        } else if (var35 == 2) {
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var34, var17, var21);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var33, var15, var21);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var33, var15, var19);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var34, var17, var19);
            tess.setColorOpaque_F(brightness, brightness, brightness);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var34, var17, var21 + 0.0625D);
            tess.addVertexWithUV(var32, (float) y + 0.015625F, var33, var15, var21 + 0.0625D);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var33, var15, var19 + 0.0625D);
            tess.addVertexWithUV(var31, (float) y + 0.015625F, var34, var17, var19 + 0.0625D);
        }

        if (!this.blockAccess.isBlockNormalCube(x, y + 1, z)) {
            var15 = (float) (var13 + 16) / 256.0F;
            var17 = ((float) (var13 + 16) + 15.99F) / 256.0F;
            var19 = (float) var14 / 256.0F;
            var21 = ((float) var14 + 15.99F) / 256.0F;
            if (this.blockAccess.isBlockNormalCube(x - 1, y, z) && this.blockAccess.getBlockId(x - 1, y + 1, z) == Block.REDSTONE_WIRE.blockID) {
                tess.setColorOpaque_F(brightness * var10, brightness * var11, brightness * var12);
                tess.addVertexWithUV((float) x + 0.015625F, (float) (y + 1) + 0.021875F, z + 1, var17, var19);
                tess.addVertexWithUV((float) x + 0.015625F, y + 0, z + 1, var15, var19);
                tess.addVertexWithUV((float) x + 0.015625F, y + 0, z + 0, var15, var21);
                tess.addVertexWithUV((float) x + 0.015625F, (float) (y + 1) + 0.021875F, z + 0, var17, var21);
                tess.setColorOpaque_F(brightness, brightness, brightness);
                tess.addVertexWithUV((float) x + 0.015625F, (float) (y + 1) + 0.021875F, z + 1, var17, var19 + 0.0625D);
                tess.addVertexWithUV((float) x + 0.015625F, y + 0, z + 1, var15, var19 + 0.0625D);
                tess.addVertexWithUV((float) x + 0.015625F, y + 0, z + 0, var15, var21 + 0.0625D);
                tess.addVertexWithUV((float) x + 0.015625F, (float) (y + 1) + 0.021875F, z + 0, var17, var21 + 0.0625D);
            }

            if (this.blockAccess.isBlockNormalCube(x + 1, y, z) && this.blockAccess.getBlockId(x + 1, y + 1, z) == Block.REDSTONE_WIRE.blockID) {
                tess.setColorOpaque_F(brightness * var10, brightness * var11, brightness * var12);
                tess.addVertexWithUV((float) (x + 1) - 0.015625F, y + 0, z + 1, var15, var21);
                tess.addVertexWithUV((float) (x + 1) - 0.015625F, (float) (y + 1) + 0.021875F, z + 1, var17, var21);
                tess.addVertexWithUV((float) (x + 1) - 0.015625F, (float) (y + 1) + 0.021875F, z + 0, var17, var19);
                tess.addVertexWithUV((float) (x + 1) - 0.015625F, y + 0, z + 0, var15, var19);
                tess.setColorOpaque_F(brightness, brightness, brightness);
                tess.addVertexWithUV((float) (x + 1) - 0.015625F, y + 0, z + 1, var15, var21 + 0.0625D);
                tess.addVertexWithUV((float) (x + 1) - 0.015625F, (float) (y + 1) + 0.021875F, z + 1, var17, var21 + 0.0625D);
                tess.addVertexWithUV((float) (x + 1) - 0.015625F, (float) (y + 1) + 0.021875F, z + 0, var17, var19 + 0.0625D);
                tess.addVertexWithUV((float) (x + 1) - 0.015625F, y + 0, z + 0, var15, var19 + 0.0625D);
            }

            if (this.blockAccess.isBlockNormalCube(x, y, z - 1) && this.blockAccess.getBlockId(x, y + 1, z - 1) == Block.REDSTONE_WIRE.blockID) {
                tess.setColorOpaque_F(brightness * var10, brightness * var11, brightness * var12);
                tess.addVertexWithUV(x + 1, y + 0, (float) z + 0.015625F, var15, var21);
                tess.addVertexWithUV(x + 1, (float) (y + 1) + 0.021875F, (float) z + 0.015625F, var17, var21);
                tess.addVertexWithUV(x + 0, (float) (y + 1) + 0.021875F, (float) z + 0.015625F, var17, var19);
                tess.addVertexWithUV(x + 0, y + 0, (float) z + 0.015625F, var15, var19);
                tess.setColorOpaque_F(brightness, brightness, brightness);
                tess.addVertexWithUV(x + 1, y + 0, (float) z + 0.015625F, var15, var21 + 0.0625D);
                tess.addVertexWithUV(x + 1, (float) (y + 1) + 0.021875F, (float) z + 0.015625F, var17, var21 + 0.0625D);
                tess.addVertexWithUV(x + 0, (float) (y + 1) + 0.021875F, (float) z + 0.015625F, var17, var19 + 0.0625D);
                tess.addVertexWithUV(x + 0, y + 0, (float) z + 0.015625F, var15, var19 + 0.0625D);
            }

            if (this.blockAccess.isBlockNormalCube(x, y, z + 1) && this.blockAccess.getBlockId(x, y + 1, z + 1) == Block.REDSTONE_WIRE.blockID) {
                tess.setColorOpaque_F(brightness * var10, brightness * var11, brightness * var12);
                tess.addVertexWithUV(x + 1, (float) (y + 1) + 0.021875F, (float) (z + 1) - 0.015625F, var17, var19);
                tess.addVertexWithUV(x + 1, y + 0, (float) (z + 1) - 0.015625F, var15, var19);
                tess.addVertexWithUV(x + 0, y + 0, (float) (z + 1) - 0.015625F, var15, var21);
                tess.addVertexWithUV(x + 0, (float) (y + 1) + 0.021875F, (float) (z + 1) - 0.015625F, var17, var21);
                tess.setColorOpaque_F(brightness, brightness, brightness);
                tess.addVertexWithUV(x + 1, (float) (y + 1) + 0.021875F, (float) (z + 1) - 0.015625F, var17, var19 + 0.0625D);
                tess.addVertexWithUV(x + 1, y + 0, (float) (z + 1) - 0.015625F, var15, var19 + 0.0625D);
                tess.addVertexWithUV(x + 0, y + 0, (float) (z + 1) - 0.015625F, var15, var21 + 0.0625D);
                tess.addVertexWithUV(x + 0, (float) (y + 1) + 0.021875F, (float) (z + 1) - 0.015625F, var17, var21 + 0.0625D);
            }
        }

        return true;
    }

    public boolean renderBlockMinecartTrack(BlockRail block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        int blockMetadata = this.blockAccess.getBlockMetadata(x, y, z);
        int var7 = block.getBlockTextureFromSideAndMetadata(0, blockMetadata);
        if (this.overrideBlockTexture >= 0) {
            var7 = this.overrideBlockTexture;
        }

        if (block.getIsPowered()) {
            blockMetadata &= 7;
        }

        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        tess.setColorOpaque_F(brightness, brightness, brightness);
        int var9 = (var7 & 15) << 4;
        int var10 = var7 & 240;
        double var11 = (float) var9 / 256.0F;
        double var13 = ((float) var9 + 15.99F) / 256.0F;
        double var15 = (float) var10 / 256.0F;
        double var17 = ((float) var10 + 15.99F) / 256.0F;
        float var19 = 0.0625F;
        float var20 = (float) (x + 1);
        float var21 = (float) (x + 1);
        float var22 = (float) (x + 0);
        float var23 = (float) (x + 0);
        float var24 = (float) (z + 0);
        float var25 = (float) (z + 1);
        float var26 = (float) (z + 1);
        float var27 = (float) (z + 0);
        float var28 = (float) y + var19;
        float var29 = (float) y + var19;
        float var30 = (float) y + var19;
        float var31 = (float) y + var19;
        if (blockMetadata != 1 && blockMetadata != 2 && blockMetadata != 3 && blockMetadata != 7) {
            if (blockMetadata == 8) {
                var20 = var21 = (float) (x + 0);
                var22 = var23 = (float) (x + 1);
                var24 = var27 = (float) (z + 1);
                var25 = var26 = (float) (z + 0);
            } else if (blockMetadata == 9) {
                var20 = var23 = (float) (x + 0);
                var21 = var22 = (float) (x + 1);
                var24 = var25 = (float) (z + 0);
                var26 = var27 = (float) (z + 1);
            }
        } else {
            var20 = var23 = (float) (x + 1);
            var21 = var22 = (float) (x + 0);
            var24 = var25 = (float) (z + 1);
            var26 = var27 = (float) (z + 0);
        }

        if (blockMetadata != 2 && blockMetadata != 4) {
            if (blockMetadata == 3 || blockMetadata == 5) {
                ++var29;
                ++var30;
            }
        } else {
            ++var28;
            ++var31;
        }

        tess.addVertexWithUV(var20, var28, var24, var13, var15);
        tess.addVertexWithUV(var21, var29, var25, var13, var17);
        tess.addVertexWithUV(var22, var30, var26, var11, var17);
        tess.addVertexWithUV(var23, var31, var27, var11, var15);
        tess.addVertexWithUV(var23, var31, var27, var11, var15);
        tess.addVertexWithUV(var22, var30, var26, var11, var17);
        tess.addVertexWithUV(var21, var29, var25, var13, var17);
        tess.addVertexWithUV(var20, var28, var24, var13, var15);
        return true;
    }

    public boolean renderBlockLadder(Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        int var6 = block.getBlockTextureFromSide(0);
        if (this.overrideBlockTexture >= 0) {
            var6 = this.overrideBlockTexture;
        }

        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        tess.setColorOpaque_F(brightness, brightness, brightness);
        int var8 = (var6 & 15) << 4;
        int var9 = var6 & 240;
        double var10 = (float) var8 / 256.0F;
        double var12 = ((float) var8 + 15.99F) / 256.0F;
        double var14 = (float) var9 / 256.0F;
        double var16 = ((float) var9 + 15.99F) / 256.0F;
        int blockMetadata = this.blockAccess.getBlockMetadata(x, y, z);
        float var19 = 0.0F;
        float var20 = 0.05F;
        if (blockMetadata == 5) {
            tess.addVertexWithUV((float) x + var20, (float) (y + 1) + var19, (float) (z + 1) + var19, var10, var14);
            tess.addVertexWithUV((float) x + var20, (float) (y + 0) - var19, (float) (z + 1) + var19, var10, var16);
            tess.addVertexWithUV((float) x + var20, (float) (y + 0) - var19, (float) (z + 0) - var19, var12, var16);
            tess.addVertexWithUV((float) x + var20, (float) (y + 1) + var19, (float) (z + 0) - var19, var12, var14);
        }

        if (blockMetadata == 4) {
            tess.addVertexWithUV((float) (x + 1) - var20, (float) (y + 0) - var19, (float) (z + 1) + var19, var12, var16);
            tess.addVertexWithUV((float) (x + 1) - var20, (float) (y + 1) + var19, (float) (z + 1) + var19, var12, var14);
            tess.addVertexWithUV((float) (x + 1) - var20, (float) (y + 1) + var19, (float) (z + 0) - var19, var10, var14);
            tess.addVertexWithUV((float) (x + 1) - var20, (float) (y + 0) - var19, (float) (z + 0) - var19, var10, var16);
        }

        if (blockMetadata == 3) {
            tess.addVertexWithUV((float) (x + 1) + var19, (float) (y + 0) - var19, (float) z + var20, var12, var16);
            tess.addVertexWithUV((float) (x + 1) + var19, (float) (y + 1) + var19, (float) z + var20, var12, var14);
            tess.addVertexWithUV((float) (x + 0) - var19, (float) (y + 1) + var19, (float) z + var20, var10, var14);
            tess.addVertexWithUV((float) (x + 0) - var19, (float) (y + 0) - var19, (float) z + var20, var10, var16);
        }

        if (blockMetadata == 2) {
            tess.addVertexWithUV((float) (x + 1) + var19, (float) (y + 1) + var19, (float) (z + 1) - var20, var10, var14);
            tess.addVertexWithUV((float) (x + 1) + var19, (float) (y + 0) - var19, (float) (z + 1) - var20, var10, var16);
            tess.addVertexWithUV((float) (x + 0) - var19, (float) (y + 0) - var19, (float) (z + 1) - var20, var12, var16);
            tess.addVertexWithUV((float) (x + 0) - var19, (float) (y + 1) + var19, (float) (z + 1) - var20, var12, var14);
        }

        return true;
    }

    public boolean renderBlockReed(Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        int colorMultiplier = block.colorMultiplier(this.blockAccess, x, y, z);
        float r = (float) (colorMultiplier >> 16 & 255) / 255.0F;
        float g = (float) (colorMultiplier >> 8 & 255) / 255.0F;
        float b = (float) (colorMultiplier & 255) / 255.0F;

        if (EntityRenderer.field_28135_a) {
            float tmpR = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float tmpG = (r * 30.0F + g * 70.0F) / 100.0F;
            float tmpB = (r * 30.0F + b * 70.0F) / 100.0F;

            r = tmpR;
            g = tmpG;
            b = tmpB;
        }

        tess.setColorOpaque_F(brightness * r, brightness * g, brightness * b);
        double var19 = x;
        double var20 = y;
        double var15 = z;
        if (block == Block.TALLGRASS) {
            long var17 = (long) (x * 3129871) ^ (long) z * 116129781L ^ (long) y;
            var17 = var17 * var17 * 42317861L + var17 * 11L;
            var19 += ((double) ((float) (var17 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            var20 += ((double) ((float) (var17 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            var15 += ((double) ((float) (var17 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        }

        this.renderCrossedSquares(block, this.blockAccess.getBlockMetadata(x, y, z), var19, var20, var15);
        return true;
    }

    public boolean renderBlockCrops(Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        float brightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        tess.setColorOpaque_F(brightness, brightness, brightness);
        this.func_1245_b(block, this.blockAccess.getBlockMetadata(x, y, z), x, (float) y - 0.0625F, z);
        return true;
    }

    public void renderTorchAtAngle(Block block, double var2, double var4, double var6, double var8, double var10) {
        Tessellator tess = Tessellator.INSTANCE;
        int var13 = block.getBlockTextureFromSide(0);
        if (this.overrideBlockTexture >= 0) {
            var13 = this.overrideBlockTexture;
        }

        int var14 = (var13 & 15) << 4;
        int var15 = var13 & 240;
        float var16 = (float) var14 / 256.0F;
        float var17 = ((float) var14 + 15.99F) / 256.0F;
        float var18 = (float) var15 / 256.0F;
        float var19 = ((float) var15 + 15.99F) / 256.0F;
        double var20 = (double) var16 + 0.02734375D;
        double var22 = (double) var18 + 0.0234375D;
        double var24 = (double) var16 + 0.03515625D;
        double var26 = (double) var18 + 0.03125D;
        var2 = var2 + 0.5D;
        var6 = var6 + 0.5D;
        double var28 = var2 - 0.5D;
        double var30 = var2 + 0.5D;
        double var32 = var6 - 0.5D;
        double var34 = var6 + 0.5D;
        double var36 = 0.0625D;
        double var38 = 0.625D;
        tess.addVertexWithUV(var2 + var8 * (1.0D - var38) - var36, var4 + var38, var6 + var10 * (1.0D - var38) - var36, var20, var22);
        tess.addVertexWithUV(var2 + var8 * (1.0D - var38) - var36, var4 + var38, var6 + var10 * (1.0D - var38) + var36, var20, var26);
        tess.addVertexWithUV(var2 + var8 * (1.0D - var38) + var36, var4 + var38, var6 + var10 * (1.0D - var38) + var36, var24, var26);
        tess.addVertexWithUV(var2 + var8 * (1.0D - var38) + var36, var4 + var38, var6 + var10 * (1.0D - var38) - var36, var24, var22);
        tess.addVertexWithUV(var2 - var36, var4 + 1.0D, var32, var16, var18);
        tess.addVertexWithUV(var2 - var36 + var8, var4 + 0.0D, var32 + var10, var16, var19);
        tess.addVertexWithUV(var2 - var36 + var8, var4 + 0.0D, var34 + var10, var17, var19);
        tess.addVertexWithUV(var2 - var36, var4 + 1.0D, var34, var17, var18);
        tess.addVertexWithUV(var2 + var36, var4 + 1.0D, var34, var16, var18);
        tess.addVertexWithUV(var2 + var8 + var36, var4 + 0.0D, var34 + var10, var16, var19);
        tess.addVertexWithUV(var2 + var8 + var36, var4 + 0.0D, var32 + var10, var17, var19);
        tess.addVertexWithUV(var2 + var36, var4 + 1.0D, var32, var17, var18);
        tess.addVertexWithUV(var28, var4 + 1.0D, var6 + var36, var16, var18);
        tess.addVertexWithUV(var28 + var8, var4 + 0.0D, var6 + var36 + var10, var16, var19);
        tess.addVertexWithUV(var30 + var8, var4 + 0.0D, var6 + var36 + var10, var17, var19);
        tess.addVertexWithUV(var30, var4 + 1.0D, var6 + var36, var17, var18);
        tess.addVertexWithUV(var30, var4 + 1.0D, var6 - var36, var16, var18);
        tess.addVertexWithUV(var30 + var8, var4 + 0.0D, var6 - var36 + var10, var16, var19);
        tess.addVertexWithUV(var28 + var8, var4 + 0.0D, var6 - var36 + var10, var17, var19);
        tess.addVertexWithUV(var28, var4 + 1.0D, var6 - var36, var17, var18);
    }

    public void renderCrossedSquares(Block block, int var2, double var3, double var5, double var7) {
        Tessellator tess = Tessellator.INSTANCE;
        int var10 = block.getBlockTextureFromSideAndMetadata(0, var2);
        if (this.overrideBlockTexture >= 0) {
            var10 = this.overrideBlockTexture;
        }

        int var11 = (var10 & 15) << 4;
        int var12 = var10 & 240;
        double var13 = (float) var11 / 256.0F;
        double var15 = ((float) var11 + 15.99F) / 256.0F;
        double var17 = (float) var12 / 256.0F;
        double var19 = ((float) var12 + 15.99F) / 256.0F;
        double var21 = var3 + 0.5D - 0.44999998807907104D;
        double var23 = var3 + 0.5D + 0.44999998807907104D;
        double var25 = var7 + 0.5D - 0.44999998807907104D;
        double var27 = var7 + 0.5D + 0.44999998807907104D;
        tess.addVertexWithUV(var21, var5 + 1.0D, var25, var13, var17);
        tess.addVertexWithUV(var21, var5 + 0.0D, var25, var13, var19);
        tess.addVertexWithUV(var23, var5 + 0.0D, var27, var15, var19);
        tess.addVertexWithUV(var23, var5 + 1.0D, var27, var15, var17);
        tess.addVertexWithUV(var23, var5 + 1.0D, var27, var13, var17);
        tess.addVertexWithUV(var23, var5 + 0.0D, var27, var13, var19);
        tess.addVertexWithUV(var21, var5 + 0.0D, var25, var15, var19);
        tess.addVertexWithUV(var21, var5 + 1.0D, var25, var15, var17);
        tess.addVertexWithUV(var21, var5 + 1.0D, var27, var13, var17);
        tess.addVertexWithUV(var21, var5 + 0.0D, var27, var13, var19);
        tess.addVertexWithUV(var23, var5 + 0.0D, var25, var15, var19);
        tess.addVertexWithUV(var23, var5 + 1.0D, var25, var15, var17);
        tess.addVertexWithUV(var23, var5 + 1.0D, var25, var13, var17);
        tess.addVertexWithUV(var23, var5 + 0.0D, var25, var13, var19);
        tess.addVertexWithUV(var21, var5 + 0.0D, var27, var15, var19);
        tess.addVertexWithUV(var21, var5 + 1.0D, var27, var15, var17);
    }

    public void func_1245_b(Block block, int var2, double var3, double var5, double var7) {
        Tessellator tess = Tessellator.INSTANCE;
        int var10 = block.getBlockTextureFromSideAndMetadata(0, var2);
        if (this.overrideBlockTexture >= 0) {
            var10 = this.overrideBlockTexture;
        }

        int var11 = (var10 & 15) << 4;
        int var12 = var10 & 240;
        double var13 = (float) var11 / 256.0F;
        double var15 = ((float) var11 + 15.99F) / 256.0F;
        double var17 = (float) var12 / 256.0F;
        double var19 = ((float) var12 + 15.99F) / 256.0F;
        double var21 = var3 + 0.5D - 0.25D;
        double var23 = var3 + 0.5D + 0.25D;
        double var25 = var7 + 0.5D - 0.5D;
        double var27 = var7 + 0.5D + 0.5D;
        tess.addVertexWithUV(var21, var5 + 1.0D, var25, var13, var17);
        tess.addVertexWithUV(var21, var5 + 0.0D, var25, var13, var19);
        tess.addVertexWithUV(var21, var5 + 0.0D, var27, var15, var19);
        tess.addVertexWithUV(var21, var5 + 1.0D, var27, var15, var17);
        tess.addVertexWithUV(var21, var5 + 1.0D, var27, var13, var17);
        tess.addVertexWithUV(var21, var5 + 0.0D, var27, var13, var19);
        tess.addVertexWithUV(var21, var5 + 0.0D, var25, var15, var19);
        tess.addVertexWithUV(var21, var5 + 1.0D, var25, var15, var17);
        tess.addVertexWithUV(var23, var5 + 1.0D, var27, var13, var17);
        tess.addVertexWithUV(var23, var5 + 0.0D, var27, var13, var19);
        tess.addVertexWithUV(var23, var5 + 0.0D, var25, var15, var19);
        tess.addVertexWithUV(var23, var5 + 1.0D, var25, var15, var17);
        tess.addVertexWithUV(var23, var5 + 1.0D, var25, var13, var17);
        tess.addVertexWithUV(var23, var5 + 0.0D, var25, var13, var19);
        tess.addVertexWithUV(var23, var5 + 0.0D, var27, var15, var19);
        tess.addVertexWithUV(var23, var5 + 1.0D, var27, var15, var17);
        var21 = var3 + 0.5D - 0.5D;
        var23 = var3 + 0.5D + 0.5D;
        var25 = var7 + 0.5D - 0.25D;
        var27 = var7 + 0.5D + 0.25D;
        tess.addVertexWithUV(var21, var5 + 1.0D, var25, var13, var17);
        tess.addVertexWithUV(var21, var5 + 0.0D, var25, var13, var19);
        tess.addVertexWithUV(var23, var5 + 0.0D, var25, var15, var19);
        tess.addVertexWithUV(var23, var5 + 1.0D, var25, var15, var17);
        tess.addVertexWithUV(var23, var5 + 1.0D, var25, var13, var17);
        tess.addVertexWithUV(var23, var5 + 0.0D, var25, var13, var19);
        tess.addVertexWithUV(var21, var5 + 0.0D, var25, var15, var19);
        tess.addVertexWithUV(var21, var5 + 1.0D, var25, var15, var17);
        tess.addVertexWithUV(var23, var5 + 1.0D, var27, var13, var17);
        tess.addVertexWithUV(var23, var5 + 0.0D, var27, var13, var19);
        tess.addVertexWithUV(var21, var5 + 0.0D, var27, var15, var19);
        tess.addVertexWithUV(var21, var5 + 1.0D, var27, var15, var17);
        tess.addVertexWithUV(var21, var5 + 1.0D, var27, var13, var17);
        tess.addVertexWithUV(var21, var5 + 0.0D, var27, var13, var19);
        tess.addVertexWithUV(var23, var5 + 0.0D, var27, var15, var19);
        tess.addVertexWithUV(var23, var5 + 1.0D, var27, var15, var17);
    }

    public boolean renderBlockFluids(Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        int colorMultiplier = block.colorMultiplier(this.blockAccess, x, y, z);

        float r = (float) (colorMultiplier >> 16 & 255) / 255.0F;
        float g = (float) (colorMultiplier >> 8 & 255) / 255.0F;
        float b = (float) (colorMultiplier & 255) / 255.0F;

        boolean var10 = block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1);
        boolean var11 = block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0);
        boolean[] var12 = new boolean[]{
                block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2),
                block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3),
                block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4),
                block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)
        };

        if (!var10 && !var11 && !var12[0] && !var12[1] && !var12[2] && !var12[3])
            return false;

        boolean var13 = false;
        float var14 = 0.5F;
        float var15 = 1.0F;
        float var16 = 0.8F;
        float var17 = 0.6F;
        double var18 = 0.0D;
        double var20 = 1.0D;
        Material var22 = block.blockMaterial;
        int var23 = this.blockAccess.getBlockMetadata(x, y, z);
        float var24 = this.func_1224_a(x, y, z, var22);
        float var25 = this.func_1224_a(x, y, z + 1, var22);
        float var26 = this.func_1224_a(x + 1, y, z + 1, var22);
        float var27 = this.func_1224_a(x + 1, y, z, var22);
        if (this.renderAllFaces || var10) {
            var13 = true;
            int var28 = block.getBlockTextureFromSideAndMetadata(1, var23);
            float rad = (float) BlockFluid.func_293_a(this.blockAccess, x, y, z, var22);
            if (rad > -999.0F) {
                var28 = block.getBlockTextureFromSideAndMetadata(2, var23);
            }

            int var30 = (var28 & 15) << 4;
            int var31 = var28 & 240;
            double var32 = ((double) var30 + 8.0D) / 256.0D;
            double var34 = ((double) var31 + 8.0D) / 256.0D;
            if (rad < -999.0F) {
                rad = 0.0F;
            } else {
                var32 = (float) (var30 + 16) / 256.0F;
                var34 = (float) (var31 + 16) / 256.0F;
            }

            float var36 = MathHelper.sin(rad) * 8.0F / 256.0F;
            float var37 = MathHelper.cos(rad) * 8.0F / 256.0F;
            float var38 = block.getBlockBrightness(this.blockAccess, x, y, z);
            tess.setColorOpaque_F(var15 * var38 * r, var15 * var38 * g, var15 * var38 * b);
            tess.addVertexWithUV(x + 0, (float) y + var24, z + 0, var32 - (double) var37 - (double) var36, var34 - (double) var37 + (double) var36);
            tess.addVertexWithUV(x + 0, (float) y + var25, z + 1, var32 - (double) var37 + (double) var36, var34 + (double) var37 + (double) var36);
            tess.addVertexWithUV(x + 1, (float) y + var26, z + 1, var32 + (double) var37 + (double) var36, var34 + (double) var37 - (double) var36);
            tess.addVertexWithUV(x + 1, (float) y + var27, z + 0, var32 + (double) var37 - (double) var36, var34 - (double) var37 - (double) var36);
        }

        if (this.renderAllFaces || var11) {
            float var52 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
            tess.setColorOpaque_F(var14 * var52, var14 * var52, var14 * var52);
            this.renderBottomFace(block, x, y, z, block.getBlockTextureFromSide(0));
            var13 = true;
        }

        for (int var53 = 0; var53 < 4; ++var53) {
            int var54 = x;
            int var55 = z;
            if (var53 == 0) {
                var55 = z - 1;
            }

            if (var53 == 1) {
                ++var55;
            }

            if (var53 == 2) {
                var54 = x - 1;
            }

            if (var53 == 3) {
                ++var54;
            }

            int var56 = block.getBlockTextureFromSideAndMetadata(var53 + 2, var23);
            int var33 = (var56 & 15) << 4;
            int var57 = var56 & 240;
            if (this.renderAllFaces || var12[var53]) {
                float var35;
                float var39;
                float var40;
                float var58;
                float var59;
                float var60;
                if (var53 == 0) {
                    var35 = var24;
                    var58 = var27;
                    var59 = (float) x;
                    var39 = (float) (x + 1);
                    var60 = (float) z;
                    var40 = (float) z;
                } else if (var53 == 1) {
                    var35 = var26;
                    var58 = var25;
                    var59 = (float) (x + 1);
                    var39 = (float) x;
                    var60 = (float) (z + 1);
                    var40 = (float) (z + 1);
                } else if (var53 == 2) {
                    var35 = var25;
                    var58 = var24;
                    var59 = (float) x;
                    var39 = (float) x;
                    var60 = (float) (z + 1);
                    var40 = (float) z;
                } else {
                    var35 = var27;
                    var58 = var26;
                    var59 = (float) (x + 1);
                    var39 = (float) (x + 1);
                    var60 = (float) z;
                    var40 = (float) (z + 1);
                }

                var13 = true;
                double var41 = (float) (var33 + 0) / 256.0F;
                double var43 = ((double) (var33 + 16) - 0.01D) / 256.0D;
                double var45 = ((float) var57 + (1.0F - var35) * 16.0F) / 256.0F;
                double var47 = ((float) var57 + (1.0F - var58) * 16.0F) / 256.0F;
                double var49 = ((double) (var57 + 16) - 0.01D) / 256.0D;
                float var51 = block.getBlockBrightness(this.blockAccess, var54, y, var55);
                if (var53 < 2) {
                    var51 = var51 * var16;
                } else {
                    var51 = var51 * var17;
                }

                tess.setColorOpaque_F(var15 * var51 * r, var15 * var51 * g, var15 * var51 * b);
                tess.addVertexWithUV(var59, (float) y + var35, var60, var41, var45);
                tess.addVertexWithUV(var39, (float) y + var58, var40, var43, var47);
                tess.addVertexWithUV(var39, y + 0, var40, var43, var49);
                tess.addVertexWithUV(var59, y + 0, var60, var41, var49);
            }
        }

        block.minY = var18;
        block.maxY = var20;
        return var13;

    }

    private float func_1224_a(int var1, int var2, int var3, Material material1) {
        int var5 = 0;
        float var6 = 0.0F;

        for (int var7 = 0; var7 < 4; ++var7) {
            int var8 = var1 - (var7 & 1);
            int var10 = var3 - (var7 >> 1 & 1);
            if (this.blockAccess.getBlockMaterial(var8, var2 + 1, var10) == material1) {
                return 1.0F;
            }

            Material material = this.blockAccess.getBlockMaterial(var8, var2, var10);
            if (material != material1) {
                if (!material.isSolid()) {
                    ++var6;
                    ++var5;
                }
            } else {
                int var12 = this.blockAccess.getBlockMetadata(var8, var2, var10);
                if (var12 >= 8 || var12 == 0) {
                    var6 += BlockFluid.getPercentAir(var12) * 10.0F;
                    var5 += 10;
                }

                var6 += BlockFluid.getPercentAir(var12);
                ++var5;
            }
        }

        return 1.0F - var6 / (float) var5;
    }

    public void renderBlockFallingSand(Block block, World world, int x, int y, int z) {
        float var6 = 0.5F;
        float var7 = 1.0F;
        float var8 = 0.8F;
        float var9 = 0.6F;
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        float var11 = block.getBlockBrightness(world, x, y, z);
        float var12 = block.getBlockBrightness(world, x, y - 1, z);
        if (var12 < var11) {
            var12 = var11;
        }

        tess.setColorOpaque_F(var6 * var12, var6 * var12, var6 * var12);
        this.renderBottomFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(0));
        var12 = block.getBlockBrightness(world, x, y + 1, z);
        if (var12 < var11) {
            var12 = var11;
        }

        tess.setColorOpaque_F(var7 * var12, var7 * var12, var7 * var12);
        this.renderTopFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(1));
        var12 = block.getBlockBrightness(world, x, y, z - 1);
        if (var12 < var11) {
            var12 = var11;
        }

        tess.setColorOpaque_F(var8 * var12, var8 * var12, var8 * var12);
        this.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(2));
        var12 = block.getBlockBrightness(world, x, y, z + 1);
        if (var12 < var11) {
            var12 = var11;
        }

        tess.setColorOpaque_F(var8 * var12, var8 * var12, var8 * var12);
        this.renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(3));
        var12 = block.getBlockBrightness(world, x - 1, y, z);
        if (var12 < var11) {
            var12 = var11;
        }

        tess.setColorOpaque_F(var9 * var12, var9 * var12, var9 * var12);
        this.renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(4));
        var12 = block.getBlockBrightness(world, x + 1, y, z);
        if (var12 < var11) {
            var12 = var11;
        }

        tess.setColorOpaque_F(var9 * var12, var9 * var12, var9 * var12);
        this.renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(5));
        tess.draw();
    }

    public boolean renderStandardBlock(Block block, int x, int y, int z) {
        int colorMul = block.colorMultiplier(this.blockAccess, x, y, z);
        float r = (float) (colorMul >> 16 & 255) / 255.0F;
        float g = (float) (colorMul >> 8 & 255) / 255.0F;
        float b = (float) (colorMul & 255) / 255.0F;
        if (EntityRenderer.field_28135_a) {
            float tmpR = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float tmpG = (r * 30.0F + g * 70.0F) / 100.0F;
            float tmpB = (r * 30.0F + b * 70.0F) / 100.0F;
            r = tmpR;
            g = tmpG;
            b = tmpB;
        }

        return Minecraft.isAmbientOcclusionEnabled()
                ? this.renderStandardBlockWithAmbientOcclusion(block, x, y, z, r, g, b)
                : this.renderStandardBlockWithColorMultiplier(block, x, y, z, r, g, b);
    }

    public boolean renderStandardBlockWithAmbientOcclusion(Block block, int x, int y, int z, float r, float g, float b) {
        this.enableAO = true;
        boolean result = false;
        float topLeftMul = this.lightValueOwn;
        float botLeftMul = this.lightValueOwn;
        float botRightMul = this.lightValueOwn;
        float topRightMul = this.lightValueOwn;
        boolean var13 = true;
        boolean var14 = true;
        boolean var15 = true;
        boolean var16 = true;
        boolean var17 = true;
        boolean var18 = true;
        this.lightValueOwn = block.getBlockBrightness(this.blockAccess, x, y, z);
        this.aoLightValueXNeg = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
        this.aoLightValueYNeg = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
        this.aoLightValueZNeg = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
        this.aoLightValueXPos = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
        this.aoLightValueYPos = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
        this.aoLightValueZPos = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
        this.field_22338_U = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x + 1, y + 1, z)];
        this.field_22359_ac = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x + 1, y - 1, z)];
        this.field_22334_Y = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x + 1, y, z + 1)];
        this.field_22363_aa = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x + 1, y, z - 1)];
        this.field_22337_V = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x - 1, y + 1, z)];
        this.field_22357_ad = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x - 1, y - 1, z)];
        this.field_22335_X = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x - 1, y, z - 1)];
        this.field_22333_Z = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x - 1, y, z + 1)];
        this.field_22336_W = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x, y + 1, z + 1)];
        this.field_22339_T = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x, y + 1, z - 1)];
        this.field_22355_ae = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x, y - 1, z + 1)];
        this.field_22361_ab = Block.CAN_BLOCK_GRASS[this.blockAccess.getBlockId(x, y - 1, z - 1)];
        if (block.blockIndexInTexture == 3) {
            var18 = false;
            var17 = false;
            var16 = false;
            var15 = false;
            var13 = false;
        }

        if (this.overrideBlockTexture >= 0) {
            var18 = false;
            var17 = false;
            var16 = false;
            var15 = false;
            var13 = false;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
            if (this.field_22352_G <= 0) {
                topRightMul = this.aoLightValueYNeg;
                botRightMul = this.aoLightValueYNeg;
                botLeftMul = this.aoLightValueYNeg;
                topLeftMul = this.aoLightValueYNeg;
            } else {
                --y;
                this.field_22376_n = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                this.field_22374_p = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                this.field_22373_q = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                this.field_22371_s = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                if (!this.field_22361_ab && !this.field_22357_ad) {
                    this.field_22377_m = this.field_22376_n;
                } else {
                    this.field_22377_m = block.getBlockBrightness(this.blockAccess, x - 1, y, z - 1);
                }

                if (!this.field_22355_ae && !this.field_22357_ad) {
                    this.field_22375_o = this.field_22376_n;
                } else {
                    this.field_22375_o = block.getBlockBrightness(this.blockAccess, x - 1, y, z + 1);
                }

                if (!this.field_22361_ab && !this.field_22359_ac) {
                    this.field_22372_r = this.field_22371_s;
                } else {
                    this.field_22372_r = block.getBlockBrightness(this.blockAccess, x + 1, y, z - 1);
                }

                if (!this.field_22355_ae && !this.field_22359_ac) {
                    this.field_22370_t = this.field_22371_s;
                } else {
                    this.field_22370_t = block.getBlockBrightness(this.blockAccess, x + 1, y, z + 1);
                }

                ++y;
                topLeftMul = (this.field_22375_o + this.field_22376_n + this.field_22373_q + this.aoLightValueYNeg) / 4.0F;
                topRightMul = (this.field_22373_q + this.aoLightValueYNeg + this.field_22370_t + this.field_22371_s) / 4.0F;
                botRightMul = (this.aoLightValueYNeg + this.field_22374_p + this.field_22371_s + this.field_22372_r) / 4.0F;
                botLeftMul = (this.field_22376_n + this.field_22377_m + this.aoLightValueYNeg + this.field_22374_p) / 4.0F;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var13 ? r : 1.0F) * 0.5F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var13 ? g : 1.0F) * 0.5F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var13 ? b : 1.0F) * 0.5F;

            this.colorRedTopLeft *= topLeftMul;
            this.colorGreenTopLeft *= topLeftMul;
            this.colorBlueTopLeft *= topLeftMul;

            this.colorRedBottomLeft *= botLeftMul;
            this.colorGreenBottomLeft *= botLeftMul;
            this.colorBlueBottomLeft *= botLeftMul;

            this.colorRedBottomRight *= botRightMul;
            this.colorGreenBottomRight *= botRightMul;
            this.colorBlueBottomRight *= botRightMul;

            this.colorRedTopRight *= topRightMul;
            this.colorGreenTopRight *= topRightMul;
            this.colorBlueTopRight *= topRightMul;
            this.renderBottomFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
            result = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
            if (this.field_22352_G <= 0) {
                topRightMul = this.aoLightValueYPos;
                botRightMul = this.aoLightValueYPos;
                botLeftMul = this.aoLightValueYPos;
                topLeftMul = this.aoLightValueYPos;
            } else {
                ++y;
                this.field_22368_v = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                this.field_22364_z = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                this.field_22366_x = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                this.field_22362_A = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                if (!this.field_22339_T && !this.field_22337_V) {
                    this.field_22369_u = this.field_22368_v;
                } else {
                    this.field_22369_u = block.getBlockBrightness(this.blockAccess, x - 1, y, z - 1);
                }

                if (!this.field_22339_T && !this.field_22338_U) {
                    this.field_22365_y = this.field_22364_z;
                } else {
                    this.field_22365_y = block.getBlockBrightness(this.blockAccess, x + 1, y, z - 1);
                }

                if (!this.field_22336_W && !this.field_22337_V) {
                    this.field_22367_w = this.field_22368_v;
                } else {
                    this.field_22367_w = block.getBlockBrightness(this.blockAccess, x - 1, y, z + 1);
                }

                if (!this.field_22336_W && !this.field_22338_U) {
                    this.field_22360_B = this.field_22364_z;
                } else {
                    this.field_22360_B = block.getBlockBrightness(this.blockAccess, x + 1, y, z + 1);
                }

                --y;
                topRightMul = (this.field_22367_w + this.field_22368_v + this.field_22362_A + this.aoLightValueYPos) / 4.0F;
                topLeftMul = (this.field_22362_A + this.aoLightValueYPos + this.field_22360_B + this.field_22364_z) / 4.0F;
                botLeftMul = (this.aoLightValueYPos + this.field_22366_x + this.field_22364_z + this.field_22365_y) / 4.0F;
                botRightMul = (this.field_22368_v + this.field_22369_u + this.aoLightValueYPos + this.field_22366_x) / 4.0F;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = var14 ? r : 1.0F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = var14 ? g : 1.0F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = var14 ? b : 1.0F;

            this.colorRedTopLeft *= topLeftMul;
            this.colorGreenTopLeft *= topLeftMul;
            this.colorBlueTopLeft *= topLeftMul;

            this.colorRedBottomLeft *= botLeftMul;
            this.colorGreenBottomLeft *= botLeftMul;
            this.colorBlueBottomLeft *= botLeftMul;

            this.colorRedBottomRight *= botRightMul;
            this.colorGreenBottomRight *= botRightMul;
            this.colorBlueBottomRight *= botRightMul;

            this.colorRedTopRight *= topRightMul;
            this.colorGreenTopRight *= topRightMul;
            this.colorBlueTopRight *= topRightMul;
            this.renderTopFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
            result = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
            if (this.field_22352_G <= 0) {
                topRightMul = this.aoLightValueZNeg;
                botRightMul = this.aoLightValueZNeg;
                botLeftMul = this.aoLightValueZNeg;
                topLeftMul = this.aoLightValueZNeg;
            } else {
                --z;
                this.field_22358_C = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                this.field_22374_p = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                this.field_22366_x = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                this.field_22356_D = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                if (!this.field_22335_X && !this.field_22361_ab) {
                    this.field_22377_m = this.field_22358_C;
                } else {
                    this.field_22377_m = block.getBlockBrightness(this.blockAccess, x - 1, y - 1, z);
                }

                if (!this.field_22335_X && !this.field_22339_T) {
                    this.field_22369_u = this.field_22358_C;
                } else {
                    this.field_22369_u = block.getBlockBrightness(this.blockAccess, x - 1, y + 1, z);
                }

                if (!this.field_22363_aa && !this.field_22361_ab) {
                    this.field_22372_r = this.field_22356_D;
                } else {
                    this.field_22372_r = block.getBlockBrightness(this.blockAccess, x + 1, y - 1, z);
                }

                if (!this.field_22363_aa && !this.field_22339_T) {
                    this.field_22365_y = this.field_22356_D;
                } else {
                    this.field_22365_y = block.getBlockBrightness(this.blockAccess, x + 1, y + 1, z);
                }

                ++z;
                topLeftMul = (this.field_22358_C + this.field_22369_u + this.aoLightValueZNeg + this.field_22366_x) / 4.0F;
                botLeftMul = (this.aoLightValueZNeg + this.field_22366_x + this.field_22356_D + this.field_22365_y) / 4.0F;
                botRightMul = (this.field_22374_p + this.aoLightValueZNeg + this.field_22372_r + this.field_22356_D) / 4.0F;
                topRightMul = (this.field_22377_m + this.field_22358_C + this.field_22374_p + this.aoLightValueZNeg) / 4.0F;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var15 ? r : 1.0F) * 0.8F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var15 ? g : 1.0F) * 0.8F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var15 ? b : 1.0F) * 0.8F;
            this.colorRedTopLeft *= topLeftMul;
            this.colorGreenTopLeft *= topLeftMul;
            this.colorBlueTopLeft *= topLeftMul;

            this.colorRedBottomLeft *= botLeftMul;
            this.colorGreenBottomLeft *= botLeftMul;
            this.colorBlueBottomLeft *= botLeftMul;

            this.colorRedBottomRight *= botRightMul;
            this.colorGreenBottomRight *= botRightMul;
            this.colorBlueBottomRight *= botRightMul;

            this.colorRedTopRight *= topRightMul;
            this.colorGreenTopRight *= topRightMul;
            this.colorBlueTopRight *= topRightMul;
            int blockTexture = block.getBlockTexture(this.blockAccess, x, y, z, 2);
            this.renderEastFace(block, x, y, z, blockTexture);

            if (fancyGrass && blockTexture == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;

                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;

                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;
                this.renderEastFace(block, x, y, z, 38);
            }

            result = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
            if (this.field_22352_G <= 0) {
                topRightMul = this.aoLightValueZPos;
                botRightMul = this.aoLightValueZPos;
                botLeftMul = this.aoLightValueZPos;
                topLeftMul = this.aoLightValueZPos;
            } else {
                ++z;
                this.field_22354_E = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                this.field_22353_F = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                this.field_22373_q = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                this.field_22362_A = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                if (!this.field_22333_Z && !this.field_22355_ae) {
                    this.field_22375_o = this.field_22354_E;
                } else {
                    this.field_22375_o = block.getBlockBrightness(this.blockAccess, x - 1, y - 1, z);
                }

                if (!this.field_22333_Z && !this.field_22336_W) {
                    this.field_22367_w = this.field_22354_E;
                } else {
                    this.field_22367_w = block.getBlockBrightness(this.blockAccess, x - 1, y + 1, z);
                }

                if (!this.field_22334_Y && !this.field_22355_ae) {
                    this.field_22370_t = this.field_22353_F;
                } else {
                    this.field_22370_t = block.getBlockBrightness(this.blockAccess, x + 1, y - 1, z);
                }

                if (!this.field_22334_Y && !this.field_22336_W) {
                    this.field_22360_B = this.field_22353_F;
                } else {
                    this.field_22360_B = block.getBlockBrightness(this.blockAccess, x + 1, y + 1, z);
                }

                --z;
                topLeftMul = (this.field_22354_E + this.field_22367_w + this.aoLightValueZPos + this.field_22362_A) / 4.0F;
                topRightMul = (this.aoLightValueZPos + this.field_22362_A + this.field_22353_F + this.field_22360_B) / 4.0F;
                botRightMul = (this.field_22373_q + this.aoLightValueZPos + this.field_22370_t + this.field_22353_F) / 4.0F;
                botLeftMul = (this.field_22375_o + this.field_22354_E + this.field_22373_q + this.aoLightValueZPos) / 4.0F;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var16 ? r : 1.0F) * 0.8F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var16 ? g : 1.0F) * 0.8F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var16 ? b : 1.0F) * 0.8F;

            this.colorRedTopLeft *= topLeftMul;
            this.colorGreenTopLeft *= topLeftMul;
            this.colorBlueTopLeft *= topLeftMul;

            this.colorRedBottomLeft *= botLeftMul;
            this.colorGreenBottomLeft *= botLeftMul;
            this.colorBlueBottomLeft *= botLeftMul;

            this.colorRedBottomRight *= botRightMul;
            this.colorGreenBottomRight *= botRightMul;
            this.colorBlueBottomRight *= botRightMul;

            this.colorRedTopRight *= topRightMul;
            this.colorGreenTopRight *= topRightMul;
            this.colorBlueTopRight *= topRightMul;
            int blockTexture = block.getBlockTexture(this.blockAccess, x, y, z, 3);
            this.renderWestFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 3));

            if (fancyGrass && blockTexture == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;

                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;

                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;

                this.renderWestFace(block, x, y, z, 38);
            }

            result = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
            if (this.field_22352_G <= 0) {
                topRightMul = this.aoLightValueXNeg;
                botRightMul = this.aoLightValueXNeg;
                botLeftMul = this.aoLightValueXNeg;
                topLeftMul = this.aoLightValueXNeg;
            } else {
                --x;
                this.field_22376_n = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                this.field_22358_C = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                this.field_22354_E = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                this.field_22368_v = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                if (!this.field_22335_X && !this.field_22357_ad) {
                    this.field_22377_m = this.field_22358_C;
                } else {
                    this.field_22377_m = block.getBlockBrightness(this.blockAccess, x, y - 1, z - 1);
                }

                if (!this.field_22333_Z && !this.field_22357_ad) {
                    this.field_22375_o = this.field_22354_E;
                } else {
                    this.field_22375_o = block.getBlockBrightness(this.blockAccess, x, y - 1, z + 1);
                }

                if (!this.field_22335_X && !this.field_22337_V) {
                    this.field_22369_u = this.field_22358_C;
                } else {
                    this.field_22369_u = block.getBlockBrightness(this.blockAccess, x, y + 1, z - 1);
                }

                if (!this.field_22333_Z && !this.field_22337_V) {
                    this.field_22367_w = this.field_22354_E;
                } else {
                    this.field_22367_w = block.getBlockBrightness(this.blockAccess, x, y + 1, z + 1);
                }

                ++x;
                topRightMul = (this.field_22376_n + this.field_22375_o + this.aoLightValueXNeg + this.field_22354_E) / 4.0F;
                topLeftMul = (this.aoLightValueXNeg + this.field_22354_E + this.field_22368_v + this.field_22367_w) / 4.0F;
                botLeftMul = (this.field_22358_C + this.aoLightValueXNeg + this.field_22369_u + this.field_22368_v) / 4.0F;
                botRightMul = (this.field_22377_m + this.field_22376_n + this.field_22358_C + this.aoLightValueXNeg) / 4.0F;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var17 ? r : 1.0F) * 0.6F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var17 ? g : 1.0F) * 0.6F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var17 ? b : 1.0F) * 0.6F;

            this.colorRedTopLeft *= topLeftMul;
            this.colorGreenTopLeft *= topLeftMul;
            this.colorBlueTopLeft *= topLeftMul;

            this.colorRedBottomLeft *= botLeftMul;
            this.colorGreenBottomLeft *= botLeftMul;
            this.colorGreenBottomLeft *= botLeftMul;
            this.colorBlueBottomLeft *= botLeftMul;

            this.colorRedBottomRight *= botRightMul;
            this.colorGreenBottomRight *= botRightMul;
            this.colorBlueBottomRight *= botRightMul;

            this.colorRedTopRight *= topRightMul;
            this.colorGreenTopRight *= topRightMul;
            this.colorBlueTopRight *= topRightMul;
            int blockTexture = block.getBlockTexture(this.blockAccess, x, y, z, 4);
            this.renderNorthFace(block, x, y, z, blockTexture);
            if (fancyGrass && blockTexture == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;

                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;

                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;
                this.renderNorthFace(block, x, y, z, 38);
            }

            result = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
            if (this.field_22352_G <= 0) {
                topRightMul = this.aoLightValueXPos;
                botRightMul = this.aoLightValueXPos;
                botLeftMul = this.aoLightValueXPos;
                topLeftMul = this.aoLightValueXPos;
            } else {
                ++x;
                this.field_22371_s = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                this.field_22356_D = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                this.field_22353_F = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                this.field_22364_z = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                if (!this.field_22359_ac && !this.field_22363_aa) {
                    this.field_22372_r = this.field_22356_D;
                } else {
                    this.field_22372_r = block.getBlockBrightness(this.blockAccess, x, y - 1, z - 1);
                }

                if (!this.field_22359_ac && !this.field_22334_Y) {
                    this.field_22370_t = this.field_22353_F;
                } else {
                    this.field_22370_t = block.getBlockBrightness(this.blockAccess, x, y - 1, z + 1);
                }

                if (!this.field_22338_U && !this.field_22363_aa) {
                    this.field_22365_y = this.field_22356_D;
                } else {
                    this.field_22365_y = block.getBlockBrightness(this.blockAccess, x, y + 1, z - 1);
                }

                if (!this.field_22338_U && !this.field_22334_Y) {
                    this.field_22360_B = this.field_22353_F;
                } else {
                    this.field_22360_B = block.getBlockBrightness(this.blockAccess, x, y + 1, z + 1);
                }

                --x;
                topLeftMul = (this.field_22371_s + this.field_22370_t + this.aoLightValueXPos + this.field_22353_F) / 4.0F;
                topRightMul = (this.aoLightValueXPos + this.field_22353_F + this.field_22364_z + this.field_22360_B) / 4.0F;
                botRightMul = (this.field_22356_D + this.aoLightValueXPos + this.field_22365_y + this.field_22364_z) / 4.0F;
                botLeftMul = (this.field_22372_r + this.field_22371_s + this.field_22356_D + this.aoLightValueXPos) / 4.0F;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var18 ? r : 1.0F) * 0.6F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var18 ? g : 1.0F) * 0.6F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var18 ? b : 1.0F) * 0.6F;

            this.colorRedTopLeft *= topLeftMul;
            this.colorGreenTopLeft *= topLeftMul;
            this.colorBlueTopLeft *= topLeftMul;

            this.colorRedBottomLeft *= botLeftMul;
            this.colorGreenBottomLeft *= botLeftMul;
            this.colorBlueBottomLeft *= botLeftMul;

            this.colorRedBottomRight *= botRightMul;
            this.colorGreenBottomRight *= botRightMul;
            this.colorBlueBottomRight *= botRightMul;

            this.colorRedTopRight *= topRightMul;
            this.colorGreenTopRight *= topRightMul;
            this.colorBlueTopRight *= topRightMul;
            int blockTexture = block.getBlockTexture(this.blockAccess, x, y, z, 5);
            this.renderSouthFace(block, x, y, z, blockTexture);

            if (fancyGrass && blockTexture == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;
                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;
                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;
                this.renderSouthFace(block, x, y, z, 38);
            }

            result = true;
        }

        this.enableAO = false;
        return result;
    }

    public boolean renderStandardBlockWithColorMultiplier(Block block, int x, int y, int z, float r, float g, float b) {
        this.enableAO = false;
        Tessellator tess = Tessellator.INSTANCE;
        boolean var9 = false;
        float var10 = 0.5F;
        float var11 = 1.0F;
        float var12 = 0.8F;
        float var13 = 0.6F;
        float var14 = var11 * r;
        float var15 = var11 * g;
        float var16 = var11 * b;
        float var17 = var10;
        float var18 = var12;
        float var19 = var13;
        float var20 = var10;
        float var21 = var12;
        float var22 = var13;
        float var23 = var10;
        float var24 = var12;
        float var25 = var13;
        if (block != Block.GRASS) {
            var17 = var10 * r;
            var18 = var12 * r;
            var19 = var13 * r;

            var20 = var10 * g;
            var21 = var12 * g;
            var22 = var13 * g;

            var23 = var10 * b;
            var24 = var12 * b;
            var25 = var13 * b;
        }

        float blockBrightness = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
            float var27 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
            tess.setColorOpaque_F(var17 * var27, var20 * var27, var23 * var27);
            this.renderBottomFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
            var9 = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
            float var29 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
            if (block.maxY != 1.0D && !block.blockMaterial.getIsLiquid()) {
                var29 = blockBrightness;
            }

            tess.setColorOpaque_F(var14 * var29, var15 * var29, var16 * var29);
            this.renderTopFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
            var9 = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
            float var30 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
            if (block.minZ > 0.0D) {
                var30 = blockBrightness;
            }

            tess.setColorOpaque_F(var18 * var30, var21 * var30, var24 * var30);
            int var28 = block.getBlockTexture(this.blockAccess, x, y, z, 2);
            this.renderEastFace(block, x, y, z, var28);
            if (fancyGrass && var28 == 3 && this.overrideBlockTexture < 0) {
                tess.setColorOpaque_F(var18 * var30 * r, var21 * var30 * g, var24 * var30 * b);
                this.renderEastFace(block, x, y, z, 38);
            }

            var9 = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
            float var31 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
            if (block.maxZ < 1.0D) {
                var31 = blockBrightness;
            }

            tess.setColorOpaque_F(var18 * var31, var21 * var31, var24 * var31);
            int var34 = block.getBlockTexture(this.blockAccess, x, y, z, 3);
            this.renderWestFace(block, x, y, z, var34);
            if (fancyGrass && var34 == 3 && this.overrideBlockTexture < 0) {
                tess.setColorOpaque_F(var18 * var31 * r, var21 * var31 * g, var24 * var31 * b);
                this.renderWestFace(block, x, y, z, 38);
            }

            var9 = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
            float var32 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
            if (block.minX > 0.0D) {
                var32 = blockBrightness;
            }

            tess.setColorOpaque_F(var19 * var32, var22 * var32, var25 * var32);
            int var35 = block.getBlockTexture(this.blockAccess, x, y, z, 4);
            this.renderNorthFace(block, x, y, z, var35);
            if (fancyGrass && var35 == 3 && this.overrideBlockTexture < 0) {
                tess.setColorOpaque_F(var19 * var32 * r, var22 * var32 * g, var25 * var32 * b);
                this.renderNorthFace(block, x, y, z, 38);
            }

            var9 = true;
        }

        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
            float var33 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
            if (block.maxX < 1.0D) {
                var33 = blockBrightness;
            }

            tess.setColorOpaque_F(var19 * var33, var22 * var33, var25 * var33);
            int blockTexture = block.getBlockTexture(this.blockAccess, x, y, z, 5);
            this.renderSouthFace(block, x, y, z, blockTexture);
            if (fancyGrass && blockTexture == 3 && this.overrideBlockTexture < 0) {
                tess.setColorOpaque_F(var19 * var33 * r, var22 * var33 * g, var25 * var33 * b);
                this.renderSouthFace(block, x, y, z, 38);
            }

            var9 = true;
        }

        return var9;
    }

    public boolean renderBlockCactus(Block block, int x, int y, int z) {
        int colorMultiplier = block.colorMultiplier(this.blockAccess, x, y, z);
        float r = (float) (colorMultiplier >> 16 & 255) / 255.0F;
        float g = (float) (colorMultiplier >> 8 & 255) / 255.0F;
        float b = (float) (colorMultiplier & 255) / 255.0F;
        if (EntityRenderer.field_28135_a) {
            float tmpR = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float tmpG = (r * 30.0F + g * 70.0F) / 100.0F;
            float tmpB = (r * 30.0F + b * 70.0F) / 100.0F;
            r = tmpR;
            g = tmpG;
            b = tmpB;
        }

        return this.func_1230_b(block, x, y, z, r, g, b);
    }

    public boolean func_1230_b(Block var1, int var2, int var3, int var4, float var5, float var6, float var7) {
        Tessellator tess = Tessellator.INSTANCE;
        boolean var9 = false;
        float var10 = 0.5F;
        float var11 = 1.0F;
        float var12 = 0.8F;
        float var13 = 0.6F;
        float var14 = var10 * var5;
        float var15 = var11 * var5;
        float var16 = var12 * var5;
        float var17 = var13 * var5;
        float var18 = var10 * var6;
        float var19 = var11 * var6;
        float var20 = var12 * var6;
        float var21 = var13 * var6;
        float var22 = var10 * var7;
        float var23 = var11 * var7;
        float var24 = var12 * var7;
        float var25 = var13 * var7;
        float var26 = 0.0625F;
        float var27 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
        if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 - 1, var4, 0)) {
            float var28 = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
            tess.setColorOpaque_F(var14 * var28, var18 * var28, var22 * var28);
            this.renderBottomFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 0));
            var9 = true;
        }

        if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 + 1, var4, 1)) {
            float var29 = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
            if (var1.maxY != 1.0D && !var1.blockMaterial.getIsLiquid()) {
                var29 = var27;
            }

            tess.setColorOpaque_F(var15 * var29, var19 * var29, var23 * var29);
            this.renderTopFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 1));
            var9 = true;
        }

        if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2)) {
            float var30 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
            if (var1.minZ > 0.0D) {
                var30 = var27;
            }

            tess.setColorOpaque_F(var16 * var30, var20 * var30, var24 * var30);
            tess.setTranslationF(0.0F, 0.0F, var26);
            this.renderEastFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 2));
            tess.setTranslationF(0.0F, 0.0F, -var26);
            var9 = true;
        }

        if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3)) {
            float var31 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
            if (var1.maxZ < 1.0D) {
                var31 = var27;
            }

            tess.setColorOpaque_F(var16 * var31, var20 * var31, var24 * var31);
            tess.setTranslationF(0.0F, 0.0F, -var26);
            this.renderWestFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 3));
            tess.setTranslationF(0.0F, 0.0F, var26);
            var9 = true;
        }

        if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4)) {
            float var32 = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
            if (var1.minX > 0.0D) {
                var32 = var27;
            }

            tess.setColorOpaque_F(var17 * var32, var21 * var32, var25 * var32);
            tess.setTranslationF(var26, 0.0F, 0.0F);
            this.renderNorthFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 4));
            tess.setTranslationF(-var26, 0.0F, 0.0F);
            var9 = true;
        }

        if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5)) {
            float var33 = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
            if (var1.maxX < 1.0D) {
                var33 = var27;
            }

            tess.setColorOpaque_F(var17 * var33, var21 * var33, var25 * var33);
            tess.setTranslationF(-var26, 0.0F, 0.0F);
            this.renderSouthFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 5));
            tess.setTranslationF(var26, 0.0F, 0.0F);
            var9 = true;
        }

        return var9;
    }

    public boolean renderBlockFence(Block block, int x, int y, int z) {
        boolean var5 = false;
        float var6 = 0.375F;
        float var7 = 0.625F;
        block.setBlockBounds(var6, 0.0F, var6, var7, 1.0F, var7);
        this.renderStandardBlock(block, x, y, z);
        var5 = true;
        boolean var8 = false;
        boolean var9 = false;
        if (this.blockAccess.getBlockId(x - 1, y, z) == block.blockID || this.blockAccess.getBlockId(x + 1, y, z) == block.blockID) {
            var8 = true;
        }

        if (this.blockAccess.getBlockId(x, y, z - 1) == block.blockID || this.blockAccess.getBlockId(x, y, z + 1) == block.blockID) {
            var9 = true;
        }

        boolean var10 = this.blockAccess.getBlockId(x - 1, y, z) == block.blockID;
        boolean var11 = this.blockAccess.getBlockId(x + 1, y, z) == block.blockID;
        boolean var12 = this.blockAccess.getBlockId(x, y, z - 1) == block.blockID;
        boolean var13 = this.blockAccess.getBlockId(x, y, z + 1) == block.blockID;
        if (!var8 && !var9) {
            var8 = true;
        }

        var6 = 0.4375F;
        var7 = 0.5625F;
        float var14 = 0.75F;
        float var15 = 0.9375F;
        float var16 = var10 ? 0.0F : var6;
        float var17 = var11 ? 1.0F : var7;
        float var18 = var12 ? 0.0F : var6;
        float var19 = var13 ? 1.0F : var7;
        if (var8) {
            block.setBlockBounds(var16, var14, var6, var17, var15, var7);
            this.renderStandardBlock(block, x, y, z);
            var5 = true;
        }

        if (var9) {
            block.setBlockBounds(var6, var14, var18, var7, var15, var19);
            this.renderStandardBlock(block, x, y, z);
            var5 = true;
        }

        var14 = 0.375F;
        var15 = 0.5625F;
        if (var8) {
            block.setBlockBounds(var16, var14, var6, var17, var15, var7);
            this.renderStandardBlock(block, x, y, z);
            var5 = true;
        }

        if (var9) {
            block.setBlockBounds(var6, var14, var18, var7, var15, var19);
            this.renderStandardBlock(block, x, y, z);
            var5 = true;
        }

        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return var5;
    }

    public boolean renderBlockStairs(Block var1, int var2, int var3, int var4) {
        boolean var5 = false;
        int blockMetadata = this.blockAccess.getBlockMetadata(var2, var3, var4);
        if (blockMetadata == 0) {
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            var1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            var5 = true;
        } else if (blockMetadata == 1) {
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            var1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            var5 = true;
        } else if (blockMetadata == 2) {
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
            this.renderStandardBlock(var1, var2, var3, var4);
            var1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            var5 = true;
        } else if (blockMetadata == 3) {
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            this.renderStandardBlock(var1, var2, var3, var4);
            var1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            var5 = true;
        }

        var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return var5;
    }

    public boolean renderBlockDoor(Block block, int x, int y, int z) {
        Tessellator tess = Tessellator.INSTANCE;
        BlockDoor blockDoor = (BlockDoor) block;
        boolean var7 = false;
        float var8 = 0.5F;
        float var9 = 1.0F;
        float var10 = 0.8F;
        float var11 = 0.6F;
        float var12 = block.getBlockBrightness(this.blockAccess, x, y, z);
        float var13 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
        if (blockDoor.minY > 0.0D) {
            var13 = var12;
        }

        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            var13 = 1.0F;
        }

        tess.setColorOpaque_F(var8 * var13, var8 * var13, var8 * var13);
        this.renderBottomFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
        var7 = true;
        var13 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
        if (blockDoor.maxY < 1.0D) {
            var13 = var12;
        }

        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            var13 = 1.0F;
        }

        tess.setColorOpaque_F(var9 * var13, var9 * var13, var9 * var13);
        this.renderTopFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
        var7 = true;
        var13 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
        if (blockDoor.minZ > 0.0D) {
            var13 = var12;
        }

        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            var13 = 1.0F;
        }

        tess.setColorOpaque_F(var10 * var13, var10 * var13, var10 * var13);
        int var14 = block.getBlockTexture(this.blockAccess, x, y, z, 2);
        if (var14 < 0) {
            this.flipTexture = true;
            var14 = -var14;
        }

        this.renderEastFace(block, x, y, z, var14);
        var7 = true;
        this.flipTexture = false;
        var13 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
        if (blockDoor.maxZ < 1.0D) {
            var13 = var12;
        }

        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            var13 = 1.0F;
        }

        tess.setColorOpaque_F(var10 * var13, var10 * var13, var10 * var13);
        var14 = block.getBlockTexture(this.blockAccess, x, y, z, 3);
        if (var14 < 0) {
            this.flipTexture = true;
            var14 = -var14;
        }

        this.renderWestFace(block, x, y, z, var14);
        var7 = true;
        this.flipTexture = false;
        var13 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
        if (blockDoor.minX > 0.0D) {
            var13 = var12;
        }

        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            var13 = 1.0F;
        }

        tess.setColorOpaque_F(var11 * var13, var11 * var13, var11 * var13);
        var14 = block.getBlockTexture(this.blockAccess, x, y, z, 4);
        if (var14 < 0) {
            this.flipTexture = true;
            var14 = -var14;
        }

        this.renderNorthFace(block, x, y, z, var14);
        var7 = true;
        this.flipTexture = false;
        var13 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
        if (blockDoor.maxX < 1.0D) {
            var13 = var12;
        }

        if (Block.LIGHT_VALUE[block.blockID] > 0) {
            var13 = 1.0F;
        }

        tess.setColorOpaque_F(var11 * var13, var11 * var13, var11 * var13);
        var14 = block.getBlockTexture(this.blockAccess, x, y, z, 5);
        if (var14 < 0) {
            this.flipTexture = true;
            var14 = -var14;
        }

        this.renderSouthFace(block, x, y, z, var14);
        var7 = true;
        this.flipTexture = false;
        return var7;
    }

    public void renderBottomFace(Block block, double var2, double var4, double var6, int var8) {
        Tessellator tess = Tessellator.INSTANCE;
        if (this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        int var11 = var8 & 240;
        double var12 = ((double) var10 + block.minX * 16.0D) / 256.0D;
        double var14 = ((double) var10 + block.maxX * 16.0D - 0.01D) / 256.0D;
        double var16 = ((double) var11 + block.minZ * 16.0D) / 256.0D;
        double var18 = ((double) var11 + block.maxZ * 16.0D - 0.01D) / 256.0D;
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            var12 = ((float) var10 + 0.0F) / 256.0F;
            var14 = ((float) var10 + 15.99F) / 256.0F;
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            var16 = ((float) var11 + 0.0F) / 256.0F;
            var18 = ((float) var11 + 15.99F) / 256.0F;
        }

        double var20 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_31082_l == 2) {
            var12 = ((double) var10 + block.minZ * 16.0D) / 256.0D;
            var16 = ((double) (var11 + 16) - block.maxX * 16.0D) / 256.0D;
            var14 = ((double) var10 + block.maxZ * 16.0D) / 256.0D;
            var18 = ((double) (var11 + 16) - block.minX * 16.0D) / 256.0D;
            var24 = var16;
            var26 = var18;
            var20 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var24;
        } else if (this.field_31082_l == 1) {
            var12 = ((double) (var10 + 16) - block.maxZ * 16.0D) / 256.0D;
            var16 = ((double) var11 + block.minX * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.minZ * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.maxX * 16.0D) / 256.0D;
            var20 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var22;
            var24 = var18;
            var26 = var16;
        } else if (this.field_31082_l == 3) {
            var12 = ((double) (var10 + 16) - block.minX * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.maxX * 16.0D - 0.01D) / 256.0D;
            var16 = ((double) (var11 + 16) - block.minZ * 16.0D) / 256.0D;
            var18 = ((double) (var11 + 16) - block.maxZ * 16.0D - 0.01D) / 256.0D;
            var20 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = var2 + block.minX;
        double var30 = var2 + block.maxX;
        double var32 = var4 + block.minY;
        double var34 = var6 + block.minZ;
        double var36 = var6 + block.maxZ;
        if (this.enableAO) {
            tess.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tess.addVertexWithUV(var28, var32, var36, var22, var26);
            tess.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tess.addVertexWithUV(var28, var32, var34, var12, var16);
            tess.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tess.addVertexWithUV(var30, var32, var34, var20, var24);
            tess.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tess.addVertexWithUV(var30, var32, var36, var14, var18);
        } else {
            tess.addVertexWithUV(var28, var32, var36, var22, var26);
            tess.addVertexWithUV(var28, var32, var34, var12, var16);
            tess.addVertexWithUV(var30, var32, var34, var20, var24);
            tess.addVertexWithUV(var30, var32, var36, var14, var18);
        }

    }

    public void renderTopFace(Block var1, double var2, double var4, double var6, int var8) {
        Tessellator tess = Tessellator.INSTANCE;
        if (this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        int var11 = var8 & 240;
        double var12 = ((double) var10 + var1.minX * 16.0D) / 256.0D;
        double var14 = ((double) var10 + var1.maxX * 16.0D - 0.01D) / 256.0D;
        double var16 = ((double) var11 + var1.minZ * 16.0D) / 256.0D;
        double var18 = ((double) var11 + var1.maxZ * 16.0D - 0.01D) / 256.0D;
        if (var1.minX < 0.0D || var1.maxX > 1.0D) {
            var12 = ((float) var10 + 0.0F) / 256.0F;
            var14 = ((float) var10 + 15.99F) / 256.0F;
        }

        if (var1.minZ < 0.0D || var1.maxZ > 1.0D) {
            var16 = ((float) var11 + 0.0F) / 256.0F;
            var18 = ((float) var11 + 15.99F) / 256.0F;
        }

        double var20 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_31083_k == 1) {
            var12 = ((double) var10 + var1.minZ * 16.0D) / 256.0D;
            var16 = ((double) (var11 + 16) - var1.maxX * 16.0D) / 256.0D;
            var14 = ((double) var10 + var1.maxZ * 16.0D) / 256.0D;
            var18 = ((double) (var11 + 16) - var1.minX * 16.0D) / 256.0D;
            var24 = var16;
            var26 = var18;
            var20 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var24;
        } else if (this.field_31083_k == 2) {
            var12 = ((double) (var10 + 16) - var1.maxZ * 16.0D) / 256.0D;
            var16 = ((double) var11 + var1.minX * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - var1.minZ * 16.0D) / 256.0D;
            var18 = ((double) var11 + var1.maxX * 16.0D) / 256.0D;
            var20 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var22;
            var24 = var18;
            var26 = var16;
        } else if (this.field_31083_k == 3) {
            var12 = ((double) (var10 + 16) - var1.minX * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - var1.maxX * 16.0D - 0.01D) / 256.0D;
            var16 = ((double) (var11 + 16) - var1.minZ * 16.0D) / 256.0D;
            var18 = ((double) (var11 + 16) - var1.maxZ * 16.0D - 0.01D) / 256.0D;
            var20 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = var2 + var1.minX;
        double var30 = var2 + var1.maxX;
        double var32 = var4 + var1.maxY;
        double var34 = var6 + var1.minZ;
        double var36 = var6 + var1.maxZ;
        if (this.enableAO) {
            tess.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);

            tess.addVertexWithUV(var30, var32, var36, var14, var18);
            tess.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tess.addVertexWithUV(var30, var32, var34, var20, var24);
            tess.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tess.addVertexWithUV(var28, var32, var34, var12, var16);
            tess.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tess.addVertexWithUV(var28, var32, var36, var22, var26);
        } else {
            tess.addVertexWithUV(var30, var32, var36, var14, var18);
            tess.addVertexWithUV(var30, var32, var34, var20, var24);
            tess.addVertexWithUV(var28, var32, var34, var12, var16);
            tess.addVertexWithUV(var28, var32, var36, var22, var26);
        }

    }

    public void renderEastFace(Block block, double var2, double var4, double var6, int var8) {
        Tessellator var9 = Tessellator.INSTANCE;
        if (this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        int var11 = var8 & 240;
        double var12 = ((double) var10 + block.minX * 16.0D) / 256.0D;
        double var14 = ((double) var10 + block.maxX * 16.0D - 0.01D) / 256.0D;
        double var16 = ((double) (var11 + 16) - block.maxY * 16.0D) / 256.0D;
        double var18 = ((double) (var11 + 16) - block.minY * 16.0D - 0.01D) / 256.0D;
        if (this.flipTexture) {
            double var20 = var12;
            var12 = var14;
            var14 = var20;
        }

        if (block.minX < 0.0D || block.maxX > 1.0D) {
            var12 = ((float) var10 + 0.0F) / 256.0F;
            var14 = ((float) var10 + 15.99F) / 256.0F;
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            var16 = ((float) var11 + 0.0F) / 256.0F;
            var18 = ((float) var11 + 15.99F) / 256.0F;
        }

        double var42 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_31087_g == 2) {
            var12 = ((double) var10 + block.minY * 16.0D) / 256.0D;
            var16 = ((double) (var11 + 16) - block.minX * 16.0D) / 256.0D;
            var14 = ((double) var10 + block.maxY * 16.0D) / 256.0D;
            var18 = ((double) (var11 + 16) - block.maxX * 16.0D) / 256.0D;
            var24 = var16;
            var26 = var18;
            var42 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var24;
        } else if (this.field_31087_g == 1) {
            var12 = ((double) (var10 + 16) - block.maxY * 16.0D) / 256.0D;
            var16 = ((double) var11 + block.maxX * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.minY * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.minX * 16.0D) / 256.0D;
            var42 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var22;
            var24 = var18;
            var26 = var16;
        } else if (this.field_31087_g == 3) {
            var12 = ((double) (var10 + 16) - block.minX * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.maxX * 16.0D - 0.01D) / 256.0D;
            var16 = ((double) var11 + block.maxY * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.minY * 16.0D - 0.01D) / 256.0D;
            var42 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = var2 + block.minX;
        double var30 = var2 + block.maxX;
        double var32 = var4 + block.minY;
        double var34 = var4 + block.maxY;
        double var36 = var6 + block.minZ;
        if (this.enableAO) {
            var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            var9.addVertexWithUV(var28, var34, var36, var42, var24);
            var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            var9.addVertexWithUV(var30, var34, var36, var12, var16);
            var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            var9.addVertexWithUV(var30, var32, var36, var22, var26);
            var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            var9.addVertexWithUV(var28, var32, var36, var14, var18);
        } else {
            var9.addVertexWithUV(var28, var34, var36, var42, var24);
            var9.addVertexWithUV(var30, var34, var36, var12, var16);
            var9.addVertexWithUV(var30, var32, var36, var22, var26);
            var9.addVertexWithUV(var28, var32, var36, var14, var18);
        }

    }

    public void renderWestFace(Block block, double var2, double var4, double var6, int var8) {
        Tessellator tess = Tessellator.INSTANCE;
        if (this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        int var11 = var8 & 240;
        double var12 = ((double) var10 + block.minX * 16.0D) / 256.0D;
        double var14 = ((double) var10 + block.maxX * 16.0D - 0.01D) / 256.0D;
        double var16 = ((double) (var11 + 16) - block.maxY * 16.0D) / 256.0D;
        double var18 = ((double) (var11 + 16) - block.minY * 16.0D - 0.01D) / 256.0D;
        if (this.flipTexture) {
            double var20 = var12;
            var12 = var14;
            var14 = var20;
        }

        if (block.minX < 0.0D || block.maxX > 1.0D) {
            var12 = ((float) var10 + 0.0F) / 256.0F;
            var14 = ((float) var10 + 15.99F) / 256.0F;
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            var16 = ((float) var11 + 0.0F) / 256.0F;
            var18 = ((float) var11 + 15.99F) / 256.0F;
        }

        double var42 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_31086_h == 1) {
            var12 = ((double) var10 + block.minY * 16.0D) / 256.0D;
            var18 = ((double) (var11 + 16) - block.minX * 16.0D) / 256.0D;
            var14 = ((double) var10 + block.maxY * 16.0D) / 256.0D;
            var16 = ((double) (var11 + 16) - block.maxX * 16.0D) / 256.0D;
            var24 = var16;
            var26 = var18;
            var42 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var24;
        } else if (this.field_31086_h == 2) {
            var12 = ((double) (var10 + 16) - block.maxY * 16.0D) / 256.0D;
            var16 = ((double) var11 + block.minX * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.minY * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.maxX * 16.0D) / 256.0D;
            var42 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var22;
            var24 = var18;
            var26 = var16;
        } else if (this.field_31086_h == 3) {
            var12 = ((double) (var10 + 16) - block.minX * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.maxX * 16.0D - 0.01D) / 256.0D;
            var16 = ((double) var11 + block.maxY * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.minY * 16.0D - 0.01D) / 256.0D;
            var42 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = var2 + block.minX;
        double var30 = var2 + block.maxX;
        double var32 = var4 + block.minY;
        double var34 = var4 + block.maxY;
        double var36 = var6 + block.maxZ;
        if (this.enableAO) {
            tess.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tess.addVertexWithUV(var28, var34, var36, var12, var16);
            tess.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tess.addVertexWithUV(var28, var32, var36, var22, var26);
            tess.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tess.addVertexWithUV(var30, var32, var36, var14, var18);
            tess.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tess.addVertexWithUV(var30, var34, var36, var42, var24);
        } else {
            tess.addVertexWithUV(var28, var34, var36, var12, var16);
            tess.addVertexWithUV(var28, var32, var36, var22, var26);
            tess.addVertexWithUV(var30, var32, var36, var14, var18);
            tess.addVertexWithUV(var30, var34, var36, var42, var24);
        }

    }

    public void renderNorthFace(Block block, double var2, double var4, double var6, int var8) {
        Tessellator tess = Tessellator.INSTANCE;
        if (this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        int var11 = var8 & 240;
        double var12 = ((double) var10 + block.minZ * 16.0D) / 256.0D;
        double var14 = ((double) var10 + block.maxZ * 16.0D - 0.01D) / 256.0D;
        double var16 = ((double) (var11 + 16) - block.maxY * 16.0D) / 256.0D;
        double var18 = ((double) (var11 + 16) - block.minY * 16.0D - 0.01D) / 256.0D;
        if (this.flipTexture) {
            double var20 = var12;
            var12 = var14;
            var14 = var20;
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            var12 = ((float) var10 + 0.0F) / 256.0F;
            var14 = ((float) var10 + 15.99F) / 256.0F;
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            var16 = ((float) var11 + 0.0F) / 256.0F;
            var18 = ((float) var11 + 15.99F) / 256.0F;
        }

        double var42 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_31084_j == 1) {
            var12 = ((double) var10 + block.minY * 16.0D) / 256.0D;
            var16 = ((double) (var11 + 16) - block.maxZ * 16.0D) / 256.0D;
            var14 = ((double) var10 + block.maxY * 16.0D) / 256.0D;
            var18 = ((double) (var11 + 16) - block.minZ * 16.0D) / 256.0D;
            var24 = var16;
            var26 = var18;
            var42 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var24;
        } else if (this.field_31084_j == 2) {
            var12 = ((double) (var10 + 16) - block.maxY * 16.0D) / 256.0D;
            var16 = ((double) var11 + block.minZ * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.minY * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.maxZ * 16.0D) / 256.0D;
            var42 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var22;
            var24 = var18;
            var26 = var16;
        } else if (this.field_31084_j == 3) {
            var12 = ((double) (var10 + 16) - block.minZ * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.maxZ * 16.0D - 0.01D) / 256.0D;
            var16 = ((double) var11 + block.maxY * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.minY * 16.0D - 0.01D) / 256.0D;
            var42 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = var2 + block.minX;
        double var30 = var4 + block.minY;
        double var32 = var4 + block.maxY;
        double var34 = var6 + block.minZ;
        double var36 = var6 + block.maxZ;
        if (this.enableAO) {
            tess.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tess.addVertexWithUV(var28, var32, var36, var42, var24);
            tess.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tess.addVertexWithUV(var28, var32, var34, var12, var16);
            tess.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tess.addVertexWithUV(var28, var30, var34, var22, var26);
            tess.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tess.addVertexWithUV(var28, var30, var36, var14, var18);
        } else {
            tess.addVertexWithUV(var28, var32, var36, var42, var24);
            tess.addVertexWithUV(var28, var32, var34, var12, var16);
            tess.addVertexWithUV(var28, var30, var34, var22, var26);
            tess.addVertexWithUV(var28, var30, var36, var14, var18);
        }

    }

    public void renderSouthFace(Block block, double var2, double var4, double var6, int var8) {
        Tessellator tess = Tessellator.INSTANCE;
        if (this.overrideBlockTexture >= 0) {
            var8 = this.overrideBlockTexture;
        }

        int var10 = (var8 & 15) << 4;
        int var11 = var8 & 240;
        double var12 = ((double) var10 + block.minZ * 16.0D) / 256.0D;
        double var14 = ((double) var10 + block.maxZ * 16.0D - 0.01D) / 256.0D;
        double var16 = ((double) (var11 + 16) - block.maxY * 16.0D) / 256.0D;
        double var18 = ((double) (var11 + 16) - block.minY * 16.0D - 0.01D) / 256.0D;
        if (this.flipTexture) {
            double var20 = var12;
            var12 = var14;
            var14 = var20;
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            var12 = ((float) var10 + 0.0F) / 256.0F;
            var14 = ((float) var10 + 15.99F) / 256.0F;
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            var16 = ((float) var11 + 0.0F) / 256.0F;
            var18 = ((float) var11 + 15.99F) / 256.0F;
        }

        double var42 = var14;
        double var22 = var12;
        double var24 = var16;
        double var26 = var18;
        if (this.field_31085_i == 2) {
            var12 = ((double) var10 + block.minY * 16.0D) / 256.0D;
            var16 = ((double) (var11 + 16) - block.minZ * 16.0D) / 256.0D;
            var14 = ((double) var10 + block.maxY * 16.0D) / 256.0D;
            var18 = ((double) (var11 + 16) - block.maxZ * 16.0D) / 256.0D;
            var24 = var16;
            var26 = var18;
            var42 = var12;
            var22 = var14;
            var16 = var18;
            var18 = var24;
        } else if (this.field_31085_i == 1) {
            var12 = ((double) (var10 + 16) - block.maxY * 16.0D) / 256.0D;
            var16 = ((double) var11 + block.maxZ * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.minY * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.minZ * 16.0D) / 256.0D;
            var42 = var14;
            var22 = var12;
            var12 = var14;
            var14 = var22;
            var24 = var18;
            var26 = var16;
        } else if (this.field_31085_i == 3) {
            var12 = ((double) (var10 + 16) - block.minZ * 16.0D) / 256.0D;
            var14 = ((double) (var10 + 16) - block.maxZ * 16.0D - 0.01D) / 256.0D;
            var16 = ((double) var11 + block.maxY * 16.0D) / 256.0D;
            var18 = ((double) var11 + block.minY * 16.0D - 0.01D) / 256.0D;
            var42 = var14;
            var22 = var12;
            var24 = var16;
            var26 = var18;
        }

        double var28 = var2 + block.maxX;
        double var30 = var4 + block.minY;
        double var32 = var4 + block.maxY;
        double var34 = var6 + block.minZ;
        double var36 = var6 + block.maxZ;
        if (this.enableAO) {
            tess.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tess.addVertexWithUV(var28, var30, var36, var22, var26);
            tess.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tess.addVertexWithUV(var28, var30, var34, var14, var18);
            tess.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tess.addVertexWithUV(var28, var32, var34, var42, var24);
            tess.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tess.addVertexWithUV(var28, var32, var36, var12, var16);
        } else {
            tess.addVertexWithUV(var28, var30, var36, var22, var26);
            tess.addVertexWithUV(var28, var30, var34, var14, var18);
            tess.addVertexWithUV(var28, var32, var34, var42, var24);
            tess.addVertexWithUV(var28, var32, var36, var12, var16);
        }

    }

    public void renderBlockOnInventory(Block block, int var2, float colorMul) {
        Tessellator tess = Tessellator.INSTANCE;
        if (this.field_31088_b) {
            int var5 = block.getRenderColor(var2);
            float r = (float) (var5 >> 16 & 255) / 255.0F;
            float g = (float) (var5 >> 8 & 255) / 255.0F;
            float b = (float) (var5 & 255) / 255.0F;
            GL11.glColor4f(
                    r * colorMul,
                    g * colorMul,
                    b * colorMul,
                    1.0F
            );
        }

        int renderType = block.getRenderType();
        if (renderType != 0 && renderType != 16) {
            if (renderType == 1) {
                tess.startDrawingQuads();
                tess.setNormal(0.0F, -1.0F, 0.0F);
                this.renderCrossedSquares(block, var2, -0.5D, -0.5D, -0.5D);
                tess.draw();
            } else if (renderType == 13) {
                block.setBlockBoundsForItemRender();
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                float var10 = 0.0625F;
                tess.startDrawingQuads();
                tess.setNormal(0.0F, -1.0F, 0.0F);
                this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
                tess.draw();
                tess.startDrawingQuads();
                tess.setNormal(0.0F, 1.0F, 0.0F);
                this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
                tess.draw();
                tess.startDrawingQuads();
                tess.setNormal(0.0F, 0.0F, -1.0F);
                tess.setTranslationF(0.0F, 0.0F, var10);
                this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
                tess.setTranslationF(0.0F, 0.0F, -var10);
                tess.draw();
                tess.startDrawingQuads();
                tess.setNormal(0.0F, 0.0F, 1.0F);
                tess.setTranslationF(0.0F, 0.0F, -var10);
                this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
                tess.setTranslationF(0.0F, 0.0F, var10);
                tess.draw();
                tess.startDrawingQuads();
                tess.setNormal(-1.0F, 0.0F, 0.0F);
                tess.setTranslationF(var10, 0.0F, 0.0F);
                this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
                tess.setTranslationF(-var10, 0.0F, 0.0F);
                tess.draw();
                tess.startDrawingQuads();
                tess.setNormal(1.0F, 0.0F, 0.0F);
                tess.setTranslationF(-var10, 0.0F, 0.0F);
                this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
                tess.setTranslationF(var10, 0.0F, 0.0F);
                tess.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            } else if (renderType == 6) {
                tess.startDrawingQuads();
                tess.setNormal(0.0F, -1.0F, 0.0F);
                this.func_1245_b(block, var2, -0.5D, -0.5D, -0.5D);
                tess.draw();
            } else if (renderType == 2) {
                tess.startDrawingQuads();
                tess.setNormal(0.0F, -1.0F, 0.0F);
                this.renderTorchAtAngle(block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
                tess.draw();
            } else if (renderType == 10) {
                for (int var11 = 0; var11 < 2; ++var11) {
                    if (var11 == 0) {
                        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
                    }

                    if (var11 == 1) {
                        block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, -1.0F, 0.0F);
                    this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, -1.0F);
                    this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, 1.0F);
                    this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(-1.0F, 0.0F, 0.0F);
                    this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(1.0F, 0.0F, 0.0F);
                    this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
                    tess.draw();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }
            } else if (renderType == 11) {
                for (int var12 = 0; var12 < 4; ++var12) {
                    float var13 = 0.125F;
                    if (var12 == 0) {
                        block.setBlockBounds(0.5F - var13, 0.0F, 0.0F, 0.5F + var13, 1.0F, var13 * 2.0F);
                    }

                    if (var12 == 1) {
                        block.setBlockBounds(0.5F - var13, 0.0F, 1.0F - var13 * 2.0F, 0.5F + var13, 1.0F, 1.0F);
                    }

                    var13 = 0.0625F;
                    if (var12 == 2) {
                        block.setBlockBounds(0.5F - var13, 1.0F - var13 * 3.0F, -var13 * 2.0F, 0.5F + var13, 1.0F - var13, 1.0F + var13 * 2.0F);
                    }

                    if (var12 == 3) {
                        block.setBlockBounds(0.5F - var13, 0.5F - var13 * 3.0F, -var13 * 2.0F, 0.5F + var13, 0.5F - var13, 1.0F + var13 * 2.0F);
                    }

                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, -1.0F, 0.0F);
                    this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, -1.0F);
                    this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, 1.0F);
                    this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(-1.0F, 0.0F, 0.0F);
                    this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(1.0F, 0.0F, 0.0F);
                    this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
                    tess.draw();
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                }

                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        } else {
            if (renderType == 16) {
                var2 = 1;
            }

            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tess.startDrawingQuads();
            tess.setNormal(0.0F, -1.0F, 0.0F);
            this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, var2));
            tess.draw();
            tess.startDrawingQuads();
            tess.setNormal(0.0F, 1.0F, 0.0F);
            this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, var2));
            tess.draw();
            tess.startDrawingQuads();
            tess.setNormal(0.0F, 0.0F, -1.0F);
            this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, var2));
            tess.draw();
            tess.startDrawingQuads();
            tess.setNormal(0.0F, 0.0F, 1.0F);
            this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, var2));
            tess.draw();
            tess.startDrawingQuads();
            tess.setNormal(-1.0F, 0.0F, 0.0F);
            this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, var2));
            tess.draw();
            tess.startDrawingQuads();
            tess.setNormal(1.0F, 0.0F, 0.0F);
            this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, var2));
            tess.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

    }
}
