/*
 * Simple Museum
 * Copyright (C) 2021 DenimRed
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package denimred.simplemuseum.common.event;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import denimred.simplemuseum.SimpleMuseum;
import denimred.simplemuseum.client.datagen.MuseumItemModelProvider;
import denimred.simplemuseum.client.datagen.MuseumLanguageProvider;
import denimred.simplemuseum.client.datagen.MuseumTextureProvider;

@Mod.EventBusSubscriber(modid = SimpleMuseum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEventHandler {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        final DataGenerator gen = event.getGenerator();
        final String modId = event.getModContainer().getModId();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new MuseumLanguageProvider(gen, modId, "en_us"));
            gen.addProvider(new MuseumItemModelProvider(gen, modId, existingFileHelper));
            gen.addProvider(new MuseumTextureProvider(gen, modId));
        }
    }
}
