package net.minecraft.client.render;

import net.hypnosis.render.Tessellator;
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

    public FontRenderer(GameSettings settings, String fontResource, RenderEngine renderEngine) {
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

            int i1 = i % 16 * 8;
            int i2 = i / 16 * 8;

            float f1 = 7.99F;
            float f2 = 0.0F;
            float f3 = 0.0F;

            tess.addVertexWithUV(0.0D, 0.0F + f1, 0.0D, (float) i1 / 128.0F + f2, ((float) i2 + f1) / 128.0F + f3);
            tess.addVertexWithUV(0.0F + f1, 0.0F + f1, 0.0D, ((float) i1 + f1) / 128.0F + f2, ((float) i2 + f1) / 128.0F + f3);
            tess.addVertexWithUV(0.0F + f1, 0.0D, 0.0D, ((float) i1 + f1) / 128.0F + f2, (float) i2 / 128.0F + f3);
            tess.addVertexWithUV(0.0D, 0.0D, 0.0D, (float) i1 / 128.0F + f2, (float) i2 / 128.0F + f3);
            tess.draw();

            GL11.glTranslatef((float) this.charWidth[i], 0.0F, 0.0F);
            GL11.glEndList();
        }

        for (int i = 0; i < 32; ++i) {
            int mod = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + mod;
            int g = (i >> 1 & 1) * 170 + mod;
            int b = (i & 1) * 170 + mod;
            if (i == 6)
                r += 85;

            boolean var31 = i >= 16;
            if (settings.anaglyph) {
                int tempRed = (r * 30 + g * 59 + b * 11) / 100;
                int tempGreen = (r * 30 + g * 70) / 100;
                int tempBlue = (r * 30 + b * 70) / 100;

                r = tempRed;
                g = tempGreen;
                b = tempBlue;
            }

            if (var31) {
                r /= 4;
                g /= 4;
                b /= 4;
            }

            GL11.glNewList(this.fontDisplayLists + 256 + i, GL11.GL_COMPILE);
            GL11.glColor3f((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F);
            GL11.glEndList();
        }
    }

    public void drawStringWithShadow(String text, int x, int y, int color) {
        this.renderString(text, x + 1, y + 1, color, true);
        this.drawString(text, x, y, color);
    }

    public void drawString(String text, int x, int y, int color) {
        this.renderString(text, x, y, color, false);
    }

    public void renderString(String text, int x, int y, int color, boolean isShadow) {
        if (text == null)
            return;

        if (isShadow) {
            int dark = color & -16777216;
            color = (color & 16579836) >> 2;
            color = color + dark;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) (color >> 24 & 255) / 255.0F;
        if (a == 0.0F)
            a = 1.0F;

        GL11.glColor4f(r, g, b, a);
        this.buffer.clear();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, 0.0F);

        for (int i = 0; i < text.length(); ++i) {
            for (; text.length() > i + 1 && text.charAt(i) == 167; i += 2) {
                int ind = "0123456789abcdef".indexOf(text.toLowerCase().charAt(i + 1));
                if (ind < 0 || ind > 15)
                    ind = 15;

                this.buffer.put(this.fontDisplayLists + 256 + ind + (isShadow ? 16 : 0));
                if (this.buffer.remaining() == 0) {
                    this.buffer.flip();
                    GL11.glCallLists(this.buffer);
                    this.buffer.clear();
                }
            }

            if (i < text.length()) {
                int index = ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(text.charAt(i));
                if (index >= 0)
                    this.buffer.put(this.fontDisplayLists + index + 32);
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

    public int getStringWidth(String text) {
        if (text == null)
            return 0;

        int width = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == 167) {
                ++i;
                continue;
            }

            int indexOf = ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(text.charAt(i));
            if (indexOf >= 0)
                width += this.charWidth[indexOf + 32];
        }

        return width;
    }

    public void func_27278_a(String var1, int var2, int var3, int var4, int var5) {
        String[] var6 = var1.split("\n");
        if (var6.length > 1) {
            for (int i = 0; i < var6.length; ++i) {
                this.func_27278_a(var6[i], var2, var3, var4, var5);
                var3 += this.func_27277_a(var6[i], var4);
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

    public int func_27277_a(String s, int var2) {
        String[] lines = s.split("\n");
        if (lines.length > 1) {
            int linesLength = 0;

            for (int i = 0; i < lines.length; ++i) {
                linesLength += this.func_27277_a(lines[i], var2);
            }

            return linesLength;
        }

        String[] var4 = s.split(" ");
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
