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
	public static float deltaTime;
	private Shader shader;
	
	private Player firstPersonPlayer;
	private Camera orthographicCam;	
	boolean orthographicCamEnabled = true;
	
	private Camera thirdPersonCam;
	boolean thirdPersonCamEnabled = false;
	private boolean wDown, aDown, sDown, dDown;
	
	//0 = Loss
	//1 = level 1
	//2 = level 2
	//etc.. if we manage..
	private int phase;
	
	private World world;
	private DeathWall deathWall; //The wall that kills you
	
	private List<PointLight> pointLights;
	private DirectionalLight dirLight;
	private Skybox skybox;
	Maze maze;
	ModelTest test;
	@Override
	public void create () {
		
		phase = 1;
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.input.setInputProcessor(this);
		Gdx.input.setCursorCatched(true);
		shader = new Shader();
		
		world = new World(5,200);

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
		
		dirLight = new DirectionalLight(new Vector3D(-19, 12, 1),new Vector3D(0.5f, 0.5f, 0.2f),new Vector3D(0.1f, 0.2f, 0.3f));
		dirLight.fetchLocs(shader);
		dirLight.updateShaderValues();

		shader.setMaterialDiffuse(0.7f, 0.2f, 0, 1);
		shader.setMaterialSpecular(0.7f, 0.2f, 0, 1);
		shader.setMaterialShine(13);
		shader.setGlobalAmbient(0.3f);
		
		int vertexPointer = shader.getVertexPointer();
		int normalPointer = shader.getNormalPointer();
		int UVPointer = shader.getUVPointer();
		
		BoxGraphic.create(vertexPointer, normalPointer, UVPointer);
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

		firstPersonPlayer = new Player(3,7,3, 27f);
		firstPersonPlayer.gravityFactor = 2f;

		firstPersonPlayer.playerCamera.PerspctiveProjection3D(80, 1, 1f, 400);
		
		firstPersonPlayer.moveToStart(new Point3D(20,50,20));
		firstPersonPlayer.playerCamera.LookAt(new Point3D(firstPersonPlayer.playerCamera.eye.x,firstPersonPlayer.playerCamera.eye.y,firstPersonPlayer.playerCamera.eye.z + 1), new Vector3D(0,1,0));
		//firstPersonPlayer.move(new Vector3D(0,0,0), new ArrayList<MazeWall>());
		
		deathWall = new DeathWall(new Point3D(0,0,0),800, 400, 300, 5);

		Point3D deathWallStart = new Point3D(world.blockWidth*world.width/2, 0, firstPersonPlayer.playerCamera.eye.z - deathWall.depth/2 - 50);
		deathWall.position = deathWallStart;
		
		orthographicCam = new Camera();

		orthographicCam.OrthographicProjection3D(-20, 20, -40, 40, 2f, 400);
		//orthographicCam.OrthographicProjection3D(-(world.blockDepth*8), (world.blockDepth*8), -(world.width*world.blockWidth), (world.width*world.blockWidth), 2f, 400);
		orthographicCam.eye = new Point3D(deathWall.position.x, deathWall.position.y + deathWall.height/2 + 20, 0);
		orthographicCam.LookAt(new Point3D(deathWall.position.x, deathWall.position.y + deathWall.height/2, 0), new Vector3D(0,0,-1));

		skybox = new Skybox();

		test = new ModelTest(new Point3D(firstPersonPlayer.playerCamera.eye.x, firstPersonPlayer.playerCamera.eye.y + 10, firstPersonPlayer.playerCamera.eye.z + 10));
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
		
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		deltaTime = Gdx.graphics.getDeltaTime();
		
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
		
		firstPersonPlayer.lookX(-Gdx.input.getDeltaX()*0.2f);
		firstPersonPlayer.lookY(Gdx.input.getDeltaY()*0.1f);
		
		playerMovement.normalize();
		firstPersonPlayer.move(playerMovement, world);
		
		for(MazeWall wall : world.blockList) {
			//NOTE THIS DOES NOTHING WITHOUT MOVING WALLS
			if(wall == null) {
				continue;
			}
			wall.move(deltaTime, firstPersonPlayer.playerCamera.eye, new Vector3D(firstPersonPlayer.width, firstPersonPlayer.height, firstPersonPlayer.depth));
		}
		
		for(PointLight pL : pointLights) {
			pL.move(deltaTime);
			pL.updateShaderValues();
		}
		
		
		world.move(deltaTime);
		
		if(firstPersonPlayer.playerCamera.eye.z / world.blockDepth > world.currentZGenerationIndex - 200)
		{
			world.addAdditionalZ(30);
		}
		
		deathWall.move(firstPersonPlayer.playerCamera.eye);
		
		skybox.position.set(firstPersonPlayer.playerCamera.eye.x, firstPersonPlayer.playerCamera.eye.y, firstPersonPlayer.playerCamera.eye.z);
		//test.position.set(firstPersonPlayer.playerCamera.eye.x, firstPersonPlayer.playerCamera.eye.y + 10, firstPersonPlayer.playerCamera.eye.z + 10);
	}
	
	private void display() {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		for(int i = 0; i < 2; i++) {
			if(i == 1) {
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				Point3D cameraPosVec = firstPersonPlayer.playerCamera.eye;
				shader.setEyePosition(cameraPosVec.x, cameraPosVec.y, cameraPosVec.z);
				shader.setProjectionMatrix(firstPersonPlayer.playerCamera.getProjectionMatrix());
				shader.setViewMatrix(firstPersonPlayer.playerCamera.getViewMatrix());	
				

				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(5*5, 60, 0);
				ModelMatrix.main.addScale(5,5,5);
				shader.setModelMatrix(ModelMatrix.main.matrix);
				//FileModel.draw();
				ModelMatrix.main.popMatrix();
				
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(skybox.position.x, skybox.position.y, skybox.position.z);
				ModelMatrix.main.addScale(skybox.size.x,skybox.size.y,skybox.size.z);
				shader.setModelMatrix(ModelMatrix.main.matrix);
				skybox.draw();
				ModelMatrix.main.popMatrix();
				
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(orthographicCam.eye.x, orthographicCam.eye.y, orthographicCam.eye.z);
				ModelMatrix.main.addScale(2,2,2);
				shader.setModelMatrix(ModelMatrix.main.matrix);
				SphereGraphic.drawSolidSphere();
				ModelMatrix.main.popMatrix();
			}
			else if (i == 0) {
				if(!orthographicCamEnabled) {
					continue;
				}
				orthographicCam.eye.set(orthographicCam.eye.x, orthographicCam.eye.y, firstPersonPlayer.playerCamera.eye.z );
				Gdx.gl.glViewport(Gdx.graphics.getWidth() * 3/4, Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight() *3/4);
				//Gdx.gl.glScissor(x, y, width, height);
				
				Point3D cameraPosVec = orthographicCam.eye;
				shader.setEyePosition(cameraPosVec.x, cameraPosVec.y, cameraPosVec.z);
				shader.setProjectionMatrix(orthographicCam.getProjectionMatrix());
				shader.setViewMatrix(orthographicCam.getViewMatrix());
				
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(firstPersonPlayer.playerCamera.eye.x, firstPersonPlayer.playerCamera.eye.y, firstPersonPlayer.playerCamera.eye.z);
				ModelMatrix.main.addScale(1,1,1);
				shader.setModelMatrix(ModelMatrix.main.matrix);
				SphereGraphic.drawSolidSphere();
				ModelMatrix.main.popMatrix();
			}


			test.draw();
			//General terrain
			for(MazeWall wall : world.blockList) {
				if(wall == null) {
					continue;
				}
				
				boolean willBeRendered = wall.position.z > deathWall.position.z;
				
				willBeRendered = willBeRendered && wall.position.z < firstPersonPlayer.playerCamera.eye.z + 200;
				
				if(willBeRendered)
				{
					//shader.setMaterialDiffuse(wall.red, wall.green, wall.blue, 1);	
					shader.setModelMatrix(wall.getModelMatrix());
					wall.draw();
				}
			}
			
			for(Obstacle pillar : world.obstacles) {
				if(pillar == null) {
					continue;
				}
				
				boolean willBeRendered = pillar.position.z > deathWall.position.z;
				
				willBeRendered = willBeRendered && pillar.position.z < firstPersonPlayer.playerCamera.eye.z + 200;
				
				if(willBeRendered)
				{
					pillar.draw(shader);
				}
			}
			
			//Death wall
			
			shader.setMaterialDiffuse(1,1,1,0.5f);	
			shader.setModelMatrix(deathWall.getModelMatrix());
			deathWall.draw();
			
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
		if(keycode == Input.Keys.SPACE) {
			firstPersonPlayer.jump(60);
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