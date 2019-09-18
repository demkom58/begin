package net.minecraft.client.input.keyboard;

@FunctionalInterface
public interface CraftKeyCallback {
    void onKey(int key, int scancode, int action, int mods);
}
