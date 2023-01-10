package dev.realsgii2.botwtemperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

import dev.realsgii2.botwtemperature.capabilities.Capabilities;
import dev.realsgii2.botwtemperature.util.DamageSources;

@Mod.EventBusSubscriber(modid = MODID)
public class EventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = event.player;
            player.getCapability(Capabilities.temperatureCapability).ifPresent(capability -> {
                if (event.side.isServer()) {

                    if (!player.isCreative() && !player.isSpectator()) {
                        if (player.tickCount % 40 == 0) {
                            if (capability.shouldDamagePlayerCold())
                                player.hurt(DamageSources.COLD, 2f);
                                
                            if (capability.shouldDamagePlayerHeat())
                                player.hurt(DamageSources.HEAT, 2f);
                                
                            if (capability.shouldSetPlayerOnFire())
                                player.setRemainingFireTicks(80);
                        }

                        if (player.tickCount % 5 == 0) {
                            if (capability.shouldFreezePlayer())
                                player.hurt(DamageSources.COLD, 2f);
                        }
                    }
                }
            });
        }
    }
}
