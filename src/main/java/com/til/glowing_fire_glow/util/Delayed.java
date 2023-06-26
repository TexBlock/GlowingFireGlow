package com.til.glowing_fire_glow.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

/***
 * 延迟获取值
 * 请使用{}创造子类去捕获父类泛型
 * @author til
 */
public abstract class Delayed<E> {
    public Supplier<E> supplier;
    public E e;

    public Delayed(Supplier<E> supplier) {
        this.supplier = supplier;
    }

    public E get() {
        if (e == null) {
            if (supplier != null) {
                e = supplier.get();
            }
            supplier = null;
        }
        return e;
    }

    public static class ItemDelayed extends Delayed<Tag<Item>> {
        public ItemDelayed(Supplier<Tag<Item>> supplier) {
            super(supplier);
        }
    }

    public static class BlockDelayed extends Delayed<Tag<Block>> {
        public BlockDelayed(Supplier<Tag<Block>> supplier) {
            super(supplier);
        }
    }

    public static class FluidDelayed extends Delayed<Tag<Fluid>> {
        public FluidDelayed(Supplier<Tag<Fluid>> supplier) {
            super(supplier);
        }
    }

    public static class ItemStackDelayed extends Delayed<ItemStack> {
        public ItemStackDelayed(Supplier<ItemStack> supplier) {
            super(supplier);
        }
    }

    public static class FluidStackDelayed extends Delayed<FluidStack> {
        public FluidStackDelayed(Supplier<FluidStack> supplier) {
            super(supplier);
        }
    }

    public static class ColorDelayed extends Delayed<GlowingFireGlowColor> {
        public ColorDelayed(Supplier<GlowingFireGlowColor> supplier) {
            super(supplier);
        }
    }

    public static class BlockStateDelayed extends Delayed<BlockState> {
        public BlockStateDelayed(Supplier<BlockState> supplier) {
            super(supplier);
        }
    }

}
