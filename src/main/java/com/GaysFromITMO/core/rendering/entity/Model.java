package com.GaysFromITMO.core.rendering.entity;

import java.util.Objects;

public class Model {
    private int id;
    private int vertexCount;
    private Material material;

    public Model(int id, int vertexCount){
        this.id = id;
        this.vertexCount = vertexCount;
        this.material = new Material();
    }

    public Model(){
        this.id = -1;
        this.vertexCount = -1;
        this.material = new Material();
    }

    public Model(int id, int vertexCount, Texture texture) {
        this.id = id;
        this.vertexCount = vertexCount;
        this.material = new Material(texture);
    }

    public Model(Model model) {
        this.id = model.getId();
        this.vertexCount = model.getVertexCount();
        this.material = new Material(model.getMaterial());
    }

    public Material getMaterial() {
        return material;
    }

    public Model(Model model, Texture texture) {
        this.id = model.getId();
        this.vertexCount = model.getVertexCount();
        this.material = model.getMaterial();
        this.material.setTexture(texture);
    }

    public Texture getTexture() {
        return material.getTexture();
    }

    public void setTexture(Texture texture) {
        this.material.setTexture(texture);
    }

    public void setTexture(Texture texture, float reflectance) {
        this.material.setTexture(texture);
        this.material.setReflectance(reflectance);
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return id == model.id;
    }
}
