package vorquel.mod.similsaxtranstructors;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = SimilsaxTranstructors.MOD_ID, name = "Similsax Transtructors", version = "@MOD_VERSION@")
public class SimilsaxTranstructors {

    public static final String MOD_ID = "SimilsaxTranstructors";
    public ItemSimilsaxTranstructor itemSimilsaxTranstructor = new ItemSimilsaxTranstructor("similsaxTranstructor");

    @SidedProxy(clientSide = "vorquel.mod.similsaxtranstructors.ClientProxy", serverSide = "vorquel.mod.similsaxtranstructors.Proxy")
    public static Proxy proxy;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        int basicUses = config.getInt("basicUses", "general", 200, 1, ItemSimilsaxTranstructor.advancedThreshold - 1, "How many times you can use the basic transtructor");
        int advancedUses = config.getInt("advancedUses", "general", 1000, 1, Short.MAX_VALUE - ItemSimilsaxTranstructor.advancedThreshold, "How many times you can use the advanced transtructor");
        int basicRange = config.getInt("basicRange", "general", 16, 2, 128, "How far you can use the basic transtructor");
        int advancedRange = config.getInt("advancedRange", "general", 64, 2, 128, "How far you can use the advanced transtructor");
        if(config.hasChanged()) config.save();
        itemSimilsaxTranstructor = new ItemSimilsaxTranstructor("similsaxTranstructor", basicUses, advancedUses, basicRange, advancedRange);
        GameRegistry.registerItem(itemSimilsaxTranstructor, "similsaxTranstructor");
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        proxy.registerBlockOverlay();
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
}
