/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package shagejack.shagecraft.network.packet.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.shagecraft.init.ShagecraftCapabilities;
import shagejack.shagecraft.network.packet.TileEntityUpdatePacket;

public class PacketSteamUpdate extends TileEntityUpdatePacket {
    private double steam_volume = 0;
    private double steam_temp = 0;
    private double steam_pressure = 0;
    private double steam_humidity = 0;
    private int steam_state = 0;

    public PacketSteamUpdate() {
    }

    public PacketSteamUpdate(TileEntity tileentity) {
        super(tileentity.getPos());
        steam_volume = tileentity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getSteamVolume();
        steam_temp = tileentity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getSteamTemp();
        steam_pressure = tileentity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getSteamPressure();
        steam_humidity = tileentity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getSteamHumidity();
        steam_state = tileentity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).getSteamState();
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSteamUpdate> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSteamUpdate message, MessageContext ctx) {
            if (player != null && player.world != null) {
                TileEntity tileEntity = player.world.getTileEntity(message.pos);

                if (tileEntity != null) {
                    tileEntity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).setSteamVolume(message.steam_volume);
                    tileEntity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).setSteamTemp(message.steam_temp);
                    tileEntity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).setSteamPressure(message.steam_pressure);
                    tileEntity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).setSteamHumidity(message.steam_humidity);
                    tileEntity.getCapability(ShagecraftCapabilities.STEAM_HANDLER, null).setSteamState(message.steam_state);
                }
            }
        }
    }
}
