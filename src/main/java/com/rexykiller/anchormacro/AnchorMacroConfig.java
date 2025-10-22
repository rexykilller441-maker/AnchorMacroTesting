package com.rexykiller.anchormacro;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class AnchorMacroConfig {
    public static int anchorSlot = 0;
    public static int glowstoneSlot = 1;
    public static int totemSlot = 2;

    public static long delayAnchor = 200;
    public static long delayGlowstone = 200;
    public static long delayTotem = 200;
    public static long delayExplode = 300;

    public static boolean autoCharge = true;
    public static boolean autoTotem = true;

    private static final File configFile = new File("config/anchormacro.json");
    private static final Gson gson = new Gson();

    public static void load() {
        try {
            if (!configFile.exists()) save();
            AnchorMacroConfig data = gson.fromJson(new FileReader(configFile), AnchorMacroConfig.class);
            anchorSlot = data.anchorSlot;
            glowstoneSlot = data.glowstoneSlot;
            totemSlot = data.totemSlot;
            delayAnchor = data.delayAnchor;
            delayGlowstone = data.delayGlowstone;
            delayTotem = data.delayTotem;
            delayExplode = data.delayExplode;
            autoCharge = data.autoCharge;
            autoTotem = data.autoTotem;
        } catch (Exception ignored) {}
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(new AnchorMacroConfig(), writer);
        } catch (Exception ignored) {}
    }
              }
