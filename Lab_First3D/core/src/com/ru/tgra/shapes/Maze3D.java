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

	public static FloatBuffer matrixBuffer;

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;

	public static int modelMatrixLoc;
	public static int viewMatrixLoc;
	public static int projectionMatrixLoc;

	private int colorLoc;
	
	private Player firstPersonPlayer;
	
	private boolean wDown, aDown, sDown, dDown;
	
	private MazeWall plane;
	private List<MazeWall> walls; 

	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(this);

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
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		colorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_color");

		Gdx.gl.glUseProgram(renderingProgramID);

		OrthographicProjection3D(0, Gdx.graphics.getWidth(), 0, Gdx.graphics.getHeight(), -1, 1);
/*
		float[] mm = new float[16];

		mm[0] = 1.0f; mm[4] = 0.0f; mm[8] = 0.0f; mm[12] = 0.0f;
		mm[1] = 0.0f; mm[5] = 1.0f; mm[9] = 0.0f; mm[13] = 0.0f;
		mm[2] = 0.0f; mm[6] = 0.0f; mm[10] = 1.0f; mm[14] = 0.0f;
		mm[3] = 0.0f; mm[7] = 0.0f; mm[11] = 0.0f; mm[15] = 1.0f;

		modelMatrixBuffer = BufferUtils.newFloatBuffer(16);
		modelMatrixBuffer.put(mm);
		modelMatrixBuffer.rewind();

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrixBuffer);
*/
		//COLOR IS SET HERE
		Gdx.gl.glUniform4f(colorLoc, 0.7f, 0.2f, 0, 1);

		BoxGraphic.create(positionLoc, normalLoc);
		SphereGraphic.create(positionLoc, normalLoc);
		SincGraphic.create(positionLoc);
		CoordFrameGraphic.create(positionLoc);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.setShaderMatrix(modelMatrixLoc);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		//OrthographicProjection3D(-2, 2, -2, 2, 1, 100);
		PerspctiveProjection3D();
		//Look3D(new Point3D(20.5f, 10.2f, 20.0f), new Point3D(0,0,0), new Vector3D(0,1,0));
		
		firstPersonPlayer = new Player();
		
		plane = new MazeWall(new Point3D(0,0,0), 100, 0.2f, 100);
		walls = new ArrayList<MazeWall>();
		
		firstPersonPlayer.playerCamera.LookAt(new Point3D(0,0,0), new Vector3D(0,1,0));
		firstPersonPlayer.playerCamera.translate(new Vector3D(20.5f, 10.2f, 20.0f));
		
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
		playerMovement.normalize();
		firstPersonPlayer.move(playerMovement);
		
		float x = Gdx.input.getDeltaX();
		float y = Gdx.input.getDeltaY();
		float ratioXY = 0;
		
		if(y != 0) {
			ratioXY = x/y;
		}
		
		firstPersonPlayer.playerCamera.pitch(y * 0.2f);
		firstPersonPlayer.playerCamera.yaw(x * 0.2f);
		

		//angle += 180.0f * deltaTime;

		//do all updates to the game
		
		
		
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl.glUniform4f(colorLoc, 0.9f, 0.3f, 0.1f, 1.0f);

		ModelMatrix.main.loadIdentityMatrix();

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslationBaseCoords(9f, 5f, -2f);
		ModelMatrix.main.addScale(2.0f, 2.0f, 2.0f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslationBaseCoords(5, -0.8f, -5);
		ModelMatrix.main.addScale(10f,  0.8f,  10f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
		
		plane.render();
		
		firstPersonPlayer.playerCamera.setShaderMatrix();
	}

	@Override
	public void render () {
		
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}

	private void OrthographicProjection3D(float left, float right, float bottom, float top, float near, float far) {
		float[] pm = new float[16];

		pm[0] = 2.0f / (right - left); pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = -(right + left) / (right - left);
		pm[1] = 0.0f; pm[5] = 2.0f / (top - bottom); pm[9] = 0.0f; pm[13] = -(top + bottom) / (top - bottom);
		pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = 2.0f / (near - far); pm[14] = (near + far) / (near - far);
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;

		matrixBuffer = BufferUtils.newFloatBuffer(16);
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrixBuffer);

		pm[0] = 1.0f; pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = 0.0f;
		pm[1] = 0.0f; pm[5] = 1.0f; pm[9] = 0.0f; pm[13] = 0.0f;
		pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = 1.0f; pm[14] = 0.0f;
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;

		matrixBuffer = BufferUtils.newFloatBuffer(16);
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrixBuffer);
	}

	private void PerspctiveProjection3D() {
		float[] pm = new float[16];

		pm[0] = 1.0f; pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = 0.0f;
		pm[1] = 0.0f; pm[5] = 1.0f; pm[9] = 0.0f; pm[13] = 0.0f;
		pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = -1.02f; pm[14] = -2.02f;
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = -1.0f; pm[15] = 0.0f;

		matrixBuffer = BufferUtils.newFloatBuffer(16);
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrixBuffer);

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