package shagejack.shagecraft.api.container;

import net.minecraft.entity.player.EntityPlayer;
import shagejack.shagecraft.machines.ShageTileEntityMachine;

public interface IMachineWatcher {
    EntityPlayer getPlayer();

    void onWatcherAdded(ShageTileEntityMachine machine);

    boolean isWatcherValid();
}

