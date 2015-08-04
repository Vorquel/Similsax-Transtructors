package vorquel.mod.similsaxtranstructors.proxy;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import vorquel.mod.similsaxtranstructors.helper.Log;
import vorquel.mod.similsaxtranstructors.helper.Ref;

import java.util.Hashtable;

public class Proxy {

    public void preInit(FMLPreInitializationEvent event) {
        Log.setLogger(event.getModLog());
    }

    public void init() {}

    public void postInit() {}
}
