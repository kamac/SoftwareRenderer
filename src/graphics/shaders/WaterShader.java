package graphics.shaders;

import graphics.Triangle;
import graphics.Vertex;
import graphics.textures.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class WaterShader extends Shader {
    public Texture reflection;

    @Override
    public Vertex vertex(Vertex v, Vector3f normal) {
        return new Vertex(_mvp.transform(new Vector4f(v.position)), v.texcoords);
    }

    @Override
    public Vector4f fragment(Vector4f color, Vector2f texcoords, Vector3f normal, Vector2f screenUV) {
        Vector4f c = tex2d(screenUV, reflection);
        c.x -= 0.1f;
        c.y -= 0.1f;
        c.z += 0.2f;
        if(c.z > 1.0f)
            c.z = 1.0f;
        if(c.x < 0.0f)
            c.x = 0.0f;
        if(c.y < 0.0f)
            c.y = 0.0f;
        return c;
    }
}
