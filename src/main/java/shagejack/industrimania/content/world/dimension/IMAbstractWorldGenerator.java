package shagejack.industrimania.content.world.dimension;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryWriteOps;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.HashSet;

public abstract class IMAbstractWorldGenerator implements DataProvider {

    private final DataGenerator generator;

    private HashCache cache;
    private final HashSet<Object> SerializeCache = new HashSet<>();

    public IMAbstractWorldGenerator(DataGenerator generator) {
        this.generator = generator;
    }

    private void generate() {

    }


}
