package Renderer;

import Components.SpriteRenderer;
import Laevis.Window;
import LaevisUtilities.AssetPool;
import kotlin.jvm.internal.PropertyReference0Impl;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL33.*;

public class RenderBatch {
    /* Vertex
     * Position          Color
     * float, float,     float, float, float, float
    */
    private final int POSITION_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] Sprites;
    private int NumberOfSprites;
    private boolean HasRoom;
    private float[] Vertices;

    private int VAO_ID, VBO_ID;
    private int MaximumBatchSize;
    private Shader Shader;

    public RenderBatch(int MaximumBatchSize) {
        Shader = AssetPool.GetShader("Assets/Shaders/default.glsl");
        this.Sprites = new SpriteRenderer[MaximumBatchSize];
        this.MaximumBatchSize = MaximumBatchSize;

        //4 Vertices Quads
        Vertices = new float[MaximumBatchSize * 4 * VERTEX_SIZE];

        this.NumberOfSprites = 0;
        this.HasRoom = true;
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
    }

    public void AddSprite(SpriteRenderer spriteRenderer) {
        // Get Index and Render Object
        int Index = this.NumberOfSprites;
        this.Sprites[Index] = spriteRenderer;
        this.NumberOfSprites++;

        // Add to Local Vertices Array
        LoadVertexProperties(Index);

        if (NumberOfSprites >= this.MaximumBatchSize) {
            this.HasRoom = false;
        }
    }

    public void RenderTheRenderBatch() {
        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Vertices);

        // Use Shader
        Shader.UseShader();
        Shader.UploadMatrix4f("UniformProjectionMatrix", Window.GetScene().Camera().GetProjectionMatrix());
        Shader.UploadMatrix4f("UniformViewMatrix", Window.GetScene().Camera().GetViewMatrix());

        glBindVertexArray(VAO_ID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.NumberOfSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        Shader.DetachShader();
    }

    private void LoadVertexProperties(int Index) {
        SpriteRenderer Sprite = this.Sprites[Index];

        // Find Offset in Array
        int Offset = Index * 4 * VERTEX_SIZE;

        Vector4f Color = Sprite.GetColor();

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
        return HasRoom;
    }
}
