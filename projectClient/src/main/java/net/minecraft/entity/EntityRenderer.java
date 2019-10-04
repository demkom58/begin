package net.minecraft.entity;

import net.hypnosis.input.mouse.Mouse;
import net.hypnosis.monitor.Window;
import net.hypnosis.render.GLU;
import net.hypnosis.render.Tessellator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.mouse.MouseFilter;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerControllerTest;
import net.minecraft.item.ItemRenderer;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3D;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProviderLoadOrGenerate;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.BiomeGenBase;
import org.lwjgl.opengl.ARBVertexBlend;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;

public class EntityRenderer {

    public static boolean field_28135_a = false;
    public static int anaglyphField;
    public ItemRenderer itemRenderer;
    volatile int field_1394_b = 0;
    volatile int field_1393_c = 0;
    FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    float fogColorRed;
    float fogColorGreen;
    float fogColorBlue;
    private Minecraft mc;
    private float farPlaneDistance = 0.0F;
    private int rendererUpdateCount;
    private Entity pointedEntity = null;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private MouseFilter mouseFilterDummy1 = new MouseFilter();
    private MouseFilter mouseFilterDummy2 = new MouseFilter();
    private MouseFilter mouseFilterDummy3 = new MouseFilter();
    private MouseFilter mouseFilterDummy4 = new MouseFilter();
    private float field_22228_r = 4.0F;
    private float field_22227_s = 4.0F;
    private float field_22226_t = 0.0F;
    private float field_22225_u = 0.0F;
    private float field_22224_v = 0.0F;
    private float field_22223_w = 0.0F;
    private float field_22222_x = 0.0F;
    private float field_22221_y = 0.0F;
    private float field_22220_z = 0.0F;
    private float field_22230_A = 0.0F;
    private boolean cloudFog = false;
    private double cameraZoom = 1.0D;
    private double cameraYaw = 0.0D;
    private double cameraPitch = 0.0D;
    private long prevFrameTime = System.currentTimeMillis();
    private long field_28133_I = 0L;
    private Random random = new Random();
    private int rainSoundCounter = 0;
    private float fogColor2;
    private float fogColor1;

    public EntityRenderer(Minecraft var1) {
        this.mc = var1;
        this.itemRenderer = new ItemRenderer(var1);
    }

    public void updateRenderer() {
        this.fogColor2 = this.fogColor1;
        this.field_22227_s = this.field_22228_r;
        this.field_22225_u = this.field_22226_t;
        this.field_22223_w = this.field_22224_v;
        this.field_22221_y = this.field_22222_x;
        this.field_22230_A = this.field_22220_z;
        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }

        float var1 = this.mc.theWorld.getLightBrightness(MathHelper.floor(this.mc.renderViewEntity.posX), MathHelper.floor(this.mc.renderViewEntity.posY), MathHelper.floor(this.mc.renderViewEntity.posZ));
        float var2 = (float) (3 - this.mc.gameSettings.renderDistance) / 3.0F;
        float var3 = var1 * (1.0F - var2) + var2;
        this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
    }

    public void getMouseOver(float var1) {
        if (this.mc.renderViewEntity != null) {
            if (this.mc.theWorld != null) {
                double var2 = this.mc.playerController.getBlockReachDistance();
                this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(var2, var1);
                double var4 = var2;
                Vec3D var6 = this.mc.renderViewEntity.getPosition(var1);
                if (this.mc.objectMouseOver != null) {
                    var4 = this.mc.objectMouseOver.hitVec.distanceTo(var6);
                }

                if (this.mc.playerController instanceof PlayerControllerTest) {
                    var2 = 32.0D;
                    var4 = 32.0D;
                } else {
                    if (var4 > 3.0D) {
                        var4 = 3.0D;
                    }

                    var2 = var4;
                }

                Vec3D var7 = this.mc.renderViewEntity.getLook(var1);
                Vec3D var8 = var6.addVector(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2);
                this.pointedEntity = null;
                float var9 = 1.0F;
                List var10 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2).expand(var9, var9, var9));
                double var11 = 0.0D;

                for (int var13 = 0; var13 < var10.size(); ++var13) {
                    Entity var14 = (Entity) var10.get(var13);
                    if (var14.canBeCollidedWith()) {
                        float var15 = var14.getCollisionBorderSize();
                        AxisAlignedBB var16 = var14.boundingBox.expand(var15, var15, var15);
                        MovingObjectPosition var17 = var16.func_706_a(var6, var8);
                        if (var16.isVecInXYZ(var6)) {
                            if (0.0D < var11 || var11 == 0.0D) {
                                this.pointedEntity = var14;
                                var11 = 0.0D;
                            }
                        } else if (var17 != null) {
                            double var18 = var6.distanceTo(var17.hitVec);
                            if (var18 < var11 || var11 == 0.0D) {
                                this.pointedEntity = var14;
                                var11 = var18;
                            }
                        }
                    }
                }

                if (this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerTest)) {
                    this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
                }

            }
        }
    }

    private float getFOVModifier(float var1) {
        EntityLiving var2 = this.mc.renderViewEntity;
        float var3 = 70.0F;
        if (var2.isInsideOfMaterial(Material.WATER)) {
            var3 = 60.0F;
        }

        if (var2.health <= 0) {
            float var4 = (float) var2.deathTime + var1;
            var3 /= (1.0F - 500.0F / (var4 + 500.0F)) * 2.0F + 1.0F;
        }

        return var3 + this.field_22221_y + (this.field_22222_x - this.field_22221_y) * var1;
    }

    private void hurtCameraEffect(float var1) {
        EntityLiving var2 = this.mc.renderViewEntity;
        float var3 = (float) var2.hurtTime - var1;
        if (var2.health <= 0) {
            float var4 = (float) var2.deathTime + var1;
            GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if (var3 >= 0.0F) {
            var3 = var3 / (float) var2.maxHurtTime;
            var3 = MathHelper.sin(var3 * var3 * var3 * var3 * 3.1415927F);
            float var7 = var2.attackedAtYaw;
            GL11.glRotatef(-var7, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
        }
    }

    private void setupViewBobbing(float var1) {
        if (this.mc.renderViewEntity instanceof EntityPlayer) {
            EntityPlayer var2 = (EntityPlayer) this.mc.renderViewEntity;
            float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
            float var4 = -(var2.distanceWalkedModified + var3 * var1);
            float var5 = var2.field_775_e + (var2.field_774_f - var2.field_775_e) * var1;
            float var6 = var2.cameraPitch + (var2.field_9328_R - var2.cameraPitch) * var1;
            GL11.glTranslatef(MathHelper.sin(var4 * 3.1415927F) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * 3.1415927F) * var5), 0.0F);
            GL11.glRotatef(MathHelper.sin(var4 * 3.1415927F) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(var4 * 3.1415927F - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
        }
    }

    private void orientCamera(float var1) {
        EntityLiving var2 = this.mc.renderViewEntity;
        float var3 = var2.yOffset - 1.62F;
        double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double) var1;
        double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double) var1 - (double) var3;
        double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double) var1;
        GL11.glRotatef(this.field_22230_A + (this.field_22220_z - this.field_22230_A) * var1, 0.0F, 0.0F, 1.0F);
        if (var2.isPlayerSleeping()) {
            var3 = (float) ((double) var3 + 1.0D);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            if (!this.mc.gameSettings.field_22273_E) {
                int var10 = this.mc.theWorld.getBlockId(MathHelper.floor(var2.posX), MathHelper.floor(var2.posY), MathHelper.floor(var2.posZ));
                if (var10 == Block.BED.blockID) {
                    int var11 = this.mc.theWorld.getBlockMetadata(MathHelper.floor(var2.posX), MathHelper.floor(var2.posY), MathHelper.floor(var2.posZ));
                    int var12 = var11 & 3;
                    GL11.glRotatef((float) (var12 * 90), 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.mc.gameSettings.thirdPersonView) {
            double var30 = this.field_22227_s + (this.field_22228_r - this.field_22227_s) * var1;
            if (this.mc.gameSettings.field_22273_E) {
                float var31 = this.field_22225_u + (this.field_22226_t - this.field_22225_u) * var1;
                float var13 = this.field_22223_w + (this.field_22224_v - this.field_22223_w) * var1;
                GL11.glTranslatef(0.0F, 0.0F, (float) (-var30));
                GL11.glRotatef(var13, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var31, 0.0F, 1.0F, 0.0F);
            } else {
                float var32 = var2.rotationYaw;
                float var33 = var2.rotationPitch;
                double var14 = (double) (-MathHelper.sin(var32 / 180.0F * 3.1415927F) * MathHelper.cos(var33 / 180.0F * 3.1415927F)) * var30;
                double var16 = (double) (MathHelper.cos(var32 / 180.0F * 3.1415927F) * MathHelper.cos(var33 / 180.0F * 3.1415927F)) * var30;
                double var18 = (double) (-MathHelper.sin(var33 / 180.0F * 3.1415927F)) * var30;

                for (int var20 = 0; var20 < 8; ++var20) {
                    float var21 = (float) ((var20 & 1) * 2 - 1);
                    float var22 = (float) ((var20 >> 1 & 1) * 2 - 1);
                    float var23 = (float) ((var20 >> 2 & 1) * 2 - 1);
                    var21 = var21 * 0.1F;
                    var22 = var22 * 0.1F;
                    var23 = var23 * 0.1F;
                    MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(var4 + (double) var21, var6 + (double) var22, var8 + (double) var23), Vec3D.createVector(var4 - var14 + (double) var21 + (double) var23, var6 - var18 + (double) var22, var8 - var16 + (double) var23));
                    if (var24 != null) {
                        double var25 = var24.hitVec.distanceTo(Vec3D.createVector(var4, var6, var8));
                        if (var25 < var30) {
                            var30 = var25;
                        }
                    }
                }

                GL11.glRotatef(var2.rotationPitch - var33, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var2.rotationYaw - var32, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float) (-var30));
                GL11.glRotatef(var32 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(var33 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!this.mc.gameSettings.field_22273_E) {
            GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, var3, 0.0F);
        var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double) var1;
        var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double) var1 - (double) var3;
        var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double) var1;
        this.cloudFog = this.mc.renderGlobal.func_27307_a(var4, var6, var8, var1);
    }

    private void setupCameraTransform(float var1, int var2) {
        this.farPlaneDistance = (float) (256 >> this.mc.gameSettings.renderDistance);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float var3 = 0.07F;
        if (this.mc.gameSettings.anaglyph)
            GL11.glTranslatef((float) (-(var2 * 2 - 1)) * var3, 0.0F, 0.0F);

        final Window window = this.mc.window;
        if (this.cameraZoom != 1.0D) {
            GL11.glTranslatef((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
            GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
            GLU.gluPerspective(this.getFOVModifier(var1), (float) window.getWidth() / (float) window.getHeight(), 0.05F, this.farPlaneDistance * 2.0F);
        } else {
            GLU.gluPerspective(this.getFOVModifier(var1), (float) window.getWidth() / (float) window.getHeight(), 0.05F, this.farPlaneDistance * 2.0F);
        }

        GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
        GL11.glLoadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float) (var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.hurtCameraEffect(var1);
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(var1);
        }

        float var4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * var1;
        if (var4 > 0.0F) {
            float var5 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
            var5 = var5 * var5;
            GL11.glRotatef(((float) this.rendererUpdateCount + var1) * 20.0F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / var5, 1.0F, 1.0F);
            GL11.glRotatef(-((float) this.rendererUpdateCount + var1) * 20.0F, 0.0F, 1.0F, 1.0F);
        }

        this.orientCamera(var1);
    }

    private void func_4135_b(float var1, int var2) {
        GL11.glLoadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float) (var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GL11.glPushMatrix();
        this.hurtCameraEffect(var1);
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(var1);
        }

        if (!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping() && !this.mc.gameSettings.hideGUI) {
            this.itemRenderer.renderItemInFirstPerson(var1);
        }

        GL11.glPopMatrix();
        if (!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping()) {
            this.itemRenderer.renderOverlays(var1);
            this.hurtCameraEffect(var1);
        }

        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(var1);
        }

    }

    public void updateCameraAndRender(float var1) {
        final Window window = this.mc.window;
        final int width = window.getWidth();
        final int height = window.getHeight();

        if (!window.isFocused()) {
            if (System.currentTimeMillis() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else this.prevFrameTime = System.currentTimeMillis();

        if (this.mc.inGameHasFocus) {
            this.mc.mouseHelper.mouseXYChange();
            float var2 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float var3 = var2 * var2 * var2 * 8.0F;

            float var4 = (float) this.mc.mouseHelper.deltaX * var3;
            float var5 = (float) this.mc.mouseHelper.deltaY * var3;
            byte var6 = 1;

            if (this.mc.gameSettings.invertMouse)
                var6 = -1;

            if (this.mc.gameSettings.smoothCamera) {
                var4 = this.mouseFilterXAxis.func_22386_a(var4, 0.05F * var3);
                var5 = this.mouseFilterYAxis.func_22386_a(var5, 0.05F * var3);
            }

            this.mc.thePlayer.func_346_d(var4, var5 * (float) var6);
        }

        if (!this.mc.skipRenderWorld) {
            field_28135_a = this.mc.gameSettings.anaglyph;
            ScaledResolution var13 = new ScaledResolution(this.mc.gameSettings, width, height);
            int var14 = var13.getScaledWidth();
            int var15 = var13.getScaledHeight();

            Mouse mouse = mc.mouse;
            int var16 = (int) (mouse.getX() * var14 / width);
            int var17 = (int) (var15 - mouse.getY() * var15 / height - 1);

            short var7 = 200;
            if (this.mc.gameSettings.limitFramerate == 1) {
                var7 = 120;
            }

            if (this.mc.gameSettings.limitFramerate == 2) {
                var7 = 40;
            }

            if (this.mc.theWorld != null) {
                if (this.mc.gameSettings.limitFramerate == 0) {
                    this.renderWorld(var1, 0L);
                } else {
                    this.renderWorld(var1, this.field_28133_I + (long) (1000000000 / var7));
                }

                if (this.mc.gameSettings.limitFramerate == 2) {
                    long var8 = (this.field_28133_I + (long) (1000000000 / var7) - System.nanoTime()) / 1000000L;
                    if (var8 > 0L && var8 < 500L) {
                        try {
                            Thread.sleep(var8);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                this.field_28133_I = System.nanoTime();
                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    this.mc.ingameGUI.renderGameOverlay(var1, this.mc.currentScreen != null, var16, var17);
                }
            } else {
                GL11.glViewport(0, 0, width, height);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
                GL11.glLoadIdentity();
                this.func_905_b();
                if (this.mc.gameSettings.limitFramerate == 2) {
                    long var18 = (this.field_28133_I + (long) (1000000000 / var7) - System.nanoTime()) / 1000000L;
                    if (var18 < 0L) {
                        var18 += 10L;
                    }

                    if (var18 > 0L && var18 < 500L) {
                        try {
                            Thread.sleep(var18);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                this.field_28133_I = System.nanoTime();
            }

            if (this.mc.currentScreen != null) {
                GL11.glClear(256);
                this.mc.currentScreen.drawScreen(var16, var17, var1);
                if (this.mc.currentScreen != null && this.mc.currentScreen.guiParticle != null) {
                    this.mc.currentScreen.guiParticle.func_25087_a(var1);
                }
            }

        }
    }

    public void renderWorld(float var1, long var2) {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }

        this.getMouseOver(var1);
        EntityLiving var4 = this.mc.renderViewEntity;
        RenderGlobal var5 = this.mc.renderGlobal;
        EffectRenderer var6 = this.mc.effectRenderer;
        double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double) var1;
        double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double) var1;
        double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double) var1;
        IChunkProvider var13 = this.mc.theWorld.getIChunkProvider();
        if (var13 instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate var14 = (ChunkProviderLoadOrGenerate) var13;
            int var15 = MathHelper.floor((float) ((int) var7)) >> 4;
            int var16 = MathHelper.floor((float) ((int) var11)) >> 4;
            var14.setCurrentChunkOver(var15, var16);
        }

        for (int var18 = 0; var18 < 2; ++var18) {
            if (this.mc.gameSettings.anaglyph) {
                anaglyphField = var18;
                if (anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            final Window window = this.mc.window;
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            this.updateFogColor(var1);
            GL11.glClear(16640);
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.setupCameraTransform(var1, var18);
            ClippingHelperImpl.getInstance();
            if (this.mc.gameSettings.renderDistance < 2) {
                this.setupFog(-1, var1);
                var5.renderSky(var1);
            }

            GL11.glEnable(GL11.GL_FOG);
            this.setupFog(1, var1);
            if (this.mc.gameSettings.ambientOcclusion) {
                GL11.glShadeModel(GL11.GL_SMOOTH);
            }

            Frustrum var19 = new Frustrum();
            var19.setPosition(var7, var9, var11);
            this.mc.renderGlobal.clipRenderersByFrustrum(var19, var1);
            if (var18 == 0) {
                while (!this.mc.renderGlobal.updateRenderers(var4, false) && var2 != 0L) {
                    long var20 = var2 - System.nanoTime();
                    if (var20 < 0L || var20 > 1000000000L) {
                        break;
                    }
                }
            }

            this.setupFog(0, var1);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            var5.sortAndRender(var4, 0, var1);
            GL11.glShadeModel(GL11.GL_FLAT);
            RenderHelper.enableStandardItemLighting();
            var5.renderEntities(var4.getPosition(var1), var19, var1);
            var6.func_1187_b(var4, var1);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, var1);
            var6.renderParticles(var4, var1);
            if (this.mc.objectMouseOver != null && var4.isInsideOfMaterial(Material.WATER) && var4 instanceof EntityPlayer) {
                EntityPlayer var21 = (EntityPlayer) var4;
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                var5.drawBlockBreaking(var21, this.mc.objectMouseOver, 0, var21.inventory.getCurrentItem(), var1);
                var5.drawSelectionBox(var21, this.mc.objectMouseOver, 0, var21.inventory.getCurrentItem(), var1);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            GL11.glBlendFunc(770, 771);
            this.setupFog(0, var1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
            if (this.mc.gameSettings.fancyGraphics) {
                if (this.mc.gameSettings.ambientOcclusion) {
                    GL11.glShadeModel(GL11.GL_SMOOTH);
                }

                GL11.glColorMask(false, false, false, false);
                int var22 = var5.sortAndRender(var4, 1, var1);
                if (this.mc.gameSettings.anaglyph) {
                    if (anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }

                if (var22 > 0) {
                    var5.renderAllRenderLists(1, var1);
                }

                GL11.glShadeModel(GL11.GL_FLAT);
            } else {
                var5.sortAndRender(var4, 1, var1);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            if (this.cameraZoom == 1.0D && var4 instanceof EntityPlayer && this.mc.objectMouseOver != null && !var4.isInsideOfMaterial(Material.WATER)) {
                EntityPlayer var23 = (EntityPlayer) var4;
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                var5.drawBlockBreaking(var23, this.mc.objectMouseOver, 0, var23.inventory.getCurrentItem(), var1);
                var5.drawSelectionBox(var23, this.mc.objectMouseOver, 0, var23.inventory.getCurrentItem(), var1);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            this.renderRainSnow(var1);
            GL11.glDisable(GL11.GL_FOG);
            if (this.pointedEntity != null) {
            }

            this.setupFog(0, var1);
            GL11.glEnable(GL11.GL_FOG);
            var5.renderClouds(var1);
            GL11.glDisable(GL11.GL_FOG);
            this.setupFog(1, var1);
            if (this.cameraZoom == 1.0D) {
                GL11.glClear(256);
                this.func_4135_b(var1, var18);
            }

            if (!this.mc.gameSettings.anaglyph) {
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
    }

    private void addRainParticles() {
        float var1 = this.mc.theWorld.getRainStrength(1.0F);
        if (!this.mc.gameSettings.fancyGraphics) {
            var1 /= 2.0F;
        }

        if (var1 != 0.0F) {
            this.random.setSeed((long) this.rendererUpdateCount * 312987231L);
            EntityLiving var2 = this.mc.renderViewEntity;
            World var3 = this.mc.theWorld;
            int var4 = MathHelper.floor(var2.posX);
            int var5 = MathHelper.floor(var2.posY);
            int var6 = MathHelper.floor(var2.posZ);
            byte var7 = 10;
            double var8 = 0.0D;
            double var10 = 0.0D;
            double var12 = 0.0D;
            int var14 = 0;

            for (int var15 = 0; var15 < (int) (100.0F * var1 * var1); ++var15) {
                int var16 = var4 + this.random.nextInt(var7) - this.random.nextInt(var7);
                int var17 = var6 + this.random.nextInt(var7) - this.random.nextInt(var7);
                int var18 = var3.findTopSolidBlock(var16, var17);
                int var19 = var3.getBlockId(var16, var18 - 1, var17);
                if (var18 <= var5 + var7 && var18 >= var5 - var7 && var3.getWorldChunkManager().getBiomeGenAt(var16, var17).canSpawnLightningBolt()) {
                    float var20 = this.random.nextFloat();
                    float var21 = this.random.nextFloat();
                    if (var19 > 0) {
                        if (Block.BLOCKS_LIST[var19].blockMaterial == Material.LAVA) {
                            this.mc.effectRenderer.addEffect(new EntitySmokeFX(var3, (float) var16 + var20, (double) ((float) var18 + 0.1F) - Block.BLOCKS_LIST[var19].minY, (float) var17 + var21, 0.0D, 0.0D, 0.0D));
                        } else {
                            ++var14;
                            if (this.random.nextInt(var14) == 0) {
                                var8 = (float) var16 + var20;
                                var10 = (double) ((float) var18 + 0.1F) - Block.BLOCKS_LIST[var19].minY;
                                var12 = (float) var17 + var21;
                            }

                            this.mc.effectRenderer.addEffect(new EntityRainFX(var3, (float) var16 + var20, (double) ((float) var18 + 0.1F) - Block.BLOCKS_LIST[var19].minY, (float) var17 + var21));
                        }
                    }
                }
            }

            if (var14 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (var10 > var2.posY + 1.0D && var3.findTopSolidBlock(MathHelper.floor(var2.posX), MathHelper.floor(var2.posZ)) > MathHelper.floor(var2.posY)) {
                    this.mc.theWorld.playSoundEffect(var8, var10, var12, "ambient.weather.rain", 0.1F, 0.5F);
                } else {
                    this.mc.theWorld.playSoundEffect(var8, var10, var12, "ambient.weather.rain", 0.2F, 1.0F);
                }
            }

        }
    }

    protected void renderRainSnow(float var1) {
        float rainStrength = this.mc.theWorld.getRainStrength(var1);
        if (rainStrength <= 0.0F)
            return;

        EntityLiving viewEntity = this.mc.renderViewEntity;
        World world = this.mc.theWorld;
        int viewX = MathHelper.floor(viewEntity.posX);
        int viewY = MathHelper.floor(viewEntity.posY);
        int viewZ = MathHelper.floor(viewEntity.posZ);
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/environment/snow.png"));
        double var9 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double) var1;
        double var11 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double) var1;
        double var13 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double) var1;
        int var15 = MathHelper.floor(var11);
        byte var16 = 5;
        if (this.mc.gameSettings.fancyGraphics) {
            var16 = 10;
        }

        BiomeGenBase[] var17 = world.getWorldChunkManager().func_4069_a(viewX - var16, viewZ - var16, var16 * 2 + 1, var16 * 2 + 1);
        int var18 = 0;

        for (int x = viewX - var16; x <= viewX + var16; ++x) {
            for (int z = viewZ - var16; z <= viewZ + var16; ++z) {
                BiomeGenBase var21 = var17[var18++];
                if (var21.getEnableSnow()) {
                    int topY = world.findTopSolidBlock(x, z);
                    if (topY < 0) {
                        topY = 0;
                    }

                    int var23 = topY;
                    if (topY < var15) {
                        var23 = var15;
                    }

                    int var24 = viewY - var16;
                    int var25 = viewY + var16;
                    if (var24 < topY) {
                        var24 = topY;
                    }

                    if (var25 < topY) {
                        var25 = topY;
                    }

                    float var26 = 1.0F;
                    if (var24 != var25) {
                        this.random.setSeed(x * x * GL11.GL_RGBA_MODE + x * 45238971 + z * z * 418711 + z * 13761);
                        float var27 = (float) this.rendererUpdateCount + var1;
                        float var28 = ((float) (this.rendererUpdateCount & 511) + var1) / 512.0F;
                        float var29 = this.random.nextFloat() + var27 * 0.01F * (float) this.random.nextGaussian();
                        float var30 = this.random.nextFloat() + var27 * (float) this.random.nextGaussian() * 0.001F;
                        double var31 = (double) ((float) x + 0.5F) - viewEntity.posX;
                        double var33 = (double) ((float) z + 0.5F) - viewEntity.posZ;
                        float var35 = MathHelper.sqrt(var31 * var31 + var33 * var33) / (float) var16;
                        tess.startDrawingQuads();
                        float var36 = world.getLightBrightness(x, var23, z);
                        GL11.glColor4f(var36, var36, var36, ((1.0F - var35 * var35) * 0.3F + 0.5F) * rainStrength);
                        tess.setTranslationD(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
                        tess.addVertexWithUV(x + 0, var24, (double) z + 0.5D, 0.0F * var26 + var29, (float) var24 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV(x + 1, var24, (double) z + 0.5D, 1.0F * var26 + var29, (float) var24 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV(x + 1, var25, (double) z + 0.5D, 1.0F * var26 + var29, (float) var25 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV(x + 0, var25, (double) z + 0.5D, 0.0F * var26 + var29, (float) var25 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV((double) x + 0.5D, var24, z + 0, 0.0F * var26 + var29, (float) var24 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV((double) x + 0.5D, var24, z + 1, 1.0F * var26 + var29, (float) var24 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV((double) x + 0.5D, var25, z + 1, 1.0F * var26 + var29, (float) var25 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV((double) x + 0.5D, var25, z + 0, 0.0F * var26 + var29, (float) var25 * var26 / 4.0F + var28 * var26 + var30);
                        tess.setTranslationD(0.0D, 0.0D, 0.0D);
                        tess.draw();
                    }
                }
            }
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/environment/rain.png"));
        if (this.mc.gameSettings.fancyGraphics) {
            var16 = 10;
        }

        var18 = 0;

        for (int var38 = viewX - var16; var38 <= viewX + var16; ++var38) {
            for (int var39 = viewZ - var16; var39 <= viewZ + var16; ++var39) {
                BiomeGenBase var40 = var17[var18++];
                if (var40.canSpawnLightningBolt()) {
                    int var41 = world.findTopSolidBlock(var38, var39);
                    int var42 = viewY - var16;
                    int var43 = viewY + var16;
                    if (var42 < var41) {
                        var42 = var41;
                    }

                    if (var43 < var41) {
                        var43 = var41;
                    }

                    float var44 = 1.0F;
                    if (var42 != var43) {
                        this.random.setSeed(var38 * var38 * GL11.GL_RGBA_MODE + var38 * 45238971 + var39 * var39 * 418711 + var39 * 13761);
                        float var45 = ((float) (this.rendererUpdateCount + var38 * var38 * GL11.GL_RGBA_MODE + var38 * 45238971 + var39 * var39 * 418711 + var39 * 13761 & 31) + var1) / 32.0F * (3.0F + this.random.nextFloat());
                        double var46 = (double) ((float) var38 + 0.5F) - viewEntity.posX;
                        double var47 = (double) ((float) var39 + 0.5F) - viewEntity.posZ;
                        float var48 = MathHelper.sqrt(var46 * var46 + var47 * var47) / (float) var16;
                        tess.startDrawingQuads();
                        float var32 = world.getLightBrightness(var38, 128, var39) * 0.85F + 0.15F;
                        GL11.glColor4f(var32, var32, var32, ((1.0F - var48 * var48) * 0.5F + 0.5F) * rainStrength);
                        tess.setTranslationD(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
                        tess.addVertexWithUV(var38 + 0, var42, (double) var39 + 0.5D, 0.0F * var44, (float) var42 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV(var38 + 1, var42, (double) var39 + 0.5D, 1.0F * var44, (float) var42 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV(var38 + 1, var43, (double) var39 + 0.5D, 1.0F * var44, (float) var43 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV(var38 + 0, var43, (double) var39 + 0.5D, 0.0F * var44, (float) var43 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV((double) var38 + 0.5D, var42, var39 + 0, 0.0F * var44, (float) var42 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV((double) var38 + 0.5D, var42, var39 + 1, 1.0F * var44, (float) var42 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV((double) var38 + 0.5D, var43, var39 + 1, 1.0F * var44, (float) var43 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV((double) var38 + 0.5D, var43, var39 + 0, 0.0F * var44, (float) var43 * var44 / 4.0F + var45 * var44);
                        tess.setTranslationD(0.0D, 0.0D, 0.0D);
                        tess.draw();
                    }
                }
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glAlphaFunc(516, 0.1F);

    }

    public void func_905_b() {
        final Window window = this.mc.window;
        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, window.getWidth(), window.getHeight());
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, var1.width, var1.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    private void updateFogColor(float var1) {
        World var2 = this.mc.theWorld;
        EntityLiving var3 = this.mc.renderViewEntity;
        float var4 = 1.0F / (float) (4 - this.mc.gameSettings.renderDistance);
        var4 = 1.0F - (float) Math.pow(var4, 0.25D);
        Vec3D var5 = var2.func_4079_a(this.mc.renderViewEntity, var1);
        float var6 = (float) var5.xCoord;
        float var7 = (float) var5.yCoord;
        float var8 = (float) var5.zCoord;
        Vec3D var9 = var2.getFogColor(var1);
        this.fogColorRed = (float) var9.xCoord;
        this.fogColorGreen = (float) var9.yCoord;
        this.fogColorBlue = (float) var9.zCoord;
        this.fogColorRed += (var6 - this.fogColorRed) * var4;
        this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
        this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
        float var10 = var2.getRainStrength(var1);
        if (var10 > 0.0F) {
            float var11 = 1.0F - var10 * 0.5F;
            float var12 = 1.0F - var10 * 0.4F;
            this.fogColorRed *= var11;
            this.fogColorGreen *= var11;
            this.fogColorBlue *= var12;
        }

        float var17 = var2.func_27166_f(var1);
        if (var17 > 0.0F) {
            float var18 = 1.0F - var17 * 0.5F;
            this.fogColorRed *= var18;
            this.fogColorGreen *= var18;
            this.fogColorBlue *= var18;
        }

        if (this.cloudFog) {
            Vec3D var19 = var2.func_628_d(var1);
            this.fogColorRed = (float) var19.xCoord;
            this.fogColorGreen = (float) var19.yCoord;
            this.fogColorBlue = (float) var19.zCoord;
        } else if (var3.isInsideOfMaterial(Material.WATER)) {
            this.fogColorRed = 0.02F;
            this.fogColorGreen = 0.02F;
            this.fogColorBlue = 0.2F;
        } else if (var3.isInsideOfMaterial(Material.LAVA)) {
            this.fogColorRed = 0.6F;
            this.fogColorGreen = 0.1F;
            this.fogColorBlue = 0.0F;
        }

        float var20 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * var1;
        this.fogColorRed *= var20;
        this.fogColorGreen *= var20;
        this.fogColorBlue *= var20;
        if (this.mc.gameSettings.anaglyph) {
            float var13 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            float var14 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float var15 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = var13;
            this.fogColorGreen = var14;
            this.fogColorBlue = var15;
        }

        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    private void setupFog(int var1, float var2) {
        EntityLiving var3 = this.mc.renderViewEntity;
        GL11.glFogfv(GL11.GL_FOG_COLOR, this.func_908_a(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.cloudFog) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            float var4 = 1.0F;
            float var5 = 1.0F;
            float var6 = 1.0F;
            if (this.mc.gameSettings.anaglyph) {
                float var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
                float var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
                float var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
            }
        } else if (var3.isInsideOfMaterial(Material.WATER)) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            float var10 = 0.4F;
            float var12 = 0.4F;
            float var14 = 0.9F;
            if (this.mc.gameSettings.anaglyph) {
                float var16 = (var10 * 30.0F + var12 * 59.0F + var14 * 11.0F) / 100.0F;
                float var18 = (var10 * 30.0F + var12 * 70.0F) / 100.0F;
                float var20 = (var10 * 30.0F + var14 * 70.0F) / 100.0F;
            }
        } else if (var3.isInsideOfMaterial(Material.LAVA)) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
            float var11 = 0.4F;
            float var13 = 0.3F;
            float var15 = 0.3F;
            if (this.mc.gameSettings.anaglyph) {
                float var17 = (var11 * 30.0F + var13 * 59.0F + var15 * 11.0F) / 100.0F;
                float var19 = (var11 * 30.0F + var13 * 70.0F) / 100.0F;
                float var21 = (var11 * 30.0F + var15 * 70.0F) / 100.0F;
            }
        } else {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance * 0.25F);
            GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
            if (var1 < 0) {
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
                GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance * 0.8F);
            }

            if (GL.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi(34138, 34139);
            }

            if (this.mc.theWorld.worldProvider.isNether) {
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            }
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
    }

    private FloatBuffer func_908_a(float var1, float var2, float var3, float var4) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(var1).put(var2).put(var3).put(var4);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
}
