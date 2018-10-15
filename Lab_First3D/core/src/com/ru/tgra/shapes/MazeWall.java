package com.ru.tgra.shapes;

import java.nio.FloatBuffer;
import java.util.List;

public class MazeWall {
	public Point3D position;
	public float width;
	public float height;
	public float depth;
	
	public float red;
	public float green;
	public float blue;
	public float shine;
	
	public boolean moving;
	
	private Point3D posA;
	private Point3D posB;
	private boolean AtoB;
	public float speed;
	private Vector3D velocity;
	
	public MazeWall(Point3D p, float w, float h, float d) {
		position = p;
		width = w;
		height = h;
		depth = d;
		
		red = 0.5f;
		green = 0.5f;
		blue = 0.5f;
		shine = 10;
		
		moving = false;
	}
	
	//For a moving wall
	public MazeWall(Point3D a, Point3D b, float w, float h, float d, float s) {
		position = a; //We start in point a
		posA = new Point3D(0,0,0);
		posA.set(a.x, a.y, a.z);
		posB = b;
		
		moving = true;
		AtoB = true;
		speed = s;
		
		width = w;
		height = h;
		depth = d;
		
		red = 0.5f;
		green = 0.5f;
		blue = 0.5f;
		shine = 10;
	}
	
	public FloatBuffer getModelMatrix() {
		ModelMatrix matrix = new ModelMatrix();
		matrix.loadIdentityMatrix();
		matrix.addTranslationBaseCoords(position.x, position.y, position.z);
		matrix.addScale(width, height, depth);
		
		return matrix.matrix;
	}
	
	public void move(float dt, Point3D playerPos, Vector3D playerDimensions)
	{
		if(!moving) {
			return;
		}
		calculateVelocity(dt);
		collides(playerPos, playerDimensions);
		position.add(velocity);
	}
	//Used only for the moving object in the maze
	private void calculateVelocity(float dt) {
		
		Vector3D fromAtoB = new Vector3D(posA.x - posB.x, posA.y - posB.y, posA.z - posB.z);
		Vector3D direction = new Vector3D(0,0,0);
		
		if(AtoB) {
			Vector3D AtoPos = new Vector3D(position.x - posA.x, position.y - posA.y, position.z - posA.z);
						
			//Check if object should turn around.
			//This should happen if the object is at or beyond the point it is heading for, as indicated by aToB.
			direction = new Vector3D(fromAtoB.x, fromAtoB.y, fromAtoB.z);
			direction.normalize();
			direction.scale(-1);
			direction.scale(speed*dt);
			
			if((AtoPos.added(direction)).length() >= fromAtoB.length()) {
				//System.out.println("in posB");
				position.set(posB.x, posB.y, posB.z);
				AtoB = false;
				direction.scale(0); //Do note move this frame
			}
		}
		else {
			Vector3D BtoPos = new Vector3D(position.x - posB.x, position.y - posB.y, position.z - posB.z);
			
			direction = new Vector3D(fromAtoB.x, fromAtoB.y, fromAtoB.z);
			direction.normalize();
			//direction.scale(-1);
			direction.scale(speed*dt);
			
			if((BtoPos.added(direction)).length() >= fromAtoB.length()) {
				
				//System.out.println("in posA");
				position.set(posA.x, posA.y, posA.z);
				AtoB = true;
				direction.scale(0); //Do note move this frame
			}
		}
		
		//System.out.println("Direction: " +  direction.x + ", " + direction.y + ", " + direction.z);
		velocity = direction;
	}
	
	private void collides(Point3D playerPos, Vector3D playerDimensions) {
		boolean inXRange = false;
		boolean inYRange = false;
		boolean inZRange = false;
		
		if(playerPos.x + playerDimensions.x/2 >= position.x + velocity.x- width/2 && playerPos.x - playerDimensions.x/2 <= position.x + velocity.x + width/2) {
			inXRange = true;
		}
		
		if(playerPos.y + playerDimensions.y/2 >= position.y + velocity.y- height/2 && playerPos.y - playerDimensions.y/2 <= position.y + velocity.y + height/2) {
			inYRange = true;
		}
		
		if(playerPos.z + playerDimensions.z/2 >= position.z + velocity.z- depth/2 && playerPos.z - playerDimensions.z/2 <= position.z + velocity.z + depth/2) {
			inZRange = true;
		}
		
		if(inXRange && inZRange && inYRange) {
			velocity.set(0, 0, 0);
		}
	}
	
	public void setColor(float r, float g, float b) {
		red = r;
		green = g;
		blue = b;
	}
	
	public void setShine(float s) {
		shine = s;
	}
	
	public void draw() {
		BoxGraphic.drawSolidCube();
	}
}
