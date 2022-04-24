package shagejack.industrimania.registers;

import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.steamAge.block.boiler.BoilerMenu;

public class AllMenus {

    public static final MenuType<BoilerMenu> BOILER = register("boiler", BoilerMenu::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> menuSupplier) {
        return Registry.register(Registry.MENU, Industrimania.asResource(name), new MenuType<>(menuSupplier));
    }
}
