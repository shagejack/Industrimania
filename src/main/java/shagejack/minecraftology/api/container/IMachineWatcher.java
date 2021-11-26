package shagejack.minecraftology.api.container;

import net.minecraft.entity.player.EntityPlayer;
import shagejack.minecraftology.machines.MCLTileEntityMachine;

public interface IMachineWatcher {
    EntityPlayer getPlayer();

    void onWatcherAdded(MCLTileEntityMachine machine);

    boolean isWatcherValid();
}

