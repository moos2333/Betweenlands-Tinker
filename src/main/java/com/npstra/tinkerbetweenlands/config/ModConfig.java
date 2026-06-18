package com.npstra.tinkerbetweenlands.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.io.File;

public class ModConfig {
    public static int dimensionId = 0;

    public static void init(FMLPreInitializationEvent event) {
        File file = new File(event.getModConfigurationDirectory(), "betweenlandstinker.cfg");
        Configuration config = new Configuration(file);
        try {
            config.load();
            dimensionId = config.getInt("BetweenlandsDimensionId", "general", 20, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
}