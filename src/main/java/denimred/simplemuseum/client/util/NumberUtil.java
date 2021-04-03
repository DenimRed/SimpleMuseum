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
package denimred.simplemuseum.client.util;

import java.util.Optional;

public class NumberUtil {
    public static String parseString(double d) {
        String s = Double.toString(d);
        if (!s.isEmpty() && s.charAt(0) != '-') {
            s = "+".concat(s);
        }
        return s;
    }

    public static String parseString(float d) {
        String s = Float.toString(d);
        if (!s.isEmpty() && s.charAt(0) != '-') {
            s = "+".concat(s);
        }
        return s;
    }

    public static boolean isValidDouble(String s) {
        if (s.isEmpty() || s.equals("+") || s.equals("-")) {
            return true;
        }
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidFloat(String s) {
        if (s.isEmpty() || s.equals("+") || s.equals("-")) {
            return true;
        }
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Optional<Double> parseDouble(String s) {
        try {
            return Optional.of(Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Float> parseFloat(String s) {
        try {
            return Optional.of(Float.parseFloat(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
