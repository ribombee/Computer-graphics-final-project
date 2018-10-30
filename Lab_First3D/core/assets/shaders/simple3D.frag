
#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_diffuse;
varying vec4 v_specular;
varying vec4 v_light;
varying vec2 v_uv;

uniform int u_renderingSkybox;

uniform int u_usesDiffuseTexture;
uniform sampler2D u_diffuseTexture;

//uniform int u_usesSpecularTexture;
//uniform sampler2D u_specularTexture;
void main()
{
	if(u_renderingSkybox == 1)
	{
		gl_FragColor = texture2D(u_diffuseTexture, v_uv);
	}
	else if(u_usesDiffuseTexture == 1)
	{
		gl_FragColor = v_light * texture2D(u_diffuseTexture, v_uv);
	}
	else
	{
		gl_FragColor = v_light;
	}

	//if(u_usesSpecularTexture == 1)
	//{
	//	gl_FragColor = gl_FragColor*texture2D(u_specularTexture, v_uv)
	//}
	
}
