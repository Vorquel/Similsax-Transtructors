package vorquel.mod.similsaxtranstructors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemSimilsaxTranstructor extends Item {

    private final IIcon[] icons = new IIcon[2];
    public static final int advancedThreshold = 0x1000;
    int basicUses;
    int advancedUses;
    int basicRange;
    int advancedRange;

    public ItemSimilsaxTranstructor() {
        setCreativeTab(CreativeTabs.tabTools);
        setUnlocalizedName("similsaxTranstructor");
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons[0] = iconRegister.registerIcon("similsaxtranstructors:similsaxTranstructorBasic");
        icons[1] = iconRegister.registerIcon("similsaxtranstructors:similsaxTranstructorAdvanced");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List subItems) {
        subItems.add(new ItemStack(item, 1, 0));
        subItems.add(new ItemStack(item, 1, advancedThreshold));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return damage < advancedThreshold ? icons[0] : icons[1];
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        int damage = stack.getItemDamage();
        return damage != 0 && damage != advancedThreshold;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int damage = stack.getItemDamage();
        if(damage < advancedThreshold)
            return ((double) damage)/ basicUses;
        else {
            damage -= advancedThreshold;
            return ((double) damage)/ advancedUses;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + (stack.getItemDamage() < advancedThreshold ? "Basic" : "Advanced");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xIn, float yIn, float zIn) {
        if(world.isRemote) return true;

        //check if you can place a block
        if(!player.capabilities.allowEdit) return false;
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if(block.isReplaceable(world, x, y, z)) return false;
        if(block.hasTileEntity(meta)) return false;
        ItemStack blockStack = new ItemStack(block, 1, block.damageDropped(meta));
        if(!player.capabilities.isCreativeMode && !player.inventory.hasItemStack(blockStack)) return false;
        return tower(stack, player, block, meta, world, x, y, z, getSide(side, xIn, yIn, zIn), blockStack);
    }

    private boolean tower(ItemStack stack, EntityPlayer player, Block block, int meta, World world, int x, int y, int z, int side, ItemStack blockStack) {
        return tower(stack, player, block, meta, world, x, y, z, side, blockStack, stack.getItemDamage() < advancedThreshold ? basicRange : advancedRange);
    }

    private boolean tower(ItemStack stack, EntityPlayer player, Block block, int meta, World world, int x, int y, int z, int side, ItemStack blockStack, int range) {
        if(range == 0) return false;
        Block otherBlock = world.getBlock(x, y, z);
        int otherMeta = world.getBlockMetadata(x, y, z);
        if(block == otherBlock && meta == otherMeta) {
            switch(side) {
                case 0: return tower(stack, player, block, meta, world, x, y+1, z, side, blockStack, range-1);
                case 1: return tower(stack, player, block, meta, world, x, y-1, z, side, blockStack, range-1);
                case 2: return tower(stack, player, block, meta, world, x, y, z+1, side, blockStack, range-1);
                case 3: return tower(stack, player, block, meta, world, x, y, z-1, side, blockStack, range-1);
                case 4: return tower(stack, player, block, meta, world, x+1, y, z, side, blockStack, range-1);
                case 5: return tower(stack, player, block, meta, world, x-1, y, z, side, blockStack, range-1);
                default: return false;
            }
        } else if(otherBlock.isReplaceable(world, x, y, z)) {
            if(!world.canPlaceEntityOnSide(block, x, y, z, false, side, null, blockStack)) return false;
            if(!player.capabilities.isCreativeMode) {
                int damage = stack.getItemDamage()+1;
                if(damage == basicUses || damage == advancedThreshold + advancedUses) {
                    player.destroyCurrentEquippedItem();
                    world.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);
                }
                stack.setItemDamage(damage);
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
            world.setBlock(x, y, z, block, meta, 3);
            world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5,
                    block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
            return true;
        } else
            return false;
    }

    private static final int[] sidesXY = new int[]{4, 5, 0, 1};
    private static final int[] sidesYZ = new int[]{0, 1, 2, 3};
    private static final int[] sidesZX = new int[]{2, 3, 4, 5};

    public static int getSide(int side, double xIn, double yIn, double zIn) {
        //if the middle was clicked, place on the opposite side
        float lo = .25f, hi = .75f;
        int centeredSides = 0;
        centeredSides += xIn>lo && xIn<hi ? 1 : 0;
        centeredSides += yIn>lo && yIn<hi ? 1 : 0;
        centeredSides += zIn>lo && zIn<hi ? 1 : 0;
        if(centeredSides >= 2)
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
