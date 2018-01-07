package graphics.textures;

import org.joml.Vector4f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DepthTexture extends Texture {
    float[] _depthBuffer;
    int width, height;

    public DepthTexture(int width, int height) {
        super(ColorType.Depth);
        _depthBuffer = new float[width * height];
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public BufferedImage getImage() {
        RGBTexture t = new RGBTexture(getWidth(), getHeight());
        for(int y = 0; y < getHeight(); y++)
            for(int x = 0; x < getWidth(); x++)
                t.setColor(x, y, getColor(x, y));
        return t.getImage();
    }

    @Override
    public Vector4f getColor(int x, int y) {
        float c = Math.min(Math.abs(getDepth(x, y)) / 255.0f, 1.0f);
        return new Vector4f(c, c, c, 1.0f);
    }

    @Override
    public void setColor(int x, int y, Vector4f color) {
        if(x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
            return;
        setDepth(x, y, color.x);
    }

    public void setDepth(int x, int y, float z) {
        if(x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
            return;
        _depthBuffer[y*getWidth() + x] = z;
    }

    public float getDepth(int x, int y) {
        if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
            return Float.NEGATIVE_INFINITY;
        else {
            return _depthBuffer[y * getWidth() + x];
        }
    }

    @Override
    public void clear() {
        Arrays.fill(_depthBuffer, Float.NEGATIVE_INFINITY);
    }
}
