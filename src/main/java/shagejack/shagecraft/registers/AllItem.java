package shagejack.shagecraft.registers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import shagejack.shagecraft.ShageCraft;
import shagejack.shagecraft.registers.dataGen.DataGenHandle;

import java.util.Objects;
import java.util.function.Function;

public class AllItem {

    public static final RegistryObject<Item> bronzeSaw = new ItemBuilder().name("bronze_saw").simpleModel("item/bronze_saw").build();
    public static final RegistryObject<Item> burntStoneSlab = new ItemBuilder().name("burnt_stone_slab").simpleModel("item/burnt_stone_slab").build();
    public static final RegistryObject<Item> clinker = new ItemBuilder().name("clinker").simpleModel("item/clinker").build();
    public static final RegistryObject<Item> cutIronIngot = new ItemBuilder().name("cut_iron_ingot").simpleModel("item/cut_iron_ingot").build();
    public static final RegistryObject<Item> cutIronRubbish = new ItemBuilder().name("cut_iron_rubbish").simpleModel("item/cut_iron_rubbish").build();
    public static final RegistryObject<Item> cutPigIronIngot = new ItemBuilder().name("cut_pig_iron_ingot").simpleModel("item/cut_pig_iron_ingot").build();
    public static final RegistryObject<Item> cutWroughtIronIngot = new ItemBuilder().name("cut_wrought_iron_ingot").simpleModel("item/cut_wrought_iron_ingot").build();
    public static final RegistryObject<Item> dustPotassiumCarbonate = new ItemBuilder().name("dust_potassium_carbonate").simpleModel("item/dust_potassium_carbonate").build();
    public static final RegistryObject<Item> dustSodiumCarbonate = new ItemBuilder().name("dust_sodium_carbonate").simpleModel("item/dust_sodium_carbonate").build();
    public static final RegistryObject<Item> fireTongs = new ItemBuilder().name("fire_tongs").simpleModel("item/fire_tongs").build();
    public static final RegistryObject<Item> flatFile = new ItemBuilder().name("flat_file").simpleModel("item/flat_file").build();
    public static final RegistryObject<Item> forgeHammer = new ItemBuilder().name("forge_hammer").simpleModel("item/forge_hammer").build();
    public static final RegistryObject<Item> gloves = new ItemBuilder().name("gloves").simpleModel("item/gloves").build();
    public static final RegistryObject<Item> ironBigPlate = new ItemBuilder().name("iron_big_plate").simpleModel("item/iron_big_plate").build();
    public static final RegistryObject<Item> ironCluster = new ItemBuilder().name("iron_cluster").simpleModel("item/iron_cluster").build();
    public static final RegistryObject<Item> ironIngot = new ItemBuilder().name("iron_ingot").simpleModel("item/iron_ingot").build();
    public static final RegistryObject<Item> ironPipe = new ItemBuilder().name("iron_pipe").simpleModel("item/iron_pipe").build();
    public static final RegistryObject<Item> ironRubbish = new ItemBuilder().name("iron_rubbish").simpleModel("item/iron_rubbish").build();
    public static final RegistryObject<Item> ironSmallPlate = new ItemBuilder().name("iron_small_plate").simpleModel("item/iron_small_plate").build();
    public static final RegistryObject<Item> omniMultimeter = new ItemBuilder().name("omni_multimeter").simpleModel("item/omni_multimeter").build();
    public static final RegistryObject<Item> rawGlassMaterial = new ItemBuilder().name("raw_glass_material").simpleModel("item/raw_glass_material").build();
    public static final RegistryObject<Item> slag = new ItemBuilder().name("slag").simpleModel("item/slag").build();
    public static final RegistryObject<Item> windFlag = new ItemBuilder().name("wind_flag").simpleModel("item/wind_flag").build();
    public static final RegistryObject<Item> wroughtIronIngot = new ItemBuilder().name("wrought_iron_ingot").simpleModel("item/wrought_iron_ingot").build();
    public static final RegistryObject<Item> wroughtIronSmallPlate = new ItemBuilder().name("wrought_iron_small_plate").simpleModel("item/wrought_iron_small_plate").build();

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
                ShageCraft.LOGGER.debug("set itemHeldModel for Item:{}", name);
                if (provider.existingFileHelper.exists(new ResourceLocation(ShageCraft.MOD_ID, texture), DataGenHandle.TEXTURE)) {
                    provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                            .parent(DataGenHandle.itemHeldModel.get()).texture("layer0", texture);
                } else {
                    System.out.println("texture:" + texture + " not exists for Item:" + name);
//                    ShageCraft.LOGGER.error("texture:{} not exists for Item:{}",texture,name);
                }
            })
//                    )
            ;
            return this;
        }

        public ItemBuilder blockModel(String model) {
            DataGenHandle.addItemModelTask((provider) -> {
                var item = this.registryObject.get();
                ShageCraft.LOGGER.debug("set itemHeldModel for ItemBlock:{}", name);
                    provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                            .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(ShageCraft.MOD_ID, model)));
            })
            ;
            return this;
        }

        public ItemBuilder specificModel(String model) {
            DataGenHandle.addItemModelTask((provider) -> {
                var item = this.registryObject.get();
                ShageCraft.LOGGER.debug("set itemHeldModel for Item:{}", name);
                if (provider.existingFileHelper.exists(new ResourceLocation(ShageCraft.MOD_ID, model), DataGenHandle.MODEL)) {
                    provider.getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath())
                            .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(ShageCraft.MOD_ID, model)));
                } else {
                    System.out.println("model:" + model + " not exists for Item:" + name);
                }
            })
            ;
            return this;
        }


        public RegistryObject<Item> build() {
            Objects.requireNonNull(name);
            if (property == null) property = new Properties().tab(AllTab.tab);
            registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> new Item(property));
            ShageCraft.LOGGER.debug("register Item {}", name);
            return registryObject;
        }

        public RegistryObject<Item> build(RegistryObject<Block> block) {
            Objects.requireNonNull(name);
            if (property == null) property = new Properties().tab(AllTab.tab);
            registryObject = RegisterHandle.ITEM_REGISTER.register(name, () -> new BlockItem(block.get(), property));
            return registryObject;
        }

        public ItemBuilder name(String name) {
            this.name = name;
            return this;
        }
    }

}
