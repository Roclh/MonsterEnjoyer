package com.GaysFromITMO.core.rendering.shaders;

import com.GaysFromITMO.core.utils.MemoryUtils;

public class TextShaderManager extends ShaderManager{
    protected TextShaderManager() throws Exception {
        super();
    }

    public TextShaderManager(String fragShaderPath, String vertShaderPath) throws Exception{
        super();
        createFragmentShader(MemoryUtils.loadResource(fragShaderPath));
        createVertexShader(MemoryUtils.loadResource(vertShaderPath));
        link();

    }
}
