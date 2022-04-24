package shagejack.industrimania.content.steamAge.block.boiler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shagejack.industrimania.content.steamAge.block.base.SteamProducerTileEntityBase;
import shagejack.industrimania.content.steamAge.steam.*;
import shagejack.industrimania.foundation.fluid.FluidTankBase;
import shagejack.industrimania.foundation.item.SmartInventory;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.AllTileEntities;

import javax.annotation.Nonnull;
import java.util.List;

public class BoilerTileEntity extends SteamProducerTileEntityBase implements Container, MenuProvider {

    public static final double C_V = 400;
    public static final double C_H = 0.84;
    public static final double C_P = 100;
    public static final double C_E = 0.4;
    private double temperature;

    FluidTankBase<BoilerTileEntity> tank;
    SmartInventory inventory;
    BoilerSteamStorage steamStorage;

    protected LazyOptional<IItemHandlerModifiable> itemCapability;
    protected LazyOptional<IFluidHandler> fluidCapability;
    protected LazyOptional<ISteamStorage> steamCapability;

    public BoilerTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.boiler.get(), pos, state);

        inventory = new SmartInventory(5, this).withMaxStackSize(64);
        tank = new FluidTankBase<>(this, 4000);
        steamStorage = new BoilerSteamStorage(this, 2000, 20, 20000, 100);

        itemCapability = LazyOptional.of(() -> inventory);
        fluidCapability = LazyOptional.of(() -> tank);
        steamCapability = LazyOptional.of(() -> steamStorage);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        super.tick();
        steamStorage.manageSteamStored();
        manageSteamProduce();
        steamStorage.manageTransfer();
    }

    @Override
    public void manageSteamProduce() {
        if (level == null)
            return;

        double heat = 0;
        SteamStack produce = SteamStack.EMPTY;

        if (temperature > 393.15) {

            if (temperature > steamStorage.getSteamTemperature()) {
                heat = temperature / C_P;
                temperature -= heat;
            }

            int amount = (int) (temperature / C_V);
            double temp = 373.15 + Math.pow(temperature - 373.15, C_H);

            if(!tank.isEmpty() && tank.getFluid().getFluid().is(FluidTags.WATER) && tank.getFluidAmount() >= amount) {
                produce = new SteamStack((double) amount / 1000, temp, SteamState.SATURATED);
                temperature -= 0.01;
                tank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
            } else if (temperature / 10000 > Math.random()) {
                level.explode(null, getBlockPos().getX() + 0.5D, getBlockPos().getY() + 0.5D, getBlockPos().getZ() + 0.5D, 5.0F, Explosion.BlockInteraction.BREAK);
                level.removeBlock(getBlockPos(), false);
            }

        } else if (temperature > 298.15) {
            temperature -= 0.2;
        }

        this.steamStorage.setSteam(SteamStack.mergeSteamStack(steamStorage.getSteam(), produce));
        this.steamStorage.setSteam(SteamStack.heatUp(steamStorage.getSteam(), heat, C_E));
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tank.writeToNBT(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.put("SteamStorage", steamStorage.serializeNBT());
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        tank.readFromNBT(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        steamStorage.deserializeNBT(tag.getCompound("SteamStorage"));
        super.read(tag, clientPacket);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("industrimania.mechanic_boiler_gui_name");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        return new BoilerMenu(containerId, inv, this);
    }

    @Override
    public int getContainerSize() {
        return BoilerMenu.CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return inventory.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return inventory.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        inventory.setItem(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    @Override
    public void clearContent() {
        inventory.clearContent();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCapability.cast();

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();

        if (cap == CapabilitySteamHandler.STEAM_HANDLER_CAPABILITY)
            return steamCapability.cast();

        return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        this.itemCapability.invalidate();
        super.invalidateCaps();
    }
}
