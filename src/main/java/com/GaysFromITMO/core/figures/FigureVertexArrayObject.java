package com.GaysFromITMO.core.figures;

import com.GaysFromITMO.core.exceptions.WrongFigureVertexArrayException;

public interface FigureVertexArrayObject {
    float[] getVertexArrayObject();
    void setVertexArrayObject(float[] vertexArray) throws WrongFigureVertexArrayException;

}
