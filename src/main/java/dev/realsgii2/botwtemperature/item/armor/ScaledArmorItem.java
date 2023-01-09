package dev.realsgii2.botwtemperature.item.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

public class ScaledArmorItem extends ArmorItem {
    private final BipedModel<LivingEntity> MODEL;

    public ScaledArmorItem(Float scale, IArmorMaterial material, EquipmentSlotType slotType, Properties properties)
    {
        super(material, slotType, properties);
        MODEL = new BipedModel<LivingEntity>(0.5f);
    }

    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
            EquipmentSlotType armorSlot, A _default) {
        return (A)MODEL;
    }
}
