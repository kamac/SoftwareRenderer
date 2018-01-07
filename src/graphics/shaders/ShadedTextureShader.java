package graphics.shaders;

import graphics.Vertex;
import graphics.textures.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Random;

public class ShadedTextureShader extends ShadedColorShader {
    protected Texture _texture;

    public ShadedTextureShader(List<Light> lights, Vector3f ambientLight, Texture texture) {
        super(lights, ambientLight);
        _texture = texture;
    }

    @Override
    public Vector4f fragment(Vector4f color, Vector2f texcoords, Vector3f normal, Vector2f screenUV) {
        Vector4f c = tex2d(texcoords, _texture);
        return super.fragment(c, texcoords, normal, screenUV);
    }
}