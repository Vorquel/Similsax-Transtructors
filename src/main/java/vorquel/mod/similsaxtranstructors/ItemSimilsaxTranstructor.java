package vorquel.mod.similsaxtranstructors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemSimilsaxTranstructor extends Item{

    private IIcon[] icons = new IIcon[2];

    public ItemSimilsaxTranstructor(String unlocalizedName) {
        setCreativeTab(CreativeTabs.tabTools);
        setUnlocalizedName(unlocalizedName);
        setMaxStackSize(1);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        icons[0] = iconRegister.registerIcon("similsaxtranstructors:similsaxTranstructorBasic");
        icons[1] = iconRegister.registerIcon("similsaxtranstructors:similsaxTranstructorAdvanced");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs tabs, List subItems) {
        subItems.add(new ItemStack(item, 1, 0));
        subItems.add(new ItemStack(item, 1, 0x1000));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return damage < 0x1000 ? icons[0] : icons[1];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + (stack.getItemDamage() < 0x1000 ? "Basic" : "Advanced");
    }
}
