
#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_diffuse;
varying vec4 v_specular;
varying vec4 v_light;
varying vec2 v_uv;
varying float v_fogRatio;

uniform vec3 u_fogColor;

uniform int u_renderingSkybox;

uniform int u_usesDiffuseTexture;
uniform sampler2D u_diffuseTexture;
uniform float u_alpha;
void main()
{
	if(u_renderingSkybox == 1)
	{
		vec4 fogColor = vec4(u_fogColor.x, u_fogColor.y, u_fogColor.z, 1);
		vec4 finalColor = mix(texture2D(u_diffuseTexture, v_uv), fogColor, v_fogRatio/2);
		gl_FragColor = finalColor;
		gl_FragColor.a = u_alpha;
	}
	else if(u_usesDiffuseTexture == 1)
	{
		vec4 fogColor = vec4(u_fogColor.x, u_fogColor.y, u_fogColor.z, 1);
		vec4 textureColor = v_light * texture2D(u_diffuseTexture, v_uv);
		vec4 finalColor = mix(textureColor, fogColor, v_fogRatio);
		
		gl_FragColor = finalColor;
		gl_FragColor.a = u_alpha;
	}
	else
	{
		gl_FragColor = v_light;
	}
}
