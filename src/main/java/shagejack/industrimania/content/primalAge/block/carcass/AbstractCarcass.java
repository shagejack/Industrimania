package shagejack.industrimania.content.primalAge.block.carcass;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import shagejack.industrimania.content.misc.itemBase.Knife;

public abstract class AbstractCarcass extends Block {

    public AbstractCarcass(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        if (player.getItemInHand(handIn).getItem() instanceof Knife) {
            if (level.isClientSide)
                return InteractionResult.SUCCESS;

            dropItem(level, pos, onSkinned(level, pos));
        }

        return InteractionResult.PASS;
    }

    public void dropItem(Level level, BlockPos pos, ItemStack stack) {
        float f = EntityType.ITEM.getHeight() / 2.0F;
        double d0 = (double)((float)pos.getX() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D);
        double d1 = (double)((float)pos.getY() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D) - (double)f;
        double d2 = (double)((float)pos.getZ() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D);
        ItemEntity item = new ItemEntity(level, d0, d1, d2, stack);
        item.setDefaultPickUpDelay();
        level.addFreshEntity(item);
    }

    public abstract ItemStack onSkinned(Level level, BlockPos pos);

}
