package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PlayerInventory extends Packet {
   public int entityID;
   public int slot;
   public int itemID;
   public int itemDamage;

   public Packet5PlayerInventory() {
   }

   public Packet5PlayerInventory(int var1, int var2, ItemStack var3) {
      this.entityID = var1;
      this.slot = var2;
      if (var3 == null) {
         this.itemID = -1;
         this.itemDamage = 0;
      } else {
         this.itemID = var3.itemID;
         this.itemDamage = var3.getItemDamage();
      }

   }

   public void readPacketData(DataInputStream inputStream) throws IOException {
      this.entityID = inputStream.readInt();
      this.slot = inputStream.readShort();
      this.itemID = inputStream.readShort();
      this.itemDamage = inputStream.readShort();
   }

   public void writePacketData(DataOutputStream outputStream) throws IOException {
      outputStream.writeInt(this.entityID);
      outputStream.writeShort(this.slot);
      outputStream.writeShort(this.itemID);
      outputStream.writeShort(this.itemDamage);
   }

   public void processPacket(NetHandler netHandler) {
      netHandler.handlePlayerInventory(this);
   }

   public int getPacketSize() {
      return 8;
   }
}
