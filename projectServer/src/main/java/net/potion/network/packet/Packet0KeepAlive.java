package net.potion.network.packet;

import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet0KeepAlive extends Packet {
    @Override
    public void processPacket(NetHandler netHandler) {
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
    }

    @Override
    public int getPacketSize() {
        return 0;
    }
}
