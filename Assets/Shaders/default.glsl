#type vertex
#version 330 core

layout (location = 0) in vec3 AttributePosition;
layout (location = 1) in vec4 AttributeColor;
layout (location = 2) in vec2 AttributeTextureCoordinates;

uniform mat4 UniformProjectionMatrix;
uniform mat4 UniformViewMatrix;

out vec4 FragmentColor;
out vec2 FragmentTextureCoordinates;

void main() {
    FragmentColor = AttributeColor;
    FragmentTextureCoordinates = AttributeTextureCoordinates;
    gl_Position = UniformProjectionMatrix * UniformViewMatrix * vec4(AttributePosition, 1.0);
}

#type fragment
#version 330 core

uniform float UniformTime;
uniform sampler2D TextureSampler;

in vec4 FragmentColor;
in vec2 FragmentTextureCoordinates;

out vec4 Color;

void main()
{
    Color = texture(TextureSampler, FragmentTextureCoordinates);
}