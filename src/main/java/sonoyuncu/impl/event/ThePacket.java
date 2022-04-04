package sonoyuncu.impl.event;

import net.minecraft.network.Packet;
import sonoyuncu.api.event.Event;

public class ThePacket extends Event {
    public Packet packet;
    public boolean read;
    public ThePacket(Packet packet, boolean read) {
        this.packet = packet;
        this.read = read;
    }
}
