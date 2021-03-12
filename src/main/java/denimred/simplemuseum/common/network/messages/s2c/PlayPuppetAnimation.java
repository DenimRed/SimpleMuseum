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

package denimred.simplemuseum.common.network.messages.s2c;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

import denimred.simplemuseum.common.network.NetworkUtil;

/** Sent to clients to notify them that a particular puppet needs to play an animation. */
public final class PlayPuppetAnimation {
    private final int puppetId;
    private final String animation;

    public PlayPuppetAnimation(int puppetId, String animation) {
        this.puppetId = puppetId;
        this.animation = animation;
    }

    public static void register(SimpleChannel channel, int id) {
        channel.registerMessage(
                id,
                PlayPuppetAnimation.class,
                PlayPuppetAnimation::encode,
                PlayPuppetAnimation::decode,
                PlayPuppetAnimation::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private static PlayPuppetAnimation decode(PacketBuffer buf) {
        final int puppetId = buf.readVarInt();
        final String animation = buf.readString();
        return new PlayPuppetAnimation(puppetId, animation);
    }

    private void encode(PacketBuffer buf) {
        buf.writeVarInt(puppetId);
        buf.writeString(animation);
    }

    private void handle(Supplier<NetworkEvent.Context> sup) {
        final NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> doWork(ctx));
        ctx.setPacketHandled(true);
    }

    private void doWork(NetworkEvent.Context ctx) {
        NetworkUtil.getValidPuppet(ctx, puppetId)
                .ifPresent(puppet -> puppet.animationManager.playAnimOnce(animation));
    }
}
