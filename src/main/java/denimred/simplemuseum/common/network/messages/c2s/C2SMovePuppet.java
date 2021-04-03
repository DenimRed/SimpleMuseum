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
package denimred.simplemuseum.common.network.messages.c2s;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

import denimred.simplemuseum.common.entity.MuseumPuppetEntity;

// TODO: This packet is vulnerable to abuse by malicious clients. Determine a way to make this
//       packet safer without sacrificing functionality.
/** Tells the server to move a puppet to a particular position. */
public class C2SMovePuppet {
    private final UUID uuid;
    private final Vector3d pos;
    private final float yaw;

    public C2SMovePuppet(UUID uuid, Vector3d pos, float yaw) {
        this.uuid = uuid;
        this.pos = pos;
        this.yaw = yaw;
    }

    public static C2SMovePuppet decode(PacketBuffer buf) {
        final UUID puppet = buf.readUniqueId();
        final double x = buf.readDouble();
        final double y = buf.readDouble();
        final double z = buf.readDouble();
        final float yaw = buf.readFloat();
        return new C2SMovePuppet(puppet, new Vector3d(x, y, z), yaw);
    }

    public void encode(PacketBuffer buf) {
        buf.writeUniqueId(uuid);
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);
        buf.writeFloat(yaw);
    }

    public void handle(Supplier<NetworkEvent.Context> sup) {
        final NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> doWork(ctx));
        ctx.setPacketHandled(true);
    }

    @SuppressWarnings("deprecation") // Mojang >:I
    private void doWork(NetworkEvent.Context ctx) {
        final ServerPlayerEntity sender = ctx.getSender();
        if (sender != null) {
            final ServerWorld world = sender.getServerWorld();
            final Entity entity = world.getEntityByUuid(uuid);
            if (entity instanceof MuseumPuppetEntity) {
                final MuseumPuppetEntity puppet = (MuseumPuppetEntity) entity;
                if (puppet.isAlive()
                        && world.isBlockLoaded(new BlockPos(puppet.getPositionVec()))
                        && world.isBlockLoaded(new BlockPos(pos))) {
                    puppet.setLocationAndAngles(
                            pos.x, pos.y, pos.z, MathHelper.wrapDegrees(yaw), puppet.rotationPitch);
                }
            }
        }
    }
}
