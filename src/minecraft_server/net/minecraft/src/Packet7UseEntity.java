package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet7UseEntity extends Packet {
   public int playerEntityId;
   public int targetEntity;
   public int isLeftClick;

   public void readPacketData(DataInputStream inputStream) throws IOException {
      this.playerEntityId = inputStream.readInt();
      this.targetEntity = inputStream.readInt();
      this.isLeftClick = inputStream.readByte();
   }

   public void writePacketData(DataOutputStream outputStream) throws IOException {
      outputStream.writeInt(this.playerEntityId);
      outputStream.writeInt(this.targetEntity);
      outputStream.writeByte(this.isLeftClick);
   }

   public void processPacket(NetHandler netHandler) {
      netHandler.func_6006_a(this);
   }

   public int getPacketSize() {
      return 9;
   }
}
