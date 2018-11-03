package com.ru.tgra.shapes;

public class Vector3D {

	public float x;
	public float y;
	public float z;

	public Vector3D(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void scale(float S)
	{
		x *= S;
		y *= S;
		z *= S;
	}
	
	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void add(Vector3D v2)
	{
		x += v2.x;
		y += v2.y;
		z += v2.z;
	}
	
	public Vector3D added(Vector3D v2)
	{
		Vector3D added = new Vector3D(x,y,z);
		added.x += v2.x;
		added.y += v2.y;
		added.z += v2.z;
		
		return added;
	}

	public float dot(Vector3D v2)
	{
		return x*v2.x + y*v2.y + z*v2.z;
	}

	public float dotSelf()
	{
		return x*x + y*y + z*z;
	}

	public float length()
	{
		return (float)Math.sqrt(dotSelf());
	}

	public void normalize()
	{
		float len = length();
		if(len == 0)
			return;
		x = x / len;
		y = y / len;
		z = z / len;
	}

	public Vector3D cross(Vector3D v2)
	{
		return new Vector3D(y*v2.z - z*v2.y, z*v2.x - x*v2.z, x*v2.y - y*v2.x);
	}

	public static Vector3D difference(Point3D P2, Point3D P1)
	{
		return new Vector3D(P2.x-P1.x, P2.y-P1.y, P2.z-P1.z);
	}
	
	public static float length(float x, float y, float z) {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
}
