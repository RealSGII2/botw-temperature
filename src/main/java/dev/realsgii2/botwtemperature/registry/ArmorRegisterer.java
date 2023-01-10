package dev.realsgii2.botwtemperature.registry;

import dev.realsgii2.botwtemperature.registry.armormaterial.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;

public class ArmorRegisterer {
    // Desert Voe Set
    public static final ScaledArmorItem DESERT_VOE_HEADBAND = ScaledArmorItem.helmet("desert_voe_headband",
            0.2f, DesertVoeMaterial.INSTANCE);

    public static final ScaledArmorItem DESERT_VOE_SPAULDER = ScaledArmorItem.chestplate("desert_voe_spaulder",
            0.2f, DesertVoeMaterial.INSTANCE);

    public static final ScaledArmorItem DESERT_VOE_TROUSERS = ScaledArmorItem.leggings("desert_voe_trousers",
            0.1f, DesertVoeMaterial.INSTANCE);

    public static final ScaledArmorItem DESERT_VOE_BOOTS = ScaledArmorItem.boots("desert_voe_boots",
            0.2f, DesertVoeMaterial.INSTANCE);

    // Snowquill Set
    public static final ScaledArmorItem SNOWQUILL_HEADRESS = ScaledArmorItem.helmet("snowquill_headress",
            0.2f, DesertVoeMaterial.INSTANCE);

    public static final ScaledArmorItem SNOWQUILL_TUNIC = ScaledArmorItem.chestplate("snowquill_tunic",
            0.2f, DesertVoeMaterial.INSTANCE);

    public static final ScaledArmorItem SNOWQUILL_TROUSERS = ScaledArmorItem.leggings("snowquill_trousers",
            0.1f, DesertVoeMaterial.INSTANCE);

    public static final ScaledArmorItem SNOWQUILL_BOOTS = ScaledArmorItem.boots("snowquill_boots",
            0.2f, DesertVoeMaterial.INSTANCE);

    private static final ScaledArmorItem[] TO_REGISTER = new ScaledArmorItem[] {
            DESERT_VOE_HEADBAND,
            DESERT_VOE_SPAULDER,
            DESERT_VOE_TROUSERS,
            DESERT_VOE_BOOTS,

            SNOWQUILL_HEADRESS,
            SNOWQUILL_TUNIC,
            SNOWQUILL_TROUSERS,
            SNOWQUILL_BOOTS
    };

    public static void register(DeferredRegister<Item> registerer) {
        for (ScaledArmorItem item : TO_REGISTER)
            registerer.register(item.name, () -> item);
    }

    public static class ScaledArmorItem extends ArmorItem {
        private static final Properties DEFAULT_PROPERTIES = new Properties().stacksTo(1);

        private final BipedModel<LivingEntity> MODEL;

        public final String name;

        public ScaledArmorItem(String name, Float scale, IArmorMaterial material, EquipmentSlotType slotType,
                Properties properties) {
            super(material, slotType, properties);
            MODEL = new BipedModel<LivingEntity>(0.5f);
            this.name = name;
        }

        public static ScaledArmorItem helmet(String name, Float scale, IArmorMaterial material) {
            return new ScaledArmorItem(name, scale, material, EquipmentSlotType.HEAD, DEFAULT_PROPERTIES);
        }

        public static ScaledArmorItem chestplate(String name, Float scale, IArmorMaterial material) {
            return new ScaledArmorItem(name, scale, material, EquipmentSlotType.CHEST, DEFAULT_PROPERTIES);
        }

        public static ScaledArmorItem leggings(String name, Float scale, IArmorMaterial material) {
            return new ScaledArmorItem(name, scale, material, EquipmentSlotType.LEGS, DEFAULT_PROPERTIES);
        }

        public static ScaledArmorItem boots(String name, Float scale, IArmorMaterial material) {
            return new ScaledArmorItem(name, scale, material, EquipmentSlotType.FEET, DEFAULT_PROPERTIES);
        }

        @SuppressWarnings("unchecked")
        public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
                EquipmentSlotType armorSlot, A _default) {
            return (A) MODEL;
        }
    }

}
