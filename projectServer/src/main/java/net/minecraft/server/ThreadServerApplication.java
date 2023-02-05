package net.minecraft.server;

public final class ThreadServerApplication extends Thread {
    // $FF: synthetic field
    final MinecraftServer server;

    public ThreadServerApplication(String var1, MinecraftServer server) {
        super(var1);
        this.server = server;
    }

    @Override
    public void run() {
        this.server.run();
    }
}
