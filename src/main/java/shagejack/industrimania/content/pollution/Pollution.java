package shagejack.industrimania.content.pollution;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.content.pollution.record.DecayReference;
import shagejack.industrimania.content.worldGen.RockRegistry;
import shagejack.industrimania.registers.block.grouped.AllRocks;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Pollution {

    private static final Map<Block, DecayReference> DAMAGE_BLOCK_MAP = new HashMap<>();
    private static final Map<Block, DecayReference> ACID_RAIN_MAP = new HashMap<>();

    static {
        //COMMON DECAY

        for (Block block : BlockTags.LEAVES.getValues()) {
            DAMAGE_BLOCK_MAP.put(block, new DecayReference(Blocks.AIR, false));
        }

        for (Block block : BlockTags.REPLACEABLE_PLANTS.getValues()) {
            DAMAGE_BLOCK_MAP.put(block, new DecayReference(Blocks.AIR, false));
        }

        for (Block block : BlockTags.CORAL_PLANTS.getValues()) {
            DAMAGE_BLOCK_MAP.put(block, new DecayReference(Blocks.AIR, true));
        }

        DAMAGE_BLOCK_MAP.put(Blocks.MOSS_BLOCK, new DecayReference(Blocks.AIR, false));
        DAMAGE_BLOCK_MAP.put(Blocks.MOSSY_COBBLESTONE, new DecayReference(Blocks.COBBLESTONE, false));
        DAMAGE_BLOCK_MAP.put(Blocks.GRASS_BLOCK, new DecayReference(Blocks.DIRT, false));
        DAMAGE_BLOCK_MAP.put(Blocks.PODZOL, new DecayReference(Blocks.DIRT, false));
        DAMAGE_BLOCK_MAP.put(Blocks.DIRT, new DecayReference(Blocks.COARSE_DIRT, false));
        DAMAGE_BLOCK_MAP.put(Blocks.FARMLAND, new DecayReference(Blocks.SAND, false));


        //ACID RAIN

        for (Block block: RockRegistry.igneousStones) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.GRAVEL, false));
        }

        for (Block block: RockRegistry.metamorphicStones) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.GRAVEL, false));
        }

        for (Block block: RockRegistry.sedimentaryStones) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.GRAVEL, false));
        }

        for (Block block: BlockTags.LOGS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.GRAVEL, false));
        }


        AllRocks.ORES.forEach((key, block) -> ACID_RAIN_MAP.put(block.block().get(), new DecayReference(Blocks.GRAVEL, false)));

        ACID_RAIN_MAP.put(Blocks.COARSE_DIRT, new DecayReference(Blocks.SAND, false));
        ACID_RAIN_MAP.put(Blocks.GRAVEL, new DecayReference(Blocks.SAND, false));
        ACID_RAIN_MAP.put(Blocks.SAND, new DecayReference(Blocks.AIR, false));

    }

    public long amount;

    public Pollution(long amount) {
        this.amount = amount;
    }

    public Pollution() {
        this(0);
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void addAmount(long amount) {
        this.amount += amount;
    }

    public void setAmountFromTag(CompoundTag tag) {
        setAmount(tag.getLong("amount"));
    }

    public void reducePollution() {
        double REDUCE_FACTOR = 0.995;
        if (getAmount() != getAmount() * REDUCE_FACTOR) {
            setAmount((long) (getAmount() * REDUCE_FACTOR));
        } else if (getAmount() > 0) {
            setAmount(getAmount() - 1);
        }
    }

    public CompoundTag getTag() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("amount", amount);
        return tag;
    }

    //Spread
    public void spread(Pollution nearbyPollution) {
        if (nearbyPollution.getAmount() * 6 < this.amount * 5) {
            long diff = this.amount - nearbyPollution.getAmount();
            diff = diff / 20;
            this.addAmount(-diff);
            nearbyPollution.addAmount(diff);
        }
    }

    //Effects
    public Color getFogColor() {
        return new Color((200F - Math.min(200F, (float) this.amount / 500000F)) / 255F, (200F - Math.min(200F, (float) this.amount / 500000F)) / 255F, (255F - Math.min(255F, (float) this.amount / 10000F)) / 255F);
    }

    public float getFogDensity() {
        return Math.max(5F, 50000000F / (float) this.amount);
    }

    public void damageEntity(LivingEntity entity) {
        entity.hurt(DamageSource.WITHER, (float) (0.5 * Math.floor((double) this.amount / 1000000)));
    }

    public void giveEffect(Level level, LivingEntity entity) {
            int duration = Math.min(600, (int) this.amount / 75000) * 20;
            int amplifier = Math.min(64, (int) this.amount / 3000000);
        entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, duration, amplifier, false, true));
        switch (level.getRandom().nextInt(4)) {
            case 0 -> entity.addEffect(new MobEffectInstance(MobEffects.POISON, duration, amplifier, false, true));
            case 1 -> entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, amplifier, false, true));
            case 2 -> entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration, amplifier, false, true));
            case 3 -> {}
            default -> {
            }
        }
    }

    public void damageBlock(Level level, BlockPos pos, Boolean isAcidRain) {

        BlockState state = level.getBlockState(pos);

        if (DAMAGE_BLOCK_MAP.containsKey(state.getBlock())) {
            DecayReference ref = DAMAGE_BLOCK_MAP.get(state.getBlock());
            if (ref.block() == Blocks.AIR) {
                level.destroyBlock(pos, ref.dropItem());
            } else {
                level.setBlock(pos, ref.block().defaultBlockState(), 2 | 16);
            }
        }

        if (level.isRaining() && isAcidRain) {

            if (level.getBlockState(pos.above()).isAir() && level.canSeeSky(pos)) {
                if (level.canSeeSky(pos)) {
                    if (ACID_RAIN_MAP.containsKey(state.getBlock())) {
                        DecayReference ref = ACID_RAIN_MAP.get(state.getBlock());
                        if (ref.block() == Blocks.AIR) {
                            level.destroyBlock(pos, ref.dropItem());
                        } else {
                            level.setBlock(pos, ref.block().defaultBlockState(), 2 | 16);
                        }
                    }
                }
            }
        }
    }


    public void giveFogEffect(Level level, LivingEntity entity) {
        int duration = Math.min(600, (int) this.amount / 75000) * 20;
        int amplifier = Math.min(64, (int) this.amount / 1500000);
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration, amplifier, false, true));
        switch (level.getRandom().nextInt(3)) {
            case 0 -> entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, amplifier, false, true));
            case 1 -> entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, amplifier, false, true));
            case 2 -> {}
            default -> {
            }
        }
    }
}