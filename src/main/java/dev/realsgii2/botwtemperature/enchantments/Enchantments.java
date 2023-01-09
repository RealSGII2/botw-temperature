package dev.realsgii2.botwtemperature.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Bus.MOD)
public final class Enchantments {
    public final static FlamebreakerEnchantment FLAMEBREAKER_ENCHANTMENT = new FlamebreakerEnchantment();
    public final static HeatProofEnchantment HEAT_PROOF_ENCHANTMENT = new HeatProofEnchantment();
    public final static ColdProofEnchantment COLD_PROOF_ENCHANTMENT = new ColdProofEnchantment();
    public final static IcebreakerEnchantment ICEBREAKER_ENCHANTMENT = new IcebreakerEnchantment();

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        final IForgeRegistry<Enchantment> registry = event.getRegistry();

        registry.register(FLAMEBREAKER_ENCHANTMENT);
        registry.register(HEAT_PROOF_ENCHANTMENT);
        registry.register(COLD_PROOF_ENCHANTMENT);
        registry.register(ICEBREAKER_ENCHANTMENT);
    }
}
