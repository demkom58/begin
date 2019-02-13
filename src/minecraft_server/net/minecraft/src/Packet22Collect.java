package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet22Collect extends Packet {
    public int collectedEntityId;
    public int collectorEntityId;

    public Packet22Collect() {
    }

    public Packet22Collect(int var1, int var2) {
        this.collectedEntityId = var1;
        this.collectorEntityId = var2;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.collectedEntityId = inputStream.readInt();
        this.collectorEntityId = inputStream.readInt();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.collectedEntityId);
        outputStream.writeInt(this.collectorEntityId);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleCollect(this);
    }

    public int getPacketSize() {
        return 8;
    }
}
