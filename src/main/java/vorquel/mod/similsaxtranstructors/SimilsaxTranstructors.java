package vorquel.mod.similsaxtranstructors;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static vorquel.mod.similsaxtranstructors.Config.showOverlay;

@Mod(modid = SimilsaxTranstructors.MOD_ID, name = "Similsax Transtructors", version = "@MOD_VERSION@")
public class SimilsaxTranstructors {

    public static final String MOD_ID = "SimilsaxTranstructors";
    public static final ItemSimilsaxTranstructor itemSimilsaxTranstructor = new ItemSimilsaxTranstructor();

    @SidedProxy(clientSide = "vorquel.mod.similsaxtranstructors.ClientProxy", serverSide = "vorquel.mod.similsaxtranstructors.Proxy")
    private static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());
        ServerConfig.init();
        GameRegistry.registerItem(itemSimilsaxTranstructor, "similsaxTranstructor");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if(showOverlay) proxy.registerBlockOverlay();
        FMLCommonHandler.instance().bus().register(new ServerConfig());
        GameRegistry.addRecipe(new ItemStack(itemSimilsaxTranstructor, 1, 0),
                "x x",
                "xox",
                " / ",
                'x', Items.iron_ingot,
                'o', Items.ender_pearl,
                '/', Items.stick);
        GameRegistry.addRecipe(new ItemStack(itemSimilsaxTranstructor, 1, ItemSimilsaxTranstructor.advancedThreshold),
                "o o",
                "oxo",
                " x ",
                'x', Items.diamond,
                'o', Items.ender_pearl);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        ItemSimilsaxTranstructor item = SimilsaxTranstructors.itemSimilsaxTranstructor;
        item.basicUses = Config.basicUses;
        item.advancedUses = Config.advancedUses;
        item.basicRange = Config.basicRange;
        item.advancedRange = Config.advancedRange;
    }
}
