package dev.realsgii2.botwtemperature.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Bus.FORGE, value = { Dist.CLIENT })
public class GuiRenderer {
    @SubscribeEvent
    @SuppressWarnings("resource")
    public static void afterGameOverlayRender(RenderGameOverlayEvent.Post event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || player.isDeadOrDying())
            return;
            
        SoundGaugeRenderer.afterGameOverlayRender(event);
        TemperatureGaugeRenderer.afterGameOverlayRender(event);
    }
}
