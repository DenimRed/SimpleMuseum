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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

import denimred.simplemuseum.common.entity.MuseumPuppetEntity;

@Deprecated // Need to update the UI before we can get rid of this
public class C2SConfigurePuppet {
    private final UUID uuid;
    private final ResourceLocation modelLoc;
    private final ResourceLocation texLoc;
    private final ResourceLocation animLoc;
    private final String selAnim;

    public C2SConfigurePuppet(
            UUID uuid,
            ResourceLocation modelLoc,
            ResourceLocation texLoc,
            ResourceLocation animLoc,
            String selAnim) {
        this.uuid = uuid;
        this.modelLoc = modelLoc;
        this.texLoc = texLoc;
        this.animLoc = animLoc;
        this.selAnim = selAnim;
    }

    public static C2SConfigurePuppet decode(PacketBuffer buf) {
        final UUID puppet = buf.readUniqueId();
        final ResourceLocation modelLoc = buf.readResourceLocation();
        final ResourceLocation texLoc = buf.readResourceLocation();
        final ResourceLocation animLoc = buf.readResourceLocation();
        final String selAnim = buf.readString(32767);
        return new C2SConfigurePuppet(puppet, modelLoc, texLoc, animLoc, selAnim);
    }

    public void encode(PacketBuffer buf) {
        buf.writeUniqueId(uuid);
        buf.writeResourceLocation(modelLoc);
        buf.writeResourceLocation(texLoc);
        buf.writeResourceLocation(animLoc);
        buf.writeString(selAnim);
    }

    public void handle(Supplier<NetworkEvent.Context> sup) {
        final NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> doWork(ctx));
        ctx.setPacketHandled(true);
    }

    private void doWork(NetworkEvent.Context ctx) {
        final ServerPlayerEntity sender = ctx.getSender();
        if (sender != null) {
            final ServerWorld world = sender.getServerWorld();
            final Entity entity = world.getEntityByUuid(uuid);
            if (entity instanceof MuseumPuppetEntity) {
                final MuseumPuppetEntity puppet = (MuseumPuppetEntity) entity;
                if (world.isBlockLoaded(puppet.getPosition()) && puppet.isAlive()) {
                    puppet.sourceManager.model.set(modelLoc);
                    puppet.sourceManager.texture.set(texLoc);
                    puppet.sourceManager.animations.set(animLoc);
                    puppet.animationManager.idle.set(selAnim);
                }
            }
        }
    }
}
