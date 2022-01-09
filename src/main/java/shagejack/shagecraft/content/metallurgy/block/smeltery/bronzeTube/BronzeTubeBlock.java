package shagejack.shagecraft.content.metallurgy.block.smeltery.bronzeTube;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import shagejack.shagecraft.content.contraptions.base.BlockDirectionalBase;
import shagejack.shagecraft.content.metallurgy.block.smeltery.ironOreSlag.IronOreSlagTileEntity;
import shagejack.shagecraft.foundation.block.ITE;
import shagejack.shagecraft.registers.AllTileEntities;


public class BronzeTubeBlock extends BlockDirectionalBase implements ITE<BronzeTubeTileEntity> {

    public BronzeTubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<BronzeTubeTileEntity> getTileEntityClass() {
        return BronzeTubeTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends BronzeTubeTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends BronzeTubeTileEntity>) AllTileEntities.bronze_tube.get();
    }



}
