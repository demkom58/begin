package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet25EntityPainting extends Packet {
   public int entityId;
   public int xPosition;
   public int yPosition;
   public int zPosition;
   public int direction;
   public String title;

   public Packet25EntityPainting() {
   }

   public Packet25EntityPainting(EntityPainting var1) {
      this.entityId = var1.entityId;
      this.xPosition = var1.xPosition;
      this.yPosition = var1.yPosition;
      this.zPosition = var1.zPosition;
      this.direction = var1.direction;
      this.title = var1.art.title;
   }

   public void readPacketData(DataInputStream inputStream) throws IOException {
      this.entityId = inputStream.readInt();
      this.title = readString(inputStream, EnumArt.field_27096_z);
      this.xPosition = inputStream.readInt();
      this.yPosition = inputStream.readInt();
      this.zPosition = inputStream.readInt();
      this.direction = inputStream.readInt();
   }

   public void writePacketData(DataOutputStream outputStream) throws IOException {
      outputStream.writeInt(this.entityId);
      writeString(this.title, outputStream);
      outputStream.writeInt(this.xPosition);
      outputStream.writeInt(this.yPosition);
      outputStream.writeInt(this.zPosition);
      outputStream.writeInt(this.direction);
   }

   public void processPacket(NetHandler netHandler) {
      netHandler.func_21003_a(this);
   }

   public int getPacketSize() {
      return 24;
   }
}
