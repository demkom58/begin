package net.minecraft;

import java.io.IOException;

class NetworkWriterThread extends Thread {
    private final NetworkManager networkManager;

    NetworkWriterThread(NetworkManager networkManager, String name) {
        super(name);
        this.networkManager = networkManager;
    }

    public void run() {
        synchronized (NetworkManager.threadSyncObject) {
            ++NetworkManager.numWriteThreads;
        }

        while (NetworkManager.isRunning(this.networkManager)) {
            while (NetworkManager.sendNetworkPacket(this.networkManager)) { }

            try {
                sleep(100L);
            } catch (InterruptedException e) { }

            try {
                if (NetworkManager.func_28136_f(this.networkManager) != null) {
                    NetworkManager.func_28136_f(this.networkManager).flush();
                }
            } catch (IOException e) {
                if (!NetworkManager.func_28135_e(this.networkManager)) {
                    NetworkManager.func_30007_a(this.networkManager, e);
                }

                e.printStackTrace();
            }
        }

        synchronized (NetworkManager.threadSyncObject) {
            --NetworkManager.numWriteThreads;
        }

    }
}
