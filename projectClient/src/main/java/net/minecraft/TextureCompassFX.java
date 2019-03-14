package net.minecraft;

import net.minecraft.client.Minecraft;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextureCompassFX extends TextureFX {
    private Minecraft mc;
    private int[] compassIconImageData = new int[256];
    private double field_4229_i;
    private double field_4228_j;

    public TextureCompassFX(Minecraft var1) {
        super(Item.COMPASS.getIconFromDamage(0));
        this.mc = var1;
        this.tileImage = 1;

        try {
            BufferedImage var2 = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
            int var3 = this.iconIndex % 16 * 16;
            int var4 = this.iconIndex / 16 * 16;
            var2.getRGB(var3, var4, 16, 16, this.compassIconImageData, 0, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onTick() {
        for (int var1 = 0; var1 < 256; ++var1) {
            int var2 = this.compassIconImageData[var1] >> 24 & 255;
            int var3 = this.compassIconImageData[var1] >> 16 & 255;
            int var4 = this.compassIconImageData[var1] >> 8 & 255;
            int var5 = this.compassIconImageData[var1] >> 0 & 255;
            if (this.anaglyphEnabled) {
                int var6 = (var3 * 30 + var4 * 59 + var5 * 11) / 100;
                int var7 = (var3 * 30 + var4 * 70) / 100;
                int var8 = (var3 * 30 + var5 * 70) / 100;
                var3 = var6;
                var4 = var7;
                var5 = var8;
            }

            this.imageData[var1 * 4 + 0] = (byte) var3;
            this.imageData[var1 * 4 + 1] = (byte) var4;
            this.imageData[var1 * 4 + 2] = (byte) var5;
            this.imageData[var1 * 4 + 3] = (byte) var2;
        }

        double var20 = 0.0D;
        if (this.mc.theWorld != null && this.mc.thePlayer != null) {
            ChunkCoordinates var21 = this.mc.theWorld.getSpawnPoint();
            double var23 = (double) var21.x - this.mc.thePlayer.posX;
            double var25 = (double) var21.z - this.mc.thePlayer.posZ;
            var20 = (double) (this.mc.thePlayer.rotationYaw - 90.0F) * 3.141592653589793D / 180.0D - Math.atan2(var25, var23);
            if (this.mc.theWorld.worldProvider.isNether) {
                var20 = Math.random() * 3.1415927410125732D * 2.0D;
            }
        }

        double var22;
        for (var22 = var20 - this.field_4229_i; var22 < -3.141592653589793D; var22 += 6.283185307179586D) {
            ;
        }

        while (var22 >= 3.141592653589793D) {
            var22 -= 6.283185307179586D;
        }

        if (var22 < -1.0D) {
            var22 = -1.0D;
        }

        if (var22 > 1.0D) {
            var22 = 1.0D;
        }

        this.field_4228_j += var22 * 0.1D;
        this.field_4228_j *= 0.8D;
        this.field_4229_i += this.field_4228_j;
        double var24 = Math.sin(this.field_4229_i);
        double var26 = Math.cos(this.field_4229_i);

        for (int var9 = -4; var9 <= 4; ++var9) {
            int var10 = (int) (8.5D + var26 * (double) var9 * 0.3D);
            int var11 = (int) (7.5D - var24 * (double) var9 * 0.3D * 0.5D);
            int var12 = var11 * 16 + var10;
            int var13 = 100;
            int var14 = 100;
            int var15 = 100;
            short var16 = 255;
            if (this.anaglyphEnabled) {
                int var17 = (var13 * 30 + var14 * 59 + var15 * 11) / 100;
                int var18 = (var13 * 30 + var14 * 70) / 100;
                int var19 = (var13 * 30 + var15 * 70) / 100;
                var13 = var17;
                var14 = var18;
                var15 = var19;
            }

            this.imageData[var12 * 4 + 0] = (byte) var13;
            this.imageData[var12 * 4 + 1] = (byte) var14;
            this.imageData[var12 * 4 + 2] = (byte) var15;
            this.imageData[var12 * 4 + 3] = (byte) var16;
        }

        for (int var27 = -8; var27 <= 16; ++var27) {
            int var28 = (int) (8.5D + var24 * (double) var27 * 0.3D);
            int var29 = (int) (7.5D + var26 * (double) var27 * 0.3D * 0.5D);
            int var30 = var29 * 16 + var28;
            int var31 = var27 >= 0 ? 255 : 100;
            int var32 = var27 >= 0 ? 20 : 100;
            int var33 = var27 >= 0 ? 20 : 100;
            short var34 = 255;
            if (this.anaglyphEnabled) {
                int var35 = (var31 * 30 + var32 * 59 + var33 * 11) / 100;
                int var36 = (var31 * 30 + var32 * 70) / 100;
                int var37 = (var31 * 30 + var33 * 70) / 100;
                var31 = var35;
                var32 = var36;
                var33 = var37;
            }

            this.imageData[var30 * 4 + 0] = (byte) var31;
            this.imageData[var30 * 4 + 1] = (byte) var32;
            this.imageData[var30 * 4 + 2] = (byte) var33;
            this.imageData[var30 * 4 + 3] = (byte) var34;
        }

    }
}
