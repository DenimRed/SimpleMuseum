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

package denimred.simplemuseum.modcompat;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import cryptcraft.cryptgui.CryptGuiScreen;
import cryptcraft.cryptmaster.CryptMasterMod;
import cryptcraft.cryptmaster.PossessionUtil;
import cryptcraft.cryptmaster.forge.CryptMasterForgeMod;
import denimred.simplemuseum.client.util.ClientUtil;
import denimred.simplemuseum.common.entity.MuseumPuppetEntity;
import denimred.simplemuseum.modcompat.cryptmaster.MuseumPlugin;
import denimred.simplemuseum.modcompat.cryptmaster.PuppetPossessableBehavior;

public class ModCompat {
    public static void enqueueIMC(@SuppressWarnings("unused") final InterModEnqueueEvent event) {
        // Constants are inlined at compile time
        InterModComms.sendTo(
                CryptMasterMod.MOD_ID,
                CryptMasterForgeMod.REGISTER_PLUGIN_IMC,
                new MuseumPlugin.Thing());
    }

    public static boolean isCryptMasterLoaded() {
        return ModList.get().isLoaded(CryptMasterMod.MOD_ID);
    }

    public static boolean isCryptMasterActive() {
        return isCryptMasterLoaded() && ClientUtil.MC.currentScreen instanceof CryptGuiScreen;
    }

    public static boolean isCryptMasterPossessing(MuseumPuppetEntity puppet) {
        return isCryptMasterLoaded() && PossessionUtil.INSTANCE.isPossessed(puppet);
    }

    public static void registerCryptMasterPossession() {
        if (isCryptMasterLoaded()) {
            PuppetPossessableBehavior.register();
        }
    }
}
