package com.ru.tgra.shapes;

public class Player {

	public Camera playerCamera;
	public float maxSpeed;
	
	public Player() {
		playerCamera = new Camera();
		maxSpeed = 0.3f;
	}
	
	void move(Vector3D speed) {
		speed.scale(maxSpeed);
		playerCamera.translate(speed);
	}
	
}
