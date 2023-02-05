package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet7UseEntity extends Packet {
    public int playerEntityId;
    public int targetEntity;
    public int isLeftClick;

    public Packet7UseEntity() {
    }

    @Side(CodeSide.CLIENT)
    public Packet7UseEntity(int var1, int var2, int var3) {
        this.playerEntityId = var1;
        this.targetEntity = var2;
        this.isLeftClick = var3;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.playerEntityId = var1.readInt();
        this.targetEntity = var1.readInt();
        this.isLeftClick = var1.readByte();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.playerEntityId);
        var1.writeInt(this.targetEntity);
        var1.writeByte(this.isLeftClick);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleUseEntity(this);
    }

    @Override
    public int getPacketSize() {
        return 9;
    }
}
