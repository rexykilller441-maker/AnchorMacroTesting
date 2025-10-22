package com.rexykiller.anchormacro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AnchorMacroClient implements ClientModInitializer {
    private static KeyBinding activateKey;

    @Override
    public void onInitializeClient() {
        AnchorMacroConfig.load();
        activateKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.anchormacro.activate",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.anchormacro"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (activateKey.wasPressed()) {
                AnchorMacroExecutor.execute(client);
            }
        });
    }
}
