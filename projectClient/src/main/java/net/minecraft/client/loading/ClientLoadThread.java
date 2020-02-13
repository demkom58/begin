package net.minecraft.client.loading;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URL;
import java.util.Objects;

public class ClientLoadThread extends Thread {
    public File resourcesFolder;

    private Minecraft mc;
    private boolean closing = false;
    private LoadingModel loadingModel;

    private int loadUnits = -1;

    public ClientLoadThread(File root, Minecraft mc) {
        this.mc = mc;
        this.setName("Client Load Thread");
        this.setDaemon(true);
        this.resourcesFolder = new File(root, "resources/");

        if (!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs())
            throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
    }

    public void setup(LoadingModel loadingModel) {
        this.loadingModel = loadingModel;
    }

    public int countLoadUnits() {
        if (loadUnits == -1)
            loadUnits = countLoadResource(this.resourcesFolder, "");

        return loadUnits;
    }

    @Override
    public void run() {
      /*try {
            URL var1 = new URL("http://s3.amazonaws.com/MinecraftResources/");
            DocumentBuilderFactory var2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder var3 = var2.newDocumentBuilder();
            Document var4 = var3.parse(var1.openStream());
            NodeList var5 = var4.getElementsByTagName("Contents");

            for (int var6 = 0; var6 < 2; ++var6) {
                for (int var7 = 0; var7 < var5.getLength(); ++var7) {
                    Node var8 = var5.item(var7);
                    if (var8.getNodeType() == 1) {
                        Element var9 = (Element) var8;
                        String var10 = var9.getElementsByTagName("Key").item(0).getChildNodes().item(0).getNodeValue();
                        long var11 = Long.parseLong(var9.getElementsByTagName("Size").item(0).getChildNodes().item(0).getNodeValue());
                        if (var11 > 0L) {
                            this.downloadAndInstallResource(var1, var10, var11, var6);
                            if (this.closing) {
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            this.loadResource(this.resourcesFolder, "");
            e.printStackTrace();
        }*/

        this.loadResource(this.resourcesFolder, "");
        loadingModel.setDone(true);
    }

    public void reloadResources() {
        this.loadResource(this.resourcesFolder, "");
    }

    private void loadResource(File resourceFolder, String resource) {
        File[] files = Objects.requireNonNull(resourceFolder.listFiles());

        for (int i = 0; i < files.length; ++i) {
            if (files[i].isDirectory()) {
                this.loadResource(files[i], resource + files[i].getName() + "/");
                continue;
            }

            try {
                this.mc.installResource(resource + files[i].getName(), files[i]);
            } catch (Exception e) {
                System.out.println("Failed to add " + resource + files[i].getName());
                e.printStackTrace();
            }

            loadingModel.addLoadUnits(1);
            loadingModel.setTitle("Loading \"" + resource + files[i].getName() + "\"");
        }

    }

    private int countLoadResource(File resourceFolder, String resource) {
        int loadUnits = 0;
        File[] files = Objects.requireNonNull(resourceFolder.listFiles());

        for (int i = 0; i < files.length; ++i) {
            if (files[i].isDirectory()) {
                loadUnits += this.countLoadResource(files[i], resource + files[i].getName() + "/");
                continue;
            }

            loadUnits++;
        }

        return loadUnits;
    }


    private void downloadAndInstallResource(URL var1, String var2, long var3, int var5) {
        try {
            int var6 = var2.indexOf("/");
            String var7 = var2.substring(0, var6);
            if (!var7.equals("sound") && !var7.equals("newsound")) {
                if (var5 != 1) {
                    return;
                }
            } else if (var5 != 0) {
                return;
            }

            File var8 = new File(this.resourcesFolder, var2);
            if (!var8.exists() || var8.length() != var3) {
                var8.getParentFile().mkdirs();
                String var9 = var2.replaceAll(" ", "%20");
                this.downloadResource(new URL(var1, var9), var8, var3);
                if (this.closing) {
                    return;
                }
            }

            this.mc.installResource(var2, var8);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void downloadResource(URL var1, File var2, long var3) throws IOException {
        byte[] var5 = new byte[4096];
        DataInputStream var6 = new DataInputStream(var1.openStream());
        DataOutputStream var7 = new DataOutputStream(new FileOutputStream(var2));
        int var8 = 0;

        while ((var8 = var6.read(var5)) >= 0) {
            var7.write(var5, 0, var8);
            if (this.closing) {
                return;
            }
        }

        var6.close();
        var7.close();
    }

    public void closeMinecraft() {
        this.closing = true;
    }
}
