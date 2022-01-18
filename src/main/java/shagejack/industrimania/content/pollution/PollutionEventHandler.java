package shagejack.industrimania.content.pollution;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PollutionEventHandler {

    private static final int IDLE = 1200;

    private static int tickCounter;

    @SubscribeEvent
    public static void tickPollution(TickEvent.WorldTickEvent event) {
        if (tickCounter == IDLE) {
            //Tick Polluted Chunks
            PollutionDataHooks.pollutionMap.forEach( (chunk, pollution) -> {
                if (event.world == chunk.getLevel()) {
                    if (pollution.amount != 0) {
                        AABB chunkRange = new AABB(chunk.getPos().getMinBlockX(), chunk.getMinBuildHeight(), chunk.getPos().getMinBlockZ(), chunk.getPos().getMaxBlockX(), chunk.getMaxBuildHeight(), chunk.getPos().getMaxBlockZ());
                        //Reduce Pollution
                        pollution.reducePollution();
                        //Manage Spread
                        Level level = chunk.getLevel();
                        int chunkX = chunk.getPos().x;
                        int chunkZ = chunk.getPos().z;
                        pollution.spread(PollutionDataHooks.getPollution(level, chunkX + 1, chunkZ));
                        pollution.spread(PollutionDataHooks.getPollution(level, chunkX, chunkZ + 1));
                        pollution.spread(PollutionDataHooks.getPollution(level, chunkX - 1, chunkZ));
                        pollution.spread(PollutionDataHooks.getPollution(level, chunkX, chunkZ - 1));
                        //Manage Entities
                        for (Entity entity : event.world.getEntities((Entity) null, chunkRange, Entity::isAlive)) {
                            if (entity.chunkPosition() == chunk.getPos()) {
                                if (entity instanceof LivingEntity) {
                                    pollution.damageEntity((LivingEntity) entity);
                                }
                            }
                        }
                    }
                }
            });
            tickCounter = 0;
        } else {
            tickCounter++;
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkDataEvent.Load event) {
        PollutionDataHooks.readData((Level) event.getWorld(), event.getChunk().getPos().x, event.getChunk().getPos().z, event.getData());
    }

    @SubscribeEvent
    public static void chunkSave(ChunkDataEvent.Save event) {
        PollutionDataHooks.saveData((Level) event.getWorld(), event.getChunk().getPos().x, event.getChunk().getPos().z, event.getData());
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkEvent.Unload event) {
        PollutionDataHooks.clearData((Level) event.getWorld(), event.getChunk().getPos().x, event.getChunk().getPos().z);
    }
}
