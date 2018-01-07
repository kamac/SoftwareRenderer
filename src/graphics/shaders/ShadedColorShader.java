package graphics.shaders;

import graphics.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Random;

public class ShadedColorShader extends Shader {
    public enum LightType {
        Directional, Point
    }

    public static class Light {
        public Vector3f position, direction, color;
        public float intensity;
        public LightType type;

        public Light(Vector3f position, Vector3f direction, Vector3f color, float intensity, LightType type) {
            this.position = position;
            this.direction = direction;
            this.color = color;
            this.intensity = intensity;
            this.type = type;
        }
    }

    protected final List<Light> _lights;
    protected final Vector3f _ambientLight;

    public ShadedColorShader(List<Light> lights, Vector3f ambientLight) {
        _lights = lights;
        _ambientLight = ambientLight;
    }

    @Override
    public Vertex vertex(Vertex v, Vector3f normal) {
        return new Vertex(_mvp.transform(new Vector4f(v.position)), v.texcoords);
    }

    @Override
    public Vector4f fragment(Vector4f color, Vector2f texcoords, Vector3f normal, Vector2f screenUV) {
        Vector3f c = new Vector3f(color.x, color.y, color.z);
        for(int i = 0; i < _lights.size(); i++) {
            Light light = _lights.get(i);
            if(light.type == LightType.Directional) {
                float strength = Math.max(Math.min(new Vector3f(normal).dot(light.direction) * light.intensity, 1.0f), 0.0f);
                c.mul(new Vector3f(_ambientLight).lerp(light.color, strength));
            }
        }
        return new Vector4f(c.x, c.y, c.z, color.w);
    }
}