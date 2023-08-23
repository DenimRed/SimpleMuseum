package denimred.simplemuseum.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import denimred.simplemuseum.client.renderer.entity.PuppetModel;
import denimred.simplemuseum.common.entity.puppet.PuppetEntity;
import denimred.simplemuseum.common.init.MuseumItems;
import denimred.simplemuseum.common.item.HeldItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class PuppetHeldItemLayerRenderer extends GeoLayerRenderer<PuppetEntity> {

    public PuppetHeldItemLayerRenderer(IGeoRenderer<PuppetEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, PuppetEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        GeoModel model = getEntityModel().getModel(getEntityModel().getModelLocation(entity));
        for(GeoBone bone : model.topLevelBones) {
            renderBoneAndChildren(bone, poseStack, bufferSource, light, entity);
        }
    }

    private void renderBoneAndChildren(GeoBone bone, PoseStack poseStack, MultiBufferSource bufferSource, int light, PuppetEntity puppet) {
        HeldItemStack heldItem = puppet.getHeldItem(bone.name);

        if(heldItem != null) {
            ItemStack itemStack = heldItem.itemStack;

            if (itemStack != null) {
                poseStack.pushPose();

                RenderUtils.moveToPivot(bone, poseStack);
                RenderUtils.rotate(bone, poseStack);
                RenderUtils.scale(bone, poseStack);
                poseStack.translate(0, 0, -0.05);
                poseStack.scale(.75f * heldItem.scale, .75f * heldItem.scale, .75f * heldItem.scale);

                Minecraft.getInstance().getItemInHandRenderer().renderItem(puppet, itemStack, heldItem.armorDisplay ? ItemTransforms.TransformType.HEAD : ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false, poseStack, bufferSource, light);

                poseStack.popPose();
            }
        }
        poseStack.pushPose();

        RenderUtils.translate(bone, poseStack);
        RenderUtils.moveToPivot(bone, poseStack);
        RenderUtils.rotate(bone, poseStack);
        RenderUtils.scale(bone, poseStack);
        RenderUtils.moveBackFromPivot(bone, poseStack);

        for (GeoBone child : bone.childBones)
            renderBoneAndChildren(child, poseStack, bufferSource, light, puppet);

        poseStack.popPose();
    }

}