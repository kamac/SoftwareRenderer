package graphics;

import graphics.shaders.ColorShader;
import graphics.shaders.Shader;
import javafx.scene.image.PixelWriter;
import org.joml.*;

import java.awt.*;
import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;

public abstract class Figure {
    protected Triangle[] _triangles;
    public Transform transform = new Transform();
    public Shader shader = new ColorShader();

    public void setColor(Vector4f color) {
        for(int i = 0; i < _triangles.length; i++) {
            _triangles[i].color = color;
        }
    }

    public Triangle[] getTriangles() {
        return _triangles;
    }
}
