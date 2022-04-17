package shagejack.industrimania.foundation.item.renderer;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.tuple.Pair;
import shagejack.industrimania.foundation.utility.nonnull.NonNullBiConsumer;
import shagejack.industrimania.foundation.utility.nonnull.NonNullFunction;

public class CustomItemModels {

    private List<Pair<Supplier<? extends Item>, NonNullFunction<BakedModel, ? extends BakedModel>>> registered;
    private Map<Item, NonNullFunction<BakedModel, ? extends BakedModel>> customModels;

    public CustomItemModels() {
        registered = new ArrayList<>();
        customModels = new IdentityHashMap<>();
    }

    public void register(Supplier<? extends Item> entry,
                         NonNullFunction<BakedModel, ? extends BakedModel> behaviour) {
        registered.add(Pair.of(entry, behaviour));
    }

    public void forEach(NonNullBiConsumer<Item, NonNullFunction<BakedModel, ? extends BakedModel>> consumer) {
        loadEntriesIfMissing();
        customModels.forEach(consumer);
    }

    private void loadEntriesIfMissing() {
        if (customModels.isEmpty())
            loadEntries();
    }

    private void loadEntries() {
        customModels.clear();
        registered.forEach(p -> customModels.put(p.getKey()
                .get(), p.getValue()));
    }

}
