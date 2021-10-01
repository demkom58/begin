package net.hypnosis.render;

import net.potion.client.render.GLAllocation;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.nio.*;

public class Tessellator {
    private static boolean convertQuadsToTriangles = true;
    private static boolean tryVBO = true;
    /**
     * The byte buffer used for GL allocation.
     */
    private final ByteBuffer byteBuffer;
    /**
     * The same memory as byteBuffer, but referenced as an integer buffer.
     */
    private final IntBuffer intBuffer;
    /**
     * The same memory as byteBuffer, but referenced as an float buffer.
     */
    private final FloatBuffer floatBuffer;
    /**
     * The same memory as byteBuffer, but referenced as an short buffer.
     */
    private final ShortBuffer shortBuffer;
    /**
     * Raw integer array.
     */
    private final int[] rawBuffer;
    /**
     * The number of vertices to be drawn in the next draw call. Reset to 0 between draw calls.
     */
    private int vertexCount;
    /**
     * The first coordinate to be used for the texture.
     */
    private double textureU;
    /**
     * The second coordinate to be used for the texture.
     */
    private double textureV;
    private int brightness;
    /**
     * The color (RGBA) value to be used for the following draw call.
     */
    private int color;
    /**
     * Whether the current draw object for this tessellator has color values.
     */
    private boolean hasColor;
    /**
     * Whether the current draw object for this tessellator has texture coordinates.
     */
    private boolean hasTexture;
    private boolean hasBrightness;
    /**
     * Whether the current draw object for this tessellator has normal values.
     */
    private boolean hasNormals;
    /**
     * The index into the raw buffer to be used for the next data.
     */
    private int rawBufferIndex;
    /**
     * The number of vertices manually added to the given draw call. This differs from vertexCount because it adds extra
     * vertices when converting quads to triangles.
     */
    private int addedVertices;
    /**
     * Disables all color information for the following draw call.
     */
    private boolean isColorDisabled;
    /**
     * The draw mode currently being used by the tessellator.
     */
    private int drawMode;
    /**
     * An offset to be applied along the x-axis for all vertices in this draw call.
     */
    private double xOffset;
    /**
     * An offset to be applied along the y-axis for all vertices in this draw call.
     */
    private double yOffset;
    /**
     * An offset to be applied along the z-axis for all vertices in this draw call.
     */
    private double zOffset;
    /**
     * The normal to be applied to the face being drawn.
     */
    private int normal;
    /**
     * The static instance of the Tessellator.
     */
    public static final Tessellator INSTANCE = new Tessellator(2097152);
    /**
     * Whether this tessellator is currently in draw mode.
     */
    private boolean isDrawing;
    private final boolean useVBO;
    private IntBuffer vertexBuffers;
    private int vboIndex = 0;
    private int vboCount = 10;
    /**
     * The size of the buffers used (in integers).
     */
    private int bufferSize;

    private Tessellator(int size) {
        this.bufferSize = size;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(size * 4);
        this.intBuffer = this.byteBuffer.asIntBuffer();
        this.floatBuffer = this.byteBuffer.asFloatBuffer();
        this.shortBuffer = this.byteBuffer.asShortBuffer();
        this.rawBuffer = new int[size];

        this.useVBO = tryVBO && GL.getCapabilities().GL_ARB_vertex_buffer_object;
        if (this.useVBO)
            ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount));
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw() {
        if (!this.isDrawing)
            throw new IllegalStateException("Not tesselating!");

        this.isDrawing = false;
        if (this.vertexCount > 0) {
            this.intBuffer.clear();
            this.intBuffer.put(this.rawBuffer, 0, this.rawBufferIndex);
            this.byteBuffer.position(0);
            this.byteBuffer.limit(this.rawBufferIndex * 4);
            if (this.useVBO) {
                this.vboIndex = (this.vboIndex + 1) % this.vboCount;
                ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, this.vertexBuffers.get(this.vboIndex));
                ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, this.byteBuffer, ARBVertexBufferObject.GL_STREAM_DRAW_ARB);
            }

            if (this.hasTexture) {
                if (this.useVBO) {
                    GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12L);
                } else {
                    this.floatBuffer.position(3);
                    GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, this.floatBuffer);
                }

                GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            }

            if (this.hasBrightness) {
                GL13.glClientActiveTexture(GL13.GL_TEXTURE1);
                if (this.useVBO) {
                    GL11.glTexCoordPointer(2, GL11.GL_SHORT, 32, 28L);
                } else {
                    this.shortBuffer.position(14);
                    GL11.glTexCoordPointer(2, GL11.GL_SHORT, 32, this.shortBuffer);
                }
                GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
            }

            if (this.hasColor) {
                if (this.useVBO) {
                    GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, 20L);
                } else {
                    this.byteBuffer.position(20);
                    GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, this.byteBuffer);
                }

                GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            }

            if (this.hasNormals) {
                if (this.useVBO) {
                    GL11.glNormalPointer(GL11.GL_BYTE, 32, 24L);
                } else {
                    this.byteBuffer.position(24);
                    GL11.glNormalPointer(GL11.GL_BYTE, 32, this.byteBuffer);
                }

                GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
            }

            if (this.useVBO) {
                GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0L);
            } else {
                this.floatBuffer.position(0);
                GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, this.floatBuffer);
            }

            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            if (this.drawMode == GL11.GL_QUADS && convertQuadsToTriangles) {
                GL11.glDrawArrays(4, 0, this.vertexCount);
            } else {
                GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
            }

            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            if (this.hasTexture) {
                GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            }

            if (this.hasBrightness) {
                GL13.glClientActiveTexture(GL13.GL_TEXTURE1);
                GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
            }

            if (this.hasColor) {
                GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
            }

            if (this.hasNormals) {
                GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
            }
        }

        this.reset();
    }

    private void reset() {
        this.vertexCount = 0;
        this.byteBuffer.clear();
        this.rawBufferIndex = 0;
        this.addedVertices = 0;
    }

    public void startDrawingQuads() {
        this.startDrawing(GL11.GL_QUADS);
    }

    /**
     * Starts draw
     * @param drawMode Specifies what kind of primitives to render. Symbolic constants
     *                  GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_LINE_STRIP_ADJACENCY,
     *                  GL_LINES_ADJACENCY, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, GL_TRIANGLES,
     *                  GL_TRIANGLE_STRIP_ADJACENCY, GL_TRIANGLES_ADJACENCY and GL_PATCHES are accepted.
     */
    public void startDrawing(int drawMode) {
        if (this.isDrawing)
            throw new IllegalStateException("Already tesselating!");

        this.isDrawing = true;
        this.reset();
        this.drawMode = drawMode;
        this.hasNormals = false;
        this.hasColor = false;
        this.hasTexture = false;
        this.hasBrightness = false;
        this.isColorDisabled = false;
    }

    public void setTextureUV(double textureU, double textureV) {
        this.hasTexture = true;
        this.textureU = textureU;
        this.textureV = textureV;
    }

    public void setBrightness(int brightness) {
        this.hasBrightness = true;
        this.brightness = brightness;
    }

    public void setColorOpaque_F(float r, float g, float b) {
        this.setColorOpaque((int) (r * 255.0F), (int) (g * 255.0F), (int) (b * 255.0F));
    }

    public void setColorRGBA_F(float r, float g, float b, float a) {
        this.setColorRGBA((int) (r * 255.0F), (int) (g * 255.0F), (int) (b * 255.0F), (int) (a * 255.0F));
    }

    public void setColorOpaque(int r, int g, int b) {
        this.setColorRGBA(r, g, b, 255);
    }

    public void setColorRGBA(int r, int g, int b, int a) {
        if (this.isColorDisabled)
            return;
        if (r > 255)
            r = 255;

        if (g > 255)
            g = 255;

        if (b > 255)
            b = 255;

        if (a > 255)
            a = 255;

        if (r < 0)
            r = 0;

        if (g < 0)
            g = 0;

        if (b < 0)
            b = 0;

        if (a < 0)
            a = 0;

        this.hasColor = true;
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
            this.color = a << 24 | b << 16 | g << 8 | r;
        else
            this.color = r << 24 | g << 16 | b << 8 | a;
    }

    public void addVertexWithUV(double x, double y, double z, double u, double v) {
        this.setTextureUV(u, v);
        this.addVertex(x, y, z);
    }

    public void addVertex(double x, double y, double z) {
        ++this.addedVertices;
        if (this.drawMode == GL11.GL_QUADS && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
            for (int i = 0; i < 2; ++i) {
                int of = 8 * (3 - i);
                if (this.hasTexture) {
                    this.rawBuffer[this.rawBufferIndex + 3] = this.rawBuffer[this.rawBufferIndex - of + 3];
                    this.rawBuffer[this.rawBufferIndex + 4] = this.rawBuffer[this.rawBufferIndex - of + 4];
                }

                if (this.hasColor) {
                    this.rawBuffer[this.rawBufferIndex + 5] = this.rawBuffer[this.rawBufferIndex - of + 5];
                }

                this.rawBuffer[this.rawBufferIndex] = this.rawBuffer[this.rawBufferIndex - of];
                this.rawBuffer[this.rawBufferIndex + 1] = this.rawBuffer[this.rawBufferIndex - of + 1];
                this.rawBuffer[this.rawBufferIndex + 2] = this.rawBuffer[this.rawBufferIndex - of + 2];
                ++this.vertexCount;
                this.rawBufferIndex += 8;
            }
        }

        if (this.hasTexture) {
            this.rawBuffer[this.rawBufferIndex + 3] = Float.floatToRawIntBits((float) this.textureU);
            this.rawBuffer[this.rawBufferIndex + 4] = Float.floatToRawIntBits((float) this.textureV);
        }

        if (this.hasBrightness)
            this.rawBuffer[this.rawBufferIndex + 7] = this.brightness;

        if (this.hasColor)
            this.rawBuffer[this.rawBufferIndex + 5] = this.color;

        if (this.hasNormals)
            this.rawBuffer[this.rawBufferIndex + 6] = this.normal;

        this.rawBuffer[this.rawBufferIndex] = Float.floatToRawIntBits((float) (x + this.xOffset));
        this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float) (y + this.yOffset));
        this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float) (z + this.zOffset));
        this.rawBufferIndex += 8;
        ++this.vertexCount;
        if (this.vertexCount % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
            this.draw();
            this.isDrawing = true;
        }

    }

    public void setColorOpaque_I(int hexColor) {
        int r = hexColor >> 16 & 255;
        int g = hexColor >> 8 & 255;
        int b = hexColor & 255;
        this.setColorOpaque(r, g, b);
    }

    public void setColorRGBA_I(int hexColor, int alpha) {
        int r = hexColor >> 16 & 255;
        int g = hexColor >> 8 & 255;
        int b = hexColor & 255;
        this.setColorRGBA(r, g, b, alpha);
    }

    public void disableColor() {
        this.isColorDisabled = true;
    }

    public void setNormal(float var1, float var2, float var3) {
        if (!this.isDrawing)
            System.out.println("But..");

        this.hasNormals = true;
        byte var4 = (byte) ((int) (var1 * 128.0F));
        byte var5 = (byte) ((int) (var2 * 127.0F));
        byte var6 = (byte) ((int) (var3 * 127.0F));
        this.normal = var4 | var5 << 8 | var6 << 16;
    }

    public void setTranslationD(double xOffset, double yOffset, double zOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public void setTranslationF(float xOffset, float yOffset, float zOffset) {
        this.xOffset += xOffset;
        this.yOffset += yOffset;
        this.zOffset += zOffset;
    }
}
