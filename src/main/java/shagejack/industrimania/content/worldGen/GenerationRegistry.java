package shagejack.industrimania.content.worldGen;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import shagejack.industrimania.registers.AllFeatures;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class GenerationRegistry {

    private static final List<GenerationStep.Decoration> decorations = new LinkedList<>();
    static {
        decorations.add(GenerationStep.Decoration.UNDERGROUND_ORES);
        decorations.add(GenerationStep.Decoration.UNDERGROUND_DECORATION);
    }

    @SubscribeEvent
    public static void onBiomesLoaded(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        for (GenerationStep.Decoration stage : decorations) {
            List<Supplier<PlacedFeature>> feats = gen.getFeatures(stage);
            List<Supplier<PlacedFeature>> filtered = OreGenRemover.filterFeatures(feats);
            for (Supplier<PlacedFeature> feature : filtered) {
                feats.remove(feature);
            }
        }

        if (event.getCategory() != Biome.BiomeCategory.THEEND && event.getCategory() != Biome.BiomeCategory.NETHER) {
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AllFeatures.ORE_REMOVAL_ALL_PLACED);
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AllFeatures.ROCK_LAYERS_ALL_PLACED);
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AllFeatures.ORE_GEN_ALL_PLACED);
        }
    }

}
