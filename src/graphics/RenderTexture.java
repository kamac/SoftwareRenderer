package graphics;

import graphics.textures.DepthTexture;
import graphics.textures.RGBTexture;
import graphics.textures.Texture;

public class RenderTexture {
    private Texture _tex;
    private DepthTexture _depthTexture;

    public RenderTexture(int width, int height) {
        _tex = new RGBTexture(width, height);
        _depthTexture = new DepthTexture(width, height);
    }

    public int getWidth() {
        return _tex.getWidth();
    }

    public int getHeight() {
        return _tex.getHeight();
    }

    public void clear() {
        _tex.clear();
        _depthTexture.clear();
    }

    public Texture getTexture() {
        return _tex;
    }

    public DepthTexture getDepthTexture() {
        return _depthTexture;
    }
}
