package com.til.glowing_fire_glow.client.register.render_type;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public abstract class RenderTypeRegister extends RegisterBasics {

    protected Map<Identifier, RenderLayer> renderTypeMap = new HashMap<>();

    protected abstract RenderLayer makeRenderType(Identifier Identifier);


    public RenderLayer getRenderType(Identifier Identifier) {
        if (renderTypeMap.containsKey(Identifier)) {
            return renderTypeMap.get(Identifier);
        } else {
            RenderLayer renderType = makeRenderType(Identifier);
            renderTypeMap.put(Identifier, renderType);
            return renderType;
        }
    }
}
