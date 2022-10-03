package com.GaysFromITMO.core.figures;

import com.GaysFromITMO.core.exceptions.WrongFigureVertexArrayException;
import com.GaysFromITMO.core.math.handlers.Function1;

public abstract class FigureVertexArrayObject {

    protected String wrongFigureMessage;
    protected int size;
    float[] vertexArray;

    public FigureVertexArrayObject(String wrongFigureMessage, int size, float[] vertexArray) {
        this.wrongFigureMessage = wrongFigureMessage;
        this.size = size;
        this.vertexArray = vertexArray;
    }

    public float[] getVertexArrayObject(){
        return this.vertexArray;
    }
    public void setVertexArrayObject(float[] vertexArray) throws WrongFigureVertexArrayException{
        if(vertexArray.length!= size) throw new WrongFigureVertexArrayException(wrongFigureMessage + vertexArray.length);
        this.vertexArray = vertexArray;
    }

    public float[] applyToAxes(Function1<Float> handler){
        for (int i =0; i<vertexArray.length; i++){
            vertexArray[i] = handler.apply(vertexArray[i]);
        }
        return vertexArray;
    }

    public int count(){
        return this.size/3;
    }

}
