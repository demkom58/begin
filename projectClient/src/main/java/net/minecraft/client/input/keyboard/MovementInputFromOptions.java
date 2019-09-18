package net.minecraft.client.input.keyboard;

import net.minecraft.client.GameSettings;
import net.minecraft.entity.player.EntityPlayer;

public class MovementInputFromOptions extends MovementInput {
    private boolean[] movementKeyStates = new boolean[10];
    private GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings var1) {
        this.gameSettings = var1;
    }

    public void checkKeyForMovementInput(int var1, boolean var2) {
        byte var3 = -1;
        if (var1 == this.gameSettings.keyBindForward.keyCode) {
            var3 = 0;
        }

        if (var1 == this.gameSettings.keyBindBack.keyCode) {
            var3 = 1;
        }

        if (var1 == this.gameSettings.keyBindLeft.keyCode) {
            var3 = 2;
        }

        if (var1 == this.gameSettings.keyBindRight.keyCode) {
            var3 = 3;
        }

        if (var1 == this.gameSettings.keyBindJump.keyCode) {
            var3 = 4;
        }

        if (var1 == this.gameSettings.keyBindSneak.keyCode) {
            var3 = 5;
        }

        if (var3 >= 0) {
            this.movementKeyStates[var3] = var2;
        }

    }

    public void resetKeyState() {
        for (int i = 0; i < 10; ++i) {
            this.movementKeyStates[i] = false;
        }

    }

    public void updatePlayerMoveState(EntityPlayer player) {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;
        if (this.movementKeyStates[0]) {
            ++this.moveForward;
        }

        if (this.movementKeyStates[1]) {
            --this.moveForward;
        }

        if (this.movementKeyStates[2]) {
            ++this.moveStrafe;
        }

        if (this.movementKeyStates[3]) {
            --this.moveStrafe;
        }

        this.jump = this.movementKeyStates[4];
        this.sneak = this.movementKeyStates[5];
        if (this.sneak) {
            this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
            this.moveForward = (float) ((double) this.moveForward * 0.3D);
        }

    }
}
