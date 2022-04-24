package shagejack.industrimania.content.steamAge.block.boiler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.registers.AllMenus;

public class BoilerMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 5;
    private final Container boiler;

    public BoilerMenu(int containerId, Inventory p_39641_) {
        this(containerId, p_39641_, new SimpleContainer(CONTAINER_SIZE));
    }

    public BoilerMenu(int containerId, Inventory p_39644_, Container container) {
        super(AllMenus.BOILER, containerId);
        this.boiler = container;
        checkContainerSize(container, CONTAINER_SIZE);
        container.startOpen(p_39644_.player);

        for(int j = 0; j < 5; ++j) {
            this.addSlot(new Slot(container, j, 44 + j * 18, 20));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return boiler.stillValid(player);
    }
}
