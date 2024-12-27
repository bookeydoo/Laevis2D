package Laevis;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ComponentDeserializer  implements JsonSerializer<Component>, JsonDeserializer<Component> {


    @Override
    public Component deserialize(JsonElement jsonIO, Type type, JsonDeserializationContext Context) throws JsonParseException {
        JsonObject JsonObject=jsonIO.getAsJsonObject();
        String Type=JsonObject.get("type").getAsString();
        JsonElement element=JsonObject.get("properties");

        try {
            return Context.deserialize(element,Class.forName(Type));
        }catch (ClassNotFoundException e){
            throw new JsonParseException("Unknown element type"+ type,e);
        }

    }

    @Override
    public JsonElement serialize(Component Source, Type type, JsonSerializationContext Context) {
       JsonObject result=new JsonObject();
       result.add("type",new JsonPrimitive(Source.getClass().getCanonicalName()));
       result.add("properties",Context.serialize(Source,Source.getClass()));
       return result;
    }
}
