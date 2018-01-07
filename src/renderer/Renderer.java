package renderer;

import graphics.*;
import graphics.shaders.Shader;
import javafx.scene.image.PixelWriter;
import org.joml.*;

import java.awt.*;
import java.awt.image.WritableRaster;
import java.lang.Math;
import java.util.ArrayList;

public class Renderer {
    public enum DrawMode {
        Wireframe, Color
    }

    public DrawMode drawMode = DrawMode.Wireframe;

    private void clampLineToRT(Vector2i start, Vector2i end, RenderTexture rt) {
        if(start.x != end.x) {
            if (start.x < 0) {
                start.y += (end.y - start.y) * ((float) start.x / (start.x - end.x));
                start.x = 0;
            } else if (start.x > rt.getWidth()) {
                start.y += (end.y - start.y) * ((float) (start.x - rt.getWidth()) / (start.x - end.x));
                start.x = rt.getWidth();
            }
        }
        if(start.y != end.y) {
            if (start.y < 0) {
                start.x += (end.x - start.x) * ((float) start.y / (start.y - end.y));
                start.y = 0;
            } else if (start.y > rt.getHeight()) {
                start.x += (end.x - start.x) * ((float) (start.y - rt.getHeight()) / (start.y - end.y));
                start.y = rt.getHeight();
            }
        }
    }

    protected void drawLine(Vector2i start, Vector2i end, RenderTexture rt, Vector4f color) {
        start = new Vector2i(start);
        end = new Vector2i(end);
        boolean steep = false;
        if (Math.abs(start.x-end.x) < Math.abs(start.y-end.y)) {
            int tmp = start.x; start.x = start.y; start.y = tmp;
            tmp = end.x; end.x = end.y; end.y = tmp;
            steep = true;
        }
        if(start.x > end.x) {
            int tmp = start.x; start.x = end.x; end.x = tmp;
            tmp = start.y; start.y = end.y; end.y = tmp;
        }
        if((start.x < 0 && end.x < 0) || (start.x > rt.getWidth() && end.x > rt.getWidth()))
            return;
        if((start.y < 0 && end.y < 0) || (start.y > rt.getHeight() && end.y > rt.getHeight()))
            return;
        clampLineToRT(start, end, rt);
        clampLineToRT(end, start, rt);
        if((start.x < 0 && end.x < 0) || (start.x > rt.getWidth() && end.x > rt.getWidth()))
            return;
        if((start.y < 0 && end.y < 0) || (start.y > rt.getHeight() && end.y > rt.getHeight()))
            return;
        int dx = end.x - start.x;
        int dy = end.y - start.y;
        int derror2 = Math.abs(dy) * 2;
        int error2 = 0;
        int y = start.y;
        for (int x = start.x; x <= end.x; x++) {
            if (steep) {
                rt.getTexture().setColor(y, x, color);
            } else {
                rt.getTexture().setColor(x, y, color);
            }
            error2 += derror2;
            if (error2 > dx) {
                y += (end.y>start.y?1:-1);
                error2 -= dx*2;
            }
        }
    }

    private Vector3f barycentric(Vector3f[] vertices, Vector2i p) {
        Vector3f d1 = new Vector3f(vertices[2].x - vertices[0].x, vertices[1].x - vertices[0].x, vertices[0].x - p.x);
        Vector3f d2 = new Vector3f(vertices[2].y - vertices[0].y, vertices[1].y - vertices[0].y, vertices[0].y - p.y);
        d1.cross(d2);
        // triangle is degenerate, in this case return smth with negative coordinates
        if(Math.abs(d1.z) < 1) return new Vector3f(-1.0f, 1.0f, 1.0f);
        return new Vector3f(1.0f - (d1.x + d1.y) / d1.z, d1.y / d1.z, d1.x / d1.z);
    }

    protected void drawTriangle(Vector3f[] vertices, Vector4f color, Vector3f normal, Shader shader, RenderTexture rt) {
        Vector2f bboxmin = new Vector2f(rt.getWidth(),  rt.getHeight());
        Vector2f bboxmax = new Vector2f(0, 0);
        Vector2f clamp = new Vector2f(rt.getWidth(), rt.getHeight());
        for (int i=0; i<3; i++) {
            bboxmin.x = Math.max(0, Math.min(bboxmin.x, vertices[i].x));
            bboxmax.x = Math.min(clamp.x, Math.max(bboxmax.x, vertices[i].x));
            bboxmin.y = Math.max(0, Math.min(bboxmin.y, vertices[i].y));
            bboxmax.y = Math.min(clamp.y, Math.max(bboxmax.y, vertices[i].y));
        }
        Vector3f P = new Vector3f(0, 0, 0);
        for (P.x = bboxmin.x; P.x <= bboxmax.x; P.x++) {
            for (P.y = bboxmin.y; P.y <= bboxmax.y; P.y++) {
                Vector3f bcScreen = barycentric(vertices, new Vector2i((int)P.x, (int)P.y));
                if (bcScreen.x < 0 || bcScreen.y < 0 || bcScreen.z < 0) continue;
                P.z = 0;
                for (int i = 0; i < 3; i++)
                    P.z += vertices[i].z * bcScreen.get(i);
                if (rt.getDepthTexture().getDepth((int)P.x, (int)P.y) < P.z) {
                    rt.getDepthTexture().setDepth((int)P.x, (int)P.y, P.z);
                    bcScreen.div(-vertices[0].z, -vertices[1].z, -vertices[2].z);
                    bcScreen.div(bcScreen.x + bcScreen.y + bcScreen.z);
                    if(shader != null) {
                        Vector2f screenUV = new Vector2f((float)P.x / rt.getWidth(), 1.0f - (float)P.y / rt.getHeight());
                        rt.getTexture().setColor((int) P.x, (int) P.y, shader.fragment(bcScreen, color, normal, screenUV));
                    } else {
                        rt.getTexture().setColor((int) P.x, (int) P.y, color);
                    }
                }
            }
        }
    }

    private static Triangle[] clipTriangle(Triangle triangle) {
        int outLen = 0;
        int[] outIdxs = new int[2];
        for(int i = 0; i < triangle.vertices.length; i++) {
            if(triangle.vertices[i].position.w <= 0) {
                outIdxs[outLen] = i;
                outLen++;
            }
        }
        switch (outLen) {
            case 0:
                return new Triangle[] { triangle };
            case 1: {
                Vertex thisVertex = triangle.vertices[outIdxs[0]];
                Vertex nextVertex = triangle.vertices[(outIdxs[0] + 1) % triangle.vertices.length];
                Vertex previousVertex = triangle.vertices[(outIdxs[0] + 2) % triangle.vertices.length];
                Vertex lerpedToNext = nextVertex.lerp(thisVertex,
                        nextVertex.position.w / (nextVertex.position.w - thisVertex.position.w) - 0.001f);
                Vertex lerpedToPrevious = previousVertex.lerp(thisVertex,
                        previousVertex.position.w / (previousVertex.position.w - thisVertex.position.w) - 0.001f);
                return new Triangle[]{
                        new Triangle(lerpedToNext, nextVertex, previousVertex, triangle.normal, triangle.color),
                        new Triangle(lerpedToPrevious, lerpedToNext, previousVertex, triangle.normal, triangle.color)
                };
            }
            case 2: {
                Vertex firstViolator = triangle.vertices[outIdxs[0]];
                Vertex secondViolator = triangle.vertices[outIdxs[1]];
                Vertex safeVertex = null;
                for(int i = 0; i < 3; i++) {
                    if(outIdxs[0] != i && outIdxs[1] != i) {
                        safeVertex = triangle.vertices[i];
                        break;
                    }
                }
                Vertex lerpedToFirst = safeVertex.lerp(firstViolator,
                        safeVertex.position.w / (safeVertex.position.w - firstViolator.position.w) - 0.001f);
                Vertex lerpedToSecond = safeVertex.lerp(secondViolator,
                        safeVertex.position.w / (safeVertex.position.w - secondViolator.position.w) - 0.001f);
                return new Triangle[]{
                        new Triangle(safeVertex, lerpedToFirst, lerpedToSecond, triangle.normal, triangle.color)
                };
            }
            default: {
                return null;
            }
        }
    }

    public void draw(Transform camera, Matrix4f projection, RenderTexture rt, ArrayList<Figure> figures) {
        Matrix4f vp = new Matrix4f();
        projection.mul(camera.getMatrix(), vp);
        int w = rt.getWidth(), h = rt.getHeight();

        for(int i = 0; i < figures.size(); i++) {
            Figure f = figures.get(i);
            Matrix4f mvp = new Matrix4f();
            vp.mul(f.transform.getMatrix(), mvp);
            Triangle[] fTriangles = f.getTriangles();
            f.shader.setMatrices(f.transform.getMatrix(), camera.getMatrix(), vp, mvp);

            for(int j = 0; j < fTriangles.length; j++) {
                Triangle fTriangle = fTriangles[j];
                Vertex[] triangleVertices = fTriangle.vertices;

                Triangle transformed = new Triangle(
                        f.shader.vertex(triangleVertices[0], 0, fTriangle.normal),
                        f.shader.vertex(triangleVertices[1], 1, fTriangle.normal),
                        f.shader.vertex(triangleVertices[2], 2, fTriangle.normal),
                        fTriangle.normal,
                        fTriangles[j].color
                );

                if(Math.abs(transformed.vertices[0].position.z) < transformed.vertices[0].position.w
                        || Math.abs(transformed.vertices[1].position.z) < transformed.vertices[1].position.w
                        || Math.abs(transformed.vertices[2].position.z) < transformed.vertices[2].position.w) {
                    Triangle[] clipped = clipTriangle(transformed);
                    for(int k = 0; k < clipped.length; k++) {
                        Triangle clippedTriangle = clipped[k];
                        float w1 = clippedTriangle.vertices[0].position.w;
                        float w2 = clippedTriangle.vertices[1].position.w;
                        float w3 = clippedTriangle.vertices[2].position.w;
                        clippedTriangle.vertices[0].position.div(clippedTriangle.vertices[0].position.w);
                        clippedTriangle.vertices[1].position.div(clippedTriangle.vertices[1].position.w);
                        clippedTriangle.vertices[2].position.div(clippedTriangle.vertices[2].position.w);

                        Vector3f[] triangleScreen = new Vector3f[]{
                                new Vector3f(
                                        (int) ((clippedTriangle.vertices[0].position.x + 1) * w / 2),
                                        (int) ((clippedTriangle.vertices[0].position.y * -1 + 1) * h / 2),
                                        -w1
                                ),
                                new Vector3f(
                                        (int) ((clippedTriangle.vertices[1].position.x + 1) * w / 2),
                                        (int) ((clippedTriangle.vertices[1].position.y * -1 + 1) * h / 2),
                                        -w2
                                ),
                                new Vector3f(
                                        (int) ((clippedTriangle.vertices[2].position.x + 1) * w / 2),
                                        (int) ((clippedTriangle.vertices[2].position.y * -1 + 1) * h / 2),
                                        -w3
                                )
                        };

                        switch(drawMode) {
                            case Wireframe:
                                drawLine(new Vector2i((int)triangleScreen[0].x, (int)triangleScreen[0].y),
                                        new Vector2i((int)triangleScreen[1].x, (int)triangleScreen[1].y), rt, clippedTriangle.color);
                                drawLine(new Vector2i((int)triangleScreen[1].x, (int)triangleScreen[1].y),
                                        new Vector2i((int)triangleScreen[2].x, (int)triangleScreen[2].y), rt, clippedTriangle.color);
                                drawLine(new Vector2i((int)triangleScreen[2].x, (int)triangleScreen[2].y),
                                        new Vector2i((int)triangleScreen[0].x, (int)triangleScreen[0].y), rt, clippedTriangle.color);
                                break;
                            case Color:
                                drawTriangle(triangleScreen, clippedTriangle.color, fTriangle.normal, f.shader, rt);
                                break;
                        }
                    }
                }
            }
        }
    }
}
