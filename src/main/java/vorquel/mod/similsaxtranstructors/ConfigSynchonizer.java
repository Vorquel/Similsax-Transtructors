package vorquel.mod.similsaxtranstructors;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

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
            SimilsaxTranstructors.proxy.addScheduledTask(
                    new Task(message.basicUses, message.advancedUses));
            return null;
        }
    }

    public static class Task implements Runnable {

        private int bu, au;

        public Task(int bu, int au) {
            this.bu = bu;
            this.au = au;
        }

        @Override
        public void run() {
            SimilsaxTranstructors.log.info("Syncing Client configs");
            SimilsaxTranstructors.itemBasic.setUses(bu);
            SimilsaxTranstructors.itemAdvanced.setUses(au);
        }
    }
}
