package net.minecraft.client.render.texture;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TexturePackCustom extends TexturePackBase {
    private ZipFile texturePackZipFile;
    private int texturePackName = -1;
    private BufferedImage texturePackThumbnail;
    private File texturePackFile;

    public TexturePackCustom(File var1) {
        this.texturePackFileName = var1.getName();
        this.texturePackFile = var1;
    }

    private String truncateString(String var1) {
        if (var1 != null && var1.length() > 34) {
            var1 = var1.substring(0, 34);
        }

        return var1;
    }

    public void func_6485_a(Minecraft var1) throws IOException {
        ZipFile var2 = null;
        InputStream var3 = null;

        try {
            var2 = new ZipFile(this.texturePackFile);

            try {
                var3 = var2.getInputStream(var2.getEntry("pack.txt"));
                BufferedReader var4 = new BufferedReader(new InputStreamReader(var3));
                this.firstDescriptionLine = this.truncateString(var4.readLine());
                this.secondDescriptionLine = this.truncateString(var4.readLine());
                var4.close();
                var3.close();
            } catch (Exception e) { }

            try {
                var3 = var2.getInputStream(var2.getEntry("pack.png"));
                this.texturePackThumbnail = ImageIO.read(var3);
                var3.close();
            } catch (Exception e) { }

            var2.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                var3.close();
            } catch (Exception e) { }

            try {
                var2.close();
            } catch (Exception e) { }

        }

    }

    public void func_6484_b(Minecraft var1) {
        if (this.texturePackThumbnail != null) {
            var1.renderEngine.deleteTexture(this.texturePackName);
        }

        this.closeTexturePackFile();
    }

    public void bindThumbnailTexture(Minecraft var1) {
        if (this.texturePackThumbnail != null && this.texturePackName < 0) {
            this.texturePackName = var1.renderEngine.allocateAndSetupTexture(this.texturePackThumbnail);
        }

        if (this.texturePackThumbnail != null) {
            var1.renderEngine.bindTexture(this.texturePackName);
        } else {
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var1.renderEngine.getTexture("/gui/unknown_pack.png"));
        }

    }

    public void func_6482_a() {
        try {
            this.texturePackZipFile = new ZipFile(this.texturePackFile);
        } catch (Exception e) { }

    }

    public void closeTexturePackFile() {
        try {
            this.texturePackZipFile.close();
        } catch (Exception e) { }

        this.texturePackZipFile = null;
    }

    public InputStream getResourceAsStream(String var1) {
        try {
            ZipEntry var2 = this.texturePackZipFile.getEntry(var1.substring(1));
            if (var2 != null) {
                return this.texturePackZipFile.getInputStream(var2);
            }
        } catch (Exception e) { }

        return TexturePackBase.class.getResourceAsStream(var1);
    }
}
