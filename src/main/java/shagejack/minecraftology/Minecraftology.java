package shagejack.minecraftology;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import shagejack.minecraftology.handler.ConfigurationHandler;
import shagejack.minecraftology.handler.GuiHandler;
import shagejack.minecraftology.handler.TickHandler;
import shagejack.minecraftology.init.BlocksMCL;
import shagejack.minecraftology.init.CapabilitiesMCL;
import shagejack.minecraftology.init.ItemsMCL;
import shagejack.minecraftology.network.PacketPipeline;
import shagejack.minecraftology.proxy.CommonProxy;

import java.io.File;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION,
        dependencies = Reference.DEPENDENCIES,
        guiFactory = Reference.GUI_FACTORY_CLASS
)
public class Minecraftology {

    public static final ItemsMCL ITEMS = new ItemsMCL();
    public static final BlocksMCL BLOCKS = new BlocksMCL();

    public static final MCLTab TAB_Minecraftology = new MCLTab("tabMCL", () -> new ItemStack(ITEMS.multimeter));
    public static final TickHandler TICK_HANDLER;
    public static final GuiHandler GUI_HANDLER;
    public static final ConfigurationHandler CONFIG_HANDLER;

    //public static final MineRegistry MINE_REGISTRY;
    public static final PacketPipeline NETWORK;
    @SidedProxy(clientSide = "shagejack.minecraftology.proxy.ClientProxy", serverSide = "shagejack.minecraftology.proxy.CommonProxy")
    public static CommonProxy PROXY;

    static {
        CONFIG_HANDLER = new ConfigurationHandler(new File("config"));
        NETWORK = new PacketPipeline();
        TICK_HANDLER = new TickHandler();
        GUI_HANDLER = new GuiHandler();
    }

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(Reference.MOD_ID)
    public static Minecraftology INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(TICK_HANDLER);

        ITEMS.init();
        BLOCKS.init();

        CapabilitiesMCL.init();

        NETWORK.registerPackets();

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
