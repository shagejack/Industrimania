package shagejack.industrimania.content.primalAge.block.stoneChoppingBoard;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registries.AllTileEntities;

import java.util.List;

public class StoneChoppingBoardTileEntity extends SmartTileEntity {

    private static final Object stoneChoppingBoardRecipeKey = new Object();
    public StoneChoppingBoardInventory inventory;
    private final LazyOptional<IItemHandler> invProvider;
    private int recipeIndex;

    public StoneChoppingBoardTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.stone_chopping_board.get(), pos, state);
        inventory = new StoneChoppingBoardInventory();
        invProvider = LazyOptional.of(() -> inventory);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    public void hit(ItemStack stack) {

    }


}
