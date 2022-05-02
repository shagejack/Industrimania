package shagejack.industrimania.content.steamAge.block.boiler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.foundation.item.SmartInventory;
import shagejack.industrimania.registers.AllMenus;

import java.util.Objects;

public class BoilerMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 5;
    public static final int DATA_COUNT = 1;

    public final BoilerTileEntity tileEntity;
    public final SmartInventory inventory;
    private final ContainerData boilerData;
    private final ContainerLevelAccess canInteractWithCallable;

    public BoilerMenu(int windowId, Inventory playerInventory, BoilerTileEntity boiler, ContainerData boilerData) {
        super(AllMenus.BOILER.get(), windowId);
        this.tileEntity = boiler;
        this.inventory = tileEntity.inventory;
        this.boilerData = boilerData;
        this.canInteractWithCallable = ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos());

        checkContainerSize(boiler, CONTAINER_SIZE);
        boiler.startOpen(playerInventory.player);

        for(int j = 0; j < 5; ++j) {
            this.addSlot(new SlotItemHandler(inventory, j, 44 + j * 18, 20));
        }
    }

    private static BoilerTileEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "player inventory should not be null");
        Objects.requireNonNull(data, "data should not be null");

        if (playerInventory.player.level.getBlockEntity(data.readBlockPos()) instanceof BoilerTileEntity te)
            return te;

        throw new IllegalStateException("Invalid TileEntity!");
    }

    public BoilerMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(DATA_COUNT));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return tileEntity.stillValid(player);
    }
}
