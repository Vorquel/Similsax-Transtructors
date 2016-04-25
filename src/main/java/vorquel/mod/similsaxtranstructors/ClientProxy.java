package vorquel.mod.similsaxtranstructors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

import static vorquel.mod.similsaxtranstructors.SimilsaxTranstructors.*;

public class ClientProxy extends Proxy {

    @Override
    public void registerBlockOverlay() {
        MinecraftForge.EVENT_BUS.register(new BlockOverlay());
    }

    @Override
    public void registerItemModel() {
        ModelLoader.setCustomModelResourceLocation(itemDummy, 0, new ModelResourceLocation(MOD_ID + ":similsaxTranstructor", "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemBasic, 0, new ModelResourceLocation(MOD_ID + ":similsaxTranstructorBasic", "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemAdvanced, 0, new ModelResourceLocation(MOD_ID + ":similsaxTranstructorAdvanced", "inventory"));
    }

    @Override
    public void addScheduledTask(Runnable runnable) {
        Minecraft.getMinecraft().addScheduledTask(runnable);
    }
}
