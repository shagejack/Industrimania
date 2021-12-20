package shagejack.shagecraft.registers;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record ItemBlock(RegistryObject<Item> item, RegistryObject<Block> block) {
    public ItemBlock item(Consumer<RegistryObject<Item>> consumer) {
        consumer.accept(item);
        return this;
    }

    public ItemBlock block(Consumer<RegistryObject<Block>> consumer) {
        consumer.accept(block);
        return this;
    }

    public ItemBlock use(BiConsumer<RegistryObject<Item>, RegistryObject<Block>> consumer) {
        consumer.accept(item, block);
        return this;
    }
}