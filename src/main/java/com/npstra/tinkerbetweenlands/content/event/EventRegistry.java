package com.npstra.tinkerbetweenlands.content.event;

import net.minecraftforge.common.MinecraftForge;

public class EventRegistry {
    public static void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new BetweenlandsEventHandler());
        MinecraftForge.EVENT_BUS.register(new SulfurLeachEventHandler());
    }
}