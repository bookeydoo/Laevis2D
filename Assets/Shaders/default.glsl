#type vertex
#version 330 core

layout (location = 0) in vec3 AttributePosition;
layout (location = 1) in vec4 AttributeColor;

uniform mat4 UniformProjectionMatrix;
uniform mat4 UniformViewMatrix;

out vec4 FragmentColor;

void main() {
    FragmentColor = AttributeColor;
    gl_Position = UniformProjectionMatrix * UniformViewMatrix * vec4(AttributePosition, 1.0);
}

#type fragment
#version 330 core

uniform float UniformTime;

in vec4 FragmentColor;

out vec4 Color;

void main()
{
    float Noise = fract(sin(dot(FragmentColor.xy, vec2(12.9898, 78.233))) * 43758.5453);
    Color = FragmentColor * Noise;
}