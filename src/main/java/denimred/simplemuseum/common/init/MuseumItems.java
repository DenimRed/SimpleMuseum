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

package denimred.simplemuseum.common.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import denimred.simplemuseum.SimpleMuseum;
import denimred.simplemuseum.common.item.CuratorsCaneItem;

public final class MuseumItems {
    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, SimpleMuseum.MOD_ID);

    public static final RegistryObject<CuratorsCaneItem> CURATORS_CANE =
            REGISTRY.register(
                    "curators_cane",
                    () ->
                            new CuratorsCaneItem(
                                    new Item.Properties()
                                            .maxStackSize(1)
                                            .rarity(Rarity.UNCOMMON)
                                            .group(ItemGroup.TOOLS)));
}
