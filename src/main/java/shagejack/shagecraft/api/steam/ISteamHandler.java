package shagejack.shagecraft.api.steam;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Used as a interface for all matter storage entities or components.
 */
public interface ISteamHandler extends INBTSerializable<NBTTagCompound> {
    /**
     * @return the current steam volume.
     */
    double getSteamVolume();

    /**
     * @return the current steam temperature.
     */
    double getSteamTemp();

    double getSteamPressure();

    double getSteamHumidity();

    int getSteamState();

    void setSteamVolume(double volume);

    void setSteamTemp(double temp);

    void setSteamPressure(double pressure);

    void setSteamHumidity(double humidity);

    void setSteamState(int state);

    /**
     * @return the maximum capacity.
     */
    double getCapacity();

    /**
     * @param capacity Sets the capacity
     */
    void setCapacity(double capacity);

    /**
     * Modifies the amount of matter stored by the given amount.
     *
     * @param amount The amount of matter to add/subtract
     * @return The amount added/subtracted
     */
    double[] modifySteam(double amount);

    /**
     * Used to receive steam.
     *
     * @param amount   the amount of received matter.
     * @param simulate is this a simulation.
     *                 No matter will be stored if this is true.
     *                 Used to check if the given amount of matter can be received.
     * @return the amount of matter received.
     * This is the same, not matter if the call is a simulation.
     */
    double[] receiveSteam(double[] properties, int state, boolean simulate);

    /**
     * Used to extract steam.
     *
     * @param amount   the amount of matter extracted.
     * @param simulate No matter will be stored if this is set to true.
     *                 Used to check if the specified amount of matter can be extracted.
     * @return the amount of matter extracted.
     * This is the same, no matter if the call is a simulation.
     */
    double[] extractSteam(double[] properties, int state, boolean simulate);

    /**
     * Used to get merged properties.
     * @return 0: volume, 1: temp, 2: pressure, 3: humidity.
     */
    double[] mergeProperties();

    void setProperties(double[] properties);

    @Override
    default NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("steam_volume", getSteamVolume());
        tag.setDouble("steam_temp", getSteamTemp());
        tag.setDouble("steam_pressure", getSteamPressure());
        tag.setDouble("steam_humidity", getSteamHumidity());
        tag.setInteger("steam_state", getSteamState());
        tag.setDouble("capacity", getCapacity());
        return tag;
    }

    @Override
    default void deserializeNBT(NBTTagCompound tag) {
        setSteamVolume(tag.getDouble("steam_volume"));
        setSteamTemp(tag.getDouble("steam_temp"));
        setSteamPressure(tag.getDouble("steam_pressure"));
        setSteamHumidity(tag.getDouble("steam_humidity"));
        setSteamState(tag.getInteger("steam_state"));
        setCapacity(tag.getDouble("capacity"));
    }


}

