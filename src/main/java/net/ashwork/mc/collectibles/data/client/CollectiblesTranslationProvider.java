/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.collectibles.data.client;

import net.ashwork.mc.collectibles.Collectibles;
import net.ashwork.mc.collectibles.util.CollectiblesTranslationKeys;
import net.ashwork.mc.collectibles.util.SupportedLocales;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link DataProvider} used to generate translations for any locales
 * specified.
 */
public class CollectiblesTranslationProvider implements DataProvider {

    private final Function<String, LanguageProvider> factory;
    private final Map<String, LanguageProvider> translations;

    /**
     * Constructs a new provider to generate language files for many locales.
     *
     * @param output metadata related to the output location of the generator
     */
    public CollectiblesTranslationProvider(PackOutput output) {
        this.factory = locale -> new LanguageProvider(output, Collectibles.ID, locale) {
            @Override
            protected void addTranslations() {}
        };
        this.translations = new HashMap<>();
    }

    /**
     * Adds the localizations for the mod.
     */
    private void addTranslations() {
        this.localizeAll(CollectiblesTranslationKeys.PACK_DESCRIPTION, Map.of(
                SupportedLocales.EN_US, "Collectibles Resources"
        ));
    }

    /**
     * Localizes all names for a given key.
     *
     * @param key the key to add localizations for
     * @param localeToName a map of locales to localized names
     */
    private void localizeAll(String key, Map<String, String> localeToName) {
        this.localizeAll((provider, name) -> provider.add(key, name), localeToName);
    }

    /**
     * Localizes all names for a given provider method.
     *
     * @param nameInserter a consumer containing the {@link LanguageProvider} and
     *                     the localized name for that provider
     * @param localeToName a map of locales to localized names
     */
    private void localizeAll(BiConsumer<LanguageProvider, String> nameInserter, Map<String, String> localeToName) {
        localeToName.forEach((locale, name) -> this.forLocale(locale, provider -> nameInserter.accept(provider, name)));
    }

    /**
     * Grants access to the {@link LanguageProvider} for the specific locale to
     * add keys.
     *
     * @param locale the locale to get the localization for
     * @param provider a consumer containing the {@link LanguageProvider}
     */
    private void forLocale(String locale, Consumer<LanguageProvider> provider) {
        provider.accept(this.translations.computeIfAbsent(locale, this.factory));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        // Add translations
        this.addTranslations();

        // Add all completable futures to final future
        CompletableFuture<?>[] futures = new CompletableFuture<?>[this.translations.size()];
        int index = 0;
        for (var provider : this.translations.values()) futures[index++] = provider.run(output);
        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return "Collectibles Translations";
    }
}
