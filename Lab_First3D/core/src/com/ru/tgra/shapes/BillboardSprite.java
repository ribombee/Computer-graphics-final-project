package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BillboardSprite {
	private static Texture image = null;
	Point3D position;
	Vector3D size;
	Quaternion rotation;
	
	public BillboardSprite(Point3D position, Vector3D size) {
		if(image == null) {
			image = new Texture(Gdx.files.internal("textures/pillr.png"));
		}
		this.position = position;
		this.size = size;
		rotation = new Quaternion();
	}
	public void lookAt(Point3D origin, Vector3D up) {
		Vector3D direction = new Vector3D(origin.x - position.x, origin.y - position.y, origin.z - position.z);
		direction.normalize();
		
		Vector3D temp1 = up.cross(direction);
		Vector3D temp2 = direction.cross(temp1);
		

		rotation.setFromAxes(false, temp1.x, temp2.x, direction.x, temp1.y, temp2.y, direction.y, temp1.z, temp2.z, direction.z);
	}
	public void draw() {
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addRotationQuaternion(rotation.x, rotation.y, rotation.z, rotation.w);
		ModelMatrix.main.addScale(size.x,size.y,size.z);
		
		Shader.mainShader.setModelMatrix(ModelMatrix.main.matrix);
		//BoxGraphic.drawSolidCube(null,null);
		SpriteGraphic.drawSprite(image);
		ModelMatrix.main.popMatrix();
	}
}
