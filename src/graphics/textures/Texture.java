package graphics.textures;

import com.sun.prism.PixelFormat;
import org.joml.Vector4f;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.Arrays;

public abstract class Texture {
    public enum ColorType {
        RGB, Depth
    }

    ColorType _type;

    public Texture(ColorType type) {
        _type = type;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract BufferedImage getImage();

    public abstract Vector4f getColor(int x, int y);

    public abstract void setColor(int x, int y, Vector4f color);

    public abstract void clear();
}
