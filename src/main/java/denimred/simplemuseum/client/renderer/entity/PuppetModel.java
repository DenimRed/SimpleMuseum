package denimred.simplemuseum.client.renderer.entity;

import static denimred.simplemuseum.common.entity.puppet.PuppetEasterEggTracker.Egg.ERROR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

import denimred.simplemuseum.SimpleMuseum;
import denimred.simplemuseum.common.entity.puppet.PuppetEasterEggTracker;
import denimred.simplemuseum.common.entity.puppet.PuppetEntity;
import denimred.simplemuseum.common.entity.puppet.manager.PuppetSourceManager;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.exception.GeckoLibException;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

// I'm not proud of any of these "emergency fallback" fixes :/
public class PuppetModel extends AnimatedTickingGeoModel<PuppetEntity> {
    @Override
    public ResourceLocation getModelLocation(PuppetEntity puppet) {
        return puppet.sourceManager.model.getSafe();
        //        return puppet.easterEggs.isActive(ERROR)
        //                ? PuppetEasterEggTracker.ERROR_MODEL
        //                : puppet.sourceManager.model.getSafe();
    }

    @Override
    public GeoModel getModel(ResourceLocation location) {
        try {
            return super.getModel(location);
        } catch (GeckoLibException e) {
            // Emergency fallback for when we render while resources are reloading
            SimpleMuseum.LOGGER.debug("EMERGENCY FALLBACK: Model '{}'", location);
            return super.getModel(PuppetSourceManager.MODEL.defaultValue);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(PuppetEntity puppet) {
        final ResourceLocation desired =
                puppet.easterEggs.isActive(ERROR)
                        ? PuppetEasterEggTracker.ERROR_TEXTURE
                        : puppet.sourceManager.texture.getSafe();
        final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        if (textureManager.getTexture(desired) != MissingTextureAtlasSprite.getTexture()) {
            return desired;
        } else {
            // Emergency fallback for when we render while resources are reloading
            SimpleMuseum.LOGGER.debug("EMERGENCY FALLBACK: Texture '{}'", desired);
            textureManager.byPath.remove(desired);
            return puppet.sourceManager.texture.getDefault();
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PuppetEntity puppet) {
        return puppet.sourceManager.animations.getSafe();
    }

    @Nullable // Parent isn't nullable, but its uses say otherwise
    @Override
    public Animation getAnimation(String name, IAnimatable animatable) {
        try {
            return super.getAnimation(name, animatable);
        } catch (GeckoLibException e) {
            // Emergency fallback for when we render while resources are reloading
            SimpleMuseum.LOGGER.debug("EMERGENCY FALLBACK: Animation '{}'", name);
            return null;
        }
    }

    @Override
    public void codeAnimations(
            PuppetEntity puppet, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        if (customPredicate != null) {
            final AnimationProcessor<?> processor = this.getAnimationProcessor();
            IBone head = processor.getBone("head");
            if (head == null) {
                head = processor.getBone("Head");
            }
            if (head != null) {
                final EntityModelData extraData =
                        (EntityModelData)
                                customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
                head.setRotationX(
                        head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
                head.setRotationY(
                        head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
            }
        }
    }
}
