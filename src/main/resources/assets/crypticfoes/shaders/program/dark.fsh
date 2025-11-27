#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate;

in vec2 texCoord;

out vec4 fragColor;

void main(){
	vec4 bg = texture(DiffuseSampler, texCoord);
	bg.rgb *= 0.1;
    fragColor = bg * ColorModulate;
}
