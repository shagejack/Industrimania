package shagejack.industrimania.registers;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.contraptions.ore.ItemOreChunk;
import shagejack.industrimania.content.metallurgyAge.item.smeltery.cluster.IronCluster;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBase;
import shagejack.industrimania.content.worldGen.OreTypeRegistry;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkItemModelExist;
import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkItemTextureExist;

public class AllItems {

    public static Map<String, RegistryObject<Item>> ORE_CHUNKS = new HashMap<>();

    /*
    * =============
    *  Primal Age
    * ============
    */
    //Material
    public static final RegistryObject<Item> mud = new ItemBuilder().name("mud").simpleModel("mud").build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> hay = new ItemBuilder().name("hay").simpleModel("hay").build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> rock = new ItemBuilder().name("rock").simpleModel("rock").build(ItemPlaceableBase::new);
    //Natural Resource

    //Tool

    /*
     * =============
     *  Stone Age
     * ============
     */

    /*
     * =============
     *  Metallurgy Age
     * ============
     */
    public static final RegistryObject<Item> bronzeHopper = new ItemBuilder().name("bronze_hopper").simpleModel("bronze_hopper").build();
    public static final RegistryObject<Item> bronzeSaw = new ItemBuilder().name("bronze_saw").simpleModel("bronze_saw").build();
    public static final RegistryObject<Item> burntStoneSlab = new ItemBuilder().name("burnt_stone_slab").simpleModel("burnt_stone_slab").build();
    public static final RegistryObject<Item> clinker = new ItemBuilder().name("clinker").simpleModel("clinker").build();
    public static final RegistryObject<Item> cutIronIngot = new ItemBuilder().name("cut_iron_ingot").simpleModel("cut_iron_ingot").build();
    public static final RegistryObject<Item> cutIronRubbish = new ItemBuilder().name("cut_iron_rubbish").simpleModel("cut_iron_rubbish").build();
    public static final RegistryObject<Item> cutPigIronIngot = new ItemBuilder().name("cut_pig_iron_ingot").simpleModel("cut_pig_iron_ingot").build();
    public static final RegistryObject<Item> cutWroughtIronIngot = new ItemBuilder().name("cut_wrought_iron_ingot").simpleModel("cut_wrought_iron_ingot").build();
    public static final RegistryObject<Item> dustPotassiumCarbonate = new ItemBuilder().name("dust_potassium_carbonate").simpleModel("dust_potassium_carbonate").build();
    public static final RegistryObject<Item> dustSodiumCarbonate = new ItemBuilder().name("dust_sodium_carbonate").simpleModel("dust_sodium_carbonate").build();
    public static final RegistryObject<Item> fireTongs = new ItemBuilder().name("fire_tongs").simpleModel("fire_tongs").build();
    public static final RegistryObject<Item> flatFile = new ItemBuilder().name("flat_file").simpleModel("flat_file").build();
    public static final RegistryObject<Item> forgeHammer = new ItemBuilder().name("forge_hammer").simpleModel("forge_hammer").build();
    public static final RegistryObject<Item> gloves = new ItemBuilder().name("gloves").simpleModel("gloves").build();
    public static final RegistryObject<Item> ironBigPlate = new ItemBuilder().name("iron_big_plate").simpleModel("iron_big_plate").build();
    public static final RegistryObject<Item> ironIngot = new ItemBuilder().name("iron_ingot").simpleModel("iron_ingot").build();
    public static final RegistryObject<Item> ironPipe = new ItemBuilder().name("iron_pipe").simpleModel("iron_pipe").build();
    public static final RegistryObject<Item> ironRubbish = new ItemBuilder().name("iron_rubbish").simpleModel("iron_rubbish").build();
    public static final RegistryObject<Item> ironSmallPlate = new ItemBuilder().name("iron_small_plate").simpleModel("iron_small_plate").build();
    public static final RegistryObject<Item> omniMultimeter = new ItemBuilder().name("omni_multimeter").simpleModel("omni_multimeter").build();
    public static final RegistryObject<Item> rawGlassMaterial = new ItemBuilder().name("raw_glass_material").simpleModel("raw_glass_material").build();
    public static final RegistryObject<Item> slag = new ItemBuilder().name("slag").simpleModel("slag").build();
    public static final RegistryObject<Item> windFlag = new ItemBuilder().name("wind_flag").simpleModel("wind_flag").build();
    public static final RegistryObject<Item> wroughtIronIngot = new ItemBuilder().name("wrought_iron_ingot").simpleModel("wrought_iron_ingot").build();
    public static final RegistryObject<Item> wroughtIronSmallPlate = new ItemBuilder().name("wrought_iron_small_plate").simpleModel("wrought_iron_small_plate").build();

    public static final RegistryObject<Item> ironCluster = new ItemBuilder().name("iron_cluster").simpleModel("iron_cluster").build(IronCluster::new);

    public static void initOres() {
        OreTypeRegistry.oreTypeMap.forEach( (oreTypeName, oreType) -> {
               for(String rockName : AllBlocks.ROCKS) {
                    for (int grade = 0; grade <= 2; grade ++) {
                        String key = rockName + "_" + oreType.name() + "_" + grade;
                        RegistryObject<Item> oreChunk
                                = new ItemBuilder()
                                .name("chunk_" + key)
                                .tab(AllTabs.tabOre)
                                .simpleModel("chunk_" + key)
                                .build(ItemOreChunk::new);

                        ORE_CHUNKS.put(key, oreChunk);
                    }
               }
            }
        );
    }

    public static final class ItemBuilder {
        private String name;
        private CreativeModeTab tab;
        private Properties property;
        RegistryObject<Item> registryObject;
        private Supplier<Supplier<BlockEntityWithoutLevelRenderer>> render;

        private final Map<String, Object> extraParam = new HashMap();

        public ItemBuilder set(Function<Properties, Properties> function) {
            if (property==null){
                this.property= new Properties();
            }
            property = function.apply(this.property);
            return this;
        }

        /**
         * also call {@link ItemBuilder#builtInEntityModel}
         */
        public ItemBuilder setBlockEntityWithoutLevelRender(Supplier<Supplier<BlockEntityWithoutLevelRenderer>> render){
            this.render=render;
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

        public ItemBuilder model(Consumer<ItemModelProvider> task){
            DataGenHandle.addItemModelTask(task);
            return this;
        }

        public ItemBuilder simpleModel(String texture) {
           model((provider) -> {
                var item = this.registryObject.get();
                if (checkItemTextureExist(provider,name,texture)) {
                    provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                            .parent(DataGenHandle.itemHeldModel.get())
                            .texture("layer0", "item/" + name + "/" + texture);
                }
               Industrimania.LOGGER.debug("set itemHeldModel for Item:{}", name);
           });
            return this;
        }

        public ItemBuilder blockModel(String model) {
            model((provider) -> {
                var item = this.registryObject.get();
                provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                        .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, model)));
                Industrimania.LOGGER.debug("set itemHeldModel for ItemBlock:{}", name);
            });
            return this;
        }

        public ItemBuilder specificModel(String model) {
            model((provider) -> {
                var item = this.registryObject.get();
                if (checkItemModelExist(provider,name,model)) {
                    provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                            .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, model)));
                    Industrimania.LOGGER.debug("set itemHeldModel for Item:{}", name);
                }
            });
            return this;
        }

        public ItemBuilder builtInEntityModel(String model){
            return model((provider -> {
                var item = this.registryObject.get();
                provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                        .parent(DataGenHandle.blockBuiltinEntity.get());
            }));
        }

        public RegistryObject<Item> build() {
            return build(()->new Item(property){
                @Override
                public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
                    if (render!=null){
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
        public <T extends Item> RegistryObject<Item> build(Function<Properties, T> factory) {
            return build(()->factory.apply(property));
        }

        /**
         * don't support set BEWLR
         */
        public <T extends Item> RegistryObject<Item> build(BiFunction<Properties, Map<String, Object>, T> factory) {
            return build(()->factory.apply(property,extraParam));
        }

        /**
         * don't support set BEWLR
         */
        public RegistryObject<Item> build(Supplier<Item> itemSupplier){
            checkProperty();
            registryObject = RegisterHandle.ITEM_REGISTER.register(name,itemSupplier);
            Industrimania.LOGGER.debug("register Item {}", name);
            return registryObject;
        }

        public RegistryObject<Item> build(RegistryObject<Block> block) {
            checkProperty();
            registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), property){
                @Override
                public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
                    if (render!=null){
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

        private void checkProperty(){
            Objects.requireNonNull(name);
            if (property==null){
                this.property = new Properties();
            }
            property.tab(Objects.requireNonNullElse(tab, AllTabs.tab));
        }

        public ItemBuilder name(String name) {
            this.name = name;
            return this;
        }
    }

}
