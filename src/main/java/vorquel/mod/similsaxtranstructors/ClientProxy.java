package vorquel.mod.similsaxtranstructors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends Proxy {

    @Override
    public void registerBlockOverlay() {
        MinecraftForge.EVENT_BUS.register(new BlockOverlay());
    }

    @Override
    public void registerItemModel() {
        ModelLoader.setCustomMeshDefinition(SimilsaxTranstructors.itemSimilsaxTranstructor, new ItemMeshDefinition() {
            private final ModelResourceLocation basicLocation =
                    new ModelResourceLocation("similsaxtranstructors:similsaxTranstructorBasic", "inventory");
            private final ModelResourceLocation advancedLocation =
                    new ModelResourceLocation("similsaxtranstructors:similsaxTranstructorAdvanced", "inventory");
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return stack.getItemDamage() < ItemSimilsaxTranstructor.advancedThreshold ? basicLocation : advancedLocation;
            }
        });
        ModelBakery.addVariantName(SimilsaxTranstructors.itemSimilsaxTranstructor,
                "similsaxtranstructors:similsaxTranstructorBasic", "similsaxtranstructors:similsaxTranstructorAdvanced");
    }

    @Override
    public void addScheduledTask(Runnable runnable) {
        Minecraft.getMinecraft().addScheduledTask(runnable);
    }
}
