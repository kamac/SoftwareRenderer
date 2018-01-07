package graphics.shaders;

import graphics.Triangle;
import graphics.Vertex;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ColorShader extends Shader {
    @Override
    public Vertex vertex(Vertex v, Vector3f normal) {
        return new Vertex(_mvp.transform(new Vector4f(v.position)), v.texcoords);
    }

    @Override
    public Vector4f fragment(Vector4f color, Vector2f texcoords, Vector3f normal, Vector2f screenUV) {
        return color;
    }
}
