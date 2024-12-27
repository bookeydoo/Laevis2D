package Laevis;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements  JsonDeserializer<GameObject>{


    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext Context) throws JsonParseException {
        JsonObject jsonObject=jsonElement.getAsJsonObject();
        String name=jsonObject.get("Name").getAsString();
        JsonArray components=jsonObject.getAsJsonArray("Components");
        Transform transform=Context.deserialize(jsonObject.get("Transform"),Transform.class);
        int zIndex=Context.deserialize(jsonObject.get("zIndex"),int.class);

        GameObject GO=new GameObject(name,transform,zIndex);

        for(JsonElement e:components){
            Component c= Context.deserialize(e,Component.class);
            GO.AddComponent(c);
        }
        return GO;
    }
}
