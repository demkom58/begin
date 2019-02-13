package net.minecraft;

public class TileEntitySign extends TileEntity {
    public String[] signText = new String[]{"", "", "", ""};
    public int lineBeingEdited = -1;
    private boolean isEditAble = true;

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("Text1", this.signText[0]);
        compound.setString("Text2", this.signText[1]);
        compound.setString("Text3", this.signText[2]);
        compound.setString("Text4", this.signText[3]);
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.isEditAble = false;
        super.readFromNBT(compound);

        for (int var2 = 0; var2 < 4; ++var2) {
            this.signText[var2] = compound.getString("Text" + (var2 + 1));
            if (this.signText[var2].length() > 15) {
                this.signText[var2] = this.signText[var2].substring(0, 15);
            }
        }

    }

    public Packet getDescriptionPacket() {
        String[] var1 = new String[4];

        for (int var2 = 0; var2 < 4; ++var2) {
            var1[var2] = this.signText[var2];
        }

        return new Packet130UpdateSign(this.xCoord, this.yCoord, this.zCoord, var1);
    }

    public boolean getIsEditAble() {
        return this.isEditAble;
    }

    public void func_32001_a(boolean var1) {
        this.isEditAble = var1;
    }
}
