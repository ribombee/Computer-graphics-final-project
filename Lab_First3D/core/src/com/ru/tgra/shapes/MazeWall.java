package com.ru.tgra.shapes;

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
	
	public void render() {
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslationBaseCoords(position.x, position.y, position.z);
		ModelMatrix.main.addScale(width, height, depth);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}
	
}
