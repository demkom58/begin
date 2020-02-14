package net.potion.server;

public final class ThreadServerApplication extends Thread {
    // $FF: synthetic field
    final PotionServer server;

    public ThreadServerApplication(String var1, PotionServer server) {
        super(var1);
        this.server = server;
    }

    @Override
    public void run() {
        this.server.run();
    }
}
