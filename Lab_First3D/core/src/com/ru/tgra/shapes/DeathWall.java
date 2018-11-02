package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DeathWall extends MazeWall{

	private static Texture deathWallTexture;
	
	//Will just have diffuse colors 1,1,1,1 and same for spec
	
	private float minZDist = 30;
	public float speed;
	
	public DeathWall(Point3D p, float w, float h, float d, float s) {
		super(p,w,h,d);
		speed = s;
		deathWallTexture = new Texture(Gdx.files.internal("textures/deathwall.png"));
		tex = deathWallTexture;
		specTex = deathWallTexture;
	}
	
	public void move(Point3D playerPos) {
		float dt = Maze3D.deltaTime;
		System.out.println("dist: " + (playerPos.z - (position.z + depth/2)));
		
		if(playerPos.z - (position.z + depth/2) > minZDist) {
			position.set(position.x, position.y, playerPos.z - (minZDist + depth/2));
		}
		else {
			position.set(position.x, position.y, position.z + speed*dt);
		}
	}
	
	public void draw() {
		Shader.mainShader.setAlpha(0.7f);
		super.draw();
		Shader.mainShader.setAlpha(1f);
		
	}
	
}
