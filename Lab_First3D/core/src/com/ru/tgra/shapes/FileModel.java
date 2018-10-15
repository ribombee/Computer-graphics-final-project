package com.ru.tgra.shapes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

public class FileModel {
	private static FloatBuffer vertexBuffer;
	private static FloatBuffer normalBuffer;
	private static int vertexPointer;
	private static int normalPointer;

	private static int vertexCount;
	public static void create(int vertexPointer, int normalPointer) {
		FileModel.vertexPointer = vertexPointer;
		FileModel.normalPointer = normalPointer;
		String filePosition = "models/FinalBaseMesh.obj";
		readFile(filePosition);
	}
	
	private static void readFile(String path) {
		ArrayList<float[]> vertices = new ArrayList<float[]>();
		ArrayList<float[]> normals = new ArrayList<float[]>();
		//System.out.println();
		try {

			BufferedReader reader = Gdx.files.internal(path).reader(100000);
			String line = reader.readLine();
			while(line != null) {
				String[] splitLine = line.split(" ");
				if(splitLine.length > 0) {
					if(splitLine[0].compareToIgnoreCase("v") == 0) {
						float[] vertex = new float[3];
						vertex[0] = Float.parseFloat(splitLine[2]);
						vertex[1] = Float.parseFloat(splitLine[3]);
						vertex[2] = Float.parseFloat(splitLine[4]);
						vertices.add(vertex);
					}
					if(splitLine[0].compareToIgnoreCase("vn") == 0) {
						float[] vertex = new float[3];
						vertex[0] = Float.parseFloat(splitLine[1]);
						vertex[1] = Float.parseFloat(splitLine[2]);
						vertex[2] = Float.parseFloat(splitLine[3]);
						normals.add(vertex);
					}
				}
				line = reader.readLine();
			}
			reader.close();
			
			vertexCount = vertices.size();
			System.out.println(vertexCount);
			vertexBuffer = BufferUtils.newFloatBuffer(vertices.size()*3);
			normalBuffer = BufferUtils.newFloatBuffer(normals.size()*3);
			for(int i = 0; i<vertices.size(); i += 3)
			{
				vertexBuffer.put(i, vertices.get(i)[0]);
				vertexBuffer.put(i+1, vertices.get(i)[1]);
				vertexBuffer.put(i+2, vertices.get(i)[2]);

				normalBuffer.put(i, normals.get(i)[0]);
				normalBuffer.put(i+1, normals.get(i)[1]);
				normalBuffer.put(i+2, normals.get(i)[2]);
			}

			vertexBuffer.rewind();
			normalBuffer.rewind();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void draw() {

		Gdx.gl.glVertexAttribPointer(vertexPointer, 3, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glVertexAttribPointer(normalPointer, 3, GL20.GL_FLOAT, false, 0, normalBuffer);

		for(int i = 0; i < vertexCount; i += 3) {
			Gdx.gl.glDrawArrays(GL20.GL_LINE_STRIP, i, 3);
		}
		/*
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 4, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 8, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 12, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 16, 4);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 20, 4);
		*/
	}
}
