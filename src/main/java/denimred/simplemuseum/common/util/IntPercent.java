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

import java.util.function.Consumer;

import javax.annotation.Nullable;

public class IntPercent {
    private final int min;
    private final int max;
    @Nullable private final Consumer<Integer> callback;
    private int intVal;
    private float floatVal;

    public IntPercent(int min, int max, int defaultValue, @Nullable Consumer<Integer> callback) {
        this.min = min;
        this.max = max;
        // Is it arguably better to set the default before assigning the callback?
        this.set(defaultValue);
        this.callback = callback;
    }

    public int asInt() {
        return intVal;
    }

    public float asFloat() {
        return floatVal;
    }

    public void set(float value) {
        this.set(Math.round(value * 100.0F));
    }

    public void set(int value) {
        intVal = Math.max(min, Math.min(max, value));
        floatVal = (float) intVal / 100.0F;
        if (callback != null) callback.accept(intVal);
    }
}
