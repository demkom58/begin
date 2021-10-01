package net.potion.client.gui;

import net.potion.entity.player.PlayerControllerSP;
import net.potion.util.MathHelper;
import net.potion.util.StringTranslate;
import net.potion.world.storage.ISaveFormat;
import net.potion.world.storage.SaveFormatData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class GuiSelectWorld extends GuiScreen {
    private final DateFormat dateFormatter = new SimpleDateFormat();
    protected GuiScreen parentScreen;
    protected String screenTitle = "Select world";
    private boolean selected = false;
    private int selectedWorld;
    private List<SaveFormatData> saveList;
    private GuiWorldSlot worldSlotContainer;
    private String screenWorld;
    private String screenConversion;
    private boolean deleting;
    private GuiButton buttonRename;
    private GuiButton buttonSelect;
    private GuiButton buttonDelete;

    public GuiSelectWorld(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    // $FF: synthetic method
    static List<SaveFormatData> getSize(GuiSelectWorld world) {
        return world.saveList;
    }

    // $FF: synthetic method
    static int setSelectedWorld(GuiSelectWorld var0, int var1) {
        return var0.selectedWorld = var1;
    }

    // $FF: synthetic method
    static int getSelectedWorld(GuiSelectWorld var0) {
        return var0.selectedWorld;
    }

    // $FF: synthetic method
    static GuiButton getSelectButton(GuiSelectWorld var0) {
        return var0.buttonSelect;
    }

    // $FF: synthetic method
    static GuiButton getRenameButton(GuiSelectWorld var0) {
        return var0.buttonRename;
    }

    // $FF: synthetic method
    static GuiButton getDeleteButton(GuiSelectWorld var0) {
        return var0.buttonDelete;
    }

    // $FF: synthetic method
    static String getScreenWorld(GuiSelectWorld var0) {
        return var0.screenWorld;
    }

    // $FF: synthetic method
    static DateFormat getDateFormatter(GuiSelectWorld var0) {
        return var0.dateFormatter;
    }

    // $FF: synthetic method
    static String getScreenConversion(GuiSelectWorld var0) {
        return var0.screenConversion;
    }

    @Override
    public void initGui() {
        StringTranslate translate = StringTranslate.getInstance();

        this.screenTitle = translate.translateKey("selectWorld.title");
        this.screenWorld = translate.translateKey("selectWorld.world");
        this.screenConversion = translate.translateKey("selectWorld.conversion");

        this.loadSaves();
        this.worldSlotContainer = new GuiWorldSlot(this);
        this.worldSlotContainer.registerScrollButtons(this.buttons, 4, 5);
        this.initButtons();
    }

    private void loadSaves() {
        ISaveFormat format = this.potion.getSaveLoader();
        this.saveList = format.readSaveFormatData();
        Collections.sort(this.saveList);
        this.selectedWorld = -1;
    }

    protected String getSaveFileName(int var1) {
        return this.saveList.get(var1).getFileName();
    }

    protected String getSaveName(int var1) {
        String displayName = this.saveList.get(var1).getDisplayName();
        if (displayName == null || MathHelper.stringNullOrLengthZero(displayName)) {
            StringTranslate translate = StringTranslate.getInstance();
            displayName = translate.translateKey("selectWorld.world") + " " + (var1 + 1);
        }

        return displayName;
    }

    public void initButtons() {
        StringTranslate translate = StringTranslate.getInstance();
        this.buttons.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, translate.translateKey("selectWorld.select")));
        this.buttons.add(this.buttonRename = new GuiButton(6, this.width / 2 - 154, this.height - 28, 70, 20, translate.translateKey("selectWorld.rename")));
        this.buttons.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, translate.translateKey("selectWorld.delete")));
        this.buttons.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, translate.translateKey("selectWorld.create")));
        this.buttons.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, translate.translateKey("gui.cancel")));
        this.buttonSelect.enabled = false;
        this.buttonRename.enabled = false;
        this.buttonDelete.enabled = false;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 2) {
            String saveName = this.getSaveName(this.selectedWorld);
            if (saveName != null) {
                this.deleting = true;
                StringTranslate translate = StringTranslate.getInstance();
                String deleteQuestionTest = translate.translateKey("selectWorld.deleteQuestion");
                String deleteWarningText = "'" + saveName + "' " + translate.translateKey("selectWorld.deleteWarning");
                String deleteBtnText = translate.translateKey("selectWorld.deleteButton");
                String cancelText = translate.translateKey("gui.cancel");
                GuiYesNo yesNo = new GuiYesNo(this, deleteQuestionTest, deleteWarningText, deleteBtnText, cancelText, this.selectedWorld);
                this.potion.displayGuiScreen(yesNo);
            }
        } else if (button.id == 1) {
            this.selectWorld(this.selectedWorld);
        } else if (button.id == 3) {
            this.potion.displayGuiScreen(new GuiCreateWorld(this));
        } else if (button.id == 6) {
            this.potion.displayGuiScreen(new GuiRenameWorld(this, this.getSaveFileName(this.selectedWorld)));
        } else if (button.id == 0) {
            this.potion.displayGuiScreen(this.parentScreen);
        } else {
            this.worldSlotContainer.actionPerformed(button);
        }
    }

    public void selectWorld(int var1) {
        this.potion.displayGuiScreen(null);
        if (this.selected)
            return;

        this.selected = true;
        this.potion.playerController = new PlayerControllerSP(this.potion);
        String fileName = this.getSaveFileName(var1);
        if (fileName == null) {
            fileName = "World" + var1;
        }

        this.potion.startWorld(fileName, this.getSaveName(var1), 0L);
        this.potion.displayGuiScreen(null);
    }

    @Override
    public void deleteWorld(boolean var1, int var2) {
        if (!this.deleting)
            return;

        this.deleting = false;
        if (var1) {
            ISaveFormat format = this.potion.getSaveLoader();
            format.flushCache();
            format.removeWorld(this.getSaveFileName(var2));
            this.loadSaves();
        }

        this.potion.displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int var1, int var2, float partialTicks) {
        this.worldSlotContainer.drawScreen(var1, var2, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(var1, var2, partialTicks);
    }
}
