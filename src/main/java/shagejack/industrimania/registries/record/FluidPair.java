package shagejack.industrimania.registries.record;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record FluidPair(RegistryObject<? extends FlowingFluid> still, RegistryObject<? extends FlowingFluid> flowing) {
    public FluidPair still(Consumer<RegistryObject<? extends FlowingFluid>> consumer) {
        consumer.accept(still);
        return this;
    }

    public FluidPair flowing(Consumer<RegistryObject<? extends FlowingFluid>> consumer) {
        consumer.accept(flowing);
        return this;
    }

    public FluidPair use(BiConsumer<RegistryObject<? extends FlowingFluid>, RegistryObject<? extends FlowingFluid>> consumer) {
        consumer.accept(still, flowing);
        return this;
    }

    public Fluid asFluid() {
        return this.still.get().getSource();
    }

    public Supplier<Fluid> asFluidSupplier() {
        return () -> this.still.get().getSource();
    }

}