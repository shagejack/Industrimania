package shagejack.industrimania.registries;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.steamAge.block.boiler.BoilerMenu;

public class AllMenus {

    public static final RegistryObject<MenuType<BoilerMenu>> BOILER = register("boiler", BoilerMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return RegisterHandle.MENU_TYPE_REGISTER.register(name, () -> IForgeMenuType.create(factory));
    }
}
