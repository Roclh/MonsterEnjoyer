package com.GaysFromITMO.core.rendering.entity.font;

import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.rendering.ObjectLoader;
import com.GaysFromITMO.core.rendering.entity.Entity;
import com.GaysFromITMO.core.rendering.entity.Texture;
import com.GaysFromITMO.core.rendering.renderers.TextRenderManager;


public class TextEntity extends Entity {
    private final ObjectLoader objectLoader = (ObjectLoader) SingletonClassProvider.getStoredClass(ObjectLoader.class);
    private final TextRenderManager renderer = (TextRenderManager) SingletonClassProvider.getStoredClass(TextRenderManager.class);
    private String text;
    private final int numCols;
    private final int numRows;

    public TextEntity(String text, String fontFileName, int numCols, int numRows) throws Exception {
        super();
        this.text = text;
        this.numCols = numCols;
        this.numRows = numRows;
        Texture texture = new Texture(objectLoader.loadTexture(fontFileName));
        this.setModel(renderer.buildTextModel(this.text,texture, numCols, numRows));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        Texture texture = this.getModel().getTexture();
        this.clearBuffers();
        this.setModel(renderer.buildTextModel(this.text,texture, numCols, numRows));
    }

}
