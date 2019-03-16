package net.minecraft;

import co.aikar.timings.MinecraftTimings;
import co.aikar.timings.Timing;

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
    private Object sendQueueLock = new Object();
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

    public NetworkManager(Socket var1, String var2, NetHandler var3) throws IOException {
        this.networkSocket = var1;
        this.remoteSocketAddress = var1.getRemoteSocketAddress();
        this.netHandler = var3;

        try {
            var1.setSoTimeout(30000);
            var1.setTrafficClass(24);
        } catch (SocketException e) {
            System.err.println(e.getMessage());
        }

        this.socketInputStream = new DataInputStream(var1.getInputStream());
        this.socketOutputStream = new DataOutputStream(new BufferedOutputStream(var1.getOutputStream(), 5120));
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
    static DataOutputStream func_28136_f(NetworkManager var0) {
        return var0.socketOutputStream;
    }

    // $FF: synthetic method
    static boolean func_28135_e(NetworkManager var0) {
        return var0.isTerminating;
    }

    // $FF: synthetic method
    static void func_30007_a(NetworkManager var0, Exception e) {
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

    public void setNetHandler(NetHandler var1) {
        this.netHandler = var1;
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
        boolean var1 = false;

        try {
            if (!this.dataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - this.dataPackets.get(0).creationTimeMillis >= (long) this.chunkDataSendCounter)) {
                Packet var2;
                synchronized (this.sendQueueLock) {
                    var2 = this.dataPackets.remove(0);
                    this.sendQueueByteLength -= var2.getPacketSize() + 1;
                }

                Packet.writePacket(var2, this.socketOutputStream);
                int[] var10000 = field_28140_e;
                int var10001 = var2.getPacketId();
                var10000[var10001] += var2.getPacketSize() + 1;
                var1 = true;
            }

            if (this.field_20175_w-- <= 0 && !this.chunkDataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - this.chunkDataPackets.get(0).creationTimeMillis >= (long) this.chunkDataSendCounter)) {
                Packet var9;
                synchronized (this.sendQueueLock) {
                    var9 = this.chunkDataPackets.remove(0);
                    this.sendQueueByteLength -= var9.getPacketSize() + 1;
                }

                Packet.writePacket(var9, this.socketOutputStream);
                int[] var12 = field_28140_e;
                int var13 = var9.getPacketId();
                var12[var13] += var9.getPacketSize() + 1;
                this.field_20175_w = 0;
                var1 = true;
            }

            return var1;
        } catch (Exception e) {
            if (!this.isTerminating) {
                this.onNetworkError(e);
            }

            return false;
        }
    }

    public void func_28138_a() {
        this.readThread.interrupt();
        this.writeThread.interrupt();
    }

    private boolean readPacket() {
        boolean var1 = false;

        try {
            Packet var2 = Packet.readPacket(this.socketInputStream, this.netHandler.isServerHandler());
            if (var2 != null) {
                int[] var10000 = field_28141_d;
                int var10001 = var2.getPacketId();
                var10000[var10001] += var2.getPacketSize() + 1;
                this.readPackets.add(var2);
                var1 = true;
            } else {
                this.networkShutdown("disconnect.endOfStream");
            }

            return var1;
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

    public void networkShutdown(String var1, Object... var2) {
        if (this.isRunning) {
            this.isTerminating = true;
            this.terminationReason = var1;
            this.field_20176_t = var2;
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

        this.func_28138_a();
        if (this.isTerminating && this.readPackets.isEmpty()) {
            this.netHandler.handleErrorMessage(this.terminationReason, this.field_20176_t);
        }

    }

    public SocketAddress getRemoteAddress() {
        return this.remoteSocketAddress;
    }

    public void serverShutdown() {
        this.func_28138_a();
        this.isServerTerminating = true;
        this.readThread.interrupt();
        (new ThreadMonitorConnection(this)).start();
    }

    public int getNumChunkDataPackets() {
        return this.chunkDataPackets.size();
    }
}
