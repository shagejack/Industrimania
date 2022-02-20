package shagejack.industrimania.content.primalAge.block.simpleCraftingTable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class SimpleCraftingTableMenu extends AbstractContainerMenu {

    protected SimpleCraftingTableMenu() {
        super(MenuType.CRAFTING, 0);
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}
