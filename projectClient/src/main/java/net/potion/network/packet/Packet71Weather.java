package net.potion.network.packet;

import net.potion.entity.Entity;
import net.potion.entity.EntityLightningBolt;
import net.potion.network.NetHandler;
import net.potion.util.MathHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet71Weather extends Packet {
    public int field_27054_a;
    public int field_27053_b;
    public int field_27057_c;
    public int field_27056_d;
    public int field_27055_e;

    public Packet71Weather() {
    }

    public Packet71Weather(Entity var1) {
        this.field_27054_a = var1.entityId;
        this.field_27053_b = MathHelper.floor(var1.posX * 32.0D);
        this.field_27057_c = MathHelper.floor(var1.posY * 32.0D);
        this.field_27056_d = MathHelper.floor(var1.posZ * 32.0D);
        if (var1 instanceof EntityLightningBolt) {
            this.field_27055_e = 1;
        }

    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.field_27054_a = var1.readInt();
        this.field_27055_e = var1.readByte();
        this.field_27053_b = var1.readInt();
        this.field_27057_c = var1.readInt();
        this.field_27056_d = var1.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.field_27054_a);
        var1.writeByte(this.field_27055_e);
        var1.writeInt(this.field_27053_b);
        var1.writeInt(this.field_27057_c);
        var1.writeInt(this.field_27056_d);
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
