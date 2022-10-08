package com.GaysFromITMO.core.rendering.entity;

import com.GaysFromITMO.core.rendering.camera.Camera;
import com.GaysFromITMO.core.rendering.renderers.DefaultRenderManager;
import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.window.WindowManager;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Entity {
    private Model model;
    private Vector3f pos, rotation;
    private DefaultRenderManager renderer = (DefaultRenderManager) SingletonClassProvider.getStoredClass(DefaultRenderManager.class);
    private float scale;

    public Entity(Model model, Vector3f pos, Vector3f rotation, float scale){
        this.model = new Model(model);
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity(){
        this.model = new Model();
        this.pos = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = 1f;
    }

    public Entity withColour(Vector4f colour){
        this.model.getMaterial().setAmbientColour(colour);
        this.model.getMaterial().setSpecularColour(colour);
        this.model.getMaterial().setDiffuseColour(colour);
        return this;
    }

    public void incPos(float x, float y, float z){
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }

    public void setPos(float x, float y, float z){
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void incRotation(float x, float y, float z){
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public boolean shouldBeRendered(Camera camera){
        float range = (float)Math.sqrt(Math.pow(camera.getPosition().x-this.pos.x,2)+Math.pow(camera.getPosition().y-this.pos.y,2)+Math.pow(camera.getPosition().z-this.pos.z,2));
        return range<WindowManager.Z_FAR;
    }

    public void clearBuffers(){
        renderer.freeMem(this.model);
    }
}
