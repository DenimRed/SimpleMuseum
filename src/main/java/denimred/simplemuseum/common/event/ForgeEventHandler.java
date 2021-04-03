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

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import denimred.simplemuseum.SimpleMuseum;
import denimred.simplemuseum.common.init.MuseumCommands;
import denimred.simplemuseum.common.init.MuseumEntities;
import denimred.simplemuseum.common.init.MuseumItems;

@Mod.EventBusSubscriber(modid = SimpleMuseum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventHandler {
    @SubscribeEvent
    public static void onItemRegistryMissingMappings(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
            if (mapping.key.equals(
                    new ResourceLocation(SimpleMuseum.MOD_ID, "museum_dummy_spawn_egg"))) {
                mapping.remap(MuseumItems.CURATORS_CANE.get());
                break; // Don't forget to remove this if we remap anything else
            }
        }
    }

    @SubscribeEvent
    public static void onEntityRegistryMissingMappings(
            RegistryEvent.MissingMappings<EntityType<?>> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityType<?>> mapping :
                event.getAllMappings()) {
            if (mapping.key.equals(new ResourceLocation(SimpleMuseum.MOD_ID, "museum_dummy"))) {
                mapping.remap(MuseumEntities.MUSEUM_PUPPET.get());
                break; // Don't forget to remove this if we remap anything else
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        MuseumCommands.register(event.getDispatcher());
    }
}
