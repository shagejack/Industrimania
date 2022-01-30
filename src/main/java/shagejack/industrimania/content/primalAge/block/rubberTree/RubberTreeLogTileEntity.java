package shagejack.industrimania.content.primalAge.block.rubberTree;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.AllFluids;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.List;

public class RubberTreeLogTileEntity extends SmartTileEntity {

    RubberTreeTank tank = new RubberTreeTank(1000);

    public RubberTreeLogTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.rubber_tree_log.get(), pos, state);
    }

    public RubberTreeLogTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        tank.fill(new FluidStack(AllFluids.rawRubber.still().get(), 1), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tank.writeToNBT(tag);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        tank.readFromNBT(tag);
    }
}
