/*
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
 */

package shagejack.industrimania.foundation.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import shagejack.industrimania.foundation.data.IMDataManager.IMDataType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IMDataReloadListener implements ResourceManagerReloadListener {
    public static final IMDataReloadListener INSTANCE = new IMDataReloadListener();

    private IMDataReloadListener() {}

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        IMDataManager.reset();
        for (IMDataType dat : IMDataType.values()) {
            for (ResourceLocation rl : manager.listResources(dat.type.getLocation(), s -> s.endsWith(".json"))) {
                try {
                    try (Resource rc = manager.getResource(rl); InputStream stream = rc.getInputStream(); InputStreamReader reader = new InputStreamReader(stream)) {
                        JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
                        IMDataManager.register(dat, object);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
