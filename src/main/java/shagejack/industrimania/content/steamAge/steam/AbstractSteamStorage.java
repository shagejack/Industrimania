package shagejack.industrimania.content.steamAge.steam;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.foundation.network.AllPackets;
import shagejack.industrimania.foundation.tileEntity.SyncedTileEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSteamStorage<T extends SyncedTileEntity> implements ISteamHandler, ISteamStorage {

    @Nonnull
    T parent;
    protected SteamStack steam = SteamStack.EMPTY;
    protected double maxVolume;
    protected double maxPressure;
    protected double maxStress;
    protected double durability;
    protected int number;

    public AbstractSteamStorage(@NotNull T parent, double maxVolume, double maxPressure, double maxStress, double durability)
    {
        this.parent = parent;
        this.maxVolume = maxVolume;
        this.maxPressure = maxPressure;
        this.maxStress = maxStress;
        this.durability = durability;
        this.number = 0;
    }

    public AbstractSteamStorage(@NotNull T parent, double maxVolume, double maxPressure, double maxStress, double durability, int number)
    {
        this.parent = parent;
        this.maxVolume = maxVolume;
        this.maxPressure = maxPressure;
        this.maxStress = maxStress;
        this.durability = durability;
        this.number = number;
    }

    public void tick() {
        this.manageSteamStored();
        this.manageTransfer();
    }

    /**
     * @return a boolean value representing if storage has exploded. If it's true, the child class method should be exited with a same return value.
     */
    public boolean manageSteamStored() {
        if (isBroken()) {
            explode();
            return true;
        }

        if (isOverloaded()) {
            damageOverloadedStorage();
        }

        return false;
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
        onContentsChanged();
    }

    public int getNumber() {
        return this.number;
    }

    @Nonnull
    public SteamStack getSteam()
    {
        return this.steam;
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

    public double getSteamPressure()
    {
        return steam.getPressure();
    }

    public double getSteamDensity() {
        return steam.getDensity();
    }

    public double getSteamVolume() {
        return steam.getVolume();
    }

    public double getSteamStress() {
        return steam.getStress();
    }

    public double getSteamMoistureContent() {
        return steam.getMoistureContent();
    }

    public double getSteamLatentHeat() {
        return steam.getLatentHeat();
    }

    public double getSteamEnthalpy() {
        return steam.getEnthalpy();
    }

    public double getSteamEnthalpyConsume() {
        return steam.getEnthalpyConsume();
    }

    public double getSteamPower(double efficiency) {
        return steam.getPower(efficiency);
    }

    public double getSteamWork(double efficiency) {
        return steam.getWork(efficiency);
    }

    public AbstractSteamStorage doWork() {
        this.steam.doWork();
        return this;
    }

    public AbstractSteamStorage clear() {
        this.steam = SteamStack.EMPTY;
        return this;
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

    public AbstractSteamStorage setSteam(@NotNull SteamStack stack) {
        this.steam = stack;
        onContentsChanged();
        return this;
    }

    public AbstractSteamStorage setSteamMass(double mass)
    {
        if (mass <= 0) {
            this.clear();
            return this;
        }

        this.steam.setMass(mass);
        onContentsChanged();
        return this;
    }

    public AbstractSteamStorage setSteamTemperature(double temperature)
    {
        if (temperature <= 273.15) {
            this.clear();
            return this;
        }

        this.steam.setTemperature(temperature);
        onContentsChanged();
        return this;
    }

    public AbstractSteamStorage setSteamState(SteamState state)
    {
        if (state == SteamState.EMPTY) {
            this.clear();
            return this;
        }

        this.steam.setState(state);
        onContentsChanged();
        return this;
    }

    public AbstractSteamStorage setSteamState(int state)
    {
        if (state == 0) {
            this.clear();
            return this;
        }

        this.steam.setState(state);
        onContentsChanged();
        return this;
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

        if (this.durability < 0)
            this.durability = 0;

        return this;
    }

    public AbstractSteamStorage repairStorage(double amount)
    {
        if (this.durability < 0)
            this.durability = 0;

        this.durability += amount;

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

    public void damageOverloadedStorage() {
        damageStorage(getOverloadDamage());
    }

    public boolean isOverloaded() {
        return getSteamVolume() > getMaxVolume() || getSteamPressure() > getMaxVolume() || getSteamStress() > getMaxStress();
    }

    protected double getOverloadDamage() {
        if (!isOverloaded()) return 0;
        double damage = 0;
        damage += getSteamVolume() - getMaxVolume();
        damage += getSteamPressure() - getMaxPressure();
        damage += getSteamStress() - getMaxStress();
        return damage;
    }

    public boolean isBroken() {
        return getDurability() <= 0;
    }

    protected void onContentsChanged()
    {
        if (parent instanceof ISteamStorageUpdater) {
            ((ISteamStorageUpdater) parent).onTankContentsChanged();
        }

        parent.setChanged();
        Level level = parent.getLevel();
        if(level != null && !level.isClientSide()) {
            AllPackets.sendToNear(level, parent.getBlockPos(), new SteamUpdatePacket(parent.getBlockPos(), this.getSteam()));
        }
    }

}
