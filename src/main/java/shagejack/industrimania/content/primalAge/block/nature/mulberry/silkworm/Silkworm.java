package shagejack.industrimania.content.primalAge.block.nature.mulberry.silkworm;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import shagejack.industrimania.registers.item.AllItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class Silkworm extends Item {

    public Silkworm(Properties properties) {
        super(properties);
    }

    public static void TagCompoundCheck(ItemStack stack) {
        if (!stack.hasTag()) {
            InitTagCompound(stack);
        }
    }

    /*
    Tags:
    Int Age (1 -> 1s avg.)

    0 ~ 1200 : Egg
    1200 ~ 1500 : Larva(1)
    1500 ~ 1600 : Larva(1) Hibernation
    1600 ~ 1850 : Larva(2)
    1850 ~ 1950 : Larva(2) Hibernation
    1950 ~ 2250 : Larva(3)
    2250 ~ 2370 : Larva(3) Hibernation
    2370 ~ 2700 : Larva(4)
    2700 ~ 2850 : Larva(4) Hibernation
    2850 ~ 3600 : Larva(5)
    3600 ~ 4000 : Pupa(Spinning)
    4000 ~ 4500 : Pupa(Mature) (Can Get Silk)
    4500 ~ 5000 : Pupa(Moth Emergence)
    5000 ~ 6500 : Adult(Moth)
    6500+ : Dead

    Float Health 0.0 ~ 100.0
    Float Hunger 0.0 ~ 100.0
    Float GrowthMultiplierBase 0.8 ~ 1.2 when initialized
    Float SicknessMultiplierBase 0.8 ~ 1.2 when initialized
    Float GrowthMultiplier
    Float SicknessMultiplier
    Boolean Sick
    Boolean Protected
    Boolean Dead
    Boolean Wet
    Int Sex
    0 : Male
    1 : Female
     */
    public static void InitTagCompound(ItemStack stack) {
        stack.setTag(new CompoundTag());
    }

    public static void init(ItemStack stack, Random random) {
        setAge(stack, random.nextInt(2001));
        setSex(stack, random.nextInt(2));
        setHunger(stack, 50 + 50 * random.nextFloat());
        setHealth(stack, 50 + 50 * random.nextFloat());
        setProtected(stack, false);
        setSick(stack, false);
        setSicknessMultiplierBase(stack, random.nextFloat(0.8f, 1.2f));
        setGrowthMultiplierBase(stack, random.nextFloat(0.8f, 1.2f));
        setSicknessMultiplier(stack, getSicknessMultiplierBase(stack));
        setGrowthMultiplier(stack, getGrowthMultiplierBase(stack));
        setDead(stack, false);
        setWet(stack, false);
        setInit(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {

        Random random = level.getRandom();

        if (!isInitialized(stack)) {
            init(stack, random);
        }

        if (isAdult(stack))
            setHunger(stack, 100);

        if (isDead(stack))
            return;

        if (random.nextDouble() < (isSick(stack) ? 0.025 : 0.05) * getGrowthMultiplier(stack) && !isStarving(stack)) {
            setAge(stack, getAge(stack) + 1);
            if (inHibernation(stack) || isPupa(stack)) {
                setHunger(stack, getHunger(stack) - 0.02f);
            } else {
                setHunger(stack, getHunger(stack) - 0.01f);
            }
        }

        if (!isStarving(stack)) {
            if (random.nextDouble() < 0.05) {
                setHunger(stack, getHunger(stack) - 0.1f);
            }
        } else {
            if (random.nextDouble() < 0.05) {
                setHealth(stack, getHealth(stack) - 1.0f);
            }
        }

        if (isSick(stack)) {
            if (random.nextDouble() < 0.0003) {
                setSick(stack, false);
            } else {
                if (random.nextDouble() < 0.01) {
                    setHunger(stack, getHunger(stack) - 0.01f);
                }
                if (random.nextDouble() < 0.05) {
                    setHealth(stack, getHealth(stack) - 0.5f);
                }
            }
        }

        if (!isProtected(stack)) {
            if (random.nextDouble() < 0.05) {
                setHealth(stack, getHealth(stack) - 0.1f);
            }
            if (random.nextDouble() < 0.00005 * getSicknessMultiplier(stack)) {
                setSick(stack, true);
            }
        } else {
            if (random.nextDouble() < 1e-6 * getSicknessMultiplier(stack)) {
                setSick(stack, true);
            }
        }

        if (getHealth(stack) <= 100) {
            if (random.nextDouble() < 0.05) {
                setHealth(stack, getHealth(stack) + 0.1f);
                setHunger(stack, getHunger(stack) - 0.01f);
            }
        }

        if (getHealth(stack) <= 0)
            setDead(stack, true);

        if (isWet(stack) && random.nextDouble() < 0.0005) {
            stack.setCount(0);
            stack = new ItemStack(AllItems.pupa.get());
        }


    }

    public void appendTooltip(ItemStack itemStack, @Nullable Level level, List<String> tooltip, TooltipFlag flag) {
        //TODO: add silkworm tooltip (when player is equipping silkgrower glass)
    }

    public boolean isStarving(ItemStack item) {
        return getHunger(item) <= 0;
    }

    public static int getAge(ItemStack item){
        return item.getOrCreateTag().getInt("Age");
    }

    public static int getSex(ItemStack item){
        return item.getOrCreateTag().getInt("Sex");
    }

    public static float getHealth(ItemStack item){
        return item.getOrCreateTag().getFloat("Health");
    }

    public static float getHunger(ItemStack item){
        return item.getOrCreateTag().getFloat("Hunger");
    }

    public static float getGrowthMultiplier(ItemStack item){
        return item.getOrCreateTag().getFloat("GrowthMultiplier");
    }

    public static float getSicknessMultiplier(ItemStack item){
        return item.getOrCreateTag().getFloat("SicknessMultiplier");
    }

    public static float getGrowthMultiplierBase(ItemStack item){
        return item.getOrCreateTag().getFloat("GrowthMultiplierBase");
    }

    public static float getSicknessMultiplierBase(ItemStack item){
        return item.getOrCreateTag().getFloat("SicknessMultiplierBase");
    }

    public static boolean isSick(ItemStack item){
        return item.getOrCreateTag().getBoolean("Sick");
    }

    public static boolean isProtected(ItemStack item){
        return item.getOrCreateTag().getBoolean("Protected");
    }

    public static boolean isWet(ItemStack item){
        return item.getOrCreateTag().getBoolean("Wet");
    }

    public static boolean isInitialized(ItemStack item){
        return item.getOrCreateTag().getBoolean("Initialized");
    }

    public static boolean isUnnaturallyDead(ItemStack item){
        return item.getOrCreateTag().getBoolean("Dead");
    }

    public static void setAge(ItemStack itemStack, int age) {
        itemStack.getOrCreateTag().putInt("Age", age);
    }

    public static void setSex(ItemStack itemStack, int sex) {
        itemStack.getOrCreateTag().putInt("Sex", sex);
    }

    public static void setHealth(ItemStack itemStack, float health) {
        itemStack.getOrCreateTag().putFloat("Health", health);
    }

    public static void setHunger(ItemStack itemStack, float hunger) {
        itemStack.getOrCreateTag().putFloat("Hunger", hunger);
    }

    public static void setSick(ItemStack itemStack, boolean sick) {
        itemStack.getOrCreateTag().putBoolean("Sick", sick);
    }

    public static void setProtected(ItemStack itemStack, boolean protect) {
        itemStack.getOrCreateTag().putBoolean("Protected", protect);
    }

    public static void setDead(ItemStack itemStack, boolean dead) {
        itemStack.getOrCreateTag().putBoolean("Dead", dead);
    }

    public static void setGrowthMultiplier(ItemStack itemStack, float growthMultiplier) {
        itemStack.getOrCreateTag().putFloat("GrowthMultiplier", growthMultiplier);
    }

    public static void setSicknessMultiplier(ItemStack itemStack, float sicknessMultiplier) {
        itemStack.getOrCreateTag().putFloat("SicknessMultiplier", sicknessMultiplier);
    }

    public static void setGrowthMultiplierBase(ItemStack itemStack, float growthMultiplierBase) {
        itemStack.getOrCreateTag().putFloat("GrowthMultiplierBase", growthMultiplierBase);
    }

    public static void setSicknessMultiplierBase(ItemStack itemStack, float sicknessMultiplierBase) {
        itemStack.getOrCreateTag().putFloat("SicknessMultiplierBase", sicknessMultiplierBase);
    }

    public static void setWet(ItemStack stack, boolean wet) {
        stack.getOrCreateTag().putBoolean("Wet", wet);
    }


    public static void setInit(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("Initialized", true);
    }

    /*
    0 ~ 1200 : Egg 0
    1200 ~ 1500 : Larva(1) 1
    1500 ~ 1600 : Larva(1) Hibernation 2
    1600 ~ 1850 : Larva(2) 3
    1850 ~ 1950 : Larva(2) Hibernation 4
    1950 ~ 2250 : Larva(3) 5
    2250 ~ 2370 : Larva(3) Hibernation 6
    2370 ~ 2700 : Larva(4) 7
    2700 ~ 2850 : Larva(4) Hibernation 8
    2850 ~ 3600 : Larva(5) 9
    3600 ~ 4000 : Pupa(Spinning) 10
    4000 ~ 4500 : Pupa(Mature) (Can Get Silk) 11
    4500 ~ 5000 : Pupa(Moth Emergence) 12
    5000 ~ 6500 : Adult(Moth) 13
    6500+ : Dead 14
     */
    public static int getStage(ItemStack stack) {
        int age = getAge(stack);
        if (age < 1200)
            return 0;
        if (age < 1500)
            return 1;
        if (age < 1600)
            return 2;
        if (age < 1850)
            return 3;
        if (age < 1950)
            return 4;
        if (age < 2250)
            return 5;
        if (age < 2370)
            return 6;
        if (age < 2700)
            return 7;
        if (age < 2850)
            return 8;
        if (age < 3600)
            return 9;
        if (age < 4000)
            return 10;
        if (age < 4500)
            return 11;
        if (age < 5000)
            return 12;
        if (age < 6500)
            return 13;
        return 14;
    }

    public static boolean inHibernation(ItemStack stack) {
        int stage = getStage(stack);
        return stage == 2 || stage == 4 || stage == 6 || stage == 8;
    }

    public static boolean isPupa(ItemStack stack) {
        return getAge(stack) >= 3600 && getAge(stack) < 5000;
    }

    public static boolean canEat(ItemStack stack) {
        int stage = getStage(stack);
        return stage == 1 || stage == 3 || stage == 5 || stage == 7 || stage == 9;
    }

    public static boolean isAdult(ItemStack stack) {
        return getAge(stack) >= 5000 && getAge(stack) < 6500;
    }

    public static boolean isDead(ItemStack stack) {
        return isUnnaturallyDead(stack) || getAge(stack) >= 6500;
    }


}
