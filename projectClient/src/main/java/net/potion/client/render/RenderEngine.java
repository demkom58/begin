package net.potion.client.render;

import net.potion.client.GameSettings;
import net.potion.client.render.texture.TextureFX;
import net.potion.client.render.texture.TexturePackBase;
import net.potion.client.render.texture.TexturePackList;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderEngine {
    public static boolean useMipmaps = false;

    private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);

    private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
    private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(1048576);

    private List<TextureFX> textures = new ArrayList<>();

    private Map<String, Integer> nameToId = new HashMap<>();
    private Map<String, int[]> nameToBuff = new HashMap<>();
    private Map<Integer, BufferedImage> nameToImage = new HashMap<>();
    private Map<Object, ThreadDownloadImageData> urlToImageData = new HashMap<>();

    private GameSettings options;
    private TexturePackList texturePack;

    private boolean clampTexture = false;
    private boolean blurTexture = false;


    public RenderEngine(TexturePackList var1, GameSettings var2) {
        this.texturePack = var1;
        this.options = var2;
        Graphics graphics = this.missingTextureImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 64, 64);
        graphics.setColor(Color.BLACK);
        graphics.drawString("missingtex", 1, 10);
        graphics.dispose();
    }

    public int[] loadTexture(String resource) {
        TexturePackBase pack = this.texturePack.selectedTexturePack;
        int[] loaded = this.nameToBuff.get(resource);

        if (loaded != null)
            return loaded;

        try {
            if (resource.startsWith("##")) {
                loaded = this.convertImage(this.unwrapImageByColumns(this.readTextureImage(pack.getResourceAsStream(resource.substring(2)))));
            } else if (resource.startsWith("%clamp%")) {
                this.clampTexture = true;
                loaded = this.convertImage(this.readTextureImage(pack.getResourceAsStream(resource.substring(7))));
                this.clampTexture = false;
            } else if (resource.startsWith("%blur%")) {
                this.blurTexture = true;
                loaded = this.convertImage(this.readTextureImage(pack.getResourceAsStream(resource.substring(6))));
                this.blurTexture = false;
            } else {
                InputStream inputStream = pack.getResourceAsStream(resource);
                if (inputStream == null)
                    loaded = this.convertImage(this.missingTextureImage);
                else
                    loaded = this.convertImage(this.readTextureImage(inputStream));
            }

            this.nameToBuff.put(resource, loaded);
            return loaded;
        } catch (IOException e) {
            e.printStackTrace();
            int[] missingTextureBuff = this.convertImage(this.missingTextureImage);
            this.nameToBuff.put(resource, missingTextureBuff);
            return missingTextureBuff;
        }
    }

    private int[] convertImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] buff = new int[width * height];
        image.getRGB(0, 0, width, height, buff, 0, width);
        return buff;
    }

    private int[] writeImage(BufferedImage image, int[] input) {
        int width = image.getWidth();
        int height = image.getHeight();
        image.getRGB(0, 0, width, height, input, 0, width);
        return input;
    }

    public int getTexture(String resource) {
        TexturePackBase pack = this.texturePack.selectedTexturePack;
        Integer id = this.nameToId.get(resource);

        if (id != null)
            return id;

        try {
            this.singleIntBuffer.clear();
            GLAllocation.generateTextureNames(this.singleIntBuffer);
            int var6 = this.singleIntBuffer.get(0);
            if (resource.startsWith("##")) {
                this.setupTexture(this.unwrapImageByColumns(this.readTextureImage(pack.getResourceAsStream(resource.substring(2)))), var6);
            } else if (resource.startsWith("%clamp%")) {
                this.clampTexture = true;
                this.setupTexture(this.readTextureImage(pack.getResourceAsStream(resource.substring(7))), var6);
                this.clampTexture = false;
            } else if (resource.startsWith("%blur%")) {
                this.blurTexture = true;
                this.setupTexture(this.readTextureImage(pack.getResourceAsStream(resource.substring(6))), var6);
                this.blurTexture = false;
            } else {
                InputStream inputStream = pack.getResourceAsStream(resource);
                if (inputStream == null) {
                    this.setupTexture(this.missingTextureImage, var6);
                } else {
                    this.setupTexture(this.readTextureImage(inputStream), var6);
                }
            }

            this.nameToId.put(resource, var6);
            return var6;
        } catch (IOException e) {
            e.printStackTrace();
            GLAllocation.generateTextureNames(this.singleIntBuffer);
            int var4 = this.singleIntBuffer.get(0);
            this.setupTexture(this.missingTextureImage, var4);
            this.nameToId.put(resource, var4);
            return var4;
        }
    }

    private BufferedImage unwrapImageByColumns(BufferedImage image) {
        int var2 = image.getWidth() / 16;
        BufferedImage var3 = new BufferedImage(16, image.getHeight() * var2, 2);
        Graphics graphics = var3.getGraphics();

        for (int i = 0; i < var2; ++i) {
            graphics.drawImage(image, -i * 16, i * image.getHeight(), null);
        }

        graphics.dispose();
        return var3;
    }

    public int allocateAndSetupTexture(BufferedImage var1) {
        this.singleIntBuffer.clear();
        GLAllocation.generateTextureNames(this.singleIntBuffer);
        int var2 = this.singleIntBuffer.get(0);
        this.setupTexture(var1, var2);
        this.nameToImage.put(var2, var1);
        return var2;
    }

    public void setupTexture(BufferedImage bufferedImage, int var2) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
        if (useMipmaps) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }

        if (this.blurTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }

        if (this.clampTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] var5 = new int[width * height];
        byte[] var6 = new byte[width * height * 4];
        bufferedImage.getRGB(0, 0, width, height, var5, 0, width);

        for (int i = 0; i < var5.length; ++i) {
            int var8 = var5[i] >> 24 & 255;
            int var9 = var5[i] >> 16 & 255;
            int var10 = var5[i] >> 8 & 255;
            int var11 = var5[i] & 255;
            if (this.options != null && this.options.anaglyph) {
                int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
                int var13 = (var9 * 30 + var10 * 70) / 100;
                int var14 = (var9 * 30 + var11 * 70) / 100;
                var9 = var12;
                var10 = var13;
                var11 = var14;
            }

            var6[i * 4] = (byte) var9;
            var6[i * 4 + 1] = (byte) var10;
            var6[i * 4 + 2] = (byte) var11;
            var6[i * 4 + 3] = (byte) var8;
        }

        this.imageData.clear();
        this.imageData.put(var6);
        this.imageData.position(0).limit(var6.length);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
        if (!useMipmaps)
            return;

        for (int i = 1; i <= 4; ++i) {
            int mul = width >> i - 1;

            int eX = width >> i;
            int eY = height >> i;

            for (int x = 0; x < eX; ++x) {
                for (int y = 0; y < eY; ++y) {
                    int var24 = this.imageData.getInt((x * 2 + (y * 2) * mul) * 4);
                    int var25 = this.imageData.getInt((x * 2 + 1 + (y * 2) * mul) * 4);
                    int var15 = this.imageData.getInt((x * 2 + 1 + (y * 2 + 1) * mul) * 4);
                    int var16 = this.imageData.getInt((x * 2 + (y * 2 + 1) * mul) * 4);
                    int var17 = this.weightedAverageColor(this.weightedAverageColor(var24, var25), this.weightedAverageColor(var15, var16));
                    this.imageData.putInt((x + y * eX) * 4, var17);
                }
            }

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, i, GL11.GL_RGBA, eX, eY, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
        }
    }

    public void func_28150_a(int[] var1, int var2, int var3, int var4) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
        if (useMipmaps) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }

        if (this.blurTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }

        if (this.clampTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
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

            var5[var6 * 4] = (byte) var8;
            var5[var6 * 4 + 1] = (byte) var9;
            var5[var6 * 4 + 2] = (byte) var10;
            var5[var6 * 4 + 3] = (byte) var7;
        }

        this.imageData.clear();
        this.imageData.put(var5);
        this.imageData.position(0).limit(var5.length);
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, var2, var3, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
    }

    public void deleteTexture(int var1) {
        this.nameToImage.remove(var1);
        this.singleIntBuffer.clear();
        this.singleIntBuffer.put(var1);
        this.singleIntBuffer.flip();
        GL11.glDeleteTextures(this.singleIntBuffer);
    }

    public int getTextureForDownloadableImage(String url, String name) {
        ThreadDownloadImageData imageData = this.urlToImageData.get(url);
        if (imageData != null && imageData.image != null && !imageData.textureSetupComplete) {
            if (imageData.textureName < 0) {
                imageData.textureName = this.allocateAndSetupTexture(imageData.image);
            } else {
                this.setupTexture(imageData.image, imageData.textureName);
            }

            imageData.textureSetupComplete = true;
        }

        if (imageData != null && imageData.textureName >= 0)
            return imageData.textureName;

        return name == null ? -1 : this.getTexture(name);
    }

    public ThreadDownloadImageData obtainImageData(String url, ImageBuffer buffer) {
        ThreadDownloadImageData imageData = this.urlToImageData.get(url);

        if (imageData == null)
            this.urlToImageData.put(url, new ThreadDownloadImageData(url, buffer));
        else
            ++imageData.referenceCount;

        return imageData;
    }

    public void releaseImageData(String url) {
        ThreadDownloadImageData imageData = this.urlToImageData.get(url);
        if (imageData == null)
            return;

        --imageData.referenceCount;
        if (imageData.referenceCount == 0) {
            if (imageData.textureName >= 0) {
                this.deleteTexture(imageData.textureName);
            }

            this.urlToImageData.remove(url);
        }
    }

    public void registerTextureFX(TextureFX texture) {
        this.textures.add(texture);
        texture.onTick();
    }

    public void updateDynamicTextures() {
        for (int i = 0; i < this.textures.size(); ++i) {
            TextureFX texture = this.textures.get(i);
            texture.anaglyphEnabled = this.options.anaglyph;
            texture.onTick();
            this.imageData.clear();
            this.imageData.put(texture.imageData);
            this.imageData.position(0).limit(texture.imageData.length);
            texture.bindImage(this);

            for (int x = 0; x < texture.tileSize; ++x) {
                for (int y = 0; y < texture.tileSize; ++y) {
                    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, texture.iconIndex % 16 * 16 + x * 16, texture.iconIndex / 16 * 16 + y * 16, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                    if (!useMipmaps)
                        continue;

                    for (int var5 = 1; var5 <= 4; ++var5) {
                        int var6 = 16 >> var5 - 1;
                        int var7 = 16 >> var5;

                        for (int var8 = 0; var8 < var7; ++var8) {
                            for (int var9 = 0; var9 < var7; ++var9) {
                                int var10 = this.imageData.getInt((var8 * 2 + (var9 * 2) * var6) * 4);
                                int var11 = this.imageData.getInt((var8 * 2 + 1 + (var9 * 2) * var6) * 4);
                                int var12 = this.imageData.getInt((var8 * 2 + 1 + (var9 * 2 + 1) * var6) * 4);
                                int var13 = this.imageData.getInt((var8 * 2 + (var9 * 2 + 1) * var6) * 4);
                                int var14 = this.averageColor(this.averageColor(var10, var11), this.averageColor(var12, var13));
                                this.imageData.putInt((var8 + var9 * var7) * 4, var14);
                            }
                        }

                        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, var5, texture.iconIndex % 16 * var7, texture.iconIndex / 16 * var7, var7, var7, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                    }
                }
            }
        }

        for (int var15 = 0; var15 < this.textures.size(); ++var15) {
            TextureFX var16 = this.textures.get(var15);
            if (var16.textureId > 0) {
                this.imageData.clear();
                this.imageData.put(var16.imageData);
                this.imageData.position(0).limit(var16.imageData.length);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, var16.textureId);
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
                if (useMipmaps) {
                    for (int var17 = 1; var17 <= 4; ++var17) {
                        int var18 = 16 >> var17 - 1;
                        int var19 = 16 >> var17;

                        for (int var20 = 0; var20 < var19; ++var20) {
                            for (int var21 = 0; var21 < var19; ++var21) {
                                int var22 = this.imageData.getInt((var20 * 2 + (var21 * 2) * var18) * 4);
                                int var23 = this.imageData.getInt((var20 * 2 + 1 + (var21 * 2) * var18) * 4);
                                int var24 = this.imageData.getInt((var20 * 2 + 1 + (var21 * 2 + 1) * var18) * 4);
                                int var25 = this.imageData.getInt((var20 * 2 + (var21 * 2 + 1) * var18) * 4);
                                int var26 = this.averageColor(this.averageColor(var22, var23), this.averageColor(var24, var25));
                                this.imageData.putInt((var20 + var21 * var19) * 4, var26);
                            }
                        }

                        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, var17, 0, 0, var19, var19, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
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

        for (Object o : this.nameToImage.keySet()) {
            int var3 = (Integer) o;
            BufferedImage var4 = this.nameToImage.get(var3);
            this.setupTexture(var4, var3);
        }

        for (ThreadDownloadImageData var11 : this.urlToImageData.values()) {
            var11.textureSetupComplete = false;
        }

        for (String var12 : this.nameToId.keySet()) {
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

                int var5 = this.nameToId.get(var12);
                this.setupTexture(var14, var5);
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String var13 : this.nameToBuff.keySet()) {
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

                this.writeImage(var15, this.nameToBuff.get(var13));
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private BufferedImage readTextureImage(InputStream inputStream) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        inputStream.close();
        return image;
    }

    public void bindTexture(int id) {
        if (id >= 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        }
    }
}
