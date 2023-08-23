package com.til.glowing_fire_glow.client.register.render_type;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public abstract class RenderTypeRegister extends RegisterBasics {

    protected Map<ResourceLocation, RenderType> renderTypeMap = new HashMap<>();

    protected abstract RenderType makeRenderType(ResourceLocation resourceLocation);


    public RenderType getRenderType(ResourceLocation resourceLocation) {
        if (renderTypeMap.containsKey(resourceLocation)) {
            return renderTypeMap.get(resourceLocation);
        } else {
            RenderType renderType = makeRenderType(resourceLocation);
            renderTypeMap.put(resourceLocation, renderType);
            return renderType;
        }
    }
}
