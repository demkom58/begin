package net.minecraft.client.gui;

import net.hypnosis.util.math.MathConstants;
import net.minecraft.stats.StatCollector;
import net.minecraft.stats.StatList;
import net.hypnosis.util.math.MathHelper;

public class GuiIngameMenu extends GuiScreen {
    private int timesSaved = 0;
    private int updateCounter = 0;

    @Override
    public void initGui() {
        this.timesSaved = 0;
        this.buttons.clear();
        byte var1 = -16;
        this.buttons.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + var1, "Save and quit to title"));
        if (this.client.isMultiplayerWorld()) {
            this.buttons.get(0).displayString = "Disconnect";
        }

        this.buttons.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + var1, "Back to game"));
        this.buttons.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + var1, "Options..."));
        this.buttons.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + var1, 98, 20, StatCollector.translateToLocal("gui.achievements")));
        this.buttons.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + var1, 98, 20, StatCollector.translateToLocal("gui.stats")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.client.displayGuiScreen(new GuiOptions(this, this.client.gameSettings));
        }

        if (button.id == 1) {
            this.client.statFileWriter.addStat(StatList.leaveGameStat, 1);
            if (this.client.isMultiplayerWorld()) {
                this.client.theWorld.sendQuittingDisconnectingPacket();
            }

            this.client.changeWorld(null);
            this.client.displayGuiScreen(new GuiMainMenu());
        }

        if (button.id == 4) {
            this.client.displayGuiScreen(null);
            this.client.setIngameFocus();
        }

        if (button.id == 5) {
            this.client.displayGuiScreen(new GuiAchievements(this.client.statFileWriter));
        }

        if (button.id == 6) {
            this.client.displayGuiScreen(new GuiStats(this, this.client.statFileWriter));
        }

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.updateCounter;
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.drawDefaultBackground();
        boolean var4 = !this.client.theWorld.save(this.timesSaved++);
        if (var4 || this.updateCounter < 20) {
            float var5 = ((float) (this.updateCounter % 10) + partialTicks) / 10.0F;
            var5 = MathHelper.sin(var5 * MathConstants.PI * 2.0F) * 0.2F + 0.8F;
            int var6 = (int) (255.0F * var5);
            this.drawString(this.fontRenderer, "Saving level..", 8, this.height - 16, var6 << 16 | var6 << 8 | var6);
        }

        this.drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 16777215);
        super.drawScreen(var1, var2, partialTicks);
    }
}
