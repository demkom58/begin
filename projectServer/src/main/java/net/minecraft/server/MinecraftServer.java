package net.minecraft.server;

import net.minecraft.*;
import util.Vec3D;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinecraftServer implements Runnable, ICommandListener {
    public static final Logger LOGGER = Logger.getLogger("Minecraft");

    public static Map<String, Integer> toProcess = new HashMap<>();
    public NetworkListenThread networkServer;
    public PropertyManager propertyManagerObj;
    public WorldServer[] worldMngr;
    public ServerConfigurationManager configManager;
    public boolean serverStopped = false;
    public String currentTask;
    public int percentDone;
    public EntityTracker[] entityTracker = new EntityTracker[2];
    public boolean onlineMode;
    public boolean spawnPeacefulMobs;
    public boolean pvpOn;
    public boolean allowFlight;
    int deathTime = 0;
    private ConsoleCommandHandler commandHandler;
    private boolean serverRunning = true;
    private List<IUpdatePlayerListBox> updatePlayerListBoxes = new ArrayList<>();
    private List<ServerCommand> commands = Collections.synchronizedList(new ArrayList<>());

    public MinecraftServer() {
        new ThreadSleepForever(this);
    }

    public static void main(String[] args) {
        StatList.func_27092_a();

        try {
            MinecraftServer minecraftServer = new MinecraftServer();
            if (!GraphicsEnvironment.isHeadless() && (args.length <= 0 || !args[0].equals("nogui"))) {
                ServerGUI.initGui(minecraftServer);
            }

            (new ThreadServerApplication("Server thread", minecraftServer)).start();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start the minecraft server", e);
        }

    }

    // $FF: synthetic method
    public static boolean isServerRunning(MinecraftServer var0) {
        return var0.serverRunning;
    }

    private boolean startServer() throws UnknownHostException {
        this.commandHandler = new ConsoleCommandHandler(this);
        ThreadCommandReader var1 = new ThreadCommandReader(this);
        var1.setDaemon(true);
        var1.start();
        ConsoleLogManager.init();
        LOGGER.info("Starting minecraft server version Beta 1.7.3");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            LOGGER.warning("**** NOT ENOUGH RAM!");
            LOGGER.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        LOGGER.info("Loading properties");
        this.propertyManagerObj = new PropertyManager(new File("server.properties"));
        String var2 = this.propertyManagerObj.getStringProperty("server-ip", "");
        this.onlineMode = this.propertyManagerObj.getBooleanProperty("online-mode", true);
        this.spawnPeacefulMobs = this.propertyManagerObj.getBooleanProperty("spawn-animals", true);
        this.pvpOn = this.propertyManagerObj.getBooleanProperty("pvp", true);
        this.allowFlight = this.propertyManagerObj.getBooleanProperty("allow-flight", false);
        InetAddress var3 = null;
        if (var2.length() > 0) {
            var3 = InetAddress.getByName(var2);
        }

        int var4 = this.propertyManagerObj.getIntProperty("server-port", 25565);
        LOGGER.info("Starting Minecraft server on " + (var2.length() == 0 ? "*" : var2) + ":" + var4);

        try {
            this.networkServer = new NetworkListenThread(this, var3, var4);
        } catch (IOException e1) {
            LOGGER.warning("**** FAILED TO BIND TO PORT!");
            LOGGER.log(Level.WARNING, "The exception was: " + e1.toString());
            LOGGER.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.onlineMode) {
            LOGGER.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            LOGGER.warning("The server will make no attempt to authenticate usernames. Beware.");
            LOGGER.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            LOGGER.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
        }

        this.configManager = new ServerConfigurationManager(this);
        this.entityTracker[0] = new EntityTracker(this, 0);
        this.entityTracker[1] = new EntityTracker(this, -1);
        long var5 = System.nanoTime();
        String var7 = this.propertyManagerObj.getStringProperty("level-name", "world");
        String var8 = this.propertyManagerObj.getStringProperty("level-seed", "");
        long var9 = (new Random()).nextLong();
        if (var8.length() > 0) {
            try {
                var9 = Long.parseLong(var8);
            } catch (NumberFormatException e) {
                var9 = (long) var8.hashCode();
            }
        }

        LOGGER.info("Preparing level \"" + var7 + "\"");
        this.initWorld(new SaveConverterMcRegion(new File(".")), var7, var9);
        LOGGER.info("Done (" + (System.nanoTime() - var5) + "ns)! For help, type \"help\" or \"?\"");
        return true;
    }

    private void initWorld(ISaveFormat saveFormat, String type, long var3) {
        if (saveFormat.isOldSaveType(type)) {
            LOGGER.info("Converting map!");
            saveFormat.converMapToMCRegion(type, new ConvertProgressUpdater(this));
        }

        this.worldMngr = new WorldServer[2];
        SaveOldDir saveOldDir = new SaveOldDir(new File("."), type, true);

        for (int i = 0; i < this.worldMngr.length; ++i) {
            if (i == 0) {
                this.worldMngr[i] = new WorldServer(this, saveOldDir, type, i == 0 ? 0 : -1, var3);
            } else {
                this.worldMngr[i] = new WorldServerMulti(this, saveOldDir, type, i == 0 ? 0 : -1, var3, this.worldMngr[0]);
            }

            this.worldMngr[i].addWorldAccess(new WorldManager(this, this.worldMngr[i]));
            this.worldMngr[i].difficultySetting = this.propertyManagerObj.getBooleanProperty("spawn-monsters", true) ? 1 : 0;
            this.worldMngr[i].setAllowedSpawnTypes(this.propertyManagerObj.getBooleanProperty("spawn-monsters", true), this.spawnPeacefulMobs);
            this.configManager.setPlayerManager(this.worldMngr);
        }

        short var18 = 196;
        long var7 = System.currentTimeMillis();

        for (int var9 = 0; var9 < this.worldMngr.length; ++var9) {
            LOGGER.info("Preparing start region for level " + var9);
            if (var9 == 0 || this.propertyManagerObj.getBooleanProperty("allow-nether", true)) {
                WorldServer var10 = this.worldMngr[var9];
                ChunkCoordinates var11 = var10.getSpawnPoint();

                for (int var12 = -var18; var12 <= var18 && this.serverRunning; var12 += 16) {
                    for (int var13 = -var18; var13 <= var18 && this.serverRunning; var13 += 16) {
                        long var14 = System.currentTimeMillis();
                        if (var14 < var7) {
                            var7 = var14;
                        }

                        if (var14 > var7 + 1000L) {
                            int var16 = (var18 * 2 + 1) * (var18 * 2 + 1);
                            int var17 = (var12 + var18) * (var18 * 2 + 1) + var13 + 1;
                            this.outputPercentRemaining("Preparing spawn area", var17 * 100 / var16);
                            var7 = var14;
                        }

                        var10.chunkProviderServer.loadChunk(var11.posX + var12 >> 4, var11.posZ + var13 >> 4);

                        while (var10.func_6156_d() && this.serverRunning) {
                        }
                    }
                }
            }
        }

        this.clearCurrentTask();
    }

    private void outputPercentRemaining(String var1, int var2) {
        this.currentTask = var1;
        this.percentDone = var2;
        LOGGER.info(var1 + ": " + var2 + "%");
    }

    private void clearCurrentTask() {
        this.currentTask = null;
        this.percentDone = 0;
    }

    private void saveServerWorld() {
        LOGGER.info("Saving chunks");

        for (int var1 = 0; var1 < this.worldMngr.length; ++var1) {
            WorldServer var2 = this.worldMngr[var1];
            var2.saveWorld(true, null);
            var2.func_30006_w();
        }

    }

    private void stopServer() {
        LOGGER.info("Stopping server");
        if (this.configManager != null) {
            this.configManager.savePlayerStates();
        }

        for (int var1 = 0; var1 < this.worldMngr.length; ++var1) {
            WorldServer var2 = this.worldMngr[var1];
            if (var2 != null) {
                this.saveServerWorld();
            }
        }

    }

    public void initiateShutdown() {
        this.serverRunning = false;
    }

    public void run() {
        try {
            if (this.startServer()) {
                long lastTickStartMillis = System.currentTimeMillis();

                for (long left = 0L; this.serverRunning; Thread.sleep(1L)) {
                    long tickStartMillis = System.currentTimeMillis();
                    long lastTickTime = tickStartMillis - lastTickStartMillis;
                    if (lastTickTime > 2000L) {
                        LOGGER.warning("Can't keep up! Did the system time change, or is the server overloaded?");
                        lastTickTime = 2000L;
                    }

                    if (lastTickTime < 0L) {
                        LOGGER.warning("Time ran backwards! Did the system time change?");
                        lastTickTime = 0L;
                    }

                    left += lastTickTime;
                    lastTickStartMillis = tickStartMillis;
                    if (this.worldMngr[0].isAllPlayersFullyAsleep()) {
                        this.doTick();
                        left = 0L;
                    } else {
                        while (left > 50L) {
                            left -= 50L;
                            this.doTick();
                        }
                    }
                }
            } else {
                while (this.serverRunning) {
                    this.commandLineParser();

                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            LOGGER.log(Level.SEVERE, "Unexpected exception", throwable);

            while (this.serverRunning) {
                this.commandLineParser();

                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                this.stopServer();
                this.serverStopped = true;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                System.exit(0);
            }

        }

    }

    private void doTick() {
        List<String> shouldRemoved = new ArrayList<>();

        for (String key : toProcess.keySet()) {
            int left = toProcess.get(key);
            if (left > 0) {
                toProcess.put(key, left - 1);
            } else {
                shouldRemoved.add(key);
            }
        }

        for (int i = 0; i < shouldRemoved.size(); ++i) {
            toProcess.remove(shouldRemoved.get(i));
        }

        AxisAlignedBB.clearBoundingBoxPool();
        Vec3D.initialize();
        ++this.deathTime;

        for (int i = 0; i < this.worldMngr.length; ++i) {
            if (i == 0 || this.propertyManagerObj.getBooleanProperty("allow-nether", true)) {
                WorldServer worldServer = this.worldMngr[i];
                if (this.deathTime % 20 == 0) {
                    this.configManager.sendPacketToAllPlayersInDimension(new Packet4UpdateTime(worldServer.getWorldTime()), worldServer.worldProvider.worldType);
                }

                worldServer.tick();
                while (worldServer.func_6156_d()) {
                }

                worldServer.updateEntities();
            }
        }

        this.networkServer.handleNetworkListenThread();
        this.configManager.onTick();

        for (int i = 0; i < this.entityTracker.length; ++i) {
            this.entityTracker[i].updateTrackedEntities();
        }

        for (int i = 0; i < this.updatePlayerListBoxes.size(); ++i) {
            this.updatePlayerListBoxes.get(i).update();
        }

        try {
            this.commandLineParser();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unexpected exception while parsing console command", e);
        }

    }

    public void addCommand(String name, ICommandListener listener) {
        this.commands.add(new ServerCommand(name, listener));
    }

    public void commandLineParser() {
        while (this.commands.size() > 0) {
            ServerCommand var1 = this.commands.remove(0);
            this.commandHandler.handleCommand(var1);
        }

    }

    public void func_6022_a(IUpdatePlayerListBox var1) {
        this.updatePlayerListBoxes.add(var1);
    }

    public File getFile(String var1) {
        return new File(var1);
    }

    public void log(String var1) {
        LOGGER.info(var1);
    }

    public void logWarning(String var1) {
        LOGGER.warning(var1);
    }

    public String getUsername() {
        return "CONSOLE";
    }

    public WorldServer getWorldManager(int var1) {
        return var1 == -1 ? this.worldMngr[1] : this.worldMngr[0];
    }

    public EntityTracker getEntityTracker(int var1) {
        return var1 == -1 ? this.entityTracker[1] : this.entityTracker[0];
    }
}
