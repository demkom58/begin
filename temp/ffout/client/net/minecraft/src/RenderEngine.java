package net.minecraft.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class RenderEngine {
   public static boolean field_1609_a = false;
   private HashMap field_6528_b = new HashMap();
   private HashMap field_28151_c = new HashMap();
   private HashMap field_1607_c = new HashMap();
   private IntBuffer field_1606_d = GLAllocation.func_1125_c(1);
   private ByteBuffer field_1605_e = GLAllocation.func_1127_b(1048576);
   private List field_1604_f = new ArrayList();
   private Map field_1603_g = new HashMap();
   private GameSettings field_1602_h;
   private boolean field_4281_i = false;
   private boolean field_4280_j = false;
   private TexturePackList field_6527_k;
   private BufferedImage field_25189_l = new BufferedImage(64, 64, 2);

   public RenderEngine(TexturePackList var1, GameSettings var2) {
      this.field_6527_k = var1;
      this.field_1602_h = var2;
      Graphics var3 = this.field_25189_l.getGraphics();
      var3.setColor(Color.WHITE);
      var3.fillRect(0, 0, 64, 64);
      var3.setColor(Color.BLACK);
      var3.drawString("missingtex", 1, 10);
      var3.dispose();
   }

   public int[] func_28149_a(String var1) {
      TexturePackBase var2 = this.field_6527_k.field_6534_a;
      int[] var3 = (int[])this.field_28151_c.get(var1);
      if (var3 != null) {
         return var3;
      } else {
         try {
            Object var6 = null;
            int[] var7;
            if (var1.startsWith("##")) {
               var7 = this.func_28148_b(this.func_1069_b(this.func_6526_a(var2.func_6481_a(var1.substring(2)))));
            } else if (var1.startsWith("%clamp%")) {
               this.field_4281_i = true;
               var7 = this.func_28148_b(this.func_6526_a(var2.func_6481_a(var1.substring(7))));
               this.field_4281_i = false;
            } else if (var1.startsWith("%blur%")) {
               this.field_4280_j = true;
               var7 = this.func_28148_b(this.func_6526_a(var2.func_6481_a(var1.substring(6))));
               this.field_4280_j = false;
            } else {
               InputStream var8 = var2.func_6481_a(var1);
               if (var8 == null) {
                  var7 = this.func_28148_b(this.field_25189_l);
               } else {
                  var7 = this.func_28148_b(this.func_6526_a(var8));
               }
            }

            this.field_28151_c.put(var1, var7);
            return var7;
         } catch (IOException var5) {
            var5.printStackTrace();
            int[] var4 = this.func_28148_b(this.field_25189_l);
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

   public int func_1070_a(String var1) {
      TexturePackBase var2 = this.field_6527_k.field_6534_a;
      Integer var3 = (Integer)this.field_6528_b.get(var1);
      if (var3 != null) {
         return var3.intValue();
      } else {
         try {
            this.field_1606_d.clear();
            GLAllocation.func_1128_a(this.field_1606_d);
            int var6 = this.field_1606_d.get(0);
            if (var1.startsWith("##")) {
               this.func_1068_a(this.func_1069_b(this.func_6526_a(var2.func_6481_a(var1.substring(2)))), var6);
            } else if (var1.startsWith("%clamp%")) {
               this.field_4281_i = true;
               this.func_1068_a(this.func_6526_a(var2.func_6481_a(var1.substring(7))), var6);
               this.field_4281_i = false;
            } else if (var1.startsWith("%blur%")) {
               this.field_4280_j = true;
               this.func_1068_a(this.func_6526_a(var2.func_6481_a(var1.substring(6))), var6);
               this.field_4280_j = false;
            } else {
               InputStream var7 = var2.func_6481_a(var1);
               if (var7 == null) {
                  this.func_1068_a(this.field_25189_l, var6);
               } else {
                  this.func_1068_a(this.func_6526_a(var7), var6);
               }
            }

            this.field_6528_b.put(var1, Integer.valueOf(var6));
            return var6;
         } catch (IOException var5) {
            var5.printStackTrace();
            GLAllocation.func_1128_a(this.field_1606_d);
            int var4 = this.field_1606_d.get(0);
            this.func_1068_a(this.field_25189_l, var4);
            this.field_6528_b.put(var1, Integer.valueOf(var4));
            return var4;
         }
      }
   }

   private BufferedImage func_1069_b(BufferedImage var1) {
      int var2 = var1.getWidth() / 16;
      BufferedImage var3 = new BufferedImage(16, var1.getHeight() * var2, 2);
      Graphics var4 = var3.getGraphics();

      for(int var5 = 0; var5 < var2; ++var5) {
         var4.drawImage(var1, -var5 * 16, var5 * var1.getHeight(), (ImageObserver)null);
      }

      var4.dispose();
      return var3;
   }

   public int func_1074_a(BufferedImage var1) {
      this.field_1606_d.clear();
      GLAllocation.func_1128_a(this.field_1606_d);
      int var2 = this.field_1606_d.get(0);
      this.func_1068_a(var1, var2);
      this.field_1607_c.put(Integer.valueOf(var2), var1);
      return var2;
   }

   public void func_1068_a(BufferedImage var1, int var2) {
      GL11.glBindTexture(3553, var2);
      if (field_1609_a) {
         GL11.glTexParameteri(3553, 10241, 9986);
         GL11.glTexParameteri(3553, 10240, 9728);
      } else {
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

      if (this.field_4280_j) {
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      }

      if (this.field_4281_i) {
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      int[] var5 = new int[var3 * var4];
      byte[] var6 = new byte[var3 * var4 * 4];
      var1.getRGB(0, 0, var3, var4, var5, 0, var3);

      for(int var7 = 0; var7 < var5.length; ++var7) {
         int var8 = var5[var7] >> 24 & 255;
         int var9 = var5[var7] >> 16 & 255;
         int var10 = var5[var7] >> 8 & 255;
         int var11 = var5[var7] & 255;
         if (this.field_1602_h != null && this.field_1602_h.field_1578_g) {
            int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
            int var13 = (var9 * 30 + var10 * 70) / 100;
            int var14 = (var9 * 30 + var11 * 70) / 100;
            var9 = var12;
            var10 = var13;
            var11 = var14;
         }

         var6[var7 * 4 + 0] = (byte)var9;
         var6[var7 * 4 + 1] = (byte)var10;
         var6[var7 * 4 + 2] = (byte)var11;
         var6[var7 * 4 + 3] = (byte)var8;
      }

      this.field_1605_e.clear();
      this.field_1605_e.put(var6);
      this.field_1605_e.position(0).limit(var6.length);
      GL11.glTexImage2D(3553, 0, 6408, var3, var4, 0, 6408, 5121, this.field_1605_e);
      if (field_1609_a) {
         for(int var18 = 1; var18 <= 4; ++var18) {
            int var19 = var3 >> var18 - 1;
            int var20 = var3 >> var18;
            int var21 = var4 >> var18;

            for(int var22 = 0; var22 < var20; ++var22) {
               for(int var23 = 0; var23 < var21; ++var23) {
                  int var24 = this.field_1605_e.getInt((var22 * 2 + 0 + (var23 * 2 + 0) * var19) * 4);
                  int var25 = this.field_1605_e.getInt((var22 * 2 + 1 + (var23 * 2 + 0) * var19) * 4);
                  int var15 = this.field_1605_e.getInt((var22 * 2 + 1 + (var23 * 2 + 1) * var19) * 4);
                  int var16 = this.field_1605_e.getInt((var22 * 2 + 0 + (var23 * 2 + 1) * var19) * 4);
                  int var17 = this.func_1077_b(this.func_1077_b(var24, var25), this.func_1077_b(var15, var16));
                  this.field_1605_e.putInt((var22 + var23 * var20) * 4, var17);
               }
            }

            GL11.glTexImage2D(3553, var18, 6408, var20, var21, 0, 6408, 5121, this.field_1605_e);
         }
      }

   }

   public void func_28150_a(int[] var1, int var2, int var3, int var4) {
      GL11.glBindTexture(3553, var4);
      if (field_1609_a) {
         GL11.glTexParameteri(3553, 10241, 9986);
         GL11.glTexParameteri(3553, 10240, 9728);
      } else {
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

      if (this.field_4280_j) {
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      }

      if (this.field_4281_i) {
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

      byte[] var5 = new byte[var2 * var3 * 4];

      for(int var6 = 0; var6 < var1.length; ++var6) {
         int var7 = var1[var6] >> 24 & 255;
         int var8 = var1[var6] >> 16 & 255;
         int var9 = var1[var6] >> 8 & 255;
         int var10 = var1[var6] & 255;
         if (this.field_1602_h != null && this.field_1602_h.field_1578_g) {
            int var11 = (var8 * 30 + var9 * 59 + var10 * 11) / 100;
            int var12 = (var8 * 30 + var9 * 70) / 100;
            int var13 = (var8 * 30 + var10 * 70) / 100;
            var8 = var11;
            var9 = var12;
            var10 = var13;
         }

         var5[var6 * 4 + 0] = (byte)var8;
         var5[var6 * 4 + 1] = (byte)var9;
         var5[var6 * 4 + 2] = (byte)var10;
         var5[var6 * 4 + 3] = (byte)var7;
      }

      this.field_1605_e.clear();
      this.field_1605_e.put(var5);
      this.field_1605_e.position(0).limit(var5.length);
      GL11.glTexSubImage2D(3553, 0, 0, 0, var2, var3, 6408, 5121, this.field_1605_e);
   }

   public void func_1078_a(int var1) {
      this.field_1607_c.remove(Integer.valueOf(var1));
      this.field_1606_d.clear();
      this.field_1606_d.put(var1);
      this.field_1606_d.flip();
      GL11.glDeleteTextures(this.field_1606_d);
   }

   public int func_1071_a(String var1, String var2) {
      ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.field_1603_g.get(var1);
      if (var3 != null && var3.field_1706_a != null && !var3.field_1707_d) {
         if (var3.field_1708_c < 0) {
            var3.field_1708_c = this.func_1074_a(var3.field_1706_a);
         } else {
            this.func_1068_a(var3.field_1706_a, var3.field_1708_c);
         }

         var3.field_1707_d = true;
      }

      if (var3 != null && var3.field_1708_c >= 0) {
         return var3.field_1708_c;
      } else {
         return var2 == null ? -1 : this.func_1070_a(var2);
      }
   }

   public ThreadDownloadImageData func_1075_a(String var1, ImageBuffer var2) {
      ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.field_1603_g.get(var1);
      if (var3 == null) {
         this.field_1603_g.put(var1, new ThreadDownloadImageData(var1, var2));
      } else {
         ++var3.field_1705_b;
      }

      return var3;
   }

   public void func_1073_b(String var1) {
      ThreadDownloadImageData var2 = (ThreadDownloadImageData)this.field_1603_g.get(var1);
      if (var2 != null) {
         --var2.field_1705_b;
         if (var2.field_1705_b == 0) {
            if (var2.field_1708_c >= 0) {
               this.func_1078_a(var2.field_1708_c);
            }

            this.field_1603_g.remove(var1);
         }
      }

   }

   public void func_1066_a(TextureFX var1) {
      this.field_1604_f.add(var1);
      var1.func_783_a();
   }

   public void func_1067_a() {
      for(int var1 = 0; var1 < this.field_1604_f.size(); ++var1) {
         TextureFX var2 = (TextureFX)this.field_1604_f.get(var1);
         var2.field_1131_c = this.field_1602_h.field_1578_g;
         var2.func_783_a();
         this.field_1605_e.clear();
         this.field_1605_e.put(var2.field_1127_a);
         this.field_1605_e.position(0).limit(var2.field_1127_a.length);
         var2.func_782_a(this);

         for(int var3 = 0; var3 < var2.field_1129_e; ++var3) {
            for(int var4 = 0; var4 < var2.field_1129_e; ++var4) {
               GL11.glTexSubImage2D(3553, 0, var2.field_1126_b % 16 * 16 + var3 * 16, var2.field_1126_b / 16 * 16 + var4 * 16, 16, 16, 6408, 5121, this.field_1605_e);
               if (field_1609_a) {
                  for(int var5 = 1; var5 <= 4; ++var5) {
                     int var6 = 16 >> var5 - 1;
                     int var7 = 16 >> var5;

                     for(int var8 = 0; var8 < var7; ++var8) {
                        for(int var9 = 0; var9 < var7; ++var9) {
                           int var10 = this.field_1605_e.getInt((var8 * 2 + 0 + (var9 * 2 + 0) * var6) * 4);
                           int var11 = this.field_1605_e.getInt((var8 * 2 + 1 + (var9 * 2 + 0) * var6) * 4);
                           int var12 = this.field_1605_e.getInt((var8 * 2 + 1 + (var9 * 2 + 1) * var6) * 4);
                           int var13 = this.field_1605_e.getInt((var8 * 2 + 0 + (var9 * 2 + 1) * var6) * 4);
                           int var14 = this.func_1072_a(this.func_1072_a(var10, var11), this.func_1072_a(var12, var13));
                           this.field_1605_e.putInt((var8 + var9 * var7) * 4, var14);
                        }
                     }

                     GL11.glTexSubImage2D(3553, var5, var2.field_1126_b % 16 * var7, var2.field_1126_b / 16 * var7, var7, var7, 6408, 5121, this.field_1605_e);
                  }
               }
            }
         }
      }

      for(int var15 = 0; var15 < this.field_1604_f.size(); ++var15) {
         TextureFX var16 = (TextureFX)this.field_1604_f.get(var15);
         if (var16.field_1130_d > 0) {
            this.field_1605_e.clear();
            this.field_1605_e.put(var16.field_1127_a);
            this.field_1605_e.position(0).limit(var16.field_1127_a.length);
            GL11.glBindTexture(3553, var16.field_1130_d);
            GL11.glTexSubImage2D(3553, 0, 0, 0, 16, 16, 6408, 5121, this.field_1605_e);
            if (field_1609_a) {
               for(int var17 = 1; var17 <= 4; ++var17) {
                  int var18 = 16 >> var17 - 1;
                  int var19 = 16 >> var17;

                  for(int var20 = 0; var20 < var19; ++var20) {
                     for(int var21 = 0; var21 < var19; ++var21) {
                        int var22 = this.field_1605_e.getInt((var20 * 2 + 0 + (var21 * 2 + 0) * var18) * 4);
                        int var23 = this.field_1605_e.getInt((var20 * 2 + 1 + (var21 * 2 + 0) * var18) * 4);
                        int var24 = this.field_1605_e.getInt((var20 * 2 + 1 + (var21 * 2 + 1) * var18) * 4);
                        int var25 = this.field_1605_e.getInt((var20 * 2 + 0 + (var21 * 2 + 1) * var18) * 4);
                        int var26 = this.func_1072_a(this.func_1072_a(var22, var23), this.func_1072_a(var24, var25));
                        this.field_1605_e.putInt((var20 + var21 * var19) * 4, var26);
                     }
                  }

                  GL11.glTexSubImage2D(3553, var17, 0, 0, var19, var19, 6408, 5121, this.field_1605_e);
               }
            }
         }
      }

   }

   private int func_1072_a(int var1, int var2) {
      int var3 = (var1 & -16777216) >> 24 & 255;
      int var4 = (var2 & -16777216) >> 24 & 255;
      return (var3 + var4 >> 1 << 24) + ((var1 & 16711422) + (var2 & 16711422) >> 1);
   }

   private int func_1077_b(int var1, int var2) {
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

   public void func_1065_b() {
      TexturePackBase var1 = this.field_6527_k.field_6534_a;
      Iterator var2 = this.field_1607_c.keySet().iterator();

      while(var2.hasNext()) {
         int var3 = ((Integer)var2.next()).intValue();
         BufferedImage var4 = (BufferedImage)this.field_1607_c.get(Integer.valueOf(var3));
         this.func_1068_a(var4, var3);
      }

      for(ThreadDownloadImageData var11 : this.field_1603_g.values()) {
         var11.field_1707_d = false;
      }

      for(String var12 : this.field_6528_b.keySet()) {
         try {
            BufferedImage var14;
            if (var12.startsWith("##")) {
               var14 = this.func_1069_b(this.func_6526_a(var1.func_6481_a(var12.substring(2))));
            } else if (var12.startsWith("%clamp%")) {
               this.field_4281_i = true;
               var14 = this.func_6526_a(var1.func_6481_a(var12.substring(7)));
            } else if (var12.startsWith("%blur%")) {
               this.field_4280_j = true;
               var14 = this.func_6526_a(var1.func_6481_a(var12.substring(6)));
            } else {
               var14 = this.func_6526_a(var1.func_6481_a(var12));
            }

            int var5 = ((Integer)this.field_6528_b.get(var12)).intValue();
            this.func_1068_a(var14, var5);
            this.field_4280_j = false;
            this.field_4281_i = false;
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      for(String var13 : this.field_28151_c.keySet()) {
         try {
            BufferedImage var15;
            if (var13.startsWith("##")) {
               var15 = this.func_1069_b(this.func_6526_a(var1.func_6481_a(var13.substring(2))));
            } else if (var13.startsWith("%clamp%")) {
               this.field_4281_i = true;
               var15 = this.func_6526_a(var1.func_6481_a(var13.substring(7)));
            } else if (var13.startsWith("%blur%")) {
               this.field_4280_j = true;
               var15 = this.func_6526_a(var1.func_6481_a(var13.substring(6)));
            } else {
               var15 = this.func_6526_a(var1.func_6481_a(var13));
            }

            this.func_28147_a(var15, (int[])this.field_28151_c.get(var13));
            this.field_4280_j = false;
            this.field_4281_i = false;
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

   }

   private BufferedImage func_6526_a(InputStream var1) throws IOException {
      BufferedImage var2 = ImageIO.read(var1);
      var1.close();
      return var2;
   }

   public void func_1076_b(int var1) {
      if (var1 >= 0) {
         GL11.glBindTexture(3553, var1);
      }
   }
}
