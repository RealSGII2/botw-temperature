package dev.realsgii2.botwtemperature.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

public final class Registerer {
        public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS,
                        MODID);
        public static final DeferredRegister<Effect> EFFECT_REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS,
                        MODID);
        public static final DeferredRegister<Potion> POTION_REGISTER = DeferredRegister.create(
                        ForgeRegistries.POTION_TYPES,
                        MODID);
        public static final DeferredRegister<Enchantment> ENCHANTMENT_REGISTER = DeferredRegister
                        .create(ForgeRegistries.ENCHANTMENTS, MODID);
        public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_REGISTER = DeferredRegister
                        .create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MODID);

        public static void register(IEventBus bus) {
                ArmorRegisterer.register(ITEM_REGISTER);
                EffectRegisterer.register(EFFECT_REGISTER);
                PotionRegisterer.register(POTION_REGISTER);
                EnchantmentRegisterer.register(ENCHANTMENT_REGISTER);

                ITEM_REGISTER.register(bus);
                EFFECT_REGISTER.register(bus);
                POTION_REGISTER.register(bus);
                ENCHANTMENT_REGISTER.register(bus);
                LootModifierRegisterer.REGISTER.register(bus);
        }
}
