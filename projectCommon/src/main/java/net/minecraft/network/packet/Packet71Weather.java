package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLightningBolt;
import net.minecraft.network.NetHandler;
import net.hypnosis.util.math.MathHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet71Weather extends Packet {
    public int entityId;
    public int x;
    public int y;
    public int z;
    public int field1;

    public Packet71Weather() {
    }

    @Side(CodeSide.SERVER)
    public Packet71Weather(Entity var1) {
        this.entityId = var1.entityId;
        this.x = MathHelper.floor(var1.posX * 32.0D);
        this.y = MathHelper.floor(var1.posY * 32.0D);
        this.z = MathHelper.floor(var1.posZ * 32.0D);
        if (var1 instanceof EntityLightningBolt) {
            this.field1 = 1;
        }

    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.field1 = var1.readByte();
        this.x = var1.readInt();
        this.y = var1.readInt();
        this.z = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        var1.writeByte(this.field1);
        var1.writeInt(this.x);
        var1.writeInt(this.y);
        var1.writeInt(this.z);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleWeather(this);
    }

    @Override
    public int getPacketSize() {
        return 17;
    }
}
