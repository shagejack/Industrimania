package shagejack.industrimania.registers.block;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.contraptions.blockBase.BlockDirectionalBase;
import shagejack.industrimania.registers.block.grouped.AsBase;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import java.util.Objects;
import java.util.function.Consumer;

public interface StateBuilder extends AsBase {

    default BlockBuilder blockState(Consumer<BlockStateProvider> blockStateProviderConsumer) {
        //Objects.requireNonNull(block);
        DataGenHandle.addBlockStateTask(blockStateProviderConsumer);
        return (BlockBuilder) this;
    }

    default BlockBuilder complexBlockState(Consumer<BlockStateProvider> blockStateProviderConsumer) {
        return blockState((provider) -> {

        });
    }

    default BlockBuilder snowLikeBlockState() {
        return blockState((provider) -> {
            final var base = this.asBase();
            var block = base.block;
            Industrimania.LOGGER.debug("set snow like block state file for Block:{}", base.name);
            for (int i = 0; i < 7; i++) {
                var modelFile = new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + base.name + "_height" + (2 + 2 * i)));
                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .partialState()
                        .with(BlockStateProperties.LAYERS, i + 1)
                        .modelForState()
                        .modelFile(modelFile)
                        .addModel();
            }

            var fullModelFile = new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + base.name.split("_")[0] + "_" + base.name.split("_")[1] + "_block"));
            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(BlockStateProperties.LAYERS, 8)
                    .modelForState()
                    .modelFile(fullModelFile)
                    .addModel();
        });
    }

    default BlockBuilder cropBlockState(int maxAge) {
        return blockState((provider) -> {
            final var base = this.asBase();
            var block = base.block;
            Industrimania.LOGGER.debug("set block state file for Block:{}", base.name);

            IntegerProperty property;

            switch (maxAge) {
                case 1 -> property = BlockStateProperties.AGE_1;
                case 2 -> property = BlockStateProperties.AGE_2;
                case 3 -> property = BlockStateProperties.AGE_3;
                case 5 -> property = BlockStateProperties.AGE_5;
                case 7 -> property = BlockStateProperties.AGE_7;
                case 15 -> property = BlockStateProperties.AGE_15;
                default -> property = BlockStateProperties.AGE_25;
            }


            for (int i = 0; i <= maxAge; i++) {
                var modelFile = new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + base.name + "_stage" + i));

                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .partialState()
                        .with(property, i)
                        .modelForState()
                        .modelFile(modelFile)
                        .rotationY(180)
                        .addModel();
            }

        });
    }

    default BlockBuilder simpleBlockState() {
        return blockState((provider) -> {
            final var base = this.asBase();
            var block = base.block;
            Industrimania.LOGGER.debug("set block state file for Block:{}", base.name);
            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .forAllStates(state -> ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + base.name))).build());
        });
    }

    default BlockBuilder binaryBlockState(BooleanProperty property) {
        return blockState((provider) -> {
            var base = asBase();
            var block = base.block;

            Industrimania.LOGGER.debug("set binary block state for Block:{}", base.name);

            var modelFileTrue = new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + base.name));
            var modelFileFalse = new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + base.name + "_0"));

            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(property, true)
                    .modelForState()
                    .modelFile(modelFileTrue)
                    .addModel();

            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(property, false)
                    .modelForState()
                    .modelFile(modelFileFalse)
                    .addModel();

        });
    }

    default BlockBuilder rotatableBlockState() {
        return blockState((provider) -> {
            var base = asBase();
            var block = base.block;
            var modelFile = new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + base.name));

            Industrimania.LOGGER.debug("set rotatable block state for Block:{}", base.name);

            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(BlockDirectionalBase.FACING, Direction.NORTH)
                    .modelForState()
                    .modelFile(modelFile)
                    .rotationY(0)
                    .addModel();

            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(BlockDirectionalBase.FACING, Direction.SOUTH)
                    .modelForState()
                    .modelFile(modelFile)
                    .rotationY(180)
                    .addModel();

            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(BlockDirectionalBase.FACING, Direction.WEST)
                    .modelForState()
                    .modelFile(modelFile)
                    .rotationY(90)
                    .addModel();

            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(BlockDirectionalBase.FACING, Direction.EAST)
                    .modelForState()
                    .modelFile(modelFile)
                    .rotationY(270)
                    .addModel();

            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(BlockDirectionalBase.FACING, Direction.UP)
                    .modelForState()
                    .modelFile(modelFile)
                    .rotationX(270)
                    .addModel();

            provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                    .partialState()
                    .with(BlockDirectionalBase.FACING, Direction.DOWN)
                    .modelForState()
                    .modelFile(modelFile)
                    .rotationX(90)
                    .addModel();
        });
    }

}
