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

package denimred.simplemuseum.client.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import cryptcraft.cryptmaster.plugin.client.UtilityTool;
import denimred.simplemuseum.common.init.MuseumEntities;
import denimred.simplemuseum.common.init.MuseumItems;
import denimred.simplemuseum.common.init.MuseumLang;
import denimred.simplemuseum.modcompat.cryptmaster.MuseumTool;

public class MuseumLanguageProvider extends LanguageProvider {
    public MuseumLanguageProvider(DataGenerator gen, String modId, String locale) {
        super(gen, modId, locale);
    }

    @Override
    protected void addTranslations() {
        // Lang for registry objects
        this.addEntityType(MuseumEntities.MUSEUM_PUPPET, "Museum Puppet");
        this.addItem(MuseumItems.CURATORS_CANE, "Curator's Cane");
        // Misc lang
        MuseumLang.provideFor(this);
        // Mod compat lang (datagen is done in dev, no need to worry about classloading)
        this.addCryptMasterTool(MuseumTool.INSTANCE, "Create/Edit Museum Puppet");
    }

    @SuppressWarnings("SameParameterValue")
    private void addCryptMasterTool(UtilityTool tool, String name) {
        this.add(tool.getTooltip().getKey(), name);
    }
}
