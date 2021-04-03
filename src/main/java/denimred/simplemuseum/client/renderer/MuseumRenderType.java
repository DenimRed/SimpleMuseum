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
package denimred.simplemuseum.client.renderer;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import denimred.simplemuseum.SimpleMuseum;

// Utility class, only extends RenderType to provide protected access
public final class MuseumRenderType extends RenderType {
    @SuppressWarnings("ConstantConditions")
    private MuseumRenderType() {
        super(null, null, 0, 0, false, false, null, null);
        throw new UnsupportedOperationException("Utility class instantiation");
    }

    public static RenderType getErrorBanners(final ResourceLocation texture) {
        final RenderType.State state =
                RenderType.State.getBuilder()
                        .alpha(DEFAULT_ALPHA)
                        .texture(new RenderState.TextureState(texture, false, false))
                        .transparency(ADDITIVE_TRANSPARENCY)
                        .build(false);
        return RenderType.makeType(
                SimpleMuseum.MOD_ID + ":error_banners",
                DefaultVertexFormats.POSITION_COLOR_TEX,
                GL11.GL_QUAD_STRIP,
                256,
                false,
                false,
                state);
    }
}
