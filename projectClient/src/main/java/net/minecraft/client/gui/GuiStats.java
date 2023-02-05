package net.minecraft.client.gui;

import net.hypnosis.render.Tessellator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.stats.StatCollector;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.StringTranslate;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class GuiStats extends GuiScreen {
    private static RenderItem renderItem = new RenderItem();
    protected GuiScreen parentGui;
    protected String statsTitle = "Select world";
    private GuiSlotStatsGeneral slotGeneral;
    private GuiSlotStatsItem slotItem;
    private GuiSlotStatsBlock slotBlock;
    private StatFileWriter statFileWriter;
    private GuiSlot selectedSlot = null;

    public GuiStats(GuiScreen parent, StatFileWriter writer) {
        this.parentGui = parent;
        this.statFileWriter = writer;
    }

    // $FF: synthetic method
    static MinecraftClient getClient(GuiStats var0) {
        return var0.client;
    }

    // $FF: synthetic method
    static FontRenderer getFontRenderer(GuiStats var0) {
        return var0.fontRenderer;
    }

    // $FF: synthetic method
    static StatFileWriter getStatFileWriter(GuiStats var0) {
        return var0.statFileWriter;
    }

    // $FF: synthetic method
    static void drawSprite(GuiStats var0, int var1, int var2, int var3, int var4) {
        var0.drawSprite(var1, var2, var3, var4);
    }

    // $FF: synthetic method
    static void drawGradientRect(GuiStats var0, int var1, int var2, int var3, int var4, int var5, int var6) {
        var0.drawGradientRect(var1, var2, var3, var4, var5, var6);
    }

    // $FF: synthetic method
    static void drawItemSprite(GuiStats var0, int var1, int var2, int var3) {
        var0.drawItemSprite(var1, var2, var3);
    }

    @Override
    public void initGui() {
        this.statsTitle = StatCollector.translateToLocal("gui.stats");
        this.slotGeneral = new GuiSlotStatsGeneral(this);
        this.slotGeneral.registerScrollButtons(this.buttons, 1, 1);
        this.slotItem = new GuiSlotStatsItem(this);
        this.slotItem.registerScrollButtons(this.buttons, 1, 1);
        this.slotBlock = new GuiSlotStatsBlock(this);
        this.slotBlock.registerScrollButtons(this.buttons, 1, 1);
        this.selectedSlot = this.slotGeneral;
        this.addHeaderButtons();
    }

    public void addHeaderButtons() {
        StringTranslate var1 = StringTranslate.getInstance();
        this.buttons.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, var1.translateKey("gui.done")));
        this.buttons.add(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("stat.generalButton")));
        GuiButton var2;
        this.buttons.add(var2 = new GuiButton(2, this.width / 2 - 46, this.height - 52, 100, 20, var1.translateKey("stat.blocksButton")));
        GuiButton var3;
        this.buttons.add(var3 = new GuiButton(3, this.width / 2 + 62, this.height - 52, 100, 20, var1.translateKey("stat.itemsButton")));
        if (this.slotBlock.getSize() == 0) {
            var2.enabled = false;
        }

        if (this.slotItem.getSize() == 0) {
            var3.enabled = false;
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 0) {
            this.client.displayGuiScreen(this.parentGui);
        } else if (button.id == 1) {
            this.selectedSlot = this.slotGeneral;
        } else if (button.id == 3) {
            this.selectedSlot = this.slotItem;
        } else if (button.id == 2) {
            this.selectedSlot = this.slotBlock;
        } else {
            this.selectedSlot.actionPerformed(button);
        }
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.selectedSlot.drawScreen(var1, var2, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.statsTitle, this.width / 2, 20, 16777215);
        super.drawScreen(var1, var2, partialTicks);
    }

    private void drawItemSprite(int var1, int var2, int var3) {
        this.drawButtonBackground(var1 + 1, var2 + 1);
        GL11.glEnable(GL15.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        renderItem.drawItemIntoGui(this.fontRenderer, this.client.renderEngine, var3, 0, Item.ITEMS_LIST[var3].getIconFromDamage(0), var1 + 2, var2 + 2);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL15.GL_RESCALE_NORMAL);
    }

    private void drawButtonBackground(int var1, int var2) {
        this.drawSprite(var1, var2, 0, 0);
    }

    private void drawSprite(int var1, int var2, int var3, int var4) {
        int textureId = this.client.renderEngine.getTexture("/gui/slot.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.renderEngine.bindTexture(textureId);
        Tessellator tess = Tessellator.INSTANCE;
        tess.startDrawingQuads();
        tess.addVertexWithUV(var1, var2 + 18, this.zLevel, (float) (var3) * 0.0078125F, (float) (var4 + 18) * 0.0078125F);
        tess.addVertexWithUV(var1 + 18, var2 + 18, this.zLevel, (float) (var3 + 18) * 0.0078125F, (float) (var4 + 18) * 0.0078125F);
        tess.addVertexWithUV(var1 + 18, var2, this.zLevel, (float) (var3 + 18) * 0.0078125F, (float) (var4) * 0.0078125F);
        tess.addVertexWithUV(var1, var2, this.zLevel, (float) (var3) * 0.0078125F, (float) (var4) * 0.0078125F);
        tess.draw();
    }
}
