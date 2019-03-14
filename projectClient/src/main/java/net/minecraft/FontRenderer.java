package net.minecraft;

import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;

public class FontRenderer {
    public int fontTextureName = 0;
    private int[] charWidth = new int[256];
    private int fontDisplayLists;
    private IntBuffer buffer = GLAllocation.createDirectIntBuffer(1024 /*GL_FRONT_LEFT*/);

    public FontRenderer(GameSettings var1, String var2, RenderEngine var3) {
        BufferedImage var4;
        try {
            var4 = ImageIO.read(RenderEngine.class.getResourceAsStream(var2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int var5 = var4.getWidth();
        int var6 = var4.getHeight();
        int[] var7 = new int[var5 * var6];
        var4.getRGB(0, 0, var5, var6, var7, 0, var5);

        for (int var8 = 0; var8 < 256; ++var8) {
            int var9 = var8 % 16;
            int var10 = var8 / 16;

            int var11;
            for (var11 = 7; var11 >= 0; --var11) {
                int var12 = var9 * 8 + var11;
                boolean var13 = true;

                for (int var14 = 0; var14 < 8 && var13; ++var14) {
                    int var15 = (var10 * 8 + var14) * var5;
                    int var16 = var7[var12 + var15] & 255;
                    if (var16 > 0) {
                        var13 = false;
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

        this.fontTextureName = var3.allocateAndSetupTexture(var4);
        this.fontDisplayLists = GLAllocation.generateDisplayLists(288);
        Tessellator var19 = Tessellator.instance;

        for (int var20 = 0; var20 < 256; ++var20) {
            GL11.glNewList(this.fontDisplayLists + var20, 4864 /*GL_COMPILE*/);
            var19.startDrawingQuads();
            int var22 = var20 % 16 * 8;
            int var24 = var20 / 16 * 8;
            float var26 = 7.99F;
            float var28 = 0.0F;
            float var30 = 0.0F;
            var19.addVertexWithUV(0.0D, (double) (0.0F + var26), 0.0D, (double) ((float) var22 / 128.0F + var28), (double) (((float) var24 + var26) / 128.0F + var30));
            var19.addVertexWithUV((double) (0.0F + var26), (double) (0.0F + var26), 0.0D, (double) (((float) var22 + var26) / 128.0F + var28), (double) (((float) var24 + var26) / 128.0F + var30));
            var19.addVertexWithUV((double) (0.0F + var26), 0.0D, 0.0D, (double) (((float) var22 + var26) / 128.0F + var28), (double) ((float) var24 / 128.0F + var30));
            var19.addVertexWithUV(0.0D, 0.0D, 0.0D, (double) ((float) var22 / 128.0F + var28), (double) ((float) var24 / 128.0F + var30));
            var19.draw();
            GL11.glTranslatef((float) this.charWidth[var20], 0.0F, 0.0F);
            GL11.glEndList();
        }

        for (int var21 = 0; var21 < 32; ++var21) {
            int var23 = (var21 >> 3 & 1) * 85;
            int var25 = (var21 >> 2 & 1) * 170 + var23;
            int var27 = (var21 >> 1 & 1) * 170 + var23;
            int var29 = (var21 >> 0 & 1) * 170 + var23;
            if (var21 == 6) {
                var25 += 85;
            }

            boolean var31 = var21 >= 16;
            if (var1.anaglyph) {
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

            GL11.glNewList(this.fontDisplayLists + 256 + var21, 4864 /*GL_COMPILE*/);
            GL11.glColor3f((float) var25 / 255.0F, (float) var27 / 255.0F, (float) var29 / 255.0F);
            GL11.glEndList();
        }

    }

    public void drawStringWithShadow(String var1, int var2, int var3, int var4) {
        this.renderString(var1, var2 + 1, var3 + 1, var4, true);
        this.drawString(var1, var2, var3, var4);
    }

    public void drawString(String var1, int var2, int var3, int var4) {
        this.renderString(var1, var2, var3, var4, false);
    }

    public void renderString(String var1, int var2, int var3, int var4, boolean var5) {
        if (var1 != null) {
            if (var5) {
                int var6 = var4 & -16777216;
                var4 = (var4 & 16579836) >> 2;
                var4 = var4 + var6;
            }

            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.fontTextureName);
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
            GL11.glTranslatef((float) var2, (float) var3, 0.0F);

            for (int var12 = 0; var12 < var1.length(); ++var12) {
                for (; var1.length() > var12 + 1 && var1.charAt(var12) == 167; var12 += 2) {
                    int var13 = "0123456789abcdef".indexOf(var1.toLowerCase().charAt(var12 + 1));
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

                if (var12 < var1.length()) {
                    int var14 = ChatAllowedCharacters.allowedCharacters.indexOf(var1.charAt(var12));
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
    }

    public int getStringWidth(String var1) {
        if (var1 == null) {
            return 0;
        } else {
            int var2 = 0;

            for (int var3 = 0; var3 < var1.length(); ++var3) {
                if (var1.charAt(var3) == 167) {
                    ++var3;
                } else {
                    int var4 = ChatAllowedCharacters.allowedCharacters.indexOf(var1.charAt(var3));
                    if (var4 >= 0) {
                        var2 += this.charWidth[var4 + 32];
                    }
                }
            }

            return var2;
        }
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
                    ;
                }

                int var10;
                for (; this.getStringWidth(var9) > var4; var9 = var9.substring(var10)) {
                    for (var10 = 0; this.getStringWidth(var9.substring(0, var10 + 1)) <= var4; ++var10) {
                        ;
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
        String[] var3 = var1.split("\n");
        if (var3.length > 1) {
            int var9 = 0;

            for (int var10 = 0; var10 < var3.length; ++var10) {
                var9 += this.func_27277_a(var3[var10], var2);
            }

            return var9;
        } else {
            String[] var4 = var1.split(" ");
            int var5 = 0;
            int var6 = 0;

            while (var5 < var4.length) {
                String var7;
                for (var7 = var4[var5++] + " "; var5 < var4.length && this.getStringWidth(var7 + var4[var5]) < var2; var7 = var7 + var4[var5++] + " ") {
                    ;
                }

                int var8;
                for (; this.getStringWidth(var7) > var2; var7 = var7.substring(var8)) {
                    for (var8 = 0; this.getStringWidth(var7.substring(0, var8 + 1)) <= var2; ++var8) {
                        ;
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
}
