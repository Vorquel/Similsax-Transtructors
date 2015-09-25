package vorquel.mod.similsaxtranstructors;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import static vorquel.mod.similsaxtranstructors.Config.advancedUses;
import static vorquel.mod.similsaxtranstructors.Config.basicUses;

public class ConfigSynchonizer {

    private static SimpleNetworkWrapper network;

    public static void init() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("SmlsxTrnstrctrs");
        network.registerMessage(Handler.class, Message.class, 0, Side.CLIENT);
    }

    @SubscribeEvent
    public void sendClientInfo(PlayerEvent.PlayerLoggedInEvent event) {
        network.sendTo(new Message(basicUses, advancedUses), (EntityPlayerMP) event.player);
    }

    public static class Message implements IMessage {

        public int basicUses;
        public int advancedUses;

        public Message() {}

        public Message(int basicUses, int advancedUses) {
            this.basicUses = basicUses;
            this.advancedUses = advancedUses;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            basicUses = buf.readInt();
            advancedUses = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(basicUses);
            buf.writeInt(advancedUses);
        }
    }

    public static class Handler implements IMessageHandler<Message, IMessage> {

        @Override
        public IMessage onMessage(Message message, MessageContext ctx) {
            SimilsaxTranstructors.itemBasic.setUses(message.basicUses);
            SimilsaxTranstructors.itemAdvanced.setUses(message.advancedUses);
            return null;
        }
    }
}
