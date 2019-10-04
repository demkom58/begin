package net.minecraft.client.render;

import javax.imageio.ImageIO;
import java.net.HttpURLConnection;
import java.net.URL;

class ThreadDownloadImage extends Thread {
    // $FF: synthetic field
    final String location;
    // $FF: synthetic field
    final ImageBuffer buffer;
    // $FF: synthetic field
    final ThreadDownloadImageData imageData;

    ThreadDownloadImage(ThreadDownloadImageData var1, String var2, ImageBuffer var3) {
        this.imageData = var1;
        this.location = var2;
        this.buffer = var3;
    }

    @Override
    public void run() {
        HttpURLConnection var1 = null;

        try {
            URL var2 = new URL(this.location);
            var1 = (HttpURLConnection) var2.openConnection();
            var1.setDoInput(true);
            var1.setDoOutput(false);
            var1.connect();
            if (var1.getResponseCode() / 100 != 4) {
                if (this.buffer == null) {
                    this.imageData.image = ImageIO.read(var1.getInputStream());
                } else {
                    this.imageData.image = this.buffer.parseUserSkin(ImageIO.read(var1.getInputStream()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            var1.disconnect();
        }

    }
}
