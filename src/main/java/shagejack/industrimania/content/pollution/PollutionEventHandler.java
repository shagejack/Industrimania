package shagejack.industrimania.content.pollution;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public class PollutionEventHandler {

    private static final int IDLE = 1200;
    private static final long SPREAD_LIMIT = 400000;
    private static final long PARTICLE_LIMIT = 750000;
    private static final long EFFECT_LIMIT = 750000;
    private static final long DAMAGE_LIMIT = 1000000;
    private static final long DECAY_LIMIT = 1000000;
    private static final long ACID_RAIN_LIMIT = 2000000;
    private static final long DECAY_DIVIDEND = 25000;
    private static final long FOG_EFFECT_LIMIT = 500000;
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
                        //TODO: Render Particles
                        if (pollution.getAmount() > PARTICLE_LIMIT) {
                            //event.world.addParticle(ParticleTypes.SMOKE, chunkRef.pos().getMinBlockX(), chunkRef.dimension().minY(), chunkRef.pos().getMinBlockZ(), chunkRef.pos().getMaxBlockX(), Math.min(chunkRef.dimension().height(), chunkRef.dimension().minY() + pollution.getAmount() / 10000), chunkRef.pos().getMaxBlockZ());
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
                            if (entity.chunkPosition().equals(chunkRef.pos())) {
                                if (entity instanceof LivingEntity) {
                                    if (pollution.getAmount() > DAMAGE_LIMIT) {
                                        pollution.damageEntity((LivingEntity) entity);
                                    }
                                    if (pollution.getAmount() > EFFECT_LIMIT) {
                                        pollution.giveEffect(event.world, (LivingEntity) entity);
                                    }
                                    if (pollution.getAmount() > FOG_EFFECT_LIMIT) {
                                        pollution.giveFogEffect(event.world, (LivingEntity) entity);
                                    }
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

    @SubscribeEvent
    public static void onFogColorRender(EntityViewRenderEvent.FogColors event) {
        PollutionDataHooks.pollutionMap.forEach( (chunkRef, pollution) -> {
            if (pollution.amount != 0) {
                Entity entity = event.getCamera().getEntity();
                if (entity instanceof Player) {
                    if (entity.level.dimensionType() == chunkRef.dimension()) {
                        if (entity.chunkPosition().equals(chunkRef.pos())) {
                            if (entity.chunkPosition().equals(chunkRef.pos())) {
                                Color color = pollution.getFogColor();
                                event.setRed((float) color.getRed() / 255F);
                                event.setBlue((float) color.getBlue() / 255F);
                                event.setGreen((float) color.getGreen() / 255F);
                            }
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onFogDensityRender(EntityViewRenderEvent.FogDensity event) {
        PollutionDataHooks.pollutionMap.forEach( (chunkRef, pollution) -> {
            if (pollution.amount != 0) {
                Entity entity = event.getCamera().getEntity();
                if (entity instanceof Player) {
                    if (entity.level.dimensionType() == chunkRef.dimension()) {
                        if (entity.chunkPosition().equals(chunkRef.pos())) {
                            if (entity.chunkPosition().equals(chunkRef.pos())) {
                                event.setDensity(pollution.getFogDensity());
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        });
    }

}
