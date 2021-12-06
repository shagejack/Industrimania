package shagejack.shagecraft.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shagejack.shagecraft.items.*;
import shagejack.shagecraft.items.includes.ShageBaseItem;
import shagejack.shagecraft.util.LogShage;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber

public class ShagecraftItems {
    public static List<Item> items = new ArrayList<>();

    //	Tools
    public Multimeter multimeter;
    public ForgeHammer forge_hammer;
    public Saw saw;
    public FlatFile flat_file;
    public FireTongs fire_tongs;
    public ShageBaseItem wind_flag;
    public ShageBaseItem gloves;
    public ShageBaseItem iron_pipe;

    //  Materials
    public ShageBaseItem slag;
    public ShageBaseItem clinker;
    public ShageBaseItem dust_sodium_carbonate;
    public ShageBaseItem dust_potassium_carbonate;
    public ShageBaseItem raw_glass_material;
    public ShageBaseItem cut_wrought_iron_ingot;
    public ShageBaseItem cut_iron_ingot;
    public ShageBaseItem cut_pig_iron_ingot;
    public ShageBaseItem cut_iron_rubbish;
    public ShageBaseItem burnt_stone_slab;
    public IronCluster iron_cluster;
    public MaterialWithMass iron_rubbish;
    public MaterialWithMass iron_ingot;
    public MaterialWithMass iron_small_plate;
    public MaterialWithMass iron_big_plate;
    public MaterialWithMass wrought_iron_ingot;
    public MaterialWithMass wrought_iron_small_plate;
    public MaterialWithMass wrought_iron_big_plate;
    public MaterialWithMass pig_iron_ingot;
    public MaterialWithMass pig_iron_small_plate;
    public MaterialWithMass pig_iron_big_plate;

    //Mould
    public ShageBaseItem glass_mould_block;

    private int registeredCount = 0;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        items.forEach(i -> event.getRegistry().register(i));
    }

    public void init() {

        LogShage.info("Registering items");

        //		Tools
        multimeter = register(new Multimeter("omni_multimeter"));
        forge_hammer = register(new ForgeHammer("forge_hammer"));
        saw = register(new Saw("saw"));
        flat_file = register(new FlatFile("flat_file"));
        fire_tongs = register(new FireTongs("fire_tongs"));
        wind_flag = register(new ShageBaseItem("wind_flag"));
        gloves = register(new ShageBaseItem("gloves"));
        iron_pipe = register(new ShageBaseItem("iron_pipe"));

        //      Materials
        slag = register(new ShageBaseItem("slag"));
        clinker = register(new ShageBaseItem("clinker"));
        dust_sodium_carbonate = register(new ShageBaseItem("dust_sodium_carbonate"));
        dust_potassium_carbonate = register(new ShageBaseItem("dust_potassium_carbonate"));
        raw_glass_material = register(new ShageBaseItem("raw_glass_material"));
        iron_rubbish = register(new MaterialWithMass("iron_rubbish"));
        cut_wrought_iron_ingot = register(new ShageBaseItem("cut_wrought_iron_ingot"));
        cut_iron_ingot = register(new ShageBaseItem("cut_iron_ingot"));
        cut_pig_iron_ingot = register(new ShageBaseItem("cut_pig_iron_ingot"));
        cut_iron_rubbish = register(new ShageBaseItem("cut_iron_rubbish"));
        burnt_stone_slab = register(new ShageBaseItem("burnt_stone_slab"));
        iron_cluster = register(new IronCluster("iron_cluster"));
        iron_ingot = register(new MaterialWithMass("iron_ingot"));
        iron_big_plate = register(new MaterialWithMass("iron_big_plate"));
        iron_small_plate = register(new MaterialWithMass("iron_small_plate"));
        wrought_iron_ingot = register(new MaterialWithMass("wrought_iron_ingot"));
        wrought_iron_big_plate = register(new MaterialWithMass("wrought_iron_big_plate"));
        wrought_iron_small_plate = register(new MaterialWithMass("wrought_iron_small_plate"));
        pig_iron_ingot = register(new MaterialWithMass("pig_iron_ingot"));
        pig_iron_big_plate = register(new MaterialWithMass("pig_iron_big_plate"));
        pig_iron_small_plate = register(new MaterialWithMass("pig_iron_small_plate"));

        //Mould
        glass_mould_block = register(new ShageBaseItem("glass_mould_block"));

        LogShage.info("Finished registering items");
        LogShage.info("Registered %d items", registeredCount);

    }

    protected <T extends Item> T register(T item) {
        items.add(item);
        registeredCount++;
        return item;
    }
}
