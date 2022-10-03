package com.GaysFromITMO.core.figures;

public class Square extends FigureVertexArrayObject{
    public Square() {
        super("Wrong Figure input. Await 12 values, got", 18, new float[]{
                //верхний треугольник
                0.5f, 0.5f, 0, -0.5f, 0.5f, 0, -0.5f, -0.5f, 0,
                //нижний треугольник
                0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0
        });
    }

}
