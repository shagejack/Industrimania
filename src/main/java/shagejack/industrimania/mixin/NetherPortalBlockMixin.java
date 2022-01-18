package shagejack.industrimania.mixin;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shagejack.industrimania.registers.AllTags;

import java.util.List;
import java.util.Random;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @Inject(at = @At("HEAD"), method = "entityInside(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    private void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, CallbackInfo callback) {
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()) {
            if (entity.getType() == EntityType.PLAYER) {
                //TODO: use something other to get in nether portal
                if (((Player) entity).getInventory().contains(new ItemStack(Items.DIAMOND))) {
                    entity.handleInsidePortal(blockPos);
                } else {
                    entity.kill();
                }
            } else {
                entity.handleInsidePortal(blockPos);
            }
        }
        callback.cancel();
    }

    @Inject(at = @At("HEAD"), method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Ljava/util/Random;)V")
    private void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random, CallbackInfo callback) {

        final int SPREAD_FACTOR = 100;

        if (serverLevel.dimensionType().natural() && random.nextInt(SPREAD_FACTOR) < serverLevel.getDifficulty().getId()) {
            List<Block> netherInvasionBlocks = Lists.newArrayList(
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.NETHERRACK,
                    Blocks.SOUL_SAND,
                    Blocks.SOUL_SAND,
                    Blocks.SOUL_SAND,
                    Blocks.LAVA,
                    Blocks.NETHER_WART_BLOCK,
                    Blocks.WARPED_WART_BLOCK
            );

            List<Block> netherInvasionDecorative = Lists.newArrayList(
                    Blocks.NETHER_SPROUTS,
                    Blocks.CRIMSON_ROOTS,
                    Blocks.WARPED_ROOTS
            );

            List<Block> replaceableBlocks = Lists.newArrayList(
                    Blocks.GRASS_BLOCK,
                    Blocks.DIRT,
                    Blocks.DIRT_PATH,
                    Blocks.COARSE_DIRT,
                    Blocks.ROOTED_DIRT,
                    Blocks.SAND,
                    Blocks.GRAVEL,
                    Blocks.CLAY,
                    Blocks.ACACIA_LOG,
                    Blocks.BIRCH_LOG,
                    Blocks.DARK_OAK_LOG,
                    Blocks.JUNGLE_LOG,
                    Blocks.OAK_LOG,
                    Blocks.SPRUCE_LOG,
                    Blocks.BAMBOO
            );

            BlockPos nPos = blockPos;
            while(serverLevel.getBlockState(nPos).is(Blocks.NETHER_PORTAL)) {
                nPos = nPos.below();
            }

            int MAX_TRY = 100000;

            while(MAX_TRY > 0 && (serverLevel.getBlockState(nPos).is(Blocks.NETHER_PORTAL) || serverLevel.getBlockState(nPos).is(Blocks.OBSIDIAN) || netherInvasionBlocks.contains(serverLevel.getBlockState(nPos).getBlock()))) {

                switch(serverLevel.getRandom().nextInt(6)) {
                    case 0:
                        if (!serverLevel.getBlockState(nPos.above()).isAir()) {
                            if (!netherInvasionDecorative.contains(serverLevel.getBlockState(nPos.above()).getBlock())) {
                                nPos = nPos.above();
                            }
                        } else {
                            if(netherInvasionBlocks.contains(serverLevel.getBlockState(nPos).getBlock())) {
                                if (serverLevel.getRandom().nextDouble() < 0.001) {
                                    if (serverLevel.getRandom().nextDouble() < 0.1) {
                                        serverLevel.setBlock(nPos.above(), Blocks.FIRE.defaultBlockState(), 2 | 16);
                                    } else {
                                        serverLevel.setBlock(nPos.above(), netherInvasionDecorative.get(serverLevel.getRandom().nextInt(netherInvasionDecorative.size())).defaultBlockState(), 2 | 16);
                                    }
                                }

                                if (serverLevel.getRandom().nextBoolean()) {
                                    if (serverLevel.getRandom().nextBoolean()) {
                                        serverLevel.setBlock(nPos, Blocks.CRIMSON_NYLIUM.defaultBlockState(), 2 | 16);
                                        if (serverLevel.getRandom().nextDouble() < 0.2) {
                                            serverLevel.setBlock(nPos.above(), Blocks.CRIMSON_FUNGUS.defaultBlockState(), 2 | 16);
                                        }
                                    } else {
                                        serverLevel.setBlock(nPos, Blocks.WARPED_NYLIUM.defaultBlockState(), 2 | 16);
                                        if (serverLevel.getRandom().nextDouble() < 0.2) {
                                            serverLevel.setBlock(nPos.above(), Blocks.WARPED_FUNGUS.defaultBlockState(), 2 | 16);
                                        }
                                    }
                                }
                            }
                        };
                        break;
                    case 1:
                        if (!serverLevel.getBlockState(nPos.below()).isAir() && !netherInvasionDecorative.contains(serverLevel.getBlockState(nPos.below()).getBlock())) nPos = nPos.below();
                        break;
                    case 2:
                        if (!serverLevel.getBlockState(nPos.north()).isAir() && !netherInvasionDecorative.contains(serverLevel.getBlockState(nPos.north()).getBlock())) nPos = nPos.north();
                        break;
                    case 3:
                        if (!serverLevel.getBlockState(nPos.south()).isAir() && !netherInvasionDecorative.contains(serverLevel.getBlockState(nPos.south()).getBlock())) nPos = nPos.south();
                        break;
                    case 4:
                        if (!serverLevel.getBlockState(nPos.west()).isAir() && !netherInvasionDecorative.contains(serverLevel.getBlockState(nPos.west()).getBlock())) nPos = nPos.west();
                        break;
                    case 5:
                        if (!serverLevel.getBlockState(nPos.east()).isAir() && !netherInvasionDecorative.contains(serverLevel.getBlockState(nPos.east()).getBlock())) nPos = nPos.east();
                        break;
                    default:
                }

                MAX_TRY--;
            }

            if (MAX_TRY != 0) {

                Block block = serverLevel.getBlockState(nPos).getBlock();

                if (BlockTags.bind(AllTags.IndustrimaniaTags.igneousStones).getValues().contains(block) || BlockTags.bind(AllTags.IndustrimaniaTags.metamorphicStones).getValues().contains(block) || BlockTags.bind(AllTags.IndustrimaniaTags.sedimentaryStones).getValues().contains(block)) {
                    serverLevel.setBlock(nPos, netherInvasionBlocks.get(serverLevel.getRandom().nextInt(netherInvasionBlocks.size())).defaultBlockState(), 2 | 16);
                }

                if (serverLevel.getBlockState(nPos.above()).isAir()) {

                    if (serverLevel.getRandom().nextDouble() < 0.3) {
                        if (serverLevel.getRandom().nextDouble() < 0.1) {
                            serverLevel.setBlock(nPos.above(), Blocks.FIRE.defaultBlockState(), 2 | 16);
                        } else {
                            serverLevel.setBlock(nPos.above(), netherInvasionDecorative.get(serverLevel.getRandom().nextInt(netherInvasionDecorative.size())).defaultBlockState(), 2 | 16);
                        }
                    }

                    if (serverLevel.getRandom().nextBoolean()) {
                        if (serverLevel.getRandom().nextBoolean()) {
                            serverLevel.setBlock(nPos, Blocks.CRIMSON_NYLIUM.defaultBlockState(), 2 | 16);
                            if (serverLevel.getRandom().nextDouble() < 0.2) {
                                serverLevel.setBlock(nPos.above(), Blocks.CRIMSON_FUNGUS.defaultBlockState(), 2 | 16);
                            }
                        } else {
                            serverLevel.setBlock(nPos, Blocks.WARPED_NYLIUM.defaultBlockState(), 2 | 16);
                            if (serverLevel.getRandom().nextDouble() < 0.2) {
                                serverLevel.setBlock(nPos.above(), Blocks.WARPED_FUNGUS.defaultBlockState(), 2 | 16);
                            }
                        }
                    }
                }
            }

        }
    }

}
