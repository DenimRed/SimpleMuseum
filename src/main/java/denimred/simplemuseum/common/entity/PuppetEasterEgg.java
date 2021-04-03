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

import java.util.function.Predicate;

public enum PuppetEasterEgg {
    HELLO_HOW_R_U(forName(":)"));

    private final Predicate<MuseumPuppetEntity> checker;

    PuppetEasterEgg(Predicate<MuseumPuppetEntity> checker) {
        this.checker = checker;
    }

    private static Predicate<MuseumPuppetEntity> forName(String name) {
        return puppet ->
                puppet.hasCustomName()
                        && puppet.getName().getUnformattedComponentText().equals(name);
    }

    public boolean isActive(MuseumPuppetEntity puppet) {
        return checker.test(puppet);
    }
}
