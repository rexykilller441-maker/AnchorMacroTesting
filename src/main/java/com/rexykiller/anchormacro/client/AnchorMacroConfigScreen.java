package com.rexykiller.anchormacro.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import com.rexykiller.anchormacro.AnchorMacroConfig;

@Environment(EnvType.CLIENT)
public class AnchorMacroConfigScreen extends Screen {

    private final Screen parent;
    private TextFieldWidget delayAnchorField;
    private TextFieldWidget delayGlowstoneField;
    private TextFieldWidget delayTotemField;
    private TextFieldWidget delayExplodeField;

    private CheckboxWidget autoChargeToggle;
    private CheckboxWidget autoTotemToggle;

    private int anchorSlot = AnchorMacroConfig.anchorSlot;
    private int glowstoneSlot = AnchorMacroConfig.glowstoneSlot;
    private int totemSlot = AnchorMacroConfig.totemSlot;

    public AnchorMacroConfigScreen(Screen parent) {
        super(new LiteralText("Anchor Macro Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 6;

        // Delay fields
        this.delayAnchorField = new TextFieldWidget(this.textRenderer, centerX - 50, startY, 100, 20, new LiteralText("Anchor Delay"));
        this.delayAnchorField.setText(String.valueOf(AnchorMacroConfig.delayAnchor));
        this.addSelectableChild(this.delayAnchorField);

        this.delayGlowstoneField = new TextFieldWidget(this.textRenderer, centerX - 50, startY + 25, 100, 20, new LiteralText("Glowstone Delay"));
        this.delayGlowstoneField.setText(String.valueOf(AnchorMacroConfig.delayGlowstone));
        this.addSelectableChild(this.delayGlowstoneField);

        this.delayTotemField = new TextFieldWidget(this.textRenderer, centerX - 50, startY + 50, 100, 20, new LiteralText("Totem Delay"));
        this.delayTotemField.setText(String.valueOf(AnchorMacroConfig.delayTotem));
        this.addSelectableChild(this.delayTotemField);

        this.delayExplodeField = new TextFieldWidget(this.textRenderer, centerX - 50, startY + 75, 100, 20, new LiteralText("Explode Delay"));
        this.delayExplodeField.setText(String.valueOf(AnchorMacroConfig.delayExplode));
        this.addSelectableChild(this.delayExplodeField);

        // Toggles
        this.autoChargeToggle = new CheckboxWidget(centerX - 75, startY + 110, 150, 20, "Auto-charge", AnchorMacroConfig.autoCharge);
        this.addDrawableChild(this.autoChargeToggle);

        this.autoTotemToggle = new CheckboxWidget(centerX - 75, startY + 135, 150, 20, "Auto-totem swap", AnchorMacroConfig.autoTotem);
        this.addDrawableChild(this.autoTotemToggle);

        // Hotbar slot buttons
        addSlotButtons(centerX - 100, startY + 170, "Anchor Slot", 9, slot -> anchorSlot = slot, AnchorMacroConfig.anchorSlot);
        addSlotButtons(centerX - 100, startY + 200, "Glowstone Slot", 9, slot -> glowstoneSlot = slot, AnchorMacroConfig.glowstoneSlot);
        addSlotButtons(centerX - 100, startY + 230, "Totem Slot", 9, slot -> totemSlot = slot, AnchorMacroConfig.totemSlot);

        // Save button
        this.addDrawableChild(new ButtonWidget(centerX - 50, startY + 270, 100, 20, new LiteralText("Save & Close"), button -> {
            saveConfig();
            client.setScreen(parent);
        }));

        // Cancel button
        this.addDrawableChild(new ButtonWidget(centerX - 50, startY + 300, 100, 20, new LiteralText("Cancel"), button -> {
            client.setScreen(parent);
        }));
    }

    private void addSlotButtons(int x, int y, String label, int slots, SlotCallback callback, int currentSlot) {
        drawCenteredText(this.textRenderer, label, x + 90, y - 10, 0xFFFFFF);
        for (int i = 0; i < slots; i++) {
            int slotIndex = i;
            this.addDrawableChild(new ButtonWidget(x + i * 20, y, 18, 18, new LiteralText(String.valueOf(i + 1)), button -> {
                callback.onSelect(slotIndex);
            }) {
                @Override
                public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                    if (slotIndex == currentSlot) {
                        fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, 0xAA00FF00);
                    }
                    super.renderButton(matrices, mouseX, mouseY, delta);
                }
            });
        }
    }

    private void saveConfig() {
        try {
            AnchorMacroConfig.delayAnchor = Long.parseLong(delayAnchorField.getText());
            AnchorMacroConfig.delayGlowstone = Long.parseLong(delayGlowstoneField.getText());
            AnchorMacroConfig.delayTotem = Long.parseLong(delayTotemField.getText());
            AnchorMacroConfig.delayExplode = Long.parseLong(delayExplodeField.getText());
        } catch (NumberFormatException ignored) {}

        AnchorMacroConfig.autoCharge = autoChargeToggle.isChecked();
        AnchorMacroConfig.autoTotem = autoTotemToggle.isChecked();

        AnchorMacroConfig.anchorSlot = anchorSlot;
        AnchorMacroConfig.glowstoneSlot = glowstoneSlot;
        AnchorMacroConfig.totemSlot = totemSlot;

        AnchorMacroConfig.save();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
        delayAnchorField.render(matrices, mouseX, mouseY, delta);
        delayGlowstoneField.render(matrices, mouseX, mouseY, delta);
        delayTotemField.render(matrices, mouseX, mouseY, delta);
        delayExplodeField.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @FunctionalInterface
    private interface SlotCallback {
        void onSelect(int slot);
    }

    private void drawCenteredText(net.minecraft.client.font.TextRenderer textRenderer, String text, int x, int y, int color) {
        textRenderer.drawWithShadow(null, text, x - textRenderer.getWidth(text) / 2f, y, color);
    }
                       }
