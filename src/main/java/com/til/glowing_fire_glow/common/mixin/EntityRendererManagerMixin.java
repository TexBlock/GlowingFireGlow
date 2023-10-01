package com.til.glowing_fire_glow.common.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.client.util.EntityRenderDataCache;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRendererManager.class)
public class EntityRendererManagerMixin {

    protected EntityRenderDataCache entityRenderDataCache;

    @Inject(
            method = "renderEntityStatic",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/matrix/MatrixStack;translate(DDD)V",
                    ordinal = 1
            )
    )
    private <E extends Entity> void renderEntityStatic(E entityIn, double xIn, double yIn, double zIn, float rotationYawIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo ci) {
        try {
            if (entityRenderDataCache == null) {
                entityRenderDataCache = GlowingFireGlow.getInstance().getWorldComponent(EntityRenderDataCache.class);
            }
            entityRenderDataCache.render(entityIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        } catch (Exception ignored) {

        }
        //GlowingFireGlow.getInstance().getWorldComponent(EntityRenderDataCache.class).render(entityIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

}
