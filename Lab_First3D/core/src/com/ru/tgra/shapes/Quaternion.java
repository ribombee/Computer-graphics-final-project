package com.ru.tgra.shapes;

public class Quaternion {
	float x;
	float y;
	float z;
	float w;
	
	public void setFromEulerAxis(float angle, Vector3D axis) {
		x = (float) (axis.x * Math.sin(angle / 2.0f));
		y = (float) (axis.y * Math.sin(angle / 2.0f));
		z = (float) (axis.z * Math.sin(angle / 2.0f));
		w = (float) Math.cos(angle / 2.0f);
	}
	public void mult(Quaternion other) {
		this.w = other.w * this.w - other.x * this.x - other.y * this.y - other.z * this.z;
		this.x = other.w * this.x - other.x * this.w - other.y * this.z - other.z * this.y;
		this.y = other.w * this.y - other.x * this.z - other.y * this.w - other.z * this.x;
		this.z = other.w * this.z - other.x * this.y - other.y * this.x - other.z * this.w;
	}
	public void setFromEulerAngles(float pitch, float yaw, float roll) {
		// Abbreviations for the various angular functions
	    float cy = (float)Math.cos(yaw * 0.5);
	    float sy = (float)Math.sin(yaw * 0.5);
	    float cr = (float)Math.cos(roll * 0.5);
	    float sr = (float)Math.sin(roll * 0.5);
	    float cp = (float)Math.cos(pitch * 0.5);
	    float sp = (float)Math.sin(pitch * 0.5);

	    w = cy * cr * cp + sy * sr * sp;
	    x = cy * sr * cp - sy * cr * sp;
	    y = cy * cr * sp + sy * sr * cp;
	    z = sy * cr * cp - cy * sr * sp;
	}
	
	//This function was taken from the quaternion class in libgdx 
	public void setFromAxes (boolean normalizeAxes, float xx, float xy, float xz, float yx, float yy, float yz, float zx,
			float zy, float zz) {
			if (normalizeAxes) {
				final float lx = 1f / Vector3D.length(xx, xy, xz);
				final float ly = 1f / Vector3D.length(yx, yy, yz);
				final float lz = 1f / Vector3D.length(zx, zy, zz);
				xx *= lx;
				xy *= lx;
				xz *= lx;
				yx *= ly;
				yy *= ly;
				yz *= ly;
				zx *= lz;
				zy *= lz;
				zz *= lz;
			}
			// the trace is the sum of the diagonal elements; see
			// http://mathworld.wolfram.com/MatrixTrace.html
			final float t = xx + yy + zz;

			// we protect the division by s by ensuring that s>=1
			if (t >= 0) { // |w| >= .5
				float s = (float)Math.sqrt(t + 1); // |s|>=1 ...
				w = 0.5f * s;
				s = 0.5f / s; // so this division isn't bad
				x = (zy - yz) * s;
				y = (xz - zx) * s;
				z = (yx - xy) * s;
			} else if ((xx > yy) && (xx > zz)) {
				float s = (float)Math.sqrt(1.0 + xx - yy - zz); // |s|>=1
				x = s * 0.5f; // |x| >= .5
				s = 0.5f / s;
				y = (yx + xy) * s;
				z = (xz + zx) * s;
				w = (zy - yz) * s;
			} else if (yy > zz) {
				float s = (float)Math.sqrt(1.0 + yy - xx - zz); // |s|>=1
				y = s * 0.5f; // |y| >= .5
				s = 0.5f / s;
				x = (yx + xy) * s;
				z = (zy + yz) * s;
				w = (xz - zx) * s;
			} else {
				float s = (float)Math.sqrt(1.0 + zz - xx - yy); // |s|>=1
				z = s * 0.5f; // |z| >= .5
				s = 0.5f / s;
				x = (xz + zx) * s;
				y = (zy + yz) * s;
				w = (yx - xy) * s;
			}

		}
}
