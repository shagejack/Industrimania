package shagejack.minecraftology.tile;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface IMCLTickable {
    void onServerTick(TickEvent.Phase phase, World world);
}
