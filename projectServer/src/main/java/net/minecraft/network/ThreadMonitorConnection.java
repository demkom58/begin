package net.minecraft.network;

class ThreadMonitorConnection extends Thread {
    // $FF: synthetic field
    final NetworkManager netManager;

    ThreadMonitorConnection(NetworkManager var1) {
        this.netManager = var1;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000L);
            if (NetworkManager.isRunning(this.netManager)) {
                NetworkManager.getWriteThread(this.netManager).interrupt();
                this.netManager.networkShutdown("disconnect.closed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
