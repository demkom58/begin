package net.potion.client.gui;

import net.potion.client.PotionClient;
import net.potion.client.render.Particle;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiParticle extends Gui {
    private List<Particle> particles = new ArrayList<>();
    private PotionClient potion;

    public GuiParticle(PotionClient potion) {
        this.potion = potion;
    }

    public void func_25088_a() {
        for (int i = 0; i < this.particles.size(); ++i) {
            Particle particle = this.particles.get(i);
            particle.func_25127_a();
            particle.func_25125_a(this);
            if (particle.field_25139_h) {
                this.particles.remove(i--);
            }
        }

    }

    public void func_25087_a(float var1) {
        this.potion.renderEngine.bindTexture(this.potion.renderEngine.getTexture("/gui/particles.png"));

        for (int i = 0; i < this.particles.size(); ++i) {
            Particle particle = this.particles.get(i);
            int x = (int) (particle.field_25144_c + (particle.field_25146_a - particle.field_25144_c) * (double) var1 - 4.0D);
            int y = (int) (particle.field_25143_d + (particle.field_25145_b - particle.field_25143_d) * (double) var1 - 4.0D);
            float a = (float) (particle.field_25129_r + (particle.field_25133_n - particle.field_25129_r) * (double) var1);
            float r = (float) (particle.field_25132_o + (particle.field_25136_k - particle.field_25132_o) * (double) var1);
            float g = (float) (particle.field_25131_p + (particle.field_25135_l - particle.field_25131_p) * (double) var1);
            float b = (float) (particle.field_25130_q + (particle.field_25134_m - particle.field_25130_q) * (double) var1);
            GL11.glColor4f(r, g, b, a);
            this.drawTexturedModalRect(x, y, 40, 0, 8, 8);
        }

    }
}
