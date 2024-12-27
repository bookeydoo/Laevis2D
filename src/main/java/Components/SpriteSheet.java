package Components;

import Renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
	private Texture Texture;
	private List<Sprite> Sprites;

	public SpriteSheet(Texture Texture, int SpriteWidth, int SpriteHeight, int NumberOfSprites, int Spacing) {
		this.Sprites = new ArrayList<>();

		this.Texture = Texture;
		int CurrentX = 0;
		int CurrentY = Texture.GetHeight() - SpriteHeight;

		// This Gets a Single Sprite from the Sprite Sheet
		for (int i = 0; i < NumberOfSprites; i++) {
			float TopY = (CurrentY + SpriteHeight) / (float)Texture.GetHeight();
			float RightX = (CurrentX + SpriteWidth) / (float)Texture.GetWidth();
			float LeftX = CurrentX / (float)Texture.GetWidth();
			float BottomY = CurrentY / (float)Texture.GetHeight();

			Vector2f[] TextureCoordinates = {
				new Vector2f(RightX, TopY),
				new Vector2f(RightX, BottomY),
				new Vector2f(LeftX, BottomY),
				new Vector2f(LeftX, TopY)
			};

			Sprite Sprite = new Sprite();
			Sprite.setTexture(this.Texture);
			Sprite.setTextCoords(TextureCoordinates);
			Sprite.setWidth(SpriteWidth);
			Sprite.setHeight(SpriteHeight);
			this.Sprites.add(Sprite);

			CurrentX += SpriteWidth + Spacing;
			if (CurrentX >= Texture.GetWidth()) {
				CurrentX = 0;
				CurrentY -= SpriteHeight + Spacing;
			}
		}
	}

	public Sprite GetSprite(int Index) {
		return this.Sprites.get(Index);
	}

	public int size(){
		return Sprites.size();
	}
}
