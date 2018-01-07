package graphics.shaders;

import graphics.textures.Texture;
import graphics.Vertex;
import org.joml.*;
import org.joml.Math;

public abstract class Shader {
    protected Matrix4f _model, _view, _vp, _mvp;
    private float[] _uvs = new float[2 * 3]; // a 2x3 matrix

    public void setMatrices(Matrix4f model, Matrix4f view, Matrix4f vp, Matrix4f mvp) {
        _model = model;
        _view = view;
        _vp = vp;
        _mvp = mvp;
    }

    protected Vector4f tex2d(Vector2f uv, Texture tex) {
        return tex.getColor(
                (int)((uv.x % 1.0f) * tex.getWidth()),
                (int)((uv.y % 1.0f) * tex.getHeight())
        );
    }

    public Vertex vertex(Vertex v, int nthVertex, Vector3f normal) {
        if(v.texcoords != null) {
            _uvs[nthVertex * 2 + 0] = v.texcoords.x;
            _uvs[nthVertex * 2 + 1] = 1.0f - v.texcoords.y;
        }
        return vertex(v, normal);
    }

    public Vector4f fragment(Vector3f barycentric, Vector4f color, Vector3f normal, Vector2f screenUV) {
        Vector2f texcoords = new Vector2f(
                _uvs[0*2 + 0]*barycentric.x + _uvs[1*2 + 0]*barycentric.y + _uvs[2*2 + 0]*barycentric.z,
                _uvs[0*2 + 1]*barycentric.x + _uvs[1*2 + 1]*barycentric.y + _uvs[2*2 + 1]*barycentric.z
        );
        return fragment(color, texcoords, normal, screenUV);
    }

    protected abstract Vertex vertex(Vertex v, Vector3f normal);
    public abstract Vector4f fragment(Vector4f color, Vector2f texcoords, Vector3f normal, Vector2f screenUV);
}
