package dev.realsgii2.botwtemperature.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

public class ColdProofEnchantment extends Enchantment {
    public final static String RESGISTRY_NAME = "coldproof";
    public final static Rarity RARITY = Rarity.RARE;
    public final static EnchantmentType TYPE = EnchantmentType.WEARABLE;
    public final static EquipmentSlotType[] SLOTS = new EquipmentSlotType[] { EquipmentSlotType.HEAD,
            EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET };

    public ColdProofEnchantment() {
        super(RARITY, TYPE, SLOTS);
        setRegistryName(MODID, RESGISTRY_NAME);
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
