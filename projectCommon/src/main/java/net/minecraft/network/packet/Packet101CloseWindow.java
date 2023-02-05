package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet101CloseWindow extends Packet {
    public int windowId;

    public Packet101CloseWindow() {
    }

    public Packet101CloseWindow(int var1) {
        this.windowId = var1;
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleCloseWindow(this);
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.windowId = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeByte(this.windowId);
    }

    @Override
    public int getPacketSize() {
        return 1;
    }
}
