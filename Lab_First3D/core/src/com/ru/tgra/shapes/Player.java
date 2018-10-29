package com.ru.tgra.shapes;

import java.util.List;

public class Player {

	public Camera playerCamera;
	public float maxSpeed;
	
	public float width;
	public float height;
	public float depth;
	
	public float gravityFactor;
	public float verticalVelocity;
	private boolean grounded;
	
	public Player(float w, float h, float d) {
		playerCamera = new Camera();
		maxSpeed = 20f;
		
		width = w;
		height = h;
		depth = d;
	}
	
	public void moveToStart(Point3D start) {
		//start = playerCamera.GetTranslationVector(start);
		Vector3D toStart = new Vector3D(start.x, start.y, start.z);
		playerCamera.translate(toStart);
	}
	
	public void jump(float initialVelocity) {
		if(grounded)
			verticalVelocity = initialVelocity;
	}
	
	public void move(Vector3D direction, List<MazeWall> cubeObjects) {
		verticalVelocity -= gravityFactor;
		direction = playerCamera.GetTranslationVector(direction);
		direction.scale(maxSpeed);
		direction.add(new Vector3D(0,verticalVelocity,0));
		direction.scale(Maze3D.deltaTime);
		direction = collides(direction, cubeObjects);
		playerCamera.translate(direction);
	}
	
	private Vector3D collides(Vector3D speed, List<MazeWall> cubeObjects) {
		grounded = false;
		float slack = 0.1f;
		for(MazeWall cube : cubeObjects) {
			boolean inXRange = false;
			boolean inYRange = false;
			boolean inZRange = false;
			
			Point3D position = playerCamera.eye;
			
			if(cube.position.x + cube.width/2 > position.x + speed.x- width/2 && cube.position.x - cube.width/2 < position.x + speed.x + width/2) {
				inXRange = true;
			}
			
			if(cube.position.y + cube.height/2 > position.y + speed.y- height/2 && cube.position.y - cube.height/2 < position.y + speed.y + height/2) {
				inYRange = true;
			}
			
			if(cube.position.z + cube.depth/2 > position.z + speed.z- depth/2 && cube.position.z - cube.depth/2 < position.z + speed.z + depth/2) {
				inZRange = true;
			}
			
			if(inXRange && inZRange && inYRange) {
				//Negative distance = behind, Positive distance = in front
				float xDistance = position.x - cube.position.x;
				float yDistance = position.y - cube.position.y;
				float zDistance = position.z - cube.position.z;
				
				if(xDistance < 0) {
					xDistance += width / 2 + cube.width / 2;
					if(xDistance <= speed.x) {
						speed.x = -xDistance;
					}
				}
				else {
					xDistance -= width / 2 + cube.width / 2;
					if(xDistance >= speed.x) {
						speed.x = -xDistance;
					}
				}
				
				if(yDistance < 0) {
					yDistance += height / 2 + cube.height / 2;
					if(yDistance <= speed.y) {
						speed.y = -yDistance;
					}
				}
				else {
					yDistance -= height / 2 + cube.height / 2;
					if(yDistance >= speed.y) {
						speed.y = -yDistance;
						//Player touched the ground
						verticalVelocity = 0;
						grounded = true;
					}
				}
				
				if(zDistance < 0) {
					zDistance += depth / 2 + cube.depth/2;
					if(zDistance <= speed.z) {
						speed.z = -zDistance;
					}
				}
				else {
					zDistance -= depth / 2 + cube.depth/2;
					if(zDistance >= speed.z) {
						speed.z = -zDistance;
					}
				}
			}
		}
		return speed;
	}
	
}
