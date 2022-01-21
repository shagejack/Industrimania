package shagejack.industrimania.registers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import shagejack.industrimania.foundation.network.AllPackets;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.block.AllGroupedBlocks;
import shagejack.industrimania.registers.block.BlockBuilder;
import shagejack.industrimania.registers.item.AllGroupedItems;
import shagejack.industrimania.registers.item.AllItems;

import static net.minecraftforge.registries.ForgeRegistries.*;
import static shagejack.industrimania.Industrimania.MOD_ID;

public class RegisterHandle {
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(BLOCKS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTER = DeferredRegister.create(BLOCK_ENTITIES, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTER = DeferredRegister.create(CONTAINERS, MOD_ID);
    public static final DeferredRegister<Fluid> FLUID_REGISTER = DeferredRegister.create(FLUIDS, MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURE_REGISTER = DeferredRegister.create(FEATURES, MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECT_REGISTER = DeferredRegister.create(MOB_EFFECTS, MOD_ID);
    public static final DeferredRegister<Enchantment> ENCHANTMENT_REGISTER = DeferredRegister.create(ENCHANTMENTS, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REGISTER = DeferredRegister.create(ENTITIES, MOD_ID);


    public static void init() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        new AllItems();
        new AllBlocks();
        bus.addListener((FMLClientSetupEvent event) -> BlockBuilder.setupRenderLayerTasks.forEach((task) -> task.get().run()));
        AllGroupedBlocks.initAll();
        AllGroupedItems.initAll();
        bus.addListener((FMLClientSetupEvent event) -> AllTileEntities.TileEntityBuilder.bind(event));
        new AllTileEntities();
        bus.addGenericListener(Block.class,(RegistryEvent<Block> event) -> new AllFeatures());
        AllPackets.registerPackets();
    }

    public static void RegRegisters() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEM_REGISTER.register(bus);
        BLOCK_REGISTER.register(bus);
        BLOCK_ENTITY_TYPE_REGISTER.register(bus);
        MENU_TYPE_REGISTER.register(bus);
        FLUID_REGISTER.register(bus);
        FEATURE_REGISTER.register(bus);
        MOB_EFFECT_REGISTER.register(bus);
        ENCHANTMENT_REGISTER.register(bus);
        ENTITY_TYPE_REGISTER.register(bus);
    }
}
