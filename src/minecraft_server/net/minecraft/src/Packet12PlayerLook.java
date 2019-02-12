package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet12PlayerLook extends Packet10Flying {
   public Packet12PlayerLook() {
      this.rotating = true;
   }

   public void readPacketData(DataInputStream inputStream) throws IOException {
      this.yaw = inputStream.readFloat();
      this.pitch = inputStream.readFloat();
      super.readPacketData(inputStream);
   }

   public void writePacketData(DataOutputStream outputStream) throws IOException {
      outputStream.writeFloat(this.yaw);
      outputStream.writeFloat(this.pitch);
      super.writePacketData(outputStream);
   }

   public int getPacketSize() {
      return 9;
   }
}
