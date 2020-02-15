package net.potion.client.gui;

import net.hypnosis.render.Tessellator;
import net.potion.util.StringTranslate;
import org.lwjgl.opengl.GL11;
import net.potion.util.MathHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GuiMainMenu extends GuiScreen {
    private static final Random rand = new Random();
    private float updateCounter = 0.0F;
    private String splashText = "missingno";
    private GuiButton multiplayerButton;

    public GuiMainMenu() {
        try {
            List<String> splashes = new ArrayList<>();
            BufferedReader splashesReader = new BufferedReader(new InputStreamReader(GuiMainMenu.class.getResourceAsStream("/title/splashes.txt"), StandardCharsets.UTF_8));

            String buffStr;
            while ((buffStr = splashesReader.readLine()) != null) {
                buffStr = buffStr.trim();
                if (buffStr.length() > 0)
                    splashes.add(buffStr);
            }

            this.splashText = splashes.get(rand.nextInt(splashes.size()));
        } catch (Exception ignored) {

        }

    }

    @Override
    public void updateScreen() {
        ++this.updateCounter;
    }

    @Override
    public void charTyped(char ch, int key) {
    }

    @Override
    public void keyTyped(int keycode, int scancode, int action, int mods) {
    }

    @Override
    public void initGui() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(Calendar.MONTH) + 1 == 11 && calendar.get(Calendar.DATE) == 9) {
            this.splashText = "Happy birthday, ez!";
        } else if (calendar.get(Calendar.MONTH) + 1 == 6 && calendar.get(Calendar.DATE) == 1) {
            this.splashText = "Happy birthday, Notch!";
        } else if (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (calendar.get(Calendar.MONTH) + 1 == 1 && calendar.get(Calendar.DATE) == 1) {
            this.splashText = "Happy new year!";
        }

        StringTranslate translate = StringTranslate.getInstance();
        int var4 = this.height / 4 + 48;
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, var4, translate.translateKey("menu.singleplayer")));
        this.buttons.add(this.multiplayerButton = new GuiButton(2, this.width / 2 - 100, var4 + 24, translate.translateKey("menu.multiplayer")));
        this.buttons.add(new GuiButton(3, this.width / 2 - 100, var4 + 48, translate.translateKey("menu.mods")));

        this.buttons.add(new GuiButton(0, this.width / 2 - 100, var4 + 72 + 12, 98, 20, translate.translateKey("menu.options")));
        this.buttons.add(new GuiButton(4, this.width / 2 + 2, var4 + 72 + 12, 98, 20, translate.translateKey("menu.quit")));

        if (this.potion.session == null) {
            this.multiplayerButton.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.potion.displayGuiScreen(new GuiOptions(this, this.potion.gameSettings));
        }

        if (button.id == 1) {
            this.potion.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            this.potion.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 3) {
            this.potion.displayGuiScreen(new GuiTexturePacks(this));
        }

        if (button.id == 4) {
            this.potion.shutdown();
        }

    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.drawDefaultBackground();
        Tessellator tess = Tessellator.INSTANCE;
        short var5 = 274;
        int var6 = this.width / 2 - var5 / 2;
        byte var7 = 30;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.potion.renderEngine.getTexture("/title/mclogo.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(var6, var7, 0, 0, 155, 44);
        this.drawTexturedModalRect(var6 + 155, var7, 0, 45, 155, 44);
        tess.setColorOpaque_I(16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float) (System.currentTimeMillis() % 1000L) / 1000.0F * 3.1415927F * 2.0F) * 0.1F);
        var8 = var8 * 100.0F / (float) (this.fontRenderer.getStringWidth(this.splashText) + 32);
        GL11.glScalef(var8, var8, var8);
        this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 0xFFFF00);
        GL11.glPopMatrix();
        this.drawString(this.fontRenderer, "Potion in-dev 0.0.1", 2, 2, 0x505050);
        String var9 = "Copyright Mojang AB. Do not distribute.";
        this.drawString(this.fontRenderer, var9, this.width - this.fontRenderer.getStringWidth(var9) - 2, this.height - 10, 0xFFFFFF);
        super.drawScreen(var1, var2, var3);
    }
}
