package shagejack.minecraftology.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shagejack.minecraftology.items.*;
import shagejack.minecraftology.items.includes.MCLBaseItem;
import shagejack.minecraftology.util.LogMCL;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber

public class ItemsMCL {
    public static List<Item> items = new ArrayList<>();

    //	Tools
    public Multimeter multimeter;
    public ForgeHammer forge_hammer;
    public Saw saw;
    public FlatFile flat_file;
    public FireTongs fire_tongs;
    public MCLBaseItem wind_flag;
    public MCLBaseItem gloves;

    //  Materials
    public MCLBaseItem slag;
    public MCLBaseItem clinker;
    public MCLBaseItem cut_wrought_iron_ingot;
    public MCLBaseItem cut_iron_ingot;
    public MCLBaseItem cut_pig_iron_ingot;
    public MCLBaseItem cut_iron_rubbish;
    public MCLBaseItem burnt_stone_slab;
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

    private int registeredCount = 0;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        items.forEach(i -> event.getRegistry().register(i));
    }

    public void init() {

        LogMCL.info("Registering items");

        //		Tools
        multimeter = register(new Multimeter("omni_multimeter"));
        forge_hammer = register(new ForgeHammer("forge_hammer"));
        saw = register(new Saw("saw"));
        flat_file = register(new FlatFile("flat_file"));
        fire_tongs = register(new FireTongs("fire_tongs"));
        wind_flag = register(new MCLBaseItem("wind_flag"));
        gloves = register(new MCLBaseItem("gloves"));

        //      Materials
        slag = register(new MCLBaseItem("slag"));
        clinker = register(new MCLBaseItem("clinker"));
        iron_rubbish = register(new MaterialWithMass("iron_rubbish"));
        cut_wrought_iron_ingot = register(new MCLBaseItem("cut_wrought_iron_ingot"));
        cut_iron_ingot = register(new MCLBaseItem("cut_iron_ingot"));
        cut_pig_iron_ingot = register(new MCLBaseItem("cut_pig_iron_ingot"));
        cut_iron_rubbish = register(new MCLBaseItem("cut_iron_rubbish"));
        burnt_stone_slab = register(new MCLBaseItem("burnt_stone_slab"));
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

        LogMCL.info("Finished registering items");
        LogMCL.info("Registered %d items", registeredCount);

    }

    protected <T extends Item> T register(T item) {
        items.add(item);
        registeredCount++;
        return item;
    }
}
