package shagejack.shagecraft.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import shagejack.shagecraft.api.internal.ItemModelProvider;
import shagejack.shagecraft.client.render.tileentity.*;
import shagejack.shagecraft.init.ShagecraftBlocks;
import shagejack.shagecraft.init.ShagecraftItems;
import shagejack.shagecraft.tile.*;

import javax.annotation.Nullable;

public class RenderHandler {
    public static final Function<ResourceLocation, TextureAtlasSprite> modelTextureBakeFunc = new Function<ResourceLocation, TextureAtlasSprite>() {
        @Nullable
        @Override
        public TextureAtlasSprite apply(@Nullable ResourceLocation input) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
        }
    };

    public RenderHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private TileEntityRendererForge tileEntityRendererForge;
    private TileEntityRendererFilingTable tileEntityRendererFilingTable;
    private TileEntityRendererSawTable tileEntityRendererSawTable;
    private TileEntityRendererConcreteMixer tileEntityRendererConcreteMixer;
    private TileEntityRendererGlassMould tileEntityRendererGlassMould;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
    }

    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
    }

    @SubscribeEvent
    public void modelLoadEvent(ModelRegistryEvent event) {
        for (Item item : ShagecraftItems.items) {
            if (item instanceof ItemModelProvider)
                ((ItemModelProvider) item).initItemModel();
        }
        for (Block block : ShagecraftBlocks.blocks) {
            if (block instanceof ItemModelProvider)
                ((ItemModelProvider) block).initItemModel();
            else
                ClientUtil.registerWithMapper(block);
        }
    }

    public void createTileEntityRenderers() {
        tileEntityRendererForge = new TileEntityRendererForge();
        tileEntityRendererFilingTable = new TileEntityRendererFilingTable();
        tileEntityRendererSawTable = new TileEntityRendererSawTable();
        tileEntityRendererConcreteMixer = new TileEntityRendererConcreteMixer();
        tileEntityRendererGlassMould = new TileEntityRendererGlassMould();
    }

    public void registerTileEntitySpecialRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForge.class, tileEntityRendererForge);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFilingTable.class, tileEntityRendererFilingTable);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySawTable.class, tileEntityRendererSawTable);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConcreteMixer.class, tileEntityRendererConcreteMixer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGlassMould.class, tileEntityRendererGlassMould);
    }

}