package shagejack.shagecraft.init;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shagejack.shagecraft.api.internal.IItemBlockFactory;
import shagejack.shagecraft.api.internal.OreDictItem;
import shagejack.shagecraft.blocks.*;
import shagejack.shagecraft.util.LogShage;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber

public class ShagecraftBlocks {
        public static List<Block> blocks = new ArrayList<>();
        public static List<Item> items = new ArrayList<>();

        //Building
        public BlockBuilding building_fine_clay;
        public BlockBuilding building_scorched_clay;

        //Gravity
        public BlockGravity gravity_iron_oxide;
        public BlockGravityCharcoal gravity_charcoal;
        public BlockGravity gravity_calcite;
        public BlockGravity gravity_dust;

        //Mechanic
        public BlockClayFurnaceBottom mechanic_clay_furnace_bottom;
        public BlockBronzeTube mechanic_bronze_tube;
        public BlockBlower mechanic_blower;
        public BlockIronOreSlag mechanic_iron_ore_slag;
        public BlockForge mechanic_forge;
        public BlockForgeFurnace mechanic_forge_furnace;
        public BlockForgeFurnaceLit mechanic_forge_furnace_lit;
        public BlockWaterPool mechanic_water_pool;
        public BlockFilingTable mechanic_filing_table;
        public BlockSawTable mechanic_saw_table;
        public BlockConcreteMixer mechanic_concrete_mixer;
        public BlockGlassMeltingFurnace mechanic_glass_melting_furnace;
        public BlockGlassMeltingFurnaceInput mechanic_glass_melting_furnace_input;
        public BlockGlassMeltingFurnaceOutput mechanic_glass_melting_furnace_output;
        public BlockGlassMould mechanic_glass_mould;

        //Steam
        public BlockBoiler steam_boiler;
        public BlockBoilerHeater steam_boiler_heater;
        public BlockSteamForgeHammer steam_forge_hammer;
        public BlockSteamPipe steam_steam_pipe;

        private int registeredCount = 0;

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            blocks.forEach(b -> event.getRegistry().register(b));
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            items.forEach(i -> event.getRegistry().register(i));
            ShagecraftItems.items.stream().filter(item -> item instanceof OreDictItem).forEach(item -> ((OreDictItem) item).registerOreDict());
            ShagecraftBlocks.blocks.stream().filter(block -> block instanceof OreDictItem).forEach(block -> ((OreDictItem) block).registerOreDict());
        }

        public void init() {

            LogShage.info("Registering blocks");

            //Building
            building_fine_clay = register(new BlockBuilding(Material.CLAY, "building.fine_clay", 4, 0, 4, SoundType.GROUND));
            building_scorched_clay = register(new BlockBuilding(Material.CLAY, "building.scorched_clay", 4, 0, 4, SoundType.GROUND));

            //Gravity
            gravity_iron_oxide = register(new BlockGravity(Material.SAND, "gravity.iron_oxide", 4, 1, 4));
            gravity_charcoal = register(new BlockGravityCharcoal(Material.SAND, "gravity.charcoal"));
            gravity_calcite = register(new BlockGravity(Material.SAND, "gravity.calcite", 4, 0, 4));
            gravity_dust = register(new BlockGravity(Material.SAND, "gravity.dust", 4, 0, 4));

            //Mechanic
            mechanic_clay_furnace_bottom = register(new BlockClayFurnaceBottom(Material.CLAY, "mechanic.clay_furnace_bottom"));
            mechanic_bronze_tube = register(new BlockBronzeTube(Material.CLAY, "mechanic.bronze_tube"));
            mechanic_blower = register(new BlockBlower(Material.ROCK, "mechanic.blower"));
            mechanic_iron_ore_slag = register(new BlockIronOreSlag(Material.SAND, "mechanic.iron_ore_slag"));
            mechanic_forge = register(new BlockForge(Material.ANVIL, "mechanic.forge"));
            mechanic_forge_furnace = register(new BlockForgeFurnace(Material.ROCK, "mechanic.forge_furnace"));
            mechanic_forge_furnace_lit = register(new BlockForgeFurnaceLit(Material.ROCK, "mechanic.forge_furnace_lit"));
            mechanic_water_pool = register(new BlockWaterPool(Material.ROCK, "mechanic.water_pool"));
            mechanic_filing_table = register(new BlockFilingTable(Material.ROCK, "mechanic.filing_table"));
            mechanic_saw_table = register(new BlockSawTable(Material.ROCK, "mechanic.saw_table"));
            mechanic_concrete_mixer = register(new BlockConcreteMixer(Material.ROCK, "mechanic.concrete_mixer"));
            mechanic_glass_melting_furnace = register(new BlockGlassMeltingFurnace(Material.ROCK, "mechanic.glass_melting_furnace"));
            mechanic_glass_melting_furnace_input = register(new BlockGlassMeltingFurnaceInput(Material.ROCK, "mechanic.glass_melting_furnace_input"));
            mechanic_glass_melting_furnace_output = register(new BlockGlassMeltingFurnaceOutput(Material.ROCK, "mechanic.glass_melting_furnace_output"));
            mechanic_glass_mould = register(new BlockGlassMould(Material.ROCK, "mechanic.glass_mould"));


            //Steam
            steam_boiler = register(new BlockBoiler(Material.ROCK, "steam.boiler"));
            steam_boiler_heater = register(new BlockBoilerHeater(Material.ROCK, "steam.boiler_heater"));
            steam_forge_hammer = register(new BlockSteamForgeHammer(Material.ROCK, "steam.forge_hammer"));
            steam_steam_pipe = register(new BlockSteamPipe(Material.ROCK, "steam.steam_pipe"));

            LogShage.info("Finished registering blocks");
            LogShage.info("Registered %d blocks", registeredCount);

        }

        protected <T extends Block> T register(T block) {
            ItemBlock itemBlock;
            if (block instanceof IItemBlockFactory) {
                itemBlock = ((IItemBlockFactory) block).createItemBlock();
           //} else if (block instanceof MCLBlockTileEntity) {
           //     itemBlock = new MCLBlockTileEntityItem(block);
            } else {
                itemBlock = new ItemBlock(block);
                itemBlock.setRegistryName(block.getRegistryName());
            }
            return register(block, itemBlock);
        }

        protected <T extends Block> T register(T block, ItemBlock itemBlock) {
            //if (block instanceof IConfigSubscriber) {
            //    Minecraftology.CONFIG_HANDLER.subscribe((IConfigSubscriber) block);
            //}
            registeredCount++;
            blocks.add(block);
            items.add(itemBlock);
            return block;
        }

    }