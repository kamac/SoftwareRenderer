package graphics;

import org.joml.*;

public class Transform {
    private Vector3f _position = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector3fc _rotation = new Vector3f(1.0f, 0.0f, 0.0f);
    private Vector3f _scale = new Vector3f(1.0f, 1.0f, 1.0f);
    private float _rotationAngle = 0.0f;
    private Matrix4f _matrix = new Matrix4f();
    private boolean _isDirty;

    public void setPosition(Vector3f p) {
        this._position = p;
        _isDirty = true;
    }

    public Vector3f getPosition() {
        return this._position;
    }

    public void setRotation(Vector3fc rotation, float angle) {
        this._rotation = rotation;
        this._rotationAngle = angle;
        _isDirty = true;
    }

    public Vector3fc getRotation() {
        return this._rotation;
    }

    public float getRotationAngle() {
        return this._rotationAngle;
    }

    public void setScale(Vector3f scale) {
        this._scale = scale;
        this._isDirty = true;
    }

    public Vector3f getScale() {
        return this._scale;
    }

    public Matrix4f getMatrix() {
        if(this._isDirty) {
            this._isDirty = false;
            this._matrix = new Matrix4f()
                    .translate(this._position.x, this._position.y, this._position.z)
                    //.scale(this._scale)
                    .rotate(this._rotationAngle, this._rotation);
        }
        return this._matrix;
    }
}
