package denimred.simplemuseum.client.gui.screen.behavior;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import denimred.simplemuseum.client.gui.screen.PuppetConfigScreen;
import denimred.simplemuseum.client.gui.screen.SelectScreen;
import denimred.simplemuseum.client.gui.widget.BetterButton;
import denimred.simplemuseum.client.gui.widget.LabelWidget;
import denimred.simplemuseum.client.gui.widget.WidgetList;
import denimred.simplemuseum.common.entity.puppet.goals.MovePuppetGoal;
import denimred.simplemuseum.common.entity.puppet.goals.PuppetGoal;
import denimred.simplemuseum.common.entity.puppet.goals.PuppetGoalTree;
import denimred.simplemuseum.common.entity.puppet.manager.value.PuppetValue;

public class PuppetGoalEditorScreen extends Screen {

    private final PuppetConfigScreen parent;
    private final PuppetValue<PuppetGoalTree, ?> valueRef;
    private final PuppetGoalTree tree;

    private PuppetGoalList goalList;
    private WidgetList<PuppetGoalEditorScreen> editor;

    public PuppetGoalEditorScreen(PuppetConfigScreen parent, PuppetValue<PuppetGoalTree, ?> valueRef) {
        super(new TextComponent("AI Goal Editor"));
        this.parent = parent;
        this.valueRef = valueRef;
        this.tree = valueRef.get();
    }

    @Override
    protected void init() {
        buttons.clear();
        children.clear();

        goalList = addWidget(new PuppetGoalList(minecraft, 25, height - 25, width / 2));
        int editorX = (width / 2) + 5;
        editor = addWidget(new WidgetList<>(this, editorX, 25, (width - 20) - editorX, height - 50));

        addButton(new BetterButton(20, height - 25, 20, 20, new TextComponent("+"), btn -> createGoal()));

        populateList();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        drawCenteredString(
                poseStack,
                font,
                title.plainCopy().withStyle(ChatFormatting.UNDERLINE),
                width / 2,
                10,
                0xFFFFFF);
        //Mockup UI
        //Right - Goal Editor
        int editorX = (width / 2) + 5;
        fill(poseStack, editorX, 25, width - 20, height - 25, 0x55FFFFFF); //bg1 - Outline
        fill(poseStack, editorX + 1, 26, width - 21, height - 26, 0xc0101010); //bg2
        //Goal Editor Properties based on Goal Type
        fill(poseStack, editorX + 5, height - 50, editorX + 100, height - 30, 0x55999999); //save goal
        fill(poseStack, width - 110, height - 50, width - 25, height - 30, 0x55999999); //delete goal

        goalList.render(poseStack, mouseX, mouseY, partialTicks);
        editor.render(poseStack, mouseX, mouseY, partialTicks);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    public void populateList() {
        goalList.clear();
        for(int i = 0; i < tree.getGoalList().size(); i++)
            goalList.addGoal(i+1, tree.getGoalList().get(i));
    }

    public void populateEditor(PuppetGoal goal) {
        editor.clear();
        if(goal instanceof MovePuppetGoal) {
            editor.add(new LabelWidget(0,0, font, LabelWidget.AnchorX.LEFT, LabelWidget.AnchorY.TOP, FormattedText.of("Move Goal")));
            editor.add(new BetterButton(0,0,0,20, new TextComponent("Edit Movement"), btn -> minecraft.setScreen(new PuppetMovementSelectScreen(this, (MovePuppetGoal) goal))));

        }
    }

    private void createGoal() {
        PuppetGoal newGoal = new MovePuppetGoal();
        tree.getGoalList().add(newGoal);
        goalList.addGoal(tree.getGoalList().size(), newGoal);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
        //ToDo Remove this once finished testing non-sync
        tree.getGoalList().clear();
    }

    class PuppetGoalList extends ObjectSelectionList<PuppetGoalList.Entry> {
        int listWidth;

        public PuppetGoalList(Minecraft minecraft, int top, int bottom, int width) {
            super(minecraft, width, bottom - top, top, bottom, 20);
            setRenderBackground(false);
            setRenderTopAndBottom(false);
            this.listWidth = width;
        }

        public void addGoal(int priority, PuppetGoal goal) {
            addEntry(new Entry(priority, goal));
        }

        public void clear() {
            clearEntries();
        }

        public class Entry extends ObjectSelectionList.Entry<PuppetGoalList.Entry> {
            private final int priority;
            private final PuppetGoal goal;

            public Entry(int priority, PuppetGoal goal) {
                this.priority = priority;
                this.goal = goal;
            }

            @Override
            public void render(PoseStack poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                fill(poseStack, left, top, left + width - 4, top + height, isSelectedItem(index) ? 0xFF00FF00 : index % 2 == 0 ? 0xFFFFFFFF : 0xFF777777);
                drawString(poseStack, font, ""+priority, left, top + (height / 2), 0xFFc1c1c1);
                drawString(poseStack, font, "M", left + (width / 2), top, goal.getFlags().contains(Goal.Flag.MOVE) ? 0x7777FF : 0x55FFFFFF);
                drawString(poseStack, font, "L", left + (width / 2) + 6, top, goal.getFlags().contains(Goal.Flag.LOOK) ? 0x7777FF : 0x55FFFFFF);
                drawString(poseStack, font, "J", left + (width / 2) + 12, top, goal.getFlags().contains(Goal.Flag.JUMP) ? 0x7777FF : 0x55FFFFFF);
                drawString(poseStack, font, "T", left + (width / 2) + 18, top, goal.getFlags().contains(Goal.Flag.TARGET) ? 0x7777FF : 0x55FFFFFF);
            }

            @Override
            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
                PuppetGoalList.this.setSelected(this);
                PuppetGoalEditorScreen.this.populateEditor(goal);
                return false;
            }
        }
    }

    class PuppetGoalSelectScreen extends SelectScreen<GoalType> {
        protected PuppetGoalSelectScreen(Screen parent, Component title) {
            super(parent, title);
        }

        @Override
        protected void onSave() {

        }

        @Override
        protected boolean isSelected(SelectScreen<GoalType>.ListWidget.Entry entry) {
            return false;
        }

        @Override
        protected CompletableFuture<List<GoalType>> getEntriesAsync() {
            return null;
        }
    }

    enum GoalType {
        Movement
    }

}
