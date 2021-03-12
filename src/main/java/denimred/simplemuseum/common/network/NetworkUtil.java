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

package denimred.simplemuseum.common.network;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;

import javax.annotation.Nullable;

import denimred.simplemuseum.client.util.ClientUtil;
import denimred.simplemuseum.common.entity.MuseumPuppetEntity;

public final class NetworkUtil {
    @SuppressWarnings("deprecation") // Mojang >:I
    public static Optional<MuseumPuppetEntity> getValidPuppet(
            NetworkEvent.Context ctx, int puppetId) {
        if (ctx.getDirection().getReceptionSide().isServer()) {
            final ServerPlayerEntity sender = ctx.getSender();
            if (sender != null) {
                final ServerWorld world = sender.getServerWorld();
                final Entity entity = world.getEntityByID(puppetId);
                if (entity instanceof MuseumPuppetEntity
                        && world.isBlockLoaded(entity.getPosition())
                        && entity.isAlive()) {
                    return Optional.of((MuseumPuppetEntity) entity);
                }
            }
        } else {
            final ClientWorld world = ClientUtil.MC.world;
            if (world != null) {
                final Entity entity = world.getEntityByID(puppetId);
                if (entity instanceof MuseumPuppetEntity) {
                    return Optional.of((MuseumPuppetEntity) entity);
                }
            }
        }
        return Optional.empty();
    }

    @Nullable
    public static CommandSource getCommonPlayerCommandSource(NetworkEvent.Context ctx) {
        if (ctx.getDirection().getReceptionSide().isServer()) {
            final ServerPlayerEntity sender = ctx.getSender();
            if (sender != null) {
                return sender.getCommandSource();
            }
        } else if (ClientUtil.MC.player != null) {
            return ClientUtil.MC.player.getCommandSource();
        }
        return null;
    }
}
