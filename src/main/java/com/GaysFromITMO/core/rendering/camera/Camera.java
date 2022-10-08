package com.GaysFromITMO.core.rendering.camera;

import org.joml.Vector3f;

public class Camera {
    private Vector3f position, rotation;
    private float vmin, vmax;

    public Camera(){
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void movePosition(float x, float y, float z){
        if(z != 0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if(x != 0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        position.y += y;
    }

    public void setVerticalRotationLimits(float vmin, float vmax){
        this.vmin = vmin;
        this.vmax = vmax;
    }

    public void setPosition(float x, float y, float z){
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
        this.rotation.x %= 360;
        this.rotation.y %= 360;
        this.rotation.z %= 360;
    }

    public void moveRotation(float x, float y, float z){
        if(this.rotation.x+x>=vmin&&this.rotation.x+x<=vmax){
            this.rotation.x += x;
        }else{
            this.rotation.x = x>=0?vmax:vmin;
        }
        this.rotation.y += y;
        this.rotation.z += z;
        this.rotation.x %= 360;
        this.rotation.y %= 360;
        this.rotation.z %= 360;
    }

    public Vector3f rotationToNormalVector(){
        return new Vector3f(
                (float) (Math.cos(Math.toRadians(this.rotation.y))*Math.cos(Math.toRadians(this.rotation.x))),
                (float) (Math.sin(Math.toRadians(this.rotation.y))*Math.cos(Math.toRadians(this.rotation.x))),
                (float) Math.sin(Math.toRadians(this.rotation.x))
        );
    }

    public Vector3f rotationToNormalVector(float range){
        return new Vector3f(
                (float) (Math.cos(Math.toRadians(this.rotation.y))*Math.cos(Math.toRadians(this.rotation.x)))*range,
                (float) Math.sin(Math.toRadians(this.rotation.x))*range,
                (float) (Math.sin(Math.toRadians(this.rotation.y))*Math.cos(Math.toRadians(this.rotation.x)))*range
        );
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
