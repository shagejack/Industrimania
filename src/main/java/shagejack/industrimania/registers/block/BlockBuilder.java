package shagejack.industrimania.registers.block;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.AllItems;
import shagejack.industrimania.registers.AllTags;
import shagejack.industrimania.registers.ItemBlock;
import shagejack.industrimania.registers.RegisterHandle;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder implements ModelBuilder, StateBuilder {

    protected String name;
    protected RegistryObject<Block> block;
    protected BlockBehaviour.Properties property;
    protected final List<String> tags = new ArrayList<>();
    protected final List<String> modTags = new ArrayList<>();
    protected float hardness;
    protected float explosionResistance;
    protected Material material;
    protected boolean requiresCorrectToolForDrops = false;
    protected final Map<String, Object> extraParam = new HashMap();

    public static final List<Supplier<Runnable>> setupRenderLayerTasks = new ArrayList<>();

    @Override
    public BlockBuilder asBase() {
        return this;
    }

    private void checkProperty() {
        Objects.requireNonNull(name);
        if (property == null) {
            if (material == null) {
                this.material = Material.STONE;
            }
            property = BlockBehaviour.Properties.of(material).strength(1.5f, 6.0f);
        }
        if (hardness != 0 && explosionResistance != 0) {
            property.strength(hardness, explosionResistance);
        }
        if (requiresCorrectToolForDrops) {
            property.requiresCorrectToolForDrops();
        }
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
        AllBlocks.ROCKS.add(name);
        AllBlocks.ROCKS_HARDNESS.put(name, hardness);
        AllBlocks.ROCKS_EXPLOSION_RESISTANCE.put(name, explosionResistance);
        this.tags(AllTags.ToolType.pickaxe, AllTags.IndustrimaniaTags.rock, rockTag);
        return this;
    }

    public BlockBuilder asOre(String oreType) {
        checkProperty();
        this.tags(AllTags.ToolType.pickaxe, AllTags.IndustrimaniaTags.ore, AllTags.IndustrimaniaTags.oreTypeEntry + oreType);
        return this;
    }

    RegistryObject<Block> checkAlreadyBuild() {
        return Objects.requireNonNull(block, "can't build ItemBlock before block is built");
    }

    public ItemBlock buildItem(String itemName, Function<AllItems.ItemBuilder, AllItems.ItemBuilder> factory) {
        final var block = checkAlreadyBuild();
        final AllItems.ItemBuilder itemBuilder = new AllItems.ItemBuilder().name(itemName).blockModel("block/" + this.name);
        factory.apply(itemBuilder);
        Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, itemName);
        return new ItemBlock(itemBuilder.build(block), block);
    }

    public ItemBlock buildItem(Function<AllItems.ItemBuilder, AllItems.ItemBuilder> factory) {
        return buildItem(name, factory);
    }

    public ItemBlock buildItem() {
        return buildItem(name, (itemBuilder) -> itemBuilder);
    }


    public BlockBuilder renderLayer(Supplier<Supplier<RenderType>> renderType) {
        setupRenderLayerTasks.add(() -> () -> ItemBlockRenderTypes.setRenderLayer(block.get(), renderType.get().get()));
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

    public BlockBuilder strength(float hardness, float explosionResistance) {
        this.hardness = hardness;
        this.explosionResistance = explosionResistance;
        return this;
    }

    public BlockBuilder requiresCorrectToolForDrops() {
        this.requiresCorrectToolForDrops = true;
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

}
