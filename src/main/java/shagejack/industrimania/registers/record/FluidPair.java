package shagejack.industrimania.registers.record;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record FluidPair(RegistryObject<? extends Fluid> still, RegistryObject<? extends Fluid> flowing) {
    public FluidPair still(Consumer<RegistryObject<? extends Fluid>> consumer) {
        consumer.accept(still);
        return this;
    }

    public FluidPair flowing(Consumer<RegistryObject<? extends Fluid>> consumer) {
        consumer.accept(flowing);
        return this;
    }

    public FluidPair use(BiConsumer<RegistryObject<? extends Fluid>, RegistryObject<? extends Fluid>> consumer) {
        consumer.accept(still, flowing);
        return this;
    }

}