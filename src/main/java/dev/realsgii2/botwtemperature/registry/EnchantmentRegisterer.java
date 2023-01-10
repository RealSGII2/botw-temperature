package dev.realsgii2.botwtemperature.registry;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;

public class EnchantmentRegisterer {
    public static final NamedEnchantment COLD_PROOF = NamedEnchantment.wearable("cold_proof", Rarity.RARE);

    public static final NamedEnchantment HEAT_PROOF = NamedEnchantment.wearable("heat_proof", Rarity.RARE);

    public static final NamedEnchantment ICEBREAKER = NamedEnchantment.wearable("icebvreaker", Rarity.RARE);

    public static final NamedEnchantment FLAMEBREAKER = NamedEnchantment.wearable("flamebreaker",
            Rarity.RARE);

    private static NamedEnchantment[] TO_REGISTER = new NamedEnchantment[] {
            COLD_PROOF,
            HEAT_PROOF,
            ICEBREAKER,
            FLAMEBREAKER
    };

    public static void register(DeferredRegister<Enchantment> registerer) {
        for (NamedEnchantment item : TO_REGISTER)
            registerer.register(item.name, () -> item);
    }

    private static class NamedEnchantment extends Enchantment {
        private static final EquipmentSlotType[] WEARABLE_SLOTS = new EquipmentSlotType[] { EquipmentSlotType.HEAD,
                EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET };

        public final String name;

        public NamedEnchantment(String name, Rarity rarity, EnchantmentType type, EquipmentSlotType[] slots) {
            super(rarity, type, slots);
            setRegistryName(MODID, name);
            this.name = name;
        }

        public static NamedEnchantment wearable(String name, Rarity rarity) {
            return new NamedEnchantment(name, rarity, EnchantmentType.WEARABLE, WEARABLE_SLOTS);
        }

        @Override
        public boolean canEnchant(ItemStack stack) {
            return isEnabled() && super.canEnchant(stack);
        }

        public boolean canApplyAtEnchantingTable(ItemStack stack) {
            return isEnabled() && super.canApplyAtEnchantingTable(stack);
        }

        @Override
        public boolean isAllowedOnBooks() {
            return isEnabled();
        }

        public boolean isEnabled() {
            return true;
        }
    }
}
