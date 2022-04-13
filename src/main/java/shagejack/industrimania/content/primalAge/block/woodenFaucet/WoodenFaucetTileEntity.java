package shagejack.industrimania.content.primalAge.block.woodenFaucet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import shagejack.industrimania.foundation.network.AllPackets;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.utility.WeakConsumerWrapper;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.List;
import java.util.Objects;

import static shagejack.industrimania.content.primalAge.block.woodenFaucet.WoodenFaucetBlock.FACING;

public class WoodenFaucetTileEntity extends SmartTileEntity {

    public FluidStack fluid;
    public FluidStack renderFluid;

    private LazyOptional<IFluidHandler> inputHandler;
    private LazyOptional<IFluidHandler> outputHandler;
    private final NonNullConsumer<LazyOptional<IFluidHandler>> inputListener = new WeakConsumerWrapper<>(this, (self, handler) -> self.inputHandler = null);
    private final NonNullConsumer<LazyOptional<IFluidHandler>> outputListener = new WeakConsumerWrapper<>(this, (self, handler) -> self.outputHandler = null);

    public boolean isPouring;
    public boolean isPouringClient;


    public WoodenFaucetTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.wooden_faucet.get(), pos, state);
    }

    public WoodenFaucetTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.fluid = FluidStack.EMPTY;
        this.isPouring = false;
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        if (!fluid.isEmpty())
            tag.put("fluid", this.fluid.writeToNBT(new CompoundTag()));
        if (!renderFluid.isEmpty())
            tag.put("renderFluid", this.renderFluid.writeToNBT(new CompoundTag()));
        tag.putBoolean("isPouring", isPouring);
        tag.putBoolean("isPouringClient", isPouringClient);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);

        if (tag.contains("fluid", Tag.TAG_COMPOUND)) {
            this.fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("fluid"));
        } else {
            this.fluid = FluidStack.EMPTY;
        }

        if (tag.contains("renderFluid", Tag.TAG_COMPOUND)) {
            this.renderFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("renderFluid"));
        } else {
            this.renderFluid = FluidStack.EMPTY;
        }

        isPouring = tag.getBoolean("isPouring");
        isPouringClient = tag.getBoolean("isPouringClient");
    }

    @Override
    public void tick() {
        super.tick();

        if (fluid.isEmpty()) {
            transfer();
        } else {

            if (fluid.getFluid().getAttributes().getTemperature(fluid) >= 300) {
                burn();
                return;
            }

            if (isPouring) {
                reset();
            } else {
                pour();
            }
        }
    }

    public Direction getFacing() {
        return this.getBlockState().getValue(FACING);
    }

    public BlockPos getInputPos() {
        return this.getBlockPos().relative(getFacing().getOpposite());
    }

    public BlockPos getOutputPos() {
        return this.getBlockPos().below();
    }

    private LazyOptional<IFluidHandler> findFluidHandler(Direction side) {
        assert level != null;
        BlockEntity te = level.getBlockEntity(worldPosition.relative(side));
        if (te != null) {
            LazyOptional<IFluidHandler> handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
            if (handler.isPresent()) {
                return handler;
            }
        }
        return LazyOptional.empty();
    }

    private LazyOptional<IFluidHandler> getInputHandler() {
        if (inputHandler == null) {
            inputHandler = findFluidHandler(getFacing().getOpposite());
            if (inputHandler.isPresent()) {
                inputHandler.addListener(inputListener);
            }
        }
        return inputHandler;
    }

    private LazyOptional<IFluidHandler> getOutputHandler() {
        if (outputHandler == null) {
            outputHandler = findFluidHandler(Direction.DOWN);
            if (outputHandler.isPresent()) {
                outputHandler.addListener(outputListener);
            }
        }
        return outputHandler;
    }

    public void neighborChanged(BlockPos neighbor) {
        // if the neighbor was below us, remove output
        if (worldPosition.equals(neighbor.above())) {
            outputHandler = null;
            // neighbor behind us
        } else if (worldPosition.equals(neighbor.relative(getBlockState().getValue(FACING)))) {
            inputHandler = null;
        }
    }

    private void transfer() {
        if (getLevel() == null)
            return;

        LazyOptional<IFluidHandler> inputOptional = getInputHandler();
        LazyOptional<IFluidHandler> outputOptional = getOutputHandler();

        if (inputOptional.isPresent()) {
            IFluidHandler input = inputOptional.orElse(EmptyFluidHandler.INSTANCE);
            FluidStack drain = input.drain(100, FluidAction.SIMULATE);

            if (!drain.isEmpty()) {
                IFluidHandler output = outputOptional.orElse(EmptyFluidHandler.INSTANCE);
                int filled = output.fill(drain, FluidAction.SIMULATE);

                if (filled > 0) {
                    this.fluid = input.drain(filled, FluidAction.EXECUTE);
                    this.isPouring = true;

                    if (!isPouringClient || !renderFluid.isFluidEqual(fluid)) {
                        syncToClient(this.fluid, true);
                    }

                    pour();
                }
            }
        }
    }

    private void pour() {
        if (fluid.isEmpty())
            return;

        LazyOptional<IFluidHandler> outputOptional = getOutputHandler();
        if (outputOptional.isPresent()) {
            FluidStack fillStack = fluid.copy();
            fillStack.setAmount(Math.min(fluid.getAmount(), 10));

            // can we fill?
            IFluidHandler output = outputOptional.orElse(EmptyFluidHandler.INSTANCE);
            int filled = output.fill(fillStack, IFluidHandler.FluidAction.SIMULATE);
            if (filled > 0) {
                // update client if they do not think we have fluid
                if (!renderFluid.isFluidEqual(fluid)) {
                    syncToClient(fluid, true);
                }

                // transfer it
                this.fluid.shrink(filled);
                fillStack.setAmount(filled);
                output.fill(fillStack, IFluidHandler.FluidAction.EXECUTE);
            }
        }
        else {
            // if output got lost, all liquid will be lost.
            reset();
        }

    }

    private void reset() {
        fluid = FluidStack.EMPTY;
        isPouring = false;
        if (isPouringClient || !renderFluid.isFluidEqual(fluid)) {
            syncToClient(FluidStack.EMPTY, false);
        }
    }

    private void burn() {
        Objects.requireNonNull(getLevel()).setBlock(getBlockPos(), Blocks.FIRE.defaultBlockState(), 3);
    }

    public void handlePacket(FluidStack renderFluid, boolean isPouring) {
        this.renderFluid = renderFluid;
        this.isPouringClient = isPouring;
    }

    public void syncToClient(FluidStack renderFluid, boolean isPouring) {
        AllPackets.sendToNear(Objects.requireNonNull(getLevel()), getBlockPos(), new WoodenFaucetFluidPacket(getBlockPos(), renderFluid, isPouring));
    }
}
