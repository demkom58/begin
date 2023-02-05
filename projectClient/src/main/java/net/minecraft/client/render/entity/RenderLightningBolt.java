package net.minecraft.client.render.entity;

import net.hypnosis.render.Tessellator;
import net.minecraft.client.render.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLightningBolt;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderLightningBolt extends Render {
    public void handleWeather(EntityLightningBolt var1, double var2, double var4, double var6, float var8, float var9) {
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        double[] var11 = new double[8];
        double[] var12 = new double[8];
        double var13 = 0.0D;
        double var15 = 0.0D;
        Random var17 = new Random(var1.field1);

        for (int var18 = 7; var18 >= 0; --var18) {
            var11[var18] = var13;
            var12[var18] = var15;
            var13 += var17.nextInt(11) - 5;
            var15 += var17.nextInt(11) - 5;
        }

        for (int var45 = 0; var45 < 4; ++var45) {
            Random var46 = new Random(var1.field1);

            for (int var19 = 0; var19 < 3; ++var19) {
                int var20 = 7;
                int var21 = 0;
                if (var19 > 0) {
                    var20 = 7 - var19;
                }

                if (var19 > 0) {
                    var21 = var20 - 2;
                }

                double var22 = var11[var20] - var13;
                double var24 = var12[var20] - var15;

                for (int var26 = var20; var26 >= var21; --var26) {
                    double var27 = var22;
                    double var29 = var24;
                    if (var19 == 0) {
                        var22 += var46.nextInt(11) - 5;
                        var24 += var46.nextInt(11) - 5;
                    } else {
                        var22 += var46.nextInt(31) - 15;
                        var24 += var46.nextInt(31) - 15;
                    }

                    tess.startDrawing(GL11.GL_TRIANGLE_STRIP);
                    float var31 = 0.5F;
                    tess.setColorRGBA_F(0.9F * var31, 0.9F * var31, 1.0F * var31, 0.3F);
                    double var32 = 0.1D + (double) var45 * 0.2D;
                    if (var19 == 0) {
                        var32 *= (double) var26 * 0.1D + 1.0D;
                    }

                    double var34 = 0.1D + (double) var45 * 0.2D;
                    if (var19 == 0) {
                        var34 *= (double) (var26 - 1) * 0.1D + 1.0D;
                    }

                    for (int var36 = 0; var36 < 5; ++var36) {
                        double var37 = var2 + 0.5D - var32;
                        double var39 = var6 + 0.5D - var32;
                        if (var36 == 1 || var36 == 2) {
                            var37 += var32 * 2.0D;
                        }

                        if (var36 == 2 || var36 == 3) {
                            var39 += var32 * 2.0D;
                        }

                        double var41 = var2 + 0.5D - var34;
                        double var43 = var6 + 0.5D - var34;
                        if (var36 == 1 || var36 == 2) {
                            var41 += var34 * 2.0D;
                        }

                        if (var36 == 2 || var36 == 3) {
                            var43 += var34 * 2.0D;
                        }

                        tess.addVertex(var41 + var22, var4 + (double) (var26 * 16), var43 + var24);
                        tess.addVertex(var37 + var27, var4 + (double) ((var26 + 1) * 16), var39 + var29);
                    }

                    tess.draw();
                }
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        this.handleWeather((EntityLightningBolt) entity, x, y, z, yaw, delta);
    }
}
