#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D EntitySampler;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    vec4 entity = texture(EntitySampler, texCoord);
	color.rgb *= 0.2;
    entity.rgb *= 2.0;
    fragColor = mix(color, entity, entity.a);
}
