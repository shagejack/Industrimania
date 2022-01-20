package shagejack.industrimania.content.pollution;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.WeatherCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import net.minecraft.world.level.material.Material;
import shagejack.industrimania.content.pollution.block.BlockAshesLayers;
import shagejack.industrimania.content.pollution.record.DecayReference;
import shagejack.industrimania.registers.ItemBlock;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.block.grouped.AllOres;
import shagejack.industrimania.registers.block.grouped.AllRocks;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Pollution {

    private static final Map<Block, DecayReference> DAMAGE_BLOCK_MAP = new HashMap<>();
    private static final Map<Block, DecayReference> ACID_RAIN_MAP = new HashMap<>();

    static {
        //COMMON DECAY

        for (Block block : BlockTags.LEAVES.getValues()) {
            DAMAGE_BLOCK_MAP.put(block, new DecayReference(Blocks.AIR, false, 500000, 1));
        }

        for (Block block : BlockTags.REPLACEABLE_PLANTS.getValues()) {
            DAMAGE_BLOCK_MAP.put(block, new DecayReference(Blocks.DEAD_BUSH, false, 500000, 1));
        }

        for (Block block : BlockTags.CORAL_PLANTS.getValues()) {
            DAMAGE_BLOCK_MAP.put(block, new DecayReference(Blocks.AIR, true, 800000,0.9));
        }

        DAMAGE_BLOCK_MAP.put(Blocks.BEE_NEST, new DecayReference(Blocks.AIR, false, 800000,0.8));
        DAMAGE_BLOCK_MAP.put(Blocks.DEAD_BUSH, new DecayReference(Blocks.AIR, false, 1500000,0.5));
        DAMAGE_BLOCK_MAP.put(Blocks.MOSS_BLOCK, new DecayReference(Blocks.AIR, false, 1500000,0.5));
        DAMAGE_BLOCK_MAP.put(Blocks.MOSSY_COBBLESTONE, new DecayReference(Blocks.COBBLESTONE, false, 1000000,0.8));
        DAMAGE_BLOCK_MAP.put(Blocks.GRASS_BLOCK, new DecayReference(Blocks.DIRT, false, 1500000,0.3));
        DAMAGE_BLOCK_MAP.put(Blocks.PODZOL, new DecayReference(Blocks.DIRT, false, 1500000,0.3));
        DAMAGE_BLOCK_MAP.put(Blocks.DIRT, new DecayReference(Blocks.COARSE_DIRT, false, 2000000,0.5));
        DAMAGE_BLOCK_MAP.put(Blocks.FARMLAND, new DecayReference(Blocks.SAND, false, 500000, 1));
        DAMAGE_BLOCK_MAP.put(Blocks.HAY_BLOCK, new DecayReference(Blocks.DIRT, false, 1500000, 0.5));
        DAMAGE_BLOCK_MAP.put(Blocks.ROOTED_DIRT, new DecayReference(Blocks.DIRT, false, 1200000, 0.8));
        DAMAGE_BLOCK_MAP.put(Blocks.DIRT_PATH, new DecayReference(Blocks.SAND, false, 500000, 1));


        //ACID RAIN

        for (Block block : BlockTags.PLANKS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 0.8));
        }

        for (Block block : BlockTags.WOODEN_BUTTONS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 1));
        }

        for (Block block : BlockTags.WOODEN_DOORS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 1));
        }

        for (Block block : BlockTags.WOODEN_FENCES.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 1));
        }

        for (Block block : BlockTags.WOODEN_SLABS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 1));
        }

        for (Block block : BlockTags.WOODEN_STAIRS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 1));
        }

        for (Block block : BlockTags.WOODEN_TRAPDOORS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 1));
        }

        for (Block block : BlockTags.WOODEN_PRESSURE_PLATES.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 1));
        }

        for (ItemBlock block: AllRocks.igneousStones) {
            ACID_RAIN_MAP.put(block.block().get(), new DecayReference(Blocks.GRAVEL, false, 5000000, 0.1));
        }

        for (ItemBlock block: AllRocks.metamorphicStones) {
            ACID_RAIN_MAP.put(block.block().get(), new DecayReference(Blocks.GRAVEL, false, 5000000, 0.1));
        }

        for (ItemBlock block: AllRocks.sedimentaryStones) {
            ACID_RAIN_MAP.put(block.block().get(), new DecayReference(Blocks.GRAVEL, false, 2000000, 0.8));
        }

        for (Block block: BlockTags.LOGS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 5000000, 0.5));
        }

        for (Block block: BlockTags.BEDS.getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.AIR, false, 2000000, 1));
        }

        ACID_RAIN_MAP.put(Blocks.LECTERN, new DecayReference(Blocks.AIR, false, 2500000, 0.5));


        AllOres.ORES.forEach((key, block) -> ACID_RAIN_MAP.put(block.block().get(), new DecayReference(Blocks.GRAVEL, false, 5000000, 0.1)));

        ACID_RAIN_MAP.put(Blocks.COBBLESTONE, new DecayReference(Blocks.GRAVEL, false, 5000000, 0.1));
        ACID_RAIN_MAP.put(Blocks.COARSE_DIRT, new DecayReference(Blocks.SAND, false, 5000000, 0.1));
        ACID_RAIN_MAP.put(Blocks.GRAVEL, new DecayReference(Blocks.SAND, false, 8000000, 0.1));
        ACID_RAIN_MAP.put(Blocks.SAND, new DecayReference(Blocks.AIR, false, 15000000, 0.1));

        ACID_RAIN_MAP.put(AllBlocks.pollution_ashes_layers.block().get(), new DecayReference(Blocks.AIR, false, 10000000, 0.1));
        ACID_RAIN_MAP.put(AllBlocks.pollution_ashes_block.block().get(), new DecayReference(Blocks.AIR, false, 50000000, 0.001));

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
        entity.hurt(DamageSource.WITHER, (float) (0.5F * Math.floor((double) this.amount / 1000000)));
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

        if (!state.isAir()) {

            if (DAMAGE_BLOCK_MAP.containsKey(state.getBlock())) {
                DecayReference ref = DAMAGE_BLOCK_MAP.get(state.getBlock());
                if (getAmount() > ref.minPollution()) {
                    if (level.getRandom().nextDouble() < ref.probability()) {
                        level.destroyBlock(pos, ref.dropItem());
                        level.setBlock(pos, ref.block().defaultBlockState(), 2 | 16);
                    }
                }
            }

            //Ashes
            if (getAmount() > 50000000) {
                if (level.getRandom().nextDouble() < Math.min((double) getAmount() / 500000000, 0.5)) {
                    if (level.canSeeSky(pos.above())) {
                        if ((AllBlocks.pollution_ashes_layers.block().get().defaultBlockState().canSurvive(level, pos.above()))) {
                            if (!level.getBlockState(pos.above()).is(AllBlocks.pollution_ashes_layers.block().get())) {
                                if (level.getBlockState(pos.above()).isAir()) {
                                    level.setBlock(pos.above(), AllBlocks.pollution_ashes_layers.block().get().defaultBlockState(), 2 | 16);
                                    addAmount(-50000);
                                }
                            } else {
                                int layers = level.getBlockState(pos.above()).getValue(BlockStateProperties.LAYERS);
                                if (layers < 8) {
                                    level.setBlock(pos.above(), AllBlocks.pollution_ashes_layers.block().get().defaultBlockState().setValue(BlockStateProperties.LAYERS, layers + 1), 2 | 16);
                                    addAmount(-50000);
                                }
                            }
                        }
                    } else if (level.getBlockState(pos.above()).is(AllBlocks.pollution_ashes_layers.block().get()) && level.getBlockState(pos.above()).getValue(BlockStateProperties.LAYERS) == 8) {
                        level.setBlock(pos.above(), AllBlocks.pollution_ashes_block.block().get().defaultBlockState(), 2 | 16);
                    } else if (level.canSeeSkyFromBelowWater(pos.above()) && level.getBlockState(pos.above()).getMaterial().isLiquid() && !level.getBlockState(pos).getMaterial().isLiquid()) {
                        if (level.getRandom().nextDouble() < Math.min((double) getAmount() / 160000000, 0.125)) {
                            level.setBlock(pos.above(), AllBlocks.pollution_ashes_block.block().get().defaultBlockState(), 2 | 16);
                            addAmount(-400000);
                        }
                    }
                }
            }

            //Acid Rain

            if (isExposedToAcidRain(level, pos)) {
                if (level.isRaining() && isAcidRain) {
                    if (ACID_RAIN_MAP.containsKey(state.getBlock())) {
                        DecayReference ref = ACID_RAIN_MAP.get(state.getBlock());
                        if (getAmount() > ref.minPollution()) {
                            if (level.getRandom().nextDouble() < ref.probability()) {
                                level.destroyBlock(pos, ref.dropItem());
                                level.setBlock(pos, ref.block().defaultBlockState(), 2 | 16);
                            }
                        }
                    } else if (getAmount() > 50000000 && level.getRandom().nextDouble() < Math.min((double) getAmount() / 100000000, 0.1)) {
                        makeFall(level, pos);
                    }
                }
            }
        }
    }

    //Falling Block Check
    public static boolean isFree(BlockState p_53242_) {
        Material material = p_53242_.getMaterial();
        return p_53242_.isAir() || p_53242_.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }

    public static void makeFall(Level level, BlockPos pos) {
        if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, level.getBlockState(pos));
            level.removeBlock(pos, true);
            level.addFreshEntity(fallingBlockEntity);
        }
    }

    public boolean isExposedToAcidRain(Level level, BlockPos pos) {
        if (level.getBlockState(pos.above()).isAir() && level.canSeeSky(pos.above())) return true;

        boolean flag = true;

        BlockPos nPos = pos.above();
        while (flag && nPos.getY() < level.getHeight()) {
            if (!level.getBlockState(nPos).isAir() && !level.getBlockState(pos.above()).is(AllBlocks.pollution_ashes_layers.block().get()) && !level.getBlockState(pos.above()).is(AllBlocks.pollution_ashes_block.block().get())) flag = false;
            nPos = nPos.above();
        }
        return flag;
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
