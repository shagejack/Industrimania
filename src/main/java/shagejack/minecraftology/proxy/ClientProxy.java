package shagejack.minecraftology.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.Reference;
import shagejack.minecraftology.client.RenderHandler;
import shagejack.minecraftology.client.model.MCLModelLoader;
import shagejack.minecraftology.handler.KeyHandler;
import shagejack.minecraftology.handler.MouseHandler;
import shagejack.minecraftology.handler.thread.RegistryToast;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    public static RenderHandler renderHandler;
    public static KeyHandler keyHandler;
    public static MouseHandler mouseHandler;
    //public static HoloIcons holoIcons;
    public static FontRenderer moFontRender;
    private MCLModelLoader modelLoader;

    public ClientProxy() {
    }

    public static ClientProxy instance() {
        if (Minecraftology.PROXY instanceof ClientProxy)
            return (ClientProxy) Minecraftology.PROXY;
        else if (Minecraftology.PROXY == null)
            throw new UnsupportedOperationException("Attempted to access ClientProxy without it being initialized");
        throw new UnsupportedOperationException("Attempted to access ClientProxy on server side");
    }

    private void registerSubscribtions() {
        MinecraftForge.EVENT_BUS.register(keyHandler);
        MinecraftForge.EVENT_BUS.register(mouseHandler);
        //MinecraftForge.EVENT_BUS.register(new TooltipHandler());
        //MinecraftForge.EVENT_BUS.register(holoIcons);
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
        modelLoader = new MCLModelLoader();
        ModelLoaderRegistry.registerLoader(modelLoader);

        renderHandler = new RenderHandler();
        //renderHandler.registerEntityRenderers();
        //renderHandler.createItemRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        //renderHandler.init(Minecraft.getMinecraft().world, Minecraft.getMinecraft().getTextureManager());
        //renderHandler.createEntityRenderers(Minecraft.getMinecraft().getRenderManager());

        keyHandler = new KeyHandler();
        mouseHandler = new MouseHandler();
        //holoIcons = new HoloIcons();

        registerSubscribtions();


        //renderHandler.createBlockRenderers();
        //renderHandler.createTileEntityRenderers(MatterOverdrive.CONFIG_HANDLER);
        //renderHandler.createBioticStatRenderers();
        //renderHandler.createStarmapRenderers();
        //renderHandler.createModels();


        //renderHandler.registerWeaponLayers();
        //renderHandler.registerTileEntitySpecialRenderers();
        //renderHandler.registerBlockColors();

        //renderHandler.registerItemColors();
        //renderHandler.registerBioticStatRenderers();
        //renderHandler.registerBionicPartRenderers();
        //renderHandler.registerStarmapRenderers();

        moFontRender = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation(Reference.MOD_ID, "textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(moFontRender);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }


    @Override
    public boolean hasTranslation(String key) {
        return I18n.hasKey(key);
    }

    @Override
    public String translateToLocal(String key, Object... params) {
        return I18n.format(key, params);
    }

   // @Override
    //public void matterToast(boolean b, long l) {
     //   Minecraft.getMinecraft().getToastGui().add(new RegistryToast(b, l));
    //}
}
