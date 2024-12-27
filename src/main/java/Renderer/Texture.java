package Renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String FilePath;
    private int TextureID;
    private int Width, Height;

/*
    public Texture(String FilePath){

    }
*/

    public void init(String filePath){
        this.FilePath = FilePath;

        //Generate Texture
        TextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, TextureID);

        //Set Texture Parameters
        //Repeat Image in both Directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //when stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        //when shrinking pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer Width = BufferUtils.createIntBuffer(1);
        IntBuffer Height = BufferUtils.createIntBuffer(1);
        IntBuffer Channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);

        ByteBuffer Image = stbi_load(filePath, Width, Height, Channels, 0);

        if (Image != null) {
            this.Width = Width.get(0);
            this.Height = Height.get(0);

            if (Channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Width.get(0), Height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, Image);
            } else if (Channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Width.get(0), Height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, Image);
            } else {
                assert false : "Error: (Texture) Unknown number of Channels " + Channels.get(0) + " ";
            }
        } else {
            assert false : "Error: (Texture) Could not Load Image " + FilePath + " ";
        }

        stbi_image_free(Image);
    }
    public void BindTexture() {
        glBindTexture(GL_TEXTURE_2D, TextureID);
    }

    public void UnbindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int GetWidth() {
        return this.Width;
    }

    public int GetHeight() {
        return this.Height;
    }
    public int getTextureID(){
        return TextureID;
    }
}
