package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DeathWall extends MazeWall{

	private static Texture deathWallTexture;
	
	//Will just have diffuse colors 1,1,1,1 and same for spec
	
	public float speed;
	
	public DeathWall(Point3D p, float w, float h, float d, float s) {
		super(p,w,h,d);
		speed = s;
		deathWallTexture = new Texture(Gdx.files.internal("textures/deathwall.png"));
		tex = deathWallTexture;
		specTex = deathWallTexture;
	}
	
	public void move(float dt) {
		position.set(position.x, position.y, position.z + speed*dt);
	}
	
}
