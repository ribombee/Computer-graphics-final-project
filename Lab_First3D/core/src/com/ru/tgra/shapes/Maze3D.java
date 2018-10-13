package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.BufferUtils;

public class Maze3D extends ApplicationAdapter implements InputProcessor {
	private Shader shader;
	
	private Player firstPersonPlayer;
	
	private boolean wDown, aDown, sDown, dDown, upDown, downDown, leftDown, rightDown, qDown, eDown;
	
	private List<MazeWall> walls; 
	MazeWall movingWall;
	
	Point3D lightSource;
	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(this);
		shader = new Shader();
		
		lightSource = new Point3D(1,15,0);
		shader.setLightSource(lightSource.x, lightSource.y, lightSource.z);
		shader.setLightDiffuse(0.9f, 0.7f, 0.2f, 1);
		shader.setLightRange(20f);
		shader.setMaterialDiffuse(0.7f, 0.2f, 0, 1);
		shader.setLightSpecular(0.7f, 0.2f, 0, 1);
		shader.setMaterialSpecular(0.7f, 0.2f, 0, 1);
		shader.setMaterialShine(13);
		
		int vertexPointer = shader.getVertexPointer();
		int normalPointer = shader.getNormalPointer();
		
		BoxGraphic.create(vertexPointer, normalPointer);
		SphereGraphic.create(vertexPointer, normalPointer);
		SincGraphic.create(vertexPointer);
		CoordFrameGraphic.create(vertexPointer);

		Gdx.gl.glClearColor(0.5f, 0.2f, 0.3f, 1);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		firstPersonPlayer = new Player(3,3,3);
		firstPersonPlayer.playerCamera.PerspctiveProjection3D(80, 1, 1, 60);
		

		walls = new ArrayList<MazeWall>();
		
		MazeWall plane = new MazeWall(new Point3D(0,0,0), 20, 0.2f, 20);
		plane.setColor(0.2f, 0.6f, 0.3f);
		
		walls.add(plane);
		for(int i = 0; i<20; i++)
		{
			
			MazeWall wall = new MazeWall(new Point3D(5*i, 2 ,0), 0.2f, 2, 2);
			wall.setColor(0.7f, 0.3f, 0.8f);
			walls.add(wall);
			
			walls.add(new MazeWall(new Point3D(0, 2 ,5*i), 2, 2, 0.2f));	
		}
		
		movingWall = new MazeWall(new Point3D(10,0,10), new Point3D(0,0,0), 1, 1, 1, 5);
		walls.add(movingWall);
		
		//firstPersonPlayer.playerCamera.LookAt(new Point3D(0,0,1), new Vector3D(0,0,1));
		firstPersonPlayer.move(new Vector3D(2f, 3, 10f), walls);
		
		//game.start();
		
	}

	private void input()
	{
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
		}
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		Vector3D playerMovement = new Vector3D(0,0,0);
		
		if(wDown) {
			playerMovement.z -= 1;
		}
		if(sDown) {
			playerMovement.z += 1;
		}
		if(aDown) {
			playerMovement.x -= 1;
		}
		if(dDown) {
			playerMovement.x += 1;
		}
		if(upDown) {
			firstPersonPlayer.playerCamera.pitch(-0.8f);
		}
		if(downDown) {
			firstPersonPlayer.playerCamera.pitch(0.8f);
		}
		if(leftDown) {
			firstPersonPlayer.playerCamera.yaw(-1.6f);
		}
		if(rightDown) {
			firstPersonPlayer.playerCamera.yaw(1.6f);
		}
		if(qDown) {
			firstPersonPlayer.playerCamera.roll(0.4f);
		}
		if(eDown) {
			firstPersonPlayer.playerCamera.roll(-0.4f);
		}
		playerMovement.normalize();
		movingWall.move(deltaTime, firstPersonPlayer.playerCamera.eye, new Vector3D(firstPersonPlayer.width, firstPersonPlayer.height, firstPersonPlayer.depth));
		firstPersonPlayer.move(playerMovement, walls);		
		
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		for(MazeWall wall : walls) {
			shader.setMaterialDiffuse(wall.red, wall.green, wall.blue, 1);	
			shader.setModelMatrix(wall.getModelMatrix());
			wall.draw();
		}
		/*
		shader.setMaterialDiffuse(movingWall.red, movingWall.green, movingWall.blue, 1);	
		shader.setModelMatrix(movingWall.getModelMatrix());
		movingWall.draw();
		*/
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(lightSource.x, lightSource.y + 7, lightSource.z);
		ModelMatrix.main.addScale(2,2,2);
		shader.setModelMatrix(ModelMatrix.main.matrix);
		SphereGraphic.drawSolidSphere();
		ModelMatrix.main.popMatrix();
		
		Point3D cameraPosVec = firstPersonPlayer.playerCamera.eye;
		shader.setEyePosition(cameraPosVec.x, cameraPosVec.y, cameraPosVec.z);
		shader.setProjectionMatrix(firstPersonPlayer.playerCamera.getProjectionMatrix());
		shader.setViewMatrix(firstPersonPlayer.playerCamera.getViewMatrix());
	}

	@Override
	public void render () {
		
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}



	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if(keycode == Input.Keys.W) {
			wDown = true;
		}
		if(keycode == Input.Keys.A) {
			aDown = true;
		}
		if(keycode == Input.Keys.S) {
			sDown = true;
		}
		if(keycode == Input.Keys.D) {
			dDown = true;
		}
		if(keycode == Input.Keys.UP) {
			upDown = true;
		}
		if(keycode == Input.Keys.DOWN) {
			downDown = true;
		}
		if(keycode == Input.Keys.LEFT) {
			leftDown = true;
		}
		if(keycode == Input.Keys.RIGHT) {
			rightDown = true;
		}
		if(keycode == Input.Keys.Q) {
			qDown = true;
		}
		if(keycode == Input.Keys.E) {
			eDown = true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		
		if(keycode == Input.Keys.W) {
			wDown = false;
		}
		if(keycode == Input.Keys.A) {
			aDown = false;
		}
		if(keycode == Input.Keys.S) {
			sDown = false;
		}
		if(keycode == Input.Keys.D) {
			dDown = false;
		}
		if(keycode == Input.Keys.UP) {
			upDown = false;
		}
		if(keycode == Input.Keys.DOWN) {
			downDown = false;
		}
		if(keycode == Input.Keys.LEFT) {
			leftDown = false;
		}
		if(keycode == Input.Keys.RIGHT) {
			rightDown = false;
		}
		if(keycode == Input.Keys.Q) {
			qDown = false;
		}
		if(keycode == Input.Keys.E) {
			eDown = false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}