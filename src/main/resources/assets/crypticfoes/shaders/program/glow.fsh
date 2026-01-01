#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

out vec4 fragColor;

void main(){
	vec4 col = texture(DiffuseSampler, texCoord);
	col.rgb *= 1.2;
	fragColor = col;
}
