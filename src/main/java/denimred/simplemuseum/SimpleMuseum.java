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

package denimred.simplemuseum;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import denimred.simplemuseum.client.util.ResourceUtil;
import denimred.simplemuseum.common.init.MuseumDataSerializers;
import denimred.simplemuseum.common.init.MuseumEntities;
import denimred.simplemuseum.common.init.MuseumItems;
import denimred.simplemuseum.common.init.MuseumNetworking;
import denimred.simplemuseum.modcompat.ModCompat;

@Mod(SimpleMuseum.MOD_ID)
public final class SimpleMuseum {
    public static final String MOD_ID = "simplemuseum";
    public static final Logger LOGGER = LogManager.getLogger();

    public SimpleMuseum() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Game object registry stuff
        MuseumDataSerializers.REGISTRY.register(bus);
        MuseumEntities.REGISTRY.register(bus);
        MuseumItems.REGISTRY.register(bus);
        // Networking stuff
        MuseumNetworking.registerMessages();
        // Misc client stuff
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ResourceUtil::registerResourceReloadListener);
        // Mod compat stuff
        bus.addListener(ModCompat::enqueueIMC);
        ModCompat.registerCryptMasterPossession();
    }
}
