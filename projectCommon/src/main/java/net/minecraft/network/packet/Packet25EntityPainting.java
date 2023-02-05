package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.EntityPainting;
import net.minecraft.entity.EnumArt;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet25EntityPainting extends Packet {
    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int direction;
    public String title;

    public Packet25EntityPainting() {
    }

    @Side(CodeSide.SERVER)
    public Packet25EntityPainting(EntityPainting var1) {
        this.entityId = var1.entityId;
        this.xPosition = var1.xPosition;
        this.yPosition = var1.yPosition;
        this.zPosition = var1.zPosition;
        this.direction = var1.direction;
        this.title = var1.art.title;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.title = readString(var1, EnumArt.maxArtTitleLength);
        this.xPosition = var1.readInt();
        this.yPosition = var1.readInt();
        this.zPosition = var1.readInt();
        this.direction = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        writeString(this.title, var1);
        var1.writeInt(this.xPosition);
        var1.writeInt(this.yPosition);
        var1.writeInt(this.zPosition);
        var1.writeInt(this.direction);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleEntityPainting(this);
    }

    @Override
    public int getPacketSize() {
        return 24;
    }
}
