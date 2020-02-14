package net.potion.client.render;

import net.hypnosis.render.Tessellator;
import net.potion.block.Block;
import net.potion.client.PotionClient;
import net.potion.client.render.entity.RenderBlocks;
import net.potion.client.render.entity.RenderSorter;
import net.potion.entity.*;
import net.potion.entity.monster.EntitySlimeFX;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemRecord;
import net.potion.item.ItemStack;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityRenderer;
import net.potion.util.*;
import net.potion.world.IWorldAccess;
import net.potion.world.World;
import net.potion.world.WorldRenderer;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.*;

public class RenderGlobal implements IWorldAccess {
    public List<TileEntity> tileEntities = new ArrayList();
    public float damagePartialTime;
    int[] dummyBuf50k = new int['\uc350'];
    IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
    int dummyInt0 = 0;
    int glDummyList = GLAllocation.generateDisplayLists(1);
    double prevSortX = -9999.0D;
    double prevSortY = -9999.0D;
    double prevSortZ = -9999.0D;
    int frustrumCheckOffset = 0;
    private World worldObj;
    private RenderEngine renderEngine;
    private List<WorldRenderer> worldRenderersToUpdate = new ArrayList<>();
    private WorldRenderer[] sortedWorldRenderers;
    private WorldRenderer[] worldRenderers;
    private int renderChunksWide;
    private int renderChunksTall;
    private int renderChunksDeep;
    private int glRenderListBase;
    private PotionClient potion;
    private RenderBlocks globalRenderBlocks;
    private IntBuffer glOcclusionQueryBase;
    private boolean occlusionEnabled = false;
    private int cloudOffsetX = 0;
    private int starGLCallList;
    private int glSkyList;
    private int glSkyList2;
    private int minBlockX;
    private int minBlockY;
    private int minBlockZ;
    private int maxBlockX;
    private int maxBlockY;
    private int maxBlockZ;
    private int renderDistance = -1;
    private int renderEntitiesStartupCounter = 2;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private int renderersLoaded;
    private int renderersBeingClipped;
    private int renderersBeingOccluded;
    private int renderersBeingRendered;
    private int renderersSkippingRenderPass;
    private int worldRenderersCheckIndex;
    private List<WorldRenderer> glRenderLists = new ArrayList<>();
    private RenderList[] allRenderLists = new RenderList[]{new RenderList(), new RenderList(), new RenderList(), new RenderList()};

    public RenderGlobal(PotionClient potion, RenderEngine renderEngine) {
        this.potion = potion;
        this.renderEngine = renderEngine;
        byte var3 = 64;
        this.glRenderListBase = GLAllocation.generateDisplayLists(var3 * var3 * var3 * 3);
        this.occlusionEnabled = potion.getOpenGlCapsChecker().checkARBOcclusion();
        if (this.occlusionEnabled) {
            this.occlusionResult.clear();
            this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(var3 * var3 * var3);
            this.glOcclusionQueryBase.clear();
            this.glOcclusionQueryBase.position(0);
            this.glOcclusionQueryBase.limit(var3 * var3 * var3);
            ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
        }

        this.starGLCallList = GLAllocation.generateDisplayLists(3);
        GL11.glPushMatrix();
        GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        Tessellator tess = Tessellator.INSTANCE;
        this.glSkyList = this.starGLCallList + 1;
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        byte var6 = 64;
        int var7 = 256 / var6 + 2;
        float var5 = 16.0F;

        for (int var8 = -var6 * var7; var8 <= var6 * var7; var8 += var6) {
            for (int var9 = -var6 * var7; var9 <= var6 * var7; var9 += var6) {
                tess.startDrawingQuads();
                tess.addVertex(var8, var5, var9);
                tess.addVertex(var8 + var6, var5, var9);
                tess.addVertex(var8 + var6, var5, var9 + var6);
                tess.addVertex(var8, var5, var9 + var6);
                tess.draw();
            }
        }

        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 2;
        GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
        var5 = -16.0F;
        tess.startDrawingQuads();

        for (int var11 = -var6 * var7; var11 <= var6 * var7; var11 += var6) {
            for (int var12 = -var6 * var7; var12 <= var6 * var7; var12 += var6) {
                tess.addVertex(var11 + var6, var5, var12);
                tess.addVertex(var11, var5, var12);
                tess.addVertex(var11, var5, var12 + var6);
                tess.addVertex(var11 + var6, var5, var12 + var6);
            }
        }

        tess.draw();
        GL11.glEndList();
    }

    private void renderStars() {
        Random rand = new Random(10842L);
        Tessellator var2 = Tessellator.INSTANCE;
        var2.startDrawingQuads();

        for (int var3 = 0; var3 < 1500; ++var3) {
            double var4 = rand.nextFloat() * 2.0F - 1.0F;
            double var6 = rand.nextFloat() * 2.0F - 1.0F;
            double var8 = rand.nextFloat() * 2.0F - 1.0F;
            double var10 = 0.25F + rand.nextFloat() * 0.25F;
            double var12 = var4 * var4 + var6 * var6 + var8 * var8;
            if (var12 < 1.0D && var12 > 0.01D) {
                var12 = 1.0D / Math.sqrt(var12);
                var4 = var4 * var12;
                var6 = var6 * var12;
                var8 = var8 * var12;
                double var14 = var4 * 100.0D;
                double var16 = var6 * 100.0D;
                double var18 = var8 * 100.0D;
                double var20 = Math.atan2(var4, var8);
                double var22 = Math.sin(var20);
                double var24 = Math.cos(var20);
                double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
                double var28 = Math.sin(var26);
                double var30 = Math.cos(var26);
                double var32 = rand.nextDouble() * 3.141592653589793D * 2.0D;
                double var34 = Math.sin(var32);
                double var36 = Math.cos(var32);

                for (int var38 = 0; var38 < 4; ++var38) {
                    double var39 = 0.0D;
                    double var41 = (double) ((var38 & 2) - 1) * var10;
                    double var43 = (double) ((var38 + 1 & 2) - 1) * var10;
                    double var47 = var41 * var36 - var43 * var34;
                    double var49 = var43 * var36 + var41 * var34;
                    double var53 = var47 * var28 + var39 * var30;
                    double var55 = var39 * var28 - var47 * var30;
                    double var57 = var55 * var22 - var49 * var24;
                    double var61 = var49 * var22 + var55 * var24;
                    var2.addVertex(var14 + var57, var16 + var53, var18 + var61);
                }
            }
        }

        var2.draw();
    }

    public void changeWorld(World world) {
        if (this.worldObj != null) {
            this.worldObj.removeWorldAccess(this);
        }

        this.prevSortX = -9999.0D;
        this.prevSortY = -9999.0D;
        this.prevSortZ = -9999.0D;
        RenderManager.instance.setWorld(world);
        this.worldObj = world;
        this.globalRenderBlocks = new RenderBlocks(world);
        if (world != null) {
            world.addWorldAccess(this);
            this.loadRenderers();
        }

    }

    public void loadRenderers() {
        Block.LEAVES.setGraphicsLevel(this.potion.gameSettings.fancyGraphics);
        this.renderDistance = this.potion.gameSettings.renderDistance;
        if (this.worldRenderers != null) {
            for (int i = 0; i < this.worldRenderers.length; ++i) {
                this.worldRenderers[i].func_1204_c();
            }
        }

        int var7 = 64 << 3 - this.renderDistance;
        if (var7 > 400) {
            var7 = 400;
        }

        this.renderChunksWide = var7 / 16 + 1;
        this.renderChunksTall = 8;
        this.renderChunksDeep = var7 / 16 + 1;
        this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        int var2 = 0;
        int var3 = 0;
        this.minBlockX = 0;
        this.minBlockY = 0;
        this.minBlockZ = 0;
        this.maxBlockX = this.renderChunksWide;
        this.maxBlockY = this.renderChunksTall;
        this.maxBlockZ = this.renderChunksDeep;

        for (int i = 0; i < this.worldRenderersToUpdate.size(); ++i) {
            this.worldRenderersToUpdate.get(i).needsUpdate = false;
        }

        this.worldRenderersToUpdate.clear();
        this.tileEntities.clear();

        for (int var8 = 0; var8 < this.renderChunksWide; ++var8) {
            for (int var5 = 0; var5 < this.renderChunksTall; ++var5) {
                for (int var6 = 0; var6 < this.renderChunksDeep; ++var6) {
                    this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8] = new WorldRenderer(this.worldObj, this.tileEntities, var8 * 16, var5 * 16, var6 * 16, 16, this.glRenderListBase + var2);
                    if (this.occlusionEnabled) {
                        this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8].glOcclusionQuery = this.glOcclusionQueryBase.get(var3);
                    }

                    this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8].isWaitingOnOcclusionQuery = false;
                    this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8].isVisible = true;
                    this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8].isInFrustum = true;
                    this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8].chunkIndex = var3++;
                    this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8].markDirty();
                    this.sortedWorldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8] = this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8];
                    this.worldRenderersToUpdate.add(this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var8]);
                    var2 += 3;
                }
            }
        }

        if (this.worldObj != null) {
            EntityLiving entity = this.potion.renderViewEntity;
            if (entity != null) {
                this.markRenderersForNewPosition(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ));
                Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entity));
            }
        }

        this.renderEntitiesStartupCounter = 2;
    }

    public void renderEntities(Vec3D vec, ICamera camera, float delta) {
        if (this.renderEntitiesStartupCounter > 0) {
            --this.renderEntitiesStartupCounter;
        } else {
            TileEntityRenderer.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.potion.fontRenderer, this.potion.renderViewEntity, delta);
            RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.potion.fontRenderer, this.potion.renderViewEntity, this.potion.gameSettings, delta);
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            EntityLiving var4 = this.potion.renderViewEntity;
            RenderManager.renderPosX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double) delta;
            RenderManager.renderPosY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double) delta;
            RenderManager.renderPosZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double) delta;
            TileEntityRenderer.staticPlayerX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double) delta;
            TileEntityRenderer.staticPlayerY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double) delta;
            TileEntityRenderer.staticPlayerZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double) delta;
            List var5 = this.worldObj.getLoadedEntityList();
            this.countEntitiesTotal = var5.size();

            for (int var6 = 0; var6 < this.worldObj.weatherEffects.size(); ++var6) {
                Entity var7 = this.worldObj.weatherEffects.get(var6);
                ++this.countEntitiesRendered;
                if (var7.isInRangeToRenderVec3D(vec)) {
                    RenderManager.instance.renderEntity(var7, delta);
                }
            }

            for (int var9 = 0; var9 < var5.size(); ++var9) {
                Entity entity = (Entity) var5.get(var9);
                if (entity.isInRangeToRenderVec3D(vec)
                        && (entity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entity.boundingBox))
                        && (entity != this.potion.renderViewEntity || this.potion.gameSettings.thirdPersonView || this.potion.renderViewEntity.isPlayerSleeping())) {
                    int var8 = MathHelper.floor(entity.posY);
                    if (var8 < 0) {
                        var8 = 0;
                    }

                    if (var8 >= 128) {
                        var8 = 127;
                    }

                    if (this.worldObj.blockExists(MathHelper.floor(entity.posX), var8, MathHelper.floor(entity.posZ))) {
                        ++this.countEntitiesRendered;
                        RenderManager.instance.renderEntity(entity, delta);
                    }
                }
            }

            for (int var10 = 0; var10 < this.tileEntities.size(); ++var10) {
                TileEntityRenderer.instance.renderTileEntity(this.tileEntities.get(var10), delta);
            }

        }
    }

    public String getDebugInfoRenders() {
        return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
    }

    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

    private void markRenderersForNewPosition(int var1, int var2, int var3) {
        var1 = var1 - 8;
        var2 = var2 - 8;
        var3 = var3 - 8;
        this.minBlockX = Integer.MAX_VALUE;
        this.minBlockY = Integer.MAX_VALUE;
        this.minBlockZ = Integer.MAX_VALUE;
        this.maxBlockX = Integer.MIN_VALUE;
        this.maxBlockY = Integer.MIN_VALUE;
        this.maxBlockZ = Integer.MIN_VALUE;
        int var4 = this.renderChunksWide * 16;
        int var5 = var4 / 2;

        for (int var6 = 0; var6 < this.renderChunksWide; ++var6) {
            int x = var6 * 16;
            int var8 = x + var5 - var1;
            if (var8 < 0) {
                var8 -= var4 - 1;
            }

            var8 = var8 / var4;
            x = x - var8 * var4;
            if (x < this.minBlockX) {
                this.minBlockX = x;
            }

            if (x > this.maxBlockX) {
                this.maxBlockX = x;
            }

            for (int var9 = 0; var9 < this.renderChunksDeep; ++var9) {
                int z = var9 * 16;
                int var11 = z + var5 - var3;
                if (var11 < 0) {
                    var11 -= var4 - 1;
                }

                var11 = var11 / var4;
                z = z - var11 * var4;
                if (z < this.minBlockZ) {
                    this.minBlockZ = z;
                }

                if (z > this.maxBlockZ) {
                    this.maxBlockZ = z;
                }

                for (int var12 = 0; var12 < this.renderChunksTall; ++var12) {
                    int y = var12 * 16;
                    if (y < this.minBlockY) {
                        this.minBlockY = y;
                    }

                    if (y > this.maxBlockY) {
                        this.maxBlockY = y;
                    }

                    WorldRenderer renderer = this.worldRenderers[(var9 * this.renderChunksTall + var12) * this.renderChunksWide + var6];
                    boolean needsUpdate = renderer.needsUpdate;
                    renderer.setPosition(x, y, z);
                    if (!needsUpdate && renderer.needsUpdate) {
                        this.worldRenderersToUpdate.add(renderer);
                    }
                }
            }
        }

    }

    public int sortAndRender(EntityLiving var1, int var2, double var3) {
        for (int var5 = 0; var5 < 10; ++var5) {
            this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
            WorldRenderer var6 = this.worldRenderers[this.worldRenderersCheckIndex];
            if (var6.needsUpdate && !this.worldRenderersToUpdate.contains(var6)) {
                this.worldRenderersToUpdate.add(var6);
            }
        }

        if (this.potion.gameSettings.renderDistance != this.renderDistance) {
            this.loadRenderers();
        }

        if (var2 == 0) {
            this.renderersLoaded = 0;
            this.renderersBeingClipped = 0;
            this.renderersBeingOccluded = 0;
            this.renderersBeingRendered = 0;
            this.renderersSkippingRenderPass = 0;
        }

        double var33 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var3;
        double var7 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var3;
        double var9 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var3;
        double var11 = var1.posX - this.prevSortX;
        double var13 = var1.posY - this.prevSortY;
        double var15 = var1.posZ - this.prevSortZ;
        if (var11 * var11 + var13 * var13 + var15 * var15 > 16.0D) {
            this.prevSortX = var1.posX;
            this.prevSortY = var1.posY;
            this.prevSortZ = var1.posZ;
            this.markRenderersForNewPosition(MathHelper.floor(var1.posX), MathHelper.floor(var1.posY), MathHelper.floor(var1.posZ));
            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var1));
        }

        RenderHelper.disableStandardItemLighting();
        int var17 = 0;
        if (this.occlusionEnabled && this.potion.gameSettings.advancedOpengl && !this.potion.gameSettings.anaglyph && var2 == 0) {
            int var18 = 0;
            int var19 = 16;
            this.checkOcclusionQueryResult(var18, var19);

            for (int var20 = var18; var20 < var19; ++var20) {
                this.sortedWorldRenderers[var20].isVisible = true;
            }

            var17 = var17 + this.renderSortedRenderers(var18, var19, var2, var3);

            do {
                var18 = var19;
                var19 *= 2;
                if (var19 > this.sortedWorldRenderers.length) {
                    var19 = this.sortedWorldRenderers.length;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_FOG);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                this.checkOcclusionQueryResult(var18, var19);
                GL11.glPushMatrix();
                float var36 = 0.0F;
                float var21 = 0.0F;
                float var22 = 0.0F;

                for (int var23 = var18; var23 < var19; ++var23) {
                    if (this.sortedWorldRenderers[var23].skipAllRenderPasses()) {
                        this.sortedWorldRenderers[var23].isInFrustum = false;
                    } else {
                        if (!this.sortedWorldRenderers[var23].isInFrustum) {
                            this.sortedWorldRenderers[var23].isVisible = true;
                        }

                        if (this.sortedWorldRenderers[var23].isInFrustum && !this.sortedWorldRenderers[var23].isWaitingOnOcclusionQuery) {
                            float var24 = MathHelper.sqrt(this.sortedWorldRenderers[var23].distanceToEntitySquared(var1));
                            int var25 = (int) (1.0F + var24 / 128.0F);
                            if (this.cloudOffsetX % var25 == var23 % var25) {
                                WorldRenderer var26 = this.sortedWorldRenderers[var23];
                                float var27 = (float) ((double) var26.posXMinus - var33);
                                float var28 = (float) ((double) var26.posYMinus - var7);
                                float var29 = (float) ((double) var26.posZMinus - var9);
                                float var30 = var27 - var36;
                                float var31 = var28 - var21;
                                float var32 = var29 - var22;
                                if (var30 != 0.0F || var31 != 0.0F || var32 != 0.0F) {
                                    GL11.glTranslatef(var30, var31, var32);
                                    var36 += var30;
                                    var21 += var31;
                                    var22 += var32;
                                }

                                ARBOcclusionQuery.glBeginQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB, this.sortedWorldRenderers[var23].glOcclusionQuery);
                                this.sortedWorldRenderers[var23].callOcclusionQueryList();
                                ARBOcclusionQuery.glEndQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB);
                                this.sortedWorldRenderers[var23].isWaitingOnOcclusionQuery = true;
                            }
                        }
                    }
                }

                GL11.glPopMatrix();
                if (this.potion.gameSettings.anaglyph) {
                    if (EntityRenderer.anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_FOG);
                var17 += this.renderSortedRenderers(var18, var19, var2, var3);
            } while (var19 < this.sortedWorldRenderers.length);
        } else {
            var17 = var17 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, var2, var3);
        }

        return var17;
    }

    private void checkOcclusionQueryResult(int var1, int var2) {
        for (int i = var1; i < var2; ++i) {
            if (!this.sortedWorldRenderers[i].isWaitingOnOcclusionQuery)
                continue;

            this.occlusionResult.clear();
            ARBOcclusionQuery.glGetQueryObjectuivARB(this.sortedWorldRenderers[i].glOcclusionQuery, ARBOcclusionQuery.GL_QUERY_RESULT_AVAILABLE_ARB, this.occlusionResult);
            if (this.occlusionResult.get(0) == 0)
                continue;

            this.sortedWorldRenderers[i].isWaitingOnOcclusionQuery = false;
            this.occlusionResult.clear();
            ARBOcclusionQuery.glGetQueryObjectuivARB(this.sortedWorldRenderers[i].glOcclusionQuery, ARBOcclusionQuery.GL_QUERY_RESULT_ARB, this.occlusionResult);
            this.sortedWorldRenderers[i].isVisible = this.occlusionResult.get(0) != 0;
        }

    }

    private int renderSortedRenderers(int begin, int end, int var3, double var4) {
        this.glRenderLists.clear();
        int var6 = 0;

        for (int i = begin; i < end; ++i) {
            if (var3 == 0) {
                ++this.renderersLoaded;
                if (this.sortedWorldRenderers[i].skipRenderPass[var3]) {
                    ++this.renderersSkippingRenderPass;
                } else if (!this.sortedWorldRenderers[i].isInFrustum) {
                    ++this.renderersBeingClipped;
                } else if (this.occlusionEnabled && !this.sortedWorldRenderers[i].isVisible) {
                    ++this.renderersBeingOccluded;
                } else {
                    ++this.renderersBeingRendered;
                }
            }

            if (!this.sortedWorldRenderers[i].skipRenderPass[var3] && this.sortedWorldRenderers[i].isInFrustum && (!this.occlusionEnabled || this.sortedWorldRenderers[i].isVisible)) {
                int var8 = this.sortedWorldRenderers[i].getGLCallListForPass(var3);
                if (var8 >= 0) {
                    this.glRenderLists.add(this.sortedWorldRenderers[i]);
                    ++var6;
                }
            }
        }

        EntityLiving entity = this.potion.renderViewEntity;
        double var20 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * var4;
        double var10 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * var4;
        double var12 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * var4;
        int var14 = 0;

        for (int var15 = 0; var15 < this.allRenderLists.length; ++var15) {
            this.allRenderLists[var15].func_859_b();
        }

        for (int var21 = 0; var21 < this.glRenderLists.size(); ++var21) {
            WorldRenderer var16 = this.glRenderLists.get(var21);
            int var17 = -1;

            for (int var18 = 0; var18 < var14; ++var18) {
                if (this.allRenderLists[var18].func_862_a(var16.posXMinus, var16.posYMinus, var16.posZMinus)) {
                    var17 = var18;
                }
            }

            if (var17 < 0) {
                var17 = var14++;
                this.allRenderLists[var17].func_861_a(var16.posXMinus, var16.posYMinus, var16.posZMinus, var20, var10, var12);
            }

            this.allRenderLists[var17].func_858_a(var16.getGLCallListForPass(var3));
        }

        this.renderAllRenderLists(var3, var4);
        return var6;
    }

    public void renderAllRenderLists(int var1, double var2) {
        for (int i = 0; i < this.allRenderLists.length; ++i) {
            this.allRenderLists[i].func_860_a();
        }
    }

    public void updateClouds() {
        ++this.cloudOffsetX;
    }

    public void renderSky(float var1) {
        if (this.potion.theWorld.worldProvider.isNether)
            return;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Vec3D vec = this.worldObj.func_4079_a(this.potion.renderViewEntity, var1);
        float x = (float) vec.xCoord;
        float y = (float) vec.yCoord;
        float z = (float) vec.zCoord;
        if (this.potion.gameSettings.anaglyph) {
            float tempX = (x * 30.0F + y * 59.0F + z * 11.0F) / 100.0F;
            float tempY = (x * 30.0F + y * 70.0F) / 100.0F;
            float tempZ = (x * 30.0F + z * 70.0F) / 100.0F;
            x = tempX;
            y = tempY;
            z = tempZ;
        }

        GL11.glColor3f(x, y, z);
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(x, y, z);
        GL11.glCallList(this.glSkyList);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        float[] var18 = this.worldObj.worldProvider.calcSunriseSunsetColors(this.worldObj.getCelestialAngle(var1), var1);
        if (var18 != null) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            float var20 = this.worldObj.getCelestialAngle(var1);
            GL11.glRotatef(var20 > 0.5F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            float var9 = var18[0];
            float var10 = var18[1];
            float var11 = var18[2];
            if (this.potion.gameSettings.anaglyph) {
                float var12 = (var9 * 30.0F + var10 * 59.0F + var11 * 11.0F) / 100.0F;
                float var13 = (var9 * 30.0F + var10 * 70.0F) / 100.0F;
                float var14 = (var9 * 30.0F + var11 * 70.0F) / 100.0F;
                var9 = var12;
                var10 = var13;
                var11 = var14;
            }

            tess.startDrawing(GL11.GL_TRIANGLE_FAN);
            tess.setColorRGBA_F(var9, var10, var11, var18[3]);
            tess.addVertex(0.0D, 100.0D, 0.0D);
            byte var26 = 16;
            tess.setColorRGBA_F(var18[0], var18[1], var18[2], 0.0F);

            for (int var28 = 0; var28 <= var26; ++var28) {
                float var29 = (float) var28 * 3.1415927F * 2.0F / (float) var26;
                float var15 = MathHelper.sin(var29);
                float var16 = MathHelper.cos(var29);
                tess.addVertex(var15 * 120.0F, var16 * 120.0F, -var16 * 40.0F * var18[3]);
            }

            tess.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glPushMatrix();
        float var19 = 1.0F - this.worldObj.getRainStrength(var1);
        float var21 = 0.0F;
        float var22 = 0.0F;
        float var23 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var19);
        GL11.glTranslatef(var21, var22, var23);
        GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(this.worldObj.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
        float var24 = 30.0F;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
        tess.startDrawingQuads();
        tess.addVertexWithUV(-var24, 100.0D, -var24, 0.0D, 0.0D);
        tess.addVertexWithUV(var24, 100.0D, -var24, 1.0D, 0.0D);
        tess.addVertexWithUV(var24, 100.0D, var24, 1.0D, 1.0D);
        tess.addVertexWithUV(-var24, 100.0D, var24, 0.0D, 1.0D);
        tess.draw();
        var24 = 20.0F;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
        tess.startDrawingQuads();
        tess.addVertexWithUV(-var24, -100.0D, var24, 1.0D, 1.0D);
        tess.addVertexWithUV(var24, -100.0D, var24, 0.0D, 1.0D);
        tess.addVertexWithUV(var24, -100.0D, -var24, 0.0D, 0.0D);
        tess.addVertexWithUV(-var24, -100.0D, -var24, 1.0D, 0.0D);
        tess.draw();
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        float starBrightness = this.worldObj.getStarBrightness(var1) * var19;
        if (starBrightness > 0.0F) {
            GL11.glColor4f(starBrightness, starBrightness, starBrightness, starBrightness);
            GL11.glCallList(this.starGLCallList);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        if (this.worldObj.worldProvider.func_28112_c()) {
            GL11.glColor3f(x * 0.2F + 0.04F, y * 0.2F + 0.04F, z * 0.6F + 0.1F);
        } else {
            GL11.glColor3f(x, y, z);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glCallList(this.glSkyList2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
    }

    public void renderClouds(float var1) {
        if (this.potion.theWorld.worldProvider.isNether)
            return;

        if (this.potion.gameSettings.fancyGraphics) {
            this.renderCloudsFancy(var1);
        } else {
            GL11.glDisable(GL11.GL_CULL_FACE);
            float var2 = (float) (this.potion.renderViewEntity.lastTickPosY + (this.potion.renderViewEntity.posY - this.potion.renderViewEntity.lastTickPosY) * (double) var1);
            byte var3 = 32;
            int var4 = 256 / var3;
            Tessellator tess = Tessellator.INSTANCE;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/environment/clouds.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Vec3D vec = this.worldObj.func_628_d(var1);

            float xCoord = (float) vec.xCoord;
            float yCoord = (float) vec.yCoord;
            float zCoord = (float) vec.zCoord;
            if (this.potion.gameSettings.anaglyph) {
                float tempX = (xCoord * 30.0F + yCoord * 59.0F + zCoord * 11.0F) / 100.0F;
                float tempY = (xCoord * 30.0F + yCoord * 70.0F) / 100.0F;
                float tempZ = (xCoord * 30.0F + zCoord * 70.0F) / 100.0F;
                xCoord = tempX;
                yCoord = tempY;
                zCoord = tempZ;
            }

            float var22 = 4.8828125E-4F;
            double var23 = this.potion.renderViewEntity.prevPosX + (this.potion.renderViewEntity.posX - this.potion.renderViewEntity.prevPosX) * (double) var1 + (double) (((float) this.cloudOffsetX + var1) * 0.03F);
            double var13 = this.potion.renderViewEntity.prevPosZ + (this.potion.renderViewEntity.posZ - this.potion.renderViewEntity.prevPosZ) * (double) var1;
            int var15 = MathHelper.floor(var23 / 2048.0D);
            int var16 = MathHelper.floor(var13 / 2048.0D);
            var23 = var23 - (double) (var15 * GL11.GL_EXP);
            var13 = var13 - (double) (var16 * GL11.GL_EXP);
            float var17 = this.worldObj.worldProvider.getCloudHeight() - var2 + 0.33F;
            float var18 = (float) (var23 * (double) var22);
            float var19 = (float) (var13 * (double) var22);
            tess.startDrawingQuads();
            tess.setColorRGBA_F(xCoord, yCoord, zCoord, 0.8F);

            for (int var20 = -var3 * var4; var20 < var3 * var4; var20 += var3) {
                for (int var21 = -var3 * var4; var21 < var3 * var4; var21 += var3) {
                    tess.addVertexWithUV(var20, var17, var21 + var3, (float) (var20) * var22 + var18, (float) (var21 + var3) * var22 + var19);
                    tess.addVertexWithUV(var20 + var3, var17, var21 + var3, (float) (var20 + var3) * var22 + var18, (float) (var21 + var3) * var22 + var19);
                    tess.addVertexWithUV(var20 + var3, var17, var21, (float) (var20 + var3) * var22 + var18, (float) (var21) * var22 + var19);
                    tess.addVertexWithUV(var20, var17, var21, (float) (var20) * var22 + var18, (float) (var21) * var22 + var19);
                }
            }

            tess.draw();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }

    public boolean func_27307_a(double var1, double var3, double var5, float var7) {
        return false;
    }

    public void renderCloudsFancy(float var1) {
        GL11.glDisable(GL11.GL_CULL_FACE);
        float var2 = (float) (this.potion.renderViewEntity.lastTickPosY + (this.potion.renderViewEntity.posY - this.potion.renderViewEntity.lastTickPosY) * (double) var1);
        Tessellator tess = Tessellator.INSTANCE;
        float var4 = 12.0F;
        float var5 = 4.0F;
        double var6 = (this.potion.renderViewEntity.prevPosX + (this.potion.renderViewEntity.posX - this.potion.renderViewEntity.prevPosX) * (double) var1 + (double) (((float) this.cloudOffsetX + var1) * 0.03F)) / (double) var4;
        double var8 = (this.potion.renderViewEntity.prevPosZ + (this.potion.renderViewEntity.posZ - this.potion.renderViewEntity.prevPosZ) * (double) var1) / (double) var4 + 0.33000001311302185D;
        float var10 = this.worldObj.worldProvider.getCloudHeight() - var2 + 0.33F;
        int var11 = MathHelper.floor(var6 / 2048.0D);
        int var12 = MathHelper.floor(var8 / 2048.0D);
        var6 = var6 - (double) (var11 * GL11.GL_EXP);
        var8 = var8 - (double) (var12 * GL11.GL_EXP);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vec3D vec = this.worldObj.func_628_d(var1);
        float xCoord = (float) vec.xCoord;
        float yCoord = (float) vec.yCoord;
        float zCoord = (float) vec.zCoord;
        if (this.potion.gameSettings.anaglyph) {
            float tempX = (xCoord * 30.0F + yCoord * 59.0F + zCoord * 11.0F) / 100.0F;
            float tempY = (xCoord * 30.0F + yCoord * 70.0F) / 100.0F;
            float tempZ = (xCoord * 30.0F + zCoord * 70.0F) / 100.0F;
            xCoord = tempX;
            yCoord = tempY;
            zCoord = tempZ;
        }

        float var35 = (float) (var6 * 0.0D);
        float var37 = (float) (var8 * 0.0D);
        float var39 = 0.00390625F;
        var35 = (float) MathHelper.floor(var6) * var39;
        var37 = (float) MathHelper.floor(var8) * var39;
        float var20 = (float) (var6 - (double) MathHelper.floor(var6));
        float var21 = (float) (var8 - (double) MathHelper.floor(var8));
        byte var22 = 8;
        byte var23 = 3;
        float var24 = 9.765625E-4F;
        GL11.glScalef(var4, 1.0F, var4);

        for (int var25 = 0; var25 < 2; ++var25) {
            if (var25 == 0) {
                GL11.glColorMask(false, false, false, false);
            } else if (this.potion.gameSettings.anaglyph) {
                if (EntityRenderer.anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, true);
                } else {
                    GL11.glColorMask(true, false, false, true);
                }
            } else {
                GL11.glColorMask(true, true, true, true);
            }

            for (int var26 = -var23 + 1; var26 <= var23; ++var26) {
                for (int var27 = -var23 + 1; var27 <= var23; ++var27) {
                    tess.startDrawingQuads();
                    float var28 = (float) (var26 * var22);
                    float var29 = (float) (var27 * var22);
                    float var30 = var28 - var20;
                    float var31 = var29 - var21;
                    if (var10 > -var5 - 1.0F) {
                        tess.setColorRGBA_F(xCoord * 0.7F, yCoord * 0.7F, zCoord * 0.7F, 0.8F);
                        tess.setNormal(0.0F, -1.0F, 0.0F);
                        tess.addVertexWithUV(var30 + 0.0F, var10 + 0.0F, var31 + (float) var22, (var28 + 0.0F) * var39 + var35, (var29 + (float) var22) * var39 + var37);
                        tess.addVertexWithUV(var30 + (float) var22, var10 + 0.0F, var31 + (float) var22, (var28 + (float) var22) * var39 + var35, (var29 + (float) var22) * var39 + var37);
                        tess.addVertexWithUV(var30 + (float) var22, var10 + 0.0F, var31 + 0.0F, (var28 + (float) var22) * var39 + var35, (var29 + 0.0F) * var39 + var37);
                        tess.addVertexWithUV(var30 + 0.0F, var10 + 0.0F, var31 + 0.0F, (var28 + 0.0F) * var39 + var35, (var29 + 0.0F) * var39 + var37);
                    }

                    if (var10 <= var5 + 1.0F) {
                        tess.setColorRGBA_F(xCoord, yCoord, zCoord, 0.8F);
                        tess.setNormal(0.0F, 1.0F, 0.0F);
                        tess.addVertexWithUV(var30 + 0.0F, var10 + var5 - var24, var31 + (float) var22, (var28 + 0.0F) * var39 + var35, (var29 + (float) var22) * var39 + var37);
                        tess.addVertexWithUV(var30 + (float) var22, var10 + var5 - var24, var31 + (float) var22, (var28 + (float) var22) * var39 + var35, (var29 + (float) var22) * var39 + var37);
                        tess.addVertexWithUV(var30 + (float) var22, var10 + var5 - var24, var31 + 0.0F, (var28 + (float) var22) * var39 + var35, (var29 + 0.0F) * var39 + var37);
                        tess.addVertexWithUV(var30 + 0.0F, var10 + var5 - var24, var31 + 0.0F, (var28 + 0.0F) * var39 + var35, (var29 + 0.0F) * var39 + var37);
                    }

                    tess.setColorRGBA_F(xCoord * 0.9F, yCoord * 0.9F, zCoord * 0.9F, 0.8F);
                    if (var26 > -1) {
                        tess.setNormal(-1.0F, 0.0F, 0.0F);

                        for (int var32 = 0; var32 < var22; ++var32) {
                            tess.addVertexWithUV(var30 + (float) var32 + 0.0F, var10 + 0.0F, var31 + (float) var22, (var28 + (float) var32 + 0.5F) * var39 + var35, (var29 + (float) var22) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var32 + 0.0F, var10 + var5, var31 + (float) var22, (var28 + (float) var32 + 0.5F) * var39 + var35, (var29 + (float) var22) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var32 + 0.0F, var10 + var5, var31 + 0.0F, (var28 + (float) var32 + 0.5F) * var39 + var35, (var29 + 0.0F) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var32 + 0.0F, var10 + 0.0F, var31 + 0.0F, (var28 + (float) var32 + 0.5F) * var39 + var35, (var29 + 0.0F) * var39 + var37);
                        }
                    }

                    if (var26 <= 1) {
                        tess.setNormal(1.0F, 0.0F, 0.0F);

                        for (int var40 = 0; var40 < var22; ++var40) {
                            tess.addVertexWithUV(var30 + (float) var40 + 1.0F - var24, var10 + 0.0F, var31 + (float) var22, (var28 + (float) var40 + 0.5F) * var39 + var35, (var29 + (float) var22) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var40 + 1.0F - var24, var10 + var5, var31 + (float) var22, (var28 + (float) var40 + 0.5F) * var39 + var35, (var29 + (float) var22) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var40 + 1.0F - var24, var10 + var5, var31 + 0.0F, (var28 + (float) var40 + 0.5F) * var39 + var35, (var29 + 0.0F) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var40 + 1.0F - var24, var10 + 0.0F, var31 + 0.0F, (var28 + (float) var40 + 0.5F) * var39 + var35, (var29 + 0.0F) * var39 + var37);
                        }
                    }

                    tess.setColorRGBA_F(xCoord * 0.8F, yCoord * 0.8F, zCoord * 0.8F, 0.8F);
                    if (var27 > -1) {
                        tess.setNormal(0.0F, 0.0F, -1.0F);

                        for (int var41 = 0; var41 < var22; ++var41) {
                            tess.addVertexWithUV(var30 + 0.0F, var10 + var5, var31 + (float) var41 + 0.0F, (var28 + 0.0F) * var39 + var35, (var29 + (float) var41 + 0.5F) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var22, var10 + var5, var31 + (float) var41 + 0.0F, (var28 + (float) var22) * var39 + var35, (var29 + (float) var41 + 0.5F) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var22, var10 + 0.0F, var31 + (float) var41 + 0.0F, (var28 + (float) var22) * var39 + var35, (var29 + (float) var41 + 0.5F) * var39 + var37);
                            tess.addVertexWithUV(var30 + 0.0F, var10 + 0.0F, var31 + (float) var41 + 0.0F, (var28 + 0.0F) * var39 + var35, (var29 + (float) var41 + 0.5F) * var39 + var37);
                        }
                    }

                    if (var27 <= 1) {
                        tess.setNormal(0.0F, 0.0F, 1.0F);

                        for (int var42 = 0; var42 < var22; ++var42) {
                            tess.addVertexWithUV(var30 + 0.0F, var10 + var5, var31 + (float) var42 + 1.0F - var24, (var28 + 0.0F) * var39 + var35, (var29 + (float) var42 + 0.5F) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var22, var10 + var5, var31 + (float) var42 + 1.0F - var24, (var28 + (float) var22) * var39 + var35, (var29 + (float) var42 + 0.5F) * var39 + var37);
                            tess.addVertexWithUV(var30 + (float) var22, var10 + 0.0F, var31 + (float) var42 + 1.0F - var24, (var28 + (float) var22) * var39 + var35, (var29 + (float) var42 + 0.5F) * var39 + var37);
                            tess.addVertexWithUV(var30 + 0.0F, var10 + 0.0F, var31 + (float) var42 + 1.0F - var24, (var28 + 0.0F) * var39 + var35, (var29 + (float) var42 + 0.5F) * var39 + var37);
                        }
                    }

                    tess.draw();
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public boolean updateRenderers(EntityLiving entity, boolean var2) {
        byte var4 = 2;
        RenderSorter var5 = new RenderSorter(entity);
        WorldRenderer[] var6 = new WorldRenderer[var4];
        ArrayList<WorldRenderer> var7 = null;
        int var8 = this.worldRenderersToUpdate.size();
        int var9 = 0;

        label169:
        for (int var10 = 0; var10 < var8; ++var10) {
            WorldRenderer var11 = this.worldRenderersToUpdate.get(var10);
            if (!var2) {
                if (var11.distanceToEntitySquared(entity) > 256.0F) {
                    int var12;
                    for (var12 = 0; var12 < var4 && (var6[var12] == null || var5.doCompare(var6[var12], var11) <= 0); ++var12) {
                    }

                    --var12;
                    if (var12 <= 0) {
                        continue;
                    }

                    int var13 = var12;

                    while (true) {
                        --var13;
                        if (var13 == 0) {
                            var6[var12] = var11;
                            continue label169;
                        }

                        var6[var13 - 1] = var6[var13];
                    }
                }
            } else if (!var11.isInFrustum) {
                continue;
            }

            if (var7 == null) {
                var7 = new ArrayList();
            }

            ++var9;
            var7.add(var11);
            this.worldRenderersToUpdate.set(var10, null);
        }

        if (var7 != null) {
            if (var7.size() > 1) {
                var7.sort(var5);
            }

            for (int var21 = var7.size() - 1; var21 >= 0; --var21) {
                WorldRenderer var23 = var7.get(var21);
                var23.updateRenderer();
                var23.needsUpdate = false;
            }
        }

        int var22 = 0;

        for (int var24 = var4 - 1; var24 >= 0; --var24) {
            WorldRenderer renderer = var6[var24];
            if (renderer != null) {
                if (!renderer.isInFrustum && var24 != var4 - 1) {
                    var6[var24] = null;
                    var6[0] = null;
                    break;
                }

                var6[var24].updateRenderer();
                var6[var24].needsUpdate = false;
                ++var22;
            }
        }

        int var25 = 0;
        int var28 = 0;

        for (int var29 = this.worldRenderersToUpdate.size(); var25 != var29; ++var25) {
            WorldRenderer var14 = this.worldRenderersToUpdate.get(var25);
            if (var14 != null) {
                boolean var15 = false;

                for (int var16 = 0; var16 < var4 && !var15; ++var16) {
                    if (var14 == var6[var16]) {
                        var15 = true;
                        break;
                    }
                }

                if (!var15) {
                    if (var28 != var25) {
                        this.worldRenderersToUpdate.set(var28, var14);
                    }

                    ++var28;
                }
            }
        }

        while (true) {
            --var25;
            if (var25 < var28) {
                return var8 == var9 + var22;
            }

            this.worldRenderersToUpdate.remove(var25);
        }
    }

    public void drawBlockBreaking(EntityPlayer player, MovingObjectPosition pos, int var3, ItemStack stack, float var5) {
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float) System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
        if (var3 == 0) {
            if (this.damagePartialTime > 0.0F) {
                GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
                int textureId = this.renderEngine.getTexture("/terrain.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                GL11.glPushMatrix();
                int blockId = this.worldObj.getBlockId(pos.blockX, pos.blockY, pos.blockZ);
                Block block = blockId > 0 ? Block.BLOCKS_LIST[blockId] : null;
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glPolygonOffset(-3.0F, -3.0F);
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                double var10 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) var5;
                double var12 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) var5;
                double var14 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) var5;
                if (block == null) {
                    block = Block.STONE;
                }

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                tessellator.startDrawingQuads();
                tessellator.setTranslationD(-var10, -var12, -var14);
                tessellator.disableColor();
                this.globalRenderBlocks.renderBlockUsingTexture(block, pos.blockX, pos.blockY, pos.blockZ, 240 + (int) (this.damagePartialTime * 10.0F));
                tessellator.draw();
                tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glPolygonOffset(0.0F, 0.0F);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
            }
        } else if (stack != null) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            float var16 = MathHelper.sin((float) System.currentTimeMillis() / 100.0F) * 0.2F + 0.8F;
            GL11.glColor4f(var16, var16, var16, MathHelper.sin((float) System.currentTimeMillis() / 200.0F) * 0.2F + 0.5F);
            int var17 = this.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var17);
            int bX = pos.blockX;
            int bY = pos.blockY;
            int bZ = pos.blockZ;
            if (pos.sideHit == 0) {
                --bY;
            }

            if (pos.sideHit == 1) {
                ++bY;
            }

            if (pos.sideHit == 2) {
                --bZ;
            }

            if (pos.sideHit == 3) {
                ++bZ;
            }

            if (pos.sideHit == 4) {
                --bX;
            }

            if (pos.sideHit == 5) {
                ++bX;
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    public void drawSelectionBox(EntityPlayer player, MovingObjectPosition pos, int var3, ItemStack stack, float var5) {
        if (var3 == 0 && pos.typeOfHit == EnumMovingObjectType.TILE) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            float expand = 0.002F;
            int blockId = this.worldObj.getBlockId(pos.blockX, pos.blockY, pos.blockZ);
            if (blockId > 0) {
                Block.BLOCKS_LIST[blockId].setBlockBoundsBasedOnState(this.worldObj, pos.blockX, pos.blockY, pos.blockZ);
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) var5;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) var5;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) var5;
                this.drawOutlinedBoundingBox(Block.BLOCKS_LIST[blockId]
                        .getSelectedBoundingBoxFromPool(this.worldObj, pos.blockX, pos.blockY, pos.blockZ)
                        .expand(expand, expand, expand)
                        .getOffsetBoundingBox(-x, -y, -z));
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }

    }

    private void drawOutlinedBoundingBox(AxisAlignedBB axis) {
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawing(GL11.GL_LINE_STRIP);
        tess.addVertex(axis.minX, axis.minY, axis.minZ);
        tess.addVertex(axis.maxX, axis.minY, axis.minZ);
        tess.addVertex(axis.maxX, axis.minY, axis.maxZ);
        tess.addVertex(axis.minX, axis.minY, axis.maxZ);
        tess.addVertex(axis.minX, axis.minY, axis.minZ);
        tess.draw();
        tess.startDrawing(GL11.GL_LINE_STRIP);
        tess.addVertex(axis.minX, axis.maxY, axis.minZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.minZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.maxZ);
        tess.addVertex(axis.minX, axis.maxY, axis.maxZ);
        tess.addVertex(axis.minX, axis.maxY, axis.minZ);
        tess.draw();
        tess.startDrawing(GL11.GL_LINES);
        tess.addVertex(axis.minX, axis.minY, axis.minZ);
        tess.addVertex(axis.minX, axis.maxY, axis.minZ);
        tess.addVertex(axis.maxX, axis.minY, axis.minZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.minZ);
        tess.addVertex(axis.maxX, axis.minY, axis.maxZ);
        tess.addVertex(axis.maxX, axis.maxY, axis.maxZ);
        tess.addVertex(axis.minX, axis.minY, axis.maxZ);
        tess.addVertex(axis.minX, axis.maxY, axis.maxZ);
        tess.draw();
    }

    public void func_949_a(int var1, int var2, int var3, int var4, int var5, int var6) {
        int var7 = MathHelper.bucketInt(var1, 16);
        int var8 = MathHelper.bucketInt(var2, 16);
        int var9 = MathHelper.bucketInt(var3, 16);
        int var10 = MathHelper.bucketInt(var4, 16);
        int var11 = MathHelper.bucketInt(var5, 16);
        int var12 = MathHelper.bucketInt(var6, 16);

        for (int var13 = var7; var13 <= var10; ++var13) {
            int var14 = var13 % this.renderChunksWide;
            if (var14 < 0) {
                var14 += this.renderChunksWide;
            }

            for (int var15 = var8; var15 <= var11; ++var15) {
                int var16 = var15 % this.renderChunksTall;
                if (var16 < 0) {
                    var16 += this.renderChunksTall;
                }

                for (int var17 = var9; var17 <= var12; ++var17) {
                    int var18 = var17 % this.renderChunksDeep;
                    if (var18 < 0) {
                        var18 += this.renderChunksDeep;
                    }

                    int var19 = (var18 * this.renderChunksTall + var16) * this.renderChunksWide + var14;
                    WorldRenderer var20 = this.worldRenderers[var19];
                    if (!var20.needsUpdate) {
                        this.worldRenderersToUpdate.add(var20);
                        var20.markDirty();
                    }
                }
            }
        }

    }

    @Override
    public void markBlockAndNeighborsNeedsUpdate(int var1, int var2, int var3) {
        this.func_949_a(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
    }

    @Override
    public void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
        this.func_949_a(var1 - 1, var2 - 1, var3 - 1, var4 + 1, var5 + 1, var6 + 1);
    }

    public void clipRenderersByFrustrum(ICamera camera, float var2) {
        for (int i = 0; i < this.worldRenderers.length; ++i) {
            if (!this.worldRenderers[i].skipAllRenderPasses() && (!this.worldRenderers[i].isInFrustum || (i + this.frustrumCheckOffset & 15) == 0)) {
                this.worldRenderers[i].updateInFrustrum(camera);
            }
        }

        ++this.frustrumCheckOffset;
    }

    @Override
    public void playRecord(String var1, int var2, int var3, int var4) {
        if (var1 != null)
            this.potion.ingameGUI.setRecordPlayingMessage("C418 - " + var1);

        this.potion.soundManager.playStreaming(var1, (float) var2, (float) var3, (float) var4, 1.0F, 1.0F);
    }

    @Override
    public void playSound(String var1, double var2, double var4, double var6, float var8, float var9) {
        float var10 = 16.0F;
        if (var8 > 1.0F) {
            var10 *= var8;
        }

        if (this.potion.renderViewEntity.getDistanceSq(var2, var4, var6) < (double) (var10 * var10)) {
            this.potion.soundManager.playSound(var1, (float) var2, (float) var4, (float) var6, var8, var9);
        }

    }

    @Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
        if (this.potion != null && this.potion.renderViewEntity != null && this.potion.effectRenderer != null) {
            double var14 = this.potion.renderViewEntity.posX - var2;
            double var16 = this.potion.renderViewEntity.posY - var4;
            double var18 = this.potion.renderViewEntity.posZ - var6;
            double var20 = 16.0D;
            if (var14 * var14 + var16 * var16 + var18 * var18 <= var20 * var20) {
                switch (var1) {
                    case "bubble":
                        this.potion.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                    case "smoke":
                        this.potion.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                    case "note":
                        this.potion.effectRenderer.addEffect(new EntityNoteFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                    case "portal":
                        this.potion.effectRenderer.addEffect(new EntityPortalFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                    case "explode":
                        this.potion.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                    case "flame":
                        this.potion.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                    case "lava":
                        this.potion.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, var2, var4, var6));
                        break;
                    case "footstep":
                        this.potion.effectRenderer.addEffect(new EntityFootStepFX(this.renderEngine, this.worldObj, var2, var4, var6));
                        break;
                    case "splash":
                        this.potion.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                    case "largesmoke":
                        this.potion.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, var2, var4, var6, var8, var10, var12, 2.5F));
                        break;
                    case "reddust":
                        this.potion.effectRenderer.addEffect(new EntityReddustFX(this.worldObj, var2, var4, var6, (float) var8, (float) var10, (float) var12));
                        break;
                    case "snowballpoof":
                        this.potion.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, var2, var4, var6, Item.SNOWBALL));
                        break;
                    case "snowshovel":
                        this.potion.effectRenderer.addEffect(new EntitySnowShovelFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                    case "slime":
                        this.potion.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, var2, var4, var6, Item.SLIMEBALL));
                        break;
                    case "heart":
                        this.potion.effectRenderer.addEffect(new EntityHeartFX(this.worldObj, var2, var4, var6, var8, var10, var12));
                        break;
                }

            }
        }
    }

    @Override
    public void obtainEntitySkin(Entity entity) {
        entity.updateCloak();
        if (entity.skinUrl != null) {
            this.renderEngine.obtainImageData(entity.skinUrl, new ImageBufferDownload());
        }

        if (entity.cloakUrl != null) {
            this.renderEngine.obtainImageData(entity.cloakUrl, new ImageBufferDownload());
        }

    }

    @Override
    public void releaseEntitySkin(Entity entity) {
        if (entity.skinUrl != null) {
            this.renderEngine.releaseImageData(entity.skinUrl);
        }

        if (entity.cloakUrl != null) {
            this.renderEngine.releaseImageData(entity.cloakUrl);
        }

    }

    @Override
    public void updateAllRenderers() {
        for (int i = 0; i < this.worldRenderers.length; ++i) {
            if (this.worldRenderers[i].isChunkLit && !this.worldRenderers[i].needsUpdate) {
                this.worldRenderersToUpdate.add(this.worldRenderers[i]);
                this.worldRenderers[i].markDirty();
            }
        }

    }

    @Override
    public void doNothingWithTileEntity(int var1, int var2, int var3, TileEntity var4) {
    }

    public void dispose() {
        GLAllocation.removeLists(this.glRenderListBase);
    }

    @Override
    public void func_28136_a(EntityPlayer player, int var2, int x, int y, int z, int var6) {
        Random rand = this.worldObj.rand;
        switch (var2) {
            case 1000:
                this.worldObj.playSoundEffect(x, y, z, "random.click", 1.0F, 1.0F);
                break;
            case 1001:
                this.worldObj.playSoundEffect(x, y, z, "random.click", 1.0F, 1.2F);
                break;
            case 1002:
                this.worldObj.playSoundEffect(x, y, z, "random.bow", 1.0F, 1.2F);
                break;
            case 1003:
                if (Math.random() < 0.5D) {
                    this.worldObj.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.door_open", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
                } else {
                    this.worldObj.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.door_close", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
                }
                break;
            case 1004:
                this.worldObj.playSoundEffect((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "random.fizz", 0.5F, 2.6F + (rand.nextFloat() - rand.nextFloat()) * 0.8F);
                break;
            case 1005:
                if (Item.ITEMS_LIST[var6] instanceof ItemRecord) {
                    this.worldObj.playRecord(((ItemRecord) Item.ITEMS_LIST[var6]).recordName, x, y, z);
                } else {
                    this.worldObj.playRecord(null, x, y, z);
                }
                break;
            case 2000:
                int var8 = var6 % 3 - 1;
                int var9 = var6 / 3 % 3 - 1;
                double var10 = (double) x + (double) var8 * 0.6D + 0.5D;
                double var12 = (double) y + 0.5D;
                double var14 = (double) z + (double) var9 * 0.6D + 0.5D;

                for (int var31 = 0; var31 < 10; ++var31) {
                    double var32 = rand.nextDouble() * 0.2D + 0.01D;
                    double var19 = var10 + (double) var8 * 0.01D + (rand.nextDouble() - 0.5D) * (double) var9 * 0.5D;
                    double var21 = var12 + (rand.nextDouble() - 0.5D) * 0.5D;
                    double var23 = var14 + (double) var9 * 0.01D + (rand.nextDouble() - 0.5D) * (double) var8 * 0.5D;
                    double var25 = (double) var8 * var32 + rand.nextGaussian() * 0.01D;
                    double var27 = -0.03D + rand.nextGaussian() * 0.01D;
                    double var29 = (double) var9 * var32 + rand.nextGaussian() * 0.01D;
                    this.spawnParticle("smoke", var19, var21, var23, var25, var27, var29);
                }

                return;
            case 2001:
                int var16 = var6 & 255;
                if (var16 > 0) {
                    Block block = Block.BLOCKS_LIST[var16];
                    this.potion.soundManager.playSound(block.stepSound.stepSoundDir(), (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                }

                this.potion.effectRenderer.addBlockDestroyEffects(x, y, z, var6 & 255, var6 >> 8 & 255);
        }

    }
}
