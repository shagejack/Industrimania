package shagejack.industrimania.content.steamAge.steam;


import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSteamStorage implements ISteamHandler, ISteamStorage {

    @Nonnull
    protected SteamStack steam = SteamStack.EMPTY;
    protected double maxVolume;
    protected double maxPressure;
    protected double maxStress;
    protected double durability;

    public AbstractSteamStorage(double maxVolume, double maxPressure, double maxStress, double durability)
    {
        this.maxVolume = maxVolume;
        this.maxPressure = maxPressure;
        this.maxStress = maxStress;
        this.durability = durability;
    }

    public void manageSteamStored() {

    }

    public void manageTransfer() {

    }

    public void explode() {

    }

    public List<SteamStack> divideSteam(int count) {
        List<SteamStack> stackList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            stackList.add(steam.getDivided(count));
        }
        return stackList;
    }

    public void mergeSteamWithStored(SteamStack stack) {
        this.steam = SteamStack.mergeSteamStack(getSteam(), stack);
    }

    @Nonnull
    public SteamStack getSteam()
    {
        return steam;
    }

    public double getSteamMass()
    {
        return steam.getMass();
    }

    public double getSteamTemperature()
    {
        return steam.getTemperature();
    }

    public SteamState getSteamState()
    {
        return steam.getState();
    }

    public void clear() {
        this.steam = SteamStack.EMPTY;
    }

    public boolean isEmpty()
    {
        return steam.isEmpty();
    }

    public AbstractSteamStorage readFromNBT(CompoundTag tag) {
        SteamStack stack = SteamStack.loadSteamStackFromNBT(tag);
        setSteam(stack);
        return this;
    }

    public CompoundTag writeToNBT(CompoundTag tag) {
        steam.writeToNBT(tag);
        return tag;
    }

    public void setSteam(@NotNull SteamStack stack) {
        this.steam = stack;
    }

    public AbstractSteamStorage setMaxVolume(double maxVolume)
    {
        this.maxVolume = maxVolume;
        return this;
    }

    public AbstractSteamStorage setMaxPressure(double maxPressure)
    {
        this.maxPressure = maxPressure;
        return this;
    }

    public AbstractSteamStorage setMaxStress(double maxStress)
    {
        this.maxStress = maxStress;
        return this;
    }

    public AbstractSteamStorage setDurability(double durability)
    {
        this.durability = durability;
        return this;
    }

    public AbstractSteamStorage damageStorage(double amount)
    {
        this.durability -= amount;
        return this;
    }

    public double getMaxVolume()
    {
        return maxVolume;
    }

    public double getMaxPressure()
    {
        return maxPressure;
    }

    public double getMaxStress()
    {
        return maxStress;
    }

    public double getDurability()
    {
        return durability;
    }

}
