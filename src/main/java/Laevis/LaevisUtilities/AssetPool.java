package LaevisUtilities;

import Components.SpriteSheet;
import Renderer.Shader;
import Renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> Shaders = new HashMap<>();
    private static Map<String, Texture> Textures = new HashMap<>();
    private static Map<String, SpriteSheet> SpriteSheets = new HashMap<>();

    public static Shader GetShader(String ResourceName) {
        File file = new File(ResourceName);
        if (AssetPool.Shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.Shaders.get(file.getAbsolutePath());
        } else {
            Shader Shader = new Shader(ResourceName);
            Shader.CompileShader();
            AssetPool.Shaders.put(file.getAbsolutePath(), Shader);
            return Shader;
        }
    }

    public static Texture GetTexture(String ResourceName) {
        File file = new File(ResourceName);
        if (AssetPool.Textures.containsKey(file.getAbsolutePath())) {
            return AssetPool.Textures.get(file.getAbsolutePath());
        } else {
            Texture Texture = new Texture();
            Texture.init(ResourceName);
            AssetPool.Textures.put(file.getAbsolutePath(), Texture);
            return Texture;
        }
    }

    public static void AddSpriteSheet(String ResourceName, SpriteSheet SpriteSheet) {
        File file = new File(ResourceName);
        if (!AssetPool.SpriteSheets.containsKey(file.getAbsolutePath())) {
            AssetPool.SpriteSheets.put(file.getAbsolutePath(), SpriteSheet);
        }
    }

    public static SpriteSheet GetSpriteSheet(String ResourceName) {
        File file = new File(ResourceName);
        if (!AssetPool.SpriteSheets.containsKey(file.getAbsolutePath())) {
            assert false : "Error: Tried to Access Sprite Sheet " + ResourceName + " and it has not been added to Asset Pool";
        }
        return AssetPool.SpriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
