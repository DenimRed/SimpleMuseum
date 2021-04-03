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
package denimred.simplemuseum.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

import denimred.simplemuseum.common.entity.MuseumPuppetEntity;

// Boy I sure do love inheritance :)
public abstract class MuseumPuppetScreen extends Screen {
    protected static final int MARGIN = 4;
    protected static final int TEXT_VALID = 0xe0e0e0;
    protected static final int TEXT_INVALID = 0xffff00;
    protected static final int TEXT_ERROR = 0xff0000;
    protected final Minecraft mc = Minecraft.getInstance(); // Parent's is nullable for some reason
    protected final MuseumPuppetEntity puppet;
    @Nullable protected final Screen parent;

    protected MuseumPuppetScreen(MuseumPuppetEntity puppet, @Nullable Screen parent) {
        super(puppet.getDisplayName());
        this.puppet = puppet;
        this.parent = parent;
    }

    protected static void drawStringLeft(
            MatrixStack matrixStack, FontRenderer font, Widget widget, ITextComponent text) {
        drawStringLeft(matrixStack, font, widget, text, 0xA0A0A0);
    }

    protected static void drawStringLeft(
            MatrixStack matrixStack,
            FontRenderer font,
            Widget widget,
            ITextComponent text,
            boolean bright) {
        drawStringLeft(matrixStack, font, widget, text, bright ? 0xFFFFFF : 0xA0A0A0);
    }

    protected static void drawStringLeft(
            MatrixStack matrixStack,
            FontRenderer font,
            Widget widget,
            ITextComponent text,
            int color) {
        drawString(
                matrixStack,
                font,
                text,
                widget.x - font.getStringPropertyWidth(text) - MARGIN,
                widget.y + widget.getHeight() / 2 - font.FONT_HEIGHT / 2,
                color);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        minecraft.keyboardListener.enableRepeatEvents(true);
        super.init(minecraft, width, height);
    }

    @Override
    public void closeScreen() {
        mc.displayGuiScreen(parent);
    }

    @Override
    public void onClose() {
        mc.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void renderWidgetTooltip(
            Widget widget, MatrixStack matrixStack, int mouseX, int mouseY) {
        this.renderTooltip(matrixStack, widget.getMessage(), mouseX, mouseY);
    }
}
