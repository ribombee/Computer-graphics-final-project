package com.ru.tgra.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Shader {
	public static FloatBuffer matrixBuffer;

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;

	public static int modelMatrixLoc;
	public static int viewMatrixLoc;
	public static int projectionMatrixLoc;

	private int lightSourceLoc;
	private int lightDiffuseLoc;
	private int lightSpecularLoc;
	private int lightRangeLoc;
	private int materialDiffuseLoc;
	private int materialSpecularLoc;
	private int materialShineLoc;
	private int eyePosLoc;
	public Shader()
	{
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/simple3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/simple3D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);
	
		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		lightSourceLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition");
		lightDiffuseLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDiffuse");
		lightRangeLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightRange");
		materialDiffuseLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
		lightSpecularLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightSpecular");
		materialSpecularLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
		materialShineLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShine");
		eyePosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");

		Gdx.gl.glUseProgram(renderingProgramID);
	}
	
	public void setLightSource(float x, float y, float z)
	{
		Gdx.gl.glUniform4f(lightSourceLoc, x, y, z, 0);
	}
	
	public void setEyePosition(float x, float y, float z)
	{
		Gdx.gl.glUniform4f(eyePosLoc, x, y, z, 0);
	}
	
	public void setLightDiffuse(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(lightDiffuseLoc, r, g, b, a);
	}
	
	public void setMaterialDiffuse(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(materialDiffuseLoc, r, g, b, a);
	}
	public void setLightSpecular(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(lightSpecularLoc, r, g, b, a);
	}
	
	public void setMaterialSpecular(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(materialSpecularLoc, r, g, b, a);
	}
	
	public void setMaterialShine(float val)
	{
		Gdx.gl.glUniform1f(materialShineLoc, val);
	}
	
	public void setLightRange(float val)
	{
		Gdx.gl.glUniform1f(lightRangeLoc, val);
	}
	
	public int getVertexPointer()
	{
		return positionLoc;
	}
	
	public int getNormalPointer()
	{
		return normalLoc;
	}
	
	public void setModelMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
	}
	
	public void setViewMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
	}
	
	public void setProjectionMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
	}
	
}
