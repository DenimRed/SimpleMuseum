package denimred.simplemuseum.client.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class SelectAnimationScreen extends AbstractSelectObjectScreen<String> {
    protected final TextFieldWidget caller;
    private final Supplier<ResourceLocation> fileLocSupplier;

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
    protected Collection<String> getEntries() {
        final ResourceLocation loc = fileLocSupplier.get();
        if (loc != null) {
            final AnimationFile file = GeckoLibCache.getInstance().getAnimations().get(loc);
            if (file != null) {
                return file.getAllAnimations().stream()
                        .map(anim -> anim.animationName)
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
