package com.GaysFromITMO.core.rendering;

import org.lwjgl.opengl.GL20;

public class ShaderManager {

    private final int programId;
    private int vertexShaderId, fragmentShaderId;

    public ShaderManager() throws Exception{
        programId = GL20.glCreateProgram();
        if(programId == 0)
            throw new Exception("Could not create shader");
    }

    public void createVertexShader(String shaderCode) throws Exception{
        vertexShaderId = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception{
        vertexShaderId = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception{
        int shaderID = GL20.glCreateShader(shaderType);
        if(shaderID == 0){
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiling shader code: TYPE: "+ shaderType
            + " Info: "+GL20.glGetShaderInfoLog(shaderID, 1024));

        GL20.glAttachShader(programId, shaderID);

        return shaderID;
    }

    public void link() throws Exception{
        GL20.glLinkProgram(programId);
        if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) ==0)
            throw new Exception("Error linking shader code "
                + " info " + GL20.glGetProgramInfoLog(programId, 1024));
        if(vertexShaderId != 0){
            GL20.glDetachShader(programId, vertexShaderId);
        }
        if(fragmentShaderId != 0){
            GL20.glDetachShader(programId, fragmentShaderId);
        }
        GL20.glValidateProgram(programId);
        if(GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == 0){
            throw new Exception("Unable to validate shader code: "+GL20.glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind(){
        GL20.glUseProgram(programId);
    }

    public void unbind(){
        GL20.glUseProgram(0);
    }

    public void cleanup(){
        unbind();
        if(programId!=0){
            GL20.glDeleteProgram(programId);
        }
    }
}