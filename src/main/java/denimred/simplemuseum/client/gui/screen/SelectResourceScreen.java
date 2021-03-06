package denimred.simplemuseum.client.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import denimred.simplemuseum.client.gui.widget.ResourceFieldWidget;
import denimred.simplemuseum.client.util.ResourceUtil;

public class SelectResourceScreen extends SelectObjectScreen<ResourceLocation> {
    protected final ResourceFieldWidget caller;

    protected SelectResourceScreen(Screen parent, Widget owner, ResourceFieldWidget caller) {
        this(parent, owner.getMessage(), caller);
    }

    protected SelectResourceScreen(
            Screen parent, ITextComponent title, ResourceFieldWidget caller) {
        super(parent, title);
        this.caller = caller;
    }

    @Override
    protected void onSave() {
        if (selected != null) {
            caller.setLocation(selected.value, true);
        }
    }

    @Override
    protected boolean isSelected(ListWidget.Entry entry) {
        return entry.value.equals(caller.getLocation());
    }

    @Override
    protected CompletableFuture<List<ResourceLocation>> getEntriesAsync() {
        final String path = caller.getPathPrefix().replaceFirst("/$", "");
        return ResourceUtil.getCachedResourcesAsync(path, caller::validate);
    }
}
