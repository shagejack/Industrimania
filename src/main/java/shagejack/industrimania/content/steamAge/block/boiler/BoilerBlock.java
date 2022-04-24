package shagejack.industrimania.content.steamAge.block.boiler;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import shagejack.industrimania.content.steamAge.block.base.SteamBlockBase;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;

public class BoilerBlock extends SteamBlockBase implements ITE<BoilerTileEntity> {
    public BoilerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level level, BlockPos pos, Player player) {
        withTileEntityDo(level, pos, te -> {
            player.openMenu(te);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        });
    }

    @Override
    public Class<BoilerTileEntity> getTileEntityClass() {
        return BoilerTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends BoilerTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends BoilerTileEntity>) AllTileEntities.boiler.get();
    }
}
