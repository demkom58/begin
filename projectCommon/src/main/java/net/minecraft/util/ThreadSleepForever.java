package net.minecraft.util;

public class ThreadSleepForever extends Thread {

    public ThreadSleepForever() {
        this.setDaemon(true);
        this.start();
    }

    public ThreadSleepForever(String name) {
        super(name);
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2147483647L);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
