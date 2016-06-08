package common.client;

import java.lang.reflect.Type;

import com.google.gson.*;


public class ClientMessageAdapter implements JsonSerializer<ClientMessage>, JsonDeserializer<ClientMessage>
{
	private static final String CLASSNAME = "CLASSNAME";
	private static final String INSTANCE = "INSTANCE";
	

	@Override
	public JsonElement serialize(ClientMessage src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject retValue = new JsonObject();
		String className = src.getClass().getCanonicalName();
		retValue.addProperty(CLASSNAME, className);
		JsonElement elem = context.serialize(src);
		retValue.add(INSTANCE, elem);
		return retValue;
	}
	
	@Override
	public ClientMessage deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
		String className = prim.getAsString();
		
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new JsonParseException(e.getMessage());
		}
		return context.deserialize(jsonObject.get(INSTANCE), cls);		
	}

	
}
