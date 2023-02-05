package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityDispenser;

public class ContainerDispenser extends Container {
    private final TileEntityDispenser dispenser;

    public ContainerDispenser(IInventory inventory, TileEntityDispenser dispenser) {
        this.dispenser = dispenser;

        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                this.addSlot(new Slot(dispenser, y + x * 3, 62 + y * 18, 17 + x * 18));
            }
        }

        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 9; ++y) {
                this.addSlot(new Slot(inventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer var1) {
        return this.dispenser.canInteractWith(var1);
    }
}
