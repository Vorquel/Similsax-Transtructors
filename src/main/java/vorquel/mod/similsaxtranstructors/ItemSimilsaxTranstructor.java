package vorquel.mod.similsaxtranstructors;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.util.EnumActionResult.PASS;
import static net.minecraft.util.EnumActionResult.SUCCESS;

public class ItemSimilsaxTranstructor extends Item {

    public int range = 0;

    public ItemSimilsaxTranstructor(String name) {
        setCreativeTab(CreativeTabs.TOOLS);
        setUnlocalizedName("similsaxTranstructor" + name);
        setMaxStackSize(1);
    }

    public void setUses(int uses) {
        setMaxDamage(uses - 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
    
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.isRemote) return SUCCESS;
        //check if you can place a block
        if(getMaxDamage() == 0) return PASS;
        if(!player.capabilities.allowEdit) return PASS;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if(block.isReplaceable(world, pos)) return PASS;
        if(block.hasTileEntity(state)) return PASS;
        ItemStack blockStack = new ItemStack(block, 1, block.damageDropped(state));
        if(!player.capabilities.isCreativeMode && !player.inventory.hasItemStack(blockStack)) return PASS;
        return tower(stack, player, block, state, world, pos, getSide(facing.getIndex(), hitX, hitY, hitZ), blockStack);
    }

    private EnumActionResult tower(ItemStack stack, EntityPlayer player, Block block, IBlockState state, World world, BlockPos pos, int side, ItemStack blockStack) {
        return tower(stack, player, block, state, world, pos, EnumFacing.getFront(side).getOpposite(), blockStack, range);
    }

    private EnumActionResult tower(ItemStack stack, EntityPlayer player, Block block, IBlockState state, World world, BlockPos pos, EnumFacing side, ItemStack blockStack, int range) {
        if(range == 0) return PASS;
        IBlockState otherState = world.getBlockState(pos);
        Block otherBlock = otherState.getBlock();
        if(block == otherBlock && state.getProperties().equals(otherState.getProperties()))
            return tower(stack, player, block, state, world, pos.offset(side), side, blockStack, range-1);
        else if(otherBlock.isReplaceable(world, pos)) {
            if(!world.canBlockBePlaced(block, pos, false, side.getOpposite(), null, blockStack)) return PASS;
            stack.damageItem(1, player);
            if(stack.stackSize == 0)
                world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
            if(!player.capabilities.isCreativeMode) {
                for(int i=0; i<player.inventory.mainInventory.length; ++i) {
                    ItemStack localStack = player.inventory.getStackInSlot(i);
                    if(localStack == null) continue;
                    if(localStack.isItemEqual(blockStack)) {
                        player.inventory.decrStackSize(i, 1);
                        player.openContainer.detectAndSendChanges();
                        break;
                    }
                }
            }
            world.setBlockState(pos, state);
            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    block.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, (block.getSoundType().volume + 1.0F) / 2.0F, block.getSoundType().pitch * 0.8F, true);
            return SUCCESS;
        } else
            return PASS;
    }

    private static final int[] sidesXY = new int[]{4, 5, 0, 1};
    private static final int[] sidesYZ = new int[]{0, 1, 2, 3};
    private static final int[] sidesZX = new int[]{2, 3, 4, 5};

    public static int getSide(int side, double xIn, double yIn, double zIn) {
        //if the middle was clicked, place on the opposite side
        float lo = .25f, hi = .75f;
        int centeredSides = 0;
        if(side != 0 && side != 1)
            centeredSides += yIn>lo && yIn<hi ? 1 : 0;
        if(side != 2 && side != 3)
            centeredSides += zIn>lo && zIn<hi ? 1 : 0;
        if(side != 4 && side != 5)
            centeredSides += xIn>lo && xIn<hi ? 1 : 0;
        if(centeredSides == 2)
            return side;

        //otherwise, place on the nearest side
        double left, right;
        int[] sides;
        switch(side) {
            case 0:
            case 1:
                left = zIn;
                right = xIn;
                sides = sidesZX;
                break;
            case 2:
            case 3:
                left = xIn;
                right = yIn;
                sides = sidesXY;
                break;
            case 4:
            case 5:
                left = yIn;
                right = zIn;
                sides = sidesYZ;
                break;
            default:
                return -1;
        }
        boolean b0 = left > right;
        boolean b1 = left > 1-right;
        if(b0 && b1)
            return sides[0];
        else if(!b0 && !b1)
            return sides[1];
        else if(b1)
            return sides[2];
        else
            return sides[3];
    }
}
