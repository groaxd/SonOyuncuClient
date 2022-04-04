package sonoyuncu.impl.event;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import sonoyuncu.api.event.Event;

public class TheTwoBuffer extends Event {
    public Packet packet;
    public PacketBuffer buf;
    public TheTwoBuffer(Packet packet, PacketBuffer buf) {
        this.packet = packet;
        this.buf = buf;
    }
}
