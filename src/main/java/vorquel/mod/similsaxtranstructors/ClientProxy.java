package vorquel.mod.similsaxtranstructors;

import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends Proxy {

    @Override
    public void registerBlockOverlay() {
        MinecraftForge.EVENT_BUS.register(new BlockOverlay());
    }
}
