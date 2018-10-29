package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Skybox {
	Texture skyTexture;
	public Vector3D size;
	
	public Skybox()
	{
		skyTexture = new Texture(Gdx.files.internal("textures/miramar_large.png"));
		size = new Vector3D(400,400,400);
	}
	
	public Skybox(float h, float w, float d)
	{
		skyTexture = new Texture(Gdx.files.internal("textures/miramar_large.png"));
		size = new Vector3D(h,w,d);
	}
	
	public void draw() {
		//Shader.mainShader.useSkyboxShader();
		BoxGraphic.drawSolidCube(skyTexture, null);
		//Shader.mainShader.useWorldShader();
	}
}
