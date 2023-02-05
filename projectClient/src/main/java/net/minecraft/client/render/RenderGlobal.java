package net.minecraft.client.render;

import net.hypnosis.render.Tessellator;
import net.hypnosis.util.math.MathConstants;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.RenderBlocks;
import net.minecraft.client.render.entity.RenderSorter;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntitySlimeFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityRenderer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldRenderer;
import net.hypnosis.util.math.Vec3d;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    private World world;
    private RenderEngine renderEngine;
    private List<WorldRenderer> worldRenderersToUpdate = new ArrayList<>();
    private WorldRenderer[] sortedWorldRenderers;
    private WorldRenderer[] worldRenderers;
    private int renderChunksWide;
    private int renderChunksTall;
    private int renderChunksDeep;
    private int glRenderListBase;
    private MinecraftClient client;
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

    public RenderGlobal(MinecraftClient client, RenderEngine renderEngine) {
        this.client = client;
        this.renderEngine = renderEngine;
        byte var3 = 64;
        this.glRenderListBase = GLAllocation.generateDisplayLists(var3 * var3 * var3 * 3);
        this.occlusionEnabled = client.getOpenGlCapsChecker().checkARBOcclusion();
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
                double var32 = rand.nextDouble() * Math.PI * 2.0D;
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
        if (this.world != null) {
            this.world.removeWorldAccess(this);
        }

        this.prevSortX = -9999.0D;
        this.prevSortY = -9999.0D;
        this.prevSortZ = -9999.0D;
        RenderManager.instance.setWorld(world);
        this.world = world;
        this.globalRenderBlocks = new RenderBlocks(world);
        if (world != null) {
            world.addWorldAccess(this);
            this.loadRenderers();
        }

    }

    public void loadRenderers() {
        Block.LEAVES.setGraphicsLevel(this.client.gameSettings.fancyGraphics);
        this.renderDistance = this.client.gameSettings.renderDistance;
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

        int id = 0;
        int chunkIdx = 0;
        for (int x = 0; x < this.renderChunksWide; ++x) {
            for (int y = 0; y < this.renderChunksTall; ++y) {
                for (int z = 0; z < this.renderChunksDeep; ++z) {
                    final WorldRenderer renderer = new WorldRenderer(
                            this.world, this.tileEntities, x * 16, y * 16, z * 16, 16, this.glRenderListBase + id
                    );

                    final int idx = (z * this.renderChunksTall + y) * this.renderChunksWide + x;
                    this.worldRenderers[idx] = renderer;

                    if (this.occlusionEnabled) {
                        renderer.glOcclusionQuery = this.glOcclusionQueryBase.get(chunkIdx);
                    }

                    renderer.isWaitingOnOcclusionQuery = false;
                    renderer.isVisible = true;
                    renderer.isInFrustum = true;
                    renderer.chunkIndex = chunkIdx++;
                    renderer.markDirty();

                    this.sortedWorldRenderers[idx] = renderer;
                    this.worldRenderersToUpdate.add(renderer);

                    id += 3;
                }
            }
        }

        if (this.world != null) {
            EntityLiving entity = this.client.renderViewEntity;
            if (entity != null) {
                this.markRenderersForNewPosition(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ));
                Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entity));
            }
        }

        this.renderEntitiesStartupCounter = 2;
    }

    public void renderEntities(Vec3d vec, ICamera camera, float delta) {
        if (this.renderEntitiesStartupCounter > 0) {
            --this.renderEntitiesStartupCounter;
        } else {
            TileEntityRenderer.instance.cacheActiveRenderInfo(this.world, this.renderEngine, this.client.fontRenderer, this.client.renderViewEntity, delta);
            RenderManager.instance.cacheActiveRenderInfo(this.world, this.renderEngine, this.client.fontRenderer, this.client.renderViewEntity, this.client.gameSettings, delta);
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            EntityLiving var4 = this.client.renderViewEntity;
            RenderManager.renderPosX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double) delta;
            RenderManager.renderPosY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double) delta;
            RenderManager.renderPosZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double) delta;
            TileEntityRenderer.staticPlayerX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double) delta;
            TileEntityRenderer.staticPlayerY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double) delta;
            TileEntityRenderer.staticPlayerZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double) delta;
            List var5 = this.world.getLoadedEntityList();
            this.countEntitiesTotal = var5.size();

            for (int var6 = 0; var6 < this.world.weatherEffects.size(); ++var6) {
                Entity var7 = this.world.weatherEffects.get(var6);
                ++this.countEntitiesRendered;
                if (var7.isInRangeToRenderVector3d(vec)) {
                    RenderManager.instance.renderEntity(var7, delta);
                }
            }

            for (int var9 = 0; var9 < var5.size(); ++var9) {
                Entity entity = (Entity) var5.get(var9);
                if (entity.isInRangeToRenderVector3d(vec)
                        && (entity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entity.boundingBox))
                        && (entity != this.client.renderViewEntity || this.client.gameSettings.thirdPersonView || this.client.renderViewEntity.isSleeping())) {
                    int var8 = MathHelper.floor(entity.posY);
                    if (var8 < 0) {
                        var8 = 0;
                    }

                    if (var8 >= 128) {
                        var8 = 127;
                    }

                    if (this.world.blockExists(MathHelper.floor(entity.posX), var8, MathHelper.floor(entity.posZ))) {
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

    public int sortAndRender(EntityLiving entity, int zeroAll, double partialTicks) {
        for (int i = 0; i < 10; ++i) {
            this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
            WorldRenderer renderer = this.worldRenderers[this.worldRenderersCheckIndex];
            if (renderer.needsUpdate && !this.worldRenderersToUpdate.contains(renderer)) {
                this.worldRenderersToUpdate.add(renderer);
            }
        }

        if (this.client.gameSettings.renderDistance != this.renderDistance) {
            this.loadRenderers();
        }

        if (zeroAll == 0) {
            this.renderersLoaded = 0;
            this.renderersBeingClipped = 0;
            this.renderersBeingOccluded = 0;
            this.renderersBeingRendered = 0;
            this.renderersSkippingRenderPass = 0;
        }

        double dX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double dY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double dZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

        double difX = entity.posX - this.prevSortX;
        double difY = entity.posY - this.prevSortY;
        double difZ = entity.posZ - this.prevSortZ;

        if (difX * difX + difY * difY + difZ * difZ > 16.0D) {
            this.prevSortX = entity.posX;
            this.prevSortY = entity.posY;
            this.prevSortZ = entity.posZ;
            this.markRenderersForNewPosition(
                    MathHelper.floor(entity.posX),
                    MathHelper.floor(entity.posY),
                    MathHelper.floor(entity.posZ)
            );
            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entity));
        }

        RenderHelper.disableStandardItemLighting();
        int var17 = 0;
        if (this.occlusionEnabled && this.client.gameSettings.advancedOpengl && !this.client.gameSettings.anaglyph && zeroAll == 0) {
            int begin = 0;
            int end = 16;
            this.checkOcclusionQueryResult(begin, end);

            for (int var20 = begin; var20 < end; ++var20) {
                this.sortedWorldRenderers[var20].isVisible = true;
            }

            var17 += this.renderSortedRenderers(begin, end, zeroAll, partialTicks);

            do {
                begin = end;
                end *= 2;
                if (end > this.sortedWorldRenderers.length) {
                    end = this.sortedWorldRenderers.length;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_FOG);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                this.checkOcclusionQueryResult(begin, end);
                GL11.glPushMatrix();
                float var36 = 0.0F;
                float var21 = 0.0F;
                float var22 = 0.0F;

                for (int var23 = begin; var23 < end; ++var23) {
                    if (this.sortedWorldRenderers[var23].skipAllRenderPasses()) {
                        this.sortedWorldRenderers[var23].isInFrustum = false;
                    } else {
                        if (!this.sortedWorldRenderers[var23].isInFrustum) {
                            this.sortedWorldRenderers[var23].isVisible = true;
                        }

                        if (this.sortedWorldRenderers[var23].isInFrustum && !this.sortedWorldRenderers[var23].isWaitingOnOcclusionQuery) {
                            float var24 = MathHelper.sqrt(this.sortedWorldRenderers[var23].distanceToEntitySquared(entity));
                            int var25 = (int) (1.0F + var24 / 128.0F);
                            if (this.cloudOffsetX % var25 == var23 % var25) {
                                WorldRenderer var26 = this.sortedWorldRenderers[var23];
                                float var27 = (float) ((double) var26.posXMinus - dX);
                                float var28 = (float) ((double) var26.posYMinus - dY);
                                float var29 = (float) ((double) var26.posZMinus - dZ);
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
                if (this.client.gameSettings.anaglyph) {
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
                var17 += this.renderSortedRenderers(begin, end, zeroAll, partialTicks);
            } while (end < this.sortedWorldRenderers.length);
        } else {
            var17 += this.renderSortedRenderers(0, this.sortedWorldRenderers.length, zeroAll, partialTicks);
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

    private int renderSortedRenderers(int begin, int end, int zeroAll, double partialTicks) {
        this.glRenderLists.clear();
        int var6 = 0;

        for (int i = begin; i < end; ++i) {
            if (zeroAll == 0) {
                ++this.renderersLoaded;
                if (this.sortedWorldRenderers[i].skipRenderPass[zeroAll]) {
                    ++this.renderersSkippingRenderPass;
                } else if (!this.sortedWorldRenderers[i].isInFrustum) {
                    ++this.renderersBeingClipped;
                } else if (this.occlusionEnabled && !this.sortedWorldRenderers[i].isVisible) {
                    ++this.renderersBeingOccluded;
                } else {
                    ++this.renderersBeingRendered;
                }
            }

            if (!this.sortedWorldRenderers[i].skipRenderPass[zeroAll] && this.sortedWorldRenderers[i].isInFrustum && (!this.occlusionEnabled || this.sortedWorldRenderers[i].isVisible)) {
                int var8 = this.sortedWorldRenderers[i].getGLCallListForPass(zeroAll);
                if (var8 >= 0) {
                    this.glRenderLists.add(this.sortedWorldRenderers[i]);
                    ++var6;
                }
            }
        }

        EntityLiving entity = this.client.renderViewEntity;
        double dX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double dY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double dZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        int var14 = 0;

        for (int i = 0; i < this.allRenderLists.length; ++i) {
            this.allRenderLists[i].resetList();
        }

        for (int i = 0; i < this.glRenderLists.size(); ++i) {
            WorldRenderer renderer = this.glRenderLists.get(i);
            int var17 = -1;

            for (int j = 0; j < var14; ++j) {
                if (this.allRenderLists[j].rendersChunk(renderer.posXMinus, renderer.posYMinus, renderer.posZMinus)) {
                    var17 = j;
                }
            }

            if (var17 < 0) {
                var17 = var14++;
                this.allRenderLists[var17].setupRenderList(renderer.posXMinus, renderer.posYMinus, renderer.posZMinus, dX, dY, dZ);
            }

            this.allRenderLists[var17].addGLRenderList(renderer.getGLCallListForPass(zeroAll));
        }

        this.renderAllRenderLists(zeroAll, partialTicks);
        return var6;
    }

    public void renderAllRenderLists(int var1, double var2) {
        for (int i = 0; i < this.allRenderLists.length; ++i) {
            this.allRenderLists[i].callLists();
        }
    }

    public void updateClouds() {
        ++this.cloudOffsetX;
    }

    public void renderSky(float var1) {
        if (this.client.theWorld.worldProvider.isNether)
            return;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Vec3d vec = this.world.getSkyColor(this.client.renderViewEntity, var1);
        float x = (float) vec.x;
        float y = (float) vec.y;
        float z = (float) vec.z;
        if (this.client.gameSettings.anaglyph) {
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
        float[] var18 = this.world.worldProvider.calcSunriseSunsetColors(this.world.getCelestialAngle(var1), var1);
        if (var18 != null) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            float var20 = this.world.getCelestialAngle(var1);
            GL11.glRotatef(var20 > 0.5F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            float var9 = var18[0];
            float var10 = var18[1];
            float var11 = var18[2];
            if (this.client.gameSettings.anaglyph) {
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
                float var29 = (float) var28 * MathConstants.PI * 2.0F / (float) var26;
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
        float var19 = 1.0F - this.world.getRainStrength(var1);
        float var21 = 0.0F;
        float var22 = 0.0F;
        float var23 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var19);
        GL11.glTranslatef(var21, var22, var23);
        GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(this.world.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
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

        float starBrightness = this.world.getStarBrightness(var1) * var19;
        if (starBrightness > 0.0F) {
            GL11.glColor4f(starBrightness, starBrightness, starBrightness, starBrightness);
            GL11.glCallList(this.starGLCallList);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        if (this.world.worldProvider.method1()) {
            GL11.glColor3f(x * 0.2F + 0.04F, y * 0.2F + 0.04F, z * 0.6F + 0.1F);
        } else {
            GL11.glColor3f(x, y, z);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glCallList(this.glSkyList2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
    }

    public void renderClouds(float partialTicks) {
        if (this.client.theWorld.worldProvider.isNether)
            return;

        if (this.client.gameSettings.fancyGraphics) {
            this.renderCloudsFancy(partialTicks);
            return;
        }

        GL11.glDisable(GL11.GL_CULL_FACE);

        final EntityLiving rve = this.client.renderViewEntity;
        float dY = (float) (rve.lastTickPosY + (rve.posY - rve.lastTickPosY) * (double) partialTicks);
        byte vol = 32;
        int hg = 256 / vol;
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Vec3d vec = this.world.cloudColor(partialTicks);
        float r = (float) vec.x;
        float g = (float) vec.y;
        float b = (float) vec.z;

        if (this.client.gameSettings.anaglyph) {
            float tempR = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float tempG = (r * 30.0F + g * 70.0F) / 100.0F;
            float tempB = (r * 30.0F + b * 70.0F) / 100.0F;
            r = tempR;
            g = tempG;
            b = tempB;
        }

        float m = 4.8828125E-4F;
        double dX = rve.prevPosX + (rve.posX - rve.prevPosX) * (double) partialTicks + (double) (((float) this.cloudOffsetX + partialTicks) * 0.03F);
        double dZ = rve.prevPosZ + (rve.posZ - rve.prevPosZ) * (double) partialTicks;

        int dX2048 = MathHelper.floor(dX / 2048.0D);
        int dZ2048 = MathHelper.floor(dZ / 2048.0D);

        dX = dX - (double) (dX2048 * GL11.GL_EXP);
        dZ = dZ - (double) (dZ2048 * GL11.GL_EXP);
        float spY = this.world.worldProvider.getCloudHeight() - dY + 0.33F;
        float spX = (float) (dX * (double) m);
        float spZ = (float) (dZ * (double) m);
        tess.startDrawingQuads();
        tess.setColorRGBA_F(r, g, b, 0.8F);

        for (int u = -vol * hg; u < vol * hg; u += vol) {
            for (int v = -vol * hg; v < vol * hg; v += vol) {
                tess.addVertexWithUV(u, spY, v + vol, u * m + spX, (v + vol) * m + spZ);
                tess.addVertexWithUV(u + vol, spY, v + vol, (u + vol) * m + spX, (v + vol) * m + spZ);
                tess.addVertexWithUV(u + vol, spY, v, (u + vol) * m + spX, v * m + spZ);
                tess.addVertexWithUV(u, spY, v, u * m + spX, v * m + spZ);
            }
        }

        tess.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public boolean func_27307_a(double var1, double var3, double var5, float var7) {
        return false;
    }

    public void renderCloudsFancy(float partialTicks) {
        GL11.glDisable(GL11.GL_CULL_FACE);

        final EntityLiving rve = this.client.renderViewEntity;
        float dY = (float) (rve.lastTickPosY + (rve.posY - rve.lastTickPosY) * (double) partialTicks);

        Tessellator t = Tessellator.INSTANCE;
        float vol = 12.0F;
        float cloudHeight = 4.0F;

        double dX = (rve.prevPosX + (rve.posX - rve.prevPosX) * (double) partialTicks + (double) (((float) this.cloudOffsetX + partialTicks) * 0.03F)) / (double) vol;
        double dZ = (rve.prevPosZ + (rve.posZ - rve.prevPosZ) * (double) partialTicks) / (double) vol + 0.33000001311302185D;
        float cpy = this.world.worldProvider.getCloudHeight() - dY + 0.33F;

        int dX2048 = MathHelper.floor(dX / 2048.0D);
        int dZ2048 = MathHelper.floor(dZ / 2048.0D);

        dX = dX - (double) (dX2048 * GL11.GL_EXP);
        dZ = dZ - (double) (dZ2048 * GL11.GL_EXP);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Vec3d color = this.world.cloudColor(partialTicks);
        float r = (float) color.x;
        float g = (float) color.y;
        float b = (float) color.z;
        if (this.client.gameSettings.anaglyph) {
            float tempR = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float tempG = (r * 30.0F + g * 70.0F) / 100.0F;
            float tempB = (r * 30.0F + b * 70.0F) / 100.0F;
            r = tempR;
            g = tempG;
            b = tempB;
        }

        float m = 0.00390625F;
        float dXm = (float) MathHelper.floor(dX) * m;
        float dZm = (float) MathHelper.floor(dZ) * m;
        float lDx = (float) (dX - (double) MathHelper.floor(dX));
        float lDz = (float) (dZ - (double) MathHelper.floor(dZ));

        byte m1 = 8;
        byte size = 3;
        float m3 = 9.765625E-4F;

        GL11.glScalef(vol, 1.0F, vol);

        for (int i = 0; i < 2; ++i) {
            if (i == 0) {
                GL11.glColorMask(false, false, false, false);
            } else if (this.client.gameSettings.anaglyph) {
                if (EntityRenderer.anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, true);
                } else {
                    GL11.glColorMask(true, false, false, true);
                }
            } else {
                GL11.glColorMask(true, true, true, true);
            }

            for (int u = -size + 1; u <= size; ++u) {
                for (int v = -size + 1; v <= size; ++v) {
                    t.startDrawingQuads();
                    float um1 = u * m1;
                    float vm1 = v * m1;
                    float x = um1 - lDx;
                    float z = vm1 - lDz;

                    // downside of cloud
                    if (cpy > -cloudHeight - 1.0F) {
                        t.setColorRGBA_F(r * 0.7F, g * 0.7F, b * 0.7F, 0.8F);
                        t.setNormal(0.0F, -1.0F, 0.0F);
                        t.addVertexWithUV(x, cpy, z + m1, um1 * m + dXm, (vm1 + m1) * m + dZm);
                        t.addVertexWithUV(x + m1, cpy, z + m1, (um1 + m1) * m + dXm, (vm1 + m1) * m + dZm);
                        t.addVertexWithUV(x + m1, cpy, z, (um1 + m1) * m + dXm, vm1 * m + dZm);
                        t.addVertexWithUV(x, cpy, z, um1 * m + dXm, vm1 * m + dZm);
                    }

                    // top of cloud
                    if (cpy <= cloudHeight + 1.0F) {
                        t.setColorRGBA_F(r, g, b, 0.8F);
                        t.setNormal(0.0F, 1.0F, 0.0F);
                        final float y1 = cpy + cloudHeight - m3;
                        t.addVertexWithUV(x, y1, z + m1, um1 * m + dXm, (vm1 + m1) * m + dZm);
                        t.addVertexWithUV(x + m1, y1, z + m1, (um1 + m1) * m + dXm, (vm1 + m1) * m + dZm);
                        t.addVertexWithUV(x + m1, y1, z, (um1 + m1) * m + dXm, vm1 * m + dZm);
                        t.addVertexWithUV(x, y1, z, um1 * m + dXm, vm1 * m + dZm);
                    }

                    // x- side of cloud
                    t.setColorRGBA_F(r * 0.9F, g * 0.9F, b * 0.9F, 0.8F);
                    if (u > -1) {
                        t.setNormal(-1.0F, 0.0F, 0.0F);

                        for (int s = 0; s < m1; ++s) {
                            final float u1 = (um1 + s + 0.5F) * m + dXm;
                            final float x1 = x + s;
                            t.addVertexWithUV(x1, cpy, z + m1, u1, (vm1 + m1) * m + dZm);
                            t.addVertexWithUV(x1, cpy + cloudHeight, z + m1, u1, (vm1 + m1) * m + dZm);
                            t.addVertexWithUV(x1, cpy + cloudHeight, z, u1, vm1 * m + dZm);
                            t.addVertexWithUV(x1, cpy, z, u1, vm1 * m + dZm);
                        }
                    }

                    // x+ side of cloud
                    if (u <= 1) {
                        t.setNormal(1.0F, 0.0F, 0.0F);

                        for (int s = 0; s < m1; ++s) {
                            final float u1 = (um1 + s + 0.5F) * m + dXm;
                            final float x1 = x + s + 1.0F - m3;
                            t.addVertexWithUV(x1, cpy, z + m1, u1, (vm1 + m1) * m + dZm);
                            t.addVertexWithUV(x1, cpy + cloudHeight, z + m1, u1, (vm1 + m1) * m + dZm);
                            t.addVertexWithUV(x1, cpy + cloudHeight, z, u1, vm1 * m + dZm);
                            t.addVertexWithUV(x1, cpy, z, u1, vm1 * m + dZm);
                        }
                    }

                    // z- side of cloud
                    t.setColorRGBA_F(r * 0.8F, g * 0.8F, b * 0.8F, 0.8F);
                    if (v > -1) {
                        t.setNormal(0.0F, 0.0F, -1.0F);

                        for (int s = 0; s < m1; ++s) {
                            final float v1 = (vm1 + s + 0.5F) * m + dZm;
                            t.addVertexWithUV(x, cpy + cloudHeight, z + s, um1 * m + dXm, v1);
                            t.addVertexWithUV(x + m1, cpy + cloudHeight, z + s, (um1 + m1) * m + dXm, v1);
                            t.addVertexWithUV(x + m1, cpy, z + s, (um1 + m1) * m + dXm, v1);
                            t.addVertexWithUV(x, cpy, z + s, um1 * m + dXm, v1);
                        }
                    }

                    // z+ side of cloud
                    if (v <= 1) {
                        t.setNormal(0.0F, 0.0F, 1.0F);

                        for (int s = 0; s < m1; ++s) {
                            final float v1 = (vm1 + s + 0.5F) * m + dZm;
                            t.addVertexWithUV(x, cpy + cloudHeight, z + s + 1.0F - m3, um1 * m + dXm, v1);
                            t.addVertexWithUV(x + m1, cpy + cloudHeight, z + s + 1.0F - m3, (um1 + m1) * m + dXm, v1);
                            t.addVertexWithUV(x + m1, cpy, z + s + 1.0F - m3, (um1 + m1) * m + dXm, v1);
                            t.addVertexWithUV(x, cpy, z + s + 1.0F - m3, um1 * m + dXm, v1);
                        }
                    }

                    t.draw();
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
        int rendersToUpdateSize = this.worldRenderersToUpdate.size();
        int var9 = 0;

        label169:
        for (int i = 0; i < rendersToUpdateSize; ++i) {
            WorldRenderer renderer = this.worldRenderersToUpdate.get(i);
            if (!var2) {
                if (renderer.distanceToEntitySquared(entity) > 256.0F) {
                    int var12;
                    for (var12 = 0; var12 < var4 && (var6[var12] == null || var5.doCompare(var6[var12], renderer) <= 0); ++var12) {
                    }

                    --var12;
                    if (var12 <= 0) {
                        continue;
                    }

                    int var13 = var12;

                    while (true) {
                        --var13;
                        if (var13 == 0) {
                            var6[var12] = renderer;
                            continue label169;
                        }

                        var6[var13 - 1] = var6[var13];
                    }
                }
            } else if (!renderer.isInFrustum) {
                continue;
            }

            if (var7 == null) {
                var7 = new ArrayList();
            }

            ++var9;
            var7.add(renderer);
            this.worldRenderersToUpdate.set(i, null);
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
                return rendersToUpdateSize == var9 + var22;
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
                int blockId = this.world.getBlockId(pos.blockX, pos.blockY, pos.blockZ);
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
            int blockId = this.world.getBlockId(pos.blockX, pos.blockY, pos.blockZ);
            if (blockId > 0) {
                Block.BLOCKS_LIST[blockId].setBlockBoundsBasedOnState(this.world, pos.blockX, pos.blockY, pos.blockZ);
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) var5;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) var5;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) var5;
                this.drawOutlinedBoundingBox(Block.BLOCKS_LIST[blockId]
                        .getSelectedBoundingBoxFromPool(this.world, pos.blockX, pos.blockY, pos.blockZ)
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
    public void markBlockAndNeighborsNeedsUpdate(int x, int y, int z) {
        this.func_949_a(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }

    @Override
    public void markBlockRangeNeedsUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.func_949_a(minX - 1, minY - 1, minZ - 1, maxX + 1, maxY + 1, maxZ + 1);
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
    public void playRecord(String recordName, int x, int y, int z) {
        if (recordName != null)
            this.client.ingameGUI.setRecordPlayingMessage("C418 - " + recordName);

        this.client.soundManager.playStreaming(recordName, (float) x, (float) y, (float) z, 1.0F, 1.0F);
    }

    @Override
    public void playSound(String soundCategory, double x, double y, double z, float volume, float pitch) {
        float var10 = 16.0F;
        if (volume > 1.0F) {
            var10 *= volume;
        }

        if (this.client.renderViewEntity.getDistanceSq(x, y, z) < (double) (var10 * var10)) {
            this.client.soundManager.playSound(soundCategory, (float) x, (float) y, (float) z, volume, pitch);
        }

    }

    @Override
    public void spawnParticle(String particleName, double x, double y, double z, double motionX, double motionY, double motionZ) {
        if (this.client != null && this.client.renderViewEntity != null && this.client.effectRenderer != null) {
            double var14 = this.client.renderViewEntity.posX - x;
            double var16 = this.client.renderViewEntity.posY - y;
            double var18 = this.client.renderViewEntity.posZ - z;
            double var20 = 16.0D;
            if (var14 * var14 + var16 * var16 + var18 * var18 <= var20 * var20) {
                switch (particleName) {
                    case "bubble":
                        this.client.effectRenderer.addEffect(new EntityBubbleFX(this.world, x, y, z, motionX, motionY, motionZ));
                        break;
                    case "smoke":
                        this.client.effectRenderer.addEffect(new EntitySmokeFX(this.world, x, y, z, motionX, motionY, motionZ));
                        break;
                    case "note":
                        this.client.effectRenderer.addEffect(new EntityNoteFX(this.world, x, y, z, motionX, motionY, motionZ));
                        break;
                    case "portal":
                        this.client.effectRenderer.addEffect(new EntityPortalFX(this.world, x, y, z, motionX, motionY, motionZ));
                        break;
                    case "explode":
                        this.client.effectRenderer.addEffect(new EntityExplodeFX(this.world, x, y, z, motionX, motionY, motionZ));
                        break;
                    case "flame":
                        this.client.effectRenderer.addEffect(new EntityFlameFX(this.world, x, y, z, motionX, motionY, motionZ));
                        break;
                    case "lava":
                        this.client.effectRenderer.addEffect(new EntityLavaFX(this.world, x, y, z));
                        break;
                    case "footstep":
                        this.client.effectRenderer.addEffect(new EntityFootStepFX(this.renderEngine, this.world, x, y, z));
                        break;
                    case "splash":
                        this.client.effectRenderer.addEffect(new EntitySplashFX(this.world, x, y, z, motionX, motionY, motionZ));
                        break;
                    case "largesmoke":
                        this.client.effectRenderer.addEffect(new EntitySmokeFX(this.world, x, y, z, motionX, motionY, motionZ, 2.5F));
                        break;
                    case "reddust":
                        this.client.effectRenderer.addEffect(new EntityReddustFX(this.world, x, y, z, (float) motionX, (float) motionY, (float) motionZ));
                        break;
                    case "snowballpoof":
                        this.client.effectRenderer.addEffect(new EntitySlimeFX(this.world, x, y, z, Item.SNOWBALL));
                        break;
                    case "snowshovel":
                        this.client.effectRenderer.addEffect(new EntitySnowShovelFX(this.world, x, y, z, motionX, motionY, motionZ));
                        break;
                    case "slime":
                        this.client.effectRenderer.addEffect(new EntitySlimeFX(this.world, x, y, z, Item.SLIMEBALL));
                        break;
                    case "heart":
                        this.client.effectRenderer.addEffect(new EntityHeartFX(this.world, x, y, z, motionX, motionY, motionZ));
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
    public void doNothingWithTileEntity(int x, int y, int z, TileEntity tile) {
    }

    public void dispose() {
        GLAllocation.removeLists(this.glRenderListBase);
    }

    @Override
    public void playEffect(EntityPlayer player, int effectId, int x, int y, int z, int subData) {
        Random rand = this.world.rand;
        switch (effectId) {
            case 1000:
                this.world.playSoundEffect(x, y, z, "random.click", 1.0F, 1.0F);
                break;
            case 1001:
                this.world.playSoundEffect(x, y, z, "random.click", 1.0F, 1.2F);
                break;
            case 1002:
                this.world.playSoundEffect(x, y, z, "random.bow", 1.0F, 1.2F);
                break;
            case 1003:
                if (Math.random() < 0.5D) {
                    this.world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.door_open", 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                } else {
                    this.world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.door_close", 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                }
                break;
            case 1004:
                this.world.playSoundEffect((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "random.fizz", 0.5F, 2.6F + (rand.nextFloat() - rand.nextFloat()) * 0.8F);
                break;
            case 1005:
                if (Item.ITEMS_LIST[subData] instanceof ItemRecord) {
                    this.world.playRecord(((ItemRecord) Item.ITEMS_LIST[subData]).recordName, x, y, z);
                } else {
                    this.world.playRecord(null, x, y, z);
                }
                break;
            case 2000:
                int var8 = subData % 3 - 1;
                int var9 = subData / 3 % 3 - 1;
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
                int var16 = subData & 255;
                if (var16 > 0) {
                    Block block = Block.BLOCKS_LIST[var16];
                    this.client.soundManager.playSound(block.stepSound.stepSoundDir(), (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                }

                this.client.effectRenderer.addBlockDestroyEffects(x, y, z, subData & 255, subData >> 8 & 255);
        }

    }
}
