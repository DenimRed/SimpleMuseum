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

package denimred.simplemuseum.common.util;

import net.minecraft.util.math.vector.Vector3d;

public final class MathUtil {
    public static float yawBetween(Vector3d a, Vector3d b) {
        return (float) Math.toDegrees(Math.atan2(b.z - a.z, b.x - a.x) - Math.PI / 2.0D);
    }
}
