package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.registries.block.AllBlocks;

public class ItemPlaceableBase extends Item {

    public ItemPlaceableBase(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockPos);

        if (player != null) {
            if (!blockstate.isAir()) {
                BlockPos clickPos = blockPos.offset(useOnContext.getClickedFace().getNormal());
                if (level.getBlockState(clickPos).isAir()) {
                    level.setBlock(clickPos, AllBlocks.mechanic_item_placeable.block().get().defaultBlockState(), 2 | 16);
                    BlockEntity posTe = level.getBlockEntity(clickPos);
                    if (posTe instanceof ItemPlaceableBaseTileEntity) {
                        if (((ItemPlaceableBaseTileEntity) posTe).addItem(useOnContext.getItemInHand().split(1))) {
                            return InteractionResult.sidedSuccess(level.isClientSide());
                        }
                    }
                }
            }
        }

        return InteractionResult.FAIL;
    }

}
