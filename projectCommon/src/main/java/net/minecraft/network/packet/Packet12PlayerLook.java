package net.minecraft.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet12PlayerLook extends Packet10Flying {
    public Packet12PlayerLook() {
        this.rotating = true;
    }

    @Side(CodeSide.CLIENT)
    public Packet12PlayerLook(float var1, float var2, boolean var3) {
        this.yaw = var1;
        this.pitch = var2;
        this.onGround = var3;
        this.rotating = true;
    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.yaw = var1.readFloat();
        this.pitch = var1.readFloat();
        super.readPacketData(var1);
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeFloat(this.yaw);
        var1.writeFloat(this.pitch);
        super.writePacketData(var1);
    }

    @Override
    public int getPacketSize() {
        return 9;
    }
}
