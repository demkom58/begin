package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet39AttachEntity extends Packet {
   public int entityId;
   public int vehicleEntityId;

   public Packet39AttachEntity() {
   }

   public Packet39AttachEntity(Entity var1, Entity var2) {
      this.entityId = var1.entityId;
      this.vehicleEntityId = var2 != null ? var2.entityId : -1;
   }

   public int getPacketSize() {
      return 8;
   }

   public void readPacketData(DataInputStream inputStream) throws IOException {
      this.entityId = inputStream.readInt();
      this.vehicleEntityId = inputStream.readInt();
   }

   public void writePacketData(DataOutputStream outputStream) throws IOException {
      outputStream.writeInt(this.entityId);
      outputStream.writeInt(this.vehicleEntityId);
   }

   public void processPacket(NetHandler netHandler) {
      netHandler.func_6003_a(this);
   }
}
