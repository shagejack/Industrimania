package shagejack.industrimania.foundation.utility;

import net.minecraftforge.event.TickEvent;

import java.util.EnumSet;

public interface ITickHandler {

    String getName();

    void tick(TickEvent.Type type, Object... context);

    EnumSet<TickEvent.Type> getHandledTypes();

    boolean shouldFire(TickEvent.Phase phase);

}
