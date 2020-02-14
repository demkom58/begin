package net.potion.client.render.texture;

import net.potion.client.PotionClient;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TexturePackDefault extends TexturePackBase {
    private int texturePackName = -1;
    private BufferedImage texturePackThumbnail;

    public TexturePackDefault() {
        this.texturePackFileName = "Default";
        this.firstDescriptionLine = "The default look of Potion";

        try {
            this.texturePackThumbnail = ImageIO.read(TexturePackDefault.class.getResource("/pack.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void func_6484_b(PotionClient var1) {
        if (this.texturePackThumbnail != null) {
            var1.renderEngine.deleteTexture(this.texturePackName);
        }

    }

    @Override
    public void bindThumbnailTexture(PotionClient var1) {
        if (this.texturePackThumbnail != null && this.texturePackName < 0) {
            this.texturePackName = var1.renderEngine.allocateAndSetupTexture(this.texturePackThumbnail);
        }

        if (this.texturePackThumbnail != null) {
            var1.renderEngine.bindTexture(this.texturePackName);
        } else {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.renderEngine.getTexture("/gui/unknown_pack.png"));
        }

    }
}
