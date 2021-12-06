package shagejack.shagecraft.handler;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shagejack.shagecraft.Reference;
import shagejack.shagecraft.util.IConfigSubscriber;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigurationHandler {
    public static final String CATEGORY_SHAGE = "shagecraft registry";
    public static final String CATEGORY_SHAGE_ITEMS = "shagecraft items registry";
    public static final String CATEGORY_WORLD_GEN = "world gen";
    public static final String CATEGORY_MACHINES = "machine options";
    public static final String CATEGORY_WORLD_SPAWN_ORES = "spawn ores";
    public static final String CATEGORY_WORLD_SPAWN_OTHER = "spawn other";
    public static final String CATEGORY_WORLD_SPAWN = "spawn";
    public static final String CATEGORY_CLIENT = "client";
    public static final String CATEGORY_SERVER = "server";
    public static final String CATEGORY_ENCHANTMENTS = "enchantments";
    public static final String CATEGORY_ENTITIES = "entities";
    public static final String CATEGORY_COMPATIBILITY = "compatibility";
    public static final String CATEGORY_DEBUG = "debug";
    public static final String KEY_MAX_BROADCASTS = "max broadcasts per tick";
    public static final String KEY_SHAGEBLACKLIST = "blacklist";
    public static final String KEY_BLACKLIST_MODS = "mod_blacklist";
    public static final String KEY_VERSION_CHECK = "version_check";
    public static final String KEY_MATTER_REGISTRATION_DEBUG = "Shage registration";
    public static final String KEY_MATTER_CALCULATION_DEBUG = "Shage calculation";

    public final File configDir;

    public final Configuration config;
    private final Set<IConfigSubscriber> subscribers;

    public ConfigurationHandler(File configDir) {
        this.configDir = configDir;
        config = new Configuration(new File(configDir, "shagecraft/shagecraft.cfg"), "1.0");
        subscribers = new HashSet<>();
    }

    public void init() {
        config.load();
        ConfigCategory category = config.getCategory(CATEGORY_SHAGE);
        category.setComment("Configuration for the Matter ");
        updateCategoryLang(category);
        config.get(CATEGORY_SHAGE, KEY_SHAGEBLACKLIST, new String[0]).setComment("Blacklist for items in the matter registry. Automatic Recipe calculation will ignore recipes with these items. Just add the unlocalized name or the ore dictionary name in the list.");
        config.get(CATEGORY_SHAGE, KEY_BLACKLIST_MODS, new String[0]).setComment("Blacklist for mods (mod ID). Automatic Recipe calculation will ignore recipes with items from this mod");
        category = config.getCategory(CATEGORY_SHAGE_ITEMS);
        category.setComment("Registration of new items and the amount of matter they contain. Add them like so: I:[registered name or ore Dictionary name](meta)=[matter amount]. () - optional parameter. Example I:dye2=10 I:egg=29");
        updateCategoryLang(category);
        updateCategoryLang(category);
        category = config.getCategory(CATEGORY_CLIENT);
        updateCategoryLang(category);
        category.setComment("Options for the Matter overdrive client");
        category = config.getCategory(CATEGORY_SERVER);
        updateCategoryLang(category);
        category.setComment("Options form the Matter Overdrive server");
        category = config.getCategory(CATEGORY_ENTITIES);
        updateCategoryLang(category);
        category.setComment("Options for Matter Overdrive Entities. Such as their Entity IDs.");
        category = config.getCategory(CATEGORY_DEBUG);
        updateCategoryLang(category);
        category.setComment("Debug Options. Such as Debug Log for Matter Recipe Calculation");
        category = config.getCategory(CATEGORY_MACHINES);
        category.setComment("Machine Options.");
        updateCategoryLang(category);
        category = config.getCategory(CATEGORY_WORLD_GEN);
        category.setComment("World Generation options.");
        updateCategoryLang(category);
        category = config.getCategory(CATEGORY_COMPATIBILITY);
        category.setComment("Option for other mods");
        updateCategoryLang(category);

        config.get(CATEGORY_WORLD_GEN, CATEGORY_WORLD_SPAWN_ORES, true, "Should ores such as dilithium and tritanium ore spawn in the world. This applies for all ores !").setLanguageKey(String.format("config.%s.name", CATEGORY_WORLD_SPAWN_ORES.replace(' ', '_')));


        save();
    }

    public void postInit() {
        config.load();
        for (IConfigSubscriber subscriber : subscribers) {
            subscriber.onConfigChanged(this);
        }
        save();
    }

    private void updateCategoryLang(ConfigCategory category) {
        category.setLanguageKey(String.format("config.%s.name", category.getName().replace(' ', '_')));
    }

    public boolean getBool(String key, String category, Boolean def, String comment) {
        return config.getBoolean(key, category, def, comment);
    }

    public boolean getBool(String key, String category, Boolean def) {
        return config.getBoolean(key, category, def, null);
    }

    public int getInt(String key, String category, Integer def, String comment) {
        Property property = config.get(category, key, def);
        property.setComment(comment);
        return property.getInt(def);
    }

    public int getInt(String key, String category, Integer def) {
        return getInt(key, category, def, "");
    }

    public void setInt(String key, String category, Integer def) {
        config.get(category, key, def).set(def);
    }

    public String[] getStringList(String category, String key) {
        return config.get(category, key, new String[0]).getStringList();
    }

    public String[] getStringList(String category, String key, String comment) {
        return config.get(category, key, new String[0], comment).getStringList();
    }

    public ConfigCategory getCategory(String cat) {
        return config.getCategory(cat);
    }

    public double getMachineDouble(String machine, String prop, double def, double min, double max, String comment) {
        Property p = config.get(CATEGORY_MACHINES + "." + machine.replaceFirst("tile.", ""), prop, def);
        p.setComment(comment);
        p.setLanguageKey(String.format("%s.config.%s", machine, prop));
        p.setMinValue(min);
        p.setMaxValue(max);
        return p.getDouble(def);
    }

    public double getMachineDouble(String machine, String prop, double def, String comment) {
        Property p = config.get(String.format("%s.%s", CATEGORY_MACHINES, machine.replaceFirst("tile.", "")), prop, def);
        p.setComment(comment);
        p.setLanguageKey(String.format("%s.config.%s", machine, prop));
        return p.getDouble(def);
    }

    public boolean getMachineBool(String machine, String prop, boolean def, String comment) {
        Property p = config.get(String.format("%s.%s", CATEGORY_MACHINES, machine.replaceFirst("tile.", "")), prop, def);
        p.setComment(comment);
        p.setLanguageKey(String.format("%s.config.%s", machine, prop));
        return p.getBoolean(def);
    }

    public int getMachineInt(String machine, String prop, int def, String comment) {
        Property p = config.get(String.format("%s.%s", CATEGORY_MACHINES, machine.replaceFirst("tile.", "")), prop, def);
        p.setComment(comment);
        p.setLanguageKey(String.format("%s.config.%s", machine, prop));
        return p.getInt(def);
    }

    public void initMachineCategory(String machine) {
        config.setCategoryLanguageKey(String.format("%s.%s", CATEGORY_MACHINES, machine.replaceFirst("tile.", "")), String.format("%s.name", machine));
    }

    public void save() {
        if (config.hasChanged()) {
            config.save();
        }
    }

    public void subscribe(IConfigSubscriber subscriber) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(Reference.MOD_ID)) {
            config.save();
        }

        for (IConfigSubscriber subscriber : subscribers) {
            subscriber.onConfigChanged(this);
        }
    }

    public void addCategoryToGui(List<IConfigElement> list) {
        list.add(new ConfigElement(getCategory(ConfigurationHandler.CATEGORY_CLIENT)));
        list.add(new ConfigElement(getCategory(ConfigurationHandler.CATEGORY_SERVER)));
        list.add(new ConfigElement(getCategory(ConfigurationHandler.CATEGORY_WORLD_GEN)));
        list.add(new ConfigElement(getCategory(ConfigurationHandler.CATEGORY_MACHINES)));
        list.add(new ConfigElement(getCategory(ConfigurationHandler.CATEGORY_ENTITIES)));
        list.add(new ConfigElement(getCategory(ConfigurationHandler.CATEGORY_DEBUG)));
        list.add(new ConfigElement(getCategory(ConfigurationHandler.CATEGORY_COMPATIBILITY)));
    }
}
