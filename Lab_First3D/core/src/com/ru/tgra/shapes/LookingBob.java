package com.ru.tgra.shapes;

import com.badlogic.gdx.graphics.Texture;

public class LookingBob {
	Point3D position;
	Texture texture;
	float yawAngle, pitchAngle, rollAngle;
	public LookingBob(Point3D position) {
		this.position = position;
	}
	public void lookAt(Point3D origin, Vector3D up) {
		Vector3D direction = new Vector3D(origin.x - position.x, origin.y - position.y, origin.z - position.z);
		direction.normalize();
		
		yawAngle = (float) (Math.atan2(direction.y, direction.x));
		pitchAngle = (float) (Math.asin(direction.z)) - (float)Math.PI/2.0f;
		
		Vector3D temp1 = new Vector3D(-direction.y, direction.x, 0);
		Vector3D temp2 = temp1.cross(direction);
		
		rollAngle = (float) Math.atan2(temp1.dot(up) / temp1.length(), temp2.dot(up) / temp2.length());
		rollAngle = (float) (rollAngle) ;
	}
	public void draw() {
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		Quaternion testQuat = new Quaternion();
		testQuat.setFromEulerAngles(-pitchAngle,0,0);
		ModelMatrix.main.addRotationQuaternion(testQuat.x, testQuat.y, testQuat.z, testQuat.w);
		ModelMatrix.main.addScale(50,50,50);

		Shader.mainShader.setModelMatrix(ModelMatrix.main.matrix);
		BobGraphic.draw();
		ModelMatrix.main.popMatrix();
	}
}
