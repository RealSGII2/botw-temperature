package dev.realsgii2.botwtemperature.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.realsgii2.botwtemperature.capabilities.TemperatureCapability;

import static dev.realsgii2.botwtemperature.BotWTemperature.LOGGER;

@Mod.EventBusSubscriber(modid = MODID, bus = Bus.FORGE, value = { Dist.CLIENT })
public class TemperatureGaugeRenderer {
    private static final Integer SIZE = 20;
    private static final double MAX_RING_SIZE = 1.875;
    private static final double MAX_VISIBLE_RING_SIZE = 1.5;
    private static final double RING_SIZE_INTERVAL = 0.0175;

    private static double currentRotation = 0;
    private static double currentRingSize = 1;

    private static double currentFlameIndex = 0;

    private TemperatureGaugeRenderer() {
    }

    @SubscribeEvent
    public static void afterGameOverlayRender(RenderGameOverlayEvent.Post event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || player.isDeadOrDying())
            return;

        TemperatureCapability temperature = TemperatureCapability.of(player);
        double currentTemperature = temperature.getCurrentTemperature();

        ResourceLocation gaugeImage = GaugeImage
                .get(Pair.of(temperature.getColdProtectionLevel(), temperature.getHeatProtectionLevel()));

        // LOGGER.info(temperature.getColdProtectionLevel());
        // LOGGER.info(temperature.getHeatProtectionLevel());

        if (temperature.shouldShowFireIndicator())
            gaugeImage = VolcanicGaugeImage;
        else if (currentTemperature == -3)
            gaugeImage = IcyGaugeImage;

        final MatrixStack stack = event.getMatrixStack();
        // final ResourceLocation currentTexture = GaugeImage.COLD0_HOT0.texture;
        final Minecraft minecraft = Minecraft.getInstance();
        final TextureManager textureManager = minecraft.getTextureManager();
        final Integer windowWidth = minecraft.getWindow().getGuiScaledWidth();
        final Integer windowHeight = minecraft.getWindow().getGuiScaledHeight();

        final Integer left = windowWidth - SIZE - 100;
        final Integer top = windowHeight - SIZE - 15;

        textureManager.bind(gaugeImage);
        blit(stack, left, top, SIZE);

        if ((temperature.shouldDamagePlayerCold() || temperature.shouldDamagePlayerHeat()) && currentTemperature != 3
                && currentTemperature != -3) {
            RingImage ringImage = temperature.shouldDamagePlayerCold()
                    ? RingImage.COLD
                    : RingImage.HEAT;
            WarningImage warningImage = temperature.shouldDamagePlayerCold()
                    ? WarningImage.COLD
                    : WarningImage.HEAT;

            // We use glTranslated to offset using a double, instead of int (which blit
            // requires)
            GL11.glPushMatrix();
            GL11.glTranslated(-1.75, -1.75, 0);
            textureManager.bind(warningImage.texture);
            blit(stack, left, top, 24);
            GL11.glPopMatrix();

            GL11.glPushMatrix();

            currentRingSize += RING_SIZE_INTERVAL;
            double opacity = Math.min(1 - (currentRingSize - 1) / (MAX_VISIBLE_RING_SIZE - 1), 1);

            if (currentRingSize >= MAX_RING_SIZE)
                currentRingSize = 1;

            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1f, 1f, 1f, (float) opacity);

            GL11.glTranslatef(left + 24 / 2, top + 24 / 2, 0);
            GL11.glScaled(currentRingSize, currentRingSize, 1);
            GL11.glTranslatef(-(left + 24 / 2), -(top + 24 / 2), 0);
            GL11.glTranslated(-1.25, -1.25, 0);

            textureManager.bind(ringImage.texture);
            blit(stack, left, top, 24);

            RenderSystem.color4f(1f, 1f, 1f, 1f);
            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();
            GL11.glPopMatrix();
        }

        if (temperature.shouldShowFireIndicator()) {
            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            currentFlameIndex += 0.4;
            if (currentFlameIndex >= flames.size())
                currentFlameIndex = 0;

            textureManager.bind(flames.get((int) Math.floor(currentFlameIndex)));
            // blit(stack, left, top, 46);
            blit(stack, left - 7, top - 10, 34);

            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();
        }

        if (temperature.shouldFreezePlayer()) {
            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            textureManager.bind(ice);
            blit(stack, left - 4, top - 4, 26);

            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();
        }

        double goalRotation = currentTemperature * 60f;
        double remainingRotation = goalRotation - currentRotation;

        if (Math.abs(remainingRotation) < 1)
            currentRotation = goalRotation;
        else
            currentRotation += remainingRotation * .025;

        if (temperature.shouldShowFireIndicator())
            currentRotation = 130f;

        if (currentTemperature == -3)
            currentRotation = -130f;

        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RotatorImage rotatorImage = RotatorImage.NORMAL;

        if (temperature.shouldShowFireIndicator())
            rotatorImage = RotatorImage.VOLCANIC;
        else if (currentTemperature == -3)
            rotatorImage = RotatorImage.ICY;

        textureManager.bind(rotatorImage.texture);
        blit(stack, left, top, SIZE, (float) currentRotation);

        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
    }

    private static void blit(MatrixStack stack, Integer left, Integer top, Integer size) {
        AbstractGui.blit(stack, left, top, 0, 0, size, size, size, size);
    }

    private static void blit(MatrixStack stack, Integer left, Integer top, Integer size, float rotation) {
        GL11.glPushMatrix();

        // Apply rotation at icon's center
        GL11.glTranslatef(left + size / 2, top + size / 2, 0);
        GL11.glRotatef(rotation, 0, 0, 1);
        GL11.glTranslatef(-(left + size / 2), -(top + size / 2), 0);

        blit(stack, left, top, size);
        GL11.glPopMatrix();
    }

    public static ResourceLocation VolcanicGaugeImage = new ResourceLocation(MODID, "textures/gauge/volcanic.png");
    public static ResourceLocation IcyGaugeImage = new ResourceLocation(MODID, "textures/gauge/icy.png");

    public static Map<Pair<Integer, Integer>, ResourceLocation> GaugeImage = new HashMap<Pair<Integer, Integer>, ResourceLocation>() {
        {
            put(Pair.of(0, 0), new ResourceLocation(MODID, "textures/gauge/cold0_hot0.png"));
            put(Pair.of(0, 1), new ResourceLocation(MODID, "textures/gauge/cold0_hot1.png"));
            put(Pair.of(0, 2), new ResourceLocation(MODID, "textures/gauge/cold0_hot2.png"));
            put(Pair.of(1, 0), new ResourceLocation(MODID, "textures/gauge/cold1_hot0.png"));
            put(Pair.of(1, 1), new ResourceLocation(MODID, "textures/gauge/cold1_hot1.png"));
            put(Pair.of(1, 2), new ResourceLocation(MODID, "textures/gauge/cold1_hot2.png"));
            put(Pair.of(2, 0), new ResourceLocation(MODID, "textures/gauge/cold2_hot0.png"));
            put(Pair.of(2, 1), new ResourceLocation(MODID, "textures/gauge/cold2_hot1.png"));
            put(Pair.of(2, 2), new ResourceLocation(MODID, "textures/gauge/cold2_hot2.png"));
        }
    };

    public static ResourceLocation ice = new ResourceLocation(MODID, "textures/gauge/ice.png");

    public static ArrayList<ResourceLocation> flames = new ArrayList<ResourceLocation>() {
        {
            add(new ResourceLocation(MODID, "textures/gauge/flame/1.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/2.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/3.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/4.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/5.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/6.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/7.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/8.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/9.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/10.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/11.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/12.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/13.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/14.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/15.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/16.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/17.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/18.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/19.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/20.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/21.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/22.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/23.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/24.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/25.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/26.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/27.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/28.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/29.png"));
            add(new ResourceLocation(MODID, "textures/gauge/flame/30.png"));
        }
    };

    public enum RingImage {
        COLD(new ResourceLocation(MODID, "textures/gauge/cold_ring.png")),
        HEAT(new ResourceLocation(MODID, "textures/gauge/heat_ring.png"));

        public final ResourceLocation texture;

        RingImage(ResourceLocation texture) {
            this.texture = Objects.requireNonNull(texture);
        }
    }

    public enum WarningImage {
        COLD(new ResourceLocation(MODID, "textures/gauge/cold_warning.png")),
        HEAT(new ResourceLocation(MODID, "textures/gauge/heat_warning.png"));

        public final ResourceLocation texture;

        WarningImage(ResourceLocation texture) {
            this.texture = Objects.requireNonNull(texture);
        }
    }

    public enum RotatorImage {
        ICY(new ResourceLocation(MODID, "textures/gauge/rotator_icy.png")),
        VOLCANIC(new ResourceLocation(MODID, "textures/gauge/rotator_volcanic.png")),
        NORMAL(new ResourceLocation(MODID, "textures/gauge/rotator_normal.png"));

        public final ResourceLocation texture;

        RotatorImage(ResourceLocation texture) {
            this.texture = Objects.requireNonNull(texture);
        }
    }
}
