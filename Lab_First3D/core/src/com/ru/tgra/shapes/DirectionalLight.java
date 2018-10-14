package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class DirectionalLight {
	Vector3D direction;
	float diffRed, diffGreen, diffBlue;
	float specRed, specGreen, specBlue;
	
	int dirLoc, diffLoc, specLoc;
	
	public DirectionalLight(Vector3D dir, Vector3D diff, Vector3D spec) {
		direction = dir;
		diffRed = diff.x;
		diffGreen = diff.y;
		diffBlue = diff.z;
		specRed = spec.x;
		specGreen = spec.y;
		specBlue = spec.z;
	}

	public void fetchLocs(Shader shader) {
		dirLoc = shader.getLoc("u_directionalLight.position");
		diffLoc = shader.getLoc("u_directionalLight.diffuse");
		specLoc = shader.getLoc("u_directionalLight.specular");
	}
	
	public void updateShaderValues() {
		Gdx.gl.glUniform4f(dirLoc, direction.x, direction.y, direction.z, 1);
		Gdx.gl.glUniform4f(diffLoc, diffRed, diffGreen, diffBlue, 1);
		Gdx.gl.glUniform4f(specLoc, specRed, specGreen, specBlue, 1);
	}

}
