package net.minecraft.src;

class PacketCounter {
   private int totalPackets;
   private long totalBytes;

   public PacketCounter() {
   }

   public void addPacket(int var1) {
      ++this.totalPackets;
      this.totalBytes += (long)var1;
   }
}
