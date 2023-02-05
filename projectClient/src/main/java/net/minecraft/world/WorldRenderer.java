package net.minecraft.world;

import net.hypnosis.render.Tessellator;
import net.minecraft.block.Block;
import net.minecraft.client.render.ICamera;
import net.minecraft.client.render.entity.RenderBlocks;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityRenderer;
import net.minecraft.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkCache;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldRenderer {
    public static int chunksUpdated = 0;
    private static Tessellator tess = Tessellator.INSTANCE;
    public World world;
    public int posX;
    public int posY;
    public int posZ;
    public int sizeWidth;
    public int sizeHeight;
    public int sizeDepth;
    public int posXMinus;
    public int posYMinus;
    public int posZMinus;
    public int posXClip;
    public int posYClip;
    public int posZClip;
    public boolean isInFrustum = false;
    public boolean[] skipRenderPass = new boolean[2];
    public int posXPlus;
    public int posYPlus;
    public int posZPlus;
    public float rendererRadius;
    public boolean needsUpdate;
    public AxisAlignedBB rendererBoundingBox;
    public int chunkIndex;
    public boolean isVisible = true;
    public boolean isWaitingOnOcclusionQuery;
    public int glOcclusionQuery;
    public boolean isChunkLit;
    public List<TileEntity> tileEntityRenderers = new ArrayList<>();
    private final int glRenderList;
    private boolean isInitialized = false;
    private List<TileEntity> tileEntities;

    public WorldRenderer(World world, List<TileEntity> tileEntities, int x, int y, int z, int size, int glRenderList) {
        this.world = world;
        this.tileEntities = tileEntities;
        this.sizeWidth = this.sizeHeight = this.sizeDepth = size;
        this.rendererRadius = MathHelper.sqrt((float) (this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth)) / 2.0F;
        this.glRenderList = glRenderList;
        this.posX = -999;
        this.setPosition(x, y, z);
        this.needsUpdate = false;
    }

    public void setPosition(int x, int y, int z) {
        if (x != this.posX || y != this.posY || z != this.posZ) {
            this.setDontDraw();

            this.posX = x;
            this.posY = y;
            this.posZ = z;

            this.posXPlus = x + this.sizeWidth / 2;
            this.posYPlus = y + this.sizeHeight / 2;
            this.posZPlus = z + this.sizeDepth / 2;

            this.posXClip = x & 1023;
            this.posYClip = y;
            this.posZClip = z & 1023;

            this.posXMinus = x - this.posXClip;
            this.posYMinus = y - this.posYClip;
            this.posZMinus = z - this.posZClip;

            float var4 = 6.0F;
            this.rendererBoundingBox = AxisAlignedBB.getBoundingBox(
                    (float) x - var4,
                    (float) y - var4,
                    (float) z - var4,
                    (float) (x + this.sizeWidth) + var4,
                    (float) (y + this.sizeHeight) + var4,
                    (float) (z + this.sizeDepth) + var4
            );

            GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
            RenderItem.renderAABB(
                    AxisAlignedBB.getBoundingBoxFromPool(
                            (float) this.posXClip - var4,
                            (float) this.posYClip - var4,
                            (float) this.posZClip - var4,
                            (float) (this.posXClip + this.sizeWidth) + var4,
                            (float) (this.posYClip + this.sizeHeight) + var4,
                            (float) (this.posZClip + this.sizeDepth) + var4
                    )
            );
            GL11.glEndList();

            this.markDirty();
        }
    }

    private void setupGLTranslation() {
        GL11.glTranslatef((float) this.posXClip, (float) this.posYClip, (float) this.posZClip);
    }

    public void updateRenderer() {
        if (!this.needsUpdate)
            return;

        ++chunksUpdated;
        int startX = this.posX;
        int startY = this.posY;
        int startZ = this.posZ;

        int endX = this.posX + this.sizeWidth;
        int endY = this.posY + this.sizeHeight;
        int endZ = this.posZ + this.sizeDepth;

        for (int var7 = 0; var7 < 2; ++var7) {
            this.skipRenderPass[var7] = true;
        }

        Chunk.isLit = false;
        Set<TileEntity> tileEntityRenderers = new HashSet<>(this.tileEntityRenderers);
        this.tileEntityRenderers.clear();
        byte var8 = 1;
        ChunkCache chunkCache = new ChunkCache(this.world, startX - var8, startY - var8, startZ - var8, endX + var8, endY + var8, endZ + var8);
        RenderBlocks renderBlocks = new RenderBlocks(chunkCache);

        for (int i = 0; i < 2; ++i) {
            boolean renderPassIs = false;
            boolean renderByRenderType = false;
            boolean startedDraw = false;

            for (int y = startY; y < endY; ++y) {
                for (int z = startZ; z < endZ; ++z) {
                    for (int x = startX; x < endX; ++x) {
                        int id = chunkCache.getBlockId(x, y, z);
                        if (id <= 0)
                            continue;

                        if (!startedDraw) {
                            startedDraw = true;
                            GL11.glNewList(this.glRenderList + i, GL11.GL_COMPILE);
                            GL11.glPushMatrix();
                            this.setupGLTranslation();
                            float scale = 1.000001F;
                            GL11.glTranslatef((float) (-this.sizeDepth) / 2.0F, (float) (-this.sizeHeight) / 2.0F, (float) (-this.sizeDepth) / 2.0F);
                            GL11.glScalef(scale, scale, scale);
                            GL11.glTranslatef((float) this.sizeDepth / 2.0F, (float) this.sizeHeight / 2.0F, (float) this.sizeDepth / 2.0F);
                            tess.startDrawingQuads();
                            tess.setTranslationD(-this.posX, -this.posY, -this.posZ);
                        }

                        if (i == 0 && Block.IS_BLOCK_CONTAINER[id]) {
                            TileEntity tile = chunkCache.getBlockTileEntity(x, y, z);
                            if (TileEntityRenderer.instance.hasSpecialRenderer(tile)) {
                                this.tileEntityRenderers.add(tile);
                            }
                        }

                        Block block = Block.BLOCKS_LIST[id];
                        int renderBlockPass = block.getRenderBlockPass();
                        if (renderBlockPass != i) {
                            renderPassIs = true;
                        } else if (renderBlockPass == i) {
                            renderByRenderType |= renderBlocks.renderBlockByRenderType(block, x, y, z);
                        }
                    }
                }
            }

            if (startedDraw) {
                tess.draw();
                GL11.glPopMatrix();
                GL11.glEndList();
                tess.setTranslationD(0.0D, 0.0D, 0.0D);
            } else renderByRenderType = false;

            if (renderByRenderType)
                this.skipRenderPass[i] = false;

            if (!renderPassIs)
                break;
        }

        Set<TileEntity> entities = new HashSet<>(this.tileEntityRenderers);
        entities.removeAll(tileEntityRenderers);
        this.tileEntities.addAll(entities);
        tileEntityRenderers.removeAll(this.tileEntityRenderers);
        this.tileEntities.removeAll(tileEntityRenderers);
        this.isChunkLit = Chunk.isLit;
        this.isInitialized = true;
    }

    public float distanceToEntitySquared(Entity entity) {
        float var2 = (float) (entity.posX - (double) this.posXPlus);
        float var3 = (float) (entity.posY - (double) this.posYPlus);
        float var4 = (float) (entity.posZ - (double) this.posZPlus);
        return var2 * var2 + var3 * var3 + var4 * var4;
    }

    public void setDontDraw() {
        for (int var1 = 0; var1 < 2; ++var1) {
            this.skipRenderPass[var1] = true;
        }

        this.isInFrustum = false;
        this.isInitialized = false;
    }

    public void func_1204_c() {
        this.setDontDraw();
        this.world = null;
    }

    public int getGLCallListForPass(int zeroAll) {
        if (!this.isInFrustum)
            return -1;

        return !this.skipRenderPass[zeroAll] ? this.glRenderList + zeroAll : -1;
    }

    public void updateInFrustrum(ICamera camera) {
        this.isInFrustum = camera.isBoundingBoxInFrustum(this.rendererBoundingBox);
    }

    public void callOcclusionQueryList() {
        GL11.glCallList(this.glRenderList + 2);
    }

    public boolean skipAllRenderPasses() {
        if (!this.isInitialized)
            return false;

        return this.skipRenderPass[0] && this.skipRenderPass[1];
    }

    public void markDirty() {
        this.needsUpdate = true;
    }
}
