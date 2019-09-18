package net.hypnosis.input;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.lwjgl.glfw.GLFW;

public enum KeySource {
    KEYBOARD("key.keyboard"),
    UNKNOWN("scancode"),
    MOUSE("key.mouse");

    private static final String[] mouseButtons;
    private final Int2ObjectMap<KeyInfo> map = new Int2ObjectOpenHashMap<>();
    private final String name;

    KeySource(String name) {
        this.name = name;
    }

    public KeyInfo getKeyInfo(int keyCode) {
        if (this.map.containsKey(keyCode))
            return this.map.get(keyCode);

        String keyName;
        if (this == MOUSE) {
            if (keyCode <= 2) keyName = "." + mouseButtons[keyCode];
            else keyName = "." + (keyCode + 1);
        } else {
            keyName = "." + keyCode;
        }

        KeyInfo code = new KeyInfo(this.name + keyName, this, keyCode);
        this.map.put(keyCode, code);
        return code;
    }

    public String getName() {
        return this.name;
    }

    private static void map(KeySource source, String name, int keyCode) {
        source.map.put(keyCode, new KeyInfo(source.getName() + "." + name, source, keyCode));
    }

    static {
        map(KEYBOARD, "unknown", GLFW.GLFW_KEY_UNKNOWN);
        map(MOUSE, "left", GLFW.GLFW_MOUSE_BUTTON_LEFT);
        map(MOUSE, "right", GLFW.GLFW_MOUSE_BUTTON_RIGHT);
        map(MOUSE, "middle", GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
        map(MOUSE, "4", GLFW.GLFW_MOUSE_BUTTON_4);
        map(MOUSE, "5", GLFW.GLFW_MOUSE_BUTTON_5);
        map(MOUSE, "6", GLFW.GLFW_MOUSE_BUTTON_6);
        map(MOUSE, "7", GLFW.GLFW_MOUSE_BUTTON_7);
        map(MOUSE, "8", GLFW.GLFW_MOUSE_BUTTON_8);
        map(KEYBOARD, "0", GLFW.GLFW_KEY_0);
        map(KEYBOARD, "1", GLFW.GLFW_KEY_1);
        map(KEYBOARD, "2", GLFW.GLFW_KEY_2);
        map(KEYBOARD, "3", GLFW.GLFW_KEY_3);
        map(KEYBOARD, "4", GLFW.GLFW_KEY_4);
        map(KEYBOARD, "5", GLFW.GLFW_KEY_5);
        map(KEYBOARD, "6", GLFW.GLFW_KEY_6);
        map(KEYBOARD, "7", GLFW.GLFW_KEY_7);
        map(KEYBOARD, "8", GLFW.GLFW_KEY_8);
        map(KEYBOARD, "9", GLFW.GLFW_KEY_9);
        map(KEYBOARD, "a", GLFW.GLFW_KEY_A);
        map(KEYBOARD, "b", GLFW.GLFW_KEY_B);
        map(KEYBOARD, "c", GLFW.GLFW_KEY_C);
        map(KEYBOARD, "d", GLFW.GLFW_KEY_D);
        map(KEYBOARD, "e", GLFW.GLFW_KEY_E);
        map(KEYBOARD, "f", GLFW.GLFW_KEY_F);
        map(KEYBOARD, "g", GLFW.GLFW_KEY_G);
        map(KEYBOARD, "h", GLFW.GLFW_KEY_H);
        map(KEYBOARD, "i", GLFW.GLFW_KEY_I);
        map(KEYBOARD, "j", GLFW.GLFW_KEY_J);
        map(KEYBOARD, "k", GLFW.GLFW_KEY_K);
        map(KEYBOARD, "l", GLFW.GLFW_KEY_L);
        map(KEYBOARD, "m", GLFW.GLFW_KEY_M);
        map(KEYBOARD, "n", GLFW.GLFW_KEY_N);
        map(KEYBOARD, "o", GLFW.GLFW_KEY_O);
        map(KEYBOARD, "p", GLFW.GLFW_KEY_P);
        map(KEYBOARD, "q", GLFW.GLFW_KEY_Q);
        map(KEYBOARD, "r", GLFW.GLFW_KEY_R);
        map(KEYBOARD, "s", GLFW.GLFW_KEY_S);
        map(KEYBOARD, "t", GLFW.GLFW_KEY_T);
        map(KEYBOARD, "u", GLFW.GLFW_KEY_U);
        map(KEYBOARD, "v", GLFW.GLFW_KEY_V);
        map(KEYBOARD, "w", GLFW.GLFW_KEY_W);
        map(KEYBOARD, "x", GLFW.GLFW_KEY_X);
        map(KEYBOARD, "y", GLFW.GLFW_KEY_Y);
        map(KEYBOARD, "z", GLFW.GLFW_KEY_Z);
        map(KEYBOARD, "f1", GLFW.GLFW_KEY_F1);
        map(KEYBOARD, "f2", GLFW.GLFW_KEY_F2);
        map(KEYBOARD, "f3", GLFW.GLFW_KEY_F3);
        map(KEYBOARD, "f4", GLFW.GLFW_KEY_F4);
        map(KEYBOARD, "f5", GLFW.GLFW_KEY_F5);
        map(KEYBOARD, "f6", GLFW.GLFW_KEY_F6);
        map(KEYBOARD, "f7", GLFW.GLFW_KEY_F7);
        map(KEYBOARD, "f8", GLFW.GLFW_KEY_F8);
        map(KEYBOARD, "f9", GLFW.GLFW_KEY_F9);
        map(KEYBOARD, "f10", GLFW.GLFW_KEY_F10);
        map(KEYBOARD, "f11", GLFW.GLFW_KEY_F11);
        map(KEYBOARD, "f12", GLFW.GLFW_KEY_F12);
        map(KEYBOARD, "f13", GLFW.GLFW_KEY_F13);
        map(KEYBOARD, "f14", GLFW.GLFW_KEY_F14);
        map(KEYBOARD, "f15", GLFW.GLFW_KEY_F15);
        map(KEYBOARD, "f16", GLFW.GLFW_KEY_F16);
        map(KEYBOARD, "f17", GLFW.GLFW_KEY_F17);
        map(KEYBOARD, "f18", GLFW.GLFW_KEY_F18);
        map(KEYBOARD, "f19", GLFW.GLFW_KEY_F19);
        map(KEYBOARD, "f20", GLFW.GLFW_KEY_F20);
        map(KEYBOARD, "f21", GLFW.GLFW_KEY_F21);
        map(KEYBOARD, "f22", GLFW.GLFW_KEY_F22);
        map(KEYBOARD, "f23", GLFW.GLFW_KEY_F23);
        map(KEYBOARD, "f24", GLFW.GLFW_KEY_F24);
        map(KEYBOARD, "f25", GLFW.GLFW_KEY_F25);
        map(KEYBOARD, "num.lock", GLFW.GLFW_KEY_NUM_LOCK);
        map(KEYBOARD, "keypad.0", GLFW.GLFW_KEY_KP_0);
        map(KEYBOARD, "keypad.1", GLFW.GLFW_KEY_KP_1);
        map(KEYBOARD, "keypad.2", GLFW.GLFW_KEY_KP_2);
        map(KEYBOARD, "keypad.3", GLFW.GLFW_KEY_KP_3);
        map(KEYBOARD, "keypad.4", GLFW.GLFW_KEY_KP_4);
        map(KEYBOARD, "keypad.5", GLFW.GLFW_KEY_KP_5);
        map(KEYBOARD, "keypad.6", GLFW.GLFW_KEY_KP_6);
        map(KEYBOARD, "keypad.7", GLFW.GLFW_KEY_KP_7);
        map(KEYBOARD, "keypad.8", GLFW.GLFW_KEY_KP_8);
        map(KEYBOARD, "keypad.9", GLFW.GLFW_KEY_KP_9);
        map(KEYBOARD, "keypad.add", GLFW.GLFW_KEY_KP_ADD);
        map(KEYBOARD, "keypad.decimal", GLFW.GLFW_KEY_KP_DECIMAL);
        map(KEYBOARD, "keypad.enter", GLFW.GLFW_KEY_KP_ENTER);
        map(KEYBOARD, "keypad.equal", GLFW.GLFW_KEY_KP_EQUAL);
        map(KEYBOARD, "keypad.multiply", GLFW.GLFW_KEY_KP_MULTIPLY);
        map(KEYBOARD, "keypad.divide", GLFW.GLFW_KEY_KP_DIVIDE);
        map(KEYBOARD, "keypad.subtract", GLFW.GLFW_KEY_KP_SUBTRACT);
        map(KEYBOARD, "down", GLFW.GLFW_KEY_DOWN);
        map(KEYBOARD, "left", GLFW.GLFW_KEY_LEFT);
        map(KEYBOARD, "right", GLFW.GLFW_KEY_RIGHT);
        map(KEYBOARD, "up", GLFW.GLFW_KEY_UP);
        map(KEYBOARD, "apostrophe", GLFW.GLFW_KEY_APOSTROPHE);
        map(KEYBOARD, "backslash", GLFW.GLFW_KEY_BACKSLASH);
        map(KEYBOARD, "comma", GLFW.GLFW_KEY_COMMA);
        map(KEYBOARD, "equal", GLFW.GLFW_KEY_EQUAL);
        map(KEYBOARD, "grave.accent", GLFW.GLFW_KEY_GRAVE_ACCENT);
        map(KEYBOARD, "left.bracket", GLFW.GLFW_KEY_LEFT_BRACKET);
        map(KEYBOARD, "minus", GLFW.GLFW_KEY_MINUS);
        map(KEYBOARD, "period", GLFW.GLFW_KEY_PERIOD);
        map(KEYBOARD, "right.bracket", GLFW.GLFW_KEY_RIGHT_BRACKET);
        map(KEYBOARD, "semicolon", GLFW.GLFW_KEY_SEMICOLON);
        map(KEYBOARD, "slash", GLFW.GLFW_KEY_SLASH);
        map(KEYBOARD, "space", GLFW.GLFW_KEY_SPACE);
        map(KEYBOARD, "tab", GLFW.GLFW_KEY_TAB);
        map(KEYBOARD, "left.alt", GLFW.GLFW_KEY_LEFT_ALT);
        map(KEYBOARD, "left.control", GLFW.GLFW_KEY_LEFT_CONTROL);
        map(KEYBOARD, "left.shift", GLFW.GLFW_KEY_LEFT_SHIFT);
        map(KEYBOARD, "left.win", GLFW.GLFW_KEY_LEFT_SUPER);
        map(KEYBOARD, "right.alt", GLFW.GLFW_KEY_RIGHT_ALT);
        map(KEYBOARD, "right.control", GLFW.GLFW_KEY_RIGHT_CONTROL);
        map(KEYBOARD, "right.shift", GLFW.GLFW_KEY_RIGHT_SHIFT);
        map(KEYBOARD, "right.win", GLFW.GLFW_KEY_RIGHT_SUPER);
        map(KEYBOARD, "enter", GLFW.GLFW_KEY_ENTER);
        map(KEYBOARD, "escape", GLFW.GLFW_KEY_ESCAPE);
        map(KEYBOARD, "backspace", GLFW.GLFW_KEY_BACKSPACE);
        map(KEYBOARD, "delete", GLFW.GLFW_KEY_DELETE);
        map(KEYBOARD, "end", GLFW.GLFW_KEY_END);
        map(KEYBOARD, "home", GLFW.GLFW_KEY_HOME);
        map(KEYBOARD, "insert", GLFW.GLFW_KEY_INSERT);
        map(KEYBOARD, "page.down", GLFW.GLFW_KEY_PAGE_DOWN);
        map(KEYBOARD, "page.up", GLFW.GLFW_KEY_PAGE_UP);
        map(KEYBOARD, "caps.lock", GLFW.GLFW_KEY_CAPS_LOCK);
        map(KEYBOARD, "pause", GLFW.GLFW_KEY_PAUSE);
        map(KEYBOARD, "scroll.lock", GLFW.GLFW_KEY_SCROLL_LOCK);
        map(KEYBOARD, "menu", GLFW.GLFW_KEY_MENU);
        map(KEYBOARD, "print.screen", GLFW.GLFW_KEY_PRINT_SCREEN);
        map(KEYBOARD, "world.1", GLFW.GLFW_KEY_WORLD_1);
        map(KEYBOARD, "world.2", GLFW.GLFW_KEY_WORLD_2);
        mouseButtons = new String[]{"left", "middle", "right"};
    }
}