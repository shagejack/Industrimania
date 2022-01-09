package shagejack.shagecraft.content.metallurgy.block.smeltery.bronzeTube;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.shagecraft.content.metallurgy.item.smeltery.cluster.IronCluster;
import shagejack.shagecraft.foundation.tileEntity.SmartTileEntity;
import shagejack.shagecraft.foundation.tileEntity.TileEntityBehaviour;
import shagejack.shagecraft.registers.AllItems;
import shagejack.shagecraft.registers.AllTileEntities;

import java.util.List;

public class BronzeTubeTileEntity extends SmartTileEntity {

    //TODO: Smelter Initialization

    public BronzeTubeTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.bronze_tube.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}


}
