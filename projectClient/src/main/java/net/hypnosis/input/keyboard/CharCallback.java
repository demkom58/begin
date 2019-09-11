package net.hypnosis.input.keyboard;

public interface CharCallback {
    /**
     * Will be called when a Unicode character is input.
     *
     * @param window    the window that received the event
     * @param codepoint the Unicode code point of the character
     */
    void onChar(long window, int codepoint);
}
