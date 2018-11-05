package com.ru.tgra.shapes;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

public class SpriteGraphic {
	private static FloatBuffer vertexBuffer;
	private static FloatBuffer normalBuffer;
	private static FloatBuffer UVBuffer;
	private static ShortBuffer indexBuffer;
	public static void create() {
		float[] vertexArray = {
				-0.5f, -0.5f, 0f, //FRONT
				-0.5f, 0.5f, 0f,
				0.5f, 0.5f, 0f,
				0.5f, -0.5f, 0f,
				};
		
		vertexBuffer = BufferUtils.newFloatBuffer(12);
		vertexBuffer.put(vertexArray);
		vertexBuffer.rewind();
		
		float[] UVArray = {
			1f, 1f,
			1f, 0f,
			0f, 0f,
			0f, 1f //FRONT
			
			};
		
		UVBuffer = BufferUtils.newFloatBuffer(8);
		UVBuffer.put(UVArray);
		UVBuffer.rewind();
		
		//NORMAL ARRAY IS FILLED HERE
		float[] normalArray = {
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f};
		
		normalBuffer = BufferUtils.newFloatBuffer(12);
		normalBuffer.put(normalArray);
		normalBuffer.rewind();
		
		short[] indexArray = { 0, 1, 2, 3 };
		indexBuffer = BufferUtils.newShortBuffer(4);
		indexBuffer.put(indexArray);
		indexBuffer.rewind();
	}
	
	public static void drawSprite(Texture tex) {
		Shader.mainShader.setDiffuseTexture(tex);
		Shader.mainShader.setMaterialDiffuse(1, 1, 1, 0);
		
		Gdx.gl.glVertexAttribPointer(Shader.mainShader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glVertexAttribPointer(Shader.mainShader.getNormalPointer(), 3, GL20.GL_FLOAT, false, 0, normalBuffer);
		Gdx.gl.glVertexAttribPointer(Shader.mainShader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, UVBuffer);
		
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLE_FAN, 4, GL20.GL_UNSIGNED_SHORT, indexBuffer);
	}
}
