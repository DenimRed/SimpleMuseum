package denimred.simplemuseum.common.util;

import com.mojang.math.Vector3f;

import net.minecraft.world.phys.Vec3;

public final class MathUtil {
    public static float yawBetween(Vec3 a, Vec3 b) {
        return (float) Math.toDegrees(Math.atan2(b.z - a.z, b.x - a.x) - Math.PI / 2.0D);
    }

    // TODO: Intended for use in AI and similar stuff, will probably be moved/removed later
    public static Vector3f getLookAtRotation(Vec3 source, Vec3 target) {
        final Vec3 diff = target.subtract(source);
        final double y = Math.atan2(diff.x, diff.z);
        final double x = Math.atan2(diff.y, Math.sqrt(diff.x * diff.x + diff.z * diff.z));
        return new Vector3f((float) x, (float) y, 0.0F);
    }

    public static int max(int start, int... values) {
        int i = start;
        for (int value : values) {
            i = Math.max(i, value);
        }
        return i;
    }
}
