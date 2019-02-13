package net.minecraft;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Packet {
    private static Int2ObjectMap<Class<? extends Packet>> packetIdToClassMap = new Int2ObjectRBTreeMap<>();
    private static Object2IntMap<Class<? extends Packet>> packetClassToIdMap = new Object2IntArrayMap<>();
    private static IntSet clientPacketIdList = new IntArraySet();
    private static IntSet serverPacketIdList = new IntArraySet();
    private static Int2ObjectMap<PacketCounter> packetStats = new Int2ObjectRBTreeMap<>();
    private static int totalPacketsCount = 0;

    static {
        addIdClassMapping(0, true, true, Packet0KeepAlive.class);
        addIdClassMapping(1, true, true, Packet1Login.class);
        addIdClassMapping(2, true, true, Packet2Handshake.class);
        addIdClassMapping(3, true, true, Packet3Chat.class);
        addIdClassMapping(4, true, false, Packet4UpdateTime.class);
        addIdClassMapping(5, true, false, Packet5PlayerInventory.class);
        addIdClassMapping(6, true, false, Packet6SpawnPosition.class);
        addIdClassMapping(7, false, true, Packet7UseEntity.class);
        addIdClassMapping(8, true, false, Packet8UpdateHealth.class);
        addIdClassMapping(9, true, true, Packet9Respawn.class);
        addIdClassMapping(10, true, true, Packet10Flying.class);
        addIdClassMapping(11, true, true, Packet11PlayerPosition.class);
        addIdClassMapping(12, true, true, Packet12PlayerLook.class);
        addIdClassMapping(13, true, true, Packet13PlayerLookMove.class);
        addIdClassMapping(14, false, true, Packet14BlockDig.class);
        addIdClassMapping(15, false, true, Packet15Place.class);
        addIdClassMapping(16, false, true, Packet16BlockItemSwitch.class);
        addIdClassMapping(17, true, false, Packet17Sleep.class);
        addIdClassMapping(18, true, true, Packet18Animation.class);
        addIdClassMapping(19, false, true, Packet19EntityAction.class);
        addIdClassMapping(20, true, false, Packet20NamedEntitySpawn.class);
        addIdClassMapping(21, true, false, Packet21PickupSpawn.class);
        addIdClassMapping(22, true, false, Packet22Collect.class);
        addIdClassMapping(23, true, false, Packet23VehicleSpawn.class);
        addIdClassMapping(24, true, false, Packet24MobSpawn.class);
        addIdClassMapping(25, true, false, Packet25EntityPainting.class);
        addIdClassMapping(27, false, true, Packet27Position.class);
        addIdClassMapping(28, true, false, Packet28EntityVelocity.class);
        addIdClassMapping(29, true, false, Packet29DestroyEntity.class);
        addIdClassMapping(30, true, false, Packet30Entity.class);
        addIdClassMapping(31, true, false, Packet31RelEntityMove.class);
        addIdClassMapping(32, true, false, Packet32EntityLook.class);
        addIdClassMapping(33, true, false, Packet33RelEntityMoveLook.class);
        addIdClassMapping(34, true, false, Packet34EntityTeleport.class);
        addIdClassMapping(38, true, false, Packet38EntityStatus.class);
        addIdClassMapping(39, true, false, Packet39AttachEntity.class);
        addIdClassMapping(40, true, false, Packet40EntityMetadata.class);
        addIdClassMapping(50, true, false, Packet50PreChunk.class);
        addIdClassMapping(51, true, false, Packet51MapChunk.class);
        addIdClassMapping(52, true, false, Packet52MultiBlockChange.class);
        addIdClassMapping(53, true, false, Packet53BlockChange.class);
        addIdClassMapping(54, true, false, Packet54PlayNoteBlock.class);
        addIdClassMapping(60, true, false, Packet60Explosion.class);
        addIdClassMapping(61, true, false, Packet61DoorChange.class);
        addIdClassMapping(70, true, false, Packet70Bed.class);
        addIdClassMapping(71, true, false, Packet71Weather.class);
        addIdClassMapping(100, true, false, Packet100OpenWindow.class);
        addIdClassMapping(101, true, true, Packet101CloseWindow.class);
        addIdClassMapping(102, false, true, Packet102WindowClick.class);
        addIdClassMapping(103, true, false, Packet103SetSlot.class);
        addIdClassMapping(104, true, false, Packet104WindowItems.class);
        addIdClassMapping(105, true, false, Packet105UpdateProgressbar.class);
        addIdClassMapping(106, true, true, Packet106Transaction.class);
        addIdClassMapping(130, true, true, Packet130UpdateSign.class);
        addIdClassMapping(131, true, false, Packet131MapData.class);
        addIdClassMapping(200, true, false, Packet200Statistic.class);
        addIdClassMapping(255, true, true, Packet255KickDisconnect.class);
    }

    public final long creationTimeMillis = System.currentTimeMillis();
    public boolean isChunkDataPacket = false;

    static void addIdClassMapping(int packetId, boolean client, boolean server, Class<? extends Packet> packetClass) {
        if (packetIdToClassMap.containsKey(packetId)) {
            throw new IllegalArgumentException("Duplicate packet id:" + packetId);
        } else if (packetClassToIdMap.containsKey(packetClass)) {
            throw new IllegalArgumentException("Duplicate packet class:" + packetClass);
        }

        packetIdToClassMap.put(packetId, packetClass);
        packetClassToIdMap.put(packetClass, packetId);

        if (client) {
            clientPacketIdList.add(packetId);
        }

        if (server) {
            serverPacketIdList.add(packetId);
        }
    }

    public static Packet getNewPacket(int id) {
        try {
            Class packetClass = packetIdToClassMap.get(id);
            return packetClass == null ? null : (Packet) packetClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Skipping packet with id " + id);
            return null;
        }
    }

    public static Packet readPacket(DataInputStream inputStream, boolean var1) throws IOException {
        final int id;
        final Packet packet;

        try {
            id = inputStream.read();
            if (id == -1) {
                return null;
            }

            if (var1 && !serverPacketIdList.contains(id) || !var1 && !clientPacketIdList.contains(id)) {
                throw new IOException("Bad packet id " + id);
            }

            packet = getNewPacket(id);
            if (packet == null) {
                throw new IOException("Bad packet id " + id);
            }

            packet.readPacketData(inputStream);
        } catch (EOFException var5) {
            System.out.println("Reached end of stream");
            return null;
        }

        PacketCounter packetCounter = packetStats.get(id);
        if (packetCounter == null) {
            packetCounter = new PacketCounter();
            packetStats.put(id, packetCounter);
        }

        packetCounter.addPacket(packet.getPacketSize());
        ++totalPacketsCount;
        if (totalPacketsCount % 1000 == 0) {
        }

        return packet;
    }

    public static void writePacket(Packet packet, DataOutputStream outputStream) throws IOException {
        outputStream.write(packet.getPacketId());
        packet.writePacketData(outputStream);
    }

    public static void writeString(String s, DataOutputStream outputStream) throws IOException {
        if (s.length() > 32767) {
            throw new IOException("String too big");
        } else {
            outputStream.writeShort(s.length());
            outputStream.writeChars(s);
        }
    }

    public static String readString(DataInputStream inputStream, int maxLength) throws IOException {
        short size = inputStream.readShort();
        if (size > maxLength) {
            throw new IOException("Received string length longer than maximum allowed (" + size + " > " + maxLength + ")");
        } else if (size < 0) {
            throw new IOException("Received string length is less than zero! Weird string!");
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            builder.append(inputStream.readChar());
        }

        return builder.toString();
    }

    public final int getPacketId() {
        return packetClassToIdMap.get(this.getClass());
    }

    public abstract void readPacketData(DataInputStream inputStream) throws IOException;

    public abstract void writePacketData(DataOutputStream outputStream) throws IOException;

    public abstract void processPacket(NetHandler netHandler);

    public abstract int getPacketSize();
}
