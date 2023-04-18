/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.collectibles.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * The main mod class for client information within this mod.
 */
public class CollectiblesClient {

    /**
     * Initialize the client information for this mod.
     *
     * @param modBus the mod event bus
     * @param forgeBus the Forge event bus
     */
    public static void init(IEventBus modBus, IEventBus forgeBus) {
        Minecraft.getInstance();
    }
}
