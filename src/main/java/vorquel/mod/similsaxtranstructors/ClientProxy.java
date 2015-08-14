package vorquel.mod.similsaxtranstructors;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends Proxy {

    @Override
    public void registerBlockOverlay() {
        MinecraftForge.EVENT_BUS.register(new BlockOverlay());
    }
}
