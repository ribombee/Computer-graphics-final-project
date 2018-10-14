package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class PointLight {
	Point3D position;
	float diffRed, diffGreen, diffBlue;
	float specRed, specGreen, specBlue;
	float range;
	float counter;
	
	int i; //Index in array of point lights.
	
	int posLoc, diffLoc, specLoc, rangeLoc;
	
	public PointLight(int i, Point3D pos,Vector3D diff, Vector3D spec, float r) {
		position = pos;
		diffRed = diff.x;
		diffGreen = diff.y;
		diffBlue = diff.z;
		specRed = spec.x;
		specGreen = spec.y;
		specBlue = spec.z;
		range = r;
		
		this.i = i;
	}
	
	public void fetchLocs(Shader shader) {
		posLoc = shader.getLoc("u_pointLights[" + i + "].position");
		diffLoc = shader.getLoc("u_pointLights[" + i + "].diffuse");
		specLoc = shader.getLoc("u_pointLights[" + i + "].specular");
		rangeLoc = shader.getLoc("u_pointLights[" + i + "].range");
	}
	
	public void updateShaderValues() {
		
		Gdx.gl.glUniform4f(posLoc, position.x, position.y, position.z, 0);
		Gdx.gl.glUniform4f(diffLoc, diffRed, diffGreen, diffBlue, 1);
		Gdx.gl.glUniform4f(specLoc, specRed, specGreen, specBlue, 1);
		Gdx.gl.glUniform1f(rangeLoc, range);
	}
	
	public void move(float delta) {
		counter += delta;
		position.x += 0.3*i*(float) Math.sin(counter);
		position.z += 0.3*i*(float) Math.cos(counter);
	}
	
}
