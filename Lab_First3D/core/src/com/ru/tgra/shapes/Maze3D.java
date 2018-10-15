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
	private Camera orthographicCam;	
	boolean orthographicCamEnabled = true;
	
	private Camera thirdPersonCam;
	boolean thirdPersonCamEnabled = false;
	private boolean wDown, aDown, sDown, dDown, upDown, downDown, leftDown, rightDown, qDown, eDown;
	
	private List<MazeWall> walls; 
	MazeWall movingWall;
	
	private List<PointLight> pointLights;
	private DirectionalLight dirLight;
	
	Maze maze;
	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(this);
		shader = new Shader();
		
		pointLights = new ArrayList<PointLight>();
		
		//These pointlights will move in a circular motion. NOTE that the points they start at are not multiplied by the model//These pointlights will move in a circular motion. NOTE that the points they start at are not multiplied by the point matrix, so they are not
		//Representative of where they actually show up in the world, but with the values we randomly decided to put in we got an effect we were happy with.
		//While less intuitive, it works well enough for us. matrix, so they are not
		pointLights.add(new PointLight(0, new Point3D(50,10,50), new Vector3D(0.8f, 0.7f, 0.2f),new Vector3D(0.7f, 0.2f, 0), 30));
		pointLights.add(new PointLight(1, new Point3D(50,20,50), new Vector3D(0.8f, 0.2f, 0.4f),new Vector3D(0.2f, 0.2f, 0), 35));
		pointLights.add(new PointLight(2, new Point3D(50,20,0), new Vector3D(0.5f, 0.5f, 0.8f),new Vector3D(0.2f, 0.2f, 0), 25));
		pointLights.add(new PointLight(3, new Point3D(-50,10,50), new Vector3D(0.2f, 0.5f, 0.8f),new Vector3D(0.2f, 0.2f, 0), 30));
		
		for(PointLight pL : pointLights) {
			pL.fetchLocs(shader);
			pL.updateShaderValues();
		}
		
		dirLight = new DirectionalLight(new Vector3D(19, 12, -1),new Vector3D(0.5f, 0.5f, 0.2f),new Vector3D(0.1f, 0.2f, 0.3f));
		dirLight.fetchLocs(shader);
		dirLight.updateShaderValues();

		shader.setMaterialDiffuse(0.7f, 0.2f, 0, 1);
		shader.setMaterialSpecular(0.7f, 0.2f, 0, 1);
		shader.setMaterialShine(13);
		
		int vertexPointer = shader.getVertexPointer();
		int normalPointer = shader.getNormalPointer();
		
		BoxGraphic.create(vertexPointer, normalPointer);
		SphereGraphic.create(vertexPointer, normalPointer);
		SincGraphic.create(vertexPointer);
		CoordFrameGraphic.create(vertexPointer);
		FileModel.create(vertexPointer, normalPointer);

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.2f, 1);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
				
		int mazeWidth = 21;
		
		maze = new Maze(mazeWidth, mazeWidth);

		firstPersonPlayer = new Player(3,3,3);

		firstPersonPlayer.playerCamera.PerspctiveProjection3D(80, 1, 1, 150);
		
		firstPersonPlayer.moveToStart(maze.playerStartPosition);
		//firstPersonPlayer.move(new Vector3D(2f, 15, 10f), maze.wallList);
		firstPersonPlayer.playerCamera.LookAt(new Point3D(0,0,0), new Vector3D(0,1,0));
		
		orthographicCam = new Camera();
		orthographicCam.OrthographicProjection3D(-(mazeWidth*maze.blockWidth+10), (mazeWidth*maze.blockWidth+10), -(mazeWidth*maze.blockHeight+10), (mazeWidth*maze.blockHeight+10), 1, 250);
		orthographicCam.eye = new Point3D(maze.blockWidth*(maze.width - 1), firstPersonPlayer.playerCamera.eye.y+20, 0);
		orthographicCam.LookAt(new Point3D(maze.blockWidth*(maze.width - 1), firstPersonPlayer.playerCamera.eye.y, 0), new Vector3D(0,0,-1));
		
		thirdPersonCam = new Camera();
		thirdPersonCam.PerspctiveProjection3D(70, 1, 1f, 180);
		thirdPersonCam.eye.set(firstPersonPlayer.playerCamera.eye.x + 10, firstPersonPlayer.playerCamera.eye.y + 10, firstPersonPlayer.playerCamera.eye.z + 10);
		thirdPersonCam.LookAt(firstPersonPlayer.playerCamera.eye, new Vector3D(0, 1, 0));
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
	
	private void update() {
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
		if(!thirdPersonCamEnabled) {
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
		}
		playerMovement.normalize();
		firstPersonPlayer.move(playerMovement, maze.wallList);
		
		for(MazeWall wall : maze.wallList) {
			if(wall == null) {
				continue;
			}
			wall.move(deltaTime, firstPersonPlayer.playerCamera.eye, new Vector3D(firstPersonPlayer.width, firstPersonPlayer.height, firstPersonPlayer.depth));
		}
		
		for(PointLight pL : pointLights) {
			pL.move(deltaTime);
			pL.updateShaderValues();
		}
		
		thirdPersonCam.eye.set(firstPersonPlayer.playerCamera.eye.x + 10, firstPersonPlayer.playerCamera.eye.y + 20, firstPersonPlayer.playerCamera.eye.z);
		thirdPersonCam.LookAt(firstPersonPlayer.playerCamera.eye, new Vector3D(0, 1, 0));
		
	}
	
	private void display() {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		for(int i = 0; i < 3; i++) {
			if(i == 0) {
				if(thirdPersonCamEnabled)
				{
					continue;
				}
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				Point3D cameraPosVec = firstPersonPlayer.playerCamera.eye;
				shader.setEyePosition(cameraPosVec.x, cameraPosVec.y, cameraPosVec.z);
				shader.setProjectionMatrix(firstPersonPlayer.playerCamera.getProjectionMatrix());
				shader.setViewMatrix(firstPersonPlayer.playerCamera.getViewMatrix());	
				

				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(5*5, 60, 0);
				ModelMatrix.main.addScale(5,5,5);
				shader.setModelMatrix(ModelMatrix.main.matrix);
				FileModel.draw();
				ModelMatrix.main.popMatrix();
			}
			else if (i == 1) {
				if(!orthographicCamEnabled) {
					continue;
				}
				Gdx.gl.glViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				Point3D cameraPosVec = orthographicCam.eye;
				shader.setEyePosition(cameraPosVec.x, cameraPosVec.y, cameraPosVec.z);
				shader.setProjectionMatrix(orthographicCam.getProjectionMatrix());
				shader.setViewMatrix(orthographicCam.getViewMatrix());
				
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(firstPersonPlayer.playerCamera.eye.x, 20, firstPersonPlayer.playerCamera.eye.z);
				ModelMatrix.main.addScale(3,3,3);
				shader.setModelMatrix(ModelMatrix.main.matrix);
				SphereGraphic.drawSolidSphere();
				ModelMatrix.main.popMatrix();
			}
			else if(i == 2) {
				if(!thirdPersonCamEnabled) {
					continue;
				}

				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				
				Point3D cameraPosVec = thirdPersonCam.eye;
				shader.setEyePosition(cameraPosVec.x, cameraPosVec.y, cameraPosVec.z);
				shader.setProjectionMatrix(thirdPersonCam.getProjectionMatrix());
				shader.setViewMatrix(thirdPersonCam.getViewMatrix());
				
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(firstPersonPlayer.playerCamera.eye.x, firstPersonPlayer.playerCamera.eye.y, firstPersonPlayer.playerCamera.eye.z);

				//ModelMatrix.main.addRotationX(180);
				ModelMatrix.main.addRotationZ(90);
				ModelMatrix.main.addRotationY(90);
				ModelMatrix.main.addScale(0.2f,0.2f,0.2f);
				shader.setModelMatrix(ModelMatrix.main.matrix);
				FileModel.draw();
				ModelMatrix.main.popMatrix();
			}

			
			for(MazeWall wall : maze.wallList) {
				if(wall == null) {
					continue;
				}
				shader.setMaterialDiffuse(wall.red, wall.green, wall.blue, 1);	
				shader.setModelMatrix(wall.getModelMatrix());
				wall.draw();	
			}
		}
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
		if(keycode == Input.Keys.SPACE) {
			orthographicCamEnabled = !orthographicCamEnabled;
			thirdPersonCamEnabled = !thirdPersonCamEnabled;
			firstPersonPlayer.playerCamera.LookAt(new Point3D(firstPersonPlayer.playerCamera.eye.x - 5, firstPersonPlayer.playerCamera.eye.y, firstPersonPlayer.playerCamera.eye.z), new Vector3D(0,1,0));
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