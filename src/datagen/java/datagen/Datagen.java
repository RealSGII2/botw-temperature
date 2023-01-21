package datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Bus.MOD)
public class Datagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer())
        {
            generator.addProvider(new LootModifierProvider(generator));
        }
    }
}
