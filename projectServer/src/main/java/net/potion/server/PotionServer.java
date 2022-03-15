package net.potion.server;

import com.demkom58.util.RollingAverage;
import net.potion.entity.EntityTracker;
import net.potion.entity.player.IUpdatePlayerListBox;
import net.potion.network.NetworkListenThread;
import net.potion.network.packet.Packet4UpdateTime;
import net.potion.server.gui.ServerGUI;
import net.potion.stats.StatList;
import net.potion.util.AxisAlignedBB;
import net.potion.util.ConvertProgressUpdater;
import net.potion.util.ThreadSleepForever;
import net.potion.world.WorldManager;
import net.potion.world.WorldServer;
import net.potion.world.WorldServerMulti;
import net.potion.world.chunk.ChunkCoordinates;
import net.potion.world.storage.ISaveFormat;
import net.potion.world.storage.SaveConverterRegion;
import net.potion.world.storage.SaveOldDir;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PotionServer implements Runnable, ICommandListener {
    public static final Logger LOGGER = Logger.getLogger("Potion");
    public static PotionServer SERVER;

    private Thread primaryThread;
    public String serverIp;
    public int port;
    public InetAddress inetAddress;
    public NetworkListenThread networkServer;
    public PropertyManager propertyManagerObj;
    public WorldServer[] worldServers;
    public ServerConfigurationManager configManager;
    public boolean serverStopped = false;
    public String currentTask;
    public int percentDone;
    public EntityTracker[] entityTracker = new EntityTracker[2];
    public boolean onlineMode;
    public boolean allowNether;
    public boolean spawnPeacefulMobs;
    public boolean pvpOn;
    public boolean allowFlight;
    int deathTime = 0;
    private ConsoleCommandHandler commandHandler;
    private boolean serverRunning = true;
    private List<IUpdatePlayerListBox> updatePlayerListBoxes = new ArrayList<>();
    private List<ServerCommand> commands = Collections.synchronizedList(new ArrayList<>());

    /**
     * Tick variables and constants.
     */
    public static final int TPS = 20;
    public static final long SEC_IN_NANO = 1000000000;
    public static final long TICK_TIME = SEC_IN_NANO / TPS;
    public static final long MAX_CATCHUP_BUFFER = TICK_TIME * TPS * 60L;
    public static final int SAMPLE_INTERVAL = 20;
    public static int currentTick = 0;
    public final RollingAverage tps1 = new RollingAverage(60);
    public final RollingAverage tps5 = new RollingAverage(60 * 5);
    public final RollingAverage tps15 = new RollingAverage(60 * 15);

    public PotionServer() {
        PotionServer.SERVER = this;
        new ThreadSleepForever();
    }

    public static void main(String[] args) {
        StatList.init();

        try {
            PotionServer server = new PotionServer();
            if (!GraphicsEnvironment.isHeadless() && (args.length <= 0 || !args[0].equals("nogui"))) {
                ServerGUI.initGui(server);
            }

            new ThreadServerApplication("Server thread", server).start();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start the potion server", e);
        }

    }

    public static boolean isServerRunning(PotionServer server) {
        return server.serverRunning;
    }

    private boolean init() throws UnknownHostException {
        this.primaryThread = Thread.currentThread();
        this.commandHandler = new ConsoleCommandHandler(this);
        ThreadCommandReader threadCommandReader = new ThreadCommandReader(this);
        threadCommandReader.setDaemon(true);
        threadCommandReader.start();

        ConsoleLogManager.init();
        LOGGER.info("Starting potion server version in-dev 0.0.1");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            LOGGER.warning("**** NOT ENOUGH RAM!");
            LOGGER.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar potion_server.jar\"");
        }

        LOGGER.info("Loading properties");
        this.propertyManagerObj = new PropertyManager(new File("server.properties"));
        this.onlineMode = this.propertyManagerObj.getBooleanProperty("online-mode", true);
        this.allowNether = this.propertyManagerObj.getBooleanProperty("allow-nether", true);
        this.spawnPeacefulMobs = this.propertyManagerObj.getBooleanProperty("spawn-animals", true);
        this.pvpOn = this.propertyManagerObj.getBooleanProperty("pvp", true);
        this.allowFlight = this.propertyManagerObj.getBooleanProperty("allow-flight", false);

        serverIp = this.propertyManagerObj.getStringProperty("server-ip", "");
        port = this.propertyManagerObj.getIntProperty("server-port", 25565);
        if (serverIp.length() > 0) {
            inetAddress = InetAddress.getByName(serverIp);
        }

        LOGGER.info("Starting Potion server on " + (serverIp.length() == 0 ? "*" : serverIp) + ":" + port);

        try {
            this.networkServer = new NetworkListenThread(this, inetAddress, port);
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
        final long serverLoadStartStamp = System.currentTimeMillis();

        String levelName = this.propertyManagerObj.getStringProperty("level-name", "world");
        String levelSeed = this.propertyManagerObj.getStringProperty("level-seed", "");

        long seed = new Random().nextLong();
        if (levelSeed.length() > 0) {
            try {
                seed = Long.parseLong(levelSeed);
            } catch (NumberFormatException e) {
                seed = levelSeed.hashCode();
            }
        }

        LOGGER.info("Preparing level \"" + levelName + "\"");
        this.initWorld(new SaveConverterRegion(new File(".")), levelName, seed);
        LOGGER.info("Done (" + (System.currentTimeMillis() - serverLoadStartStamp) + "ms)! For help, type \"help\" or \"?\"");
        return true;
    }

    private void initWorld(ISaveFormat saveFormat, String type, long seed) {
        if (saveFormat.isOldMapFormat(type)) {
            LOGGER.info("Converting map!");
            saveFormat.convertMapFormat(type, new ConvertProgressUpdater(this));
        }

        this.worldServers = new WorldServer[2];
        SaveOldDir saveOldDir = new SaveOldDir(new File("."), type, true);

        for (int i = 0; i < this.worldServers.length; ++i) {
            if (i == 0) {
                this.worldServers[i] = new WorldServer(this, saveOldDir, type, i == 0 ? 0 : -1, seed);
            } else {
                this.worldServers[i] = new WorldServerMulti(this, saveOldDir, type, i == 0 ? 0 : -1, seed, this.worldServers[0]);
            }

            this.worldServers[i].addWorldAccess(new WorldManager(this, this.worldServers[i]));
            this.worldServers[i].difficultySetting = this.propertyManagerObj.getBooleanProperty("spawn-monsters", true) ? 1 : 0;
            this.worldServers[i].setAllowedSpawnTypes(this.propertyManagerObj.getBooleanProperty("spawn-monsters", true), this.spawnPeacefulMobs);
            this.configManager.setPlayerManager(this.worldServers);
        }

        short loadRange = 196;
        long preparingStart = System.currentTimeMillis();
        for (int i = 0; i < this.worldServers.length; ++i) {
            LOGGER.info("Preparing start region for level " + i);
            if (i == 0 || this.propertyManagerObj.getBooleanProperty("allow-nether", true)) {
                WorldServer worldServer = this.worldServers[i];
                ChunkCoordinates chunkCoordinates = worldServer.getSpawnPoint();

                for (int j = -loadRange; j <= loadRange && this.serverRunning; j += 16) {
                    for (int k = -loadRange; k <= loadRange && this.serverRunning; k += 16) {
                        long var14 = System.currentTimeMillis();
                        if (var14 < preparingStart) {
                            preparingStart = var14;
                        }

                        if (var14 > preparingStart + 1000L) {
                            int var16 = (loadRange * 2 + 1) * (loadRange * 2 + 1);
                            int var17 = (j + loadRange) * (loadRange * 2 + 1) + k + 1;
                            this.outputPercentRemaining("Preparing spawn area", var17 * 100 / var16);
                            preparingStart = var14;
                        }

                        worldServer.chunkProviderServer.prepareChunk(chunkCoordinates.x + j >> 4, chunkCoordinates.z + k >> 4);

                        while (worldServer.updatingLighting() && this.serverRunning) {
                        }
                    }
                }
            }
        }

        this.clearCurrentTask();
    }

    private void outputPercentRemaining(String currentTask, int percentDone) {
        this.currentTask = currentTask;
        this.percentDone = percentDone;
        LOGGER.info(currentTask + ": " + percentDone + "%");
    }

    private void clearCurrentTask() {
        this.currentTask = null;
        this.percentDone = 0;
    }

    private void saveServerWorld() {
        LOGGER.info("Saving chunks");

        for (int i = 0; i < this.worldServers.length; ++i) {
            WorldServer worldServer = this.worldServers[i];
            worldServer.saveWorld(true, null);
            worldServer.clearCache();
        }

    }

    private void stopServer() {
        LOGGER.info("Stopping server");
        if (this.configManager != null) {
            this.configManager.savePlayerStates();
        }

        for (int i = 0; i < this.worldServers.length; ++i) {
            WorldServer worldServer = this.worldServers[i];
            if (worldServer != null) {
                this.saveServerWorld();
            }
        }

    }

    public void initiateShutdown() {
        this.serverRunning = false;
    }

    @Override
    public void run() {
        try {
            if (this.init()) {
                long start = System.nanoTime(), lastTick = start - TICK_TIME, catchupTime =0, curTime, wait, tickSection = start;

                while (this.serverRunning) {
                    curTime = System.nanoTime();
                    wait = TICK_TIME - (curTime - lastTick);

                    if (wait > 0) {
                        if (catchupTime < 2E6) {
                            wait += Math.abs(catchupTime);
                        } else if (wait < catchupTime) {
                            catchupTime -= wait;
                            wait = 0;
                        } else {
                            wait -= catchupTime;
                            catchupTime = 0;
                        }
                    }

                    if (wait > 0) {
                        Thread.sleep(wait / 1_000_000);
                        curTime = System.nanoTime();
                        wait = TICK_TIME - (curTime - lastTick);
                    }

                    catchupTime = Math.min(MAX_CATCHUP_BUFFER, catchupTime - wait);
                    if (++PotionServer.currentTick % SAMPLE_INTERVAL == 0) {
                        final long diff = curTime - tickSection;
                        double currentTps = 1E9 / diff * SAMPLE_INTERVAL;
                        tps1.add(currentTps, diff);
                        tps5.add(currentTps, diff);
                        tps15.add(currentTps, diff);

                        tickSection = curTime;
                    }
                    lastTick = curTime;
                    doTick();
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
        AxisAlignedBB.clearBoundingBoxPool();
        ++this.deathTime;

        for (int i = 0; i < this.worldServers.length; ++i) {
            if (i == 0 || allowNether) {
                final WorldServer worldServer = this.worldServers[i];

                if (this.deathTime % 20 == 0) {
                    Packet4UpdateTime packet = new Packet4UpdateTime(worldServer.getWorldTime());
                    this.configManager.sendPacketToAllPlayersInDimension(packet, worldServer.worldProvider.worldType);
                }

                worldServer.tick();
                while (worldServer.updatingLighting()) { }
                worldServer.updateEntities();
            }
        }

        this.networkServer.handleNetworkListenThread();

        this.configManager.onTick();

        for (int i = 0; i < this.entityTracker.length; ++i)
            this.entityTracker[i].updateTrackedEntities();

        for (int i = 0; i < this.updatePlayerListBoxes.size(); ++i)
            this.updatePlayerListBoxes.get(i).update();

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
            ServerCommand serverCommand = this.commands.remove(0);
            this.commandHandler.handleCommand(serverCommand);
        }

    }

    public void addPlayerListBox(IUpdatePlayerListBox listBox) {
        this.updatePlayerListBoxes.add(listBox);
    }

    public File getFile(String file) {
        return new File(file);
    }

    @Override
    public void log(String message) {
        LOGGER.info(message);
    }

    public void logWarning(String message) {
        LOGGER.warning(message);
    }

    @Override
    public String getUsername() {
        return "CONSOLE";
    }

    public WorldServer getWorldServer(int world) {
        return world == -1 ? this.worldServers[1] : this.worldServers[0];
    }

    public EntityTracker getEntityTracker(int var1) {
        return var1 == -1 ? this.entityTracker[1] : this.entityTracker[0];
    }

    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(primaryThread);
    }

}
