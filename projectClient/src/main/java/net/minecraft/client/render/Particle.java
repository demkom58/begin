package net.minecraft.client.render;

import net.minecraft.client.gui.GuiParticle;

public class Particle {
    public double posX;
    public double posY;
    public double prevPosX;
    public double prevPosY;
    public double velX;
    public double velY;
    public double accelScale;
    public boolean isDead;
    public int timeTick;
    public int timeLimit;
    public double tintRed;
    public double tintGreen;
    public double tintBlue;
    public double tintAlpha;
    public double prevTintRed;
    public double prevTintGreen;
    public double prevTintBlue;
    public double prevTintAlpha;

    public void update(GuiParticle g) {
        this.posX += this.velX;
        this.posY += this.velY;
        this.velX *= this.accelScale;
        this.velY *= this.accelScale;
        this.velY += 0.1D;
        if (++this.timeTick > this.timeLimit) {
            this.setDead();
        }

        this.tintAlpha = 2.0D - (double) this.timeTick / (double) this.timeLimit * 2.0D;
        if (this.tintAlpha > 1.0D) {
            this.tintAlpha = 1.0D;
        }

        this.tintAlpha *= this.tintAlpha;
        this.tintAlpha *= 0.5D;
    }

    public void func_25127_a() {
        this.prevTintRed = this.tintRed;
        this.prevTintGreen = this.tintGreen;
        this.prevTintBlue = this.tintBlue;
        this.prevTintAlpha = this.tintAlpha;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
    }

    public void setDead() {
        this.isDead = true;
    }
}
