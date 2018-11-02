package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Skybox {
	Texture skyTexture;
	public Vector3D size;
	public Point3D position;
	
	public Skybox()
	{
		skyTexture = new Texture(Gdx.files.internal("textures/skybox.png"));
		size = new Vector3D(400,400,400);
		position = new Point3D(0,0,0);
	}
	
	public Skybox(float h, float w, float d)
	{
		skyTexture = new Texture(Gdx.files.internal("textures/skybox.png"));
		size = new Vector3D(h,w,d);
		position = new Point3D(0,0,0);
	}
	
	public void draw() {
		Shader.mainShader.useSkyboxShader();
		BoxGraphic.drawSolidCube(skyTexture, null);
		Shader.mainShader.useWorldShader();
	}
}
