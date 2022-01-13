package shagejack.industrimania.content.metallurgyAge.block.smeltery.bronzeTube;

import net.minecraft.world.level.block.entity.BlockEntityType;
import shagejack.industrimania.content.contraptions.base.BlockDirectionalBase;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;


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
