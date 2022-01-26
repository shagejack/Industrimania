package shagejack.industrimania.content.steamAge.steam;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public interface ISteamHandler {

    @Nonnull
    SteamStack getSteamInStorage(int number);

}
