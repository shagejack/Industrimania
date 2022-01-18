package shagejack.industrimania.content.pollution;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;
import shagejack.industrimania.api.pollution.IPollutionData;
import shagejack.industrimania.content.pollution.record.ChunkReference;

import java.util.concurrent.ConcurrentHashMap;

public class PollutionDataHooks implements IPollutionData {

    public static ConcurrentHashMap<ChunkReference, Pollution> pollutionMap = new ConcurrentHashMap<>();

    public static Pollution getPollution(DimensionType dimension, ChunkPos chunkPos) {
        Pollution pollution = pollutionMap.get(new ChunkReference(dimension, chunkPos));
        if (pollution != null) {
            return pollution;
        } else {
            Pollution p = new Pollution();
            pollutionMap.put(new ChunkReference(dimension, chunkPos), p);
            return p;
        }
    }
    public static Pollution getPollution(DimensionType dimension, int chunkX, int chunkZ) {
        Pollution pollution = pollutionMap.get(new ChunkReference(dimension, new ChunkPos(chunkX, chunkZ)));
        if (pollution != null) {
            return pollution;
        } else {
            Pollution p = new Pollution();
            pollutionMap.put(new ChunkReference(dimension, new ChunkPos(chunkX, chunkZ)), p);
            return p;
        }
    }

    public static void putPollution(DimensionType dimension, ChunkPos chunkPos, Pollution pollution) {
        if (pollution == null) pollution = new Pollution();
        pollutionMap.put(new ChunkReference(dimension, chunkPos), pollution);
    }

    public static void putPollution(DimensionType dimension, int chunkX, int chunkZ, Pollution pollution) {
        if (pollution == null) pollution = new Pollution();
        pollutionMap.put(new ChunkReference(dimension, new ChunkPos(chunkX, chunkZ)), pollution);
    }

    public static Pollution getPollutionFromTag(CompoundTag tag) {
        return new Pollution(tag.getLong("amount"));
    }

    public static void saveData(DimensionType dimension, int chunkX, int chunkZ, CompoundTag data) {
        ChunkReference key = new ChunkReference(dimension, new ChunkPos(chunkX, chunkZ));
        Pollution pollution = pollutionMap.get(key);
        if (pollution != null) {
            data.put("pollution", pollution.getTag());
        } else {
            data.getCompound("pollution").putLong("amount", 0);
        }
    }

    public static void readData(DimensionType dimension, int chunkX, int chunkZ, CompoundTag data) {
        ChunkReference key = new ChunkReference(dimension, new ChunkPos(chunkX, chunkZ));
        if (!pollutionMap.containsKey(key)) {
            Pollution pollution = getPollutionFromTag(data.getCompound("pollution"));
            pollutionMap.put(key, pollution);
        } else {
            pollutionMap.get(key).setAmountFromTag(data.getCompound("pollution"));
        }
    }

    public static void clearData(DimensionType dimension, int chunkX, int chunkZ) {
        pollutionMap.remove(new ChunkReference(dimension, new ChunkPos(chunkX, chunkZ)));
    }

}
