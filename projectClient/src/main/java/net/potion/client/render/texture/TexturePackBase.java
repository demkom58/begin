package net.potion.client.render.texture;

import net.potion.client.PotionClient;

import java.io.IOException;
import java.io.InputStream;

public abstract class TexturePackBase {
    public String texturePackFileName;
    public String firstDescriptionLine;
    public String secondDescriptionLine;
    public String field_6488_d;

    public void func_6482_a() {
    }

    public void closeTexturePackFile() {
    }

    public void func_6485_a(PotionClient var1) throws IOException {
    }

    public void func_6484_b(PotionClient var1) {
    }

    public void bindThumbnailTexture(PotionClient var1) {
    }

    public InputStream getResourceAsStream(String var1) {
        return TexturePackBase.class.getResourceAsStream(var1);
    }
}
