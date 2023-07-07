package com.til.glowing_fire_glow.common.mixin;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Excluder.class, remap = false)
public class ExcluderMixin {
    @Inject(method = "create",cancellable = true,
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lcom/google/gson/reflect/TypeToken;getRawType()Ljava/lang/Class;",
                    opcode = 1
            ) )
    public <T> void create(Gson gson, TypeToken<T> type, CallbackInfoReturnable<TypeAdapter<T>> cir) {
        Class<?> rawType = type.getRawType();
        if (IForgeRegistryEntry.class.isAssignableFrom(rawType)) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }

}
