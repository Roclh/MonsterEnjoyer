package com.GaysFromITMO.core.rendering.renderers;

import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.rendering.ObjectLoader;
import com.GaysFromITMO.core.rendering.entity.Model;
import com.GaysFromITMO.core.rendering.entity.Texture;
import com.GaysFromITMO.core.rendering.shaders.TextShaderManager;
import com.GaysFromITMO.core.utils.TransformationUtils;
import com.GaysFromITMO.logger.Log;
import com.GaysFromITMO.logger.Loggable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TextRenderManager extends RenderManager implements Loggable<TextRenderManager> {
    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;
    private final ObjectLoader objectLoader = (ObjectLoader) SingletonClassProvider.getStoredClass(ObjectLoader.class);
    private TextShaderManager shader;
    private boolean isLogged = false;
    public TextRenderManager(){
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws Exception{
        shader = new TextShaderManager("/shaders/textFragment.frag","/shaders/textVertex.vert");
        Log.info(this.getClass().getName() + " was initiated with shaders \"/shaders/textFragment.frag\" and \"/shaders/textVertex.vert\"", isLogged);
    }


    public Model buildTextModel(String text,Texture texture, int numCols, int numRows){
        byte[] chars = text.getBytes(StandardCharsets.ISO_8859_1);
        int numChars = chars.length;
        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals = new float[0];
        List<Integer> indices = new ArrayList<>();
        float tileWidth = 512 / (float) numCols;
        float tileHeight = 512 / (float) numRows;
        for (int i = 0; i < numChars; i++) {
            byte currChar = chars[i];
            int col = currChar % numCols;
            int row = currChar / numCols;
            positions.add((float) i * tileWidth); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float) col / (float) numCols);
            textCoords.add((float) row / (float) numRows);
            indices.add(i * VERTICES_PER_QUAD);
            positions.add((float) i * tileWidth); // x
            positions.add(tileHeight); //y
            positions.add(ZPOS); //z
            textCoords.add((float) col / (float) numCols);
            textCoords.add((float) (row + 1) / (float) numRows);
            indices.add(i * VERTICES_PER_QUAD + 1);
            positions.add((float) i * tileWidth + tileWidth); // x
            positions.add(tileHeight); //y
            positions.add(ZPOS); //z
            textCoords.add((float) (col + 1) / (float) numCols);
            textCoords.add((float) (row + 1) / (float) numRows);
            indices.add(i * VERTICES_PER_QUAD + 2);
            positions.add((float) i * tileWidth + tileWidth); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float) (col + 1) / (float) numCols);
            textCoords.add((float) row / (float) numRows);
            indices.add(i * VERTICES_PER_QUAD + 3);
            indices.add(i * VERTICES_PER_QUAD);
            indices.add(i * VERTICES_PER_QUAD + 2);
        }
        Model model =objectLoader.loadModel(TransformationUtils.convertToFloatArray(positions) , TransformationUtils.convertToFloatArray(textCoords), normals,
                indices.stream().mapToInt(i->i).toArray());
        model.setTexture(texture);
        return model;
    }

    public TextShaderManager getShader(){
        return this.shader;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public boolean isLogged() {
        return isLogged;
    }

    @Override
    public TextRenderManager isLogged(boolean isLogged) {
        this.isLogged = isLogged;
        return this;
    }
}
