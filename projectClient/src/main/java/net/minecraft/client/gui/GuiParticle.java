package net.minecraft.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Particle;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiParticle extends Gui {
    private List<Particle> particles = new ArrayList<>();
    private MinecraftClient client;

    public GuiParticle(MinecraftClient client) {
        this.client = client;
    }

    public void func_25088_a() {
        for (int i = 0; i < this.particles.size(); ++i) {
            Particle particle = this.particles.get(i);
            particle.func_25127_a();
            particle.update(this);
            if (particle.isDead) {
                this.particles.remove(i--);
            }
        }

    }

    public void func_25087_a(float var1) {
        this.client.renderEngine.bindTexture(this.client.renderEngine.getTexture("/gui/particles.png"));

        for (int i = 0; i < this.particles.size(); ++i) {
            Particle particle = this.particles.get(i);
            int x = (int) (particle.prevPosX + (particle.posX - particle.prevPosX) * (double) var1 - 4.0D);
            int y = (int) (particle.prevPosY + (particle.posY - particle.prevPosY) * (double) var1 - 4.0D);
            float a = (float) (particle.prevTintAlpha + (particle.tintAlpha - particle.prevTintAlpha) * (double) var1);
            float r = (float) (particle.prevTintRed + (particle.tintRed - particle.prevTintRed) * (double) var1);
            float g = (float) (particle.prevTintGreen + (particle.tintGreen - particle.prevTintGreen) * (double) var1);
            float b = (float) (particle.prevTintBlue + (particle.tintBlue - particle.prevTintBlue) * (double) var1);
            GL11.glColor4f(r, g, b, a);
            this.drawTexturedModalRect(x, y, 40, 0, 8, 8);
        }

    }
}
