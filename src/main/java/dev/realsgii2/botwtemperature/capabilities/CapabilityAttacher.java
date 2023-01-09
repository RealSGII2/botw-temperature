package dev.realsgii2.botwtemperature.capabilities;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;
import static dev.realsgii2.botwtemperature.BotWTemperature.LOGGER;

@Mod.EventBusSubscriber(modid = MODID)
public class CapabilityAttacher {
    @SubscribeEvent
    public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            TemperatureCapability temperatureCapability = player instanceof ServerPlayerEntity
                    ? new ServerTemperatureCapability(player)
                    : DistExecutor.unsafeRunForDist(
                            () -> () -> ClientFunctions.createClientTemperatureCapability(player),
                            () -> () -> new RemoteTemperatureCapability(player));

            LOGGER.info("Capability added");

            event.addCapability(new ResourceLocation(MODID, "temperature_capability"), temperatureCapability);
        }
    }

    private static class ClientFunctions {
        public static TemperatureCapability createClientTemperatureCapability(PlayerEntity player) {
            return player instanceof ClientPlayerEntity
                    ? new ClientTemperatureCapability(player)
                    : new RemoteTemperatureCapability(player);
        }
    }
}
