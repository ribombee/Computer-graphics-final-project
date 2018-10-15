package com.ru.tgra.shapes;

public class MovingLight {
	Point3D basePosition;
	Point3D position;
	Vector3D velocity;
	Vector3D maxDirection;
	
	public MovingLight(Point3D origin, Vector3D maxDirection)
	{
		basePosition = origin;
		position = new Point3D(origin.x, origin.y, origin.z);
		this.maxDirection = maxDirection;
		velocity = new Vector3D(maxDirection.x/180.0f, maxDirection.y/180.0f, maxDirection.z/180.0f);
	}
	public void update()
	{
		position.add(velocity);
	}
}
