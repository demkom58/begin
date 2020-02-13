package net.minecraft.network.packet;

import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet27Position extends Packet {
    private float strafeMovement;
    private float fowardMovement;
    private boolean field_22039_c;
    private boolean isInJump;
    private float pitchRotation;
    private float yawRotation;

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.strafeMovement = inputStream.readFloat();
        this.fowardMovement = inputStream.readFloat();
        this.pitchRotation = inputStream.readFloat();
        this.yawRotation = inputStream.readFloat();
        this.field_22039_c = inputStream.readBoolean();
        this.isInJump = inputStream.readBoolean();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeFloat(this.strafeMovement);
        outputStream.writeFloat(this.fowardMovement);
        outputStream.writeFloat(this.pitchRotation);
        outputStream.writeFloat(this.yawRotation);
        outputStream.writeBoolean(this.field_22039_c);
        outputStream.writeBoolean(this.isInJump);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleMovementTypePacket(this);
    }

    @Override
    public int getPacketSize() {
        return 18;
    }

    public float func_22031_c() {
        return this.strafeMovement;
    }

    public float func_22029_d() {
        return this.pitchRotation;
    }

    public float func_22028_e() {
        return this.fowardMovement;
    }

    public float func_22033_f() {
        return this.yawRotation;
    }

    public boolean func_22032_g() {
        return this.field_22039_c;
    }

    public boolean func_22030_h() {
        return this.isInJump;
    }
}
