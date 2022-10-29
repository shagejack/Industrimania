package shagejack.industrimania.registries.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registries.RegisterHandle;
import shagejack.industrimania.registries.record.FluidPair;
import shagejack.industrimania.registries.tags.TagBuilder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class FluidBuilder {

    protected String name;
    protected FluidAttributes.Builder attributesBuilder;
    protected ForgeFlowingFluid.Properties properties;
    protected final List<String> tags = new ArrayList<>();
    protected RegistryObject<Fluid> STILL;
    protected RegistryObject<Fluid> FLOWING;
    @Nullable
    private Supplier<? extends Item> bucketItem = null;
    @Nullable
    private Supplier<? extends LiquidBlock> liquidBlock = null;

    public FluidBuilder(String name) {
        this.name = Objects.requireNonNull(name);
        STILL = RegistryObject.create(Industrimania.asResource(this.name), ForgeRegistries.FLUIDS);
        FLOWING = RegistryObject.create(Industrimania.asResource("flowing_" + this.name), ForgeRegistries.FLUIDS);
        attributesBuilder = FluidAttributes.builder(Industrimania.asResource("block/fluid/" + this.name + "/still"), Industrimania.asResource("block/fluid/" + this.name + "/flowing"));
        properties = new ForgeFlowingFluid.Properties(STILL, FLOWING, attributesBuilder);
    }

    public FluidBuilder asBase() {
        return this;
    }

    public FluidPair build() {
        if (bucketItem != null) {
            properties.bucket(bucketItem);
        }

        if (liquidBlock != null) {
            properties.block(liquidBlock);
        }

        RegistryObject<FlowingFluid> fluidStill = RegisterHandle.FLUID_REGISTER.register(name, () -> new ForgeFlowingFluid.Source(properties));
        RegistryObject<FlowingFluid> fluidFlowing = RegisterHandle.FLUID_REGISTER.register("flowing_" + name, () -> new ForgeFlowingFluid.Flowing(properties));

        if (!tags.isEmpty()) {
            TagBuilder.fluidTag(fluidStill, tags);
            TagBuilder.fluidTag(fluidFlowing, tags);
            Industrimania.LOGGER.debug("register Fluid: {} with Tags: {}", name, tags.toString());
        } else {
            Industrimania.LOGGER.debug("register Fluid: {}", name);
        }

        return new FluidPair(fluidStill, fluidFlowing);
    }

    public FluidBuilder bucketItem(Supplier<? extends Item> bucketItem) {
        this.bucketItem = bucketItem;
        return this;
    }

    public FluidBuilder liquidBlock(Supplier<? extends LiquidBlock> liquidBlock) {
        this.liquidBlock = liquidBlock;
        return this;
    }

    public FluidBuilder canMultiply() {
        this.properties.canMultiply();
        return this;
    }

    public FluidBuilder tickRate(int tickRate) {
        this.properties.tickRate(tickRate);
        return this;
    }

    public FluidBuilder density(int density) {
        this.attributesBuilder.density(density);
        return this;
    }

    public FluidBuilder viscosity(int viscosity) {
        this.attributesBuilder.viscosity(viscosity);
        return this;
    }

    public FluidBuilder temperature(int temperature) {
        this.attributesBuilder.temperature(temperature);
        return this;
    }

    public FluidBuilder luminosity(int luminosity) {
        this.attributesBuilder.luminosity(luminosity);
        return this;
    }

    public final FluidBuilder rarity(Rarity rarity)
    {
        this.attributesBuilder.rarity(rarity);
        return this;
    }

    public FluidBuilder gaseous() {
        this.attributesBuilder.gaseous();
        return this;
    }

    public FluidBuilder sound(SoundEvent sound)
    {
        this.attributesBuilder.sound(sound);
        return this;
    }

    public FluidBuilder sound(SoundEvent fillSound, SoundEvent emptySound)
    {
        this.attributesBuilder.sound(fillSound, emptySound);
        return this;
    }

    public FluidBuilder tags(TagKey<?>... tags) {
        this.tags.addAll(Arrays.stream(tags).map(tag -> tag.location().toString()).toList());
        return this;
    }

    public FluidBuilder tags(ResourceLocation... tags) {
        this.tags.addAll(Arrays.stream(tags).map(ResourceLocation::toString).toList());
        return this;
    }

    public FluidBuilder tags(String... tags) {
        this.tags.addAll(List.of(tags));
        return this;
    }

}