package dev.realsgii2.botwtemperature;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

public class CreativeTab extends ItemGroup {
    public CreativeTab() {
        super(MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.WATER_BUCKET);
    }

    public static final CreativeTab INSTANCE = new CreativeTab();
}
