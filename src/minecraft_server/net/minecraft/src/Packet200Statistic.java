package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet200Statistic extends Packet {
   public int field_27041_a;
   public int field_27040_b;

   public Packet200Statistic() {
   }

   public Packet200Statistic(int var1, int var2) {
      this.field_27041_a = var1;
      this.field_27040_b = var2;
   }

   public void processPacket(NetHandler netHandler) {
      netHandler.func_27001_a(this);
   }

   public void readPacketData(DataInputStream inputStream) throws IOException {
      this.field_27041_a = inputStream.readInt();
      this.field_27040_b = inputStream.readByte();
   }

   public void writePacketData(DataOutputStream outputStream) throws IOException {
      outputStream.writeInt(this.field_27041_a);
      outputStream.writeByte(this.field_27040_b);
   }

   public int getPacketSize() {
      return 6;
   }
}
