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
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;
import java.util.function.Supplier;

import denimred.simplemuseum.common.init.MuseumItems;

public class MuseumItemModelProvider extends ItemModelProvider {
    public MuseumItemModelProvider(
            DataGenerator generator, String modId, ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.handheld(MuseumItems.CURATORS_CANE);
    }

    @SuppressWarnings("SameParameterValue")
    protected void handheld(Supplier<? extends Item> item) {
        final ResourceLocation name = Objects.requireNonNull(item.get().getRegistryName());
        final ResourceLocation tex =
                new ResourceLocation(name.getNamespace(), ITEM_FOLDER + "/" + name.getPath());
        this.withExistingParent(name.toString(), "item/handheld").texture("layer0", tex);
    }
}
