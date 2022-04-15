package shagejack.industrimania.content.primalAge.block.woodenFaucet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import shagejack.industrimania.foundation.fluid.FluidTankBase;
import shagejack.industrimania.foundation.network.AllPackets;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.utility.WeakConsumerWrapper;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.List;
import java.util.Objects;

import static shagejack.industrimania.content.primalAge.block.woodenFaucet.WoodenFaucetBlock.FACING;

public class WoodenFaucetTileEntity extends SmartTileEntity {

    public FluidTankBase<WoodenFaucetTileEntity> tank;

    private LazyOptional<IFluidHandler> inputHandler;
    private LazyOptional<IFluidHandler> outputHandler;
    private final NonNullConsumer<LazyOptional<IFluidHandler>> inputListener = new WeakConsumerWrapper<>(this, (self, handler) -> self.inputHandler = null);
    private final NonNullConsumer<LazyOptional<IFluidHandler>> outputListener = new WeakConsumerWrapper<>(this, (self, handler) -> self.outputHandler = null);

    public boolean isPouring;
    public boolean isPouringClient;


    public WoodenFaucetTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.wooden_faucet.get(), pos, state);
        this.tank = new FluidTankBase<>(this, 200);
        this.isPouring = false;
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        if (!tank.isEmpty())
            tag.put("fluid", this.tank.writeToNBT(new CompoundTag()));
        tag.putBoolean("isPouring", isPouring);
        tag.putBoolean("isPouringClient", isPouringClient);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);

        if (tag.contains("fluid", Tag.TAG_COMPOUND)) {
            this.tank.readFromNBT(tag.getCompound("fluid"));
        } else {
            this.tank = new FluidTankBase<>(this, 200);
        }

        isPouring = tag.getBoolean("isPouring");
        isPouringClient = tag.getBoolean("isPouringClient");
    }

    @Override
    public void tick() {
        super.tick();

        if (tank.isEmpty()) {
            transfer();
        } else {

            if (tank.getFluid().getFluid().getAttributes().getTemperature(tank.getFluid()) >= 600) {
                burn();
                return;
            }

            if (!isPouring) {
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

        if (inputOptional.isPresent()) {
            IFluidHandler input = inputOptional.orElse(EmptyFluidHandler.INSTANCE);
            FluidStack drain = input.drain(100, FluidAction.SIMULATE);

            if (!drain.isEmpty()) {
                int filled = this.tank.fill(drain, FluidAction.SIMULATE);

                if (filled > 0) {
                    this.tank.fill(input.drain(filled, FluidAction.EXECUTE), FluidAction.EXECUTE);
                    this.isPouring = true;

                    if (!isPouringClient) {
                        syncToClient(true);
                    }

                    pour();
                }
            }
        }
    }

    private void pour() {
        if (tank.isEmpty())
            return;

        LazyOptional<IFluidHandler> outputOptional = getOutputHandler();
        if (outputOptional.isPresent()) {
            FluidStack fillStack = tank.getFluid().copy();
            fillStack.setAmount(Math.min(tank.getFluidAmount(), 10));

            // can we fill?
            IFluidHandler output = outputOptional.orElse(EmptyFluidHandler.INSTANCE);
            int filled = output.fill(fillStack, IFluidHandler.FluidAction.SIMULATE);
            if (filled > 0) {
                // transfer it
                this.tank.drain(filled, FluidAction.EXECUTE);
                fillStack.setAmount(filled);
                output.fill(fillStack, IFluidHandler.FluidAction.EXECUTE);
            }
        } else {
            // if output got lost, all liquid will be lost.
            reset();
        }

    }

    private void reset() {
        tank.empty();
        isPouring = false;
        if (isPouringClient) {
            syncToClient(false);
        }
    }

    private void burn() {
        Objects.requireNonNull(getLevel()).setBlock(getBlockPos(), Blocks.FIRE.defaultBlockState(), 3);
    }

    public void handlePacket(boolean isPouring) {
        this.isPouringClient = isPouring;
    }

    public void syncToClient(boolean isPouring) {
        AllPackets.sendToNear(Objects.requireNonNull(getLevel()), getBlockPos(), new WoodenFaucetPacket(getBlockPos(), isPouring));
    }
}
