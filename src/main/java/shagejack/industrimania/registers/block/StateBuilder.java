package shagejack.industrimania.registers.block;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.contraptions.base.BlockDirectionalBase;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import java.util.Objects;
import java.util.function.Consumer;

public interface StateBuilder {

    BlockBuilder asBase();

    default BlockBuilder blockState(Consumer<BlockStateProvider> blockStateProviderConsumer) {
        //Objects.requireNonNull(block);
        DataGenHandle.addBlockStateTask(blockStateProviderConsumer);
        return (BlockBuilder) this;
    }

    default BlockBuilder complexBlockState(Consumer<BlockStateProvider> blockStateProviderConsumer) {
        return blockState((provider) -> {

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
