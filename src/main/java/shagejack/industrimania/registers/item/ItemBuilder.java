package shagejack.industrimania.registers.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.AllTabs;
import shagejack.industrimania.registers.RegisterHandle;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkItemModelExist;
import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkItemTextureExist;

public final class ItemBuilder implements ModelBuilder{
     String name;
    private CreativeModeTab tab;
    private Item.Properties property;
    RegistryObject<Item> registryObject;
    private Supplier<Supplier<BlockEntityWithoutLevelRenderer>> render;

    private int durability;
    private final Map<String, Object> extraParam = new HashMap();

    @Override
    public ItemBuilder asBase() {
        return this;
    }

    public ItemBuilder set(Function<Item.Properties, Item.Properties> function) {
        if (property == null) {
            this.property = new Item.Properties();
        }
        property = function.apply(this.property);
        return this;
    }

    /**
     * also call {@link ItemBuilder#builtInEntityModel}
     */
    public ItemBuilder setBlockEntityWithoutLevelRender(Supplier<Supplier<BlockEntityWithoutLevelRenderer>> render) {
        this.render = render;
        return this;
    }

    public ItemBuilder addExtraParam(String name, Object param) {
        this.extraParam.put(name, param);
        return this;
    }

    public ItemBuilder tab(CreativeModeTab tab) {
        this.tab = tab;
        return this;
    }

    public RegistryObject<Item> build() {
        return build(() -> new Item(property) {
            @Override
            public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
                if (render != null) {
                    consumer.accept(new IItemRenderProperties() {
                        @Override
                        public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                            return render.get().get();
                        }
                    });
                }
            }
        });
    }

    /**
     * don't support set BEWLR
     */
    public <T extends Item> RegistryObject<Item> build(Function<Item.Properties, T> factory) {
        return build(() -> factory.apply(property));
    }

    /**
     * don't support set BEWLR
     */
    public <T extends Item> RegistryObject<Item> build(BiFunction<Item.Properties, Map<String, Object>, T> factory) {
        return build(() -> factory.apply(property, extraParam));
    }

    /**
     * don't support set BEWLR
     */
    public RegistryObject<Item> build(Supplier<Item> itemSupplier) {
        checkProperty();
        registryObject = RegisterHandle.ITEM_REGISTER.register(name, itemSupplier);
        Industrimania.LOGGER.debug("register Item {}", name);
        return registryObject;
    }

    public RegistryObject<Item> build(RegistryObject<Block> block) {
        checkProperty();
        registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), property) {
            @Override
            public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
                if (render != null) {
                    consumer.accept(new IItemRenderProperties() {
                        @Override
                        public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                            return render.get().get();
                        }
                    });
                }
            }
        });
        return registryObject;
    }

    private void checkProperty() {
        Objects.requireNonNull(name);
        if (property == null) {
            this.property = new Item.Properties();
        }

        if (durability != 0) {
            this.property.durability(durability);
        }

        property.tab(Objects.requireNonNullElse(tab, AllTabs.tab));
    }

    public ItemBuilder durability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }
}
