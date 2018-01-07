package graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Triangle {
    public final Vertex[] vertices;
    public Vector4f color;
    public Vector3f normal;

    public Triangle(Vertex p1, Vertex p2, Vertex p3) {
        this.vertices = new Vertex[3];
        this.vertices[0] = new Vertex(p1);
        this.vertices[1] = new Vertex(p2);
        this.vertices[2] = new Vertex(p3);
        this.color = new Vector4f(1.0f, 0.0f, 1.0f, 1.0f);
        this.normal = new Vector3f(0.0f, 1.0f, 0.0f);
    }

    public Triangle(Vertex p1, Vertex p2, Vertex p3, Vector4f color) {
        this.vertices = new Vertex[3];
        this.vertices[0] = new Vertex(p1);
        this.vertices[1] = new Vertex(p2);
        this.vertices[2] = new Vertex(p3);
        this.color = color;
        this.normal = new Vector3f(0.0f, 1.0f, 0.0f);
    }

    public Triangle(Vertex p1, Vertex p2, Vertex p3, Vector3f normal) {
        this.vertices = new Vertex[3];
        this.vertices[0] = new Vertex(p1);
        this.vertices[1] = new Vertex(p2);
        this.vertices[2] = new Vertex(p3);
        this.color = new Vector4f(1.0f, 0.0f, 1.0f, 1.0f);
        this.normal = normal;
    }

    public Triangle(Vertex p1, Vertex p2, Vertex p3, Vector3f normal, Vector4f color) {
        this.vertices = new Vertex[3];
        this.vertices[0] = new Vertex(p1);
        this.vertices[1] = new Vertex(p2);
        this.vertices[2] = new Vertex(p3);
        this.color = color;
        this.normal = normal;
    }

    public Triangle(Triangle other) {
        this.vertices = new Vertex[3];
        this.vertices[0] = new Vertex(other.vertices[0]);
        this.vertices[1] = new Vertex(other.vertices[1]);
        this.vertices[2] = new Vertex(other.vertices[2]);
        this.color = new Vector4f(other.color);
        this.normal = new Vector3f(other.normal);
    }
}
