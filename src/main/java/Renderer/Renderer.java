package Renderer;

import Components.SpriteRenderer;
import Laevis.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 100000;
    private List<RenderBatch> Batches;

    public Renderer() {
        this.Batches = new ArrayList<>();
    }

    public void AddRenderer(GameObject gameObject) {
        SpriteRenderer spriteRenderer = gameObject.GetComponent(SpriteRenderer.class);
        if (spriteRenderer != null) {
            AddRenderer(spriteRenderer);
        }
    }

    public void AddRenderer(SpriteRenderer Sprite) {
        boolean Added = false;

        for (RenderBatch Batch : Batches) {
            if (Batch.GetHasRoom() && (Batch.getzIndex() == Sprite.GameObject.getzIndex() )){
                Texture texture = Sprite.GetTexture();
                if (texture == null || (Batch.GetHasTexture(texture) || Batch.GetHasTextureRoom())) {
                    Batch.AddSprite(Sprite);
                    Added = true;
                    break;
                }
            }
        }

        if (!Added) {
            RenderBatch NewBatch = new RenderBatch(MAX_BATCH_SIZE,Sprite.GameObject.getzIndex());
            NewBatch.StartBatchRenderer();
            Batches.add(NewBatch);
            NewBatch.AddSprite(Sprite);
            Collections.sort(Batches);
        }
    }

    public void Render() {
        for (RenderBatch Batch : Batches) {
            Batch.RenderTheRenderBatch();
        }
    }
}
