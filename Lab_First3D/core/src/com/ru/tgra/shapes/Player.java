package com.ru.tgra.shapes;

import java.util.List;

public class Player {

	public Camera playerCamera;
	public float maxSpeed;
	
	private float width;
	private float height;
	private float depth;
	
	public Player(float w, float h, float d) {
		playerCamera = new Camera();
		maxSpeed = 0.3f;
		
		width = w;
		height = h;
		depth = d;
	}
	
	public void move(Vector3D speed, List<MazeWall> cubeObjects) {
		speed.scale(maxSpeed);
		speed = collides(speed, cubeObjects);
		playerCamera.translate(speed);
	}
	
	private Vector3D collides(Vector3D speed, List<MazeWall> cubeObjects) {
		
		//System.out.println("Eye: " + playerCamera.eye.x + ", " + playerCamera.eye.y + ", " + playerCamera.eye.z);
		
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
			
			if(inXRange && inYRange && inZRange) {
				
				if(speed.x > 0 && cube.position.x - cube.width/2 - (position.x +width/2) < speed.x) {
					speed.x = cube.position.x - cube.width/2 - (position.x +width/2);
					
					System.out.println("ADJUSTED X SPEED TO: " + speed.x);
				}
				else if(speed.x < 0 && position.x +width/2 - (cube.position.x - cube.width/2) > speed.x){
					speed.x =  position.x +width/2 - (cube.position.x - cube.width/2);
					
					System.out.println("ADJUSTED X SPEED TO: " + speed.x);
				}
				
				if(speed.y > 0 && cube.position.y - cube.height/2 - (position.y +height/2) < speed.y) {
					speed.y = cube.position.y - cube.height/2 - (position.y +height/2);
					
					System.out.println("ADJUSTED Y SPEED TO: " + speed.y);
				}
				else if(speed.y < 0 && position.y +height/2 - (cube.position.y - cube.height/2) > speed.y){
					speed.y =  position.y +height/2 - (cube.position.y - cube.height/2);
					
					System.out.println("ADJUSTED Y SPEED TO: " + speed.y);
				}
				
				if(speed.z > 0 && cube.position.z - cube.depth/2 - (position.z +depth/2) < speed.z) {
					speed.z = cube.position.z - cube.depth/2 - (position.z +depth/2);
					
					System.out.println("ADJUSTED Z SPEED TO: " + speed.z);
				}
				else if(speed.z < 0 && position.z +depth/2 - (cube.position.z - cube.depth/2) > speed.z){
					speed.z =  position.z +depth/2 - (cube.position.z - cube.depth/2);
					
					System.out.println("ADJUSTED Z SPEED TO: " + speed.z);
				}
				
				System.out.println("COLLISION");
			}
		}
		return speed;
	}
	
}
