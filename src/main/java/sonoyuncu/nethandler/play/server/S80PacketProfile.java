package sonoyuncu.nethandler.play.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S80PacketProfile implements Packet<INetHandlerPlayClient> {

    public Map<String, String> profile = new HashMap();
    private boolean test = false;

    public S80PacketProfile() {
        this(false);
    }

    public S80PacketProfile(final boolean test) {
        this(test, new HashMap<String, String>());
    }

    public S80PacketProfile(final boolean test, final Map<String, String> profile) {
        this.test = test;
        this.profile = profile;
    }


    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        final int size = buf.readVarIntFromBuffer();
        for (int i = 0; i < size; i++) {
            byte[] array1 = buf.readByteArray();
            byte[] array2 = buf.readByteArray();

            this.profile.put(new String(array1, StandardCharsets.UTF_8), new String(array2, StandardCharsets.UTF_8));
        }
        if (buf.isReadable()) {
            this.test = buf.readBoolean();
        }

    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.profile.size());
        this.profile.forEach((key, value) -> {
            buf.writeByteArray(key.getBytes(StandardCharsets.UTF_8));
            buf.writeByteArray(value.getBytes(StandardCharsets.UTF_8));
        });
        buf.writeBoolean(this.test);

    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        //handler.handleProfile(this);
    }


}
