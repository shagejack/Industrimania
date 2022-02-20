package shagejack.industrimania.content.primalAge.block.nature.rubberTree;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.AllFluids;
import shagejack.industrimania.registers.AllTileEntities;
import shagejack.industrimania.registers.block.AllBlocks;

import java.util.List;

public class RubberTreeLogTileEntity extends SmartTileEntity {

    RubberTreeTank tank = new RubberTreeTank(1000);

    public RubberTreeLogTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.rubber_tree_log.get(), pos, state);
        setLazyTickRate(20);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        super.tick();
        if (level.getBlockState(getBlockPos().below()).is(AllBlocks.nature_rubber_tree_log.block().get())) {
            RubberTreeLogTileEntity te = (RubberTreeLogTileEntity) level.getBlockEntity(getBlockPos().below());
            if (te != null) {
                FluidStack tankFluid = this.tank.getFluid().copy();
                if (tankFluid.getFluid().isSame(AllFluids.rawRubber.still().get())) {
                    if (this.tank.getCapacity() > 0 && te.tank.fill(tankFluid, IFluidHandler.FluidAction.SIMULATE) != 0) {
                        this.tank.drain(te.tank.fill(tankFluid, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!getBlockState().isAir() && hasLevel() && level.getRandom().nextDouble() < 0.1)
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
