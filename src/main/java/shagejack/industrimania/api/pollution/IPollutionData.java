package shagejack.industrimania.api.pollution;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public interface IPollutionData {
    static void saveData(Level level, int chunkX, int chunkZ, CompoundTag data) {

    }

    static CompoundTag readData(Level level, int chunkX, int chunkZ) {
        return null;
    }

    static void clearData(Level level, int chunkX, int chunkZ) {

    }

}
