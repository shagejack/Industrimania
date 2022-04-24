package shagejack.industrimania.content.steamAge.block.boiler;

import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.content.steamAge.steam.AbstractSteamStorage;
import shagejack.industrimania.foundation.tileEntity.SyncedTileEntity;

public class BoilerSteamStorage extends AbstractSteamStorage {

    public BoilerSteamStorage(@NotNull SyncedTileEntity parent, double maxVolume, double maxPressure, double maxStress, double durability) {
        super(parent, maxVolume, maxPressure, maxStress, durability);
    }

    @Override
    public void manageTransfer() {
        // TODO: steam transfer
    }

    @Override
    public void explode() {

    }

    @Override
    public double getSteamPower() {
        return 0;
    }

    @Override
    public double getSteamWork() {
        return 0;
    }
}
