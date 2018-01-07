import graphics.CubeFigure;
import graphics.ExternalFigure;
import graphics.Figure;
import graphics.shaders.ShadedColorShader;
import graphics.shaders.ShadedTextureShader;
import graphics.shaders.TextureShader;
import graphics.textures.RGBTexture;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class Main extends JFrame {
    final double GAME_HERTZ = 30.0;
    final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
    private boolean _isRunning = true;
    private int _fps = 60;
    private int _frameCount = 0;
    ArrayList<Figure> figures = new ArrayList<>();

    public Main() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel grid = new JPanel(new GridLayout(2, 2));
        grid.setPreferredSize(new Dimension(500, 500));

        FigureView wb = new FigureView();
        grid.add("figure", wb);
        EditorView wbFront = new EditorView(EditorView.ViewDirection.FRONT);
        grid.add("front", wbFront);
        EditorView wbLeft = new EditorView(EditorView.ViewDirection.LEFT);
        grid.add("left", wbLeft);
        EditorView wbTop = new EditorView(EditorView.ViewDirection.TOP);
        grid.add("top", wbTop);

        Figure cube = new CubeFigure(1.0f, 1.0f, 1.0f);
        cube.setColor(new Vector4f(0.0f, 0.9f, 0.0f, 1.0f));
        cube.shader = new ShadedColorShader(wb.lights, wb.ambientLight);
        figures.add(cube);
        View.addFigure(cube);

        // create controls
        JPanel controls = new JPanel(new GridLayout(3, 2));
        JButton btn = new JButton("Load model");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser ch = new JFileChooser();
                int returnVal = ch.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = ch.getSelectedFile();
                    Figure f = new ExternalFigure(file);
                    f.shader = new ShadedColorShader(wb.lights, wb.ambientLight);
                    View.addFigure(f);
                    figures.add(f);
                }
            }
        });
        controls.add(btn);
        btn = new JButton("Load diffuse (shaded)");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser ch = new JFileChooser();
                int returnVal = ch.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = ch.getSelectedFile();
                    figures.get(figures.size()-1).shader = new ShadedTextureShader(
                            wb.lights, wb.ambientLight, new RGBTexture(file));
                }
            }
        });
        controls.add(btn);
        btn = new JButton("Load diffuse");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser ch = new JFileChooser();
                int returnVal = ch.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = ch.getSelectedFile();
                    figures.get(figures.size()-1).shader = new TextureShader(new RGBTexture(file));
                }
            }
        });
        controls.add(btn);
        btn = new JButton("Clear scene");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View.clearFigures();
                figures.clear();
            }
        });
        controls.add(btn);

        JSlider fovSlider = new JSlider(1, 89, 75);
        Hashtable labelTable = new Hashtable();
        labelTable.put(new Integer(1), new JLabel("1"));
        labelTable.put(new Integer(45), new JLabel("FOV"));
        labelTable.put(new Integer(89), new JLabel("89"));
        fovSlider.setLabelTable(labelTable);
        fovSlider.setPaintLabels(true);
        fovSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting()) {
                    wb.setFOV((int)source.getValue());
                    wbFront.refreshFrustum();
                    wbLeft.refreshFrustum();
                    wbTop.refreshFrustum();
                    View.repaintViews();
                }
            }
        });
        controls.add(fovSlider);

        JCheckBox waterEnabled = new JCheckBox("Water", false);
        waterEnabled.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox source = (JCheckBox)e.getSource();
                wb.isWaterEnabled = source.isSelected();
                View.repaintViews();
            }
        });
        controls.add(waterEnabled);

        this.add(grid, BorderLayout.NORTH);
        this.add(new JSeparator(), BorderLayout.CENTER);
        this.add(controls, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        Main frame = new Main();

        frame.pack();
        frame.setVisible(true);
        frame.runGameLoop();
    }

    float _time = 0.0f;
    private void updateGame(double deltaTime) {
        _time += deltaTime;
        if(figures.size() == 0)
            return;
        /*figures.get(figures.size()-1).transform.setRotation(new Vector3f(0.0f, 1.0f, 0.0f),
                (float) (figures.get(i).transform.getRotationAngle() + deltaTime * Math.PI / 10));*/
        figures.get(figures.size()-1).transform.setRotation(new Vector3f(0.0f, 0.0f, 1.0f),
                (float) Math.cos(_time / 5 * Math.PI) * (float)Math.PI / 50);
    }

    public void runGameLoop() {
        Thread loop = new Thread() {
            public void run() {
                gameLoop();
            }
        };
        loop.start();
    }

    private void gameLoop() {
        final int MAX_UPDATES_BEFORE_RENDER = 5;
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();

        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (_isRunning) {
            double now = System.nanoTime();
            int updateCount = 0;

            while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                updateGame(TIME_BETWEEN_UPDATES / 1000000000.0);
                lastUpdateTime += TIME_BETWEEN_UPDATES;
                updateCount++;
            }

            if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                lastUpdateTime = now - TIME_BETWEEN_UPDATES;
            }

            View.repaintViews();
            _frameCount++;
            lastRenderTime = now;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime) {
                //System.out.println(_fps);
                _fps = _frameCount;
                _frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                Thread.yield();
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
                now = System.nanoTime();
            }
        }
    }
}
