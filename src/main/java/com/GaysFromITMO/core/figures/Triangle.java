package com.GaysFromITMO.core.figures;

import com.GaysFromITMO.core.exceptions.WrongFigureVertexArrayException;

public class Triangle implements FigureVertexArrayObject{
    private final String wrongFigureMessage = "Wrong Figure input. Await 9 values, got";
    private float[] vertexArray = {
            0.0f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f
    };
    private final int size = 9;

    @Override
    public float[] getVertexArrayObject() {
        return vertexArray;
    }

    @Override
    public void setVertexArrayObject(float[] vertexArray) throws WrongFigureVertexArrayException {
        if(vertexArray.length!= size) throw new WrongFigureVertexArrayException(wrongFigureMessage + vertexArray.length);
        this.vertexArray = vertexArray;
    }
}
