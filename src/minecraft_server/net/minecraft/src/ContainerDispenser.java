package net.minecraft.src;

public class ContainerDispenser extends Container {
    private TileEntityDispenser field_21133_a;

    public ContainerDispenser(IInventory var1, TileEntityDispenser var2) {
        this.field_21133_a = var2;

        for (int var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 3; ++var4) {
                this.addSlot(new Slot(var2, var4 + var3 * 3, 62 + var4 * 18, 17 + var3 * 18));
            }
        }

        for (int var5 = 0; var5 < 3; ++var5) {
            for (int var7 = 0; var7 < 9; ++var7) {
                this.addSlot(new Slot(var1, var7 + var5 * 9 + 9, 8 + var7 * 18, 84 + var5 * 18));
            }
        }

        for (int var6 = 0; var6 < 9; ++var6) {
            this.addSlot(new Slot(var1, var6, 8 + var6 * 18, 142));
        }

    }

    public boolean canInteractWith(EntityPlayer var1) {
        return this.field_21133_a.canInteractWith(var1);
    }
}
