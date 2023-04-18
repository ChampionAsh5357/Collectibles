/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.collectibles.util;

import net.ashwork.mc.collectibles.Collectibles;

/**
 * A utility containing information related to the translation keys used within
 * this mod.
 */
public class CollectiblesTranslationKeys {

    /**
     * Private constructor.
     */
    private CollectiblesTranslationKeys() {
        throw new AssertionError("CollectiblesTranslationKeys should not be initialized.");
    }

    /**
     * A translation key representing the description of this mod's resources
     * in the {@code pack.mcmeta}.
     */
    public static final String PACK_DESCRIPTION = create("pack", "description");

    /**
     * Creates a new localization key with this mod's id and interns it for
     * any future use.
     *
     * @param type the object type the localization is representing
     * @param key the key of the object the localization is representing
     * @return a localization key
     */
    public static String create(String type, String key) {
        return (type + "." + Collectibles.ID + "." + key).intern();
    }
}
