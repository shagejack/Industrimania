package shagejack.industrimania.content.primalAge.block.mixingBasin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import shagejack.industrimania.content.contraptions.processing.HeatCondition;
import shagejack.industrimania.content.contraptions.processing.ProcessingRecipe;
import shagejack.industrimania.content.contraptions.processing.ProcessingRecipeBuilder;
import shagejack.industrimania.foundation.fluid.FluidIngredient;
import shagejack.industrimania.foundation.fluid.SmartFluidTankBehaviour;
import shagejack.industrimania.foundation.item.SmartInventory;
import shagejack.industrimania.foundation.utility.Iterate;
import shagejack.industrimania.foundation.utility.recipe.IRecipeTypeInfo;
import shagejack.industrimania.registers.AllRecipeTypes;

import java.util.*;

public class MixingBasinRecipe extends ProcessingRecipe<SmartInventory> {

    public MixingBasinRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.MIXING_BASIN.getType(), params);
    }

    public static boolean match(MixingBasinTileEntity basin, Recipe<?> recipe) {
        return apply(basin, recipe, true);
    }

    public static boolean apply(MixingBasinTileEntity basin, Recipe<?> recipe) {
        return apply(basin, recipe, false);
    }

    public static boolean apply(MixingBasinTileEntity basin, Recipe<?> recipe, boolean test) {
        if (recipe instanceof MixingBasinRecipe basinRecipe) {
            IItemHandler availableItems = basin.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .orElse(null);
            IFluidHandler availableFluids = basin.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                    .orElse(null);

            if (availableItems == null || availableFluids == null)
                return false;

            List<ItemStack> recipeOutputItems = new ArrayList<>();
            List<FluidStack> recipeOutputFluids = new ArrayList<>();

            List<Ingredient> ingredients = new LinkedList<>(recipe.getIngredients());
            ingredients.sort(Comparator.comparingInt(i -> i.getItems().length));
            List<FluidIngredient> fluidIngredients = basinRecipe.getFluidIngredients();

            for (boolean simulate : Iterate.trueAndFalse) {

                // return true in non-simulation run it's a test
                if (!simulate && test)
                    return true;

                int[] extractedItemsFromSlot = new int[availableItems.getSlots()];
                int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

                Ingredients:
                for (Ingredient ingredient : ingredients) {
                    for (int slot = 0; slot < availableItems.getSlots(); slot++) {
                        if (simulate && availableItems.getStackInSlot(slot)
                                .getCount() <= extractedItemsFromSlot[slot])
                            continue;
                        ItemStack extracted = availableItems.extractItem(slot, 1, true);
                        if (!ingredient.test(extracted))
                            continue;
                        // Catalyst items are never consumed
                        if (extracted.hasContainerItem() && extracted.getContainerItem()
                                .sameItem(extracted))
                            continue Ingredients;
                        if (!simulate)
                            availableItems.extractItem(slot, 1, false);
                        else if (extracted.hasContainerItem())
                            recipeOutputItems.add(extracted.getContainerItem()
                                    .copy());
                        extractedItemsFromSlot[slot]++;
                        continue Ingredients;
                    }

                    // something wasn't found
                    return false;
                }

                boolean fluidsAffected = false;

                FluidIngredients:
                for (FluidIngredient fluidIngredient : fluidIngredients) {
                    int amountRequired = fluidIngredient.getRequiredAmount();

                    for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                        FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                        if (simulate && fluidStack.getAmount() <= extractedFluidsFromTank[tank])
                            continue;
                        if (!fluidIngredient.test(fluidStack))
                            continue;
                        int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                        if (!simulate) {
                            fluidStack.shrink(drainedAmount);
                            fluidsAffected = true;
                        }
                        amountRequired -= drainedAmount;
                        if (amountRequired != 0)
                            continue;
                        extractedFluidsFromTank[tank] += drainedAmount;
                        continue FluidIngredients;
                    }

                    // something wasn't found
                    return false;
                }

                if (fluidsAffected) {
                    basin.getBehaviour(SmartFluidTankBehaviour.INPUT)
                            .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                    basin.getBehaviour(SmartFluidTankBehaviour.OUTPUT)
                            .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                }

                if (simulate) {
                    recipeOutputItems.addAll(basinRecipe.rollResults());
                    recipeOutputFluids.addAll(basinRecipe.getFluidResults());
                }

                if (!basin.acceptOutputs(recipeOutputItems, recipeOutputFluids, simulate))
                    return false;
            }

            return true;
        }

        return false;
    }


    @Override
    protected int getMaxInputCount() {
        return 9;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    }

    @Override
    public boolean matches(SmartInventory inv, Level level) {
        return false;
    }
}
