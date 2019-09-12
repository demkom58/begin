package net.minecraft.client.render;

import net.minecraft.client.GameSettings;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;

public class FontRenderer {
    public int fontTextureName;
    private int[] charWidth = new int[256];
    private int fontDisplayLists;
    private IntBuffer buffer = GLAllocation.createDirectIntBuffer(GL11.GL_FRONT_LEFT);

    public FontRenderer(GameSettings gameSettings, String fontResource, RenderEngine renderEngine) {
        BufferedImage image;
        try {
            image = ImageIO.read(RenderEngine.class.getResourceAsStream(fontResource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int[] map = new int[width * height];
        image.getRGB(0, 0, width, height, map, 0, width);

        for (int var8 = 0; var8 < 256; ++var8) {
            int var9 = var8 % 16;
            int var10 = var8 / 16;

            int var11;
            for (var11 = 7; var11 >= 0; --var11) {
                int var12 = var9 * 8 + var11;
                boolean var13 = true;

                for (int var14 = 0; var14 < 8; ++var14) {
                    int var15 = (var10 * 8 + var14) * width;
                    int var16 = map[var12 + var15] & 255;
                    if (var16 > 0) {
                        var13 = false;
                        break;
                    }
                }

                if (!var13) {
                    break;
                }
            }

            if (var8 == 32) {
                var11 = 2;
            }

            this.charWidth[var8] = var11 + 2;
        }

        this.fontTextureName = renderEngine.allocateAndSetupTexture(image);
        this.fontDisplayLists = GLAllocation.generateDisplayLists(288);
        Tessellator tess = Tessellator.INSTANCE;

        for (int i = 0; i < 256; ++i) {
            GL11.glNewList(this.fontDisplayLists + i, GL11.GL_COMPILE);
            tess.startDrawingQuads();
            int var22 = i % 16 * 8;
            int var24 = i / 16 * 8;
            float var26 = 7.99F;
            float var28 = 0.0F;
            float var30 = 0.0F;
            tess.addVertexWithUV(0.0D, 0.0F + var26, 0.0D, (float) var22 / 128.0F + var28, ((float) var24 + var26) / 128.0F + var30);
            tess.addVertexWithUV(0.0F + var26, 0.0F + var26, 0.0D, ((float) var22 + var26) / 128.0F + var28, ((float) var24 + var26) / 128.0F + var30);
            tess.addVertexWithUV(0.0F + var26, 0.0D, 0.0D, ((float) var22 + var26) / 128.0F + var28, (float) var24 / 128.0F + var30);
            tess.addVertexWithUV(0.0D, 0.0D, 0.0D, (float) var22 / 128.0F + var28, (float) var24 / 128.0F + var30);
            tess.draw();
            GL11.glTranslatef((float) this.charWidth[i], 0.0F, 0.0F);
            GL11.glEndList();
        }

        for (int i = 0; i < 32; ++i) {
            int var23 = (i >> 3 & 1) * 85;
            int var25 = (i >> 2 & 1) * 170 + var23;
            int var27 = (i >> 1 & 1) * 170 + var23;
            int var29 = (i & 1) * 170 + var23;
            if (i == 6) {
                var25 += 85;
            }

            boolean var31 = i >= 16;
            if (gameSettings.anaglyph) {
                int var32 = (var25 * 30 + var27 * 59 + var29 * 11) / 100;
                int var33 = (var25 * 30 + var27 * 70) / 100;
                int var17 = (var25 * 30 + var29 * 70) / 100;
                var25 = var32;
                var27 = var33;
                var29 = var17;
            }

            if (var31) {
                var25 /= 4;
                var27 /= 4;
                var29 /= 4;
            }

            GL11.glNewList(this.fontDisplayLists + 256 + i, GL11.GL_COMPILE);
            GL11.glColor3f((float) var25 / 255.0F, (float) var27 / 255.0F, (float) var29 / 255.0F);
            GL11.glEndList();
        }

    }

    public void drawStringWithShadow(String text, int var2, int var3, int var4) {
        this.renderString(text, var2 + 1, var3 + 1, var4, true);
        this.drawString(text, var2, var3, var4);
    }

    public void drawString(String var1, int var2, int var3, int var4) {
        this.renderString(var1, var2, var3, var4, false);
    }

    public void renderString(String str, int x, int y, int var4, boolean var5) {
        if (str == null)
            return;

        if (var5) {
            int var6 = var4 & -16777216;
            var4 = (var4 & 16579836) >> 2;
            var4 = var4 + var6;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
        float var11 = (float) (var4 >> 16 & 255) / 255.0F;
        float var7 = (float) (var4 >> 8 & 255) / 255.0F;
        float var8 = (float) (var4 & 255) / 255.0F;
        float var9 = (float) (var4 >> 24 & 255) / 255.0F;
        if (var9 == 0.0F) {
            var9 = 1.0F;
        }

        GL11.glColor4f(var11, var7, var8, var9);
        this.buffer.clear();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, 0.0F);

        for (int i = 0; i < str.length(); ++i) {
            for (; str.length() > i + 1 && str.charAt(i) == 167; i += 2) {
                int var13 = "0123456789abcdef".indexOf(str.toLowerCase().charAt(i + 1));
                if (var13 < 0 || var13 > 15) {
                    var13 = 15;
                }

                this.buffer.put(this.fontDisplayLists + 256 + var13 + (var5 ? 16 : 0));
                if (this.buffer.remaining() == 0) {
                    this.buffer.flip();
                    GL11.glCallLists(this.buffer);
                    this.buffer.clear();
                }
            }

            if (i < str.length()) {
                int var14 = ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(str.charAt(i));
                if (var14 >= 0) {
                    this.buffer.put(this.fontDisplayLists + var14 + 32);
                }
            }

            if (this.buffer.remaining() == 0) {
                this.buffer.flip();
                GL11.glCallLists(this.buffer);
                this.buffer.clear();
            }
        }

        this.buffer.flip();
        GL11.glCallLists(this.buffer);
        GL11.glPopMatrix();
    }

    public int getStringWidth(String str) {
        if (str == null)
            return 0;

        int width = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == 167) {
                ++i;
                continue;
            }

            int indexOf = ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(str.charAt(i));
            if (indexOf >= 0) {
                width += this.charWidth[indexOf + 32];
            }

        }

        return width;
    }

    public void func_27278_a(String var1, int var2, int var3, int var4, int var5) {
        String[] var6 = var1.split("\n");
        if (var6.length > 1) {
            for (int var11 = 0; var11 < var6.length; ++var11) {
                this.func_27278_a(var6[var11], var2, var3, var4, var5);
                var3 += this.func_27277_a(var6[var11], var4);
            }

        } else {
            String[] var7 = var1.split(" ");
            int var8 = 0;

            while (var8 < var7.length) {
                String var9;
                for (var9 = var7[var8++] + " "; var8 < var7.length && this.getStringWidth(var9 + var7[var8]) < var4; var9 = var9 + var7[var8++] + " ") {
                }

                int var10;
                for (; this.getStringWidth(var9) > var4; var9 = var9.substring(var10)) {
                    for (var10 = 0; this.getStringWidth(var9.substring(0, var10 + 1)) <= var4; ++var10) {
                    }

                    if (var9.substring(0, var10).trim().length() > 0) {
                        this.drawString(var9.substring(0, var10), var2, var3, var5);
                        var3 += 8;
                    }
                }

                if (var9.trim().length() > 0) {
                    this.drawString(var9, var2, var3, var5);
                    var3 += 8;
                }
            }

        }
    }

    public int func_27277_a(String var1, int var2) {
        String[] lines = var1.split("\n");
        if (lines.length > 1) {
            int var9 = 0;

            for (int i = 0; i < lines.length; ++i) {
                var9 += this.func_27277_a(lines[i], var2);
            }

            return var9;
        }

        String[] var4 = var1.split(" ");
        int var5 = 0;
        int var6 = 0;

        while (var5 < var4.length) {
            String var7;
            for (var7 = var4[var5++] + " "; var5 < var4.length && this.getStringWidth(var7 + var4[var5]) < var2; var7 = var7 + var4[var5++] + " ") {
            }

            int var8;
            for (; this.getStringWidth(var7) > var2; var7 = var7.substring(var8)) {
                for (var8 = 0; this.getStringWidth(var7.substring(0, var8 + 1)) <= var2; ++var8) {
                }

                if (var7.substring(0, var8).trim().length() > 0) {
                    var6 += 8;
                }
            }

            if (var7.trim().length() > 0) {
                var6 += 8;
            }
        }

        if (var6 < 8) {
            var6 += 8;
        }

        return var6;
    }
}
