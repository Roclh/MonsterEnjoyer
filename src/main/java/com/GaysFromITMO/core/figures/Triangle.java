package com.GaysFromITMO.core.figures;

public class Triangle extends FigureVertexArrayObject{
    public Triangle() {
        super("Wrong Figure input. Await 9 values, got", 9, new float[]{
                0.0f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f
        });
    }
}
