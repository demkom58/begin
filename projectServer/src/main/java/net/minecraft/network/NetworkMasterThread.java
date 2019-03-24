package net.minecraft.network;

class NetworkMasterThread extends Thread {
    // $FF: synthetic field
    final NetworkManager netManager;

    NetworkMasterThread(NetworkManager var1) {
        this.netManager = var1;
    }

    public void run() {
        try {
            Thread.sleep(5000L);
            if (NetworkManager.getReadThread(this.netManager).isAlive()) {
                try {
                    NetworkManager.getReadThread(this.netManager).stop();
                } catch (Throwable throwable) {
                }
            }

            if (NetworkManager.getWriteThread(this.netManager).isAlive()) {
                try {
                    NetworkManager.getWriteThread(this.netManager).stop();
                } catch (Throwable throwable) {
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
