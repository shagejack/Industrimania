package shagejack.industrimania.registers.block;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.client.handler.BlockColorHandler;
import shagejack.industrimania.registers.AllTags;
import shagejack.industrimania.registers.record.ItemBlock;
import shagejack.industrimania.registers.RegisterHandle;
import shagejack.industrimania.registers.block.grouped.AllRocks;
import shagejack.industrimania.registers.item.ItemBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class BlockBuilder implements ModelBuilder, StateBuilder, AllGroupedBlocks {

    protected String name;
    protected RegistryObject<Block> block;
    protected BlockBehaviour.Properties property;
    protected final List<String> tags = new ArrayList<>();
    protected final List<String> modTags = new ArrayList<>();
    protected float hardness;
    protected float explosionResistance;
    protected Material material;
    protected SoundType soundType;
    protected boolean isRandomlyTicking;
    protected boolean dynamicShape;
    protected boolean noCollission;
    protected boolean noDrops;
    protected boolean isAir;
    protected boolean requiresCorrectToolForDrops = false;
    protected ToIntFunction<BlockState> lightLevelFun;
    protected BlockColor blockColor;
    protected BlockBehaviour.StatePredicate isRedstoneConductor;
    protected BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn;
    protected BlockBehaviour.StatePredicate isSuffocating;
    protected BlockBehaviour.StatePredicate isViewBlocking;
    protected final Map<String, Object> extraParam = new HashMap();

    public static final List<Supplier<Runnable>> setupRenderLayerTasks = new ArrayList<>();

    @Override
    public BlockBuilder asBase() {
        return this;
    }

    public void checkProperty() {
        Objects.requireNonNull(name);
        if (property == null) {
            if (material == null) {
                this.material = Material.STONE;
            }

            if (soundType == null) {
                this.soundType = SoundType.STONE;
            }

            property = BlockBehaviour.Properties.of(material).sound(soundType).strength(1.5f, 6.0f);
        }
        if (hardness != 0 && explosionResistance != 0) {
            property.strength(hardness, explosionResistance);
        }

        if (requiresCorrectToolForDrops) {
            property.requiresCorrectToolForDrops();
        }

        if (isRandomlyTicking) {
            property.randomTicks();
        }

        if (dynamicShape) {
            property.dynamicShape();
        }

        if (noCollission) {
            property.noCollission();
        }

        if (isValidSpawn != null)
            property.isValidSpawn(isValidSpawn);

        if (isViewBlocking != null)
            property.isViewBlocking(isViewBlocking);

        if (isSuffocating != null)
            property.isSuffocating(isSuffocating);

        if (isRedstoneConductor != null)
            property.isRedstoneConductor(isRedstoneConductor);

        if (isAir)
            property.air();

        if (noDrops)
            property.noDrops();

        if (lightLevelFun != null)
            property.lightLevel(lightLevelFun);

    }

    public RegistryObject<Block> getBlock() {
        return block;
    }

    public BlockBuilder buildBlock(Supplier<Block> blockSupplier) {
        checkProperty();
        block = RegisterHandle.BLOCK_REGISTER.register(name, blockSupplier);
        Industrimania.LOGGER.debug("register Block:{}", name);
        if (!tags.isEmpty()) {
            AllBlocks.BLOCK_TAGS.put(block, tags);
            Industrimania.LOGGER.debug("for block:{} add tags:{}", name, tags.toString());
        }

        if (blockColor != null) {
            BlockColorHandler.register(block, blockColor);
        }

        return this;
    }

    public <T extends Block> BlockBuilder buildBlock(Function<BlockBehaviour.Properties, T> factory) {
        return buildBlock(() -> factory.apply(property));
    }

    public <T extends Block> BlockBuilder buildBlock(BiFunction<BlockBehaviour.Properties, Map<String, Object>, T> factory) {
        return buildBlock(() -> factory.apply(property, extraParam));
    }

    public BlockBuilder buildBlock() {
        return buildBlock(() -> new Block(property));
    }

    public BlockBuilder asRock(float hardness, float explosionResistance, String rockTag) {
        checkProperty();
        AllRocks.ROCKS_NAME.add(name);
        AllRocks.ROCKS_HARDNESS.put(name, hardness);
        AllRocks.ROCKS_EXPLOSION_RESISTANCE.put(name, explosionResistance);
        this.tags(AllTags.ToolType.pickaxe, AllTags.IndustrimaniaTags.rock, rockTag);
        return this;
    }



    RegistryObject<Block> checkAlreadyBuild() {
        return Objects.requireNonNull(block, "can't build ItemBlock before block is built");
    }

    public ItemBlock buildItem(String itemName, Function<ItemBuilder, ItemBuilder> factory) {
        final var block = checkAlreadyBuild();
        final ItemBuilder itemBuilder = new ItemBuilder().name(itemName).blockModel("block/" + this.name);
        factory.apply(itemBuilder);
        Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, itemName);
        ItemBlock itemBlock = new ItemBlock(itemBuilder.build(block), block);
        checkTags(itemBlock, tags);
        return itemBlock;
    }

    public <T extends BlockItem> ItemBlock buildItem(String itemName, Function<ItemBuilder, ItemBuilder> factory, BiFunction<Block, Item.Properties, T> itemBlockFactory) {
        final var block = checkAlreadyBuild();
        final ItemBuilder itemBuilder = new ItemBuilder().name(itemName).blockModel("block/" + this.name);
        factory.apply(itemBuilder);
        Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, itemName);
        ItemBlock itemBlock = new ItemBlock(itemBuilder.build(block, itemBlockFactory), block);
        checkTags(itemBlock, tags);
        return itemBlock;
    }

    public ItemBlock buildItemWithModel(String itemName, Function<ItemBuilder, ItemBuilder> factory) {
        return buildItemWithModel(itemName, factory, false);
    }

    public ItemBlock buildItemWithModel(String itemName, Function<ItemBuilder, ItemBuilder> factory, boolean useItemNameForModelOnly) {
        final var block = checkAlreadyBuild();
        final ItemBuilder itemBuilder = new ItemBuilder().name(useItemNameForModelOnly ? name : itemName).specificModel("item/model/" + itemName);
        factory.apply(itemBuilder);
        Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, useItemNameForModelOnly ? name : itemName);
        ItemBlock itemBlock = new ItemBlock(itemBuilder.build(block), block);
        checkTags(itemBlock, tags);
        return itemBlock;
    }

    public <T extends BlockItem> ItemBlock buildItemWithModel(String itemName, Function<ItemBuilder, ItemBuilder> factory, boolean useItemNameForModelOnly, BiFunction<Block, Item.Properties, T> itemBlockFactory) {
        final var block = checkAlreadyBuild();
        final ItemBuilder itemBuilder = new ItemBuilder().name(useItemNameForModelOnly ? name : itemName).simpleModel(itemName);
        factory.apply(itemBuilder);
        Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, useItemNameForModelOnly ? name : itemName);
        ItemBlock itemBlock = new ItemBlock(itemBuilder.build(block, itemBlockFactory), block);
        checkTags(itemBlock, tags);
        return itemBlock;
    }

    public <T extends BlockItem> ItemBlock buildItemWithModel(String itemName, Function<ItemBuilder, ItemBuilder> factory, BiFunction<Block, Item.Properties, T> itemBlockFactory) {
        return buildItemWithModel(itemName, factory, false, itemBlockFactory);
    }

    public ItemBlock buildItem(Function<ItemBuilder, ItemBuilder> factory) {
        return buildItem(name, factory);
    }

    public <T extends BlockItem> ItemBlock buildItem(Function<ItemBuilder, ItemBuilder> factory, BiFunction<Block, Item.Properties, T> itemBlockFactory) {
        return buildItem(name, factory, itemBlockFactory);
    }

    public ItemBlock buildItem() {
        return buildItem(name, (itemBuilder) -> itemBuilder);
    }

    /**
     * build item with specific name and model
     * @param itemName
     * @return
     */
    public ItemBlock buildItem(String itemName) {
        return buildItemWithModel(itemName, (itemBuilder) -> itemBuilder);
    }

    public BlockBuilder renderLayer(Supplier<Supplier<RenderType>> renderType) {
        setupRenderLayerTasks.add(() -> () -> ItemBlockRenderTypes.setRenderLayer(block.get(), renderType.get().get()));
        return this;
    }

    public void checkTags(ItemBlock itemBlock, List<String> tags) {
        if (tags.contains(AllTags.IndustrimaniaTags.igneousStones)) AllRocks.igneousStones.add(itemBlock);
        if (tags.contains(AllTags.IndustrimaniaTags.metamorphicStones)) AllRocks.metamorphicStones.add(itemBlock);
        if (tags.contains(AllTags.IndustrimaniaTags.sedimentaryStones)) AllRocks.sedimentaryStones.add(itemBlock);
    }

    public BlockBuilder tags(TagKey<?>... tags) {
        this.tags.clear();
        this.tags.addAll(Arrays.stream(tags).map(tag -> tag.location().toString()).toList());
        return this;
    }

    public BlockBuilder tags(ResourceLocation... tags) {
        this.tags.clear();
        this.tags.addAll(Arrays.stream(tags).map(ResourceLocation::toString).toList());
        return this;
    }

    public BlockBuilder tags(String... tags) {
        this.tags.clear();
        this.tags.addAll(Arrays.stream(tags).toList());
        return this;
    }

    public BlockBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public BlockBuilder sound(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public BlockBuilder strength(float hardness, float explosionResistance) {
        this.hardness = hardness;
        this.explosionResistance = explosionResistance;
        return this;
    }

    public BlockBuilder strength(float strength) {
        this.strength(strength, strength);
        return this;
    }

    public BlockBuilder requiresCorrectToolForDrops() {
        this.requiresCorrectToolForDrops = true;
        return this;
    }

    public BlockBuilder isAir() {
        this.isAir = true;
        return this;
    }

    public BlockBuilder noCollission() {
        this.noCollission = true;
        return this;
    }

    public BlockBuilder randomTicks() {
        this.isRandomlyTicking = true;
        return this;
    }

    public BlockBuilder dynamicShape() {
        this.dynamicShape = true;
        return this;
    }

    public BlockBuilder addExtraParam(String name, Object param) {
        this.extraParam.put(name, param);
        return this;
    }

    public BlockBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BlockBuilder isViewBlocking(BlockBehaviour.StatePredicate statePredicate) {
        this.isViewBlocking = statePredicate;
        return this;
    }

    public BlockBuilder isSuffocating(BlockBehaviour.StatePredicate statePredicate) {
        this.isSuffocating = statePredicate;
        return this;
    }

    public BlockBuilder isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> stateArgumentPredicate) {
        this.isValidSpawn = stateArgumentPredicate;
        return this;
    }

    public BlockBuilder isRedstoneConductor(BlockBehaviour.StatePredicate statePredicate) {
        this.isRedstoneConductor = statePredicate;
        return this;
    }

    public BlockBuilder setRGBOverlay(Color color) {
        this.blockColor = (state, blockAndTintGetter, pos, p_92649_) -> color.getRGB();
        return this;
    }

    public BlockBuilder setBlockColor(BlockColor blockColor) {
        this.blockColor = blockColor;
        return this;
    }

    public BlockBuilder noDrops() {
        this.noDrops = true;
        return this;
    }

    public BlockBuilder lightLevel(ToIntFunction<BlockState> lightLevelFun) {
        this.lightLevelFun = lightLevelFun;
        return this;
    }

}
