package Components;

import Renderer.Texture;
import org.joml.Vector2f;
import org.w3c.dom.Text;

public class Sprite {
	private Texture Texture=null;
	private float Width,Height;

	public void setWidth(float width) {
		Width = width;
	}

	public void setHeight(float height) {
		Height = height;
	}

	public float getWidth() {
		return Width;
	}

	public float getHeight() {
		return Height;
	}
	public int getTexId(){
		return Texture==null ? -1 : Texture.getTextureID();
	}

	private Vector2f[] TextureCoordinates={
			new Vector2f(1,1),
			new Vector2f(1,0),
			new Vector2f(0,0),
			new Vector2f(0,1)
	};


/*
	public Sprite(Texture Texture) {
		this.Texture = Texture;
		Vector2f[] TextureCoordinatesArray = {
			new Vector2f(1, 1),
			new Vector2f(1, 0),
			new Vector2f(0, 0),
			new Vector2f(0, 1)
		};
		this.TextureCoordinates = TextureCoordinatesArray;
	}*/
//
//	public Sprite(Texture Texture, Vector2f[] TextureCoordinates) {
//		this.Texture = Texture;
//		this.TextureCoordinates = TextureCoordinates;
//	}

	public Texture GetTexture() {
		return this.Texture;
	}

	public Vector2f[] GetTextureCoordinates() {
		return this.TextureCoordinates;
	}
	public void setTexture(Texture text){
		this.Texture=text;
	}
	public void setTextCoords(Vector2f[] textCoords){
		this.TextureCoordinates=textCoords;

	}
}
