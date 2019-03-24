package net.minecraft.network;

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
                if (NetworkManager.getOutputStream(this.networkManager) != null) {
                    NetworkManager.getOutputStream(this.networkManager).flush();
                }
            } catch (IOException e) {
                if (!NetworkManager.isTerminating(this.networkManager)) {
                    NetworkManager.networkError(this.networkManager, e);
                }

                e.printStackTrace();
            }
        }

        synchronized (NetworkManager.threadSyncObject) {
            --NetworkManager.numWriteThreads;
        }

    }
}
