package denimred.simplemuseum.client.gui.widget.value;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import denimred.simplemuseum.SimpleMuseum;
import denimred.simplemuseum.client.gui.screen.PuppetConfigScreen;
import denimred.simplemuseum.client.gui.screen.SelectScreen;
import denimred.simplemuseum.client.gui.widget.BetterTextFieldWidget;
import denimred.simplemuseum.client.gui.widget.IconButton;
import denimred.simplemuseum.common.entity.puppet.manager.value.PuppetValue;
import denimred.simplemuseum.common.entity.puppet.manager.value.checked.CheckedValue;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.resource.GeckoLibCache;

public final class AnimationWidget extends ValueWidget<String, CheckedValue<String>> {
    public static final ResourceLocation FOLDER_BUTTON_TEXTURE =
            new ResourceLocation(SimpleMuseum.MOD_ID, "textures/gui/folder_button.png");
    private final TranslatableComponent title;
    private final IconButton selectButton;
    private final AnimationTextField animField;

    public AnimationWidget(PuppetConfigScreen parent, PuppetValue<?, ?> value) {
        this(parent, 0, 0, 0, 35 + HEIGHT_MARGIN * 2, value.cast());
        heightMargin = HEIGHT_MARGIN;
    }

    public AnimationWidget(
            PuppetConfigScreen parent,
            int x,
            int y,
            int width,
            int height,
            CheckedValue<String> value) {
        super(parent, x, y, width, height, value);
        title = new TranslatableComponent(value.provider.translationKey);
        selectButton =
                this.addChild(
                        new IconButton(
                                0,
                                0,
                                20,
                                20,
                                FOLDER_BUTTON_TEXTURE,
                                0,
                                0,
                                64,
                                32,
                                20,
                                this::selectAnimation));
        animField = this.addChild(new AnimationTextField(), true);
        this.setMaxWidth(200);
        this.detectAndSync();
    }

    @Override
    protected void recalculateChildren() {
        final int yPos = y + TITLE_OFFSET + heightMargin;
        selectButton.x = x;
        selectButton.y = yPos;
        animField.x = x + 20;
        animField.y = yPos;
        animField.setWidth(width - 60);
        this.setChangeButtonsPos(animField.x + animField.getWidth(), yPos);
        super.recalculateChildren();
    }

    @Override
    public void detectChanges() {
        super.detectChanges();
        final ResourceLocation animLoc = parent.getAnimationFile();
        final AnimationFile animFile = GeckoLibCache.getInstance().getAnimations().get(animLoc);
        final String anim = animField.getValue();
        final boolean fileExists = animFile != null;
        if (!fileExists || anim.isEmpty() || animFile.getAnimation(anim) != null) {
            animField.setTextColor(BetterTextFieldWidget.TEXT_VALID);
        } else {
            animField.setTextColor(BetterTextFieldWidget.TEXT_INVALID);
        }
        selectButton.active = fileExists;
    }

    @Override
    public void syncWithValue() {
        animField.setValue(valueRef.get());
        animField.moveCursorToStart();
    }

    private void selectAnimation(Button button) {
        MC.setScreen(new AnimSelectScreen());
    }

    private final class AnimationTextField extends BetterTextFieldWidget {
        public AnimationTextField() {
            super(MC.font, 0, 0, 0, 20, title);
            this.setMaxLength(MAX_PACKET_STRING);
            this.setResponder(this::respond);
        }

        private void respond(String s) {
            valueRef.set(s);
            AnimationWidget.this.detectChanges();
        }
    }

    private final class AnimSelectScreen extends SelectScreen<String> {
        private AnimSelectScreen() {
            super(AnimationWidget.this.parent, new TextComponent("Select Animation"));
        }

        @Override
        protected void onSave() {
            if (selected != null) {
                valueRef.set(selected.value);
                AnimationWidget.this.syncWithValue();
            }
        }

        @Override
        protected boolean isSelected(ListWidget.Entry entry) {
            return entry.value.equals(valueRef.get());
        }

        @Override
        protected CompletableFuture<List<String>> getEntriesAsync() {
            final ResourceLocation loc = AnimationWidget.this.parent.getAnimationFile();
            final AnimationFile file = GeckoLibCache.getInstance().getAnimations().get(loc);
            if (file != null) {
                return CompletableFuture.supplyAsync(
                        () ->
                                file.getAllAnimations().stream()
                                        .map(anim -> anim.animationName)
                                        .collect(Collectors.toList()));
            }
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
    }
}
