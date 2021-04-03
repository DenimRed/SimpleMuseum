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
package denimred.simplemuseum.common.network.messages.bidirectional;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import denimred.simplemuseum.client.util.ClientUtil;
import denimred.simplemuseum.common.entity.MuseumPuppetEntity;
import denimred.simplemuseum.common.init.MuseumLang;
import denimred.simplemuseum.common.init.MuseumNetworking;
import denimred.simplemuseum.common.network.NetworkUtil;

// TODO: This packet is vulnerable to abuse by malicious clients. Determine a way to make this
//       packet safer without sacrificing functionality.
/**
 * Handshake between the server and client to handle copy/pasting puppet data.
 *
 * <p>When sending to the client, the server simply requests the client to either copy data to their
 * clipboard or send their (parsed) clipboard back to the server.
 *
 * <p>When sending to the server, the client will parse their clipboard as NBT and send it if it's
 * valid.
 */
public final class CopyPastePuppetData {
    private static final int REQUEST_COPY = 0;
    private static final int REQUEST_PASTE = 1;
    private static final int SEND_CLIPBOARD = 2;
    private final int puppetId;
    private final int state;
    private final CompoundNBT data;

    private CopyPastePuppetData(int puppetId, int state) {
        this(puppetId, state, new CompoundNBT());
    }

    private CopyPastePuppetData(int puppetId, int state, CompoundNBT data) {
        this.puppetId = puppetId;
        this.state = state;
        this.data = data;
    }

    public static CopyPastePuppetData copy(MuseumPuppetEntity puppet) {
        return new CopyPastePuppetData(puppet.getEntityId(), REQUEST_COPY);
    }

    public static CopyPastePuppetData paste(MuseumPuppetEntity puppet) {
        return new CopyPastePuppetData(puppet.getEntityId(), REQUEST_PASTE);
    }

    public static void register(SimpleChannel channel, int id) {
        channel.registerMessage(
                id,
                CopyPastePuppetData.class,
                CopyPastePuppetData::encode,
                CopyPastePuppetData::decode,
                CopyPastePuppetData::handle);
    }

    private static CopyPastePuppetData decode(PacketBuffer buf) {
        final int puppetId = buf.readVarInt();
        final int state = buf.readVarInt();
        if (state == SEND_CLIPBOARD) {
            final CompoundNBT data = buf.readCompoundTag();
            if (data != null) {
                return new CopyPastePuppetData(puppetId, state, data);
            }
        }
        return new CopyPastePuppetData(puppetId, state);
    }

    private void encode(PacketBuffer buf) {
        buf.writeVarInt(puppetId);
        buf.writeVarInt(state);
        if (state == SEND_CLIPBOARD) {
            buf.writeCompoundTag(data);
        }
    }

    private void handle(Supplier<NetworkEvent.Context> sup) {
        final NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> doWork(ctx));
        ctx.setPacketHandled(true);
    }

    private void doWork(NetworkEvent.Context ctx) {
        NetworkUtil.getValidPuppet(ctx, puppetId).ifPresent(puppet -> processPuppet(ctx, puppet));
    }

    private void processPuppet(NetworkEvent.Context ctx, MuseumPuppetEntity puppet) {
        final CommandSource source = NetworkUtil.getCommonPlayerCommandSource(ctx);
        if (puppet.world.isRemote) {
            if (state == REQUEST_COPY) {
                this.copyPuppetData(puppet, source);
            } else if (state == REQUEST_PASTE) {
                this.sendPuppetData(puppet, source, ctx);
            }
        } else if (state == SEND_CLIPBOARD) {
            this.receivePuppetData(puppet, source);
        }
    }

    private void copyPuppetData(MuseumPuppetEntity puppet, @Nullable CommandSource source) {
        final CompoundNBT tag = new CompoundNBT();
        puppet.writeModTag(tag);
        ClientUtil.MC.keyboardListener.setClipboardString(tag.toString());
        if (source != null) {
            source.sendFeedback(
                    MuseumLang.COMMAND_FEEDBACK_PUPPET_COPY.asText(puppet.getDisplayName()), false);
        }
    }

    private void sendPuppetData(
            MuseumPuppetEntity puppet, @Nullable CommandSource source, NetworkEvent.Context ctx) {
        try {
            final String clipboard = ClientUtil.MC.keyboardListener.getClipboardString();
            final CompoundNBT data = JsonToNBT.getTagFromJson(clipboard);
            MuseumNetworking.CHANNEL.reply(
                    new CopyPastePuppetData(puppet.getEntityId(), SEND_CLIPBOARD, data), ctx);
        } catch (CommandSyntaxException e) {
            if (source != null) {
                source.sendErrorMessage(new StringTextComponent(e.getMessage()));
            }
        }
    }

    private void receivePuppetData(MuseumPuppetEntity puppet, @Nullable CommandSource source) {
        puppet.readModTag(data);
        if (source != null) {
            source.sendFeedback(
                    MuseumLang.COMMAND_FEEDBACK_PUPPET_PASTE.asText(puppet.getDisplayName()), true);
        }
    }
}
