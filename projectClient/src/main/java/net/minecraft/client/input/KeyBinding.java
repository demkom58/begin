package net.minecraft.client.input;

public class KeyBinding {
    public String keyDescription;
    public int keyCode;

    public KeyBinding(String keyDescription, int keyCode) {
        this.keyDescription = keyDescription;
        this.keyCode = keyCode;
    }
}
