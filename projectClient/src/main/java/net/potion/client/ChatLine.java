package net.potion.client;

public class ChatLine {
    public String message;
    public int updateCounter;

    public ChatLine(String message) {
        this.message = message;
        this.updateCounter = 0;
    }
}
