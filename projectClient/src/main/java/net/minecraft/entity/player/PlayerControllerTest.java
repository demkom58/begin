package net.minecraft.entity.player;

import net.minecraft.client.Session;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class PlayerControllerTest extends PlayerController {
    public PlayerControllerTest(Minecraft mc) {
        super(mc);
        this.ghost = true;
    }

    @Override
    public void func_6473_b(EntityPlayer player) {
        for (int i = 0; i < 9; ++i) {
            if (player.inventory.mainInventory[i] == null)
                this.mc.thePlayer.inventory.mainInventory[i] = new ItemStack(Session.registeredBlocksList.get(i));
            else
                this.mc.thePlayer.inventory.mainInventory[i].stackSize = 1;
        }

    }

    @Override
    public boolean shouldDrawHUD() {
        return false;
    }

    @Override
    public void updateController() {
    }
}
