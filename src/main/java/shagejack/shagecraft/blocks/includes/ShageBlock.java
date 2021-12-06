package shagejack.shagecraft.blocks.includes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.Reference;
import shagejack.shagecraft.api.internal.ItemModelProvider;
import shagejack.shagecraft.tile.ShageTileEntity;
import shagejack.shagecraft.util.ShageBlockHelper;

import static shagejack.shagecraft.util.ShageBlockHelper.RotationType;
import static shagejack.shagecraft.util.ShageBlockHelper.SIDE_LEFT;

import javax.annotation.Nonnull;

public class ShageBlock extends Block implements ItemModelProvider {

    public static final PropertyDirection PROPERTY_DIRECTION = PropertyDirection.create("facing");
    protected AxisAlignedBB boundingBox = FULL_BLOCK_AABB;
    private BlockStateContainer blockState;
    private boolean hasRotation;
    private ShageBlockHelper.RotationType rotationType;

    public ShageBlock(Material material, String name) {
        super(material);
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        this.blockState = createBlockState();
        this.setDefaultState(getBlockState().getBaseState());
        this.fullBlock = getDefaultState().isOpaqueCube();
        this.lightOpacity = fullBlock ? 255 : 0;
        this.setTranslationKey(name);
        setCreativeTab(Shagecraft.TAB_Shagecraft);
        rotationType = ShageBlockHelper.RotationType.FOUR_WAY;
    }

    @Override
    public void initItemModel() {
        NonNullList<ItemStack> sub = NonNullList.create();
        getSubBlocks(CreativeTabs.SEARCH, sub);
        for (ItemStack stack : sub) {
            ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(), new ModelResourceLocation(getRegistryName(), "inventory"));
        }
    }

    @Nonnull
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (hasRotation) {
            return new BlockStateContainer(this, PROPERTY_DIRECTION);
        }
        return super.createBlockState();
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        if (hasRotation) {
            return getDefaultState().withProperty(PROPERTY_DIRECTION, EnumFacing.byIndex(meta));
        } else {
            return getDefaultState();
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (hasRotation) {
            EnumFacing facing = state.getValue(PROPERTY_DIRECTION);
            return facing.getIndex();
        } else {
            return 0;
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);

        ShageTileEntity tileEntity = (ShageTileEntity) worldIn.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.onAdded(worldIn, pos, state);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
        super.neighborChanged(state, world, pos, blockIn, neighbor);
        ShageTileEntity tileEntity = (ShageTileEntity) world.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.onNeighborBlockChange(world, pos, world.getBlockState(pos), world.getBlockState(neighbor).getBlock());
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (hasRotation) {
            return getDefaultState().withProperty(PROPERTY_DIRECTION, rotationType == RotationType.FOUR_WAY ? placer.getHorizontalFacing().getOpposite() : EnumFacing.getDirectionFromEntityLiving(pos, placer));
        }
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (rotationType != RotationType.PREVENT) {
            IBlockState state = world.getBlockState(pos);
            for (IProperty prop : state.getProperties().keySet()) {
                if (prop.getName().equals(PROPERTY_DIRECTION)) {
                    EnumFacing facing = state.getValue(PROPERTY_DIRECTION);

                    if (rotationType == RotationType.FOUR_WAY) {
                        facing = EnumFacing.VALUES[SIDE_LEFT[facing.ordinal() % SIDE_LEFT.length]];
                    } else if (rotationType == RotationType.SIX_WAY) {
                        if (facing.ordinal() < 6) {
                            facing = EnumFacing.VALUES[(facing.ordinal() + 1) % 6];
                        }
                    }

                    world.setBlockState(pos, world.getBlockState(pos).withProperty(PROPERTY_DIRECTION, facing), 3);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (hasTileEntity(state) && worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof ShageTileEntity) {
            ((ShageTileEntity) worldIn.getTileEntity(pos)).onDestroyed(worldIn, pos, state);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public void setRotationType(RotationType type) {
        rotationType = type;
    }

    public void setHasRotation() {
        this.hasRotation = true;
        this.blockState = createBlockState();
        setDefaultState(blockState.getBaseState().withProperty(PROPERTY_DIRECTION, rotationType == RotationType.PREVENT ? EnumFacing.DOWN : rotationType == RotationType.FOUR_WAY ? EnumFacing.NORTH : EnumFacing.UP));
    }

    @Override
    public BlockStateContainer getBlockState() {
        return this.blockState;
    }
}
