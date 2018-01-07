package graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;

public class PerspectiveFigure extends Figure {
    public PerspectiveFigure(Matrix4f m) {
        float fov = m.perspectiveFov();
        float near = m.perspectiveNear();
        float far = m.perspectiveFar();

        float depth = far - near;
        float nearSize = 2.0f * near * (float)Math.tan(fov / 2);
        float farSize = 2.0f * far * (float)Math.tan(fov / 2);

        ArrayList<Triangle> triangles = new ArrayList<>();
        // front
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(-nearSize/2, -nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(nearSize/2, -nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(nearSize/2, nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-nearSize/2, nearSize/2, depth/2, 1.0f)),
                new Vector3f(0.0f, 0.0f, -1.0f)
        )));

        // back
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(-farSize/2, farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(-farSize/2, -farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(farSize/2, -farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(farSize/2, farSize/2, -depth/2, 1.0f)),
                new Vector3f(0.0f, 0.0f, 1.0f)
        )));

        // top
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(nearSize/2, nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-nearSize/2, nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-farSize/2, farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(farSize/2, farSize/2, -depth/2, 1.0f)),
                new Vector3f(0.0f, 1.0f, 0.0f)
        )));

        // bottom
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(farSize/2, -farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(nearSize/2, -nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-nearSize/2, -nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-farSize/2, -farSize/2, -depth/2, 1.0f)),
                new Vector3f(0.0f, -1.0f, 0.0f)
        )));

        // left
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(-farSize/2, -farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(-farSize/2, farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(-nearSize/2, nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(-nearSize/2, -nearSize/2, depth/2, 1.0f)),
                new Vector3f(-1.0f, 0.0f, 0.0f)
        )));

        // right
        triangles.addAll(Arrays.asList(QuadFigure.build(
                new Vertex(new Vector4f(nearSize/2, -nearSize/2, depth/2, 1.0f)),
                new Vertex(new Vector4f(farSize/2, -farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(farSize/2, farSize/2, -depth/2, 1.0f)),
                new Vertex(new Vector4f(nearSize/2, nearSize/2, depth/2, 1.0f)),
                new Vector3f(1.0f, 0.0f, 0.0f)
        )));

        _triangles = triangles.toArray(new Triangle[triangles.size()]);
        for(int i = 0; i < _triangles.length; i++)
            for(int j = 0; j < _triangles[i].vertices.length; j++)
                _triangles[i].vertices[j].position.add(0.0f, 0.0f, -depth/2 - near, 0.0f);
    }
}
