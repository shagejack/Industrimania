package shagejack.industrimania.registers.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.contraptions.goggles.GogglesItem;
import shagejack.industrimania.content.contraptions.itemBase.*;
import shagejack.industrimania.content.contraptions.materialBase.KnifeMaterials;
import shagejack.industrimania.content.contraptions.materialBase.SawMaterials;
import shagejack.industrimania.content.logistics.item.filter.FilterItem;
import shagejack.industrimania.content.metallurgyAge.item.smeltery.cluster.IronCluster;
import shagejack.industrimania.content.pollution.protection.PollutionProtectiveArmor;
import shagejack.industrimania.content.primalAge.item.fireStarter.PrimitiveFireBow;
import shagejack.industrimania.content.primalAge.item.handOilLamp.HandOilLamp;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBase;
import shagejack.industrimania.content.primalAge.item.sandpaper.Sandpaper;
import shagejack.industrimania.registers.AllTabs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllItems {

    public static Map<RegistryObject<Item>, List<String>> ITEM_TAGS = new HashMap<>();

    /*
    * =============
    *  Primal Age
    * =============
    */
    //Material
    public static final RegistryObject<Item> sharpenedStick = new ItemBuilder().name("sharpened_stick").simpleModel("sharpened_stick").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> weedFiber = new ItemBuilder().name("weed_fiber").simpleModel("weed_fiber").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> weedRope = new ItemBuilder().name("weed_rope").simpleModel("weed_rope").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> woodPeg = new ItemBuilder().name("wood_peg").simpleModel("wood_peg").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> mud = new ItemBuilder().name("mud").simpleModel("mud").tab(AllTabs.tabMaterial).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> hay = new ItemBuilder().name("hay").simpleModel("hay").tab(AllTabs.tabMaterial).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> rottenGrass = new ItemBuilder().name("rotten_grass").simpleModel("rotten_grass").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> boneFragment = new ItemBuilder().name("bone_fragment").simpleModel("bone_fragment").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> rushRoot = new ItemBuilder().name("rush_root").simpleModel("rush_root").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> rushStalk = new ItemBuilder().name("rush_stalk").simpleModel("rush_stalk").tab(AllTabs.tabMaterial).build();

    //Natural Resource
    public static final RegistryObject<Item> silkworm = new ItemBuilder().name("silkworm").simpleModel("silkworm").tab(AllTabs.tabNature).maxStackSize(1).build();
    public static final RegistryObject<Item> pupa = new ItemBuilder().name("pupa").simpleModel("pupa").tab(AllTabs.tabNature).build();
    public static final RegistryObject<Item> dryPupa = new ItemBuilder().name("dry_pupa").simpleModel("dry_pupa").tab(AllTabs.tabNature).build();

    public static final RegistryObject<Item> grass = new ItemBuilder().name("grass").simpleModel("grass").tab(AllTabs.tabNature).build();
    public static final RegistryObject<Item> logAcacia = new ItemBuilder().name("log_acacia").simpleModel("log_acacia").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> logBirch = new ItemBuilder().name("log_birch").simpleModel("log_birch").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> logJungle = new ItemBuilder().name("log_jungle").simpleModel("log_jungle").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> logOak = new ItemBuilder().name("log_oak").simpleModel("log_oak").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> logSpruce = new ItemBuilder().name("log_spruce").simpleModel("log_spruce").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> logDarkOak = new ItemBuilder().name("log_darkoak").simpleModel("log_darkoak").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> plankAcacia = new ItemBuilder().name("plank_acacia").simpleModel("plank_acacia").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> plankBirch = new ItemBuilder().name("plank_birch").simpleModel("plank_birch").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> plankJungle = new ItemBuilder().name("plank_jungle").simpleModel("plank_jungle").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> plankOak = new ItemBuilder().name("plank_oak").simpleModel("plank_oak").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> plankSpruce = new ItemBuilder().name("plank_spruce").simpleModel("plank_spruce").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> plankDarkOak = new ItemBuilder().name("plank_darkoak").simpleModel("plank_darkoak").tab(AllTabs.tabNature).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> plankAcaciaSmooth = new ItemBuilder().name("plank_acacia_smooth").simpleModel("plank_acacia_smooth").tab(AllTabs.tabNature).build();
    public static final RegistryObject<Item> plankBirchSmooth = new ItemBuilder().name("plank_birch_smooth").simpleModel("plank_birch_smooth").tab(AllTabs.tabNature).build();
    public static final RegistryObject<Item> plankJungleSmooth = new ItemBuilder().name("plank_jungle_smooth").simpleModel("plank_jungle_smooth").tab(AllTabs.tabNature).build();
    public static final RegistryObject<Item> plankOakSmooth = new ItemBuilder().name("plank_oak_smooth").simpleModel("plank_oak_smooth").tab(AllTabs.tabNature).build();
    public static final RegistryObject<Item> plankSpruceSmooth = new ItemBuilder().name("plank_spruce_smooth").simpleModel("plank_spruce_smooth").tab(AllTabs.tabNature).build();
    public static final RegistryObject<Item> plankDarkOakSmooth = new ItemBuilder().name("plank_darkoak_smooth").simpleModel("plank_darkoak_smooth").tab(AllTabs.tabNature).build();
    public static final RegistryObject<Item> mulberryFruit = new ItemBuilder().name("mulberry_fruit").simpleModel("mulberry_fruit").tab(AllTabs.tabNature).food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.5F).build()).build();
    public static final RegistryObject<Item> mulberryLeaf = new ItemBuilder().name("mulberry_leaf").simpleModel("mulberry_leaf").tab(AllTabs.tabNature).durability(16).build();

    //Tool
    public static final RegistryObject<Item> longStick = new ItemBuilder().name("long_stick").simpleModel("long_stick").tab(AllTabs.tabTool).build();
    public static final RegistryObject<Item> simpleBoneKnife = new ItemBuilder().name("simple_bone_knife").simpleModel("simple_bone_knife").tab(AllTabs.tabTool).addExtraParam("KnifeMaterial", KnifeMaterials.BONE).durability(16).build(IMKnifeItemBase::new);
    public static final RegistryObject<Item> primitiveFireBow = new ItemBuilder().name("primitive_fire_bow").simpleModel("primitive_fire_bow").durability(64).tab(AllTabs.tabTool).build(PrimitiveFireBow::new);
    public static final RegistryObject<Item> handOilLamp = new ItemBuilder().name("hand_oil_lamp").simpleModel("hand_oil_lamp").durability(128).tab(AllTabs.tabTool).build(HandOilLamp::new);
    public static final RegistryObject<Item> flintKnife = new ItemBuilder().name("flint_knife").simpleModel("flint_knife").tab(AllTabs.tabTool).addExtraParam("KnifeMaterial", KnifeMaterials.FLINT).durability(64).build(IMKnifeItemBase::new);
    public static final RegistryObject<Item> flintSaw = new ItemBuilder().name("flint_saw").simpleModel("flint_saw").tab(AllTabs.tabTool).addExtraParam("SawMaterial", SawMaterials.FLINT).durability(64).build(IMSawItemBase::new);
    public static final RegistryObject<Item> flintAxe = new ItemBuilder().name("flint_axe").simpleModel("flint_axe").tab(AllTabs.tabTool).addExtraParam("Tier", IMTiers.FLINT).addExtraParam("AttackDamageBaseline", 6.0f).addExtraParam("AttackSpeed", -3.2f).durability(64).tags().build(IMAxeItemBase::new);
    public static final RegistryObject<Item> flintPickaxe = new ItemBuilder().name("flint_pickaxe").simpleModel("flint_pickaxe").tab(AllTabs.tabTool).addExtraParam("Tier", IMTiers.FLINT).addExtraParam("AttackDamageBaseline", 1.0f).addExtraParam("AttackSpeed", -2.8f).durability(64).build(IMAxeItemBase::new);
    public static final RegistryObject<Item> sandpaper = new ItemBuilder().name("sandpaper").simpleModel("sandpaper").tab(AllTabs.tabTool).durability(64).build(Sandpaper::new);

    //Misc
    public static final RegistryObject<Item> woodenBarrelCover = new ItemBuilder().name("wooden_barrel_cover").simpleModel("wooden_barrel_cover").tab(AllTabs.tabMisc).maxStackSize(1).build();


    /*
     * =============
     *  Stone Age
     * =============
     */

    //Material
    public static final RegistryObject<Item> rockAdhesive = new ItemBuilder().name("rock_adhesive").simpleModel("rock_adhesive").tab(AllTabs.tabMaterial).build(ItemPlaceableBase::new);
    public static final RegistryObject<Item> silica = new ItemBuilder().name("silica").simpleModel("silica").tab(AllTabs.tabMaterial).build();

    /*
     * ===============
     *  Metallurgy Age
     * ===============
     */
    public static final RegistryObject<Item> bronzeHopper = new ItemBuilder().name("bronze_hopper").simpleModel("bronze_hopper").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> bronzeSaw = new ItemBuilder().name("bronze_saw").simpleModel("bronze_saw").tab(AllTabs.tabTool).build();
    public static final RegistryObject<Item> burntStoneSlab = new ItemBuilder().name("burnt_stone_slab").simpleModel("burnt_stone_slab").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> clinker = new ItemBuilder().name("clinker").simpleModel("clinker").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> cutIronIngot = new ItemBuilder().name("cut_iron_ingot").simpleModel("cut_iron_ingot").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> cutIronRubbish = new ItemBuilder().name("cut_iron_rubbish").simpleModel("cut_iron_rubbish").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> cutPigIronIngot = new ItemBuilder().name("cut_pig_iron_ingot").simpleModel("cut_pig_iron_ingot").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> cutWroughtIronIngot = new ItemBuilder().name("cut_wrought_iron_ingot").simpleModel("cut_wrought_iron_ingot").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> dustPotassiumCarbonate = new ItemBuilder().name("dust_potassium_carbonate").simpleModel("dust_potassium_carbonate").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> dustSodiumCarbonate = new ItemBuilder().name("dust_sodium_carbonate").simpleModel("dust_sodium_carbonate").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> fireTongs = new ItemBuilder().name("fire_tongs").simpleModel("fire_tongs").tab(AllTabs.tabTool).build();
    public static final RegistryObject<Item> flatFile = new ItemBuilder().name("flat_file").simpleModel("flat_file").tab(AllTabs.tabTool).build();
    public static final RegistryObject<Item> forgeHammer = new ItemBuilder().name("forge_hammer").simpleModel("forge_hammer").tab(AllTabs.tabTool).build();
    public static final RegistryObject<Item> gloves = new ItemBuilder().name("gloves").simpleModel("gloves").tab(AllTabs.tabTool).build();
    public static final RegistryObject<Item> ironBigPlate = new ItemBuilder().name("iron_big_plate").simpleModel("iron_big_plate").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> ironIngot = new ItemBuilder().name("iron_ingot").simpleModel("iron_ingot").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> ironPipe = new ItemBuilder().name("iron_pipe").simpleModel("iron_pipe").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> ironRubbish = new ItemBuilder().name("iron_rubbish").simpleModel("iron_rubbish").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> ironSmallPlate = new ItemBuilder().name("iron_small_plate").simpleModel("iron_small_plate").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> omniMultimeter = new ItemBuilder().name("omni_multimeter").simpleModel("omni_multimeter").tab(AllTabs.tabTool).build();
    public static final RegistryObject<Item> rawGlassMaterial = new ItemBuilder().name("raw_glass_material").simpleModel("raw_glass_material").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> slag = new ItemBuilder().name("slag").simpleModel("slag").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> windFlag = new ItemBuilder().name("wind_flag").simpleModel("wind_flag").tab(AllTabs.tabTool).build();
    public static final RegistryObject<Item> wroughtIronIngot = new ItemBuilder().name("wrought_iron_ingot").simpleModel("wrought_iron_ingot").tab(AllTabs.tabMaterial).build();
    public static final RegistryObject<Item> wroughtIronSmallPlate = new ItemBuilder().name("wrought_iron_small_plate").simpleModel("wrought_iron_small_plate").tab(AllTabs.tabMaterial).build();

    public static final RegistryObject<Item> ironCluster = new ItemBuilder().name("iron_cluster").simpleModel("iron_cluster").tab(AllTabs.tabMaterial).build(IronCluster::new);

    /*
     * =============
     *  Steam Age
     * =============
     */

    public static final RegistryObject<Item> hazardProtectiveHelmet = new ItemBuilder().name("hazard_protective_helmet").simpleModel("hazard_protective_helmet").addExtraParam("ArmorMaterial", ArmorMaterials.LEATHER).addExtraParam("EquipmentSlot",EquipmentSlot.HEAD).tab(AllTabs.tabEquipment).build(PollutionProtectiveArmor::new);
    public static final RegistryObject<Item> hazardProtectiveChestplate = new ItemBuilder().name("hazard_protective_chestplate").simpleModel("hazard_protective_chestplate").addExtraParam("ArmorMaterial", ArmorMaterials.LEATHER).addExtraParam("EquipmentSlot",EquipmentSlot.CHEST).tab(AllTabs.tabEquipment).build(PollutionProtectiveArmor::new);
    public static final RegistryObject<Item> hazardProtectiveLeggings = new ItemBuilder().name("hazard_protective_leggings").simpleModel("hazard_protective_leggings").addExtraParam("ArmorMaterial", ArmorMaterials.LEATHER).addExtraParam("EquipmentSlot",EquipmentSlot.LEGS).tab(AllTabs.tabEquipment).build(PollutionProtectiveArmor::new);
    public static final RegistryObject<Item> hazardProtectiveBoots = new ItemBuilder().name("hazard_protective_boots").simpleModel("hazard_protective_boots").addExtraParam("ArmorMaterial", ArmorMaterials.LEATHER).addExtraParam("EquipmentSlot",EquipmentSlot.FEET).tab(AllTabs.tabEquipment).build(PollutionProtectiveArmor::new);


    /*
     * =========================
     *  Logistics
     * =========================
     */
    public static final RegistryObject<Item> FILTER = new ItemBuilder().name("filter").simpleModel("filter").build(FilterItem::regular);
    public static final RegistryObject<Item> ATTRIBUTE_FILTER = new ItemBuilder().name("attribute_filter").simpleModel("filter").build(FilterItem::attribute);

    /*
     * =========================
     *  Contraptions
     * =========================
     */
    public static final RegistryObject<Item> GOGGLES = new ItemBuilder().name("goggles").simpleModel("goggles").build(GogglesItem::new);


    /*
     * =========================
     *  Creative Mode/ Test Item
     * =========================
     */
    public static final RegistryObject<Item> creativeHazardProtectiveHelmet = new ItemBuilder().name("creative_hazard_protective_helmet").simpleModel("creative_hazard_protective_helmet").addExtraParam("ArmorMaterial", ArmorMaterials.NETHERITE).addExtraParam("EquipmentSlot",EquipmentSlot.HEAD).tab(AllTabs.tabEquipment).build(IMArmorItemBase::new);

    /*
     * ==========================
     *  Model Item for Rendering
     * ==========================
     */
    public static final RegistryObject<Item> modelLog = new ItemBuilder().name("model_log").specificModel("item/model/log").noTab().build();
    public static final RegistryObject<Item> modelWood = new ItemBuilder().name("model_wood").specificModel("item/model/wood").noTab().build();
}
