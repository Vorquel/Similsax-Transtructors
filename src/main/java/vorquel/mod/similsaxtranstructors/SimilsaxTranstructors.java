package vorquel.mod.similsaxtranstructors;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static vorquel.mod.similsaxtranstructors.Config.showOverlay;

@Mod(modid = SimilsaxTranstructors.MOD_ID, name = "Similsax Transtructors", version = "@MOD_VERSION@")
public class SimilsaxTranstructors {

    public static final String MOD_ID = "SimilsaxTranstructors";
    public static final ItemDummy itemDummy = new ItemDummy("similsaxTranstructor");
    public static final ItemSimilsaxTranstructor itemBasic = new ItemSimilsaxTranstructor("Basic");
    public static final ItemSimilsaxTranstructor itemAdvanced = new ItemSimilsaxTranstructor("Advanced");

    @SidedProxy(clientSide = "vorquel.mod.similsaxtranstructors.ClientProxy", serverSide = "vorquel.mod.similsaxtranstructors.Proxy")
    static Proxy proxy;

    static Logger log = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());
        ConfigSynchonizer.init();
        GameRegistry.register(itemDummy, new ResourceLocation(MOD_ID, "similsaxTranstructor"));
        GameRegistry.register(itemBasic, new ResourceLocation(MOD_ID, "similsaxTranstructorBasic"));
        GameRegistry.register(itemAdvanced, new ResourceLocation(MOD_ID, "similsaxTranstructorAdvanced"));
        proxy.registerItemModel();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if(showOverlay) proxy.registerBlockOverlay();
        MinecraftForge.EVENT_BUS.register(new ConfigSynchonizer());
        GameRegistry.addRecipe(new ItemStack(itemBasic),
                "x x",
                "xox",
                " / ",
                'x', Items.IRON_INGOT,
                'o', Items.ENDER_PEARL,
                '/', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(itemAdvanced),
                "o o",
                "oxo",
                " x ",
                'x', Items.DIAMOND,
                'o', Items.ENDER_PEARL);
        GameRegistry.addRecipe(new IRecipe() {
            @Override
            public boolean matches(InventoryCrafting inventory, World worldIn) {
                int count = 0;
                for(int i = 0; i < inventory.getSizeInventory(); ++i) {
                    if(inventory.getStackInSlot(i) == null)
                        continue;
                    if(inventory.getStackInSlot(i).getItem() != itemDummy)
                        return false;
                    ++count;
                }
                return count == 1;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inventory) {
                for(int i = 0; i < inventory.getSizeInventory(); ++i) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if(stack == null || stack.getItem() != itemDummy) continue;
                    int damage = stack.getItemDamage();
                    if(damage < 0x1000)
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

            @Override
            public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_) {
                return ForgeHooks.defaultRecipeGetRemainingItems(p_179532_1_);
            }
        });
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        log.info("Syncing Server configs");
        itemBasic.setUses(Config.basicUses);
        itemAdvanced.setUses(Config.advancedUses);
        itemBasic.range = Config.basicRange;
        itemAdvanced.range = Config.advancedRange;
    }
}
