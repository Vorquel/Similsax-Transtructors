package vorquel.mod.similsaxtranstructors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemDummy extends Item {

    public ItemDummy(String name) {
        setUnlocalizedName(name);
        setMaxStackSize(1);
        setMaxDamage(Short.MAX_VALUE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.translateToLocal("item.similsaxTranstructor.tooltip"));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }
}
