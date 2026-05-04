package net.satisfy.herbalbrews.client.gui;

import net.satisfy.herbalbrews.client.gui.handler.TeaKettleGuiHandler;
import net.satisfy.herbalbrews.core.registry.ObjectRegistry;
import net.satisfy.herbalbrews.core.util.HerbalBrewsIdentifier;
import org.joml.Vector2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TeaKettleGui extends AbstractContainerScreen<TeaKettleGuiHandler> {
    private static final ResourceLocation BACKGROUND = HerbalBrewsIdentifier.identifier("textures/gui/tea_kettle.png");
    private static final int ARROW_X = 54;
    private static final int ARROW_Y = 22;
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 17;
    private static final int WATER_X = 141;
    private static final int WATER_Y = 16;
    private static final int WATER_WIDTH = 8;
    private static final int WATER_HEIGHT = 43;
    private static final int WATER_TEXTURE_X = 183;
    private static final int WATER_TEXTURE_Y = 31;
    private static final int HEAT_X = 156;
    private static final int HEAT_Y = 16;
    private static final int HEAT_WIDTH = 5;
    private static final int HEAT_HEIGHT = 43;
    private static final int HEAT_TEXTURE_X = 176;
    private static final int HEAT_TEXTURE_Y = 31;
    private static final int HEATING_SLOT_X = 151;
    private static final int HEATING_SLOT_Y = 62;
    private static final int HEATING_SLOT_WIDTH = 15;
    private static final int HEATING_SLOT_HEIGHT = 15;
    private final Vector2i screenPos = new Vector2i();

    @SuppressWarnings("unused")
    public TeaKettleGui(TeaKettleGuiHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, ObjectRegistry.TEA_KETTLE.get().getName());
    }

    @Override
    protected void init() {
        super.init();
        screenPos.set(leftPos, topPos);
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(BACKGROUND, screenPos.x(), screenPos.y(), 0, 0, imageWidth, imageHeight);
        int progress = this.menu.getScaledProgress(24);
        guiGraphics.blit(BACKGROUND, screenPos.x() + ARROW_X, screenPos.y() + ARROW_Y, 176, 14, progress, ARROW_HEIGHT);
        if (menu.isBeingBurned()) {
            guiGraphics.blit(BACKGROUND, screenPos.x() + 152, screenPos.y() + 63, 176, 0, 14, 14);
        }
        int waterLevel = this.menu.getWaterLevel();
        int filledHeight = (waterLevel * WATER_HEIGHT) / 100;
        guiGraphics.blit(BACKGROUND, screenPos.x() + WATER_X, screenPos.y() + WATER_Y + WATER_HEIGHT - filledHeight, WATER_TEXTURE_X, WATER_TEXTURE_Y + WATER_HEIGHT - filledHeight, WATER_WIDTH, filledHeight);
        int heatLevel = this.menu.getHeatLevel();
        int filledHeatHeight = (heatLevel * HEAT_HEIGHT) / 100;
        guiGraphics.blit(BACKGROUND, screenPos.x() + HEAT_X, screenPos.y() + HEAT_Y + HEAT_HEIGHT - filledHeatHeight, HEAT_TEXTURE_X, HEAT_TEXTURE_Y + HEAT_HEIGHT - filledHeatHeight, HEAT_WIDTH, filledHeatHeight);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        if (isMouseOverWaterArea(mouseX, mouseY)) {
            int waterLevel = this.menu.getWaterLevel();
            Component tooltip = Component.translatable("tooltip.herbalbrews.tea_kettle.water_level", waterLevel);
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
        if (isMouseOverWaterSlot(mouseX, mouseY)) {
            Component tooltip = Component.translatable("tooltip.herbalbrews.tea_kettle.water_slot");
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
        if (isMouseOverHeatArea(mouseX, mouseY)) {
            int heatLevel = this.menu.getHeatLevel();
            Component tooltip = Component.translatable("tooltip.herbalbrews.tea_kettle.heat_level", heatLevel);
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
        if (isMouseOverProgressArrow(mouseX, mouseY)) {
            int remainingTicks = this.menu.getRequiredDuration() - this.menu.getCookingTime();
            String formattedTime = formatTicks(remainingTicks);
            Component tooltip = Component.translatable("tooltip.herbalbrews.tea_kettle.remaining_time", formattedTime);
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
        if (isMouseOverHeatingSlot(mouseX, mouseY)) {
            int heatIncrease = 35;
            Component tooltip = Component.translatable("tooltip.herbalbrews.tea_kettle.heat_increase", heatIncrease);
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private boolean isMouseOverWaterArea(int mouseX, int mouseY) {
        int left = screenPos.x() + WATER_X;
        int top = screenPos.y() + WATER_Y;
        return mouseX >= left && mouseX < left + WATER_WIDTH && mouseY >= top && mouseY < top + WATER_HEIGHT;
    }

    private boolean isMouseOverHeatArea(int mouseX, int mouseY) {
        int left = screenPos.x() + HEAT_X;
        int top = screenPos.y() + HEAT_Y;
        return mouseX >= left && mouseX < left + HEAT_WIDTH && mouseY >= top && mouseY < top + HEAT_HEIGHT;
    }

    private boolean isMouseOverProgressArrow(int mouseX, int mouseY) {
        int left = screenPos.x() + ARROW_X;
        int top = screenPos.y() + ARROW_Y;
        return mouseX >= left && mouseX < left + ARROW_WIDTH && mouseY >= top && mouseY < top + ARROW_HEIGHT;
    }

    private boolean isMouseOverHeatingSlot(int mouseX, int mouseY) {
        int left = screenPos.x() + HEATING_SLOT_X;
        int top = screenPos.y() + HEATING_SLOT_Y;
        return mouseX >= left && mouseX < left + HEATING_SLOT_WIDTH && mouseY >= top && mouseY < top + HEATING_SLOT_HEIGHT;
    }

    private boolean isMouseOverWaterSlot(int mouseX, int mouseY) {
        int left = screenPos.x() + 118;
        int top = screenPos.y() + 25;
        return mouseX >= left && mouseX < left + 18 && mouseY >= top && mouseY < top + 18;
    }

    private String formatTicks(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
