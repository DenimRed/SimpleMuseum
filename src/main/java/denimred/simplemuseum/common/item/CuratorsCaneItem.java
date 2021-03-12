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

package denimred.simplemuseum.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SimpleFoiledItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import denimred.simplemuseum.client.util.ClientUtil;
import denimred.simplemuseum.common.entity.MuseumPuppetEntity;

public class CuratorsCaneItem extends SimpleFoiledItem {
    public CuratorsCaneItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType itemInteractionForEntity(
            ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof MuseumPuppetEntity) {
            if (!player.world.isRemote) {
                return ActionResultType.CONSUME;
            } else {
                ClientUtil.openPuppetScreen((MuseumPuppetEntity) target, null);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        final World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        } else {
            final Vector3d pos = context.getHitVec();
            final PlayerEntity player = context.getPlayer();
            MuseumPuppetEntity.spawn((ServerWorld) world, pos, player);

            return ActionResultType.CONSUME;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        final ItemStack stack = player.getHeldItem(hand);
        if (!(world instanceof ServerWorld)) {
            final MuseumPuppetEntity puppet = ClientUtil.getSelectedPuppet();
            if (puppet != null) {
                ClientUtil.openPuppetScreen(puppet, null);
                return ActionResult.resultSuccess(stack);
            }
        }
        return ActionResult.resultConsume(stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof MuseumPuppetEntity) {
            target.remove();
            return true;
        }
        return false;
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(
            BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }
}
