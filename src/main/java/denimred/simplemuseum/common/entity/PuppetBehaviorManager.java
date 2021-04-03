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
package denimred.simplemuseum.common.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;

public class PuppetBehaviorManager extends PuppetManager {
    // The root NBT key that this manager uses
    public static final String BEHAVIOR_MANAGER_NBT = "BehaviorManager";

    protected PuppetBehaviorManager(MuseumPuppetEntity puppet) {
        super(puppet, BEHAVIOR_MANAGER_NBT);
    }

    @Override
    protected void registerDataKeys() {}

    @Override
    public void onDataChanged(DataParameter<?> key) {}

    @Override
    protected void readNBT(CompoundNBT tag) {}

    @Override
    protected void writeNBT(CompoundNBT tag) {}
}
