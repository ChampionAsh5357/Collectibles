/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.collectibles;

import net.ashwork.mc.collectibles.client.CollectiblesClient;
import net.ashwork.mc.collectibles.data.CollectiblesData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * The main mod class for this mod.
 */
@Mod(Collectibles.ID)
public class Collectibles {

    /**
     * The identifier of this mod.
     */
    public static final String ID = "collectibles";

    /**
     * Constructs the mod instance.
     */
    public Collectibles() {
        // Get buses
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        var forgeBus = MinecraftForge.EVENT_BUS;

        // Setup client
        if (FMLEnvironment.dist == Dist.CLIENT)
            CollectiblesClient.init(modBus, forgeBus);

        // Setup data
        CollectiblesData.init(modBus);
    }
}
