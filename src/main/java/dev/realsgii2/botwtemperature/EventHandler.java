package dev.realsgii2.botwtemperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

import dev.realsgii2.botwtemperature.common.DamageSources;
import dev.realsgii2.botwtemperature.common.Temperature;

@Mod.EventBusSubscriber(modid = MODID)
public class EventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = event.player;
            if (event.side.isServer()) {
                Temperature temperature = new Temperature(player);

                if (!player.isCreative() && !player.isSpectator()) {
                    if (player.tickCount % 40 == 0) {
                        if (temperature.shouldDamagePlayerCold())
                            player.hurt(DamageSources.COLD, 2f);

                        if (temperature.shouldDamagePlayerHeat())
                            player.hurt(DamageSources.HEAT, 2f);

                        if (temperature.shouldSetPlayerOnFire())
                            player.setRemainingFireTicks(80);
                    }

                    if (player.tickCount % 5 == 0) {
                        if (temperature.shouldFreezePlayer())
                            player.hurt(DamageSources.COLD, 2f);
                    }
                }
            }
        }
    }
}
