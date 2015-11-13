package vorquel.mod.similsaxtranstructors;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import static vorquel.mod.similsaxtranstructors.Config.*;

@Mod(modid = SimilsaxTranstructors.MOD_ID, name = "Similsax Transtructors", version = "@MOD_VERSION@")
public class SimilsaxTranstructors {

    public static final String MOD_ID = "SimilsaxTranstructors";
    public static final ItemDummy itemDummy = new ItemDummy("similsaxTranstructor");
    public static final ItemSimilsaxTranstructor itemBasic = new ItemSimilsaxTranstructor("Basic");
    public static final ItemSimilsaxTranstructor itemAdvanced = new ItemSimilsaxTranstructor("Advanced");

    @SidedProxy(clientSide = "vorquel.mod.similsaxtranstructors.ClientProxy", serverSide = "vorquel.mod.similsaxtranstructors.Proxy")
    private static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());
        ConfigSynchonizer.init();
        GameRegistry.registerItem(itemDummy, "similsaxTranstructor");
        GameRegistry.registerItem(itemBasic, "similsaxTranstructorBasic");
        GameRegistry.registerItem(itemAdvanced, "similsaxTranstructorAdvanced");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if(showOverlay) proxy.registerBlockOverlay();
        FMLCommonHandler.instance().bus().register(new ConfigSynchonizer());
        GameRegistry.addRecipe(new ItemStack(itemBasic),
                "x x",
                "xox",
                " / ",
                'x', Items.iron_ingot,
                'o', Items.ender_pearl,
                '/', Items.stick);
        GameRegistry.addRecipe(new ItemStack(itemAdvanced),
                "o o",
                "oxo",
                " x ",
                'x', Items.diamond,
                'o', Items.ender_pearl);
        GameRegistry.addRecipe(new IRecipe() {
            @Override
            public boolean matches(InventoryCrafting inventory, World worldIn) {
                int count = 0;
                for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                    if (inventory.getStackInSlot(i) == null)
                        continue;
                    if (inventory.getStackInSlot(i).getItem() != itemDummy)
                        return false;
                    ++count;
                }
                return count == 1;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inventory) {
                for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (stack == null || stack.getItem() != itemDummy) continue;
                    int damage = stack.getCurrentDurability();
                    if (damage < 0x1000)
                        return new ItemStack(itemBasic, 1, damage);
                    else
                        return new ItemStack(itemAdvanced, 1, damage - 0x1000);
                }
                return new ItemStack(itemBasic);
            }

            @Override
            public int getRecipeSize() {
                return 1;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return null;
            }
        });
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        itemBasic.setUses(Config.basicUses);
        itemAdvanced.setUses(Config.advancedUses);
        itemBasic.range = basicRange;
        itemAdvanced.range = advancedRange;
    }
}
