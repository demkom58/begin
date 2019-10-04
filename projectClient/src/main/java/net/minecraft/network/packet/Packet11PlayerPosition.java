package net.minecraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet11PlayerPosition extends Packet10Flying {
    public Packet11PlayerPosition() {
        this.moving = true;
    }

    public Packet11PlayerPosition(double var1, double var3, double var5, double var7, boolean var9) {
        this.xPosition = var1;
        this.yPosition = var3;
        this.stance = var5;
        this.zPosition = var7;
        this.onGround = var9;
        this.moving = true;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.xPosition = var1.readDouble();
        this.yPosition = var1.readDouble();
        this.stance = var1.readDouble();
        this.zPosition = var1.readDouble();
        super.readPacketData(var1);
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeDouble(this.xPosition);
        var1.writeDouble(this.yPosition);
        var1.writeDouble(this.stance);
        var1.writeDouble(this.zPosition);
        super.writePacketData(var1);
    }

    @Override
    public int getPacketSize() {
        return 33;
    }
}
