package shagejack.industrimania.content.primalAge.block.nature.rubberTree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.AllFluids;
import shagejack.industrimania.registers.AllTileEntities;
import shagejack.industrimania.registers.block.AllBlocks;

import java.util.List;

public class RubberTreeLogTileEntity extends SmartTileEntity {

    RubberTreeTank tank = new RubberTreeTank(1000);
    LazyOptional<IFluidHandler> tankHandlerLazyOptional;

    public RubberTreeLogTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.rubber_tree_log.get(), pos, state);
        this.tankHandlerLazyOptional = LazyOptional.of(() -> tank);
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

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return tankHandlerLazyOptional.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        tankHandlerLazyOptional.invalidate();
    }
}
