package net.potion.client;

public final class Starter {

    public static void main(String[] args) {
        String username = args.length > 0 ? args[0] : "Player" + System.currentTimeMillis() % 1000L;
        String sessionId = args.length > 1 ? args[1] : "-";
        Starter.start(username, sessionId);
    }

    public static void start(String username, String sessionId) {
        Starter.start(username, sessionId, null);
    }

    public static void start(String username, String sessionId, String connectionIp) {
        PotionClient potion = new PotionClient(854, 480, false);
        potion.potionUri = "www.potion.net";

        if (username != null && sessionId != null)
            potion.session = new Session(username, sessionId);
        else
            potion.session = new Session("Player" + System.currentTimeMillis() % 1000L, "");

        if (connectionIp != null) {
            String[] addressArr = connectionIp.split(":");
            potion.setServer(addressArr[0], Integer.parseInt(addressArr[1]));
        }

        Thread thread = new Thread(potion, "Potion main thread");
        thread.setPriority(10);
        thread.start();
    }

}