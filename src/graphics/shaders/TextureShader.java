package graphics.shaders;

import graphics.Vertex;
import graphics.textures.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Random;

public class TextureShader extends ColorShader {
    protected Texture _texture;

    public TextureShader(Texture texture) {
        _texture = texture;
    }

    @Override
    public Vector4f fragment(Vector4f color, Vector2f texcoords, Vector3f normal, Vector2f screenUV) {
        return tex2d(texcoords, _texture);
    }
}