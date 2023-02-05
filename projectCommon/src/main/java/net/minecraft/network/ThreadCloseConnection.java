package net.minecraft.network;

class ThreadCloseConnection extends Thread {
    private final NetworkManager networkManager;

    ThreadCloseConnection(NetworkManager var1) {
        this.networkManager = var1;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000L);
            if (NetworkManager.isRunning(this.networkManager)) {
                NetworkManager.getWriteThread(this.networkManager).interrupt();
                this.networkManager.networkShutdown("disconnect.closed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
