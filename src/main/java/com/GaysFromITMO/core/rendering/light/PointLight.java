package com.GaysFromITMO.core.rendering.light;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class PointLight {
    private Vector4f colour, position;
    private float intencity, constant, linear, exponent;

    public PointLight(Vector4f colour, Vector4f position, float intencity, float constant, float linear, float exponent) {
        this.colour = colour;
        this.position = position;
        this.intencity = intencity;
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
    }

    public PointLight(Vector4f colour, Vector4f position, float intencity) {
        this(colour, position, intencity, 1, 0, 0);
    }

    public Vector4f getColour() {
        return colour;
    }

    public void setColour(Vector4f colour) {
        this.colour = colour;
    }

    public Vector4f getPosition() {
        return position;
    }

    public void setPosition(Vector4f position) {
        this.position = position;
    }

    public float getIntencity() {
        return intencity;
    }

    public void setIntencity(float intencity) {
        this.intencity = intencity;
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getExponent() {
        return exponent;
    }

    public void setExponent(float exponent) {
        this.exponent = exponent;
    }
}
