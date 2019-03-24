package net.minecraft.network;

import com.demkom58.timings.MinecraftTimings;
import com.demkom58.timings.Timing;
import net.minecraft.network.packet.Packet;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkManager {
    public static final Object threadSyncObject = new Object();
    public static int numReadThreads;
    public static int numWriteThreads;
    public static int[] field_28141_d = new int[256];
    public static int[] field_28140_e = new int[256];
    private final SocketAddress remoteSocketAddress;
    public int chunkDataSendCounter = 0;
    private final Object sendQueueLock = new Object();
    private Socket networkSocket;
    private DataInputStream socketInputStream;
    private DataOutputStream socketOutputStream;
    private boolean isRunning = true;
    private List<Packet> readPackets = Collections.synchronizedList(new ArrayList<>());
    private List<Packet> dataPackets = Collections.synchronizedList(new ArrayList<>());
    private List<Packet> chunkDataPackets = Collections.synchronizedList(new ArrayList<>());
    private NetHandler netHandler;
    private boolean isServerTerminating = false;
    private Thread writeThread;
    private Thread readThread;
    private boolean isTerminating = false;
    private String terminationReason = "";
    private Object[] field_20176_t;
    private int timeSinceLastRead = 0;
    private int sendQueueByteLength = 0;
    private int field_20175_w = 50;

    public NetworkManager(Socket networkSocket, String name, NetHandler netHandler) throws IOException {
        this.networkSocket = networkSocket;
        this.remoteSocketAddress = networkSocket.getRemoteSocketAddress();
        this.netHandler = netHandler;

        try {
            networkSocket.setSoTimeout(30000);
            networkSocket.setTrafficClass(24);
        } catch (SocketException e) {
            System.err.println(e.getMessage());
        }

        this.socketInputStream = new DataInputStream(networkSocket.getInputStream());
        this.socketOutputStream = new DataOutputStream(new BufferedOutputStream(networkSocket.getOutputStream(), 5120));
        this.readThread = new NetworkReaderThread(this, name + " read thread");
        this.writeThread = new NetworkWriterThread(this, name + " write thread");
        this.readThread.start();
        this.writeThread.start();
    }

    // $FF: synthetic method
    static boolean isRunning(NetworkManager networkManager) {
        return networkManager.isRunning;
    }

    // $FF: synthetic method
    static boolean isServerTerminating(NetworkManager networkManager) {
        return networkManager.isServerTerminating;
    }

    // $FF: synthetic method
    static boolean readNetworkPacket(NetworkManager networkManager) {
        return networkManager.readPacket();
    }

    // $FF: synthetic method
    static boolean sendNetworkPacket(NetworkManager networkManager) {
        return networkManager.sendPacket();
    }

    // $FF: synthetic method
    static DataOutputStream getOutputStream(NetworkManager networkManager) {
        return networkManager.socketOutputStream;
    }

    // $FF: synthetic method
    static boolean isTerminating(NetworkManager networkManager) {
        return networkManager.isTerminating;
    }

    // $FF: synthetic method
    static void networkError(NetworkManager networkManager, Exception e) {
        networkManager.onNetworkError(e);
    }

    // $FF: synthetic method
    static Thread getReadThread(NetworkManager networkManager) {
        return networkManager.readThread;
    }

    // $FF: synthetic method
    static Thread getWriteThread(NetworkManager networkManager) {
        return networkManager.writeThread;
    }

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }

    public void addToSendQueue(Packet packet) {
        if (!this.isServerTerminating) {
            synchronized (this.sendQueueLock) {
                this.sendQueueByteLength += packet.getPacketSize() + 1;
                if (packet.isChunkDataPacket) {
                    this.chunkDataPackets.add(packet);
                } else {
                    this.dataPackets.add(packet);
                }

            }
        }
    }

    private boolean sendPacket() {
        boolean done = false;

        try {
            if (!this.dataPackets.isEmpty() && (this.chunkDataSendCounter == 0
                    || System.currentTimeMillis() - this.dataPackets.get(0).creationTimeMillis >= (long) this.chunkDataSendCounter)) {
                Packet packet;
                synchronized (this.sendQueueLock) {
                    packet = this.dataPackets.remove(0);
                    this.sendQueueByteLength -= packet.getPacketSize() + 1;
                }

                Packet.writePacket(packet, this.socketOutputStream);
                int[] space = field_28140_e;
                int packetId = packet.getPacketId();
                space[packetId] += packet.getPacketSize() + 1;
                done = true;
            }

            if (this.field_20175_w-- <= 0 && !this.chunkDataPackets.isEmpty()
                    && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - this.chunkDataPackets.get(0).creationTimeMillis >= (long) this.chunkDataSendCounter)) {

                Packet packet;
                synchronized (this.sendQueueLock) {
                    packet = this.chunkDataPackets.remove(0);
                    this.sendQueueByteLength -= packet.getPacketSize() + 1;
                }

                Packet.writePacket(packet, this.socketOutputStream);
                int[] space = field_28140_e;
                int packetId = packet.getPacketId();
                space[packetId] += packet.getPacketSize() + 1;
                this.field_20175_w = 0;
                done = true;
            }

            return done;
        } catch (Exception e) {
            if (!this.isTerminating) {
                this.onNetworkError(e);
            }

            return false;
        }
    }

    public void interrupt() {
        this.readThread.interrupt();
        this.writeThread.interrupt();
    }

    private boolean readPacket() {
        boolean done = false;

        try {
            Packet packet = Packet.readPacket(this.socketInputStream, this.netHandler.isServerHandler());
            if (packet != null) {
                int[] space = field_28141_d;
                int packetId = packet.getPacketId();
                space[packetId] += packet.getPacketSize() + 1;
                this.readPackets.add(packet);
                done = true;
            } else {
                this.networkShutdown("disconnect.endOfStream");
            }

            return done;
        } catch (Exception e) {
            if (!this.isTerminating) {
                this.onNetworkError(e);
            }

            return false;
        }
    }

    private void onNetworkError(Exception e) {
        e.printStackTrace();
        this.networkShutdown("disconnect.genericReason", "Internal exception: " + e.toString());
    }

    public void networkShutdown(String reason, Object... additional) {
        if (this.isRunning) {
            this.isTerminating = true;
            this.terminationReason = reason;
            this.field_20176_t = additional;
            (new NetworkMasterThread(this)).start();
            this.isRunning = false;

            try {
                this.socketInputStream.close();
                this.socketInputStream = null;
            } catch (Throwable throwable) {
            }

            try {
                this.socketOutputStream.close();
                this.socketOutputStream = null;
            } catch (Throwable throwable) {
            }

            try {
                this.networkSocket.close();
                this.networkSocket = null;
            } catch (Throwable throwable) {
            }

        }
    }

    public void processReadPackets() {
        if (this.sendQueueByteLength > 1048576) {
            this.networkShutdown("disconnect.overflow");
        }

        if (this.readPackets.isEmpty()) {
            if (this.timeSinceLastRead++ == 1200) {
                this.networkShutdown("disconnect.timeout");
            }
        } else {
            this.timeSinceLastRead = 0;
        }

        int var1 = 100;

        while (!this.readPackets.isEmpty() && var1-- >= 0) {
            Packet packet = this.readPackets.remove(0);
            try (Timing timing = MinecraftTimings.getPacketTiming(packet).startTiming()) {
                packet.processPacket(this.netHandler);
            }

        }

        this.interrupt();
        if (this.isTerminating && this.readPackets.isEmpty()) {
            this.netHandler.handleErrorMessage(this.terminationReason, this.field_20176_t);
        }

    }

    public SocketAddress getRemoteAddress() {
        return this.remoteSocketAddress;
    }

    public void serverShutdown() {
        this.interrupt();
        this.isServerTerminating = true;
        this.readThread.interrupt();
        (new ThreadMonitorConnection(this)).start();
    }

    public int getNumChunkDataPackets() {
        return this.chunkDataPackets.size();
    }

}
