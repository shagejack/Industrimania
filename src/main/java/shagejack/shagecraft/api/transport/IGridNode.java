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

package shagejack.shagecraft.api.transport;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGridNode<T extends IGridNetwork> {
    T getNetwork();

    void setNetwork(T network);

    BlockPos getNodePos();

    World getNodeWorld();

    boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction);

    /**
     * Can the Matter Connection connect form a given side.
     *
     * @param blockState
     * @param side       the side of the tested connection.
     * @return can the connection be made trough the given side.
     */
    boolean canConnectFromSide(IBlockState blockState, EnumFacing side);
}
