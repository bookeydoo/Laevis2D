#type vertex
#version 330 core

layout (location = 0) in vec3 AttributePosition;
layout (location = 1) in vec4 AttributeColor;
layout (location = 2) in vec2 AttributeTextureCoordinates;
layout (location = 3) in float AttributeTextureID;


uniform mat4 UniformProjectionMatrix;
uniform mat4 UniformViewMatrix;

out vec4 FragmentColor;
out vec2 FragmentTextureCoordinates;
out float FragmentTextureID;

void main() {
    FragmentColor = AttributeColor;
    FragmentTextureCoordinates = AttributeTextureCoordinates;
    FragmentTextureID = AttributeTextureID;

    gl_Position = UniformProjectionMatrix * UniformViewMatrix * vec4(AttributePosition, 1.0);
}

#type fragment
#version 330 core

in vec4 FragmentColor;
in vec2 FragmentTextureCoordinates;
in float FragmentTextureID;

uniform sampler2D UniformTextures[8];

out vec4 Color;

void main()
{
    if (FragmentTextureID > 0) {
        int ID = int(FragmentTextureID);
        Color = FragmentColor * texture(UniformTextures[ID], FragmentTextureCoordinates);
        //Color = vec4(FragmentTextureCoordinates, 0, 1);
    } else {
        Color = FragmentColor;
    }
}