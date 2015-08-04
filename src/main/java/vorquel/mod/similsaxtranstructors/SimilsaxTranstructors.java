package vorquel.mod.similsaxtranstructors;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import vorquel.mod.similsaxtranstructors.helper.Ref;
import vorquel.mod.similsaxtranstructors.proxy.Proxy;

@Mod(modid = Ref.MOD_ID, name = "Similsax Transtructors", version = "@MOD_VERSION@")
public class SimilsaxTranstructors {

    @Mod.Instance(Ref.MOD_ID)
    @SuppressWarnings("unused")
    public static SimilsaxTranstructors instance;

    @SidedProxy(serverSide = "vorquel.mod.similsaxtranstructors.proxy.Proxy", clientSide = "vorquel.mod.similsaxtranstructors.proxy.ProxyClient")
    public static Proxy proxy;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
