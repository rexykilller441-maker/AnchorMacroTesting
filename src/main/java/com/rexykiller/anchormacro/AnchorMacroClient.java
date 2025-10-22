package com.rexykiller.anchormacro;

import com.rexykiller.anchormacro.client.AnchorMacroConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AnchorMacroClient implements ClientModInitializer {

    private static KeyBinding activateKey;
    private static KeyBinding configKey;

    @Override
    public void onInitializeClient() {
        AnchorMacroConfig.load();

        // Macro activation key (default R)
        activateKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.anchormacro.activate",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.anchormacro"
        ));

        // Config GUI key (default O)
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.anchormacro.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.anchormacro"
        ));

        // Tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Execute macro
            while (activateKey.wasPressed()) {
                AnchorMacroExecutor.execute(client);
            }

            // Open config GUI
            while (configKey.wasPressed()) {
                client.setScreen(new AnchorMacroConfigScreen(client.currentScreen));
            }
        });
    }
}
