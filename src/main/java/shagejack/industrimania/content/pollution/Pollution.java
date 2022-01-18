package shagejack.industrimania.content.pollution;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Pollution {

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
        setAmount((long) (getAmount() * REDUCE_FACTOR));
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


}
