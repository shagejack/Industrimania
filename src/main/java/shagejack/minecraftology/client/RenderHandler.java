package shagejack.minecraftology.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;
import shagejack.minecraftology.api.internal.ItemModelProvider;
import shagejack.minecraftology.client.render.tileentity.*;
import shagejack.minecraftology.init.BlocksMCL;
import shagejack.minecraftology.init.ItemsMCL;
import shagejack.minecraftology.tile.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
        for (Item item : ItemsMCL.items) {
            if (item instanceof ItemModelProvider)
                ((ItemModelProvider) item).initItemModel();
        }
        for (Block block : BlocksMCL.blocks) {
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