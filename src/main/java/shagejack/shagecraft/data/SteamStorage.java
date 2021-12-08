package shagejack.shagecraft.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import shagejack.shagecraft.api.steam.ISteamHandler;

public class SteamStorage implements ISteamHandler {

    private double steam_volume;
    private double steam_temp;
    private double steam_pressure;
    private double steam_humidity;
    private int steam_state;

    private double capacity;
    private double maxExtract;
    private double maxReceive;

    public SteamStorage(double capacity) {
        this(capacity, capacity, capacity);
    }

    public SteamStorage(double capacity, double maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public SteamStorage(double capacity, double maxExtract, double maxReceive) {
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public double getMaxExtract() {
        return maxExtract;
    }

    public void setMaxExtract(double maxExtract) {
        this.maxExtract = maxExtract;
    }

    public double getMaxReceive() {
        return maxReceive;
    }

    public void setMaxReceive(double maxReceive) {
        this.maxReceive = maxReceive;
    }

    @Override
    public double[] modifySteam(double amount) {
        double lastAmount = steam_volume;
        double newAmount = lastAmount + amount;
        newAmount = MathHelper.clamp(newAmount, 0, getCapacity());
        setSteamVolume(newAmount);
        //return lastAmount - newAmount;
        return new double[0];
    }

    @Override
    public double getSteamVolume() {
        return steam_volume;
    }

    @Override
    public double getSteamTemp() {
        return steam_temp;
    }

    @Override
    public double getSteamPressure() {
        return steam_pressure;
    }

    @Override
    public double getSteamHumidity() {
        return steam_humidity;
    }

    @Override
    public int getSteamState() {
        return steam_state;
    }

    @Override
    public void setSteamVolume(double volume) {

    }

    @Override
    public void setSteamTemp(double temp) {

    }

    @Override
    public void setSteamPressure(double pressure) {

    }

    @Override
    public void setSteamHumidity(double humidity) {

    }

    @Override
    public void setSteamState(int state) {

    }

    @Override
    public double getCapacity() {
        return 0;
    }

    @Override
    public void setCapacity(double capacity) {

    }

    @Override
    public double[] receiveSteam(double[] properties, int state, boolean simulate) {
        return properties;
    }

    @Override
    public double[] extractSteam(double[] properties, int state, boolean simulate) {
        return properties;
    }

    public SteamStorage readFromNBT(NBTTagCompound nbt)
    {
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {

        return nbt;
    }

    @Override
    public double[] mergeProperties(){
        double[] properties = new double[4];
        properties[0] = getSteamVolume();
        properties[1] = getSteamTemp();
        properties[2] = getSteamPressure();
        properties[3] = getSteamHumidity();
        return properties;
    }

    @Override
    public void setProperties(double[] properties){
        setSteamVolume(properties[0]);
        setSteamTemp(properties[1]);
        setSteamPressure(properties[2]);
        setSteamHumidity(properties[3]);
    }
}
