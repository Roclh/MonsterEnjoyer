package com.GaysFromITMO.core.rendering.camera;

import com.GaysFromITMO.core.rendering.shaders.DefaultShaderManager;
import com.GaysFromITMO.core.rendering.entity.Entity;
import com.GaysFromITMO.core.utils.MemoryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hud {
    List<Entity> hudItems = new ArrayList<>();
    DefaultShaderManager shaderManager;


    private void setupShaders() throws Exception{
        Scanner scanner = new Scanner(new File("fragment.frag"));
        scanner.next();
        shaderManager = new DefaultShaderManager();
        shaderManager.createVertexShader(MemoryUtils.loadResource("/shaders/textVertex.vert"));
        shaderManager.createFragmentShader(MemoryUtils.loadResource("/shaders/textFragment.frag"));
        shaderManager.link();
        shaderManager.createUniform("projModelMatrix");
        shaderManager.createUniform("colour");

    }

    private void cleanUp(){
        hudItems.forEach(Entity::clearBuffers);
        hudItems.clear();
    }

}
