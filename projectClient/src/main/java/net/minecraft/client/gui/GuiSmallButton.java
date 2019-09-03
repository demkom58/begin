package net.minecraft.client.gui;

public class GuiSmallButton extends GuiButton {
    private final EnumOption enumOption;

    public GuiSmallButton(int var1, int var2, int var3, String var4) {
        this(var1, var2, var3, null, var4);
    }

    public GuiSmallButton(int var1, int var2, int var3, int var4, int var5, String var6) {
        super(var1, var2, var3, var4, var5, var6);
        this.enumOption = null;
    }

    public GuiSmallButton(int var1, int var2, int var3, EnumOption var4, String var5) {
        super(var1, var2, var3, 150, 20, var5);
        this.enumOption = var4;
    }

    public EnumOption returnEnumOptions() {
        return this.enumOption;
    }
}
