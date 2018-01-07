package graphics.textures;

import org.joml.Vector4f;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class RGBTexture extends Texture {
    BufferedImage _img;

    public RGBTexture(int width, int height) {
        super(ColorType.RGB);
        _img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public RGBTexture(File file) {
        super(ColorType.RGB);
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _img = img;
    }

    @Override
    public int getWidth() {
        return _img.getWidth();
    }

    @Override
    public int getHeight() {
        return _img.getHeight();
    }

    @Override
    public BufferedImage getImage() {
        return _img;
    }

    @Override
    public Vector4f getColor(int x, int y) {
        Color c = new Color(_img.getRGB(x, y));
        return new Vector4f(
                (float)c.getRed() / 255,
                (float)c.getGreen() / 255,
                (float)c.getBlue() / 255,
                (float)c.getAlpha() / 255
        );
    }

    @Override
    public void setColor(int x, int y, Vector4f color) {
        if(x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
            return;
        int r = (int)(color.x*255), g = (int)(color.y*255), b = (int)(color.z*255);
        _img.setRGB(x, y, new Color(
                r, g, b
        ).getRGB());
    }

    @Override
    public void clear() {
        Graphics2D g = (Graphics2D) this._img.getGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setComposite(AlphaComposite.SrcOver);
    }
}
