package shagejack.shagecraft.api.steam;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Used as a interface for all matter storage entities or components.
 */
public interface ISteamHandler extends INBTSerializable<NBTTagCompound> {
    /**
     * @return the current steam mass.
     */
    double getSteamMass();

    /**
     *
     * @param properties
     * @return actual enthalpy in KJ/kg
     */
    double getActualEnthalpy(double[] properties);

    /**
     *
     * @param efficiency
     * @param mass
     * @param enthalpy
     * @return actual work in KJ.
     */
    double getActualWork(double efficiency, double mass, double enthalpy);

    double getCurrentActualWork(double efficiency);

    /**
     *
     * @return actual power in KW.
     */
    double getActualPower(double efficiency, double mass, double pressure);

    double getCurrentActualPower(double efficiency);

    double getCurrentEnthalpyConsume();

    /**
     *
     * @param consume consumed enthalpy.
     * @return temperature drop from consumed enthalpy;
     */
    double manageSteamTempFromConsume(double consume);

    /**
     * @return the current steam temperature.
     */
    double getSteamTemp();

    double getSteamPressure();

    //double getSteamQuality();

    int getSteamState();

    void setSteamMass(double mass);

    void setSteamTemp(double temp);

    //void setSteamPressure(double pressure);

    //void setSteamQuality(double quality);

    void setSteamState(int state);

    /**
     * empty the steam storage.
     */
    void releaseSteam();

    boolean hasSteam();

    boolean isExceededCapacity();

    /**
     * @return the maximum capacity.
     */
    double getCapacity();

    /**
     * @param capacity Sets the capacity.
     */
    void setCapacity(double capacity);

    /**
     * Used to receive steam.
     *
     * @param properties   the properties of received matter.
     * @param simulate is this a simulation.
     *                 No steam will be stored if this is true.
     *
     * @return the amount of matter received.
     * This is the same, not matter if the call is a simulation.
     */
    double[] receiveSteam(double[] properties, boolean simulate);

    /**
     * Used to extract steam.
     *
     * @param properties  the properties of steam extracted.
     * @param simulate No steam will be stored if this is set to true.
     *
     * @return the amount of matter extracted.
     * This is the same, no matter if the call is a simulation.
     */
    double[] mergeSteam(double[] properties, double[] target, boolean simulate);

    /**
     * Used to get merged properties.
     * @return 0: mass, 1: temp, 2: state.
     */
    double[] mergeProperties();

    void setProperties(double[] properties);

    @Override
    default NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("steam_mass", getSteamMass());
        tag.setDouble("steam_temp", getSteamTemp());
        //tag.setDouble("steam_pressure", getSteamPressure());
        //tag.setDouble("steam_quality", getSteamQuality());
        tag.setInteger("steam_state", getSteamState());
        tag.setDouble("capacity", getCapacity());
        return tag;
    }

    @Override
    default void deserializeNBT(NBTTagCompound tag) {
        setSteamMass(tag.getDouble("steam_mass"));
        setSteamTemp(tag.getDouble("steam_temp"));
        //setSteamPressure(tag.getDouble("steam_pressure"));
        //setSteamQuality(tag.getDouble("steam_quality"));
        setSteamState(tag.getInteger("steam_state"));
        setCapacity(tag.getDouble("capacity"));
    }


}

