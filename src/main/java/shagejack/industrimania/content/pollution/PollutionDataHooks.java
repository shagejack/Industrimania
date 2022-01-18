package shagejack.industrimania.content.pollution;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import shagejack.industrimania.api.pollution.IPollutionData;

import java.util.concurrent.ConcurrentHashMap;

public class PollutionDataHooks implements IPollutionData {

    public static ConcurrentHashMap<LevelChunk, Pollution> pollutionMap = new ConcurrentHashMap<>();

    public static Pollution getPollution(Level level, ChunkPos chunkPos) {
        return pollutionMap.get(new LevelChunk(level, chunkPos));
    }
    public static Pollution getPollution(Level level, int chunkX, int chunkZ) {
        return pollutionMap.get(new LevelChunk(level, new ChunkPos(chunkX, chunkZ)));
    }


    public static void saveData(Level level, int chunkX, int chunkZ, CompoundTag data) {
        LevelChunk key = new LevelChunk(level, new ChunkPos(chunkX, chunkZ));
        Pollution pollution = pollutionMap.get(key);
        if (pollution != null) {
            data.put("pollution", pollution.getTag());
        } else {
            data.getCompound("pollution").putLong("amount", 0);
        }
    }

    public static void readData(Level level, int chunkX, int chunkZ, CompoundTag data) {
        LevelChunk key = new LevelChunk(level, new ChunkPos(chunkX, chunkZ));
        if (pollutionMap.get(key) == null) {
            Pollution pollution = Pollution.getPollution(data.getCompound("pollution"));
            pollutionMap.put(key, pollution);
        } else {
            pollutionMap.get(key).setAmountFromTag(data.getCompound("pollution"));
        }
    }

    public static void clearData(Level level, int chunkX, int chunkZ) {
        pollutionMap.remove(new LevelChunk(level, new ChunkPos(chunkX, chunkZ)));
    }

}
