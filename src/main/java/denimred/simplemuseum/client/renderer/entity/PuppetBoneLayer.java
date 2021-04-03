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
package denimred.simplemuseum.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.util.RenderUtils;

// TODO: I'd prefer not to hardcode the checks here (i.e. let "_slime" be configurable)
public enum PuppetBoneLayer {
    SOLID(PuppetBoneLayer::isSolid, null),
    TRANSLUCENT(bone -> bone.name.endsWith("_slime"), RenderType::getEntityTranslucent);

    private final Predicate<GeoBone> predicate;
    private final @Nullable Function<ResourceLocation, RenderType> type;

    PuppetBoneLayer(
            Predicate<GeoBone> predicate, @Nullable Function<ResourceLocation, RenderType> type) {
        this.predicate = predicate;
        this.type = type;
    }

    private static boolean isSolid(GeoBone bone) {
        for (PuppetBoneLayer layer : PuppetBoneLayer.values()) {
            if (layer != SOLID && layer.isBoneOnLayer(bone)) {
                return false;
            }
        }
        return true;
    }

    public boolean isBoneOnLayer(GeoBone bone) {
        return predicate.test(bone);
    }

    public Optional<RenderType> getType(ResourceLocation texture) {
        return Optional.ofNullable(type).map(f -> f.apply(texture));
    }

    public void render(MatrixStack stack, GeoBone bone, Consumer<GeoCube> cubeRenderer) {
        stack.push();
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        RenderUtils.rotate(bone, stack);
        RenderUtils.scale(bone, stack);
        RenderUtils.moveBackFromPivot(bone, stack);

        if (!bone.isHidden) {
            if (this.isBoneOnLayer(bone)) {
                for (GeoCube cube : bone.childCubes) {
                    stack.push();
                    cubeRenderer.accept(cube);
                    stack.pop();
                }
            }
            for (GeoBone childBone : bone.childBones) {
                this.render(stack, childBone, cubeRenderer);
            }
        }

        stack.pop();
    }
}
