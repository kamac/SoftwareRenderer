import graphics.Figure;
import graphics.QuadFigure;
import graphics.RenderTexture;
import graphics.Vertex;
import graphics.shaders.ShadedColorShader;
import graphics.shaders.WaterShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.Renderer;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by kamac on 30.11.2017.
 */
public class FigureView extends View {
    public static FigureView instance;
    public ArrayList<ShadedColorShader.Light> lights = new ArrayList<>();
    public Vector3f ambientLight = new Vector3f(0.0f, 0.0f, 0.0f);

    private Figure _water;
    private RenderTexture _waterRT;
    public boolean isWaterEnabled = false;

    public FigureView() {
        super("figure view");
        instance = this;

        setFOV(75.0f);
        _cameraTransform.setPosition(new Vector3f(0.0f, 0.0f, -2.0f));
        _renderer.drawMode = Renderer.DrawMode.Color;

        ambientLight = new Vector3f(0.1f, 0.1f, 0.2f);
        lights.add(new ShadedColorShader.Light(
                null,
                new Vector3f(-1.0f, -0.8f, 0.6f),
                new Vector3f(1.0f, 1.0f, 1.0f),
                1.0f,
                ShadedColorShader.LightType.Directional
        ));

        _water = new QuadFigure(
                new Vertex(new Vector4f(40.0f, 0.0f, 40.0f, 1.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector4f(40.0f, 0.0f, -40.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector4f(-40.0f, 0.0f, -40.0f, 1.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector4f(-40.0f, 0.0f, 40.0f, 1.0f), new Vector2f(0.0f, 0.0f))
        );
        _water.shader = new WaterShader();

        this.setBackground(Color.BLACK);
    }

    public void setFOV(float fov) {
        _projection = new Matrix4f();
        _projection.perspective((float)Math.toRadians(fov), 1.0f, 0.1f, 100.0f);
    }

    @Override
    public void paintComponent(Graphics g) {
        if(isWaterEnabled) {
            if (_waterRT == null) {
                _waterRT = new RenderTexture(getWidth() / 2, getHeight() / 2);
            }

            _waterRT.clear();
            Vector3f oldCameraPos = _cameraTransform.getPosition();
            _cameraTransform.setPosition(new Vector3f(oldCameraPos.x, 0.5f-oldCameraPos.y, oldCameraPos.z));

            drawSceneToRT(_waterRT);
            _cameraTransform.setPosition(oldCameraPos);
            ((WaterShader) _water.shader).reflection = _waterRT.getTexture();
            addPrivateFigure(_water);

            super.paintComponent(g);
            removePrivateFigure(_water);

            /*Graphics2D g2d = (Graphics2D)g;
            g.drawImage(_waterRT.getTexture().getImage(), 0, 0, null);*/
        } else {
            super.paintComponent(g);
        }
    }
}
