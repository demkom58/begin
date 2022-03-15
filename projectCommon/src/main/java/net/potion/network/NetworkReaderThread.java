package net.potion.network;

class NetworkReaderThread extends Thread {
    private final NetworkManager networkManager;

    NetworkReaderThread(NetworkManager networkManager, String name) {
        super(name);
        this.networkManager = networkManager;
    }

    @Override
    public void run() {
        synchronized (NetworkManager.threadSyncObject) {
            ++NetworkManager.numReadThreads;
        }

        while (true) {
            if (!NetworkManager.isRunning(this.networkManager))
                break;

            if (NetworkManager.isServerTerminating(this.networkManager))
                break;

            while (NetworkManager.readNetworkPacket(this.networkManager)) { }

            try {
                sleep(100L);
            } catch (InterruptedException ignored) { }

        }

        synchronized (NetworkManager.threadSyncObject) {
            --NetworkManager.numReadThreads;
        }
    }
}
