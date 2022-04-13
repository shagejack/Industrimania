package shagejack.industrimania.content.primalAge.block.woodenBarrel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shagejack.industrimania.content.primalAge.block.woodenFaucet.WoodenFaucetFluidPacket;
import shagejack.industrimania.foundation.network.AllPackets;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.utility.DropUtils;
import shagejack.industrimania.foundation.utility.WeakConsumerWrapper;
import shagejack.industrimania.registers.AllTileEntities;
import shagejack.industrimania.registers.block.AllBlocks;

import java.util.List;
import java.util.Objects;

import static shagejack.industrimania.content.primalAge.block.woodenFaucet.WoodenFaucetBlock.FACING;

public class WoodenBarrelTileEntity extends SmartTileEntity {

    private static final int CAPACITY = 4000;

    public FluidTank tank;
    LazyOptional<IFluidHandler> tankHandlerLazyOptional;


    public WoodenBarrelTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.wooden_faucet.get(), pos, state);
        this.tankHandlerLazyOptional = LazyOptional.of(() -> tank);
    }

    public WoodenBarrelTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tank = new FluidTank(CAPACITY);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        this.tank.writeToNBT(tag);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.tank.readFromNBT(tag);
    }

    @Override
    public void tick() {
        super.tick();

        if (!tank.isEmpty()) {
            if (tank.getFluid().getFluid().getAttributes().getTemperature(tank.getFluid()) >= 300) {
                burn();
            }
        }

    }

    public void empty() {
        this.tank = new FluidTank(CAPACITY);
    }

    private void burn() {
        Objects.requireNonNull(getLevel()).setBlock(getBlockPos(), Blocks.FIRE.defaultBlockState(), 3);
    }

    public ItemStack getDrop(boolean sealed) {
        ItemStack stack = new ItemStack(AllBlocks.mechanic_wooden_barrel.item().get());

        if (!sealed)
            return stack;

        tank.writeToNBT(stack.getOrCreateTag());

        return stack;
    }

    public void initByItem(ItemStack stack) {
        this.tank.readFromNBT(stack.getOrCreateTag());
    }

    public void destroy(boolean sealed) {
        DropUtils.dropItemStack(getLevel(), getBlockPos(), getDrop(sealed));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (side == Direction.UP || side == Direction.DOWN) {
                return tankHandlerLazyOptional.cast();
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        tankHandlerLazyOptional.invalidate();
    }

}
