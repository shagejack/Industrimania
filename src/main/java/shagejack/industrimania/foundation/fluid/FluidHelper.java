package shagejack.industrimania.foundation.fluid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.Industrimania;

import javax.annotation.Nullable;

public class FluidHelper {

	public enum FluidExchange {
		ITEM_TO_TANK, TANK_TO_ITEM
	}

	public static boolean isWater(Fluid fluid) {
		return convertToStill(fluid) == Fluids.WATER;
	}

	public static boolean isLava(Fluid fluid) {
		return convertToStill(fluid) == Fluids.LAVA;
	}

	public static boolean hasBlockState(Fluid fluid) {
		BlockState blockState = fluid.defaultFluidState()
			.createLegacyBlock();
		return blockState != null && !blockState.isAir();
	}

	public static FluidStack copyStackWithAmount(FluidStack fs, int amount) {
		if (amount <= 0)
			return FluidStack.EMPTY;
		if (fs.isEmpty())
			return FluidStack.EMPTY;
		FluidStack copy = fs.copy();
		copy.setAmount(amount);
		return copy;
	}

	public static Fluid convertToFlowing(Fluid fluid) {
		if (fluid == Fluids.WATER)
			return Fluids.FLOWING_WATER;
		if (fluid == Fluids.LAVA)
			return Fluids.FLOWING_LAVA;
		if (fluid instanceof ForgeFlowingFluid)
			return ((ForgeFlowingFluid) fluid).getFlowing();
		return fluid;
	}

	public static Fluid convertToStill(Fluid fluid) {
		if (fluid == Fluids.FLOWING_WATER)
			return Fluids.WATER;
		if (fluid == Fluids.FLOWING_LAVA)
			return Fluids.LAVA;
		if (fluid instanceof ForgeFlowingFluid)
			return ((ForgeFlowingFluid) fluid).getSource();
		return fluid;
	}

	public static JsonElement serializeFluidStack(FluidStack stack) {
		JsonObject json = new JsonObject();
		json.addProperty("fluid", stack.getFluid()
			.getRegistryName()
			.toString());
		json.addProperty("amount", stack.getAmount());
		if (stack.hasTag())
			json.addProperty("nbt", stack.getTag()
				.toString());
		return json;
	}

	public static FluidStack deserializeFluidStack(JsonObject json) {
		ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
		Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
		if (fluid == null)
			throw new JsonSyntaxException("Unknown fluid '" + id + "'");
		int amount = GsonHelper.getAsInt(json, "amount");
		FluidStack stack = new FluidStack(fluid, amount);

		if (!json.has("nbt"))
			return stack;

		try {
			JsonElement element = json.get("nbt");
			stack.setTag(TagParser.parseTag(
				element.isJsonObject() ? Industrimania.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));

		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}

		return stack;
	}

	@Nullable
	public static FluidExchange exchange(IFluidHandler fluidTank, IFluidHandlerItem fluidItem, FluidExchange preferred,
		int maxAmount) {
		return exchange(fluidTank, fluidItem, preferred, true, maxAmount);
	}

	@Nullable
	public static FluidExchange exchangeAll(IFluidHandler fluidTank, IFluidHandlerItem fluidItem,
		FluidExchange preferred) {
		return exchange(fluidTank, fluidItem, preferred, false, Integer.MAX_VALUE);
	}

	@Nullable
	private static FluidExchange exchange(IFluidHandler fluidTank, IFluidHandlerItem fluidItem, FluidExchange preferred,
		boolean singleOp, int maxTransferAmountPerTank) {

		// Locks in the transfer direction of this operation
		FluidExchange lockedExchange = null;

		for (int tankSlot = 0; tankSlot < fluidTank.getTanks(); tankSlot++) {
			for (int slot = 0; slot < fluidItem.getTanks(); slot++) {

				FluidStack fluidInTank = fluidTank.getFluidInTank(tankSlot);
				int tankCapacity = fluidTank.getTankCapacity(tankSlot) - fluidInTank.getAmount();
				boolean tankEmpty = fluidInTank.isEmpty();

				FluidStack fluidInItem = fluidItem.getFluidInTank(tankSlot);
				int itemCapacity = fluidItem.getTankCapacity(tankSlot) - fluidInItem.getAmount();
				boolean itemEmpty = fluidInItem.isEmpty();

				boolean undecided = lockedExchange == null;
				boolean canMoveToTank = (undecided || lockedExchange == FluidExchange.ITEM_TO_TANK) && tankCapacity > 0;
				boolean canMoveToItem = (undecided || lockedExchange == FluidExchange.TANK_TO_ITEM) && itemCapacity > 0;

				// Incompatible Liquids
				if (!tankEmpty && !itemEmpty && !fluidInItem.isFluidEqual(fluidInTank))
					continue;

				// Transfer liquid to tank
				if (((tankEmpty || itemCapacity <= 0) && canMoveToTank)
					|| undecided && preferred == FluidExchange.ITEM_TO_TANK) {

					int amount = fluidTank.fill(
						fluidItem.drain(Math.min(maxTransferAmountPerTank, tankCapacity), FluidAction.EXECUTE),
						FluidAction.EXECUTE);
					if (amount > 0) {
						lockedExchange = FluidExchange.ITEM_TO_TANK;
						if (singleOp)
							return lockedExchange;
						continue;
					}
				}

				// Transfer liquid from tank
				if (((itemEmpty || tankCapacity <= 0) && canMoveToItem)
					|| undecided && preferred == FluidExchange.TANK_TO_ITEM) {

					int amount = fluidItem.fill(
						fluidTank.drain(Math.min(maxTransferAmountPerTank, itemCapacity), FluidAction.EXECUTE),
						FluidAction.EXECUTE);
					if (amount > 0) {
						lockedExchange = FluidExchange.TANK_TO_ITEM;
						if (singleOp)
							return lockedExchange;
						continue;
					}

				}

			}
		}

		return null;
	}

}
