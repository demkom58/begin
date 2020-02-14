package net.potion.client.render;

import net.hypnosis.render.Tessellator;
import net.potion.block.Block;
import net.potion.entity.Entity;
import net.potion.entity.EntityDiggingFX;
import net.potion.entity.EntityFX;
import net.potion.util.MathHelper;
import net.potion.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EffectRenderer {
    protected World worldObj;
    private List[] fxLayers = new List[4];
    private RenderEngine renderer;
    private Random rand = new Random();

    public EffectRenderer(World var1, RenderEngine var2) {
        if (var1 != null) {
            this.worldObj = var1;
        }

        this.renderer = var2;

        for (int var3 = 0; var3 < 4; ++var3) {
            this.fxLayers[var3] = new ArrayList();
        }

    }

    public void addEffect(EntityFX var1) {
        int var2 = var1.getFXLayer();
        if (this.fxLayers[var2].size() >= 4000) {
            this.fxLayers[var2].remove(0);
        }

        this.fxLayers[var2].add(var1);
    }

    public void updateEffects() {
        for (int var1 = 0; var1 < 4; ++var1) {
            for (int var2 = 0; var2 < this.fxLayers[var1].size(); ++var2) {
                EntityFX var3 = (EntityFX) this.fxLayers[var1].get(var2);
                var3.onUpdate();
                if (var3.isDead) {
                    this.fxLayers[var1].remove(var2--);
                }
            }
        }

    }

    public void renderParticles(Entity var1, float var2) {
        float var3 = MathHelper.cos(var1.rotationYaw * 3.1415927F / 180.0F);
        float var4 = MathHelper.sin(var1.rotationYaw * 3.1415927F / 180.0F);
        float var5 = -var4 * MathHelper.sin(var1.rotationPitch * 3.1415927F / 180.0F);
        float var6 = var3 * MathHelper.sin(var1.rotationPitch * 3.1415927F / 180.0F);
        float var7 = MathHelper.cos(var1.rotationPitch * 3.1415927F / 180.0F);
        EntityFX.interpPosX = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double) var2;
        EntityFX.interpPosY = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double) var2;
        EntityFX.interpPosZ = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double) var2;

        for (int var8 = 0; var8 < 3; ++var8) {
            if (this.fxLayers[var8].size() != 0) {
                int var9 = 0;
                if (var8 == 0) {
                    var9 = this.renderer.getTexture("/particles.png");
                }

                if (var8 == 1) {
                    var9 = this.renderer.getTexture("/terrain.png");
                }

                if (var8 == 2) {
                    var9 = this.renderer.getTexture("/gui/items.png");
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9);
                Tessellator var10 = Tessellator.INSTANCE;
                var10.startDrawingQuads();

                for (int var11 = 0; var11 < this.fxLayers[var8].size(); ++var11) {
                    EntityFX var12 = (EntityFX) this.fxLayers[var8].get(var11);
                    var12.renderParticle(var10, var2, var3, var7, var4, var5, var6);
                }

                var10.draw();
            }
        }

    }

    public void func_1187_b(Entity var1, float var2) {
        byte var3 = 3;
        if (this.fxLayers[var3].size() != 0) {
            Tessellator var4 = Tessellator.INSTANCE;

            for (int var5 = 0; var5 < this.fxLayers[var3].size(); ++var5) {
                EntityFX var6 = (EntityFX) this.fxLayers[var3].get(var5);
                var6.renderParticle(var4, var2, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            }

        }
    }

    public void clearEffects(World var1) {
        this.worldObj = var1;

        for (int var2 = 0; var2 < 4; ++var2) {
            this.fxLayers[var2].clear();
        }

    }

    public void addBlockDestroyEffects(int var1, int var2, int var3, int var4, int var5) {
        if (var4 != 0) {
            Block var6 = Block.BLOCKS_LIST[var4];
            byte var7 = 4;

            for (int var8 = 0; var8 < var7; ++var8) {
                for (int var9 = 0; var9 < var7; ++var9) {
                    for (int var10 = 0; var10 < var7; ++var10) {
                        double var11 = (double) var1 + ((double) var8 + 0.5D) / (double) var7;
                        double var13 = (double) var2 + ((double) var9 + 0.5D) / (double) var7;
                        double var15 = (double) var3 + ((double) var10 + 0.5D) / (double) var7;
                        int var17 = this.rand.nextInt(6);
                        this.addEffect((new EntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double) var1 - 0.5D, var13 - (double) var2 - 0.5D, var15 - (double) var3 - 0.5D, var6, var17, var5)).position(var1, var2, var3));
                    }
                }
            }

        }
    }

    public void addBlockHitEffects(int x, int y, int z, int sideHit) {
        int blockId = this.worldObj.getBlockId(x, y, z);
        if (blockId == 0)
            return;

        Block block = Block.BLOCKS_LIST[blockId];
        float rad = 0.1F;
        double efX = (double) x + this.rand.nextDouble() * (block.maxX - block.minX - (double) (rad * 2.0F)) + (double) rad + block.minX;
        double efY = (double) y + this.rand.nextDouble() * (block.maxY - block.minY - (double) (rad * 2.0F)) + (double) rad + block.minY;
        double efZ = (double) z + this.rand.nextDouble() * (block.maxZ - block.minZ - (double) (rad * 2.0F)) + (double) rad + block.minZ;

        switch (sideHit) {
            case 0: efY = (double) y + block.minY - (double) rad; break;
            case 1: efY = (double) y + block.maxY + (double) rad; break;
            case 2: efZ = (double) z + block.minZ - (double) rad; break;
            case 3: efZ = (double) z + block.maxZ + (double) rad; break;
            case 4: efX = (double) x + block.minX - (double) rad; break;
            case 5: efX = (double) x + block.maxX + (double) rad; break;
        }

        this.addEffect(
                new EntityDiggingFX(this.worldObj, efX, efY, efZ, 0.0D, 0.0D, 0.0D, block, sideHit, this.worldObj.getBlockMetadata(x, y, z))
                        .position(x, y, z).motion(0.2F).scale(0.6F)
        );
    }

    public String getStatistics() {
        return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
    }
}
