package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet255KickDisconnect extends Packet {
   public String reason;

   public Packet255KickDisconnect() {
   }

   public Packet255KickDisconnect(String var1) {
      this.reason = var1;
   }

   public void readPacketData(DataInputStream inputStream) throws IOException {
      this.reason = readString(inputStream, 100);
   }

   public void writePacketData(DataOutputStream outputStream) throws IOException {
      writeString(this.reason, outputStream);
   }

   public void processPacket(NetHandler netHandler) {
      netHandler.handleKickDisconnect(this);
   }

   public int getPacketSize() {
      return this.reason.length();
   }
}
