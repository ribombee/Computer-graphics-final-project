package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.BufferUtils;

public class Camera {
	private Point3D eye;
	private Vector3D u;
	private Vector3D v;
	private Vector3D n;
			
	public Camera() {
		
		u = new Vector3D(1, 0, 0);
		v = new Vector3D(0, 1, 0);
		n = new Vector3D(0, 0, 1);
		
		eye = new Point3D(1.0f, 1.0f, 1.0f);
				
		LookAt(new Point3D(1.0f, 1.0f, 1.0f), new Vector3D(0.0f, 1.0f, 0.0f));
	}
	
	public void setShaderMatrix() {
		Vector3D minusEye = new Vector3D(-eye.x, -eye.y, -eye.z);
		
		float[] pm = new float[16];

		pm[0] = u.x; pm[4] = u.y; pm[8] = u.z; pm[12] = minusEye.dot(u);
		pm[1] = v.x; pm[5] = v.y; pm[9] = v.z; pm[13] = minusEye.dot(v);
		pm[2] = n.x; pm[6] = n.y; pm[10] = n.z; pm[14] = minusEye.dot(n);
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;
		
		Maze3D.matrixBuffer = BufferUtils.newFloatBuffer(16);
		Maze3D.matrixBuffer.put(pm);
		Maze3D.matrixBuffer.rewind();
		Gdx.gl.glUniformMatrix4fv(Maze3D.viewMatrixLoc, 1, false, Maze3D.matrixBuffer);
		
	}
	
	public void LookAt( Point3D center, Vector3D up) {
		
		n = Vector3D.difference(eye, center);
		u = up.cross(n);
		n.normalize();
		u.normalize();
		v = n.cross(u);
	}
	
	public void yaw(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = -(float)Math.sin(radians);
		Vector3D t = new Vector3D(u.x, u.y, u.z);
		
		u.set(t.x * c - n.x * s, t.y * c - n.y *s, t.z * c - n.z * s);
		n.set(t.x * s + n.x * c, t.y * s + n.y *c, t.z * s + n.z * c);
	}
	
	public void pitch(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = -(float)Math.sin(radians);
		Vector3D t = new Vector3D(n.x, n.y, n.z);
		
		n.set(t.x * c - v.x * s, t.y * c - v.y *s, t.z * c - v.z * s);
		v.set(t.x * s + v.x * c, t.y * s + v.y *c, t.z * s + v.z * c);
	}
	
	public void roll(float angle) {
		float radians = angle * (float)Math.PI / 180.0f;
		float c = (float)Math.cos(radians);
		float s = -(float)Math.sin(radians);
		Vector3D t = new Vector3D(u.x, u.y, u.z);
		
		u.set(t.x * c - v.x * s, t.y * c - v.y *s, t.z * c - v.z * s);
		v.set(t.x * s + v.x * c, t.y * s + v.y *c, t.z * s + v.z * c);
	}
	
	public void translate(Vector3D direction ) {
		
		float delX = direction.x;
		float delY = direction.y;
		float delZ = direction.z;
		
		eye.x += delX*u.x + delY*v.x + delZ*n.x;
		 //eye.y += delX*u.y + delY*v.y + delZ*n.y;
		eye.z += delX*u.z + delY*v.z + delZ*n.z;
	}	
	
}
