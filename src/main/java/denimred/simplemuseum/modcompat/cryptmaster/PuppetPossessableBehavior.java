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

package denimred.simplemuseum.modcompat.cryptmaster;

import net.minecraft.entity.player.PlayerEntity;

import cryptcraft.cryptcomp.entity.EntityComponent;
import cryptcraft.cryptmaster.IPossessableBehavior;
import cryptcraft.cryptmaster.PossessableComponent;
import denimred.simplemuseum.common.entity.MuseumPuppetEntity;

public class PuppetPossessableBehavior implements IPossessableBehavior {
    private final MuseumPuppetEntity puppet;

    public PuppetPossessableBehavior(MuseumPuppetEntity puppet) {
        this.puppet = puppet;
    }

    public static void register() {
        EntityComponent.INSTANCE.registerInitializer(
                MuseumPuppetEntity.class,
                PossessableComponent.class,
                entity -> PuppetPossessableBehavior.createComponent((MuseumPuppetEntity) entity));
    }

    public static PossessableComponent createComponent(MuseumPuppetEntity puppet) {
        final PossessableComponent comp = new PossessableComponent(puppet);
        comp.setBehavior(new PuppetPossessableBehavior(puppet));
        return comp;
    }

    @Override
    public void startPossess() {}

    @Override
    public void endPossess() {}

    @Override
    public void applyActing(PlayerEntity player) {
        puppet.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
        puppet.setMotion(player.getMotion());
        puppet.setNoGravity(true);
        puppet.setSneaking(player.isSneaking());
        puppet.setSwimming(player.isSwimming());
        puppet.setPose(player.getPose());

        puppet.rotationYaw = player.rotationYaw;
        puppet.prevRotationYaw = player.prevRotationYaw;
        puppet.rotationPitch = player.rotationPitch;
        puppet.prevRotationPitch = player.prevRotationPitch;
        puppet.rotationYawHead = player.rotationYawHead;
        puppet.prevRotationYawHead = player.prevRotationYawHead;
        puppet.renderYawOffset = player.renderYawOffset;
        puppet.prevRenderYawOffset = player.prevRenderYawOffset;
    }
}
