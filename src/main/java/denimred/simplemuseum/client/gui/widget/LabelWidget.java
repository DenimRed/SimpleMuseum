package denimred.simplemuseum.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class LabelWidget extends AbstractWidget {
    public final Font font;
    private final AnchorX anchorX;
    private final AnchorY anchorY;
    private List<? extends FormattedText> texts;
    @Nullable private Tooltip tooltip;
    private int left;
    private int right;
    private int top;
    private int bottom;
    private int lastX = x;
    private int lastY = y;

    public LabelWidget(
            int x, int y, Font font, AnchorX anchorX, AnchorY anchorY, FormattedText... messages) {
        this(x, y, font, anchorX, anchorY, Arrays.asList(messages));
    }

    public LabelWidget(
            int x,
            int y,
            Font font,
            AnchorX anchorX,
            AnchorY anchorY,
            List<? extends FormattedText> texts) {
        super(x, y, 0, 0, TextComponent.EMPTY);
        this.font = font;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.texts = texts;
        this.recalculateBounds();
    }

    private static int getMaxWidth(Font font, List<? extends FormattedText> messages) {
        int width = 0;
        for (FormattedText message : messages) {
            width = Math.max(width, font.width(message));
        }
        return width;
    }

    public void setTooltip(@Nullable Tooltip tooltip) {
        this.tooltip = tooltip;
    }

    private void recalculateBounds() {
        this.setWidth(getMaxWidth(font, texts));
        this.setHeight(font.lineHeight * texts.size());
        switch (anchorX) {
            case LEFT:
                left = x;
                right = x + width;
                break;
            case RIGHT:
                left = x - width;
                right = x;
                break;
            case CENTER:
                left = x - width / 2;
                right = x + width / 2;
                break;
        }
        switch (anchorY) {
            case TOP:
                top = y;
                bottom = y + height;
                break;
            case BOTTOM:
                top = y - height;
                bottom = y;
                break;
            case CENTER:
                top = y - height / 2;
                bottom = y + height / 2;
                break;
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            if (lastX != x || lastY != y) {
                this.recalculateBounds();
                lastX = x;
                lastY = y;
            }

            isHovered = mouseX >= left && mouseY >= top && mouseX < right && mouseY < bottom;

            if (wasHovered != this.isHovered()) {
                if (this.isHovered()) {
                    if (focused) {
                        this.queueNarration(200);
                    } else {
                        this.queueNarration(750);
                    }
                } else {
                    nextNarration = Long.MAX_VALUE;
                }
            }

            if (visible) {
                this.renderButton(poseStack, mouseX, mouseY, partialTicks);
            }

            this.narrate();
            wasHovered = this.isHovered();
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0, count = texts.size(); i < count; i++) {
            final FormattedText text = texts.get(i);
            final int w = font.width(text);
            final int h = font.lineHeight;

            final int x;
            switch (anchorX) {
                case LEFT:
                    x = left;
                    break;
                case RIGHT:
                    x = right - w;
                    break;
                case CENTER:
                    x = this.x - w / 2;
                    break;
                default:
                    x = 0;
                    break;
            }

            final int y;
            switch (anchorY) {
                case TOP:
                    y = top + h * i;
                    break;
                case BOTTOM:
                    y = bottom - h * (count - i);
                    break;
                case CENTER:
                    y = this.y - count * h / 2 + h * i;
                    break;
                default:
                    y = 0;
                    break;
            }

            font.drawShadow(poseStack, Language.getInstance().getVisualOrder(text), x, y, -1);
        }
        if (tooltip != null && this.isHovered()) {
            tooltip.render(this, poseStack, mouseX, mouseY);
        }
    }

    @Override
    public void playDownSound(SoundManager handler) {
        // no-op
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return active && visible && isHovered;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return active && visible && isHovered;
    }

    public void setTexts(FormattedText... texts) {
        this.setTexts(Arrays.asList(texts));
    }

    public void setTexts(List<? extends FormattedText> texts) {
        this.texts = texts;
        this.recalculateBounds();
    }

    public void wrap(int wrapWidth) {
        final List<FormattedText> wrapped = new ArrayList<>(texts.size());
        final StringSplitter splitter = font.getSplitter();
        for (FormattedText text : texts) {
            if (font.width(text) > wrapWidth) {
                wrapped.addAll(splitter.splitLines(text, wrapWidth, Style.EMPTY));
            } else {
                wrapped.add(text);
            }
        }
        this.setTexts(wrapped);
    }

    public enum AnchorX {
        LEFT,
        RIGHT,
        CENTER
    }

    public enum AnchorY {
        TOP,
        BOTTOM,
        CENTER
    }

    public interface Tooltip {
        void render(LabelWidget label, PoseStack poseStack, int mouseX, int mouseY);
    }
}
