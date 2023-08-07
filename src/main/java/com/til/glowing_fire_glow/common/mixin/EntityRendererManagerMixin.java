package com.til.glowing_fire_glow.common.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.client.register.capability.render.AllCapabilityRenderRegister;
import com.til.glowing_fire_glow.client.register.capability.render.CapabilityRenderRegister;
import com.til.glowing_fire_glow.client.util.EntityRenderDataCache;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityRendererManager.class)
@OnlyIn(Dist.CLIENT)
public class EntityRendererManagerMixin {

    protected static EntityRenderDataCache entityRenderDataCache;

    @Inject(
            method = "renderEntityStatic",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    target = "Lcom/mojang/blaze3d/matrix/MatrixStack;pop()V",
                    opcode = 1
            )
    )
    public <E extends Entity> void renderEntityStatic(E entityIn, double xIn, double yIn, double zIn, float rotationYawIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo ci) {
        if (entityRenderDataCache == null) {
            GlowingFireGlow.getInstance().getWorldComponent(EntityRenderDataCache.class);
        }
        entityRenderDataCache.render(entityIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

}
