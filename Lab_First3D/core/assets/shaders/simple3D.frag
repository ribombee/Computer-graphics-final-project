
#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_diffuse;
varying vec4 v_specular;
varying vec4 v_light;

void main()
{
	gl_FragColor = v_light;
}