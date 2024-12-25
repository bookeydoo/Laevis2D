package Renderer;

import Components.SpriteRenderer;
import Laevis.Window;
import LaevisUtilities.AssetPool;
import kotlin.jvm.internal.PropertyReference0Impl;
import kotlin.time.ComparableTimeMark;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class RenderBatch implements Comparable<RenderBatch> {
    /* Vertex
     * Position          Color                           Texture Coordinates         Texture ID
     * float, float,     float, float, float, float,     float, float,               float
    */
    private final int POSITION_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXTURE_COORDINATES_SIZE = 2;
    private final int TEXTURE_ID_SIZE = 1;

    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
    private final int TEXTURE_COORDINATES_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_ID_OFFSET = TEXTURE_COORDINATES_OFFSET + TEXTURE_COORDINATES_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + TEXTURE_COORDINATES_SIZE + TEXTURE_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] Sprites;
    private int NumberOfSprites;
    private boolean HasRoom;
    private float[] Vertices;
    private int[] TextureSlots = { 0, 1, 2, 3, 4, 5, 6, 7 };

    private List<Texture> Textures;
    private int VAO_ID, VBO_ID;
    private int MaximumBatchSize;
    private Shader Shader;
    private int zIndex;

    public RenderBatch(int MaximumBatchSize, int zIndex) {
        this.zIndex=zIndex;
        Shader = AssetPool.GetShader("Assets/Shaders/default.glsl");
        this.Sprites = new SpriteRenderer[MaximumBatchSize];
        this.MaximumBatchSize = MaximumBatchSize;

        //4 Vertices Quads
        Vertices = new float[MaximumBatchSize * 4 * VERTEX_SIZE];

        this.NumberOfSprites = 0;
        this.HasRoom = true;
        this.Textures = new ArrayList<>();
    }

    public void StartBatchRenderer() {
        VAO_ID = glGenVertexArrays();
        glBindVertexArray(VAO_ID);

        // Allocate Space
        VBO_ID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
        glBufferData(GL_ARRAY_BUFFER, (long)Vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Create & Upload Buffer
        int EBO_ID = glGenBuffers();
        int[] Indices = GenerateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO_ID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Indices, GL_STATIC_DRAW);

        //Enable the Buffer Attribute Pointers
        glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POSITION_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEXTURE_COORDINATES_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_COORDINATES_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void AddSprite(SpriteRenderer spriteRenderer) {
        // Get Index and Render Object
        int Index = this.NumberOfSprites;
        this.Sprites[Index] = spriteRenderer;
        this.NumberOfSprites++;

        if (spriteRenderer.GetTexture() != null) {
            if (!Textures.contains(spriteRenderer.GetTexture())) {
                Textures.add(spriteRenderer.GetTexture());
            }
        }

        // Add to Local Vertices Array
        LoadVertexProperties(Index);

        if (NumberOfSprites >= this.MaximumBatchSize) {
            this.HasRoom = false;
        }
    }

    public void RenderTheRenderBatch() {
        boolean rebufferdata=false;
        for(int i=0;i<NumberOfSprites;i++){
            SpriteRenderer spr=Sprites[i];
            if(spr.isDirty()){
                LoadVertexProperties(i);
                spr.setClean();
                rebufferdata=true;
            }
        }
        if(rebufferdata) {
            //rebuffer the data if we have a dirty flag
            glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, Vertices);
        }
        // Use Shader
        Shader.UseShader();
        Shader.UploadMatrix4f("UniformProjectionMatrix", Window.GetScene().Camera().GetProjectionMatrix());
        Shader.UploadMatrix4f("UniformViewMatrix", Window.GetScene().Camera().GetViewMatrix());
        for (int i = 0; i < Textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            Textures.get(i).BindTexture();
        }
        Shader.UploadIntArray("UniformTextures", TextureSlots);

        glBindVertexArray(VAO_ID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.NumberOfSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i = 0; i < Textures.size(); i++) {
            Textures.get(i).UnbindTexture();
        }

        Shader.DetachShader();
    }

    private void LoadVertexProperties(int Index) {
        SpriteRenderer Sprite = this.Sprites[Index];

        // Find Offset in Array
        int Offset = Index * 4 * VERTEX_SIZE;

        Vector4f Color = Sprite.GetColor();
        Vector2f[] TextureCoordinates = Sprite.GetTextureCoordinates();

        int TextureID = 0;
        if (Sprite.GetTexture() != null) {
            for (int i = 0; i < Textures.size(); i++) {
                if (Textures.get(i) == Sprite.GetTexture()) {
                    TextureID = i + 1;
                    break;
                }
            }
        }

        // Add Vertices with their Properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;

        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            // Load Position
            Vertices[Offset] = Sprite.GameObject.Transform.Position.x + (xAdd * Sprite.GameObject.Transform.Scale.x);
            Vertices[Offset + 1] = Sprite.GameObject.Transform.Position.y + (yAdd * Sprite.GameObject.Transform.Scale.y);

            //Load Color
            Vertices[Offset + 2] = Color.x;
            Vertices[Offset + 3] = Color.y;
            Vertices[Offset + 4] = Color.z;
            Vertices[Offset + 5] = Color.w;

            //Load Texture Coordinates
            Vertices[Offset + 6] = TextureCoordinates[i].x;
            Vertices[Offset + 7] = TextureCoordinates[i].y;

            //Load Texture ID
            Vertices[Offset + 8] = TextureID;

            Offset += VERTEX_SIZE;
        }
    }

    private int[] GenerateIndices() {
        // 6 Indices per Quad
        int[] Elements = new int[6 * MaximumBatchSize];
        for (int i = 0; i < MaximumBatchSize; i++) {
            LoadElementIndices(Elements, i);
        }

        return Elements;
    }

    private void LoadElementIndices(int[] Elements, int Index) {
        int OffsetArrayIndex = 6 * Index;
        int Offset = 4 * Index;

        //Triangle 1
        Elements[OffsetArrayIndex] = Offset + 3;
        Elements[OffsetArrayIndex + 1] = Offset + 2;
        Elements[OffsetArrayIndex + 2] = Offset;

        //Triangle 2
        Elements[OffsetArrayIndex + 3] = Offset;
        Elements[OffsetArrayIndex + 4] = Offset + 2;
        Elements[OffsetArrayIndex + 5] = Offset + 1;
    }

    public boolean GetHasRoom() {
        return this.HasRoom;
    }

    public boolean GetHasTextureRoom() {
        return this.Textures.size() < 8;
    }

    public boolean GetHasTexture(Texture texture) {
        return this.Textures.contains(texture);
    }
    public int getzIndex(){
        return this.zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex,o.zIndex);
    }
}
