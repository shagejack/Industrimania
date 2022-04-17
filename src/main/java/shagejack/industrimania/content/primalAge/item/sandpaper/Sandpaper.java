package shagejack.industrimania.content.primalAge.item.sandpaper;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import shagejack.industrimania.foundation.item.renderer.SimpleCustomRenderer;
import shagejack.industrimania.foundation.utility.VecUtils;
import shagejack.industrimania.foundation.utility.recipe.RecipeConditions;
import shagejack.industrimania.foundation.utility.recipe.RecipeFinder;
import shagejack.industrimania.registers.AllRecipeTypes;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sandpaper extends Item {

    private static final Object sandpaperRecipesKey = new Object();

    public Sandpaper(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // sandpaper can only be used in main hand
        if (hand == InteractionHand.MAIN_HAND) {
            // interaction between sandpaper and player's offhand item when player is sneaking
            if (player.isShiftKeyDown()) {
                ItemStack offStack = player.getItemInHand(InteractionHand.OFF_HAND);
                // check if there's item in offhand
                if (!offStack.isEmpty()) {
                    // if there is, then copy the offhand stack
                    ItemStack copy = offStack.copy();
                    // check if sandpaper contains item
                    if (containItem(stack)) {
                        // if it does, then check if it's same as offhand and if stack in offhand is full
                        if (getStackContained(stack).is(copy.getItem()) && copy.getMaxStackSize() > copy.getCount()) {
                            // move the item from sandpaper to offhand
                            stack.getOrCreateTag().put("ItemStack", ItemStack.EMPTY.serializeNBT());
                            offStack.grow(1);
                            // reset current use duration to 0
                            stack.getOrCreateTag().putInt("UseDuration", 0);
                        } else {
                            // else, check if the offhand stack size is 1
                            if (copy.getCount() == 1) {
                                // exchange item in offhand and sandpaper
                                // replace offhand item
                                ItemStack containStack = getStackContained(stack);
                                stack.getOrCreateTag().put("ItemStack", ItemStack.EMPTY.serializeNBT());
                                player.setItemInHand(InteractionHand.OFF_HAND, containStack);
                                // replace sandpaper item
                                copy.setCount(1);
                                stack.getOrCreateTag().put("ItemStack", copy.serializeNBT());
                                stack.getOrCreateTag().putInt("UseDuration", getRecipeDuration(copy, level));
                            }
                        }
                    } else {
                        // if sandpaper contains no item, move one item in offhand stack to sandpaper.
                        offStack.shrink(1);
                        copy.setCount(1);
                        stack.getOrCreateTag().put("ItemStack", copy.serializeNBT());
                        // set current use duration from recipe
                        stack.getOrCreateTag().putInt("UseDuration", getRecipeDuration(copy, level));
                    }
                } else {
                    // if there isn't item in offhand, then check if sandpaper contains item
                    if (containItem(stack)) {
                        // if it does, move the item from sandpaper to offhand
                        ItemStack containStack = getStackContained(stack);
                        stack.getOrCreateTag().put("ItemStack", ItemStack.EMPTY.serializeNBT());
                        player.setItemInHand(InteractionHand.OFF_HAND, containStack);
                        // reset current use duration to 0
                        stack.getOrCreateTag().putInt("UseDuration", 0);
                    }
                }

                return InteractionResultHolder.success(stack);
            } else {
                // if not sneaking, check if stack contains item
                if (containItem(stack)) {
                    // start using sandpaper
                    player.startUsingItem(InteractionHand.MAIN_HAND);
                    return InteractionResultHolder.consume(stack);
                } else {
                    ItemStack offStack = player.getItemInHand(InteractionHand.OFF_HAND);
                    // check if there's item in offhand
                    if (!offStack.isEmpty()) {
                        ItemStack copy = offStack.copy();
                        // if sandpaper contains no item, move one item in offhand stack to sandpaper.
                        offStack.shrink(1);
                        copy.setCount(1);
                        stack.getOrCreateTag().put("ItemStack", copy.serializeNBT());
                        // set current use duration from recipe
                        stack.getOrCreateTag().putInt("UseDuration", getRecipeDuration(copy, level));
                    }
                }
            }
        }

        return super.use(level, player, hand);
    }

    public static void spawnParticles(Vec3 location, ItemStack polishedStack, Level world) {
        for (int i = 0; i < 20; i++) {
            Vec3 motion = VecUtils.offsetRandomly(Vec3.ZERO, world.random, 1 / 8f);
            world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, polishedStack), location.x, location.y,
                    location.z, motion.x, motion.y, motion.z);
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        // if sandpaper contains item and stored use duration is bigger than 0, play eating animation
        return containItem(stack) && getStoredUseDuration(stack) > 0 ? UseAnim.EAT : UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        // get use duration from stored data due to the fact that this method does not pass a Level parameter
        return containItem(stack) ? getStoredUseDuration(stack) : 0;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // sand only when player is not sneaking and the sandpaper contains an item which has sandpaper recipe (by checking is the sand result same as the sanded item)
        if (entity instanceof Player && !entity.isShiftKeyDown() && containItem(stack) && !getSandResult(getStackContained(stack), level).is(getStackContained(stack).getItem())) {
            return sand(stack, level, (Player) entity);
        }
        return stack;
    }

    /**
     * sand the item in sandpaper item stack
     * @param stack sandpaper item stack
     * @param level
     * @param player
     * @return the sandpaper item stack after sanding (or doing nothing)
     */
    public ItemStack sand(ItemStack stack, Level level, Player player) {
        ItemStack containStack = getStackContained(stack);
        ItemStack resultStack = getSandResult(containStack, level);

        // spawn item fragment particles
        if (level.isClientSide) {
            spawnParticles(player.getEyePosition(1).add(player.getLookAngle().scale(.5f)), containStack, level);
            return stack;
        }

        // sand only when the result is not same as the contained stack
        // this check is a bit redundant as the result has been checked when this method is called from Item#finishingUsingItem
        // but still I choose to preserve it as this method could be called somewhere else
        if (!resultStack.is(containStack.getItem())) {
            // clear sandpaper, reset the stored use duration
            stack.getOrCreateTag().put("ItemStack", ItemStack.EMPTY.serializeNBT());
            stack.getOrCreateTag().putInt("UseDuration", 0);
            // decrease sandpaper durability
            stack.hurtAndBreak(1, player, p -> {});
            // cause player to exhaust
            player.causeFoodExhaustion(2);
            // give player the result stack
            player.getInventory().placeItemBackInInventory(resultStack);
        }

        // return the sandpaper item stack no matter if sanding process is implemented
        return stack;
    }

    public ItemStack getSandResult(ItemStack containStack, Level level) {

        List<? extends Recipe<?>> recipes = getRecipes(containStack, level);

        if (!recipes.isEmpty()) {
            if (recipes.get(0) instanceof SandpaperRecipe sandpaperRecipe) {
                return sandpaperRecipe.getResultItem().copy();
            }
        }

        return containStack;
    }

    public int getStoredUseDuration(ItemStack stack) {
        return stack.getOrCreateTag().contains("UseDuration", Tag.TAG_INT) ? stack.getOrCreateTag().getInt("UseDuration") : 0;
    }

    public int getRecipeDuration(ItemStack containStack, Level level) {
        List<? extends Recipe<?>> recipes = getRecipes(containStack, level);

        if (!recipes.isEmpty()) {
            if (recipes.get(0) instanceof SandpaperRecipe sandpaperRecipe) {
                return sandpaperRecipe.getProcessingDuration();
            }
        }

        return 0;
    }

    public List<? extends Recipe<?>> getRecipes(ItemStack containStack, Level level) {
        Predicate<Recipe<?>> types;

        types = RecipeConditions.isOfType(AllRecipeTypes.SANDPAPER.getType());

        List<Recipe<?>> startedSearch = RecipeFinder.get(sandpaperRecipesKey, level, types);

        return startedSearch.stream()
                .filter(RecipeConditions.firstIngredientMatches(containStack))
                .collect(Collectors.toList());
    }

    public static boolean containItem(ItemStack stack) {
        return stack.getOrCreateTag().contains("ItemStack", Tag.TAG_COMPOUND) && !ItemStack.of(stack.getOrCreateTag().getCompound("ItemStack")).isEmpty();
    }

    /**
     * the return tag is just a copy. DO NOT modify sandpaper stack by modifying the returned stack.
     * @return contained stack
      */
    public static ItemStack getStackContained(ItemStack stack) {
        return ItemStack.of(stack.getOrCreateTag().getCompound("ItemStack"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new SandpaperRenderer()));
    }

}
