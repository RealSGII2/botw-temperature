package dev.realsgii2.botwtemperature.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.realsgii2.botwtemperature.util.Color;
import dev.realsgii2.botwtemperature.util.Interpolator;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = MODID, bus = Bus.FORGE, value = { Dist.CLIENT })
public class SoundGaugeRenderer {
    private static final Integer SIZE = 20;
    private static final Integer RADIUS = SIZE / 2;

    private static final Color LINE_COLOR = Color.of(224, 116, 206);

    private static double currentVolume = 1;

    private static ResourceLocation gaugeImage = new ResourceLocation(MODID, "textures/gauge/sound_gauge.png");
    private static ResourceLocation gaugeImageWhite = new ResourceLocation(MODID, "textures/gauge/sound_gauge_t.png");

    private static List<String> whitelistedSounds = new ArrayList<String>() {
        {
            add(".close");
            add(".open");
            add(".burp");

            add(".player.hurt");

            add(".fall");
            add(".hit");
            add(".place");

            add(".click");
            add(".click_off");
            add(".click_on");
        }
    };

    private static BlockPos lastPlayerPosition;

    private static Double[] points = new Double[] {
            0.0
    };

    private static Double[] currentPoints = new Double[] {
            0.0
    };

    private static final Random random = new Random();

    @SuppressWarnings({ "resource" })
    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (!event.getPos().closerThan(player.blockPosition(), 5.0)) return;
        
        currentVolume = 1.0;
    }

    @SuppressWarnings({ "resource" })
    @SubscribeEvent
    public static void harvest(HarvestCheck event)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (!event.getEntity().is(player)) return;
        
        currentVolume = 1.0;
    }

    @SuppressWarnings({ "resource" })
    @SubscribeEvent
    public static void interact(HarvestCheck event)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (!event.getEntity().is(player)) return;
        
        currentVolume = 1.0;
    }

    @SubscribeEvent
    @SuppressWarnings({ "resource" })
    public static void soundEvent(PlaySoundAtEntityEvent event) {
        if (event.getEntity() == null) return;

        boolean considerSound = false;

        String soundPath = event.getSound().getRegistryName().getPath();
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (!event.getEntity().blockPosition().closerThan(player.blockPosition(), 5.0)) return;

        for (String sound : whitelistedSounds) {
            if (considerSound)
                break;
            if (soundPath.indexOf(sound) != -1)
                considerSound = true;
        }

        if (considerSound && event.getVolume() > currentVolume) 
            currentVolume = 1.0;
        
    }

    @SuppressWarnings({ "resource", "deprecation" })
    public static void afterGameOverlayRender(RenderGameOverlayEvent.Post event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (lastPlayerPosition == null || Math.sqrt(player.blockPosition().distSqr(lastPlayerPosition)) > 1 && player.isOnGround()) {
            lastPlayerPosition = player.blockPosition();

            if (!player.isCrouching() || player.isSwimming())
                currentVolume = 0.5;
            else if (player.isCrouching() || player.isInWater())
                currentVolume = 0.25;
        }

        if (player.isSprinting())
            currentVolume = 1.0;

        if (currentVolume > 0.1)
            currentVolume -= 0.01;
        else
            currentVolume = 0.2;

        BufferBuilder buffer = Tessellator.getInstance().getBuilder();

        final MatrixStack stack = event.getMatrixStack();
        final Minecraft minecraft = Minecraft.getInstance();
        final TextureManager textureManager = minecraft.getTextureManager();
        final Integer windowWidth = minecraft.getWindow().getGuiScaledWidth();
        final Integer windowHeight = minecraft.getWindow().getGuiScaledHeight();

        final Integer offsetLeft = windowWidth - SIZE - 100;
        final Integer offsetTop = windowHeight - SIZE - 40;

        final double top = offsetTop + RADIUS;
        RenderSystem.disableDepthTest();

        RenderSystem.lineWidth(1f);

        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.defaultBlendFunc();

        textureManager.bind(gaugeImage);
        blit(stack, offsetLeft, offsetTop, SIZE);

        textureManager.bind(gaugeImageWhite);
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR_TEX);

        if (player.tickCount % 3 == 0) {
            Double[] prePoints = new Double[] {
                    random.nextDouble() * currentVolume, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, random.nextDouble() * currentVolume,
            };
            final int iterate = currentVolume > 0.3 ? 8 : 25;

            for (int i = 0; i < prePoints.length; i += iterate) {
                if (i >= prePoints.length)
                    break;

                prePoints[i] = random.nextDouble() * currentVolume;
            }

            if (currentVolume > 0.35)
                points = Interpolator.interpolate(prePoints);
            else
                points = Interpolator.interpolate3(prePoints);

            if (currentVolume > 0.3)
                for (int j = 0; j < points.length; j++) {
                    if (j % 2 == 0)
                        points[j] = -points[j];
                }
            else
                for (int j = 0; j < points.length; j++)
                    if (random.nextBoolean())
                        points[j] = -points[j];
        }

        if (currentPoints.length == 1)
            currentPoints = points.clone();

        for (int i = 0; i < currentPoints.length; i++) {
            currentPoints[i] += (points[i] - currentPoints[i]) * 0.2;

            double x = (double) i / (double) (currentPoints.length - 1) * (double) SIZE;
            double y = currentPoints[i] * (double) RADIUS;

            if (x < 0 || x > SIZE)
                continue;

            float u = Math.max(Math.min((float) x / (float) SIZE, 1), 0);
            float v = (float) currentPoints[i].doubleValue() / 2.0f + 0.5f;

            buffer.vertex(offsetLeft + x, top + y, 0)
                    .color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha)
                    .uv(u, v)
                    .endVertex();
        }

        buffer.end();
        WorldVertexBufferUploader.end(buffer);

        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();

        // Minecraft.getInstance().font.drawShadow(stack, Double.toString(currentVolume), 5, 5, 16777215);
    }

    private static void blit(MatrixStack stack, Integer left, Integer top,
            Integer size) {
        AbstractGui.blit(stack, left, top, 0, 0, size, size, size, size);
    }

    public static double blend(double blendFrom, double blendTo, double factor, double rangeMin, double rangeMax) {
        if (factor <= rangeMin)
            return blendFrom;
        if (factor >= rangeMax)
            return blendTo;
        return ((1 / (rangeMax - rangeMin)) * (factor - rangeMin)) * (blendTo - blendFrom) + blendFrom;
    }
}

// buffer.vertex(left - RADIUS, top - 1, 25).color(1f, 1f, 1f, 1f).uv(0.5f,
// 0.5f).endVertex();
// buffer.vertex(left + RADIUS, top - 1, 25).color(1f, 1f, 1f, 1f).uv(0.5f,
// 0.5f).endVertex();
// buffer.vertex(left - RADIUS, top + 1, 25).color(1f, 1f, 1f, 1f).uv(0.5f,
// 0.5f).endVertex();
// buffer.vertex(left + RADIUS, top + 1, 25).color(1f, 1f, 1f, 1f).uv(0.5f,
// 0.5f).endVertex();

// final double[] pointMultipiliers = new double[] {
// 1.7, -0.8, 0.73, -0.65, 0.81, -0.69, 0.83, -0.75, 0.75, -0.75, 0.65, -0.78,
// 0.84, -0.86, 0.85, -0.8,
// 0.81, -0.75, 0.78, -0.75
// };

// buffer.vertex(left - 2000, top,
// 0)
// .color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha)
// .uv(0.5f, 0.5f)
// // .uv2(SIZE, SIZE)
// .endVertex();

// for (int i = 0; i < pointMultipiliers.length; i++) {
// // textureManager.bind(new ResourceLocation(MODID,
// "textures/gauge/first.png"));

// buffer.vertex(left - RADIUS + i * (SIZE / (pointMultipiliers.length)),
// top + (pointMultipiliers[i] * RADIUS),
// 0)
// .color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha)
// // .uv((float) (i / pointMultipiliers.length), 0.5f)
// // .uv((float) (i / (pointMultipiliers.length)), (float) (0.5f +
// // (pointMultipiliers[i] / 2)))
// // .uv(0.5f, (float) (0.5f - (pointMultipiliers[i] / 2)))
// .uv(0.5f, 0.5f)
// // .uv2(SIZE, SIZE)
// // .uv((float) (i / (pointMultipiliers.length - 1)), 0.55f)
// .endVertex();
// }