package sonoyuncu.nethandler.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S82PacketChingChong implements Packet<INetHandlerPlayClient> {
    private String str;

    public S82PacketChingChong() {
    }

    public S82PacketChingChong(final String str) {
        this.str = str;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.str = buf.readStringFromBuffer(32767);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(str);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {

    }

}
