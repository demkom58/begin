package net.potion.entity.player;

import net.potion.client.PotionClient;
import net.potion.client.Session;
import net.potion.item.ItemStack;

public class PlayerControllerTest extends PlayerController {
    public PlayerControllerTest(PotionClient potion) {
        super(potion);
        this.ghost = true;
    }

    @Override
    public void func_6473_b(EntityPlayer player) {
        for (int i = 0; i < 9; ++i) {
            if (player.inventory.mainInventory[i] == null)
                this.potion.thePlayer.inventory.mainInventory[i] = new ItemStack(Session.registeredBlocksList.get(i));
            else
                this.potion.thePlayer.inventory.mainInventory[i].stackSize = 1;
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
