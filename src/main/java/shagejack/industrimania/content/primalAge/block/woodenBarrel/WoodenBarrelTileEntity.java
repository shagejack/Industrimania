package shagejack.industrimania.content.primalAge.block.woodenBarrel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shagejack.industrimania.foundation.fluid.FluidTankBase;
import shagejack.industrimania.foundation.fluid.ITankTileEntity;
import shagejack.industrimania.foundation.network.packet.FluidUpdatePacket;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.utility.DropUtils;
import shagejack.industrimania.registers.AllTileEntities;
import shagejack.industrimania.registers.block.AllBlocks;

import java.util.List;
import java.util.Objects;

public class WoodenBarrelTileEntity extends SmartTileEntity implements ITankTileEntity, FluidUpdatePacket.IFluidPacketReceiver {

    private static final int CAPACITY = 4000;
    private static final int BURN_TEMPERATURE = 600;

    public FluidTankBase<WoodenBarrelTileEntity> tank;
    LazyOptional<IFluidHandler> tankHandlerLazyOptional;

    private boolean overheat;


    public WoodenBarrelTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.wooden_barrel.get(), pos, state);
        this.tank = new FluidTankBase<>(this, CAPACITY);
        this.tankHandlerLazyOptional = LazyOptional.of(() -> tank);
        this.overheat = false;
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
            if (tank.getFluid().getFluid().getAttributes().getTemperature(tank.getFluid()) >= BURN_TEMPERATURE) {
                burn();
            }
        }

    }

    public void empty() {
        this.tank = new FluidTankBase<>(this, CAPACITY);
    }

    private void burn() {
        Objects.requireNonNull(getLevel()).setBlock(getBlockPos(), Blocks.FIRE.defaultBlockState(), 3);
        overheat = true;
    }

    public ItemStack getDrop(boolean open) {
        ItemStack stack = new ItemStack(AllBlocks.mechanic_wooden_barrel.item().get());
        stack.getOrCreateTag().putBoolean("Open", open);

        if (open)
            return stack;

        tank.writeToNBT(stack.getOrCreateTag());

        return stack;
    }

    public void initByItem(ItemStack stack) {
        this.tank.readFromNBT(stack.getOrCreateTag());
    }

    public void destroy(boolean open) {
        DropUtils.dropItemStack(getLevel(), getBlockPos(), getDrop(open));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getBlockState().getValue(WoodenBarrelBlock.OPEN)) {
            return tankHandlerLazyOptional.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        tankHandlerLazyOptional.invalidate();
    }

    public boolean isOverheat() {
        return this.overheat;
    }

    @Override
    public int getCapacity() {
        return this.tank.getCapacity();
    }

    @Override
    public void updateFluidTo(FluidStack fluid) {
        this.tank.setFluid(fluid);
    }
}
