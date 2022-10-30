/*
 * Modified from https://github.com/TeamMoegMC/FrostedHeart/blob/master/src/main/java/com/teammoeg/frostedheart/data/FHDataManager.java
 *
 * Copyright (c) 2021 TeamMoeg
 *
 * This file is part of Frosted Heart.
 *
 * Frosted Heart is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Frosted Heart is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Frosted Heart. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package shagejack.industrimania.foundation.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import shagejack.industrimania.Industrimania;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class IMDataManager {

    private IMDataManager() {}

    @SuppressWarnings("rawtypes")
    public static final EnumMap<IMDataType, ResourceMap> ALL_DATA = new EnumMap<>(IMDataType.class);
    public static boolean synched = false;
    private static final JsonParser parser = new JsonParser();

    static {
        for (IMDataType dt : IMDataType.values()) {
            ALL_DATA.put(dt, new ResourceMap<>());
        }
    }

    public static void reset() {
        synched = false;
        for (ResourceMap<?> rm : ALL_DATA.values())
            rm.clear();
    }

    @SuppressWarnings("unchecked")
    public static void register(IMDataType dt, JsonObject data) {
        JsonDataHolder jdh = dt.type.create(data);
        ALL_DATA.get(dt).put(jdh.getId(), jdh);
        synched = false;
    }

    @SuppressWarnings("unchecked")
    public static <T extends JsonDataHolder> ResourceMap<T> get(IMDataType dt) {
        return ALL_DATA.get(dt);

    }

    @SuppressWarnings("unchecked")
    public static void load(DataEntry[] entries) {
        reset();
        for (DataEntry de : entries) {
            JsonDataHolder jdh = de.type.type.create(parser.parse(de.data).getAsJsonObject());
            ALL_DATA.get(de.type).put(jdh.getId(), jdh);
        }
    }

    @SuppressWarnings("rawtypes")
    public static DataEntry[] save() {
        int tsize = 0;
        for (ResourceMap map : ALL_DATA.values()) {
            tsize += map.size();
        }
        DataEntry[] entries = new DataEntry[tsize];
        int i = -1;
        for (Map.Entry<IMDataType, ResourceMap> entry : ALL_DATA.entrySet()) {
            for (Object jdh : entry.getValue().values()) {
                entries[++i] = new DataEntry(entry.getKey(), ((JsonDataHolder) jdh).getData());
            }
        }
        return entries;
    }

    public static BlockTempData getBlockData(Block b) {
        return IMDataManager.<BlockTempData>get(IMDataType.BlockTemp).get(b.getRegistryName());
    }

    public static BlockTempData getBlockData(ItemStack b) {
        return IMDataManager.<BlockTempData>get(IMDataType.BlockTemp).get(b.getItem().getRegistryName());
    }

    public static double getBiomeTemp(Biome b) {
        if (b == null) return 0.0D;
        BiomeTempData data = IMDataManager.<BiomeTempData>get(IMDataType.BiomeTemp).get(b.getRegistryName());
        if (data != null)
            return data.getTemp();
        return 0.0D;
    }

    public enum IMDataType {
        BlockTemp(new DataType<>(BlockTempData.class, "temperature", "block")),
        BiomeTemp(new DataType<>(BiomeTempData.class, "temperature", "biome"))
        ;

        record DataType<T extends JsonDataHolder>(Class<T> dataClass, String domain, String location) {

            public T create(JsonObject object) {
                try {
                    return dataClass.getConstructor(JsonObject.class).newInstance(object);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public String getLocation() {
                return domain + "/" + location;
            }

            public ResourceLocation asResource() {
                return Industrimania.asResource(getLocation());
            }
        }

        public final DataType<? extends JsonDataHolder> type;

        IMDataType(DataType<? extends JsonDataHolder> type) {
            this.type = type;
        }

    }

    public static class ResourceMap<T extends JsonDataHolder> extends HashMap<ResourceLocation, T> {

        private static final long serialVersionUID = 632018284392820937L;

        public ResourceMap() {
            super();
        }

        public ResourceMap(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }

        public ResourceMap(int initialCapacity) {
            super(initialCapacity);
        }

        public ResourceMap(Map<? extends ResourceLocation, ? extends T> m) {
            super(m);
        }
    }
}
