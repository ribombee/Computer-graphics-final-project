
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec2 a_uvpos;

uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;
u

varying vec2 v_uv;

void main()
{
	v_uv = a_uvpos;
	gl_position = u_projectionMatrix * u_viewMatrix * a_position;
}