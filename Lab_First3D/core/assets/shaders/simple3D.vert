
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

void main()
{
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	vec4 normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	normal = u_modelMatrix * normal;
	
	vec4 s = u_lightPosition - position;
	vec4 v = u_eyePosition - position;
	
	vec4 h = s + v;
	
	float distFactor = pow(length(s)/u_lightRange,2);
	
	float lambert = max(0, (dot(normal, s) / (length(normal)*length(s))));
	v_diffuse = lambert * u_lightDiffuse * u_materialDiffuse;
	v_diffuse = v_diffuse / distFactor;
	
	position = u_viewMatrix * position;
	//normal = u_viewMatrix * normal;
	
	float phong = max(0, (dot(normal, h) / (length(normal)*length(h))));
	v_specular = pow(phong, u_materialShine) * u_lightSpecular * u_materialSpecular;
	v_specular = v_specular / distFactor;


	gl_Position = u_projectionMatrix * position;
}