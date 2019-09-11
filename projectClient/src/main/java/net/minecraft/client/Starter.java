package net.minecraft.client;

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
        Minecraft minecraft = new Minecraft(854, 480, false);
        minecraft.minecraftUri = "www.minecraft.net";

        if (username != null && sessionId != null)
            minecraft.session = new Session(username, sessionId);
        else
            minecraft.session = new Session("Player" + System.currentTimeMillis() % 1000L, "");

        if (connectionIp != null) {
            String[] addressArr = connectionIp.split(":");
            minecraft.setServer(addressArr[0], Integer.parseInt(addressArr[1]));
        }

        Thread thread = new Thread(minecraft, "Minecraft main thread");
        thread.setPriority(10);
        thread.start();
    }

}