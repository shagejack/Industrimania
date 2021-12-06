package shagejack.shagecraft.tile;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface IShageTickable {
    void onServerTick(TickEvent.Phase phase, World world);
}
