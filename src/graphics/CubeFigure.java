package graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;

public class CubeFigure extends Figure {
    public CubeFigure(float width, float height, float depth) {
        _triangles = build(width, height, depth);
    }

    public static Triangle[] build(float width, float height, float depth) {
        ArrayList<Triangle> triangles = new ArrayList<>();

        // front
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(-width/2, -height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, -height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, height/2, -depth/2, 1.0f)),
                new Vector3f(0.0f, 0.0f, -1.0f)
        )));

        // back
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(-width/2, height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, -height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, -height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, height/2, depth/2, 1.0f)),
                new Vector3f(0.0f, 0.0f, 1.0f)
        )));

        // top
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(width/2, height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, height/2, -depth/2, 1.0f)),
                new Vector3f(0.0f, 1.0f, 0.0f)
        )));

        // bottom
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(width/2, -height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, -height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, -height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, -height/2, -depth/2, 1.0f)),
                new Vector3f(0.0f, -1.0f, 0.0f)
        )));

        // left
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(-width/2, -height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-width/2, -height/2, depth/2, 1.0f)),
                new Vector3f(-1.0f, 0.0f, 0.0f)
        )));

        // right
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(width/2, -height/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, -height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, height/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(width/2, height/2, depth/2, 1.0f)),
                new Vector3f(1.0f, 0.0f, 0.0f)
        )));

        return triangles.toArray(new Triangle[triangles.size()]);
    }
}
