package shagejack.shagecraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import shagejack.shagecraft.handler.ConfigurationHandler;
import shagejack.shagecraft.handler.GuiHandler;
import shagejack.shagecraft.handler.TickHandler;
import shagejack.shagecraft.handler.steam_network.SteamNetworkHandler;
import shagejack.shagecraft.init.ShagecraftBlocks;
import shagejack.shagecraft.init.ShagecraftCapabilities;
import shagejack.shagecraft.init.ShagecraftFluids;
import shagejack.shagecraft.init.ShagecraftItems;
import shagejack.shagecraft.network.PacketPipeline;
import shagejack.shagecraft.proxy.CommonProxy;

import java.io.File;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION,
        dependencies = Reference.DEPENDENCIES,
        guiFactory = Reference.GUI_FACTORY_CLASS
)
public class Shagecraft {

    public static final ShagecraftItems ITEMS = new ShagecraftItems();
    public static final ShagecraftBlocks BLOCKS = new ShagecraftBlocks();

    public static final ShageTab TAB_Shagecraft = new ShageTab("tabShage", () -> new ItemStack(ITEMS.multimeter));
    public static final TickHandler TICK_HANDLER;
    public static final GuiHandler GUI_HANDLER;
    public static final ConfigurationHandler CONFIG_HANDLER;
    public static final SteamNetworkHandler STEAM_NETWORK_HANDLER;

    //public static final MineRegistry MINE_REGISTRY;
    public static final PacketPipeline NETWORK;
    @SidedProxy(clientSide = "shagejack.shagecraft.proxy.ClientProxy", serverSide = "shagejack.shagecraft.proxy.CommonProxy")
    public static CommonProxy PROXY;

    static {
        FluidRegistry.enableUniversalBucket();
        CONFIG_HANDLER = new ConfigurationHandler(new File("config"));
        NETWORK = new PacketPipeline();
        TICK_HANDLER = new TickHandler();
        GUI_HANDLER = new GuiHandler();
        STEAM_NETWORK_HANDLER = new SteamNetworkHandler();
    }

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(Reference.MOD_ID)
    public static Shagecraft INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(TICK_HANDLER);

        ITEMS.init();
        ShagecraftFluids.init(event);
        BLOCKS.init();

        ShagecraftCapabilities.init();

        NETWORK.registerPackets();

        MinecraftForge.EVENT_BUS.register(STEAM_NETWORK_HANDLER);

        PROXY.preInit(event);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GUI_HANDLER.register(event.getSide());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, GUI_HANDLER);
        CONFIG_HANDLER.init();


        PROXY.init(event);
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);

        CONFIG_HANDLER.postInit();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartedEvent event) {
        TICK_HANDLER.onServerStart(event);
    }

}
