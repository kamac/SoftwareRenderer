import graphics.CubeFigure;
import graphics.Figure;
import graphics.PerspectiveFigure;
import graphics.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class EditorView extends View {
    public enum ViewDirection {
        TOP, FRONT, LEFT
    }

    private ViewDirection _viewDir;
    private float _cameraZoom = 10.0f;
    private Figure _cameraFigure;
    private Point _lastMouseDragPos = null;
    private boolean _isDragging = false;

    public EditorView(ViewDirection viewDir) {
        super(viewDir.toString());
        _viewDir = viewDir;
        switch(_viewDir) {
            case TOP:
                _cameraTransform.setRotation(new Vector3f(1.0f, 0.0f, 0.0f), -(float)Math.PI/2);
                break;
            case LEFT:
                _cameraTransform.setRotation(new Vector3f(0.0f, 1.0f, 0.0f), (float)Math.PI/2);
                break;
        }
        repositionCamera();
        this.setBackground(new Color(20, 20, 20));

        _cameraFigure = new PerspectiveFigure(FigureView.instance._projection);
        this.addPrivateFigure(_cameraFigure);

        EditorView self = this;
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                _lastMouseDragPos = e.getPoint();
                _isDragging = self.contains(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if(_lastMouseDragPos != null && _isDragging) {
                    float diffX = e.getPoint().x - _lastMouseDragPos.x;
                    float diffY = e.getPoint().y - _lastMouseDragPos.y;
                    Transform t = FigureView.instance.getCameraTransform();
                    float dx = diffX / getWidth() * _cameraZoom;
                    float dy = diffY / getHeight() * _cameraZoom;
                    switch(viewDir) {
                        case TOP:
                            t.setPosition(t.getPosition().add(-dx, 0.0f, dy));
                            break;
                        case FRONT:
                            t.setPosition(t.getPosition().add(-dx, dy, 0.0f));
                            break;
                        case LEFT:
                            t.setPosition(t.getPosition().add(0.0f, dy, -dx));
                            break;
                    }
                    View.repaintViews();
                }
                _lastMouseDragPos = e.getPoint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        };
        MouseWheelListener wheelListener = new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                _cameraZoom += (float)e.getUnitsToScroll() / 2.0f;
                repositionCamera();
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(wheelListener);
    }

    public void refreshFrustum() {
        removePrivateFigure(_cameraFigure);
        _cameraFigure = new PerspectiveFigure(FigureView.instance._projection);
        addPrivateFigure(_cameraFigure);
    }

    protected void repositionCamera() {
        _projection = new Matrix4f().ortho(-_cameraZoom, _cameraZoom, -_cameraZoom, _cameraZoom, 0.01f, 1000.0f);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Transform figureViewCam = FigureView.instance.getCameraTransform();
        _cameraFigure.transform.setPosition(new Vector3f(figureViewCam.getPosition()).mul(-1));
        _cameraFigure.transform.setRotation(new Vector3f(figureViewCam.getRotation()), figureViewCam.getRotationAngle());
        super.paintComponent(g);
    }
}
