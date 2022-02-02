package shagejack.industrimania.foundation.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import shagejack.industrimania.registers.block.AllBlocks;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DynamicLights {

    private static ConcurrentHashMap<Level, ConcurrentLinkedQueue<Pair<BlockPos, Integer>>> levelLightsMap;

    public static boolean isLit(Level level, BlockPos pos) {
        ConcurrentLinkedQueue<Pair<BlockPos, Integer>> levelLights = levelLightsMap.get(level);
        if (levelLights != null) {
            return levelLights.stream().anyMatch(pair -> pair.getFirst() == pos);
        }
        return false;
    }

    public static void addLight(Level level, BlockPos pos, int lightLevel) {
        ConcurrentLinkedQueue<Pair<BlockPos, Integer>> levelLights = levelLightsMap.get(level);

        if (levelLights == null) {
            levelLights = new ConcurrentLinkedQueue<>();
        }

        if (levelLights.stream().anyMatch(pair -> pair.getFirst() == pos && pair.getSecond() >= lightLevel)) {
            return;
        }

        levelLights.add(Pair.of(pos, lightLevel));

        levelLightsMap.put(level, levelLights);
    }

    public static void removeLight(Level level, BlockPos pos) {
        ConcurrentLinkedQueue<Pair<BlockPos, Integer>> levelLights = levelLightsMap.get(level);

        if (levelLights == null) {
            return;
        }

        levelLights.stream().filter(pair -> pair.getFirst() == pos).forEach(pair -> {
            if (level.getBlockState(pair.getFirst()).is(AllBlocks.mechanic_fake_air_light.block().get()))
                level.removeBlock(pair.getFirst(), false);
        });

        levelLights.removeIf(pair -> pair.getFirst() == pos);

        levelLightsMap.put(level, levelLights);
    }

    @SubscribeEvent
    public void serverWorldTick(TickEvent.WorldTickEvent event) {

        if (event.side != LogicalSide.SERVER) {
            return;
        }

        ConcurrentLinkedQueue<Pair<BlockPos, Integer>> levelLights = levelLightsMap.get(event.world);
        if (levelLights != null) {
            for (Pair<BlockPos, Integer> pair : levelLights) {
                if (event.world.getBlockState(pair.getFirst()).isAir() && (!event.world.getBlockState(pair.getFirst()).is(AllBlocks.mechanic_fake_air_light.block().get()) || event.world.getBlockState(pair.getFirst()).getValue(BlockStateProperties.LEVEL) < pair.getSecond()))
                    event.world.setBlock(pair.getFirst(), event.world.getBlockState(pair.getFirst()).setValue(BlockStateProperties.LEVEL, pair.getSecond()), 3);
            }
        }
    }

}
