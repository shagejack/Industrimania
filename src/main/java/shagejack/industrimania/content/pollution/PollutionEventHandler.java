package shagejack.industrimania.content.pollution;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PollutionEventHandler {

    private static final int IDLE = 1200;
    private static final long SPREAD_LIMIT = 400000;
    private static final long DECAY_LIMIT = 1000000;
    private static final long ACID_RAIN_LIMIT = 2000000;
    private static final long DECAY_DIVIDEND = 25000;
    private static final int DECAY_SUBTRAHEND= 20;

    private static int tickCounter;

    @SubscribeEvent
    public static void tickPollution(TickEvent.WorldTickEvent event) {
        if (tickCounter == IDLE) {
            //Tick Polluted Chunks
            PollutionDataHooks.pollutionMap.forEach( (chunkRef, pollution) -> {
                if (event.world.dimensionType() == chunkRef.dimension()) {
                    if (pollution.amount != 0) {
                        DimensionType dim = chunkRef.dimension();
                        int chunkX = chunkRef.pos().x;
                        int chunkZ = chunkRef.pos().z;
                        AABB chunkRange = new AABB(chunkRef.pos().getMinBlockX(), chunkRef.dimension().minY(), chunkRef.pos().getMinBlockZ(), chunkRef.pos().getMaxBlockX(), chunkRef.dimension().height(), chunkRef.pos().getMaxBlockZ());
                        //Reduce Pollution
                        pollution.reducePollution();
                        //Manage Spread
                        if (pollution.getAmount() > SPREAD_LIMIT) {
                            pollution.spread(PollutionDataHooks.getPollution(dim, chunkX + 1, chunkZ));
                            pollution.spread(PollutionDataHooks.getPollution(dim, chunkX, chunkZ + 1));
                            pollution.spread(PollutionDataHooks.getPollution(dim, chunkX - 1, chunkZ));
                            pollution.spread(PollutionDataHooks.getPollution(dim, chunkX, chunkZ - 1));
                        }
                        //Damage Blocks
                        if (pollution.getAmount() > DECAY_LIMIT) {
                            for (int i = DECAY_SUBTRAHEND; i < pollution.getAmount() / DECAY_DIVIDEND; i++) {
                                int x = chunkRef.pos().getMinBlockX() + event.world.getRandom().nextInt(16);
                                int y = chunkRef.dimension().minY() + event.world.getRandom().nextInt(chunkRef.dimension().height());
                                int z = chunkRef.pos().getMinBlockZ() + event.world.getRandom().nextInt(16);
                                pollution.damageBlock(event.world, new BlockPos(x, y, z), pollution.getAmount() > ACID_RAIN_LIMIT);
                            }
                        }
                        //Manage Entities
                        for (Entity entity : event.world.getEntities((Entity) null, chunkRange, Entity::isAlive)) {
                            if (entity.chunkPosition() == chunkRef.pos()) {
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
        if (event.getWorld() != null) {
            PollutionDataHooks.readData(event.getWorld().dimensionType(), event.getChunk().getPos().x, event.getChunk().getPos().z, event.getData());
        }
    }

    @SubscribeEvent
    public static void chunkSave(ChunkDataEvent.Save event) {
        if (event.getWorld() != null) {
            PollutionDataHooks.saveData(event.getWorld().dimensionType(), event.getChunk().getPos().x, event.getChunk().getPos().z, event.getData());
        }
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkEvent.Unload event) {
        if (event.getWorld() != null) {
            PollutionDataHooks.clearData(event.getWorld().dimensionType(), event.getChunk().getPos().x, event.getChunk().getPos().z);
        }
    }
}
