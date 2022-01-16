package shagejack.industrimania.registers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.metallurgyAge.item.smeltery.cluster.IronCluster;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBase;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import java.util.Objects;
import java.util.function.Function;

public class AllItems {

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

    public static final class ItemBuilder {
        private String name;
        private Properties property;
        RegistryObject<Item> registryObject;

        public ItemBuilder set(Function<Properties, Properties> function) {
            property = function.apply(new Properties());
            return this;
        }

        public ItemBuilder simpleModel(String texture) {
//            DataGenHandle.runOnDataGen( () -> () ->
            DataGenHandle.addItemModelTask((provider) -> {
                var item = this.registryObject.get();
                Industrimania.LOGGER.debug("set itemHeldModel for Item:{}", name);
                if (provider.existingFileHelper.exists(new ResourceLocation(Industrimania.MOD_ID, "item/" + name + "/" + texture), DataGenHandle.TEXTURE)) {
                    provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                            .parent(DataGenHandle.itemHeldModel.get()).texture("layer0", "item/" + name + "/" + texture);
                } else {
                    System.out.println("texture:item/" + name + "/" + texture + " not exists for Item:" + name);
//                    Industrimania.LOGGER.error("texture:{} not exists for Item:{}",texture,name);
                }
            })
//                    )
            ;
            return this;
        }

        public ItemBuilder blockModel(String model) {
            DataGenHandle.addItemModelTask((provider) -> {
                var item = this.registryObject.get();
                Industrimania.LOGGER.debug("set itemHeldModel for ItemBlock:{}", name);
                    provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                            .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, model)));
            })
            ;
            return this;
        }

        public ItemBuilder specificModel(String model) {
            DataGenHandle.addItemModelTask((provider) -> {
                var item = this.registryObject.get();
                Industrimania.LOGGER.debug("set itemHeldModel for Item:{}", name);
                if (provider.existingFileHelper.exists(new ResourceLocation(Industrimania.MOD_ID, model), DataGenHandle.MODEL)) {
                    provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                            .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, model)));
                } else {
                    System.out.println("model:" + model + " not exists for Item:" + name);
                }
            })
            ;
            return this;
        }


        public RegistryObject<Item> build() {
            Objects.requireNonNull(name);
            if (property == null) property = new Properties().tab(AllTabs.tab);
            registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> new Item(property));
            Industrimania.LOGGER.debug("register Item {}", name);
            return registryObject;
        }

        public <T extends Item> RegistryObject<Item> build(Function<Properties, T> factory) {
            Objects.requireNonNull(name);
            if (property == null) property = new Properties().tab(AllTabs.tab);
            registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> factory.apply(property));
            Industrimania.LOGGER.debug("register Item {}", name);
            return registryObject;
        }

        public RegistryObject<Item> build(RegistryObject<Block> block) {
            Objects.requireNonNull(name);
            if (property == null) property = new Properties().tab(AllTabs.tab);
            registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), property));
            return registryObject;
        }

        public ItemBuilder name(String name) {
            this.name = name;
            return this;
        }
    }

}
