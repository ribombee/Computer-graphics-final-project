package com.ru.tgra.shapes;

import java.nio.FloatBuffer;

public class MazeWall {
	public Point3D position;
	public float width;
	public float height;
	public float depth;
	
	public MazeWall(Point3D p, float w, float h, float d) {
		position = p;
		width = w;
		height = h;
		depth = d;
	}
	
	
	public FloatBuffer getModelMatrix() {
		ModelMatrix matrix = new ModelMatrix();
		matrix.loadIdentityMatrix();
		matrix.addTranslationBaseCoords(position.x, position.y, position.z);
		matrix.addScale(width, height, depth);
		
		return matrix.matrix;
	}
	
	public void draw() {
		BoxGraphic.drawSolidCube();
	}
}
