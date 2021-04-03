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
package denimred.simplemuseum.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import denimred.simplemuseum.SimpleMuseum;
import denimred.simplemuseum.client.renderer.entity.MuseumPuppetRenderer;
import denimred.simplemuseum.common.init.MuseumEntities;
import denimred.simplemuseum.common.init.MuseumKeybinds;

@Mod.EventBusSubscriber(
        modid = SimpleMuseum.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public final class ClientModEventHandler {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(
                MuseumEntities.MUSEUM_PUPPET.get(), MuseumPuppetRenderer::new);
        ClientRegistry.registerKeyBinding(MuseumKeybinds.GLOBAL_HIGHLIGHTS);
    }
}
