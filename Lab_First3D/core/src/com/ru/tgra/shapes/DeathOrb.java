package com.ru.tgra.shapes;

public class DeathOrb extends MazeWall{
	
	float radius;
	//TYPE
	//1: hovering up and down
	//2: goes in a circle
	//3: shot between two cannons or something
	int type;
	float speed;
	float totalTime = 0;
	
	Point3D controlPoint1;
	Point3D controlPoint2;
	
	float startTime = 0;
	
	public DeathOrb(Point3D p, float r, int t, float s)
	{		
		super(p, r, r, r);
		speed = s;
		type = t;
		radius = r;
		
		controlPoint1 = new Point3D(p.x, p.y, p.z);
		controlPoint2 = new Point3D(p.x, p.y + 10, p.z);
	}
	
	private void moveHover(float dt) {
		totalTime += dt;
		
		float currentTime = totalTime%(speed*2);
		
		if(currentTime > speed) {
			
			//On the way down.
			float percentage = (2*speed - currentTime) / (speed - 0);
			Point3D newPos = new Point3D(controlPoint1.x , controlPoint2.y + percentage * (controlPoint1.y - controlPoint2.y), controlPoint1.z);
			position = newPos;
		}
		else {
			//On the way up.
			 float percentage = (speed - currentTime) / (speed - 0);
			 Point3D newPos = new Point3D(controlPoint1.x, controlPoint1.y + percentage * (controlPoint2.y - controlPoint1.y), controlPoint1.z);
			 position = newPos;
		}
		
	}
	
	public void move(float dt) {
		if(type == 1) {
			moveHover(dt);
		}
	}
	
	public void draw() {
		SphereGraphic.drawSolidSphere();
	}

}
