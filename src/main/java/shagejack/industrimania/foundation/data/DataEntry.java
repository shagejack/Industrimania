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

import net.minecraft.network.FriendlyByteBuf;
import shagejack.industrimania.foundation.data.IMDataManager.IMDataType;

public class DataEntry {
    IMDataType type;
    String data;

    public DataEntry(IMDataType type, JsonObject data) {
        this.type = type;
        this.data = data.toString();
    }

    public DataEntry(FriendlyByteBuf buffer) {
        this.type = IMDataType.values()[buffer.readVarInt()];
        this.data = buffer.readUtf();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(type.ordinal());
        buffer.writeUtf(data);
    }
}