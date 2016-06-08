package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import common.client.*;
import common.server.*;

public class MessageJsonInterface
{
	private final boolean DEBUG = false;
	
	private final Gson gson;
	private final TypeToken<ClientMessage> cmTypeToken;
	private final TypeToken<ServerMessage> smTypeToken;
	private final RuntimeTypeAdapterFactory<ClientMessage> cmAdapter;
	private final RuntimeTypeAdapterFactory<ServerMessage> smAdapter;
    
    public MessageJsonInterface()
    {   
    	this.cmTypeToken = new TypeToken<ClientMessage>() {};
    	this.smTypeToken = new TypeToken<ServerMessage>() {};
    	
    	this.cmAdapter = RuntimeTypeAdapterFactory.of(ClientMessage.class, "type");
    	this.cmAdapter.registerSubtype(CreateRoomCM.class, "createroom");
    	this.cmAdapter.registerSubtype(DeleteCM.class, "delete");
    	this.cmAdapter.registerSubtype(IdentChangeCM.class, "identitychange");
    	this.cmAdapter.registerSubtype(JoinCM.class, "join");
    	this.cmAdapter.registerSubtype(KickCM.class, "kick");
    	this.cmAdapter.registerSubtype(ListCM.class, "list");
    	this.cmAdapter.registerSubtype(MessageCM.class, "message");
    	this.cmAdapter.registerSubtype(QuitCM.class, "quit");
    	this.cmAdapter.registerSubtype(WhoCM.class, "who");
 
    	this.smAdapter = RuntimeTypeAdapterFactory.of(ServerMessage.class, "type");
    	this.smAdapter.registerSubtype(MessageSM.class, "message");
    	this.smAdapter.registerSubtype(NewIdentitySM.class, "newidentity");
    	this.smAdapter.registerSubtype(RoomChangeSM.class, "roomchange");
    	this.smAdapter.registerSubtype(RoomContentsSM.class, "roomcontents");
    	this.smAdapter.registerSubtype(RoomListSM.class, "roomlist");
    	this.smAdapter.registerSubtype(InvalidSM.class, "invalid");
    	this.smAdapter.registerSubtype(NoOpSM.class, "noop");
    	
    	this.gson = new GsonBuilder()
    			.registerTypeAdapterFactory(cmAdapter)
    			.registerTypeAdapterFactory(smAdapter)
    			.create();
    }

    public String clientMessageToJson(ClientMessage m)
    {   
    	String data = gson.toJson(m, cmTypeToken.getType());
    	if (DEBUG) {
    		System.out.println(data);
    	}
    	return data;
    }
    
    public String serverMessageToJson(ServerMessage m)
    {
    	String data = gson.toJson(m, smTypeToken.getType());
    	if (DEBUG) {
    		System.out.println(data);
    	}
    	return data;
    }
    
    public ClientMessage jsonToClientMessage(String jsonStr)
    {   
    	if (DEBUG) {
    		System.out.println(jsonStr);
    	}
    	return gson.fromJson(jsonStr, cmTypeToken.getType());
    }
    
    public ServerMessage jsonToServerMessage(String jsonStr)
    {
    	if (DEBUG) {
    		System.out.println(jsonStr);
    	}
    	return gson.fromJson(jsonStr, smTypeToken.getType());
    }
}
