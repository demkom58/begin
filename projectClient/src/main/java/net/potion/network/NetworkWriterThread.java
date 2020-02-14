package net.potion.network;

import java.io.IOException;

class NetworkWriterThread extends Thread {
    private final NetworkManager netManager;

    NetworkWriterThread(NetworkManager networkManager, String var2) {
        super(var2);
        this.netManager = networkManager;
    }

    @Override
    public void run() {
        synchronized (NetworkManager.threadSyncObject) {
            ++NetworkManager.numWriteThreads;
        }

        while (true) {
            boolean var13 = false;

            try {
                var13 = true;
                if (!NetworkManager.isRunning(this.netManager)) {
                    var13 = false;
                    break;
                }

                while (NetworkManager.sendNetworkPacket(this.netManager)) { }

                try {
                    sleep(100L);
                } catch (InterruptedException ignored) {
                }

                try {
                    if (NetworkManager.func_28140_f(this.netManager) != null) {
                        NetworkManager.func_28140_f(this.netManager).flush();
                    }
                } catch (IOException e) {
                    if (!NetworkManager.func_28138_e(this.netManager)) {
                        NetworkManager.func_30005_a(this.netManager, e);
                    }

                    e.printStackTrace();
                }
            } finally {
                if (var13) {
                    synchronized (NetworkManager.threadSyncObject) {
                        --NetworkManager.numWriteThreads;
                    }
                }
            }
        }

        synchronized (NetworkManager.threadSyncObject) {
            --NetworkManager.numWriteThreads;
        }
    }
}
