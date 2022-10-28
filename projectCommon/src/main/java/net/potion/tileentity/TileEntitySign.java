package net.potion.tileentity;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.nbt.TagCompound;
import net.potion.network.packet.Packet;
import net.potion.network.packet.Packet130UpdateSign;

public class TileEntitySign extends TileEntity {
    public String[] signText = new String[]{"", "", "", ""};
    public int lineBeingEdited = -1;
    private boolean editable = true;

    @Override
    public void writeToNBT(TagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("Text1", this.signText[0]);
        tag.setString("Text2", this.signText[1]);
        tag.setString("Text3", this.signText[2]);
        tag.setString("Text4", this.signText[3]);
    }

    @Override
    public void readFromNBT(TagCompound tag) {
        this.editable = false;
        super.readFromNBT(tag);

        for (int var2 = 0; var2 < 4; ++var2) {
            this.signText[var2] = tag.getString("Text" + (var2 + 1));
            if (this.signText[var2].length() > 15) {
                this.signText[var2] = this.signText[var2].substring(0, 15);
            }
        }

    }

    @Override
    @Side(CodeSide.SERVER)
    public Packet getDescriptionPacket() {
        String[] var1 = new String[4];

        System.arraycopy(this.signText, 0, var1, 0, 4);

        return new Packet130UpdateSign(this.xCoord, this.yCoord, this.zCoord, var1);
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
