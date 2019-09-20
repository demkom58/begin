package net.minecraft.client.input.keyboard;

import net.hypnosis.input.keyboard.Keyboard;
import net.hypnosis.monitor.Window;
import org.jetbrains.annotations.NotNull;

public class CraftKeyboard {
    private final Window window;
    private final Keyboard keyboard;

    public CraftKeyboard(@NotNull final Window window) {
        this.window = window;
        this.keyboard = new Keyboard(window);
    }

    public void setCharCallback(@NotNull final CraftCharCallback callback) {
        keyboard.setCharModsCallback((window, codepoint, mods) -> {
            if (window != this.window.getPointer())
                return;

            if (Character.charCount(codepoint) == 1) {
                callback.onChar((char) codepoint, mods);
                return;
            }

            char[] chars = Character.toChars(codepoint);
            for (int i = 0; i < chars.length; ++i)
                callback.onChar(chars[i], mods);
        });
    }

    public void setKeyCallback(@NotNull final CraftKeyCallback callback) {
        keyboard.setKeyCallback((window, key, scancode, action, mods) -> {
            if (window != this.window.getPointer())
                return;

            callback.onKey(key, scancode, action, mods);
        });
    }

    public boolean isKeyDown(int keyCode) {
        return keyboard.isKeyDown(keyCode);
    }



}
