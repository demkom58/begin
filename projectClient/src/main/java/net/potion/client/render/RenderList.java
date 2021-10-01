package net.potion.client.render;

import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

public class RenderList {
    private int renderChunkX;
    private int renderChunkY;
    private int renderChunkZ;
    private float cameraX;
    private float cameraY;
    private float cameraZ;
    private IntBuffer glList = GLAllocation.createDirectIntBuffer(65536);
    private boolean valid = false;
    private boolean bufferFlipped = false;

    public void setupRenderList(int renderChunkX, int renderChunkY, int renderChunkZ, double cameraX, double cameraY, double cameraZ) {
        this.valid = true;
        this.glList.clear();
        this.renderChunkX = renderChunkX;
        this.renderChunkY = renderChunkY;
        this.renderChunkZ = renderChunkZ;
        this.cameraX = (float) cameraX;
        this.cameraY = (float) cameraY;
        this.cameraZ = (float) cameraZ;
    }

    public boolean rendersChunk(int chunkX, int chunkY, int chunkZ) {
        return this.valid && chunkX == this.renderChunkX && chunkY == this.renderChunkY && chunkZ == this.renderChunkZ;
    }

    public void addGLRenderList(int var1) {
        this.glList.put(var1);
        if (this.glList.remaining() == 0) {
            this.callLists();
        }

    }

    public void callLists() {
        if (this.valid) {
            if (!this.bufferFlipped) {
                this.glList.flip();
                this.bufferFlipped = true;
            }

            if (this.glList.remaining() > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef(
                        (float) this.renderChunkX - this.cameraX,
                        (float) this.renderChunkY - this.cameraY,
                        (float) this.renderChunkZ - this.cameraZ
                );
                GL11.glCallLists(this.glList);
                GL11.glPopMatrix();
            }

        }
    }

    public void resetList() {
        this.valid = false;
        this.bufferFlipped = false;
    }
}
