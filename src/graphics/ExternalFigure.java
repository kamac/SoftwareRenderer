package graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ExternalFigure extends Figure {
    public ExternalFigure(File file) {
        _triangles = build(file);
    }

    public static Triangle[] build(File file) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            list = br.lines().collect(Collectors.toList());
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        ArrayList<Vector4f> vertices = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        ArrayList<Vector2f> texcoords = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            String line = list.get(i).toUpperCase();
            if(line.isEmpty() || line.startsWith("#"))
                continue;
            Scanner sc = new Scanner(line).useLocale(Locale.ENGLISH);
            String first = sc.next();
            if(first.equals("V")) {
                float x = Float.parseFloat(sc.next());
                float y = Float.parseFloat(sc.next());
                float z = Float.parseFloat(sc.next());
                vertices.add(new Vector4f(x, y, z, 1.0f));
            } else if(first.equals("VT")) {
                float u = Float.parseFloat(sc.next());
                float v = Float.parseFloat(sc.next());
                texcoords.add(new Vector2f(u, v));
            } else if(first.equals("VN")) {
                float x = Float.parseFloat(sc.next());
                float y = Float.parseFloat(sc.next());
                float z = Float.parseFloat(sc.next());
                normals.add(new Vector3f(x, y, z).normalize());
            } else if(first.equals("F")) {
                String[] idx = sc.next().split("/");
                Vertex v1 = new Vertex(vertices.get(Integer.parseInt(idx[0])-1), texcoords.get(Integer.parseInt(idx[1])-1));
                idx = sc.next().split("/");
                Vertex v2 = new Vertex(vertices.get(Integer.parseInt(idx[0])-1), texcoords.get(Integer.parseInt(idx[1])-1));
                idx = sc.next().split("/");
                Vertex v3 = new Vertex(vertices.get(Integer.parseInt(idx[0])-1), texcoords.get(Integer.parseInt(idx[1])-1));
                Random rng = new Random();
                triangles.add(new Triangle(v1, v2, v3, normals.get(Integer.parseInt(idx[2])-1)));
            }
        }
        return triangles.toArray(new Triangle[triangles.size()]);
    }
}
