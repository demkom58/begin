package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet3Chat extends Packet {
   public String message;

   public Packet3Chat() {
   }

   public Packet3Chat(String var1) {
      if (var1.length() > 119) {
         var1 = var1.substring(0, 119);
      }

      this.message = var1;
   }

   public void readPacketData(DataInputStream inputStream) throws IOException {
      this.message = readString(inputStream, 119);
   }

   public void writePacketData(DataOutputStream outputStream) throws IOException {
      writeString(this.message, outputStream);
   }

   public void processPacket(NetHandler netHandler) {
      netHandler.handleChat(this);
   }

   public int getPacketSize() {
      return this.message.length();
   }
}
