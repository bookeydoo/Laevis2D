package Components;

import Renderer.Texture;
import org.joml.Vector2f;

public class Sprite {
	private Texture Texture;
	private Vector2f[] TextureCoordinates;

	public Sprite(Texture Texture) {
		this.Texture = Texture;
		Vector2f[] TextureCoordinatesArray = {
			new Vector2f(1, 1),
			new Vector2f(1, 0),
			new Vector2f(0, 0),
			new Vector2f(0, 1)
		};
		this.TextureCoordinates = TextureCoordinatesArray;
	}

	public Sprite(Texture Texture, Vector2f[] TextureCoordinates) {
		this.Texture = Texture;
		this.TextureCoordinates = TextureCoordinates;
	}

	public Texture GetTexture() {
		return this.Texture;
	}

	public Vector2f[] GetTextureCoordinates() {
		return this.TextureCoordinates;
	}
}
