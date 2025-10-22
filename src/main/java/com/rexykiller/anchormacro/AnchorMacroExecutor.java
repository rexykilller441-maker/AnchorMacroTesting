package com.rexykiller.anchormacro;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;

public class AnchorMacroExecutor {
    public static void execute(MinecraftClient client) {
        if (client == null || client.player == null) return;
        ClientPlayerEntity player = client.player;

        new Thread(() -> {
            try {
                smoothSwitch(player, AnchorMacroConfig.anchorSlot);
                Thread.sleep(AnchorMacroConfig.delayAnchor);
                simulateUse(player);

                if (AnchorMacroConfig.autoCharge) {
                    smoothSwitch(player, AnchorMacroConfig.glowstoneSlot);
                    Thread.sleep(AnchorMacroConfig.delayGlowstone);
                    simulateUse(player);
                }

                if (AnchorMacroConfig.autoTotem) {
                    smoothSwitch(player, AnchorMacroConfig.totemSlot);
                    Thread.sleep(AnchorMacroConfig.delayTotem);
                }

                Thread.sleep(AnchorMacroConfig.delayExplode);
                simulateUse(player);

            } catch (InterruptedException ignored) {}
        }).start();
    }

    private static void smoothSwitch(ClientPlayerEntity player, int slot) {
        player.getInventory().selectedSlot = slot;
    }

    private static void simulateUse(ClientPlayerEntity player) {
        MinecraftClient.getInstance().interactionManager.interactItem(player, Hand.MAIN_HAND);
    }
        }
