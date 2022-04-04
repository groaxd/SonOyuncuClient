package sonoyuncu.api.event;

import sonoyuncu.impl.main.SOPacket;

public class Event {
    public boolean cancelled = false;

    public Event() {
        SOPacket.INSTANCE.eventBus.post(this);
    }
}
