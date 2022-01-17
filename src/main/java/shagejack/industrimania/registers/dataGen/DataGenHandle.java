package shagejack.industrimania.registers.dataGen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.foundation.utility.Wrapper;
import shagejack.industrimania.registers.AllBlocks;
import shagejack.industrimania.registers.AllItems;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static shagejack.industrimania.Industrimania.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenHandle {

    public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    public static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    private static final ArrayList<Consumer<ItemModelProvider>> itemModelTasks = new ArrayList();
    private static final ArrayList<Consumer<BlockModelProvider>> blockModelTasks = new ArrayList();
    private static final ArrayList<Consumer<BlockStateProvider>> blockStateTasks = new ArrayList();
    private static final ArrayList<Consumer<LanguageProvider>> languageTasks = new ArrayList<>();
    private static final Wrapper<ItemModelProvider> itemModelPro = new Wrapper();
    private static final Wrapper<BlockModelProvider> blockModelPro = new Wrapper();
    private static final Wrapper<BlockStateProvider> blockStatePro = new Wrapper();
    private static final Wrapper<BlockTagsProvider> blockTagsPro = new Wrapper<>();
    private static final Wrapper<LanguageProvider> languagePro = new Wrapper<>();
    public static Lazy<ExistingModelFile> itemGeneratedModel = () -> existingModel(itemModelPro.get(), "item/generated");
    public static Lazy<ExistingModelFile> itemHeldModel = () -> existingModel(itemModelPro.get(), "item/handheld");
    public static Lazy<UncheckedModelFile> blockBuiltinEntity = () -> uncheckedModel( "builtin/entity");
    public static Lazy<ExistingModelFile> blockCubeAll = () -> existingModel(itemModelPro.get(), "block/cube_all");
    public static Lazy<ExistingModelFile> blockCube = () -> existingModel(itemModelPro.get(), "block/cube");
    public static Lazy<ExistingModelFile> blockCubeRotatable = () -> existingModel(itemModelPro.get(), "");
    public static Lazy<ExistingModelFile> blockCrossTexture = () -> existingModel(itemModelPro.get(), "block/cross");

    public static void addItemModelTask(Consumer<ItemModelProvider> task) {
        runOnDataGen(() -> () -> {
            itemModelTasks.add(task);
        });
    }

    public static void addBlockModelTask(Consumer<BlockModelProvider> task) {
        runOnDataGen(() -> () -> {
            blockModelTasks.add(task);
        });
    }

    public static void addBlockStateTask(Consumer<BlockStateProvider> task) {
        runOnDataGen(() -> () -> {
            blockStateTasks.add(task);
        });
    }

    public static void runOnDataGen(Supplier<DistExecutor.SafeRunnable> toRun) {
        if (FMLEnvironment.dist == Dist.CLIENT && Industrimania.isDataGen) {
            toRun.get().run();
        }
    }

    public static boolean checkTextureFileExist(ModelProvider<?> provider, String texturePath){
        return provider.existingFileHelper.exists(new ResourceLocation(MOD_ID,texturePath), DataGenHandle.TEXTURE);
    }

    public static boolean checkItemTextureFileExist(ModelProvider<?> provider, String texturePath){
        return checkTextureFileExist(provider, String.format("item/%s", texturePath));
    }

    public static boolean checkBlockTextureFileExist(ModelProvider<?> provider, String texturePath){
        return  checkTextureFileExist(provider,String.format("block/%s",texturePath ));
    }

    static ExistingModelFile existingModel(ModelProvider<?> provider, String path) {
        return provider.getExistingFile(provider.mcLoc(path));
    }

    static UncheckedModelFile uncheckedModel(String path) {
        return new UncheckedModelFile(path);
    }


//    static ExistingModelFile ItemGeneratedModel(ModelProvider<?> provider) {
//        return existingModel(provider, "item/generated");
//    }
//
//    public static ExistingModelFile ItemHeldModel(ModelProvider<?> provider) {
//        return existingModel(provider, "item/handheld");
//    }
//
//    static UncheckedModelFile BlockBuiltinEntity(ModelProvider<?> provider) {
//        return new UncheckedModelFile("builtin/entity");
//    }
//
//    static ExistingModelFile BlockCubeAll(ModelProvider<?> provider) {
//        return existingModel(provider, "block/cube_all");
//    }
//
//    static ExistingModelFile BlockCube(ModelProvider<?> provider) {
//        return existingModel(provider, "block/cube");
//    }


    @SubscribeEvent
    public static void processDataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ItemModelProvider itemModelProvider = new ItemModelProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void registerModels() {
                itemModelTasks.forEach((task) -> task.accept(this));
            }
        };

        BlockModelProvider blockModelProvider = new BlockModelProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void registerModels() {
                blockModelTasks.forEach((task) -> task.accept(this));
            }
        };

        BlockStateProvider blockStateProvider = new BlockStateProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void registerStatesAndModels() {
                blockStateTasks.forEach((task) -> task.accept(this));
            }
        };

        BlockTagsProvider blockTagsProvider = new BlockTagsProvider(generator, MOD_ID, existingFileHelper) {
            @Override
            protected void addTags() {
                AllBlocks.BLOCK_TAGS.forEach((block, tags) -> tags.forEach(
                                (t) -> tag(BlockTags.bind(t)).add(block.get())
                        )
                );
            }
        };

        LanguageProvider languageProviderEN = new LanguageProvider(generator,MOD_ID, "en_US") {
            @Override
            protected void addTranslations() {

            }
        };

       LanguageProvider languageProviderCN = new LanguageProvider(generator,MOD_ID, "zh_CN") {
            @Override
            protected void addTranslations() {

            }
        };

        itemModelPro.set(() -> itemModelProvider);
        blockModelPro.set(() -> blockModelProvider);
        blockStatePro.set(() -> blockStateProvider);
        blockTagsPro.set(() -> blockTagsProvider);


        generator.addProvider(itemModelProvider);
        generator.addProvider(blockModelProvider);
        generator.addProvider(blockStateProvider);
        generator.addProvider(blockTagsProvider);

//        generator.addProvider(languageProvider);
    }
}