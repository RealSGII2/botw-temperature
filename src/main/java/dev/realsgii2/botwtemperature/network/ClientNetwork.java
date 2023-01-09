package dev.realsgii2.botwtemperature.network;

import java.util.function.Supplier;

import dev.realsgii2.botwtemperature.capabilities.TemperatureCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import static dev.realsgii2.botwtemperature.BotWTemperature.LOGGER;

public final class ClientNetwork {
    public static void handleIncomingSyncTemperatureCapabilityNetworkMessageClient(
            SyncTemperatureCapabilityNetworkMessage message, Supplier<NetworkEvent.Context> context) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;

        TemperatureCapability temperatureCapability = TemperatureCapability.of(player);
        if (temperatureCapability == null) {
            LOGGER.error("ClientNetwork#handleIncomingSyncTemperatureCapabilityNetworkMessageClient failed to get TemperatureCapability");
        } else {
            LOGGER.info("TemperatureCapability Updated");
            message.copyTo(temperatureCapability);
        }
    }
}
