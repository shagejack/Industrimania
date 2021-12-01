package shagejack.minecraftology.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundEvent;
import shagejack.minecraftology.Minecraftology;
import shagejack.minecraftology.blocks.BlockGravity;
import shagejack.minecraftology.blocks.includes.MCLBlock;
import shagejack.minecraftology.util.LogMCL;
import shagejack.minecraftology.util.MCLBlockHelper;
import shagejack.minecraftology.util.MCLMultiBlockCheckHelper;

public class BlockGravityCharcoal extends BlockGravity {

    private final String clay = "minecraftology:building.fine_clay";
    private final String tube = "minecraftology:mechanic.bronze_tube";
    private final String coal = "minecraftology:gravity.charcoal";

    //Multi Blocks
    private String[] structure = {
            clay,clay,clay,
            clay,     clay,
            clay,clay,clay,

            clay,clay,clay,
            clay,     clay,
            clay,clay,clay,

            clay,clay,clay,
            clay,     clay,
            clay,clay,clay,

            clay,clay,clay,
            clay,     clay,
            clay,clay,clay,

            clay,tube,clay,
            clay,coal,clay,
            clay,clay,clay,

            clay,clay,clay,
            clay,clay,clay,
            clay,clay,clay
    };

    private BlockPos[] posArr = {
            new BlockPos(-1, 5, 1),   new BlockPos(0, 5, 1),  new BlockPos(1, 5, 1),
            new BlockPos(-1, 5, 0),                                    new BlockPos(1, 5, 0),
            new BlockPos(-1, 5, -1),  new BlockPos(0, 5, -1),  new BlockPos(1, 5, -1),

            new BlockPos(-1, 4, 1),   new BlockPos(0, 4, 1),  new BlockPos(1, 4, 1),
            new BlockPos(-1, 4, 0),                                    new BlockPos(1, 4, 0),
            new BlockPos(-1, 4, -1),  new BlockPos(0, 4, -1),  new BlockPos(1, 4, -1),

            new BlockPos(-1, 3, 1),   new BlockPos(0, 3, 1),  new BlockPos(1, 3, 1),
            new BlockPos(-1, 3, 0),                                                           new BlockPos(1, 3, 0),
            new BlockPos(-1, 3, -1),  new BlockPos(0, 3, -1),  new BlockPos(1, 3, -1),

            new BlockPos(-1, 2, 1),   new BlockPos(0, 2, 1),  new BlockPos(1, 2, 1),
            new BlockPos(-1, 2, 0),                                                           new BlockPos(1, 2, 0),
            new BlockPos(-1, 2, -1),  new BlockPos(0, 2, -1),  new BlockPos(1, 2, -1),

            new BlockPos(-1, 1, 1),   new BlockPos(0, 1, 1),  new BlockPos(1, 1, 1),
            new BlockPos(-1, 1, 0),   new BlockPos(0, 1, 0),  new BlockPos(1, 1, 0),
            new BlockPos(-1, 1, -1),  new BlockPos(0, 1, -1),  new BlockPos(1, 1, -1),

            new BlockPos(-1, 0, 1),   new BlockPos(0, 0, 1),  new BlockPos(1, 0, 1),
            new BlockPos(-1, 0, 0),   new BlockPos(0, 0, 0),  new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, -1),  new BlockPos(0, 0, -1),  new BlockPos(1, 0, -1)
    };

    public BlockGravityCharcoal(Material material, String name) {
        super(material, name, 4, 0, 4);
        setSoundType(SoundType.SAND);
        setCreativeTab(Minecraftology.TAB_Minecraftology);
        setRotationType(MCLBlockHelper.RotationType.PREVENT);
        setTickRandomly(true);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
        world.scheduleUpdate(pos, this, this.tickRate(world));
        if (neighbor.equals(pos.up()) && world.getBlockState(neighbor).getBlock() == Blocks.FIRE){
            if (world.getBlockState(pos).getBlock() == Minecraftology.BLOCKS.gravity_charcoal) {
                int complete = debugCheckComplete(world, pos.down());
                if (complete != -1) {
                    world.setBlockToAir(neighbor);
                    world.setBlockState(pos.down(), Minecraftology.BLOCKS.mechanic_clay_furnace_bottom.getDefaultState());
                    world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.ENTITY_FIREWORK_TWINKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.5D, (double)pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.setBlockState(pos, Minecraftology.BLOCKS.gravity_dust.getDefaultState());
                }
            }
        }
    }

    public int checkComplete(World world, BlockPos pos) {
        int complete = -1;
        int completeState;
        completeState = MCLMultiBlockCheckHelper.checkComplete(world, pos, structure, posArr);
        if (completeState != -1) {
            IBlockState state = world.getBlockState(pos.add(MCLMultiBlockCheckHelper.getRotatedPos(new BlockPos(0, 1, 1), completeState)));
            EnumFacing rotation = state.getValue(MCLBlock.PROPERTY_DIRECTION);
            switch (completeState) {
                case -1:
                    break;
                case 0:
                    complete = (rotation == EnumFacing.NORTH || rotation == EnumFacing.SOUTH) ? 0 : -1;
                    break;
                case 1:
                    complete = (rotation == EnumFacing.WEST || rotation == EnumFacing.EAST) ? 1 : -1;
                    break;
                case 2:
                    complete = (rotation == EnumFacing.NORTH || rotation == EnumFacing.SOUTH) ? 2 : -1;
                    break;
                case 3:
                    complete = (rotation == EnumFacing.WEST || rotation == EnumFacing.EAST) ? 3 : -1;
                    break;
            }
        }
        return complete;
    }

    public int debugCheckComplete(World world, BlockPos pos){
        int complete = -1;
        int completeState;
        completeState = MCLMultiBlockCheckHelper.checkComplete(world, pos, structure, posArr, true);
        IBlockState state = world.getBlockState(pos.add(MCLMultiBlockCheckHelper.getRotatedPos(new BlockPos(0, 1, 1), completeState)));
        EnumFacing rotation = state.getValue(MCLBlock.PROPERTY_DIRECTION);
        if (completeState != -1) {
            switch (completeState) {
                case 0:
                    complete = (rotation == EnumFacing.NORTH || rotation == EnumFacing.SOUTH) ? 0 : -1;
                    if (complete == -1)
                        LogMCL.debug("Incomplete! Tube Rotation Tab should be WEST or EAST but actually " + rotation);
                    break;
                case 1:
                    complete = (rotation == EnumFacing.WEST || rotation == EnumFacing.EAST) ? 1 : -1;
                    if (complete == -1)
                        LogMCL.debug("Incomplete! Tube Rotation Tab should be NORTH or SOUTH but actually " + rotation);
                    break;
                case 2:
                    complete = (rotation == EnumFacing.NORTH || rotation == EnumFacing.SOUTH) ? 2 : -1;
                    if (complete == -1)
                        LogMCL.debug("Incomplete! Tube Rotation Tab should be WEST or EAST but actually " + rotation);
                    break;
                case 3:
                    complete = (rotation == EnumFacing.WEST || rotation == EnumFacing.EAST) ? 3 : -1;
                    if (complete == -1)
                        LogMCL.debug("Incomplete! Tube Rotation Tab should be NORTH or SOUTH but actually " + rotation);
                    break;
            }
        }
        LogMCL.debug("Result: " + complete);
        return complete;
    }




}
