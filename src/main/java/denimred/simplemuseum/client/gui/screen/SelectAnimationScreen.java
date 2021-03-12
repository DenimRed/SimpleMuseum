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

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class SelectAnimationScreen extends SelectObjectScreen<String> {
    protected final TextFieldWidget caller;
    private final Supplier<ResourceLocation> fileLocSupplier;

    protected SelectAnimationScreen(
            Screen parent,
            Widget owner,
            TextFieldWidget caller,
            Supplier<ResourceLocation> fileLocSupplier) {
        this(parent, owner.getMessage(), caller, fileLocSupplier);
    }

    protected SelectAnimationScreen(
            Screen parent,
            ITextComponent title,
            TextFieldWidget caller,
            Supplier<ResourceLocation> fileLocSupplier) {
        super(parent, title);
        this.caller = caller;
        this.fileLocSupplier = fileLocSupplier;
    }

    @Override
    protected void onSave() {
        if (selected != null) {
            caller.setText(selected.value);
        }
    }

    @Override
    protected boolean isSelected(ListWidget.Entry entry) {
        return entry.value.equals(caller.getText());
    }

    @Override
    protected CompletableFuture<List<String>> getEntriesAsync() {
        final ResourceLocation loc = fileLocSupplier.get();
        if (loc != null) {
            final AnimationFile file = GeckoLibCache.getInstance().getAnimations().get(loc);
            if (file != null) {
                return CompletableFuture.supplyAsync(
                        () ->
                                file.getAllAnimations().stream()
                                        .map(anim -> anim.animationName)
                                        .collect(Collectors.toList()));
            }
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
