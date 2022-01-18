package shagejack.industrimania.content.pollution;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.content.pollution.record.DecayReference;
import shagejack.industrimania.registers.AllTags;

import java.util.HashMap;
import java.util.List;
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

        for (Block block : BlockTags.MOSS_REPLACEABLE.getValues()) {
            DAMAGE_BLOCK_MAP.put(block, new DecayReference(Blocks.AIR, true));
        }

        DAMAGE_BLOCK_MAP.put(Blocks.MOSS_BLOCK, new DecayReference(Blocks.AIR, false));
        DAMAGE_BLOCK_MAP.put(Blocks.MOSSY_COBBLESTONE, new DecayReference(Blocks.COBBLESTONE, false));
        DAMAGE_BLOCK_MAP.put(Blocks.GRASS, new DecayReference(Blocks.COARSE_DIRT, false));
        DAMAGE_BLOCK_MAP.put(Blocks.FARMLAND, new DecayReference(Blocks.SAND, false));


        //ACID RAIN

        for (Block block: BlockTags.bind(AllTags.IndustrimaniaTags.igneousStones).getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.GRAVEL, false));
        }

        for (Block block: BlockTags.bind(AllTags.IndustrimaniaTags.metamorphicStones).getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.GRAVEL, false));
        }

        for (Block block: BlockTags.bind(AllTags.IndustrimaniaTags.sedimentaryStones).getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.GRAVEL, false));
        }

        for (Block block: BlockTags.bind(AllTags.IndustrimaniaTags.ore).getValues()) {
            ACID_RAIN_MAP.put(block, new DecayReference(Blocks.GRAVEL, false));
        }

        ACID_RAIN_MAP.put(Blocks.COBBLESTONE, new DecayReference(Blocks.GRAVEL, false));
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

    public static Pollution getPollution(CompoundTag tag) {
        return new Pollution(tag.getLong("amount"));
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
    public void damageEntity(LivingEntity entity) {
        if (amount > 1000000) {
            entity.hurt(DamageSource.WITHER, (float) amount / 1000000);
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


}
