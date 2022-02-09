package shagejack.industrimania.registers.item;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.client.handler.ItemColorHandler;
import shagejack.industrimania.registers.AllTabs;
import shagejack.industrimania.registers.AllTags;
import shagejack.industrimania.registers.RegisterHandle;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.block.BlockBuilder;
import shagejack.industrimania.registers.block.grouped.AllRocks;
import shagejack.industrimania.registers.record.ItemBlock;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ItemBuilder implements ModelBuilder{
     String name;
    private CreativeModeTab tab;
    private boolean hasTab = true;
    private Item.Properties property;
    RegistryObject<Item> registryObject;
    private Supplier<Supplier<BlockEntityWithoutLevelRenderer>> render;
    private ItemColor itemColor;
    private final java.util.List<String> tags = new ArrayList<>();
    private final List<String> modTags = new ArrayList<>();

    private int durability;
    private FoodProperties foodProperties;
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
        this.hasTab = true;
        this.tab = tab;
        return this;
    }

    public ItemBuilder noTab() {
        this.hasTab = false;
        return this;
    }

    public ItemBuilder food(FoodProperties properties) {
        this.foodProperties = foodProperties;
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

        if (itemColor != null)
            ItemColorHandler.register(registryObject, itemColor);

        if (!tags.isEmpty()) {
            AllItems.ITEM_TAGS.put(registryObject, tags);
            Industrimania.LOGGER.debug("for item:{} add tags:{}", name, tags.toString());
        }

        if (itemColor != null)
            ItemColorHandler.register(registryObject, itemColor);

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

        if (!tags.isEmpty()) {
            AllItems.ITEM_TAGS.put(registryObject, tags);
            Industrimania.LOGGER.debug("for item:{} add tags:{}", name, tags.toString());
        }

        if (itemColor != null)
            ItemColorHandler.register(registryObject, itemColor);

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

        if(hasTab) {
            this.property.tab(Objects.requireNonNullElse(tab, AllTabs.tab));
        }

        if (foodProperties != null) {
            this.property.food(foodProperties);
        }
    }

    public ItemBuilder setRGBOverlay(Color color) {
        this.itemColor = (stack, p_92673_) -> color.getRGB();
        return this;
    }

    public ItemBuilder setItemColor(ItemColor itemColor) {
        this.itemColor = itemColor;
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder tags(String... tags) {
        this.tags.clear();
        this.tags.addAll(Arrays.stream(tags).toList());
        return this;
    }
}
