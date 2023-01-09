package dev.realsgii2.botwtemperature.item.armor.DesertVoeSet;

import dev.realsgii2.botwtemperature.CreativeTab;
import dev.realsgii2.botwtemperature.item.armor.ScaledArmorItem;
import net.minecraft.inventory.EquipmentSlotType;

public class DesertVoeLeggings extends ScaledArmorItem {
    private static final Properties PROPERTIES = new Properties().stacksTo(1).tab(CreativeTab.INSTANCE);
    private static final float SCALE = 0.1f;

    public DesertVoeLeggings() {
        super(SCALE, DesertVoeMaterial.INSTANCE, EquipmentSlotType.LEGS, PROPERTIES);
    }
}
