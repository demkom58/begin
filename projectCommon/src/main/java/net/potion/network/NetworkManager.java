package net.potion.network;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.network.packet.Packet;

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
    public static int[] packetSizes = new int[256];
    public static int[] packetSizes2 = new int[256];
    private final SocketAddress remoteSocketAddress;
    public int chunkDataSendCounter = 0;
    private final Object sendQueueLock = new Object();
    private Socket networkSocket;
    private DataInputStream socketInputStream;
    private DataOutputStream socketOutputStream;
    private boolean isRunning = true;
    private final List<Packet> readPackets = Collections.synchronizedList(new ArrayList<>());
    private final List<Packet> dataPackets = Collections.synchronizedList(new ArrayList<>());
    private final List<Packet> chunkDataPackets = Collections.synchronizedList(new ArrayList<>());
    private NetHandler netHandler;
    private boolean isServerTerminating = false;
    private final Thread writeThread;
    private final Thread readThread;
    private boolean isTerminating = false;
    private String terminationReason = "";
    private Object[] args;
    private int timeSinceLastRead = 0;
    private int sendQueueByteLength = 0;
    private int field1 = 50;

    public NetworkManager(Socket networkSocket, String var2, NetHandler netHandler) throws IOException {
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
        this.readThread = new NetworkReaderThread(this, var2 + " read thread");
        this.writeThread = new NetworkWriterThread(this, var2 + " write thread");
        this.readThread.start();
        this.writeThread.start();
    }

    // $FF: synthetic method
    static boolean isRunning(NetworkManager var0) {
        return var0.isRunning;
    }

    // $FF: synthetic method
    static boolean isServerTerminating(NetworkManager var0) {
        return var0.isServerTerminating;
    }

    // $FF: synthetic method
    static boolean readNetworkPacket(NetworkManager var0) {
        return var0.readPacket();
    }

    // $FF: synthetic method
    static boolean sendNetworkPacket(NetworkManager var0) {
        return var0.sendPacket();
    }

    // $FF: synthetic method
    static DataOutputStream getOutputStream(NetworkManager var0) {
        return var0.socketOutputStream;
    }

    // $FF: synthetic method
    static boolean isTerminating(NetworkManager var0) {
        return var0.isTerminating;
    }

    // $FF: synthetic method
    static void networkError(NetworkManager var0, Exception e) {
        var0.onNetworkError(e);
    }

    // $FF: synthetic method
    static Thread getReadThread(NetworkManager var0) {
        return var0.readThread;
    }

    // $FF: synthetic method
    static Thread getWriteThread(NetworkManager var0) {
        return var0.writeThread;
    }

    public void addToSendQueue(Packet var1) {
        if (!this.isServerTerminating) {
            synchronized (this.sendQueueLock) {
                this.sendQueueByteLength += var1.getPacketSize() + 1;
                if (var1.isChunkDataPacket) {
                    this.chunkDataPackets.add(var1);
                } else {
                    this.dataPackets.add(var1);
                }

            }
        }
    }

    private boolean sendPacket() {
        boolean done = false;

        try {
            if (!this.dataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - this.dataPackets.get(0).creationTimeMillis >= (long) this.chunkDataSendCounter)) {
                Packet packet;
                synchronized (this.sendQueueLock) {
                    packet = this.dataPackets.remove(0);
                    this.sendQueueByteLength -= packet.getPacketSize() + 1;
                }

                Packet.writePacket(packet, this.socketOutputStream);
                int[] sizes2 = packetSizes2;
                int packetId = packet.getPacketId();
                sizes2[packetId] += packet.getPacketSize() + 1;
                done = true;
            }

            if (this.field1-- <= 0
                    && !this.chunkDataPackets.isEmpty()
                    && (this.chunkDataSendCounter == 0
                        || System.currentTimeMillis() - this.chunkDataPackets.get(0).creationTimeMillis >= (long) this.chunkDataSendCounter)) {
                Packet packet;
                synchronized (this.sendQueueLock) {
                    packet = this.chunkDataPackets.remove(0);
                    this.sendQueueByteLength -= packet.getPacketSize() + 1;
                }

                Packet.writePacket(packet, this.socketOutputStream);
                int[] sizes2 = packetSizes2;
                int packetId = packet.getPacketId();
                sizes2[packetId] += packet.getPacketSize() + 1;
                this.field1 = 0;
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
                int[] sizes = packetSizes;
                int packetId = packet.getPacketId();
                sizes[packetId] += packet.getPacketSize() + 1;
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
        this.networkShutdown("disconnect.genericReason", "Internal exception: " + e);
    }

    public void networkShutdown(String reason, Object... args) {
        if (this.isRunning) {
            this.isTerminating = true;
            this.terminationReason = reason;
            this.args = args;
            (new NetworkMasterThread(this)).start();
            this.isRunning = false;

            try {
                this.socketInputStream.close();
                this.socketInputStream = null;
            } catch (Throwable ignored) {
            }

            try {
                this.socketOutputStream.close();
                this.socketOutputStream = null;
            } catch (Throwable ignored) {
            }

            try {
                this.networkSocket.close();
                this.networkSocket = null;
            } catch (Throwable ignored) {
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
            packet.processPacket(this.netHandler);
        }

        this.interrupt();
        if (this.isTerminating && this.readPackets.isEmpty()) {
            this.netHandler.handleErrorMessage(this.terminationReason, this.args);
        }

    }

    @Side(CodeSide.SERVER)
    public SocketAddress getRemoteAddress() {
        return this.remoteSocketAddress;
    }

    public void serverShutdown() {
        this.interrupt();
        this.isServerTerminating = true;
        this.readThread.interrupt();
        (new ThreadCloseConnection(this)).start();
    }

    public int getNumChunkDataPackets() {
        return this.chunkDataPackets.size();
    }

    public void setNetHandler(NetHandler netHandler) {
        this.netHandler = netHandler;
    }
}
