package net.minecraft.client;

import net.hypnosis.input.KeySource;
import net.hypnosis.input.mouse.Mouse;
import net.hypnosis.monitor.Window;
import net.hypnosis.render.Tessellator;
import net.hypnosis.render.gl.Api;
import net.hypnosis.render.gl.ContextApi;
import net.hypnosis.render.gl.OpenGL;
import net.hypnosis.render.gl.Profile;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.achievement.AchievementList;
import net.minecraft.block.Block;
import net.minecraft.client.gui.*;
import net.minecraft.client.input.keyboard.CraftKeyboard;
import net.minecraft.client.input.keyboard.MovementInputFromOptions;
import net.minecraft.client.input.mouse.MouseHelper;
import net.minecraft.client.loading.ClientLoadGui;
import net.minecraft.client.loading.ClientLoadThread;
import net.minecraft.client.loading.LoadingModel;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.RenderBlocks;
import net.minecraft.client.render.texture.*;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityRenderer;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetClientHandler;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldRenderer;
import net.minecraft.world.chunk.ChunkCoordinates;
import net.minecraft.world.chunk.ChunkProviderLoadOrGenerate;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveConverterRegion;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public final class MinecraftClient implements Runnable {
    public static final String VERSION = "1.7.3b";
    /**
     * Bytes reversed memory, that than will removed.
     * In this case it is 10 mebibytes.
     */
    public static byte[] reserved = new byte[(int) (10 * Math.pow(2, 20))];
    public static long[] frameTimes = new long[(int) Math.pow(2, 9)];
    public static long[] tickTimes = new long[(int) Math.pow(2, 9)];

    private static MinecraftClient instance;
    private static File minecraftDir = null;

    public static int numRecordedFrameTimes = 0;
    public static long hasPaidCheckTime = 0L;

    public String minecraftUri;
    private File dataDir;

    /**
     * Engine objects
     */
    public Window window;
    public Mouse mouse;
    public CraftKeyboard keyboard;

    /**
     * Game system objects
     */
    public GameSettings gameSettings;
    public MouseHelper mouseHelper;
    public MovingObjectPosition objectMouseOver = null;
    public SoundManager soundManager = new SoundManager();

    /**
     * Render objects
     */
    public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
    public RenderGlobal renderGlobal;
    public EffectRenderer effectRenderer;
    public EntityRenderer entityRenderer;
    public RenderEngine renderEngine;
    public FontRenderer fontRenderer;
    private OpenGlCapsChecker glCapabilities;

    /**
     * Gui screen objects
     */
    public GuiScreen currentScreen = null;
    public GuiAchievement guiAchievement = new GuiAchievement(this);
    public GuiIngame ingameGUI;

    /**
     * Game world objects
     */
    public World theWorld;
    public EntityPlayerSP thePlayer;
    public EntityLiving renderViewEntity;
    public PlayerController playerController;
    public Session session = null;

    /**
     * Resource objects
     */
    public StatFileWriter statFileWriter;
    public TexturePackList texturePackList;
    private ISaveFormat saveLoader;
    private ClientLoadThread clientLoadThread;

    /**
     * Dynamic textures
     */
    private TextureWaterFX textureWaterFX = new TextureWaterFX();
    private TextureLavaFX textureLavaFX = new TextureLavaFX();

    /**
     * Render utility variables
     */
    public long prevFrameTime = -1L;
    public long systemTime = System.currentTimeMillis();

    /**
     * Game state variables
     */
    private boolean hasCrashed = false;
    public boolean isTakingScreenshot = false;
    public volatile boolean isGamePaused = false;
    public volatile boolean running = true;
    public boolean inGameHasFocus = false;
    public boolean skipRenderWorld = false;

    /**
     * Counter variables
     */
    private int ticksRan = 0;
    private int fpsCounter;
    private int leftClickCounter = 0;
    private int mouseTicksRan = 0;
    private int joinPlayerCounter = 0;

    public String debug = "";
    private String serverName;
    private int serverPort;

    private final Timer timer = new Timer(20.0F);
    private long debugUpdateTime = System.currentTimeMillis();

    /**
     * Arguments of game
     */
    private final int displayWidthArg, displayHeightArg;
    private final boolean fullscreenArg;


    public MinecraftClient(int displayWidth, int displayHeight, boolean fullscreen) {
        this.displayWidthArg = displayWidth;
        this.displayHeightArg = displayHeight;
        this.fullscreenArg = fullscreen;

        StatList.init();
        new ThreadSleepForever("Timer hack thread");
        instance = this;
    }

    public void startGame() {
        OpenGL.init(Api.OPENGL, ContextApi.NATIVE, Profile.COMPAT, 3, 3, false);
        this.window = Window.builder()
                .title("Minecraft [" + MinecraftClient.VERSION + "]")
                .width(displayWidthArg)
                .height(displayHeightArg)
                .fullscreen(fullscreenArg)
                .onResize(this::resize)
                .build();
        this.window.show();

        this.dataDir = getMinecraftDir();
        this.saveLoader = new SaveConverterRegion(new File(this.dataDir, "saves"));
        this.gameSettings = new GameSettings(this, this.dataDir);
        this.texturePackList = new TexturePackList(this, this.dataDir);
        this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
        this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine);

        this.mouse = new Mouse(window);
        this.keyboard = new CraftKeyboard(window);
        this.mouseHelper = new MouseHelper(window, mouse);

        RenderColorizerWater.setWaterBuffer(this.renderEngine.loadTexture("/misc/watercolor.png"));
        RenderColorizerGrass.setGrassBuffer(this.renderEngine.loadTexture("/misc/grasscolor.png"));
        RenderColorizerFoliage.setFoliageBuffer(this.renderEngine.loadTexture("/misc/foliagecolor.png"));

        this.entityRenderer = new EntityRenderer(this);
        RenderManager.instance.itemRenderer = new ItemRenderer(this);
        this.statFileWriter = new StatFileWriter(this.session, this.dataDir);

        AchievementList.openInventory.setStatStringFormatter(s -> {
            String name = KeySource.KEYBOARD.getKeyInfo(this.gameSettings.keyBindInventory.keyCode).getName();
            return String.format(s, StringTranslate.getInstance().translateKey(name));
        });

        this.window.setPhase("Pre startup");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        this.window.setPhase("Startup");
        this.glCapabilities = new OpenGlCapsChecker();

        this.soundManager.setSettings(this.gameSettings);

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
        this.clientLoadThread = new ClientLoadThread(this.dataDir, this);

        final LoadingModel loadingModel = new LoadingModel(clientLoadThread.countLoadUnits());
        final ClientLoadGui clientLoadGui = new ClientLoadGui(this, window, gameSettings, renderEngine, fontRenderer, loadingModel);
        clientLoadThread.setup(loadingModel);

        this.mouse.setScrollCallback(this::onScroll);
        this.mouse.setButtonCallback(this::onMouseButton);

        this.keyboard.setCharCallback(this::onChar);
        this.keyboard.setKeyCallback(this::onKey);

        try {
            this.clientLoadThread.start();
            while (!loadingModel.isDone()) {
                clientLoadGui.update();
                Thread.sleep(10);
            }
        } catch (Exception ignored) {
        }

        this.window.setPhase("Post startup");
        this.window.logOnGlError();

        this.ingameGUI = new GuiIngame(this);

        if (this.serverName != null)
            this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
        else
            this.displayGuiScreen(new GuiMainMenu());
    }

    public void onChar(char ch, int keycode) {
        if (this.currentScreen != null)
            this.currentScreen.charTyped(ch, keycode);
    }

    public void onKey(int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_F11)
            this.toggleFullscreen();

        if (key == GLFW.GLFW_KEY_F2) {
            this.window.update();

            if (this.ingameGUI != null) {
                if (action == GLFW.GLFW_PRESS) {
                    this.isTakingScreenshot = true;
                    this.ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, this.window.getWidth(), this.window.getHeight()));
                } else this.isTakingScreenshot = false;
            }
        }

        if (key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS) {
            this.toggleFullscreen();
            return;
        }

        if (this.currentScreen == null || this.currentScreen.inputable)
            this.thePlayer.handleKeyPress(key, (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT));

        if (this.currentScreen == null) {
            // Ingame control keys
            if (action == GLFW.GLFW_PRESS) {
                if (key == GLFW.GLFW_KEY_ESCAPE)
                    this.displayInGameMenu();

                if (key == GLFW.GLFW_KEY_S && keyboard.isKeyDown(GLFW.GLFW_KEY_F3))
                    this.forceReload();

                if (key == GLFW.GLFW_KEY_F1)
                    this.gameSettings.hideGUI = !this.gameSettings.hideGUI;

                if (key == GLFW.GLFW_KEY_F3)
                    this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;

                if (key == GLFW.GLFW_KEY_F5)
                    this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;

                if (key == GLFW.GLFW_KEY_F8)
                    this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;

                if (key == this.gameSettings.keyBindInventory.keyCode)
                    this.displayGuiScreen(new GuiInventory(this.thePlayer));

                if (key == this.gameSettings.keyBindDrop.keyCode)
                    this.thePlayer.dropCurrentItem();

                if (this.isMultiplayerWorld() && key == this.gameSettings.keyBindChat.keyCode)
                    this.displayGuiScreen(new GuiChat());

                for (int i = 0; i < 9; ++i) {
                    if (key == GLFW.GLFW_KEY_1 + i)
                        this.thePlayer.inventory.currentItem = i;
                }
            }
        } else {
            this.currentScreen.keyTyped(key, scancode, action, mods);
        }

        if (action != GLFW.GLFW_PRESS)
            return;

        if (key == this.gameSettings.keyBindToggleFog.keyCode) {
            this.gameSettings.setOptionValue(
                    EnumOption.RENDER_DISTANCE,
                    !this.keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) && !this.keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT) ? 1 : -1
            );
        }
    }

    public void onMouseButton(long windowPointer, int button, int action, int mods) {
        if (this.currentScreen == null || this.currentScreen.inputable) {
            if (this.currentScreen == null) {
                if (!this.inGameHasFocus && action == GLFW.GLFW_PRESS) {
                    this.setIngameFocus();
                } else {
                    if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
                        this.clickMouse(GLFW.GLFW_MOUSE_BUTTON_LEFT);
                        this.mouseTicksRan = this.ticksRan;
                    }

                    if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
                        this.clickMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
                        this.mouseTicksRan = this.ticksRan;
                    }

                    if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW.GLFW_PRESS) {
                        this.clickMiddleMouseButton();
                    }
                }
            }
        }

        if (this.leftClickCounter > 0)
            --this.leftClickCounter;

        if (this.currentScreen == null) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && (float) (this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
                this.clickMouse(GLFW.GLFW_MOUSE_BUTTON_LEFT);
                this.mouseTicksRan = this.ticksRan;
            }

            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && (float) (this.ticksRan - this.mouseTicksRan) >= this.timer.ticksPerSecond / 4.0F && this.inGameHasFocus) {
                this.clickMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
                this.mouseTicksRan = this.ticksRan;
            }
        } else this.currentScreen.handleMouseInput(windowPointer, button, action, mods);
    }

    public void onScroll(long window, double xOffset, double yOffset) {
        if (this.currentScreen == null || this.currentScreen.inputable) {
            long diff = System.currentTimeMillis() - this.systemTime;
            if (diff <= 200L) {
                int offset = (int) yOffset;
                if (offset != 0) {
                    this.thePlayer.inventory.changeCurrentItem(offset);
                    if (this.gameSettings.noclip) {
                        if (offset > 0) {
                            offset = 1;
                        }

                        if (offset < 0) {
                            offset = -1;
                        }

                        this.gameSettings.noclipRate += (float) offset * 0.25F;
                    }
                }
            }
        }
    }

    public void onCrash(UnexpectedThrowable throwable) {
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


    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }

    public void displayGuiScreen(GuiScreen guiScreen) {
        if (this.currentScreen instanceof GuiUnused)
            return;

        if (this.currentScreen != null)
            this.currentScreen.onGuiClosed();

        if (guiScreen instanceof GuiMainMenu)
            this.statFileWriter.onExitOrWorldChange();

        this.statFileWriter.syncStats();
        if (guiScreen == null && this.theWorld == null) {
            guiScreen = new GuiMainMenu();
        } else if (guiScreen == null && this.thePlayer.health <= 0) {
            guiScreen = new GuiGameOver();
        }

        if (guiScreen instanceof GuiMainMenu)
            this.ingameGUI.clearChatMessages();

        this.currentScreen = guiScreen;
        if (guiScreen != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledResolution = new ScaledResolution(this.gameSettings, this.window.getWidth(), this.window.getHeight());
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();
            guiScreen.setWorldAndResolution(this, width, height);
            this.skipRenderWorld = false;
        } else this.setIngameFocus();
    }

    public void destroy() {
        try {
            this.statFileWriter.onExitOrWorldChange();
            this.statFileWriter.syncStats();

            try {
                if (this.clientLoadThread != null)
                    this.clientLoadThread.closeMinecraft();
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
        } finally {
            try {
                this.soundManager.dispose();
            } catch (Throwable ignored) {
            }

            window.destroy();
            if (!this.hasCrashed)
                System.exit(0);
        }

        System.gc();
    }

    @Override
    public void run() {
        this.running = true;

        try {
            this.startGame();
        } catch (Exception e) {
            e.printStackTrace();
            this.onCrash(new UnexpectedThrowable("Failed to start game", e));
            return;
        }

        try {
            while (this.running) {
                try {
                    update();
                } catch (MinecraftException e) {
                    this.theWorld = null;
                    this.changeWorld(null);
                    this.displayGuiScreen(new GuiConflictWarning());
                } catch (OutOfMemoryError e) {
                    this.dispose();
                    this.displayGuiScreen(new GuiErrorScreen());
                    System.gc();
                }
            }
        } catch (MinecraftError ignored) {
        } catch (Throwable throwable) {
            this.dispose();
            throwable.printStackTrace();
            this.onCrash(new UnexpectedThrowable("Unexpected error", throwable));
        } finally {
            this.destroy();
        }

    }

    private void update() throws InterruptedException {
        this.soundManager.tick();
        AxisAlignedBB.clearBoundingBoxPool();

        if (window.isCloseRequested()) {
            this.shutdown();
        }

        if (this.isGamePaused && this.theWorld != null) {
            float partialTicks = this.timer.renderPartialTicks;
            this.timer.updateTimer();
            this.timer.renderPartialTicks = partialTicks;
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
        this.soundManager.setListenerData(this.thePlayer, this.timer.renderPartialTicks);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (this.theWorld != null)
            this.theWorld.updatingLighting();

        if (!keyboard.isKeyDown(GLFW.GLFW_KEY_F7)) {
            window.update();
        }

        if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock())
            this.gameSettings.thirdPersonView = false;

        if (!this.skipRenderWorld) {
            if (this.playerController != null)
                this.playerController.setPartialTime(this.timer.renderPartialTicks);

            this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
        }

        GL11.glFlush();
        if (!this.window.isFocused()) {
            if (this.window.isFullscreen())
                this.toggleFullscreen();

            Thread.sleep(10L);
        }

        if (this.gameSettings.showDebugInfo) {
            this.displayDebugInfo(totalTick);
        } else {
            this.prevFrameTime = System.nanoTime();
        }

        this.guiAchievement.updateAchievementWindow();
        Thread.yield();

        if (keyboard.isKeyDown(GLFW.GLFW_KEY_F7)) {
            window.update();
        }

        // was input handle here
        this.window.setPhase("Post render");
        ++fpsCounter;

        for (this.isGamePaused = !this.isMultiplayerWorld()
                                 && this.currentScreen != null
                                 && this.currentScreen.doesGuiPauseGame(); System.currentTimeMillis() >= debugUpdateTime + 1000L; fpsCounter = 0) {
            this.debug = fpsCounter + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
            WorldRenderer.chunksUpdated = 0;
            debugUpdateTime += 1000L;
        }
    }

    public void dispose() {
        try {
            reserved = new byte[0];
            this.renderGlobal.dispose();
        } catch (Throwable ignored) {
        }

        try {
            System.gc();
            AxisAlignedBB.resetPool();
        } catch (Throwable ignored) {
        }

        try {
            System.gc();
            this.changeWorld(null);
        } catch (Throwable ignored) {
        }

        System.gc();
    }

    private void displayDebugInfo(long delta) {
        long var3 = 16666666L;
        if (this.prevFrameTime == -1L)
            this.prevFrameTime = System.nanoTime();

        long nanoTime = System.nanoTime();
        tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = delta;
        frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = nanoTime - this.prevFrameTime;
        this.prevFrameTime = nanoTime;

        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        int displayWidth = this.window.getWidth();
        int displayHeight = this.window.getHeight();

        GL11.glOrtho(0.0D, displayWidth, displayHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawing(GL11.GL_QUADS);
        int var8 = (int) (var3 / 200000L);
        tess.setColorOpaque_I(0x20000000);
        tess.addVertex(0.0D, displayHeight - var8, 0.0D);
        tess.addVertex(0.0D, displayHeight, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight - var8, 0.0D);
        tess.setColorOpaque_I(0x20200000);
        tess.addVertex(0.0D, displayHeight - var8 * 2, 0.0D);
        tess.addVertex(0.0D, displayHeight - var8, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight - var8, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight - var8 * 2, 0.0D);
        tess.draw();

        long totalTime = 0L;
        for (long frameTime : frameTimes)
            totalTime += frameTime;

        int var20 = (int) (totalTime / 200000L / (long) frameTimes.length);
        tess.startDrawing(GL11.GL_QUADS);
        tess.setColorOpaque_I(0x20400000);
        tess.addVertex(0.0D, displayHeight - var20, 0.0D);
        tess.addVertex(0.0D, displayHeight, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight, 0.0D);
        tess.addVertex(frameTimes.length, displayHeight - var20, 0.0D);
        tess.draw();
        tess.startDrawing(GL11.GL_LINES);

        for (int frame = 0; frame < frameTimes.length; ++frame) {
            int var13 = (frame - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
            int var14 = var13 * var13 / 255;
            var14 = var14 * var14 / 255;
            int var15 = var14 * var14 / 255;
            var15 = var15 * var15 / 255;

            if (frameTimes[frame] > var3)
                tess.setColorOpaque_I(-16777216 + var14 * 65536);
            else
                tess.setColorOpaque_I(-16777216 + var14 * 256);

            long var16 = frameTimes[frame] / 200_000L;
            long var18 = tickTimes[frame] / 200_000L;
            tess.addVertex((float) frame + 0.5F, (float) ((long) displayHeight - var16) + 0.5F, 0.0D);
            tess.addVertex((float) frame + 0.5F, (float) displayHeight + 0.5F, 0.0D);
            tess.setColorOpaque_I(-16777216 + var14 * 65536 + var14 * 256 + var14);
            tess.addVertex((float) frame + 0.5F, (float) ((long) displayHeight - var16) + 0.5F, 0.0D);
            tess.addVertex((float) frame + 0.5F, (float) ((long) displayHeight - (var16 - var18)) + 0.5F, 0.0D);
        }

        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void shutdown() {
        this.running = false;
    }

    public void setIngameFocus() {
        if (!this.window.isFocused() || this.inGameHasFocus)
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

        if (this.thePlayer != null)
            this.thePlayer.resetPlayerKeyState();

        this.inGameHasFocus = false;
        this.mouseHelper.ungrabMouseCursor();
        this.mouseHelper.setCursorInCenter();
    }

    public void displayInGameMenu() {
        if (this.currentScreen == null)
            this.displayGuiScreen(new GuiIngameMenu());
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

            if (!this.thePlayer.isSwinging)
                this.thePlayer.swingItem();

            this.playerController.sendBlockRemoving(x, y, z, this.objectMouseOver.sideHit);
            this.effectRenderer.addBlockHitEffects(x, y, z, this.objectMouseOver.sideHit);
        } else this.playerController.resetBlockRemoving();
    }

    /**
     * Calls on mouse click.
     *
     * @param buttonId - id of mouse button.
     *   0 = Left Click
     *   1 = Right Click
     */
    private void clickMouse(int buttonId) {
        if (buttonId == 0 && this.leftClickCounter > 0)
            return;

        if (buttonId == 0)
            this.thePlayer.swingItem();

        boolean notLeftClick = true;
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
            int x = this.objectMouseOver.blockX;
            int y = this.objectMouseOver.blockY;
            int z = this.objectMouseOver.blockZ;
            int sideHit = this.objectMouseOver.sideHit;
            if (buttonId == 0) {
                this.playerController.clickBlock(x, y, z, this.objectMouseOver.sideHit);
            } else {
                ItemStack currentItem = this.thePlayer.inventory.getCurrentItem();
                int stackSize = currentItem != null ? currentItem.stackSize : 0;
                if (this.playerController.sendPlaceBlock(this.thePlayer, this.theWorld, currentItem, x, y, z, sideHit)) {
                    notLeftClick = false;
                    this.thePlayer.swingItem();
                }

                if (currentItem == null)
                    return;

                if (currentItem.stackSize == 0) {
                    this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                } else if (currentItem.stackSize != stackSize) {
                    this.entityRenderer.itemRenderer.func_9449_b();
                }
            }
        }

        if (notLeftClick && buttonId == 1) {
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
            this.window.setResizable(fullscreen);
            this.window.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resize(long window, int width, int height) {
        if (this.currentScreen == null)
            return;

        ScaledResolution resolution = new ScaledResolution(this.gameSettings, width, height);
        this.currentScreen.setWorldAndResolution(this, resolution.getScaledWidth(), resolution.getScaledHeight());
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

    public void runTick() {
        this.statFileWriter.saveAndPush();
        this.ingameGUI.updateTick();
        this.entityRenderer.getMouseOver(1.0F);
        if (this.thePlayer != null) {
            IChunkProvider provider = this.theWorld.getIChunkProvider();
            if (provider instanceof ChunkProviderLoadOrGenerate currentChunkOver) {
                int chunkX = MathHelper.floor((float) ((int) this.thePlayer.posX)) >> 4;
                int chunkY = MathHelper.floor((float) ((int) this.thePlayer.posZ)) >> 4;
                currentChunkOver.setCurrentChunkOver(chunkX, chunkY);
            }
        }

        if (!this.isGamePaused && this.theWorld != null)
            this.playerController.updateController();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
        if (!this.isGamePaused)
            this.renderEngine.updateDynamicTextures();

        if (this.currentScreen == null && this.thePlayer != null) {
            if (this.thePlayer.health <= 0) {
                this.displayGuiScreen(null);
            } else if (this.thePlayer.isSleeping() && this.theWorld != null && this.theWorld.localWorld) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isSleeping()) {
            this.displayGuiScreen(null);
        }

        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
            this.mouseTicksRan = this.ticksRan + 10000;

            this.currentScreen.guiParticle.func_25088_a();
            this.currentScreen.updateScreen();
        }

        if (currentScreen == null || currentScreen.inputable) {
            this.handleBlockBreaking(
                    0,
                    this.currentScreen == null && mouse.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT) && this.inGameHasFocus
            );
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
            if (this.theWorld.localWorld)
                this.theWorld.difficultySetting = 3;

            if (!this.isGamePaused)
                this.entityRenderer.updateRenderer();

            if (!this.isGamePaused)
                this.renderGlobal.updateClouds();

            if (!this.isGamePaused) {
                if (this.theWorld.field1 > 0)
                    --this.theWorld.field1;

                this.theWorld.updateEntities();
            }

            if (!this.isGamePaused || this.isMultiplayerWorld()) {
                this.theWorld.setAllowedSpawnTypes(this.gameSettings.difficulty > 0, true);
                this.theWorld.tick();
            }

            if (!this.isGamePaused && this.theWorld != null && this.thePlayer != null) {
                this.theWorld.randomDisplayUpdates(
                        MathHelper.floor(this.thePlayer.posX),
                        MathHelper.floor(this.thePlayer.posY),
                        MathHelper.floor(this.thePlayer.posZ)
                );
            }

            if (!this.isGamePaused)
                this.effectRenderer.updateEffects();
        }

        this.systemTime = System.currentTimeMillis();
    }

    private void forceReload() {
        System.out.println("FORCING RELOAD!");
        this.soundManager = new SoundManager();
        this.soundManager.setSettings(this.gameSettings);
        this.clientLoadThread.reloadResources();
    }

    public boolean isMultiplayerWorld() {
        return this.theWorld != null && this.theWorld.localWorld;
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

        this.theWorld.removeEntity(this.thePlayer);
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

        this.thePlayer.world = this.theWorld;
        if (this.thePlayer.isEntityAlive()) {
            this.thePlayer.setLocationAndAngles(x, this.thePlayer.posY, z, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
            this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
            new Teleporter().setExitLocation(this.theWorld, this.thePlayer);
        }

    }

    public void changeWorld(@Nullable World world) {
        this.changeWorld(world, "");
    }

    public void changeWorld(@Nullable World world, String loadScreenText) {
        this.changeWorld(world, loadScreenText, null);
    }

    public void changeWorld(@Nullable World world, String loadScreenText, EntityPlayer player) {
        this.statFileWriter.onExitOrWorldChange();
        this.statFileWriter.syncStats();
        this.renderViewEntity = null;
        this.loadingScreen.printText(loadScreenText);
        this.loadingScreen.displayLoadingString("");
        this.soundManager.playStreaming(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        if (this.theWorld != null)
            this.theWorld.saveWorldIndirectly(this.loadingScreen);

        this.theWorld = world;
        if (world != null) {
            this.playerController.func_717_a(world);
            if (!this.isMultiplayerWorld()) {
                if (player == null) {
                    this.thePlayer = (EntityPlayerSP) world.queryEntity(EntityPlayerSP.class);
                }
            } else if (this.thePlayer != null) {
                this.thePlayer.preparePlayerToSpawn();
                world.entityJoinedWorld(this.thePlayer);
            }

            if (!world.localWorld)
                this.func_6255_d(loadScreenText);

            if (this.thePlayer == null) {
                this.thePlayer = (EntityPlayerSP) this.playerController.createPlayer(world);
                this.thePlayer.preparePlayerToSpawn();
                this.playerController.flipPlayer(this.thePlayer);
            }

            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            if (this.renderGlobal != null)
                this.renderGlobal.changeWorld(world);

            if (this.effectRenderer != null)
                this.effectRenderer.clearEffects(world);

            this.playerController.func_6473_b(this.thePlayer);
            if (player != null)
                world.emptyMethod1();

            IChunkProvider chunkProvider = world.getIChunkProvider();
            if (chunkProvider instanceof ChunkProviderLoadOrGenerate provider) {
                int x = MathHelper.floor((float) ((int) this.thePlayer.posX)) >> 4;
                int y = MathHelper.floor((float) ((int) this.thePlayer.posZ)) >> 4;
                provider.setCurrentChunkOver(x, y);
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
        short rad = 128;
        int progress = 0;
        int total = rad * 2 / 16 + 1;
        total = total * total;
        IChunkProvider chunkProvider = this.theWorld.getIChunkProvider();
        ChunkCoordinates chunkCoord = this.theWorld.getSpawnPoint();
        if (this.thePlayer != null) {
            chunkCoord.x = (int) this.thePlayer.posX;
            chunkCoord.z = (int) this.thePlayer.posZ;
        }

        if (chunkProvider instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate ch = (ChunkProviderLoadOrGenerate) chunkProvider;
            ch.setCurrentChunkOver(chunkCoord.x >> 4, chunkCoord.z >> 4);
        }

        for (int x = -rad; x <= rad; x += 16) {
            for (int z = -rad; z <= rad; z += 16) {
                this.loadingScreen.setLoadingProgress(progress++ / total * 100);
                this.theWorld.getBlockId(chunkCoord.x + x, 64, chunkCoord.z + z);

                while (this.theWorld.updatingLighting()) ;
            }
        }

        this.loadingScreen.displayLoadingString("Simulating world for a bit");
        this.theWorld.unloadOldChunks();
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

    public String getDebugInfoRenders() {
        return this.renderGlobal.getDebugInfoRenders();
    }

    public String getDebugInfoEntities() {
        return this.renderGlobal.getDebugInfoEntities();
    }

    public String func_21002_o() {
        return this.theWorld.chunkStatistic();
    }

    public String func_6245_o() {
        return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.entitiesStatistic();
    }

    public void respawn(boolean var1, int var2) {
        if (!this.theWorld.localWorld && !this.theWorld.worldProvider.canRespawnHere())
            this.usePortal();

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
        if (chunkProvider instanceof ChunkProviderLoadOrGenerate loadOrGenerate) {
            loadOrGenerate.setCurrentChunkOver(var4.x >> 4, var4.z >> 4);
        }

        this.theWorld.setSpawnLocation();
        this.theWorld.updateEntityList();
        int playerId = 0;
        if (this.thePlayer != null) {
            playerId = this.thePlayer.entityId;
            this.theWorld.removeEntity(this.thePlayer);
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
        this.thePlayer.playRespawnAnimation();
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

    public static MinecraftClient getInstance() {
        return instance;
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

    public static File getMinecraftDir() {
        if (minecraftDir == null)
            minecraftDir = new File("."); //TODO: set again EnumOS.getAppDir("minecraft") and make it customizable

        return minecraftDir;
    }

}
