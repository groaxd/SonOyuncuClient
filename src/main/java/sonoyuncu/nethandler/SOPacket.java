package sonoyuncu.nethandler;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.*;
import net.minecraft.network.*;

/* Packets */
import sonoyuncu.nethandler.play.client.*;
import sonoyuncu.nethandler.play.server.*;

import sonoyuncu.nethandler.login.client.*;
import sonoyuncu.nethandler.login.server.*;
public enum SOPacket {
INSTANCE; 
	
	private State State;
	public void initialize()
	{
		/* Usage: EnumPacketDirection Class , Packet Id Packet Class */
		
		this.setState(State.play_server);
		this.registerCustomPacket(EnumPacketDirection.SERVERBOUND, 83, C83PacketCipherRelease.class);
		
		
		this.setState(State.play_server);
		this.registerCustomPacket(EnumPacketDirection.CLIENTBOUND, 80, S80PacketProfile.class);
		
		
		this.setState(State.login_server);
		this.registerCustomPacket(EnumPacketDirection.CLIENTBOUND, 80, S80PacketSessionIDGenerator.class);
		
		
		this.setState(State.login_client);
		this.registerCustomPacket(EnumPacketDirection.SERVERBOUND, 80, C80PacketSessionIDReceiver.class);
	}
	private void registerCustomPacket(EnumPacketDirection direction, Integer id, Class clazz)
    {
    	EnumConnectionState enum0 = EnumConnectionState.PLAY;
        BiMap bimap = (BiMap)enum0.directionMaps.get(direction);

        if (bimap == null)
        {
            bimap = HashBiMap.create();
            enum0.directionMaps.put(direction, bimap);
        }

        if (!bimap.containsValue(clazz))
            bimap.put(id, clazz);
    }
private enum State
{
	play_client, play_server, login_client, login_server;
}

public final State getState() {
	return State;
}

public final void setState(State State) {
	this.State = State;
}
}
