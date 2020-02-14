package net.potion.client.input.keyboard;

@FunctionalInterface
public interface CraftCharCallback {
    void onChar(char ch, int keycode);
}
