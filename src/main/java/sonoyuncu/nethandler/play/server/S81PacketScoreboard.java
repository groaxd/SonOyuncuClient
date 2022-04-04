package sonoyuncu.nethandler.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import sonoyuncu.api.scoreboard.Elements;

import java.io.IOException;

public class S81PacketScoreboard implements Packet<INetHandlerPlayClient> {

    public Elements element;
    public byte[] jsonObject;

    public S81PacketScoreboard() {
    }

    public S81PacketScoreboard(final Elements element, final byte[] jsonObject) {
        this.element = element;
        this.jsonObject = jsonObject;
    }


    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.element = Elements.get(buf.readVarIntFromBuffer());
        this.jsonObject = buf.readByteArray();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.element.ordinal());
        buf.writeByteArray(this.jsonObject);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        //handler.handleScoreboard(this);
    }

}
