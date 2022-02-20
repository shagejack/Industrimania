package shagejack.industrimania.registers.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import java.util.Objects;
import java.util.function.Consumer;

import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkItemModelExist;
import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkItemTextureExist;

public interface ModelBuilder {
    ItemBuilder asBase();

    default ItemBuilder model(Consumer<ItemModelProvider> task) {
        DataGenHandle.addItemModelTask(task);
        return (ItemBuilder) this;
    }

    default ItemBuilder simpleModel(String texture) {
        return model((provider) -> {
            var base = asBase();
            var name = base.name;
            var item = base.registryObject.get();
            if (checkItemTextureExist(provider, name, texture)) {
                provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                        .parent(DataGenHandle.itemHeldModel.get())
                        .texture("layer0", "item/" + name + "/" + texture);
            }
            Industrimania.LOGGER.debug("set itemHeldModel for Item:{}", name);
        });
    }

    default ItemBuilder blockModel(String model) {
        return model((provider) -> {
            var base = asBase();
            var item = base.registryObject.get();
            provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                    .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, model)));
            Industrimania.LOGGER.debug("set itemHeldModel for ItemBlock:{}", base.name);
        });
    }

    default ItemBuilder specificModel(String model) {
        return model((provider) -> {
            var base = asBase();
            var name = base.name;
            var item = base.registryObject.get();
            //if (checkItemModelExist(provider, name, model)) {
                provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                        .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, model)));
                Industrimania.LOGGER.debug("set itemHeldModel for Item:{}", name);
            //}
        });
    }

    default ItemBuilder builtInEntityModel(String model) {
        return model(provider -> {
            var item = asBase().registryObject.get();
            provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                    .parent(DataGenHandle.blockBuiltinEntity.get());
        });
    }

}
