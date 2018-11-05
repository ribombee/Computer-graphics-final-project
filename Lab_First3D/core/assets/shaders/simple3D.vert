
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

attribute vec2 a_uvpos;

uniform int u_renderingSkybox;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;

uniform vec4 u_globalAmbient;
uniform vec4 u_lightPosition;
uniform vec4 u_lightDiffuse;
uniform vec4 u_lightSpecular;
uniform float u_lightRange;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform float u_materialShine;

uniform float u_fogStart;
uniform float u_fogEnd;

//Colors turn out really cool when we don't restrain them, so we keep it in
uniform int u_fogRestrainLerp;

varying vec4 v_diffuse;
varying vec4 v_specular;

varying vec4 v_light;

varying vec2 v_uv;

varying float v_fogRatio;

struct pointLight  {
	vec4 position;
	vec4 diffuse;
	vec4 specular;
	float range;
};

struct directionalLight {
	vec4 direction;
	vec4 diffuse;
	vec4 specular;
};

uniform pointLight u_pointLights[4];
uniform directionalLight u_directionalLight;

vec4 directionalLightCalculations(directionalLight light, vec4 pos, vec4 norm, vec4 v) {

	vec4 s = light.direction;
	vec4 h = s + v;
	
	vec4 diffuseDiff = vec4(0.0);
	
	float lambert = max(0, (dot(norm, s) / (length(norm)*length(s))));
	diffuseDiff = lambert * light.diffuse * u_materialDiffuse;
		
	float phong = max(0, (dot(norm, h) / (length(norm)*length(h))));
	
	vec4 specularDiff = vec4(0.0);
	
	specularDiff = pow(phong, u_materialShine) * light.specular * u_materialSpecular;
	
	specularDiff[3] = 0;
	diffuseDiff[3] = 0;

	return (diffuseDiff + specularDiff);
}

vec4 pointLightCalculations(pointLight light, vec4 pos, vec4 norm, vec4 v) {

	vec4 s = light.position - pos;		
	vec4 h = s + v;
	
	float distFactor = pow(length(s)/light.range,2);
		
	vec4 diffuseDiff = vec4(0.0);
	
	float lambert = max(0, (dot(norm, s) / (length(norm)*length(s))));
	diffuseDiff = lambert * light.diffuse * u_materialDiffuse;
	diffuseDiff = diffuseDiff / distFactor;
	
	pos = u_viewMatrix * pos;
	
	float phong = max(0, (dot(norm, h) / (length(norm)*length(h))));
	
	vec4 specularDiff = vec4(0.0);
	
	specularDiff = pow(phong, u_materialShine) * light.specular * u_materialSpecular;
	specularDiff = specularDiff / distFactor;
	
	specularDiff[3] = 0;
	diffuseDiff[3] = 0;

	return (diffuseDiff + specularDiff);
}


void main()
{

	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);

	vec4 normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);

	position = u_modelMatrix * position;
	
	normal = u_modelMatrix * normal;
	
	vec4 v = u_eyePosition - position;
	
	v_specular = vec4(0.0);
	v_diffuse = vec4(0.0);
	v_light = vec4(0.0);
	
	v_light += directionalLightCalculations(u_directionalLight, position, normal, v);
	
	for(int i = 0; i < 4; i++) {
		v_light += pointLightCalculations(u_pointLights[i], position, normal, v);
	}
	
	v_light += u_globalAmbient;
	
	v_light[3] = 1;
	
	position = u_viewMatrix * position;
	
	float posLength = length(position);
	if(posLength < u_fogStart && u_fogRestrainLerp == 1) {
		v_fogRatio = 0.0;
	}
	else if(posLength > u_fogEnd && u_fogRestrainLerp == 1) {
		v_fogRatio = 1.0;
	}
	else {
		v_fogRatio = (posLength - u_fogStart)/(u_fogEnd - u_fogStart);
	}
	
	v_uv = a_uvpos;
	gl_Position = u_projectionMatrix * position;
}