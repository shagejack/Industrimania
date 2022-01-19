package shagejack.industrimania.registers.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.contraptions.itemBase.IMArmorItemBase;
import shagejack.industrimania.content.metallurgyAge.item.smeltery.cluster.IronCluster;
import shagejack.industrimania.content.pollution.protection.PollutionProtectiveArmor;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBase;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.woodPlaceable.ItemWoodPlaceable;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.woodPlaceable.WoodPlaceableTileEntity;
import shagejack.industrimania.registers.AllTabs;

import java.util.*;

public class AllItems {

    /*
    * =============
    *  Primal Age
    * ============
    */
    //Material
    public static final RegistryObject<Item> mud = new ItemBuilder().name("mud").simpleModel("mud").build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> hay = new ItemBuilder().name("hay").simpleModel("hay").build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> rock_adhesive = new ItemBuilder().name("rock_adhesive").simpleModel("rock_adhesive").build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> silica = new ItemBuilder().name("silica").simpleModel("silica").build();

    //Natural Resource
    public static final RegistryObject<Item> log_acacia = new ItemBuilder().name("log_acacia").simpleModel("log_acacia").tab(AllTabs.tabNature).build(ItemWoodPlaceable::new);
    public static final RegistryObject<Item> log_birch = new ItemBuilder().name("log_birch").simpleModel("log_birch").tab(AllTabs.tabNature).build(ItemWoodPlaceable::new);
    public static final RegistryObject<Item> log_jungle = new ItemBuilder().name("log_jungle").simpleModel("log_jungle").tab(AllTabs.tabNature).build(ItemWoodPlaceable::new);
    public static final RegistryObject<Item> log_oak = new ItemBuilder().name("log_oak").simpleModel("log_oak").tab(AllTabs.tabNature).build(ItemWoodPlaceable::new);
    public static final RegistryObject<Item> log_spruce = new ItemBuilder().name("log_spruce").simpleModel("log_spruce").tab(AllTabs.tabNature).build(ItemWoodPlaceable::new);
    public static final RegistryObject<Item> log_darkoak = new ItemBuilder().name("log_darkoak").simpleModel("log_darkoak").tab(AllTabs.tabNature).build(ItemWoodPlaceable::new);

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

    /*
     * =============
     *  Steam Age
     * ============
     */

    public static final RegistryObject<Item> hazardProtectiveHelmet = new ItemBuilder().name("hazard_protective_helmet").simpleModel("hazard_protective_helmet").addExtraParam("armorMaterial", ArmorMaterials.LEATHER).addExtraParam("equipmentSlot",EquipmentSlot.HEAD).build(PollutionProtectiveArmor::new);
    public static final RegistryObject<Item> hazardProtectiveChestplate = new ItemBuilder().name("hazard_protective_chestplate").simpleModel("hazard_protective_chestplate").addExtraParam("armorMaterial", ArmorMaterials.LEATHER).addExtraParam("equipmentSlot",EquipmentSlot.CHEST).build(PollutionProtectiveArmor::new);
    public static final RegistryObject<Item> hazardProtectiveLeggings = new ItemBuilder().name("hazard_protective_leggings").simpleModel("hazard_protective_leggings").addExtraParam("armorMaterial", ArmorMaterials.LEATHER).addExtraParam("equipmentSlot",EquipmentSlot.LEGS).build(PollutionProtectiveArmor::new);
    public static final RegistryObject<Item> hazardProtectiveBoots = new ItemBuilder().name("hazard_protective_boots").simpleModel("hazard_protective_boots").addExtraParam("armorMaterial", ArmorMaterials.LEATHER).addExtraParam("equipmentSlot",EquipmentSlot.FEET).build(PollutionProtectiveArmor::new);



    /*
     * =========================
     *  Creative Mode/ Test Item
     * =========================
     */
    public static final RegistryObject<Item> creativeHazardProtectiveHelmet = new ItemBuilder().name("creative_hazard_protective_helmet").simpleModel("creative_hazard_protective_helmet").addExtraParam("armorMaterial", ArmorMaterials.NETHERITE).addExtraParam("equipmentSlot",EquipmentSlot.HEAD).build(IMArmorItemBase::new);

}
