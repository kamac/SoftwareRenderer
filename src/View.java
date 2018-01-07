import graphics.Figure;
import graphics.RenderTexture;
import graphics.Transform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.*;
import renderer.Renderer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public abstract class View extends JPanel {
    protected static ArrayList<View> _views = new ArrayList<>();
    private static ArrayList<Figure> _figures = new ArrayList<>();
    private ArrayList<Figure> _privateFigures = new ArrayList<>();
    protected RenderTexture _mainRT;
    protected renderer.Renderer _renderer = new Renderer();
    protected Transform _cameraTransform = new Transform();
    protected Matrix4f _projection;

    public View(String name) {
        _views.add(this);
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                name
        ));
    }

    public static void addFigure(Figure f) {
        _figures.add(f);
        repaintViews();
    }

    public static void clearFigures() {
        _figures.clear();
        repaintViews();
    }

    public static void repaintViews() {
        for(int i = 0; i < _views.size(); i++) {
            _views.get(i).repaint();
        }
    }

    public int addPrivateFigure(Figure f) {
        _privateFigures.add(f);
        this.repaint();
        return _privateFigures.size()-1;
    }

    public void removePrivateFigure(Figure f) {
        _privateFigures.remove(f);
    }

    public Renderer getRenderer() {
        return _renderer;
    }

    public Transform getCameraTransform() {
        return this._cameraTransform;
    }

    public void drawSceneToRT(RenderTexture tex) {
        _renderer.draw(_cameraTransform, _projection, tex, _figures);
        _renderer.draw(_cameraTransform, _projection, tex, _privateFigures);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(_mainRT == null) {
            _mainRT = new RenderTexture(this.getWidth(), this.getHeight());
        }
        _mainRT.clear();
        Graphics2D g2d = (Graphics2D)g;

        drawSceneToRT(_mainRT);
        g.drawImage(_mainRT.getTexture().getImage(), 0, 0, null);
    }
}
