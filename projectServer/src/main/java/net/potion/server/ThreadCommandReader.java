package net.potion.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThreadCommandReader extends Thread {
    // $FF: synthetic field
    final PotionServer server;

    public ThreadCommandReader(PotionServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));
        String var2 = null;

        try {
            while (!this.server.serverStopped && PotionServer.isServerRunning(this.server) && (var2 = var1.readLine()) != null) {
                this.server.addCommand(var2, this.server);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
