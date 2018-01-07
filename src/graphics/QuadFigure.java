package graphics;

import org.joml.Vector3f;

public class QuadFigure extends Figure {
    public QuadFigure(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        _triangles = QuadFigure.build(v1, v2, v3, v4);
    }

    public static Triangle[] build(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        Triangle[] triangles = new Triangle[2];
        triangles[0] = new Triangle(v1, v2, v3);
        triangles[1] = new Triangle(v3, v4, v1);
        return triangles;
    }

    public static Triangle[] build(Vertex v1, Vertex v2, Vertex v3, Vertex v4, Vector3f normal) {
        Triangle[] triangles = new Triangle[2];
        triangles[0] = new Triangle(v1, v2, v3, normal);
        triangles[1] = new Triangle(v3, v4, v1, normal);
        return triangles;
    }
}
