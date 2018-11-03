package com.ru.tgra.shapes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class Player {

	public Camera playerCamera;
	public float maxSpeed;
	
	public float width;
	public float height;
	public float depth;
	
	public float gravityFactor;
	public float verticalVelocity;
	private boolean grounded;
	
	public float yRotation, xRotation;
	
	private ArrayList<MazeWall> collisionBuffer;
	
	public Player(float w, float h, float d, float speed) {
		playerCamera = new Camera();
		maxSpeed = speed;
		
		width = w;
		height = h;
		depth = d;
		
		collisionBuffer = new ArrayList<MazeWall>(8);
		collisionBuffer.ensureCapacity(8);
		yRotation = 0;
		xRotation = 0;
		Camera.activeCamera = playerCamera;
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
	
	public void move(Vector3D direction, World world) {
		verticalVelocity -= gravityFactor;
		direction = playerCamera.GetTranslationVector(direction);
		direction.scale(maxSpeed);
		direction.add(new Vector3D(0,verticalVelocity,0));
		direction.scale(Maze3D.deltaTime);
		
		boolean groundedOnAny = false;
		direction = collides(direction, world.blockList);
		groundedOnAny = groundedOnAny || grounded;
		
		for(Obstacle pillar : world.obstacles) {
			direction = collides(direction, pillar.blockList);
			groundedOnAny = groundedOnAny || grounded;
		}
		
		grounded = groundedOnAny;
		
		playerCamera.translate(direction);
	}
	
	public boolean collision(MazeWall cube, Vector3D speed) {
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
		
		return (inXRange && inZRange && inYRange);
	}
	
	private float resolveAxisCollision(Vector3D speed, MazeWall cube, char axis) {
		float position, distance, playerAxisSize, cubeAxisSize, axisSpeed;
		if(axis == 'x') {
			position = playerCamera.eye.x;
			distance = position - cube.position.x;
			playerAxisSize = width/2;
			cubeAxisSize = cube.width/2;
			axisSpeed = speed.x;
		}
		else if(axis == 'y') {
			position = playerCamera.eye.y;
			distance = position - cube.position.y;
			playerAxisSize = height/2;
			cubeAxisSize = cube.height/2;
			axisSpeed = speed.y;
		}
		else if(axis == 'z') {
			position = playerCamera.eye.z;
			distance = position - cube.position.z;
			playerAxisSize = depth/2;
			cubeAxisSize = cube.depth/2;
			axisSpeed = speed.z;
		}
		else {
			return 0;
		}
		if(distance <= 0) {
			distance += playerAxisSize + cubeAxisSize;
			if(distance <= axisSpeed) {
				if(axis == 'y') {
					verticalVelocity = 0;
				}
				//System.out.println("Testing1");
				return -distance;
			}
		}
		else {
			distance -= playerAxisSize + cubeAxisSize;
			if(distance >= axisSpeed) {
				if(axis == 'y') {
					verticalVelocity = 0;
					grounded = true;
				}
				//System.out.println("Testing2");
				return -distance;
			}
		}
		//System.out.println("Testing3");
		return axisSpeed;
	}
	private Vector3D collides(Vector3D speed, List<MazeWall> cubeObjects) {
		grounded = false;
		Point3D position = playerCamera.eye;
		int wallCollisionCounter = 0;
		for(MazeWall cube : cubeObjects) {
			if(collision(cube, speed) && wallCollisionCounter < 8)
			{
				collisionBuffer.add(cube);
			}
		}
		
		
		
		Comparator<MazeWall> playerDist = (MazeWall a, MazeWall b) -> {
			float aDist = new Vector3D(a.position.x - position.x, a.position.y - position.y, a.position.z - position.z).length();
			float bDist = new Vector3D(b.position.x - position.x, b.position.y - position.y, b.position.z - position.z).length();
		    return aDist >= bDist ? 1 : -1;
		};
		
		collisionBuffer.sort(playerDist);
		for(MazeWall cube : collisionBuffer) {
			if(collision(cube, speed)) {
				//Resolve for Y-axis
				speed.y = resolveAxisCollision(speed, cube, 'y');
			}
			if(collision(cube, speed)) {
				//Resolve for X-axis
				speed.x = resolveAxisCollision(speed, cube, 'x');
			}
			if(collision(cube, speed)) {
				//Resolve for Z-axis
				speed.z = resolveAxisCollision(speed, cube, 'z');
			}
		}
		collisionBuffer.clear();
		
		return speed;
	}
	
	public void lookY(float angle) {
		if(yRotation + angle < 90 && yRotation + angle > -90) {
			yRotation += angle;
			playerCamera.pitch(Gdx.input.getDeltaY()*0.1f);
		}
	}
	
	public void lookX(float angles) {
		xRotation += angles;
		
		//Perform simple rotation correction in case it gets too large
		if(xRotation > 360)
			xRotation -= 360;
		else if(xRotation < -360)
			xRotation += 360;
		
		playerCamera.rotateY(-Gdx.input.getDeltaX()*0.2f);
	}
}
