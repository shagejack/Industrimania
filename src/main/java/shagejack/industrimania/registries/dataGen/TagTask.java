package shagejack.industrimania.registries.dataGen;

import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.BiConsumer;

public record TagTask<T>(RegistryObject<T> registryObject, List<String> tags) {
    public void run(BiConsumer<T, String> tagConsumer) {
        tags.forEach(tag -> tagConsumer.accept(registryObject.get(), tag));
    }
}
