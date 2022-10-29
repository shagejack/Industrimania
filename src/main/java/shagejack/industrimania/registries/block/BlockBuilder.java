package shagejack.industrimania.registries.block;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.client.handler.BlockColorHandler;
import shagejack.industrimania.registries.tags.AllTags;
import shagejack.industrimania.registries.record.ItemBlock;
import shagejack.industrimania.registries.RegisterHandle;
import shagejack.industrimania.registries.block.grouped.AllRocks;
import shagejack.industrimania.registries.item.ItemBuilder;
import shagejack.industrimania.registries.tags.TagBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder implements ModelBuilder, StateBuilder, AllGroupedBlocks {

    protected String name;
    protected RegistryObject<Block> block;
    protected BlockBehaviour.Properties properties;
    protected final List<String> tags = new ArrayList<>();
    protected boolean presetItemModel = false;
    protected boolean simpleItemModel = false;
    protected String simpleItemModelPath = "";
    protected String specificModel = "";
    protected String customItemName = "";
    protected BlockColor blockColor;
    protected BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory;

    public static final List<Supplier<Runnable>> setupRenderLayerTasks = new ArrayList<>();

    @Override
    public BlockBuilder asBase() {
        return this;
    }

    public BlockBuilder() {
        this.defaultProperties();
    }

    public void checkName() {
        Objects.requireNonNull(name);
    }

    public RegistryObject<Block> getBlock() {
        return block;
    }

    public BlockBuilder buildBlock(Supplier<Block> blockSupplier) {
        checkName();
        block = RegisterHandle.BLOCK_REGISTER.register(name, blockSupplier);
        if (!tags.isEmpty()) {
            TagBuilder.blockTag(block, tags);
            Industrimania.LOGGER.debug("Register Block: {} with Tags: {}", name, tags.toString());
        } else {
            Industrimania.LOGGER.debug("Register Block: {}", name);
        }

        if (blockColor != null) {
            BlockColorHandler.register(block, blockColor);
        }

        return this;
    }

    public <T extends Block> BlockBuilder buildBlock(Function<BlockBehaviour.Properties, T> factory) {
        return buildBlock(() -> factory.apply(properties));
    }

    public BlockBuilder buildBlock() {
        return buildBlock(() -> new Block(properties));
    }

    public BlockBuilder asRock(float hardness, float explosionResistance, String rockTag) {
        checkName();
        AllRocks.ROCKS_NAME.add(name);
        AllRocks.ROCKS_HARDNESS.put(name, hardness);
        AllRocks.ROCKS_EXPLOSION_RESISTANCE.put(name, explosionResistance);
        this.tags(AllTags.ToolType.pickaxe, AllTags.IndustrimaniaTags.rock, rockTag);
        return this;
    }

    RegistryObject<Block> checkAlreadyBuild() {
        return Objects.requireNonNull(block, "can't build ItemBlock before block is built");
    }

    // =============================
    //  Build Item Method Overloads
    // =============================

    public ItemBlock buildItem(Function<ItemBuilder, ItemBuilder> factory) {
        final var itemName = this.customItemName.isEmpty() ? this.name : this.customItemName;
        final var block = checkAlreadyBuild();
        final ItemBuilder itemBuilder;
        if (this.presetItemModel) {
            itemBuilder = new ItemBuilder().name(itemName);
        } else if (this.simpleItemModel || !this.customItemName.isEmpty()) {
            itemBuilder = new ItemBuilder().name(itemName).simpleModel(this.simpleItemModelPath.isEmpty() ? itemName : this.simpleItemModelPath);
        } else if (!this.specificModel.isEmpty()) {
            itemBuilder = new ItemBuilder().name(itemName).specificModel(this.specificModel);
        } else {
            itemBuilder = new ItemBuilder().name(itemName).blockModel("block/" + this.name);
        }
        factory.apply(itemBuilder);
        Industrimania.LOGGER.debug("Register Block: {} with Item: {}", name, itemName);
        ItemBlock itemBlock = this.blockItemFactory == null ? new ItemBlock(itemBuilder.build(block), block) : new ItemBlock(itemBuilder.build(block, this.blockItemFactory), block);
        checkTags(itemBlock, this.tags);
        return itemBlock;
    }

    public ItemBlock buildItem() {
        return buildItem(itemBuilder -> itemBuilder);
    }

    // =============================
    //  No Item
    // =============================

    public RegistryObject<Block> noItem() {
        return checkAlreadyBuild();
    }

    // =============================
    //  BlockBuilder Parameters
    // =============================

    public BlockBuilder customItemName(String itemName) {
        this.customItemName = itemName;
        return this;
    }

    public BlockBuilder simpleItemModel() {
        this.simpleItemModel = true;
        return this;
    }

    public BlockBuilder simpleItemModel(String simpleItemModelPath) {
        this.simpleItemModel = true;
        this.simpleItemModelPath = simpleItemModelPath;
        return this;
    }

    public BlockBuilder specificItemModel(String modelPath) {
        this.specificModel = modelPath;
        return this;
    }

    /**
     * For those wierd circumstances where DataGen can't handle.
     * @return
     */
    public BlockBuilder presetItemModel() {
        this.presetItemModel = true;
        return this;
    }

    public BlockBuilder blockItemFactory(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory) {
        this.blockItemFactory = blockItemFactory;
        return this;
    }

    public BlockBuilder renderLayer(Supplier<Supplier<RenderType>> renderType) {
        setupRenderLayerTasks.add(() -> () -> ItemBlockRenderTypes.setRenderLayer(block.get(), renderType.get().get()));
        return this;
    }

    public BlockBuilder defaultProperties() {
        this.properties = BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(1.5f, 6.0f);
        return this;
    }

    public BlockBuilder properties(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> factory) {
        if (properties == null) {
            this.properties = BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(1.5f, 6.0f);
        }
        this.properties = factory.apply(this.properties);
        return this;
    }

    public BlockBuilder material(Material material) {
        this.properties = BlockBehaviour.Properties.of(material);
        return this;
    }

    public void checkTags(ItemBlock itemBlock, List<String> tags) {
        if (tags.contains(AllTags.IndustrimaniaTags.igneousStones)) AllRocks.igneousStones.add(itemBlock);
        if (tags.contains(AllTags.IndustrimaniaTags.metamorphicStones)) AllRocks.metamorphicStones.add(itemBlock);
        if (tags.contains(AllTags.IndustrimaniaTags.sedimentaryStones)) AllRocks.sedimentaryStones.add(itemBlock);
    }

    public BlockBuilder tags(TagKey<?>... tags) {
        this.tags.addAll(Arrays.stream(tags).map(tag -> tag.location().toString()).toList());
        return this;
    }

    public BlockBuilder tags(ResourceLocation... tags) {
        this.tags.addAll(Arrays.stream(tags).map(ResourceLocation::toString).toList());
        return this;
    }

    public BlockBuilder tags(String... tags) {
        this.tags.addAll(List.of(tags));
        return this;
    }

    public BlockBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BlockBuilder setRGBOverlay(Color color) {
        this.blockColor = (state, blockAndTintGetter, pos, pTintIndex) -> color.getRGB();
        return this;
    }

    public BlockBuilder setBlockColor(BlockColor blockColor) {
        this.blockColor = blockColor;
        return this;
    }

}
