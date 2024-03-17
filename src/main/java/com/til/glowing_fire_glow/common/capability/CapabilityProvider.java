package com.til.glowing_fire_glow.common.capability;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/***
 * 能力包装
 */
public class CapabilityProvider implements ICapabilityProvider, INBTSerializable<NbtCompound> {

    public final Map<Capability<?>, LazyOptional<?>> capabilityLazyOptionalMap = new HashMap<>();

    public <E> void addCapability(Capability<E> capability, E e) {
        if (capabilityLazyOptionalMap.containsKey(capability)) {
            GlowingFireGlow.LOGGER.warn("重复的{}能力注入", capability);
            return;
        }
        LazyOptional<E> lazyOptional = LazyOptional.of(() -> Util.forcedConversion(e));
        capabilityLazyOptionalMap.put(capability, lazyOptional);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!capabilityLazyOptionalMap.containsKey(cap)) {
            return LazyOptional.empty();
        }
        return capabilityLazyOptionalMap.get(cap).cast();
    }

    @Override
    public NbtCompound serializeNBT() {
        NbtCompound NbtCompound = new NbtCompound();
        for (Map.Entry<Capability<?>, LazyOptional<?>> capabilityLazyOptionalEntry : capabilityLazyOptionalMap.entrySet()) {
            if (!capabilityLazyOptionalEntry.getValue().isPresent()) {
                continue;
            }
            NbtElement inbt;
            Object obj = capabilityLazyOptionalEntry.getValue().orElse(null);
            if (obj instanceof INBTSerializable) {
                INBTSerializable<?> inbtSerializable = ((INBTSerializable<?>) obj);
                inbt = inbtSerializable.serializeNBT();
            } else {
                inbt = capabilityLazyOptionalEntry.getKey().writeNBT(Util.forcedConversion(obj), null);
            }
            NbtCompound.put(capabilityLazyOptionalEntry.getKey().getName(), inbt);
        }
        return NbtCompound;
    }

    @Override
    public void deserializeNBT(NbtCompound nbt) {
        for (Map.Entry<Capability<?>, LazyOptional<?>> capabilityLazyOptionalEntry : capabilityLazyOptionalMap.entrySet()) {
            if (!capabilityLazyOptionalEntry.getValue().isPresent()) {
                continue;
            }
            if (!nbt.contains(capabilityLazyOptionalEntry.getKey().getName())) {
                continue;
            }
            Object obj = capabilityLazyOptionalEntry.getValue().orElse(null);
            NbtElement inbt = nbt.get(capabilityLazyOptionalEntry.getKey().getName());
            if (obj instanceof INBTSerializable) {
                INBTSerializable<?> inbtSerializable = ((INBTSerializable<?>) obj);
                inbtSerializable.deserializeNBT(Util.forcedConversion(inbt));
            } else {
                capabilityLazyOptionalEntry.getKey().readNBT(Util.forcedConversion(obj), null, inbt);
            }
        }
    }
}
