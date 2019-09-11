package net.minecraft.client;

import net.hypnosis.lwjgl.Api;
import net.hypnosis.lwjgl.ContextApi;
import net.hypnosis.lwjgl.LWJGL;
import net.hypnosis.lwjgl.Profile;
import net.hypnosis.monitor.Window;
import net.minecraft.achievement.AchievementList;
import net.minecraft.block.Block;
import net.minecraft.client.gui.*;
import net.minecraft.client.input.MouseHelper;
import net.minecraft.client.input.MovementInputFromOptions;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.RenderBlocks;
import net.minecraft.client.render.texture.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityRenderer;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetClientHandler;
import net.minecraft.sound.SoundManager;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatStringFormatKeyInv;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldRenderer;
import net.minecraft.world.chunk.ChunkCoordinates;
import net.minecraft.world.chunk.ChunkProviderLoadOrGenerate;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveConverterMcRegion;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public final class Minecraft implements Runnable {
    /**
     * Bytes reversed memory, that than will removed.
     * In this case it is 10 mebibytes.
     */
    public static byte[] reserved = new byte[(int) (10 * Math.pow(2, 20))];
    public static long[] frameTimes = new long[(int) Math.pow(2, 9)];
    public static long[] tickTimes = new long[(int) Math.pow(2, 9)];

    private static Minecraft instance;
    private static File minecraftDir = null;

    public static int numRecordedFrameTimes = 0;
    public static long hasPaidCheckTime = 0L;

    public String minecraftUri;
    private File mcDataDir;

    /**
     * Engine objects
     */
    public Window window;

    /**
     * Game objects
     */
    public GameSettings gameSettings;
    public MouseHelper mouseHelper;
    public MovingObjectPosition objectMouseOver = null;
    public SoundManager soundManager = new SoundManager();

    public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
    public RenderGlobal renderGlobal;
    public EffectRenderer effectRenderer;
    public EntityRenderer entityRenderer;
    public RenderEngine renderEngine;
    public FontRenderer fontRenderer;
    private OpenGlCapsChecker glCapabilities;

    public GuiScreen currentScreen = null;
    public GuiAchievement guiAchievement = new GuiAchievement(this);
    public GuiIngame ingameGUI;

    public World theWorld;
    public EntityPlayerSP thePlayer;
    public EntityLiving renderViewEntity;
    public PlayerController playerController;
    public Session session = null;

    public StatFileWriter statFileWriter;
    public TexturePackList texturePackList;
    private ISaveFormat saveLoader;
    private ThreadDownloadResources downloadResourcesThread;

    private TextureWaterFX textureWaterFX = new TextureWaterFX();
    private TextureLavaFX textureLavaFX = new TextureLavaFX();

    public long prevFrameTime = -1L;
    public long systemTime = System.currentTimeMillis();

    private boolean hasCrashed = false;
    public boolean isTakingScreenshot = false;
    public volatile boolean isGamePaused = false;
    public volatile boolean running = true;
    public boolean inGameHasFocus = false;
    public boolean skipRenderWorld = false;

    private int ticksRan = 0;
    private int leftClickCounter = 0;
    private int mouseTicksRan = 0;
    private int joinPlayerCounter = 0;

    public String debug = "";
    private String serverName;
    private int serverPort;

    private Timer timer = new Timer(20.0F);

    public Minecraft(int displayWidth, int displayHeight, boolean fullscreen) {
        StatList.func_27360_a();
        new ThreadSleepForever(this, "Timer hack thread");

        this.window = new Window("Minecraft Beta 1.7.3", displayWidth, displayHeight, MemoryUtil.NULL, fullscreen,
                true, this::resize, null, null, null);
        LWJGL.init(Api.OPENGL, ContextApi.NATIVE, Profile.CORE, 3, 3);
        this.window.makeCurrentContext();

        instance = this;
    }

    public static void main(String[] args) {
        String username = args.length > 0 ? args[0] : "Player" + System.currentTimeMillis() % 1000L;
        String sessionId = args.length > 1 ? args[1] : "-";
        startMainThread(username, sessionId);
    }

    public static File getMinecraftDir() {
        if (minecraftDir == null)
            minecraftDir = EnumOS.getAppDir("minecraft");

        return minecraftDir;
    }

    public static void startMainThread(String username, String sessionId) {
        startMainThread(username, sessionId, null);
    }

    public static void startMainThread(String username, String sessionId, String connectionIp) {
        Minecraft minecraft = new Minecraft(854, 480, false);

        minecraft.minecraftUri = "www.minecraft.net";

        if (username != null && sessionId != null)
            minecraft.session = new Session(username, sessionId);
        else
            minecraft.session = new Session("Player" + System.currentTimeMillis() % 1000L, "");

        if (connectionIp != null) {
            String[] addressArr = connectionIp.split(":");
            minecraft.setServer(addressArr[0], Integer.parseInt(addressArr[1]));
        }

        Thread thread = new Thread(minecraft, "Minecraft main thread");
        thread.setPriority(10);
        thread.start();
    }

    public static boolean isGuiEnabled() {
        return instance == null || !instance.gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled() {
        return instance != null && instance.gameSettings.fancyGraphics;
    }

    public static boolean isAmbientOcclusionEnabled() {
        return instance != null && instance.gameSettings.ambientOcclusion;
    }

    public static boolean isDebugInfoEnabled() {
        return instance != null && instance.gameSettings.showDebugInfo;
    }

    public void onMinecraftCrash(UnexpectedThrowable throwable) {
        this.hasCrashed = true;
        this.displayUnexpectedThrowable(throwable);
    }

    public void displayUnexpectedThrowable(UnexpectedThrowable throwable) {
        Frame frame = new Frame("Minecraft Crashed");
        frame.setLayout(new BorderLayout());
        frame.setSize(window.getWidth(), window.getHeight());
        frame.setLocation(window.getX(), window.getY());
        frame.add(new PanelCrashReport(throwable), "Center");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }

    public void setServer(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void startGame() {
        this.mcDataDir = getMinecraftDir();
        this.saveLoader = new SaveConverterMcRegion(new File(this.mcDataDir, "saves"));
        this.gameSettings = new GameSettings(this, this.mcDataDir);
        this.texturePackList = new TexturePackList(this, this.mcDataDir);
        this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
        this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine);
        ColorizerWater.setWaterBuffer(this.renderEngine.loadTexture("/misc/watercolor.png"));
        ColorizerGrass.setGrassBuffer(this.renderEngine.loadTexture("/misc/grasscolor.png"));
        ColorizerFoliage.setFoliageBuffer(this.renderEngine.loadTexture("/misc/foliagecolor.png"));
        this.entityRenderer = new EntityRenderer(this);
        RenderManager.instance.itemRenderer = new ItemRenderer(this);
        this.statFileWriter = new StatFileWriter(this.session, this.mcDataDir);
        AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
        this.loadScreen();
        Keyboard.create();
        Mouse.create();

        this.mouseHelper = new MouseHelper();

        try {
            Controllers.create();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.window.setPhase("Pre startup");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(515);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        this.window.setPhase("Startup");
        this.glCapabilities = new OpenGlCapsChecker();
        this.soundManager.loadSoundSettings(this.gameSettings);
        this.renderEngine.registerTextureFX(this.textureLavaFX);
        this.renderEngine.registerTextureFX(this.textureWaterFX);
        this.renderEngine.registerTextureFX(new TexturePortalFX());
        this.renderEngine.registerTextureFX(new TextureCompassFX(this));
        this.renderEngine.registerTextureFX(new TextureWatchFX(this));
        this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
        this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
        this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
        this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
        this.renderGlobal = new RenderGlobal(this, this.renderEngine);
        GL11.glViewport(0, 0, this.window.getWidth(), this.window.getHeight());
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

        try {
            this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
            this.downloadResourcesThread.start();
        } catch (Exception ignored) {
        }

        this.window.setPhase("Post startup");
        this.window.logOnGlError();

        this.ingameGUI = new GuiIngame(this);
        if (this.serverName != null) {
            this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
        } else {
            this.displayGuiScreen(new GuiMainMenu());
        }

    }

    private void loadScreen() {
        final Window window = this.window;
        final ScaledResolution res = new ScaledResolution(this.gameSettings, window.getWidth(), window.getHeight());

        GL11.glClear(16640);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, res.width, res.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Tessellator tess = Tessellator.INSTANCE;
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
        tess.startDrawingQuads();
        tess.setColorOpaque_I(16777215);
        tess.addVertexWithUV(0.0D, window.getHeight(), 0.0D, 0.0D, 0.0D);
        tess.addVertexWithUV(window.getWidth(), window.getHeight(), 0.0D, 0.0D, 0.0D);
        tess.addVertexWithUV(window.getWidth(), 0.0D, 0.0D, 0.0D, 0.0D);
        tess.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        tess.draw();
        short var3 = 256;
        short var4 = 256;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tess.setColorOpaque_I(16777215);
        this.func_6274_a((res.getScaledWidth() - var3) / 2, (res.getScaledHeight() - var4) / 2, 0, 0, var3, var4);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(516, 0.1F);
        window.swapBuffer();
    }

    public void func_6274_a(int var1, int var2, int var3, int var4, int var5, int var6) {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertexWithUV(var1 + 0, var2 + var6, 0.0D, (float) (var3 + 0) * var7, (float) (var4 + var6) * var8);
        tess.addVertexWithUV(var1 + var5, var2 + var6, 0.0D, (float) (var3 + var5) * var7, (float) (var4 + var6) * var8);
        tess.addVertexWithUV(var1 + var5, var2 + 0, 0.0D, (float) (var3 + var5) * var7, (float) (var4 + 0) * var8);
        tess.addVertexWithUV(var1 + 0, var2 + 0, 0.0D, (float) (var3 + 0) * var7, (float) (var4 + 0) * var8);
        tess.draw();
    }

    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }

    public void displayGuiScreen(GuiScreen guiScreen) {
        if (this.currentScreen instanceof GuiUnused)
            return;

        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }

        if (guiScreen instanceof GuiMainMenu) {
            this.statFileWriter.func_27175_b();
        }

        this.statFileWriter.syncStats();
        if (guiScreen == null && this.theWorld == null) {
            guiScreen = new GuiMainMenu();
        } else if (guiScreen == null && this.thePlayer.health <= 0) {
            guiScreen = new GuiGameOver();
        }

        if (guiScreen instanceof GuiMainMenu) {
            this.ingameGUI.clearChatMessages();
        }

        this.currentScreen = guiScreen;
        if (guiScreen != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledResolution = new ScaledResolution(this.gameSettings, this.window.getWidth(), this.window.getHeight());
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();
            guiScreen.setWorldAndResolution(this, width, height);
            this.skipRenderWorld = false;
        } else {
            this.setIngameFocus();
        }

    }

    public void shutdownMinecraftApplet() {
        try {
            this.statFileWriter.func_27175_b();
            this.statFileWriter.syncStats();

            try {
                if (this.downloadResourcesThread != null) {
                    this.downloadResourcesThread.closeMinecraft();
                }
            } catch (Exception ignored) {
            }

            System.out.println("Stopping!");

            try {
                this.changeWorld(null);
            } catch (Throwable ignored) {
            }

            try {
                GLAllocation.deleteTexturesAndDisplayLists();
            } catch (Throwable ignored) {
            }

            this.soundManager.closeMinecraft();
            Mouse.destroy();
            Keyboard.destroy();
        } finally {
            window.destroy();
            if (!this.hasCrashed) {
                System.exit(0);
            }

        }

        System.gc();
    }

    public void run() {
        this.running = true;

        try {
            this.startGame();
        } catch (Exception e) {
            e.printStackTrace();
            this.onMinecraftCrash(new UnexpectedThrowable("Failed to start game", e));
            return;
        }

        try {
            long renderStart = System.currentTimeMillis();
            int fps = 0;

            while (this.running) {
                try {
                    AxisAlignedBB.clearBoundingBoxPool();
                    Vec3D.initialize();
                    if (window.isCloseRequested()) {
                        this.shutdown();
                    }

                    if (this.isGamePaused && this.theWorld != null) {
                        float var4 = this.timer.renderPartialTicks;
                        this.timer.updateTimer();
                        this.timer.renderPartialTicks = var4;
                    } else {
                        this.timer.updateTimer();
                    }

                    long tickStart = System.nanoTime();

                    for (int i = 0; i < this.timer.elapsedTicks; ++i) {
                        ++this.ticksRan;

                        try {
                            this.runTick();
                        } catch (MinecraftException e) {
                            this.theWorld = null;
                            this.changeWorld(null);
                            this.displayGuiScreen(new GuiConflictWarning());
                        }
                    }

                    long totalTick = System.nanoTime() - tickStart;
                    this.window.setPhase("Pre render");
                    RenderBlocks.fancyGrass = this.gameSettings.fancyGraphics;
                    this.soundManager.func_338_a(this.thePlayer, this.timer.renderPartialTicks);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    if (this.theWorld != null) {
                        this.theWorld.updatingLighting();
                    }

                    if (!Keyboard.isKeyDown(Keyboard.KEY_F2)) {
                        this.window.update();
                    }

                    if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
                        this.gameSettings.thirdPersonView = false;
                    }

                    if (!this.skipRenderWorld) {
                        if (this.playerController != null) {
                            this.playerController.setPartialTime(this.timer.renderPartialTicks);
                        }

                        this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
                    }

                    if (!this.window.isFocused()) {
                        if (this.window.isFullscreen()) {
                            this.toggleFullscreen();
                        }

                        Thread.sleep(10L);
                    }

                    if (this.gameSettings.showDebugInfo) {
                        this.displayDebugInfo(totalTick);
                    } else {
                        this.prevFrameTime = System.nanoTime();
                    }

                    this.guiAchievement.updateAchievementWindow();
                    Thread.yield();
                    if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
                        this.window.update();
                    }

                    this.screenshotListener();

//                    int displayWidth = Display.getWidth();
//                    int displayHeight = Display.getHeight();
//                    if (!this.fullscreen && (displayWidth != this.displayWidth || displayHeight != this.displayHeight)) {
//                        this.displayWidth = displayWidth;
//                        this.displayHeight = displayHeight;
//
//                        if (this.displayWidth <= 0)
//                            this.displayWidth = 1;
//
//                        if (this.displayHeight <= 0)
//                            this.displayHeight = 1;
//
//                        this.resize(this.displayWidth, this.displayHeight);
//                    }

                    this.window.setPhase("Post render");
                    ++fps;

                    for (this.isGamePaused = !this.isMultiplayerWorld()
                            && this.currentScreen != null
                            && this.currentScreen.doesGuiPauseGame(); System.currentTimeMillis() >= renderStart + 1000L; fps = 0) {
                        this.debug = fps + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
                        WorldRenderer.chunksUpdated = 0;
                        renderStart += 1000L;
                    }
                } catch (MinecraftException e) {
                    this.theWorld = null;
                    this.changeWorld(null);
                    this.displayGuiScreen(new GuiConflictWarning());
                } catch (OutOfMemoryError e) {
                    this.func_28002_e();
                    this.displayGuiScreen(new GuiErrorScreen());
                    System.gc();
                }
            }
        } catch (MinecraftError ignored) {
        } catch (Throwable throwable) {
            this.func_28002_e();
            throwable.printStackTrace();
            this.onMinecraftCrash(new UnexpectedThrowable("Unexpected error", throwable));
        } finally {
            this.shutdownMinecraftApplet();
        }

    }

    public void func_28002_e() {
        try {
            reserved = new byte[0];
            this.renderGlobal.func_28137_f();
        } catch (Throwable ignored) {
        }

        try {
            System.gc();
            AxisAlignedBB.resetPool();
            Vec3D.resetPool();
        } catch (Throwable ignored) {
        }

        try {
            System.gc();
            this.changeWorld(null);
        } catch (Throwable ignored) {
        }

        System.gc();
    }

    private void screenshotListener() {
        if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
            if (this.isTakingScreenshot)
                return;
            this.isTakingScreenshot = true;

            Window window = this.window;
            this.ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, window.getWidth(), window.getHeight()));
        } else this.isTakingScreenshot = false;
    }

    private void displayDebugInfo(long var1) {
        long var3 = 16666666L;
        if (this.prevFrameTime == -1L) {
            this.prevFrameTime = System.nanoTime();
        }

        long var5 = System.nanoTime();
        tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = var1;
        frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = var5 - this.prevFrameTime;
        this.prevFrameTime = var5;
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        final Window window = this.window;
        int displayWidth = window.getWidth();
        int displayHeight = window.getHeight();

        GL11.glOrtho(0.0D, displayWidth, displayHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawing(7);
        int var8 = (int) (var3 / 200000L);
        tess.setColorOpaque_I(536870912);
        tess.addVertex(0.0D, displayHeight - var8, 0.0D);
        tess.addVertex(0.0D, displayHeight, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight - var8, 0.0D);
        tess.setColorOpaque_I(538968064);
        tess.addVertex(0.0D, displayHeight - var8 * 2, 0.0D);
        tess.addVertex(0.0D, displayHeight - var8, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight - var8, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight - var8 * 2, 0.0D);
        tess.draw();
        long var9 = 0L;

        for (long frameTime : frameTimes) {
            var9 += frameTime;
        }

        int var20 = (int) (var9 / 200000L / (long) frameTimes.length);
        tess.startDrawing(7);
        tess.setColorOpaque_I(541065216);
        tess.addVertex(0.0D, displayHeight - var20, 0.0D);
        tess.addVertex(0.0D, displayHeight, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight - var20, 0.0D);
        tess.draw();
        tess.startDrawing(1);

        for (int i = 0; i < frameTimes.length; ++i) {
            int var13 = (i - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
            int var14 = var13 * var13 / 255;
            var14 = var14 * var14 / 255;
            int var15 = var14 * var14 / 255;
            var15 = var15 * var15 / 255;
            if (frameTimes[i] > var3) {
                tess.setColorOpaque_I(-16777216 + var14 * 65536);
            } else {
                tess.setColorOpaque_I(-16777216 + var14 * 256);
            }

            long var16 = frameTimes[i] / 200000L;
            long var18 = tickTimes[i] / 200000L;
            tess.addVertex((float) i + 0.5F, (float) ((long) displayHeight - var16) + 0.5F, 0.0D);
            tess.addVertex((float) i + 0.5F, (float) displayHeight + 0.5F, 0.0D);
            tess.setColorOpaque_I(-16777216 + var14 * 65536 + var14 * 256 + var14 * 1);
            tess.addVertex((float) i + 0.5F, (float) ((long) displayHeight - var16) + 0.5F, 0.0D);
            tess.addVertex((float) i + 0.5F, (float) ((long) displayHeight - (var16 - var18)) + 0.5F, 0.0D);
        }

        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void shutdown() {
        this.running = false;
    }

    public void setIngameFocus() {
        if (!this.window.isFocused())
            return;

        if (this.inGameHasFocus)
            return;

        this.inGameHasFocus = true;
        this.mouseHelper.grabMouseCursor();
        this.displayGuiScreen(null);
        this.leftClickCounter = 10000;
        this.mouseTicksRan = this.ticksRan + 10000;
    }

    public void setIngameNotInFocus() {
        if (!this.inGameHasFocus)
            return;

        if (this.thePlayer != null) {
            this.thePlayer.resetPlayerKeyState();
        }

        this.inGameHasFocus = false;
        this.mouseHelper.ungrabMouseCursor();
    }

    public void displayInGameMenu() {
        if (this.currentScreen == null) {
            this.displayGuiScreen(new GuiIngameMenu());
        }
    }

    private void handleBlockBreaking(int buttonId, boolean pressed) {
        if (this.playerController.ghost)
            return;

        if (!pressed)
            this.leftClickCounter = 0;

        if (buttonId == 0 && this.leftClickCounter > 0)
            return;

        if (pressed
                && this.objectMouseOver != null
                && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE
                && buttonId == 0) {
            int x = this.objectMouseOver.blockX;
            int y = this.objectMouseOver.blockY;
            int z = this.objectMouseOver.blockZ;
            this.playerController.sendBlockRemoving(x, y, z, this.objectMouseOver.sideHit);
            this.effectRenderer.addBlockHitEffects(x, y, z, this.objectMouseOver.sideHit);
        } else this.playerController.resetBlockRemoving();
    }

    /**
     * Calls on mouse click.
     *
     * @param buttonId - id of mouse button.
     *                 0 = Left Click
     *                 1 = Right Click
     */
    private void clickMouse(int buttonId) {
        if (buttonId == 0 && this.leftClickCounter > 0)
            return;

        if (buttonId == 0) {
            this.thePlayer.swingItem();
        }

        boolean var2 = true;
        if (this.objectMouseOver == null) {
            if (buttonId == 0 && !(this.playerController instanceof PlayerControllerTest)) {
                this.leftClickCounter = 10;
            }
        } else if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
            if (buttonId == 0) {
                this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
            }

            if (buttonId == 1) {
                this.playerController.interactWithEntity(this.thePlayer, this.objectMouseOver.entityHit);
            }
        } else if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
            int var3 = this.objectMouseOver.blockX;
            int var4 = this.objectMouseOver.blockY;
            int var5 = this.objectMouseOver.blockZ;
            int var6 = this.objectMouseOver.sideHit;
            if (buttonId == 0) {
                this.playerController.clickBlock(var3, var4, var5, this.objectMouseOver.sideHit);
            } else {
                ItemStack currentItem = this.thePlayer.inventory.getCurrentItem();
                int var8 = currentItem != null ? currentItem.stackSize : 0;
                if (this.playerController.sendPlaceBlock(this.thePlayer, this.theWorld, currentItem, var3, var4, var5, var6)) {
                    var2 = false;
                    this.thePlayer.swingItem();
                }

                if (currentItem == null)
                    return;

                if (currentItem.stackSize == 0) {
                    this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                } else if (currentItem.stackSize != var8) {
                    this.entityRenderer.itemRenderer.func_9449_b();
                }
            }
        }

        if (var2 && buttonId == 1) {
            ItemStack currentItem = this.thePlayer.inventory.getCurrentItem();
            if (currentItem != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, currentItem)) {
                this.entityRenderer.itemRenderer.func_9450_c();
            }
        }
    }

    public void toggleFullscreen() {
        try {
            boolean fullscreen = this.window.isFullscreen();
            this.window.setFullscreen(!fullscreen);

            if (this.window.isFullscreen()) {
                this.window.setResizable(!fullscreen);
            }

            this.window.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resize(long window, int width, int height) {
        if (this.currentScreen == null)
            return;

        ScaledResolution scaledResolution = new ScaledResolution(this.gameSettings, width, height);
        this.currentScreen.setWorldAndResolution(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
    }

    private void clickMiddleMouseButton() {
        if (this.objectMouseOver == null)
            return;

        int blockId = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
        if (blockId == Block.GRASS.blockID)
            blockId = Block.DIRT.blockID;

        if (blockId == Block.STAIR_DOUBLE.blockID)
            blockId = Block.STAIR_SINGLE.blockID;

        if (blockId == Block.BEDROCK.blockID)
            blockId = Block.STONE.blockID;

        this.thePlayer.inventory.setCurrentItem(blockId, this.playerController instanceof PlayerControllerTest);
    }

    private void func_28001_B() {
        new ThreadCheckHasPaid(this).start();
    }

    public void runTick() {
        if (this.ticksRan == 6000) {
            this.func_28001_B();
        }

        this.statFileWriter.func_27178_d();
        this.ingameGUI.updateTick();
        this.entityRenderer.getMouseOver(1.0F);
        if (this.thePlayer != null) {
            IChunkProvider provider = this.theWorld.getIChunkProvider();
            if (provider instanceof ChunkProviderLoadOrGenerate) {
                ChunkProviderLoadOrGenerate var2 = (ChunkProviderLoadOrGenerate) provider;
                int var3 = MathHelper.floor((float) ((int) this.thePlayer.posX)) >> 4;
                int var4 = MathHelper.floor((float) ((int) this.thePlayer.posZ)) >> 4;
                var2.setCurrentChunkOver(var3, var4);
            }
        }

        if (!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
        if (!this.isGamePaused) {
            this.renderEngine.updateDynamicTextures();
        }

        if (this.currentScreen == null && this.thePlayer != null) {
            if (this.thePlayer.health <= 0) {
                this.displayGuiScreen(null);
            } else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null && this.theWorld.multiplayerWorld) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
            this.displayGuiScreen(null);
        }

        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
            this.mouseTicksRan = this.ticksRan + 10000;
        }

        if (this.currentScreen != null) {
            this.currentScreen.handleInput();
            if (this.currentScreen != null) {
                this.currentScreen.guiParticle.func_25088_a();
                this.currentScreen.updateScreen();
            }
        }

        if (this.currentScreen == null || this.currentScreen.field_948_f) {
            while (Mouse.next()) {
                long var5 = System.currentTimeMillis() - this.systemTime;
                if (var5 <= 200L) {
                    int var7 = Mouse.getEventDWheel();
                    if (var7 != 0) {
                        this.thePlayer.inventory.changeCurrentItem(var7);
                        if (this.gameSettings.field_22275_C) {
                            if (var7 > 0) {
                                var7 = 1;
                            }

                            if (var7 < 0) {
                                var7 = -1;
                            }

                            this.gameSettings.field_22272_F += (float) var7 * 0.25F;
                        }
                    }

                    if (this.currentScreen == null) {
                        if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                            this.setIngameFocus();
                        } else {
                            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                                this.clickMouse(0);
                                this.mouseTicksRan = this.ticksRan;
                            }

                            if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                                this.clickMouse(1);
                                this.mouseTicksRan = this.ticksRan;
                            }

                            if (Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
                                this.clickMiddleMouseButton();
                            }
                        }
                    } else if (this.currentScreen != null) {
                        this.currentScreen.handleMouseInput();
                    }
                }
            }

            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }

            while (Keyboard.next()) {
                this.thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if (!Keyboard.getEventKeyState())
                    continue;

                if (Keyboard.getEventKey() == Keyboard.KEY_F11) {
                    this.toggleFullscreen();
                    continue;
                }

                if (this.currentScreen == null) {
                    if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
                        this.displayInGameMenu();

                    if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_F3))
                        this.forceReload();

                    if (Keyboard.getEventKey() == Keyboard.KEY_F1)
                        this.gameSettings.hideGUI = !this.gameSettings.hideGUI;

                    if (Keyboard.getEventKey() == Keyboard.KEY_F3)
                        this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;

                    if (Keyboard.getEventKey() == Keyboard.KEY_F5)
                        this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;

                    if (Keyboard.getEventKey() == Keyboard.KEY_F8)
                        this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;

                    if (Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode)
                        this.displayGuiScreen(new GuiInventory(this.thePlayer));

                    if (Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode)
                        this.thePlayer.dropCurrentItem();

                    if (this.isMultiplayerWorld() && Keyboard.getEventKey() == this.gameSettings.keyBindChat.keyCode)
                        this.displayGuiScreen(new GuiChat());
                } else this.currentScreen.handleKeyboardInput();

                for (int i = 0; i < 9; ++i) {
                    if (Keyboard.getEventKey() == 2 + i) {
                        this.thePlayer.inventory.currentItem = i;
                    }
                }

                if (Keyboard.getEventKey() == this.gameSettings.keyBindToggleFog.keyCode) {
                    this.gameSettings.setOptionValue(EnumOption.RENDER_DISTANCE, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
                }
            }

            if (this.currentScreen == null) {
                if (Mouse.isButtonDown(0) && (float) (this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
                    this.clickMouse(0);
                    this.mouseTicksRan = this.ticksRan;
                }

                if (Mouse.isButtonDown(1) && (float) (this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
                    this.clickMouse(1);
                    this.mouseTicksRan = this.ticksRan;
                }
            }

            this.handleBlockBreaking(0, this.currentScreen == null && Mouse.isButtonDown(0) && this.inGameHasFocus);
        }

        if (this.theWorld != null) {
            if (this.thePlayer != null) {
                ++this.joinPlayerCounter;
                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.theWorld.joinEntityInSurroundings(this.thePlayer);
                }
            }

            this.theWorld.difficultySetting = this.gameSettings.difficulty;
            if (this.theWorld.multiplayerWorld) {
                this.theWorld.difficultySetting = 3;
            }

            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }

            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }

            if (!this.isGamePaused) {
                if (this.theWorld.field_27172_i > 0) {
                    --this.theWorld.field_27172_i;
                }

                this.theWorld.updateEntities();
            }

            if (!this.isGamePaused || this.isMultiplayerWorld()) {
                this.theWorld.setAllowedMobSpawns(this.gameSettings.difficulty > 0, true);
                this.theWorld.tick();
            }

            if (!this.isGamePaused && this.theWorld != null) {
                this.theWorld.randomDisplayUpdates(MathHelper.floor(this.thePlayer.posX), MathHelper.floor(this.thePlayer.posY), MathHelper.floor(this.thePlayer.posZ));
            }

            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        }

        this.systemTime = System.currentTimeMillis();
    }

    private void forceReload() {
        System.out.println("FORCING RELOAD!");
        this.soundManager = new SoundManager();
        this.soundManager.loadSoundSettings(this.gameSettings);
        this.downloadResourcesThread.reloadResources();
    }

    public boolean isMultiplayerWorld() {
        return this.theWorld != null && this.theWorld.multiplayerWorld;
    }

    public void startWorld(String var1, String var2, long var3) {
        this.changeWorld(null);
        System.gc();
        if (this.saveLoader.isOldMapFormat(var1)) {
            this.convertMapFormat(var1, var2);
            return;
        }

        ISaveHandler saveLoader = this.saveLoader.getSaveLoader(var1, false);
        World world = new World(saveLoader, var2, var3);

        if (world.isNewWorld) {
            this.statFileWriter.addStat(StatList.createWorldStat, 1);
            this.statFileWriter.addStat(StatList.startGameStat, 1);
            this.changeWorld(world, "Generating level");
            return;
        }

        this.statFileWriter.addStat(StatList.loadWorldStat, 1);
        this.statFileWriter.addStat(StatList.startGameStat, 1);
        this.changeWorld(world, "Loading level");
    }

    public void usePortal() {
        System.out.println("Toggling dimension!!");

        if (this.thePlayer.dimension == -1)
            this.thePlayer.dimension = 0;
        else
            this.thePlayer.dimension = -1;

        this.theWorld.setEntityDead(this.thePlayer);
        this.thePlayer.isDead = false;
        double x = this.thePlayer.posX;
        double z = this.thePlayer.posZ;
        double var5 = 8.0D;
        if (this.thePlayer.dimension == -1) {
            x = x / var5;
            z = z / var5;
            this.thePlayer.setLocationAndAngles(x, this.thePlayer.posY, z, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
            if (this.thePlayer.isEntityAlive()) {
                this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
            }

            World world = new World(this.theWorld, WorldProvider.getProviderForDimension(-1));
            this.changeWorld(world, "Entering the Nether", this.thePlayer);
        } else {
            x = x * var5;
            z = z * var5;
            this.thePlayer.setLocationAndAngles(x, this.thePlayer.posY, z, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
            if (this.thePlayer.isEntityAlive()) {
                this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
            }

            World world = new World(this.theWorld, WorldProvider.getProviderForDimension(0));
            this.changeWorld(world, "Leaving the Nether", this.thePlayer);
        }

        this.thePlayer.worldObj = this.theWorld;
        if (this.thePlayer.isEntityAlive()) {
            this.thePlayer.setLocationAndAngles(x, this.thePlayer.posY, z, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
            this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
            new Teleporter().func_4107_a(this.theWorld, this.thePlayer);
        }

    }

    public void changeWorld(World world) {
        this.changeWorld(world, "");
    }

    public void changeWorld(World world, String loadScreenText) {
        this.changeWorld(world, loadScreenText, null);
    }

    public void changeWorld(World world, String loadScreenText, EntityPlayer player) {
        this.statFileWriter.func_27175_b();
        this.statFileWriter.syncStats();
        this.renderViewEntity = null;
        this.loadingScreen.printText(loadScreenText);
        this.loadingScreen.displayLoadingString("");
        this.soundManager.playStreaming(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        if (this.theWorld != null) {
            this.theWorld.saveWorldIndirectly(this.loadingScreen);
        }

        this.theWorld = world;
        if (world != null) {
            this.playerController.func_717_a(world);
            if (!this.isMultiplayerWorld()) {
                if (player == null) {
                    this.thePlayer = (EntityPlayerSP) world.func_4085_a(EntityPlayerSP.class);
                }
            } else if (this.thePlayer != null) {
                this.thePlayer.preparePlayerToSpawn();
                if (world != null) {
                    world.entityJoinedWorld(this.thePlayer);
                }
            }

            if (!world.multiplayerWorld) {
                this.func_6255_d(loadScreenText);
            }

            if (this.thePlayer == null) {
                this.thePlayer = (EntityPlayerSP) this.playerController.createPlayer(world);
                this.thePlayer.preparePlayerToSpawn();
                this.playerController.flipPlayer(this.thePlayer);
            }

            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            if (this.renderGlobal != null) {
                this.renderGlobal.changeWorld(world);
            }

            if (this.effectRenderer != null) {
                this.effectRenderer.clearEffects(world);
            }

            this.playerController.func_6473_b(this.thePlayer);
            if (player != null) {
                world.emptyMethod1();
            }

            IChunkProvider chunkProvider = world.getIChunkProvider();
            if (chunkProvider instanceof ChunkProviderLoadOrGenerate) {
                ChunkProviderLoadOrGenerate var5 = (ChunkProviderLoadOrGenerate) chunkProvider;
                int var6 = MathHelper.floor((float) ((int) this.thePlayer.posX)) >> 4;
                int var7 = MathHelper.floor((float) ((int) this.thePlayer.posZ)) >> 4;
                var5.setCurrentChunkOver(var6, var7);
            }

            world.spawnPlayerWithLoadedChunks(this.thePlayer);
            if (world.isNewWorld)
                world.saveWorldIndirectly(this.loadingScreen);

            this.renderViewEntity = this.thePlayer;
        } else this.thePlayer = null;


        System.gc();
        this.systemTime = 0L;
    }

    private void convertMapFormat(String var1, String var2) {
        this.loadingScreen.printText("Converting World to " + this.saveLoader.getFormatName());
        this.loadingScreen.displayLoadingString("This may take a while :)");
        this.saveLoader.convertMapFormat(var1, this.loadingScreen);
        this.startWorld(var1, var2, 0L);
    }

    private void func_6255_d(String var1) {
        this.loadingScreen.printText(var1);
        this.loadingScreen.displayLoadingString("Building terrain");
        short var2 = 128;
        int var3 = 0;
        int var4 = var2 * 2 / 16 + 1;
        var4 = var4 * var4;
        IChunkProvider var5 = this.theWorld.getIChunkProvider();
        ChunkCoordinates var6 = this.theWorld.getSpawnPoint();
        if (this.thePlayer != null) {
            var6.x = (int) this.thePlayer.posX;
            var6.z = (int) this.thePlayer.posZ;
        }

        if (var5 instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate var7 = (ChunkProviderLoadOrGenerate) var5;
            var7.setCurrentChunkOver(var6.x >> 4, var6.z >> 4);
        }

        for (int var11 = -var2; var11 <= var2; var11 += 16) {
            for (int var8 = -var2; var8 <= var2; var8 += 16) {
                this.loadingScreen.setLoadingProgress(var3++ * 100 / var4);
                this.theWorld.getBlockId(var6.x + var11, 64, var6.z + var8);

                while (this.theWorld.updatingLighting()) {
                }
            }
        }

        this.loadingScreen.displayLoadingString("Simulating world for a bit");
        var4 = 2000;
        this.theWorld.func_656_j();
    }

    public void installResource(String resource, File file) {
        int index = resource.indexOf("/");
        String path = resource.substring(0, index);
        resource = resource.substring(index + 1);

        if (path.equalsIgnoreCase("sound")) {
            this.soundManager.addSound(resource, file);
        } else if (path.equalsIgnoreCase("newsound")) {
            this.soundManager.addSound(resource, file);
        } else if (path.equalsIgnoreCase("streaming")) {
            this.soundManager.addStreaming(resource, file);
        } else if (path.equalsIgnoreCase("music")) {
            this.soundManager.addMusic(resource, file);
        } else if (path.equalsIgnoreCase("newmusic")) {
            this.soundManager.addMusic(resource, file);
        }

    }

    public OpenGlCapsChecker getOpenGlCapsChecker() {
        return this.glCapabilities;
    }

    public String func_6241_m() {
        return this.renderGlobal.getDebugInfoRenders();
    }

    public String func_6262_n() {
        return this.renderGlobal.getDebugInfoEntities();
    }

    public String func_21002_o() {
        return this.theWorld.func_21119_g();
    }

    public String func_6245_o() {
        return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.func_687_d();
    }

    public void respawn(boolean var1, int var2) {
        if (!this.theWorld.multiplayerWorld && !this.theWorld.worldProvider.canRespawnHere()) {
            this.usePortal();
        }

        ChunkCoordinates var3 = null;
        ChunkCoordinates var4 = null;
        boolean var5 = true;
        if (this.thePlayer != null && !var1) {
            var3 = this.thePlayer.getPlayerSpawnCoordinate();
            if (var3 != null) {
                var4 = EntityPlayer.func_25060_a(this.theWorld, var3);
                if (var4 == null) {
                    this.thePlayer.addChatMessage("tile.bed.notValid");
                }
            }
        }

        if (var4 == null) {
            var4 = this.theWorld.getSpawnPoint();
            var5 = false;
        }

        IChunkProvider chunkProvider = this.theWorld.getIChunkProvider();
        if (chunkProvider instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate loadOrGenerate = (ChunkProviderLoadOrGenerate) chunkProvider;
            loadOrGenerate.setCurrentChunkOver(var4.x >> 4, var4.z >> 4);
        }

        this.theWorld.setSpawnLocation();
        this.theWorld.updateEntityList();
        int playerId = 0;
        if (this.thePlayer != null) {
            playerId = this.thePlayer.entityId;
            this.theWorld.setEntityDead(this.thePlayer);
        }

        this.renderViewEntity = null;
        this.thePlayer = (EntityPlayerSP) this.playerController.createPlayer(this.theWorld);
        this.thePlayer.dimension = var2;
        this.renderViewEntity = this.thePlayer;
        this.thePlayer.preparePlayerToSpawn();
        if (var5) {
            this.thePlayer.setPlayerSpawnCoordinate(var3);
            this.thePlayer.setLocationAndAngles((float) var4.x + 0.5F, (float) var4.y + 0.1F, (float) var4.z + 0.5F, 0.0F, 0.0F);
        }

        this.playerController.flipPlayer(this.thePlayer);
        this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
        this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.thePlayer.entityId = playerId;
        this.thePlayer.func_6420_o();
        this.playerController.func_6473_b(this.thePlayer);
        this.func_6255_d("Respawning");
        if (this.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }

    }

    public NetClientHandler getSendQueue() {
        return this.thePlayer instanceof EntityClientPlayerMP
                ? ((EntityClientPlayerMP) this.thePlayer).sendQueue
                : null;
    }

    public boolean lineIsCommand(String line) {
        return line.startsWith("/");
    }

    public static Minecraft getInstance() {
        return instance;
    }
}
