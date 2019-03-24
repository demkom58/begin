package net.minecraft;

import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.*;

public class RenderEngine {
    public static boolean useMipmaps = false;
    private HashMap<String, Object> textureMap = new HashMap<>();
    private HashMap<String, Object> field_28151_c = new HashMap<>();
    private HashMap textureNameToImageMap = new HashMap();
    private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
    private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(1048576);
    private List textureList = new ArrayList();
    private Map<Object, ThreadDownloadImageData> urlToImageDataMap = new HashMap<>();
    private GameSettings options;
    private boolean clampTexture = false;
    private boolean blurTexture = false;
    private TexturePackList texturePack;
    private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);

    public RenderEngine(TexturePackList var1, GameSettings var2) {
        this.texturePack = var1;
        this.options = var2;
        Graphics var3 = this.missingTextureImage.getGraphics();
        var3.setColor(Color.WHITE);
        var3.fillRect(0, 0, 64, 64);
        var3.setColor(Color.BLACK);
        var3.drawString("missingtex", 1, 10);
        var3.dispose();
    }

    public int[] func_28149_a(String var1) {
        TexturePackBase var2 = this.texturePack.selectedTexturePack;
        int[] var3 = (int[]) this.field_28151_c.get(var1);
        if (var3 != null) {
            return var3;
        } else {
            try {
                Object var6 = null;
                int[] var7;
                if (var1.startsWith("##")) {
                    var7 = this.func_28148_b(this.unwrapImageByColumns(this.readTextureImage(var2.getResourceAsStream(var1.substring(2)))));
                } else if (var1.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    var7 = this.func_28148_b(this.readTextureImage(var2.getResourceAsStream(var1.substring(7))));
                    this.clampTexture = false;
                } else if (var1.startsWith("%blur%")) {
                    this.blurTexture = true;
                    var7 = this.func_28148_b(this.readTextureImage(var2.getResourceAsStream(var1.substring(6))));
                    this.blurTexture = false;
                } else {
                    InputStream var8 = var2.getResourceAsStream(var1);
                    if (var8 == null) {
                        var7 = this.func_28148_b(this.missingTextureImage);
                    } else {
                        var7 = this.func_28148_b(this.readTextureImage(var8));
                    }
                }

                this.field_28151_c.put(var1, var7);
                return var7;
            } catch (IOException e) {
                e.printStackTrace();
                int[] var4 = this.func_28148_b(this.missingTextureImage);
                this.field_28151_c.put(var1, var4);
                return var4;
            }
        }
    }

    private int[] func_28148_b(BufferedImage var1) {
        int var2 = var1.getWidth();
        int var3 = var1.getHeight();
        int[] var4 = new int[var2 * var3];
        var1.getRGB(0, 0, var2, var3, var4, 0, var2);
        return var4;
    }

    private int[] func_28147_a(BufferedImage var1, int[] var2) {
        int var3 = var1.getWidth();
        int var4 = var1.getHeight();
        var1.getRGB(0, 0, var3, var4, var2, 0, var3);
        return var2;
    }

    public int getTexture(String var1) {
        TexturePackBase var2 = this.texturePack.selectedTexturePack;
        Integer var3 = (Integer) this.textureMap.get(var1);
        if (var3 != null) {
            return var3;
        } else {
            try {
                this.singleIntBuffer.clear();
                GLAllocation.generateTextureNames(this.singleIntBuffer);
                int var6 = this.singleIntBuffer.get(0);
                if (var1.startsWith("##")) {
                    this.setupTexture(this.unwrapImageByColumns(this.readTextureImage(var2.getResourceAsStream(var1.substring(2)))), var6);
                } else if (var1.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    this.setupTexture(this.readTextureImage(var2.getResourceAsStream(var1.substring(7))), var6);
                    this.clampTexture = false;
                } else if (var1.startsWith("%blur%")) {
                    this.blurTexture = true;
                    this.setupTexture(this.readTextureImage(var2.getResourceAsStream(var1.substring(6))), var6);
                    this.blurTexture = false;
                } else {
                    InputStream var7 = var2.getResourceAsStream(var1);
                    if (var7 == null) {
                        this.setupTexture(this.missingTextureImage, var6);
                    } else {
                        this.setupTexture(this.readTextureImage(var7), var6);
                    }
                }

                this.textureMap.put(var1, var6);
                return var6;
            } catch (IOException e) {
                e.printStackTrace();
                GLAllocation.generateTextureNames(this.singleIntBuffer);
                int var4 = this.singleIntBuffer.get(0);
                this.setupTexture(this.missingTextureImage, var4);
                this.textureMap.put(var1, var4);
                return var4;
            }
        }
    }

    private BufferedImage unwrapImageByColumns(BufferedImage var1) {
        int var2 = var1.getWidth() / 16;
        BufferedImage var3 = new BufferedImage(16, var1.getHeight() * var2, 2);
        Graphics var4 = var3.getGraphics();

        for (int var5 = 0; var5 < var2; ++var5) {
            var4.drawImage(var1, -var5 * 16, var5 * var1.getHeight(), null);
        }

        var4.dispose();
        return var3;
    }

    public int allocateAndSetupTexture(BufferedImage var1) {
        this.singleIntBuffer.clear();
        GLAllocation.generateTextureNames(this.singleIntBuffer);
        int var2 = this.singleIntBuffer.get(0);
        this.setupTexture(var1, var2);
        this.textureNameToImageMap.put(var2, var1);
        return var2;
    }

    public void setupTexture(BufferedImage var1, int var2) {
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var2);
        if (useMipmaps) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9986 /*GL_NEAREST_MIPMAP_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        } else {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9728 /*GL_NEAREST*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        }

        if (this.blurTexture) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9729 /*GL_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9729 /*GL_LINEAR*/);
        }

        if (this.clampTexture) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10496 /*GL_CLAMP*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10496 /*GL_CLAMP*/);
        } else {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10497 /*GL_REPEAT*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10497 /*GL_REPEAT*/);
        }

        int var3 = var1.getWidth();
        int var4 = var1.getHeight();
        int[] var5 = new int[var3 * var4];
        byte[] var6 = new byte[var3 * var4 * 4];
        var1.getRGB(0, 0, var3, var4, var5, 0, var3);

        for (int var7 = 0; var7 < var5.length; ++var7) {
            int var8 = var5[var7] >> 24 & 255;
            int var9 = var5[var7] >> 16 & 255;
            int var10 = var5[var7] >> 8 & 255;
            int var11 = var5[var7] & 255;
            if (this.options != null && this.options.anaglyph) {
                int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
                int var13 = (var9 * 30 + var10 * 70) / 100;
                int var14 = (var9 * 30 + var11 * 70) / 100;
                var9 = var12;
                var10 = var13;
                var11 = var14;
            }

            var6[var7 * 4 + 0] = (byte) var9;
            var6[var7 * 4 + 1] = (byte) var10;
            var6[var7 * 4 + 2] = (byte) var11;
            var6[var7 * 4 + 3] = (byte) var8;
        }

        this.imageData.clear();
        this.imageData.put(var6);
        this.imageData.position(0).limit(var6.length);
        GL11.glTexImage2D(3553 /*GL_TEXTURE_2D*/, 0, 6408 /*GL_RGBA*/, var3, var4, 0, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
        if (useMipmaps) {
            for (int var18 = 1; var18 <= 4; ++var18) {
                int var19 = var3 >> var18 - 1;
                int var20 = var3 >> var18;
                int var21 = var4 >> var18;

                for (int var22 = 0; var22 < var20; ++var22) {
                    for (int var23 = 0; var23 < var21; ++var23) {
                        int var24 = this.imageData.getInt((var22 * 2 + 0 + (var23 * 2 + 0) * var19) * 4);
                        int var25 = this.imageData.getInt((var22 * 2 + 1 + (var23 * 2 + 0) * var19) * 4);
                        int var15 = this.imageData.getInt((var22 * 2 + 1 + (var23 * 2 + 1) * var19) * 4);
                        int var16 = this.imageData.getInt((var22 * 2 + 0 + (var23 * 2 + 1) * var19) * 4);
                        int var17 = this.weightedAverageColor(this.weightedAverageColor(var24, var25), this.weightedAverageColor(var15, var16));
                        this.imageData.putInt((var22 + var23 * var20) * 4, var17);
                    }
                }

                GL11.glTexImage2D(3553 /*GL_TEXTURE_2D*/, var18, 6408 /*GL_RGBA*/, var20, var21, 0, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
            }
        }

    }

    public void func_28150_a(int[] var1, int var2, int var3, int var4) {
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var4);
        if (useMipmaps) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9986 /*GL_NEAREST_MIPMAP_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        } else {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9728 /*GL_NEAREST*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        }

        if (this.blurTexture) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9729 /*GL_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9729 /*GL_LINEAR*/);
        }

        if (this.clampTexture) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10496 /*GL_CLAMP*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10496 /*GL_CLAMP*/);
        } else {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10497 /*GL_REPEAT*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10497 /*GL_REPEAT*/);
        }

        byte[] var5 = new byte[var2 * var3 * 4];

        for (int var6 = 0; var6 < var1.length; ++var6) {
            int var7 = var1[var6] >> 24 & 255;
            int var8 = var1[var6] >> 16 & 255;
            int var9 = var1[var6] >> 8 & 255;
            int var10 = var1[var6] & 255;
            if (this.options != null && this.options.anaglyph) {
                int var11 = (var8 * 30 + var9 * 59 + var10 * 11) / 100;
                int var12 = (var8 * 30 + var9 * 70) / 100;
                int var13 = (var8 * 30 + var10 * 70) / 100;
                var8 = var11;
                var9 = var12;
                var10 = var13;
            }

            var5[var6 * 4 + 0] = (byte) var8;
            var5[var6 * 4 + 1] = (byte) var9;
            var5[var6 * 4 + 2] = (byte) var10;
            var5[var6 * 4 + 3] = (byte) var7;
        }

        this.imageData.clear();
        this.imageData.put(var5);
        this.imageData.position(0).limit(var5.length);
        GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, 0, 0, var2, var3, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
    }

    public void deleteTexture(int var1) {
        this.textureNameToImageMap.remove(var1);
        this.singleIntBuffer.clear();
        this.singleIntBuffer.put(var1);
        this.singleIntBuffer.flip();
        GL11.glDeleteTextures(this.singleIntBuffer);
    }

    public int getTextureForDownloadableImage(String var1, String var2) {
        ThreadDownloadImageData var3 = this.urlToImageDataMap.get(var1);
        if (var3 != null && var3.image != null && !var3.textureSetupComplete) {
            if (var3.textureName < 0) {
                var3.textureName = this.allocateAndSetupTexture(var3.image);
            } else {
                this.setupTexture(var3.image, var3.textureName);
            }

            var3.textureSetupComplete = true;
        }

        if (var3 != null && var3.textureName >= 0) {
            return var3.textureName;
        } else {
            return var2 == null ? -1 : this.getTexture(var2);
        }
    }

    public ThreadDownloadImageData obtainImageData(String var1, ImageBuffer var2) {
        ThreadDownloadImageData var3 = this.urlToImageDataMap.get(var1);
        if (var3 == null) {
            this.urlToImageDataMap.put(var1, new ThreadDownloadImageData(var1, var2));
        } else {
            ++var3.referenceCount;
        }

        return var3;
    }

    public void releaseImageData(String var1) {
        ThreadDownloadImageData var2 = this.urlToImageDataMap.get(var1);
        if (var2 != null) {
            --var2.referenceCount;
            if (var2.referenceCount == 0) {
                if (var2.textureName >= 0) {
                    this.deleteTexture(var2.textureName);
                }

                this.urlToImageDataMap.remove(var1);
            }
        }

    }

    public void registerTextureFX(TextureFX var1) {
        this.textureList.add(var1);
        var1.onTick();
    }

    public void updateDynamicTextures() {
        for (int var1 = 0; var1 < this.textureList.size(); ++var1) {
            TextureFX var2 = (TextureFX) this.textureList.get(var1);
            var2.anaglyphEnabled = this.options.anaglyph;
            var2.onTick();
            this.imageData.clear();
            this.imageData.put(var2.imageData);
            this.imageData.position(0).limit(var2.imageData.length);
            var2.bindImage(this);

            for (int var3 = 0; var3 < var2.tileSize; ++var3) {
                for (int var4 = 0; var4 < var2.tileSize; ++var4) {
                    GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, var2.iconIndex % 16 * 16 + var3 * 16, var2.iconIndex / 16 * 16 + var4 * 16, 16, 16, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
                    if (useMipmaps) {
                        for (int var5 = 1; var5 <= 4; ++var5) {
                            int var6 = 16 >> var5 - 1;
                            int var7 = 16 >> var5;

                            for (int var8 = 0; var8 < var7; ++var8) {
                                for (int var9 = 0; var9 < var7; ++var9) {
                                    int var10 = this.imageData.getInt((var8 * 2 + 0 + (var9 * 2 + 0) * var6) * 4);
                                    int var11 = this.imageData.getInt((var8 * 2 + 1 + (var9 * 2 + 0) * var6) * 4);
                                    int var12 = this.imageData.getInt((var8 * 2 + 1 + (var9 * 2 + 1) * var6) * 4);
                                    int var13 = this.imageData.getInt((var8 * 2 + 0 + (var9 * 2 + 1) * var6) * 4);
                                    int var14 = this.averageColor(this.averageColor(var10, var11), this.averageColor(var12, var13));
                                    this.imageData.putInt((var8 + var9 * var7) * 4, var14);
                                }
                            }

                            GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, var5, var2.iconIndex % 16 * var7, var2.iconIndex / 16 * var7, var7, var7, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
                        }
                    }
                }
            }
        }

        for (int var15 = 0; var15 < this.textureList.size(); ++var15) {
            TextureFX var16 = (TextureFX) this.textureList.get(var15);
            if (var16.textureId > 0) {
                this.imageData.clear();
                this.imageData.put(var16.imageData);
                this.imageData.position(0).limit(var16.imageData.length);
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var16.textureId);
                GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, 0, 0, 16, 16, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
                if (useMipmaps) {
                    for (int var17 = 1; var17 <= 4; ++var17) {
                        int var18 = 16 >> var17 - 1;
                        int var19 = 16 >> var17;

                        for (int var20 = 0; var20 < var19; ++var20) {
                            for (int var21 = 0; var21 < var19; ++var21) {
                                int var22 = this.imageData.getInt((var20 * 2 + 0 + (var21 * 2 + 0) * var18) * 4);
                                int var23 = this.imageData.getInt((var20 * 2 + 1 + (var21 * 2 + 0) * var18) * 4);
                                int var24 = this.imageData.getInt((var20 * 2 + 1 + (var21 * 2 + 1) * var18) * 4);
                                int var25 = this.imageData.getInt((var20 * 2 + 0 + (var21 * 2 + 1) * var18) * 4);
                                int var26 = this.averageColor(this.averageColor(var22, var23), this.averageColor(var24, var25));
                                this.imageData.putInt((var20 + var21 * var19) * 4, var26);
                            }
                        }

                        GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, var17, 0, 0, var19, var19, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
                    }
                }
            }
        }

    }

    private int averageColor(int var1, int var2) {
        int var3 = (var1 & -16777216) >> 24 & 255;
        int var4 = (var2 & -16777216) >> 24 & 255;
        return (var3 + var4 >> 1 << 24) + ((var1 & 16711422) + (var2 & 16711422) >> 1);
    }

    private int weightedAverageColor(int var1, int var2) {
        int var3 = (var1 & -16777216) >> 24 & 255;
        int var4 = (var2 & -16777216) >> 24 & 255;
        short var5 = 255;
        if (var3 + var4 == 0) {
            var3 = 1;
            var4 = 1;
            var5 = 0;
        }

        int var6 = (var1 >> 16 & 255) * var3;
        int var7 = (var1 >> 8 & 255) * var3;
        int var8 = (var1 & 255) * var3;
        int var9 = (var2 >> 16 & 255) * var4;
        int var10 = (var2 >> 8 & 255) * var4;
        int var11 = (var2 & 255) * var4;
        int var12 = (var6 + var9) / (var3 + var4);
        int var13 = (var7 + var10) / (var3 + var4);
        int var14 = (var8 + var11) / (var3 + var4);
        return var5 << 24 | var12 << 16 | var13 << 8 | var14;
    }

    public void refreshTextures() {
        TexturePackBase var1 = this.texturePack.selectedTexturePack;

        for (Object o : this.textureNameToImageMap.keySet()) {
            int var3 = (Integer) o;
            BufferedImage var4 = (BufferedImage) this.textureNameToImageMap.get(var3);
            this.setupTexture(var4, var3);
        }

        for (ThreadDownloadImageData var11 : this.urlToImageDataMap.values()) {
            var11.textureSetupComplete = false;
        }

        for (String var12 : this.textureMap.keySet()) {
            try {
                BufferedImage var14;
                if (var12.startsWith("##")) {
                    var14 = this.unwrapImageByColumns(this.readTextureImage(var1.getResourceAsStream(var12.substring(2))));
                } else if (var12.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    var14 = this.readTextureImage(var1.getResourceAsStream(var12.substring(7)));
                } else if (var12.startsWith("%blur%")) {
                    this.blurTexture = true;
                    var14 = this.readTextureImage(var1.getResourceAsStream(var12.substring(6)));
                } else {
                    var14 = this.readTextureImage(var1.getResourceAsStream(var12));
                }

                int var5 = (Integer) this.textureMap.get(var12);
                this.setupTexture(var14, var5);
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String var13 : this.field_28151_c.keySet()) {
            try {
                BufferedImage var15;
                if (var13.startsWith("##")) {
                    var15 = this.unwrapImageByColumns(this.readTextureImage(var1.getResourceAsStream(var13.substring(2))));
                } else if (var13.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    var15 = this.readTextureImage(var1.getResourceAsStream(var13.substring(7)));
                } else if (var13.startsWith("%blur%")) {
                    this.blurTexture = true;
                    var15 = this.readTextureImage(var1.getResourceAsStream(var13.substring(6)));
                } else {
                    var15 = this.readTextureImage(var1.getResourceAsStream(var13));
                }

                this.func_28147_a(var15, (int[]) this.field_28151_c.get(var13));
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private BufferedImage readTextureImage(InputStream var1) throws IOException {
        BufferedImage var2 = ImageIO.read(var1);
        var1.close();
        return var2;
    }

    public void bindTexture(int var1) {
        if (var1 >= 0) {
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var1);
        }
    }
}
