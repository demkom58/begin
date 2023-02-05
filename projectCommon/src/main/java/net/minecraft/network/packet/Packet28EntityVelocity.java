package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet28EntityVelocity extends Packet {
    public int entityId;
    public int motionX;
    public int motionY;
    public int motionZ;

    public Packet28EntityVelocity() {
    }

    @Side(CodeSide.SERVER)
    public Packet28EntityVelocity(Entity entity) {
        this(entity.entityId, entity.motionX, entity.motionY, entity.motionZ);
    }

    @Side(CodeSide.SERVER)
    public Packet28EntityVelocity(int var1, double var2, double var4, double var6) {
        this.entityId = var1;
        double var8 = 3.9D;
        if (var2 < -var8) {
            var2 = -var8;
        }

        if (var4 < -var8) {
            var4 = -var8;
        }

        if (var6 < -var8) {
            var6 = -var8;
        }

        if (var2 > var8) {
            var2 = var8;
        }

        if (var4 > var8) {
            var4 = var8;
        }

        if (var6 > var8) {
            var6 = var8;
        }

        this.motionX = (int) (var2 * 8000.0D);
        this.motionY = (int) (var4 * 8000.0D);
        this.motionZ = (int) (var6 * 8000.0D);
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.motionX = var1.readShort();
        this.motionY = var1.readShort();
        this.motionZ = var1.readShort();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        var1.writeShort(this.motionX);
        var1.writeShort(this.motionY);
        var1.writeShort(this.motionZ);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleEntityVelocity(this);
    }

    @Override
    public int getPacketSize() {
        return 10;
    }
}
