
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;

uniform vec4 u_lightPosition;
uniform vec4 u_lightDiffuse;
uniform vec4 u_lightSpecular;
uniform float u_lightRange;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform float u_materialShine;

varying vec4 v_diffuse;
varying vec4 v_specular;

varying vec4 v_light;

struct pointLight  {
	vec4 position;
	vec4 diffuse;
	vec4 specular;
	float range;
};

uniform pointLight u_pointLight;

uniform pointLight u_pointLights[4];


vec4 pointLightCalculations(pointLight light, vec4 pos, vec4 norm, vec4 v) {

	
	
	vec4 s = light.position - pos;		
	vec4 h = s + v;
	
	float distFactor = pow(length(s)/light.range,2);
		
	vec4 diffuseDiff = vec4(0.0);
	
	float lambert = max(0, (dot(norm, s) / (length(norm)*length(s))));
	diffuseDiff = lambert * light.diffuse * u_materialDiffuse;
	diffuseDiff = diffuseDiff / distFactor;
	
	//pos = u_viewMatrix * pos;
	
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
	
	for(int i = 0; i < 4; i++) {
		v_light += pointLightCalculations(u_pointLights[i], position, normal, v);
	}
	
	v_light[3] = 1;
	
	
	position = u_viewMatrix * position;
	//v_light = v_specular + v_diffuse;
	
	gl_Position = u_projectionMatrix * position;
}