package shagejack.industrimania.registries.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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
import shagejack.industrimania.registries.AllTabs;
import shagejack.industrimania.registries.RegisterHandle;
import shagejack.industrimania.registries.tags.TagBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemBuilder implements ModelBuilder{

    String name;
    private CreativeModeTab tab;
    private boolean hasTab = true;
    private Item.Properties properties;
    RegistryObject<Item> registryObject;
    private Supplier<Supplier<BlockEntityWithoutLevelRenderer>> render;
    private ItemColor itemColor;
    private final java.util.List<String> tags = new ArrayList<>();

    private int durability;
    private int maxStackSize;
    private FoodProperties foodProperties;
    private final Map<String, Object> extraParam = new HashMap();

    public ItemBuilder() {
        this.defaultProperties();
    }

    @Override
    public ItemBuilder asBase() {
        return this;
    }

    public ItemBuilder set(Function<Item.Properties, Item.Properties> function) {
        if (properties == null) {
            this.properties = new Item.Properties();
        }
        properties = function.apply(this.properties);

        return this;
    }

    public ItemBuilder simpleModel() {
        return this.simpleModel(this.name);
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

    public ItemBuilder food(FoodProperties foodProperties) {
        this.foodProperties = foodProperties;
        return this;
    }

    public ItemBuilder defaultProperties() {
        this.properties = new Item.Properties();
        return this;
    }

    public ItemBuilder properties(Function<Item.Properties, Item.Properties> function) {
        if (properties == null) {
            this.properties = new Item.Properties();
        }
        properties = function.apply(this.properties);
        return this;
    }

    public RegistryObject<Item> build() {
        if (render != null) {
            return build(() -> new Item(properties) {
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
        } else {
            return build(() -> new Item(properties));
        }
    }

    /**
     * Don't support set BEWLR. You are supposed to use a custom class overriding Item#initalizeClient.
     */
    public <T extends Item> RegistryObject<Item> build(Function<Item.Properties, T> factory) {
        return build(() -> factory.apply(properties));
    }

    // TODO: remove this shitty implementation...
    /**
     * Don't support set BEWLR. You are supposed to use a custom class overriding Item#initalizeClient.
     */
    public <T extends Item> RegistryObject<Item> build(BiFunction<Item.Properties, Map<String, Object>, T> factory) {
        return build(() -> factory.apply(properties, extraParam));
    }

    /**
     * Don't support set BEWLR. You are supposed to use a custom class overriding Item#initalizeClient.
     */
    public RegistryObject<Item> build(Supplier<Item> itemSupplier) {
        checkProperty();

        registryObject = RegisterHandle.ITEM_REGISTER.register(name, itemSupplier);

        if (!tags.isEmpty()) {
            TagBuilder.itemTag(registryObject, tags);
            Industrimania.LOGGER.debug("Register Item:{} with Tags:{}", name, tags.toString());
        } else {
            Industrimania.LOGGER.debug("Register Item {}", name);
        }

        if (itemColor != null)
            ItemColorHandler.register(registryObject, itemColor);

        return registryObject;
    }

    public RegistryObject<Item> build(RegistryObject<Block> block) {
        checkProperty();
        if (render != null) {
            registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), properties) {
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
        } else {
            registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), properties));
        }

        if (!tags.isEmpty()) {
            TagBuilder.itemTag(registryObject, tags);
            Industrimania.LOGGER.debug("Register Item:{} with Tags:{}", name, tags.toString());
        } else {
            Industrimania.LOGGER.debug("Register Item {}", name);
        }

        if (itemColor != null)
            ItemColorHandler.register(registryObject, itemColor);

        return registryObject;
    }

    public <T extends BlockItem> RegistryObject<Item> build(RegistryObject<Block> block, BiFunction<Block, Item.Properties, T> blockItemFactory) {
        return build(() -> blockItemFactory.apply(block.get(), properties));
    }

    private void checkProperty() {
        Objects.requireNonNull(name);
        if (properties == null) {
            this.properties = new Item.Properties();
        }

        if (durability != 0) {
            this.properties.durability(durability);
        }

        if (maxStackSize != 0) {
            this.properties.stacksTo(maxStackSize);
        }

        if (hasTab) {
            this.properties.tab(Objects.requireNonNullElse(tab, AllTabs.tabMain));
        }

        if (foodProperties != null) {
            this.properties.food(foodProperties);
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

    public ItemBuilder tags(TagKey<?>... tags) {
        this.tags.addAll(Arrays.stream(tags).map(tag -> tag.location().toString()).toList());
        return this;
    }

    public ItemBuilder tags(ResourceLocation... tags) {
        this.tags.addAll(Arrays.stream(tags).map(ResourceLocation::toString).toList());
        return this;
    }

    public ItemBuilder tags(String... tags) {
        this.tags.addAll(List.of(tags));
        return this;
    }

    public ItemBuilder maxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }
}
