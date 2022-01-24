package shagejack.industrimania.content.steamAge.steam;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public interface ISteamStorage {

    /**
     * @return SteamStack representing the steam in the storage
     */
    @Nonnull
    SteamStack getSteam();

    double getMaxVolume();

    double getMaxPressure();

    double getMaxStress();

    /**
     * @return current mass of steam in the storage
     */
    double getSteamMass();

    /**
     * @return current temperature of steam in the storage
     */
    double getSteamTemperature();

    SteamState getSteamState();

    AbstractSteamStorage setSteamMass(double mass);

    AbstractSteamStorage setSteamTemperature(double temperature);

    AbstractSteamStorage setSteamState(SteamState state);

    AbstractSteamStorage setSteamState(int state);

    double getSteamPressure();

    double getSteamDensity();

    double getSteamVolume();

    double getSteamStress();

    double getSteamMoistureContent();

    double getSteamLatentHeat();

    double getSteamEnthalpy();

    double getSteamEnthalpyConsume();

    double getSteamPower();

    double getSteamWork();

    AbstractSteamStorage doWork();

    AbstractSteamStorage clear();

    AbstractSteamStorage damageStorage(double amount);

    AbstractSteamStorage repairStorage(double amount);

    int getNumber();

    boolean isBroken();

    boolean isOverloaded();

    @NotNull
    default AbstractSteamStorage getStorageFromNumber(@NonNull List<? extends AbstractSteamStorage> storages, int number) throws IllegalArgumentException {
        Iterator<? extends AbstractSteamStorage> matchedStorage = storages.stream().filter((storage) -> storage.getNumber() == number).iterator();
        if (!matchedStorage.hasNext())
            return matchedStorage.next();
        throw new IllegalArgumentException(String.valueOf(number));
    }

}
