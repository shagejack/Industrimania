package shagejack.industrimania.registers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegistryEvent;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.contraptions.processing.ProcessingRecipeBuilder;
import shagejack.industrimania.content.contraptions.processing.ProcessingRecipeSerializer;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackRecipe;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackRottenRecipe;
import shagejack.industrimania.foundation.utility.Lang;
import shagejack.industrimania.foundation.utility.recipe.IRecipeTypeInfo;

import java.util.Optional;
import java.util.function.Supplier;

public enum AllRecipeTypes implements IRecipeTypeInfo {

    DRYING_RACK(DryingRackRecipe::new),
    DRYING_RACK_ROTTEN(DryingRackRottenRecipe::new)

    ;

    private ResourceLocation id;
    private Supplier<RecipeSerializer<?>> serializerSupplier;
    private Supplier<RecipeType<?>> typeSupplier;
    private RecipeSerializer<?> serializer;
    private RecipeType<?> type;

    AllRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier) {
        this.id = Industrimania.asResource(Lang.asId(name()));
        this.serializerSupplier = serializerSupplier;
        this.typeSupplier = typeSupplier;
    }

    AllRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, RecipeType<?> existingType) {
        this(serializerSupplier, () -> existingType);
    }

    AllRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        this.id = Industrimania.asResource(Lang.asId(name()));
        this.serializerSupplier = serializerSupplier;
        this.typeSupplier = () -> simpleType(id);
    }

    AllRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
        this(processingSerializer(processingFactory));
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type;
    }

    public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    public static void register(RegistryEvent.Register<RecipeSerializer<?>> event) {
        ShapedRecipe.setCraftingSize(9, 9);

        for (AllRecipeTypes r : AllRecipeTypes.values()) {
            r.serializer = r.serializerSupplier.get();
            r.type = r.typeSupplier.get();
            r.serializer.setRegistryName(r.id);
            event.getRegistry()
                    .register(r.serializer);
        }
    }

    private static Supplier<RecipeSerializer<?>> processingSerializer(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> factory) {
        return () -> new ProcessingRecipeSerializer<>(factory);
    }

    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return Registry.register(Registry.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return stringId;
            }
        });
    }

    public static boolean isManualRecipe(Recipe<?> recipe) {
        return recipe.getId()
                .getPath()
                .endsWith("_manual_only");
    }
}
