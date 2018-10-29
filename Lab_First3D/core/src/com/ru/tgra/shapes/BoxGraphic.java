package com.ru.tgra.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

public class BoxGraphic {

	private static FloatBuffer vertexBuffer;
	private static FloatBuffer normalBuffer;
	private static FloatBuffer UVBuffer;
	
	private static int vertexPointer;
	private static int normalPointer;
	private static int UVPointer;

	public static void create(int vertexPointer, int normalPointer, int uvPointer) {
		BoxGraphic.vertexPointer = vertexPointer;
		BoxGraphic.normalPointer = normalPointer;
		BoxGraphic.UVPointer = uvPointer;
		//VERTEX ARRAY IS FILLED HERE
		float[] vertexArray = {
						-0.5f, -0.5f, -0.5f, //FRONT
						-0.5f, 0.5f, -0.5f,
						0.5f, 0.5f, -0.5f,
						0.5f, -0.5f, -0.5f,
						
						-0.5f, -0.5f, 0.5f, //BACK
						-0.5f, 0.5f, 0.5f,
						0.5f, 0.5f, 0.5f,
						0.5f, -0.5f, 0.5f,
						
						-0.5f, -0.5f, -0.5f, //BOTTOM
						0.5f, -0.5f, -0.5f,
						0.5f, -0.5f, 0.5f,
						-0.5f, -0.5f, 0.5f,
						
						-0.5f, 0.5f, -0.5f, //TOP
						0.5f, 0.5f, -0.5f,
						0.5f, 0.5f, 0.5f,
						-0.5f, 0.5f, 0.5f,
						
						-0.5f, -0.5f, -0.5f, //RIGHT
						-0.5f, -0.5f, 0.5f,
						-0.5f, 0.5f, 0.5f,
						-0.5f, 0.5f, -0.5f,
						
						0.5f, -0.5f, -0.5f, //LEFT
						0.5f, -0.5f, 0.5f,
						0.5f, 0.5f, 0.5f,
						0.5f, 0.5f, -0.5f};
		
		vertexBuffer = BufferUtils.newFloatBuffer(72);
		vertexBuffer.put(vertexArray);
		vertexBuffer.rewind();
		
		float[] UVArray = {
				0.5f, 0.66666f, //FRONT
				0.5f, 0.33333f,
				0.25f, 0.33333f,
				0.25f, 0.66666f, 

				0.75f, 0.66666f, //BACK
				0.75f, 0.33333f,
				1.0f, 0.33333f,
				1.0f, 0.66666f,
				
				0.25f, 1, //BOTTOM
				0.25f, 0.66666f,
				0.5f, 0.66666f,
				0.5f, 1,
			
				0.5f, 0.33333f,	//TOP
				0.25f, 0.33333f, 
				0.25f, 0,
				0.5f, 0,
				
				0.5f, 0.66666f, //RIGHT
				0.75f, 0.66666f,
				0.75f, 0.33333f,
				0.5f, 0.33333f,
				
				0.25f, 0.66666f, //LEFT
				0, 0.66666f, 
				0, 0.33333f,
				0.25f, 0.33333f
		};
		
		UVBuffer = BufferUtils.newFloatBuffer(48);
		UVBuffer.put(UVArray);
		UVBuffer.rewind();
		
		//NORMAL ARRAY IS FILLED HERE
		float[] normalArray = {0.0f, 0.0f, -1.0f,
							0.0f, 0.0f, -1.0f,
							0.0f, 0.0f, -1.0f,
							0.0f, 0.0f, -1.0f,
							0.0f, 0.0f, 1.0f,
							0.0f, 0.0f, 1.0f,
							0.0f, 0.0f, 1.0f,
							0.0f, 0.0f, 1.0f,
							0.0f, -1.0f, 0.0f,
							0.0f, -1.0f, 0.0f,
							0.0f, -1.0f, 0.0f,
							0.0f, -1.0f, 0.0f,
							0.0f, 1.0f, 0.0f,
							0.0f, 1.0f, 0.0f,
							0.0f, 1.0f, 0.0f,
							0.0f, 1.0f, 0.0f,
							-1.0f, 0.0f, 0.0f,
							-1.0f, 0.0f, 0.0f,
							-1.0f, 0.0f, 0.0f,
							-1.0f, 0.0f, 0.0f,
							1.0f, 0.0f, 0.0f,
							1.0f, 0.0f, 0.0f,
							1.0f, 0.0f, 0.0f,
							1.0f, 0.0f, 0.0f};

		normalBuffer = BufferUtils.newFloatBuffer(72);
		normalBuffer.put(normalArray);
		normalBuffer.rewind();
	}

	public static void drawSolidCube(Texture tex, Texture spectex) {

		Shader.mainShader.setSpecularTexture(spectex);
		Shader.mainShader.setDiffuseTexture(tex);
		
		Gdx.gl.glVertexAttribPointer(Shader.mainShader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glVertexAttribPointer(normalPointer, 3, GL20.GL_FLOAT, false, 0, normalBuffer);
		Gdx.gl.glVertexAttribPointer(Shader.mainShader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, UVBuffer);
		
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 4, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 8, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 12, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 16, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 20, 4);

	}

	public static void drawOutlineCube() {

		Gdx.gl.glVertexAttribPointer(vertexPointer, 3, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glVertexAttribPointer(normalPointer, 3, GL20.GL_FLOAT, false, 0, normalBuffer);
		
		Gdx.gl.glDrawArrays(GL20.GL_LINE_LOOP, 0, 4);
		Gdx.gl.glDrawArrays(GL20.GL_LINE_LOOP, 4, 4);
		Gdx.gl.glDrawArrays(GL20.GL_LINE_LOOP, 8, 4);
		Gdx.gl.glDrawArrays(GL20.GL_LINE_LOOP, 12, 4);
		Gdx.gl.glDrawArrays(GL20.GL_LINE_LOOP, 16, 4);
		Gdx.gl.glDrawArrays(GL20.GL_LINE_LOOP, 20, 4);
	}

}
