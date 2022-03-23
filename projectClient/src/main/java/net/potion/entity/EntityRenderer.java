package net.potion.entity;

import net.hypnosis.input.mouse.Mouse;
import net.hypnosis.monitor.Window;
import net.hypnosis.render.GLU;
import net.hypnosis.render.Tessellator;
import net.hypnosis.util.math.MathConstants;
import net.potion.block.Block;
import net.potion.client.PotionClient;
import net.potion.client.input.mouse.MouseFilter;
import net.potion.client.render.*;
import net.potion.entity.player.EntityPlayer;
import net.potion.entity.player.EntityPlayerSP;
import net.potion.entity.player.PlayerControllerTest;
import net.potion.item.ItemRenderer;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.potion.util.MovingObjectPosition;
import net.potion.world.World;
import net.potion.world.chunk.ChunkProviderLoadOrGenerate;
import net.potion.world.chunk.IChunkProvider;
import net.potion.world.gen.biome.BiomeGenBase;
import net.hypnosis.util.math.Vec3d;
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
    private PotionClient potion;
    private float farPlaneDistance = 0.0F;
    private int rendererUpdateCount;
    private Entity pointedEntity = null;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private MouseFilter mouseFilterDummy1 = new MouseFilter();
    private MouseFilter mouseFilterDummy2 = new MouseFilter();
    private MouseFilter mouseFilterDummy3 = new MouseFilter();
    private MouseFilter mouseFilterDummy4 = new MouseFilter();
    private float thirdPersonDistance = 4.0F;
    private float thirdPersonDistanceTemp = 4.0F;
    private float debugCamYaw = 0.0F;
    private float prevDebugCamYaw = 0.0F;
    private float debugCamPitch = 0.0F;
    private float prevDebugCamPitch = 0.0F;
    private float debugCamFOV = 0.0F;
    private float prevDebugCamFOV = 0.0F;
    private float camRoll = 0.0F;
    private float prevCamRoll = 0.0F;
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

    public EntityRenderer(PotionClient var1) {
        this.potion = var1;
        this.itemRenderer = new ItemRenderer(var1);
    }

    public void updateRenderer() {
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistanceTemp = this.thirdPersonDistance;
        this.prevDebugCamYaw = this.debugCamYaw;
        this.prevDebugCamPitch = this.debugCamPitch;
        this.prevDebugCamFOV = this.debugCamFOV;
        this.prevCamRoll = this.camRoll;
        if (this.potion.renderViewEntity == null) {
            this.potion.renderViewEntity = this.potion.thePlayer;
        }

        float var1 = this.potion.theWorld.getLightBrightness(MathHelper.floor(this.potion.renderViewEntity.posX), MathHelper.floor(this.potion.renderViewEntity.posY), MathHelper.floor(this.potion.renderViewEntity.posZ));
        float var2 = (float) (3 - this.potion.gameSettings.renderDistance) / 3.0F;
        float var3 = var1 * (1.0F - var2) + var2;
        this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
    }

    public void getMouseOver(float partialTicks) {
        if (this.potion.renderViewEntity != null) {
            if (this.potion.theWorld != null) {
                double var2 = this.potion.playerController.getBlockReachDistance();
                this.potion.objectMouseOver = this.potion.renderViewEntity.rayTrace(var2, partialTicks);
                double var4 = var2;
                Vec3d var6 = this.potion.renderViewEntity.getPosition(partialTicks);
                if (this.potion.objectMouseOver != null) {
                    var4 = this.potion.objectMouseOver.hitVec.distanceTo(var6);
                }

                if (this.potion.playerController instanceof PlayerControllerTest) {
                    var2 = 32.0D;
                    var4 = 32.0D;
                } else {
                    if (var4 > 3.0D) {
                        var4 = 3.0D;
                    }

                    var2 = var4;
                }

                Vec3d var7 = this.potion.renderViewEntity.getLook(partialTicks);
                Vec3d var8 = new Vec3d(var6).add(var7.x * var2, var7.y * var2, var7.z * var2);
                this.pointedEntity = null;
                float var9 = 1.0F;
                List var10 = this.potion.theWorld.getEntitiesWithinAABBExcludingEntity(this.potion.renderViewEntity, this.potion.renderViewEntity.boundingBox.addCoord(var7.x * var2, var7.y * var2, var7.z * var2).expand(var9, var9, var9));
                double var11 = 0.0D;

                for (int var13 = 0; var13 < var10.size(); ++var13) {
                    Entity var14 = (Entity) var10.get(var13);
                    if (var14.canBeCollidedWith()) {
                        float var15 = var14.getCollisionBorderSize();
                        AxisAlignedBB var16 = var14.boundingBox.expand(var15, var15, var15);
                        MovingObjectPosition var17 = var16.raycast(var6, var8);
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

                if (this.pointedEntity != null && !(this.potion.playerController instanceof PlayerControllerTest)) {
                    this.potion.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
                }

            }
        }
    }

    private float getFOVModifier(float partialTicks) {
        EntityLiving entity = this.potion.renderViewEntity;
        float fov = 70.0F;

        if (entity.health <= 0) {
            float deathFov = (float) entity.deathTime + partialTicks;
            fov /= (1.0F - 500.0F / (deathFov + 500.0F)) * 2.0F + 1.0F;
        }

        if (entity.isInsideOfMaterial(Material.WATER)) {
            fov *= 60.0F / 70.0F;
        }

        return fov + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * partialTicks;
    }

    private void hurtCameraEffect(float var1) {
        EntityLiving viewEntity = this.potion.renderViewEntity;
        float var3 = (float) viewEntity.hurtTime - var1;
        if (viewEntity.health <= 0) {
            float var4 = (float) viewEntity.deathTime + var1;
            GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if (var3 >= 0.0F) {
            var3 = var3 / (float) viewEntity.maxHurtTime;
            var3 = MathHelper.sin(var3 * var3 * var3 * var3 * MathConstants.PI);
            float var7 = viewEntity.attackedAtYaw;
            GL11.glRotatef(-var7, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
        }
    }

    private void setupViewBobbing(float partialTicks) {
        if (this.potion.renderViewEntity instanceof EntityPlayer player) {
            float walked = player.distanceWalkedModified - player.prevDistanceWalkedModified;
            float walkedInterp = -(player.distanceWalkedModified + walked * partialTicks);
            float yawInterp = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
            float pitchInterp = player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * partialTicks;

            float walkedRad = walkedInterp * MathConstants.PI;
            GL11.glTranslatef(MathHelper.sin(walkedRad) * yawInterp * 0.5F, -Math.abs(MathHelper.cos(walkedRad) * yawInterp), 0.0F);
            GL11.glRotatef(MathHelper.sin(walkedRad) * yawInterp * 3.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(walkedRad - 0.2F) * yawInterp) * 5.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(pitchInterp, 1.0F, 0.0F, 0.0F);
        }
    }

    private void orientCamera(float partialTicks) {
        EntityLiving viewEntity = this.potion.renderViewEntity;

        float yOffset = viewEntity.yOffset - 1.62F;
        double x = viewEntity.prevPosX + (viewEntity.posX - viewEntity.prevPosX) * (double) partialTicks;
        double y = viewEntity.prevPosY + (viewEntity.posY - viewEntity.prevPosY) * (double) partialTicks - (double) yOffset;
        double z = viewEntity.prevPosZ + (viewEntity.posZ - viewEntity.prevPosZ) * (double) partialTicks;
        GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * partialTicks, 0.0F, 0.0F, 1.0F);

        if (viewEntity.isSleeping()) {
            yOffset += 1.0F;
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            if (!this.potion.gameSettings.debugCamEnable) {
                int blockId = this.potion.theWorld.getBlockId(
                        MathHelper.floor(viewEntity.posX),
                        MathHelper.floor(viewEntity.posY),
                        MathHelper.floor(viewEntity.posZ)
                );

                if (blockId == Block.BED.blockID) {
                    int metadata = this.potion.theWorld.getBlockMetadata(
                            MathHelper.floor(viewEntity.posX),
                            MathHelper.floor(viewEntity.posY),
                            MathHelper.floor(viewEntity.posZ)
                    );
                    int var12 = metadata & 3;
                    GL11.glRotatef(var12 * 90, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(viewEntity.prevRotationYaw + (viewEntity.rotationYaw - viewEntity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(viewEntity.prevRotationPitch + (viewEntity.rotationPitch - viewEntity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.potion.gameSettings.thirdPersonView) {
            double distance = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;
            if (this.potion.gameSettings.debugCamEnable) {
                float yaw = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * partialTicks;
                float pitch = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * partialTicks;
                GL11.glTranslatef(0.0F, 0.0F, (float) (-distance));
                GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
            } else {
                float yaw = viewEntity.rotationYaw;
                float pitch = viewEntity.rotationPitch;
                double rX = -MathHelper.sin(yaw / 180.0F * MathConstants.PI) * MathHelper.cos(pitch / 180.0F * MathConstants.PI) * distance;
                double rZ = MathHelper.cos(yaw / 180.0F * MathConstants.PI) * MathHelper.cos(pitch / 180.0F * MathConstants.PI) * distance;
                double rY =  -MathHelper.sin(pitch / 180.0F * MathConstants.PI) * distance;

                for (int pass = 0; pass < 8; ++pass) {
                    float pX = (pass & 1) * 2 - 1;
                    float pY = (pass >> 1 & 1) * 2 - 1;
                    float pZ = (pass >> 2 & 1) * 2 - 1;
                    pX *= 0.1F;
                    pY *= 0.1F;
                    pZ *= 0.1F;
                    MovingObjectPosition position = this.potion.theWorld.rayTraceBlocks(
                            new Vec3d(x + pX, y + pY, z + pZ),
                            new Vec3d(x - rX + pX + pZ, y - rY + pY, z - rZ + pZ));
                    if (position != null) {
                        double cDistance = position.hitVec.distanceTo(new Vec3d(x, y, z));
                        if (cDistance < distance) {
                            distance = cDistance;
                        }
                    }
                }

                GL11.glRotatef(viewEntity.rotationPitch - pitch, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(viewEntity.rotationYaw - yaw, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float) (-distance));
                GL11.glRotatef(yaw - viewEntity.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(pitch - viewEntity.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!this.potion.gameSettings.debugCamEnable) {
            GL11.glRotatef(viewEntity.prevRotationPitch + (viewEntity.rotationPitch - viewEntity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(viewEntity.prevRotationYaw + (viewEntity.rotationYaw - viewEntity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, yOffset, 0.0F);
        x = viewEntity.prevPosX + (viewEntity.posX - viewEntity.prevPosX) * (double) partialTicks;
        y = viewEntity.prevPosY + (viewEntity.posY - viewEntity.prevPosY) * (double) partialTicks - (double) yOffset;
        z = viewEntity.prevPosZ + (viewEntity.posZ - viewEntity.prevPosZ) * (double) partialTicks;
        this.cloudFog = this.potion.renderGlobal.func_27307_a(x, y, z, partialTicks);
    }

    private void setupCameraTransform(float partialTicks, int var2) {
        this.farPlaneDistance = (float) (256 >> this.potion.gameSettings.renderDistance);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float var3 = 0.07F;

        if (this.potion.gameSettings.anaglyph) {
            GL11.glTranslatef(-(var2 * 2F - 1F) * var3, 0.0F, 0.0F);
        }

        final Window window = this.potion.window;
        if (this.cameraZoom != 1.0D) {
            GL11.glTranslatef((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
            GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
        }

        float aspect = (float) window.getWidth() / (float) window.getHeight();
        float fovModifier = this.getFOVModifier(partialTicks);

        GLU.gluPerspective(fovModifier, aspect, 0.05F, this.farPlaneDistance * 2.0F);

        GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
        GL11.glLoadIdentity();
        if (this.potion.gameSettings.anaglyph) {
            GL11.glTranslatef((var2 * 2F - 1F) * 0.1F, 0.0F, 0.0F);
        }

        this.hurtCameraEffect(partialTicks);
        if (this.potion.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }

        final EntityPlayerSP player = this.potion.thePlayer;
        final float timePortalInterp = player.prevTimeInPortal + (player.timeInPortal - player.prevTimeInPortal) * partialTicks;

        if (timePortalInterp > 0.0F) {
            float var5 = 5.0F / (timePortalInterp * timePortalInterp + 5.0F) - timePortalInterp * 0.04F;
            var5 *= var5;
            GL11.glRotatef(((float) this.rendererUpdateCount + partialTicks) * 20.0F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / var5, 1.0F, 1.0F);
            GL11.glRotatef(-((float) this.rendererUpdateCount + partialTicks) * 20.0F, 0.0F, 1.0F, 1.0F);
        }

        this.orientCamera(partialTicks);
    }

    private void renderHand(float partialTicks, int var2) {
        GL11.glLoadIdentity();
        if (this.potion.gameSettings.anaglyph) {
            GL11.glTranslatef((float) (var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GL11.glPushMatrix();
        this.hurtCameraEffect(partialTicks);
        if (this.potion.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }

        if (!this.potion.gameSettings.thirdPersonView && !this.potion.renderViewEntity.isSleeping() && !this.potion.gameSettings.hideGUI) {
            this.itemRenderer.renderItemInFirstPerson(partialTicks);
        }

        GL11.glPopMatrix();
        if (!this.potion.gameSettings.thirdPersonView && !this.potion.renderViewEntity.isSleeping()) {
            this.itemRenderer.renderOverlays(partialTicks);
            this.hurtCameraEffect(partialTicks);
        }

        if (this.potion.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks);
        }

    }

    public void updateCameraAndRender(float partialTicks) {
        final Window window = this.potion.window;
        final int width = window.getWidth();
        final int height = window.getHeight();

        if (!window.isFocused()) {
            if (System.currentTimeMillis() - this.prevFrameTime > 500L) {
                this.potion.displayInGameMenu();
            }
        } else this.prevFrameTime = System.currentTimeMillis();

        if (this.potion.inGameHasFocus) {
            this.potion.mouseHelper.mouseXYChange();
            float var2 = this.potion.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float var3 = var2 * var2 * var2 * 8.0F;

            float var4 = (float) this.potion.mouseHelper.deltaX * var3;
            float var5 = (float) this.potion.mouseHelper.deltaY * var3;
            byte var6 = 1;

            if (this.potion.gameSettings.invertMouse)
                var6 = -1;

            if (this.potion.gameSettings.smoothCamera) {
                var4 = this.mouseFilterXAxis.func_22386_a(var4, 0.05F * var3);
                var5 = this.mouseFilterYAxis.func_22386_a(var5, 0.05F * var3);
            }

            this.potion.thePlayer.updateLook(var4, var5 * (float) var6);
        }

        if (!this.potion.skipRenderWorld) {
            field_28135_a = this.potion.gameSettings.anaglyph;
            ScaledResolution var13 = new ScaledResolution(this.potion.gameSettings, width, height);
            int var14 = var13.getScaledWidth();
            int var15 = var13.getScaledHeight();

            Mouse mouse = potion.mouse;
            int var16 = (int) (mouse.getX() * var14 / width);
            int var17 = (int) (var15 - mouse.getY() * var15 / height - 1);

            short var7 = 200;
            if (this.potion.gameSettings.limitFramerate == 1) {
                var7 = 120;
            }

            if (this.potion.gameSettings.limitFramerate == 2) {
                var7 = 40;
            }

            if (this.potion.theWorld != null) {
                if (this.potion.gameSettings.limitFramerate == 0) {
                    this.renderWorld(partialTicks, 0L);
                } else {
                    this.renderWorld(partialTicks, this.field_28133_I + (long) (1000000000 / var7));
                }

                if (this.potion.gameSettings.limitFramerate == 2) {
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
                if (!this.potion.gameSettings.hideGUI || this.potion.currentScreen != null) {
                    this.potion.ingameGUI.renderGameOverlay(partialTicks, this.potion.currentScreen != null, var16, var17);
                }
            } else {
                GL11.glViewport(0, 0, width, height);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
                GL11.glLoadIdentity();
                this.func_905_b();
                if (this.potion.gameSettings.limitFramerate == 2) {
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

            if (this.potion.currentScreen != null) {
                GL11.glClear(256);
                this.potion.currentScreen.drawScreen(var16, var17, partialTicks);
                if (this.potion.currentScreen != null && this.potion.currentScreen.guiParticle != null) {
                    this.potion.currentScreen.guiParticle.func_25087_a(partialTicks);
                }
            }

        }
    }

    public void renderWorld(float partialTicks, long var2) {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        if (this.potion.renderViewEntity == null) {
            this.potion.renderViewEntity = this.potion.thePlayer;
        }

        this.getMouseOver(partialTicks);
        EntityLiving viewEntity = this.potion.renderViewEntity;
        RenderGlobal renderGlobal = this.potion.renderGlobal;
        EffectRenderer effectRenderer = this.potion.effectRenderer;

        double x = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        double y = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        double z = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;

        IChunkProvider chunkProvider = this.potion.theWorld.getIChunkProvider();
        if (chunkProvider instanceof ChunkProviderLoadOrGenerate chunkProviderLOG) {
            int chunkX = MathHelper.floor(x) >> 4;
            int chunkZ = MathHelper.floor(z) >> 4;
            chunkProviderLOG.setCurrentChunkOver(chunkX, chunkZ);
        }

        for (int pass = 0; pass < 2; ++pass) {
            if (this.potion.gameSettings.anaglyph) {
                anaglyphField = pass;
                if (anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            final Window window = this.potion.window;
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            this.updateFogColor(partialTicks);
            GL11.glClear(16640);
            GL11.glEnable(GL11.GL_CULL_FACE);
            this.setupCameraTransform(partialTicks, pass);
            ClippingHelperImpl.getInstance();
            if (this.potion.gameSettings.renderDistance < 2) {
                this.setupFog(-1, partialTicks);
                renderGlobal.renderSky(partialTicks);
            }

            GL11.glEnable(GL11.GL_FOG);
            this.setupFog(1, partialTicks);
            if (this.potion.gameSettings.ambientOcclusion) {
                GL11.glShadeModel(GL11.GL_SMOOTH);
            }

            Frustrum frustrum = new Frustrum();
            frustrum.setPosition(x, y, z);
            this.potion.renderGlobal.clipRenderersByFrustrum(frustrum, partialTicks);
            if (pass == 0) {
                while (!this.potion.renderGlobal.updateRenderers(viewEntity, false) && var2 != 0L) {
                    long now = var2 - System.nanoTime();
                    if (now < 0L || now > 1000000000L) {
                        break;
                    }
                }
            }

            this.setupFog(0, partialTicks);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            renderGlobal.sortAndRender(viewEntity, 0, partialTicks);
            GL11.glShadeModel(GL11.GL_FLAT);
            RenderHelper.enableStandardItemLighting();
            renderGlobal.renderEntities(viewEntity.getPosition(partialTicks), frustrum, partialTicks);
            effectRenderer.func_1187_b(viewEntity, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            effectRenderer.renderParticles(viewEntity, partialTicks);
            if (this.potion.objectMouseOver != null && viewEntity.isInsideOfMaterial(Material.WATER) && viewEntity instanceof EntityPlayer player) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                renderGlobal.drawBlockBreaking(player, this.potion.objectMouseOver, 0, player.inventory.getCurrentItem(), partialTicks);
                renderGlobal.drawSelectionBox(player, this.potion.objectMouseOver, 0, player.inventory.getCurrentItem(), partialTicks);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }


            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(true);
            this.setupFog(0, partialTicks);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("/terrain.png"));
            if (this.potion.gameSettings.fancyGraphics) {
                if (this.potion.gameSettings.ambientOcclusion) {
                    GL11.glShadeModel(GL11.GL_SMOOTH);
                }

                GL11.glColorMask(false, false, false, false);
                int var22 = renderGlobal.sortAndRender(viewEntity, 1, partialTicks);
                if (this.potion.gameSettings.anaglyph) {
                    if (anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }

                if (var22 > 0) {
                    renderGlobal.renderAllRenderLists(1, partialTicks);
                }

                GL11.glShadeModel(GL11.GL_FLAT);
            } else {
                renderGlobal.sortAndRender(viewEntity, 1, partialTicks);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            if (this.cameraZoom == 1.0D && viewEntity instanceof EntityPlayer player && this.potion.objectMouseOver != null && !viewEntity.isInsideOfMaterial(Material.WATER)) {
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                renderGlobal.drawBlockBreaking(player, this.potion.objectMouseOver, 0, player.inventory.getCurrentItem(), partialTicks);
                renderGlobal.drawSelectionBox(player, this.potion.objectMouseOver, 0, player.inventory.getCurrentItem(), partialTicks);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            this.renderRainSnow(partialTicks);
            GL11.glDisable(GL11.GL_FOG);

            this.setupFog(0, partialTicks);
            GL11.glEnable(GL11.GL_FOG);
            renderGlobal.renderClouds(partialTicks);
            GL11.glDisable(GL11.GL_FOG);
            this.setupFog(1, partialTicks);
            if (this.cameraZoom == 1.0D) {
                GL11.glClear(256);
                this.renderHand(partialTicks, pass);
            }

            if (!this.potion.gameSettings.anaglyph) {
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
    }

    private void addRainParticles() {
        float var1 = this.potion.theWorld.getRainStrength(1.0F);
        if (!this.potion.gameSettings.fancyGraphics) {
            var1 /= 2.0F;
        }

        if (var1 != 0.0F) {
            this.random.setSeed((long) this.rendererUpdateCount * 312987231L);
            EntityLiving var2 = this.potion.renderViewEntity;
            World var3 = this.potion.theWorld;
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
                int var18 = var3.findTopSolidOrLiquidBlock(var16, var17);
                int var19 = var3.getBlockId(var16, var18 - 1, var17);
                if (var18 <= var5 + var7 && var18 >= var5 - var7 && var3.getWorldChunkManager().getBiomeGenAt(var16, var17).canSpawnLightningBolt()) {
                    float var20 = this.random.nextFloat();
                    float var21 = this.random.nextFloat();
                    if (var19 > 0) {
                        if (Block.BLOCKS_LIST[var19].blockMaterial == Material.LAVA) {
                            this.potion.effectRenderer.addEffect(new EntitySmokeFX(var3, (float) var16 + var20, (double) ((float) var18 + 0.1F) - Block.BLOCKS_LIST[var19].minY, (float) var17 + var21, 0.0D, 0.0D, 0.0D));
                        } else {
                            ++var14;
                            if (this.random.nextInt(var14) == 0) {
                                var8 = (float) var16 + var20;
                                var10 = (double) ((float) var18 + 0.1F) - Block.BLOCKS_LIST[var19].minY;
                                var12 = (float) var17 + var21;
                            }

                            this.potion.effectRenderer.addEffect(new EntityRainFX(var3, (float) var16 + var20, (double) ((float) var18 + 0.1F) - Block.BLOCKS_LIST[var19].minY, (float) var17 + var21));
                        }
                    }
                }
            }

            if (var14 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (var10 > var2.posY + 1.0D && var3.findTopSolidOrLiquidBlock(MathHelper.floor(var2.posX), MathHelper.floor(var2.posZ)) > MathHelper.floor(var2.posY)) {
                    this.potion.theWorld.playSoundEffect(var8, var10, var12, "ambient.weather.rain", 0.1F, 0.5F);
                } else {
                    this.potion.theWorld.playSoundEffect(var8, var10, var12, "ambient.weather.rain", 0.2F, 1.0F);
                }
            }

        }
    }

    protected void renderRainSnow(float var1) {
        float rainStrength = this.potion.theWorld.getRainStrength(var1);
        if (rainStrength <= 0.0F)
            return;

        EntityLiving viewEntity = this.potion.renderViewEntity;
        World world = this.potion.theWorld;
        int viewX = MathHelper.floor(viewEntity.posX);
        int viewY = MathHelper.floor(viewEntity.posY);
        int viewZ = MathHelper.floor(viewEntity.posZ);
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("/environment/snow.png"));
        double var9 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double) var1;
        double var11 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double) var1;
        double var13 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double) var1;
        int var15 = MathHelper.floor(var11);
        byte var16 = 5;
        if (this.potion.gameSettings.fancyGraphics) {
            var16 = 10;
        }

        BiomeGenBase[] var17 = world.getWorldChunkManager().getBiomeGensAt(viewX - var16, viewZ - var16, var16 * 2 + 1, var16 * 2 + 1);
        int var18 = 0;

        for (int x = viewX - var16; x <= viewX + var16; ++x) {
            for (int z = viewZ - var16; z <= viewZ + var16; ++z) {
                BiomeGenBase var21 = var17[var18++];
                if (var21.getEnableSnow()) {
                    int topY = world.findTopSolidOrLiquidBlock(x, z);
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
                        tess.addVertexWithUV(x, var24, (double) z + 0.5D, 0.0F * var26 + var29, (float) var24 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV(x + 1, var24, (double) z + 0.5D, 1.0F * var26 + var29, (float) var24 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV(x + 1, var25, (double) z + 0.5D, 1.0F * var26 + var29, (float) var25 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV(x, var25, (double) z + 0.5D, 0.0F * var26 + var29, (float) var25 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV((double) x + 0.5D, var24, z, 0.0F * var26 + var29, (float) var24 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV((double) x + 0.5D, var24, z + 1, 1.0F * var26 + var29, (float) var24 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV((double) x + 0.5D, var25, z + 1, 1.0F * var26 + var29, (float) var25 * var26 / 4.0F + var28 * var26 + var30);
                        tess.addVertexWithUV((double) x + 0.5D, var25, z, 0.0F * var26 + var29, (float) var25 * var26 / 4.0F + var28 * var26 + var30);
                        tess.setTranslationD(0.0D, 0.0D, 0.0D);
                        tess.draw();
                    }
                }
            }
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("/environment/rain.png"));
        if (this.potion.gameSettings.fancyGraphics) {
            var16 = 10;
        }

        var18 = 0;

        for (int var38 = viewX - var16; var38 <= viewX + var16; ++var38) {
            for (int var39 = viewZ - var16; var39 <= viewZ + var16; ++var39) {
                BiomeGenBase var40 = var17[var18++];
                if (var40.canSpawnLightningBolt()) {
                    int var41 = world.findTopSolidOrLiquidBlock(var38, var39);
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
                        tess.addVertexWithUV(var38, var42, (double) var39 + 0.5D, 0.0F * var44, (float) var42 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV(var38 + 1, var42, (double) var39 + 0.5D, 1.0F * var44, (float) var42 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV(var38 + 1, var43, (double) var39 + 0.5D, 1.0F * var44, (float) var43 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV(var38, var43, (double) var39 + 0.5D, 0.0F * var44, (float) var43 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV((double) var38 + 0.5D, var42, var39, 0.0F * var44, (float) var42 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV((double) var38 + 0.5D, var42, var39 + 1, 1.0F * var44, (float) var42 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV((double) var38 + 0.5D, var43, var39 + 1, 1.0F * var44, (float) var43 * var44 / 4.0F + var45 * var44);
                        tess.addVertexWithUV((double) var38 + 0.5D, var43, var39, 0.0F * var44, (float) var43 * var44 / 4.0F + var45 * var44);
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
        final Window window = this.potion.window;
        ScaledResolution var1 = new ScaledResolution(this.potion.gameSettings, window.getWidth(), window.getHeight());
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, var1.width, var1.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    private void updateFogColor(float var1) {
        World var2 = this.potion.theWorld;
        EntityLiving var3 = this.potion.renderViewEntity;
        float var4 = 1.0F / (float) (4 - this.potion.gameSettings.renderDistance);
        var4 = 1.0F - (float) Math.pow(var4, 0.25D);
        Vec3d var5 = var2.getSkyColor(this.potion.renderViewEntity, var1);
        float var6 = (float) var5.x;
        float var7 = (float) var5.y;
        float var8 = (float) var5.z;
        Vec3d var9 = var2.getFogColor(var1);
        this.fogColorRed = (float) var9.x;
        this.fogColorGreen = (float) var9.y;
        this.fogColorBlue = (float) var9.z;
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

        float var17 = var2.getThunderStrength(var1);
        if (var17 > 0.0F) {
            float var18 = 1.0F - var17 * 0.5F;
            this.fogColorRed *= var18;
            this.fogColorGreen *= var18;
            this.fogColorBlue *= var18;
        }

        if (this.cloudFog) {
            Vec3d var19 = var2.cloudColor(var1);
            this.fogColorRed = (float) var19.x;
            this.fogColorGreen = (float) var19.y;
            this.fogColorBlue = (float) var19.z;
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
        if (this.potion.gameSettings.anaglyph) {
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
        EntityLiving var3 = this.potion.renderViewEntity;
        GL11.glFogfv(GL11.GL_FOG_COLOR, this.func_908_a(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.cloudFog) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            float var4 = 1.0F;
            float var5 = 1.0F;
            float var6 = 1.0F;
            if (this.potion.gameSettings.anaglyph) {
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
            if (this.potion.gameSettings.anaglyph) {
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
            if (this.potion.gameSettings.anaglyph) {
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

            if (this.potion.theWorld.worldProvider.isNether) {
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
