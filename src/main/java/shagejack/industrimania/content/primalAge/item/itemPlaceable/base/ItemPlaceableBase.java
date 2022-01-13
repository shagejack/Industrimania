package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ItemPlaceableBase extends Item {

    public ItemPlaceableBase(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockPos);

        if (!blockstate.isAir()) {
            BlockEntity te = level.getBlockEntity(blockPos);
            if (te instanceof ItemPlaceableBaseTileEntity) {
                if (((ItemPlaceableBaseTileEntity) te).addItem(itemStack.getItem().getRegistryName().toString())) {
                    itemStack.shrink(1);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }

        return InteractionResult.FAIL;
    }

}
