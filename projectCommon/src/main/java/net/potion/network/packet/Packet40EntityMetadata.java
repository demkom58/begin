package net.potion.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.network.NetHandler;
import net.potion.util.DataWatcher;
import net.potion.util.WatchableObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet40EntityMetadata extends Packet {
    public int entityId;
    private List<WatchableObject> objects;

    public Packet40EntityMetadata() {
    }

    @Side(CodeSide.SERVER)
    public Packet40EntityMetadata(int var1, DataWatcher var2) {
        this.entityId = var1;
        this.objects = var2.getChangedObjects();
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.objects = DataWatcher.readWatchableObjects(var1);
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        DataWatcher.writeObjectsInListToStream(this.objects, var1);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleEntityMetadata(this);
    }

    @Override
    public int getPacketSize() {
        return 5;
    }

    public List<WatchableObject> getObjects() {
        return this.objects;
    }
}
