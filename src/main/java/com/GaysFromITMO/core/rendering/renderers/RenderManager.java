package com.GaysFromITMO.core.rendering.renderers;

import com.GaysFromITMO.binder.SingletonClassProvider;
import com.GaysFromITMO.core.rendering.entity.Entity;
import com.GaysFromITMO.core.rendering.entity.Model;
import com.GaysFromITMO.core.window.WindowManager;
import com.GaysFromITMO.logger.Loggable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RenderManager implements Renderer {
    protected Map<Model, List<Entity>> entities = new HashMap<>();
    private final WindowManager window = (WindowManager) SingletonClassProvider.getStoredClass(WindowManager.class);
    public void unbind(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }



    public void freeMem(Model model){
        entities.remove(model);
    }

    public void processEntity(Entity entity){
        List<Entity> entityList = entities.get(entity.getModel());
        if(entityList != null){
            entityList.add(entity);
        }else{
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entities.put(entity.getModel(), newEntityList);
        }
    }

    @Override
    public void render(){
        clear();
        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }
    }

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }


}
