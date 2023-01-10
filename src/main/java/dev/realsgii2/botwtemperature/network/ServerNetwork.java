package dev.realsgii2.botwtemperature.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

import java.util.Optional;
import java.util.function.Supplier;

public final class ServerNetwork {
    private ServerNetwork() {
    }

    public static final String NETWORK_VERSION = "1.0";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> NETWORK_VERSION,
            NETWORK_VERSION::equals,
            NETWORK_VERSION::equals);

    public static void init() {
        int id = -1;

        NETWORK.registerMessage(
                id++,
                SyncTemperatureCapabilityNetworkMessage.class,
                SyncTemperatureCapabilityNetworkMessage::write,
                SyncTemperatureCapabilityNetworkMessage::read,
                ServerNetwork::handleIncomingSyncTemperatureCapabilityNetworkMessageServer,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static void handleIncomingSyncTemperatureCapabilityNetworkMessageServer(
            SyncTemperatureCapabilityNetworkMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientNetwork
                .handleIncomingSyncTemperatureCapabilityNetworkMessageClient(message, context)));
        context.get().setPacketHandled(true);
    }
}
