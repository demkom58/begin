package net.potion.network.packet;

import net.potion.util.DataWatcher;
import net.potion.network.NetHandler;
import net.potion.util.WatchableObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet40EntityMetadata extends Packet {
    public int entityId;
    private List<WatchableObject> field_21018_b;

    public Packet40EntityMetadata() {
    }

    public Packet40EntityMetadata(int var1, DataWatcher var2) {
        this.entityId = var1;
        this.field_21018_b = var2.getChangedObjects();
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.field_21018_b = DataWatcher.readWatchableObjects(inputStream);
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        DataWatcher.writeObjectsInListToStream(this.field_21018_b, outputStream);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_21002_a(this);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }
}
