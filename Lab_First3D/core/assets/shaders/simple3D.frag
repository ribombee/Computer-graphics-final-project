
#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_diffuse;
varying vec4 v_specular;

void main()
{
	gl_FragColor = v_diffuse + v_specular;
}