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

	private static int faceCount;
	private static int faceSize;
	public static void create(int vertexPointer, int normalPointer) {
		FileModel.vertexPointer = vertexPointer;
		FileModel.normalPointer = normalPointer;
		String filePosition = "models/Cat.obj";
		readFile(filePosition);
	}
	
	private static void readFile(String path) {
		ArrayList<float[]> vertices = new ArrayList<float[]>();
		ArrayList<float[]> normals = new ArrayList<float[]>();
		ArrayList<float[]> facesVertices = new ArrayList<float[]>();
		ArrayList<float[]> facesNormals = new ArrayList<float[]>();
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
					if(splitLine[0].compareToIgnoreCase("f") == 0) {
						int substrStart = 2;
						if(line.substring(1, 3).compareTo("  ") == 0) {
							substrStart = 3;
						}
						splitLine = line.substring(substrStart, line.length()).split(" ");
						
						faceSize = splitLine.length;
						
						for(int i = 0; i<faceSize; i++) {
							String[] splitValue = splitLine[i].split("[\\/\\\\]+");
							int vertexIndex = Integer.parseInt(splitValue[0]) - 1;
							int normalIndex = 0;
							if(splitValue.length > 2) {
								normalIndex = Integer.parseInt(splitValue[2]) - 1;
							}
							else {
								normalIndex = Integer.parseInt(splitValue[1]) - 1;
							}
							facesVertices.add(vertices.get(vertexIndex));
							facesNormals.add(normals.get(normalIndex));
						}
					}
				}
				line = reader.readLine();
			}
			reader.close();
			
			faceCount = facesVertices.size();
			System.out.println(faceCount);
			System.out.println(faceSize);
			vertexBuffer = BufferUtils.newFloatBuffer(faceCount*3);
			normalBuffer = BufferUtils.newFloatBuffer(faceCount*3);
			for(int i = 0; i< faceCount; i += 1) {
				
				vertexBuffer.put(facesVertices.get(i)[0]);
				vertexBuffer.put(facesVertices.get(i)[1]);
				vertexBuffer.put(facesVertices.get(i)[2]);

				normalBuffer.put(facesNormals.get(i)[0]);
				normalBuffer.put(facesNormals.get(i)[1]);
				normalBuffer.put(facesNormals.get(i)[2]);
				
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

		for(int i = 0; i < faceCount; i += faceSize) {
			Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, i, faceSize);
		}
	}
}
