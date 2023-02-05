package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet27Position extends Packet {
    private float strafeMovement;
    private float fowardMovement;
    private boolean sneak;
    private boolean isInJump;
    private float pitchRotation;
    private float yawRotation;

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.strafeMovement = var1.readFloat();
        this.fowardMovement = var1.readFloat();
        this.pitchRotation = var1.readFloat();
        this.yawRotation = var1.readFloat();
        this.sneak = var1.readBoolean();
        this.isInJump = var1.readBoolean();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeFloat(this.strafeMovement);
        var1.writeFloat(this.fowardMovement);
        var1.writeFloat(this.pitchRotation);
        var1.writeFloat(this.yawRotation);
        var1.writeBoolean(this.sneak);
        var1.writeBoolean(this.isInJump);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handlePosition(this);
    }

    @Override
    public int getPacketSize() {
        return 18;
    }

    public float getStrafe() {
        return this.strafeMovement;
    }

    public float getForward() {
        return this.fowardMovement;
    }

    public float getPitch() {
        return this.pitchRotation;
    }

    public float getYaw() {
        return this.yawRotation;
    }

    public boolean isSneak() {
        return this.sneak;
    }

    public boolean isJumping() {
        return this.isInJump;
    }
}
