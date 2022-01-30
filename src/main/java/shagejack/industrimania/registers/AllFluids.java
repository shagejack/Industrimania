package shagejack.industrimania.registers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;

import javax.annotation.Nullable;
import java.util.function.Supplier;


public class AllFluids {

    public static final FluidPair rawRubber
            = new FluidBuilder()
            .density(1024)
            .viscosity(1024)
            .build();


    public static class FluidBuilder {

        protected String name;
        protected RegistryObject<Fluid> STILL;
        protected RegistryObject<Fluid> FLOWING;
        protected FluidAttributes.Builder attributesBuilder;
        protected ForgeFlowingFluid.Properties properties;
        @Nullable
        private Supplier<? extends Item> bucketItem = null;
        @Nullable
        private Supplier<? extends LiquidBlock> liquidBlock = null;

        public FluidBuilder() {
            STILL = RegistryObject.of(new ResourceLocation(name), ForgeRegistries.FLUIDS);
            FLOWING = RegistryObject.of(new ResourceLocation("flowing_" + name), ForgeRegistries.FLUIDS);
            attributesBuilder = FluidAttributes.builder(new ResourceLocation(Industrimania.MOD_ID, "block/fluid/" + name + "/still"), new ResourceLocation(Industrimania.MOD_ID, "block/fluid/" + name + "/flowing"));
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

            RegistryObject<? extends Fluid> fluidStill = RegisterHandle.FLUID_REGISTER.register(name, () -> new ForgeFlowingFluid.Source(properties).setRegistryName(STILL.getId()));
            RegistryObject<? extends Fluid> fluidFlowing = RegisterHandle.FLUID_REGISTER.register("flowing_" + name, () -> new ForgeFlowingFluid.Flowing(properties).setRegistryName(FLOWING.getId()));

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

    }

}
