package graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Vertex {
    public final Vector4f position;
    public final Vector2f texcoords;

    public Vertex(Vector4f pos) {
        position = pos;
        texcoords = null;
    }

    public Vertex(Vector4f pos, Vector2f texcoords) {
        position = pos;
        this.texcoords = texcoords;
    }

    public Vertex(Vertex other) {
        position = new Vector4f(other.position);
        texcoords = other.texcoords != null ? new Vector2f(other.texcoords) : null;
    }

    public Vertex lerp(Vertex other, float t) {
        Vector4f lerpedPos = new Vector4f(position).lerp(other.position, t);
        Vector2f lerpedTexcoords = null;
        if(this.texcoords != null && other.texcoords != null) {
            Vector2f thisTexcoords = this.texcoords != null ? this.texcoords : new Vector2f(0.0f, 0.0f);
            Vector2f otherTexcoords = other.texcoords != null ? other.texcoords : new Vector2f(0.0f, 0.0f);
            lerpedTexcoords = new Vector2f(thisTexcoords).lerp(otherTexcoords, t);
        }
        return new Vertex(lerpedPos, lerpedTexcoords);
    }
}
