package com.ru.tgra.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class Shader {
	public static Shader mainShader;
	public static FloatBuffer matrixBuffer;
	public boolean usingWorldShader;

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private int renderingSkyboxLoc;
	
	private int positionLoc;
	private int normalLoc;
	private int uvLoc;
	private int diffuseTextureLoc;
	private int usesDiffuseTextureLoc;

	public static int modelMatrixLoc;
	public static int viewMatrixLoc;
	public static int projectionMatrixLoc;
	
	private int globalAmbientLoc;

	private int materialDiffuseLoc;
	private int materialSpecularLoc;
	private int materialShineLoc;
	private int eyePosLoc;
	
	private int fogColorLoc;
	private int fogMinDistLoc;
	private int fogMaxDistLoc;
	
	private int alphaLoc;
	
	Texture currentTexture;
		
	public Shader()
	{
		//Standard shader setup
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
		
		
		//Standard shader pointer setup
		uvLoc					= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_uvpos");
		Gdx.gl.glEnableVertexAttribArray(uvLoc);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);
		

		diffuseTextureLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_diffuseTexture");
		usesDiffuseTextureLoc   = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_usesDiffuseTexture");
		System.out.println("test: " + diffuseTextureLoc);
		
		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");
		
		globalAmbientLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");
		
		materialDiffuseLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
		materialSpecularLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
		materialShineLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShine");
		eyePosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");		
		renderingSkyboxLoc 		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_renderingSkybox");
		alphaLoc 				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_alpha");
		
		fogColorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_fogColor");
		fogMinDistLoc 			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_fogStart");
		fogMaxDistLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_fogEnd");
		Gdx.gl.glUseProgram(renderingProgramID);
		usingWorldShader = true;
		mainShader = this;
		useWorldShader();
	}
	
	public void set1f(String s, float f) {
		Gdx.gl.glUniform1f(Gdx.gl.glGetUniformLocation(renderingProgramID, s), f);

	}
	
	public void set4f(String s, float x, float y, float z, float w) {
		Gdx.gl.glUniform4f(Gdx.gl.glGetUniformLocation(renderingProgramID, s), x, y, x, w);
	}
	
	public int getLoc(String s) {
		return Gdx.gl.glGetUniformLocation(renderingProgramID, s);
	}
	
	public void setPointLight(int i, Point3D position, Vector3D diffuse, Vector3D specular, float range) {
		
		String stwing= "u_pointLights[" + i + "]";
		set4f(stwing +".position", position.x, position.y, position.x, 0);
		set4f(stwing +".diffuse", diffuse.x, diffuse.y, diffuse.x, 1);
		set4f(stwing +".specular", specular.x, specular.y, specular.x, 1);
		set1f(stwing +".range", range);
	}
	
	public void setEyePosition(float x, float y, float z)
	{
		Gdx.gl.glUniform4f(eyePosLoc, x, y, z, 0);
	}
	
	public void setMaterialDiffuse(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(materialDiffuseLoc, r, g, b, a);
	}
	public void setMaterialSpecular(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(materialSpecularLoc, r, g, b, a);
	}
	
	public void setMaterialShine(float val)
	{
		Gdx.gl.glUniform1f(materialShineLoc, val);
	}
	
	public void setGlobalAmbient(float a)
	{
		Gdx.gl.glUniform4f(globalAmbientLoc, a, a, a, 1);
	}
	
	public int getVertexPointer()
	{
		return positionLoc;
	}
	
	public int getNormalPointer()
	{
		return normalLoc;
	}
	
	public int getUVPointer()
	{
		return uvLoc;
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
	
	public void useWorldShader()
	{
		usingWorldShader = true;
		Gdx.gl.glUniform1i(renderingSkyboxLoc, 0);
	}
	
	public void useSkyboxShader()
	{
		usingWorldShader = false;
		Gdx.gl.glUniform1i(renderingSkyboxLoc, 1);
	}
	
	public void setDiffuseTexture(Texture tex)
	{
		if(tex == null)
		{
			currentTexture = null;
			Gdx.gl.glUniform1i(usesDiffuseTextureLoc, 0);	
		}
		else
		{
			if(currentTexture != tex) {
				tex.bind(0);
				currentTexture = tex;
			}
			
			Gdx.gl.glUniform1i(diffuseTextureLoc, 0);
			Gdx.gl.glUniform1i(usesDiffuseTextureLoc, 1);	
		}
	}
	
	public void setAlpha(float alpha) {
		Gdx.gl.glUniform1f(alphaLoc, alpha);
	}
	
	public void setFogColor(float r, float g, float b) {
		Gdx.gl.glUniform3f(fogColorLoc, r, g, b);
	}
	public void setFogDist(float near, float far) {
		Gdx.gl.glUniform1f(fogMinDistLoc, near);
		Gdx.gl.glUniform1f(fogMaxDistLoc, far);
	}
}
