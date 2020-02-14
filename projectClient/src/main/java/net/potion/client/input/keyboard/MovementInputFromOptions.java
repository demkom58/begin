package net.potion.client.input.keyboard;

import net.potion.client.GameSettings;
import net.potion.entity.player.EntityPlayer;

public class MovementInputFromOptions extends MovementInput {
    private boolean[] movementKeyStates = new boolean[10];
    private GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings var1) {
        this.gameSettings = var1;
    }

    @Override
    public void checkKeyForMovementInput(int key, boolean isDown) {
        byte moveId = -1;

        if (key == this.gameSettings.keyBindForward.keyCode)
            moveId = 0;
        else if (key == this.gameSettings.keyBindBack.keyCode)
            moveId = 1;
        else if (key == this.gameSettings.keyBindLeft.keyCode)
            moveId = 2;
        else if (key == this.gameSettings.keyBindRight.keyCode)
            moveId = 3;
        else if (key == this.gameSettings.keyBindJump.keyCode)
            moveId = 4;
        else if (key == this.gameSettings.keyBindSneak.keyCode)
            moveId = 5;

        if (moveId >= 0)
            this.movementKeyStates[moveId] = isDown;
    }

    @Override
    public void resetKeyState() {
        for (int i = 0; i < 10; ++i) {
            this.movementKeyStates[i] = false;
        }

    }

    @Override
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
