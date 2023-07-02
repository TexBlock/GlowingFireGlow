package com.til.glowing_fire_glow.client;


import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.world.block.AllBlockRegister;
import com.til.glowing_fire_glow.common.register.world.block.BlockRegister;
import com.til.glowing_fire_glow.common.register.world.fluid.AllFluidRegister;
import com.til.glowing_fire_glow.common.register.world.fluid.FluidRegister;
import com.til.glowing_fire_glow.common.register.world.item.AllItemRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.util.Extension;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author til
 */
@OnlyIn(Dist.CLIENT)
public class ColorProxyManage implements IWorldComponent {

    protected final Map<Item, ItemColorPack> ITEM_COLOR_PACK_MAP = new HashMap<>();
    protected final Map<Block, BlockColorPack> BLOCK_COLOR_PACK_MAP = new HashMap<>();

    @VoluntarilyAssignment
    protected AllItemRegister allItemRegister;

    @VoluntarilyAssignment
    protected AllBlockRegister allBlockRegister;

    @VoluntarilyAssignment
    protected AllFluidRegister allFluidRegister;

    @Override
    public void registerModEvent(IEventBus eventBus) {
        IWorldComponent.super.registerModEvent(eventBus);
        eventBus.addListener(EventPriority.LOW, this::itemColor);
        eventBus.addListener(EventPriority.LOW, this::blockColor);
    }

    public void itemColor(ColorHandlerEvent.Item event) {


        for (ItemRegister itemRegister : allItemRegister.forAll()) {
            ItemColorPack itemColorPack = new ItemColorPack(itemRegister.getItem());
            itemRegister.dyeColor(itemColorPack);
            ITEM_COLOR_PACK_MAP.put(itemColorPack.itemLike, itemColorPack);
        }
        for (BlockRegister blockRegister : allBlockRegister.forAll()) {
            ItemColorPack itemColorPack = new ItemColorPack(blockRegister.getBlockItem());
            blockRegister.dyeColor(itemColorPack);
            ITEM_COLOR_PACK_MAP.put(itemColorPack.itemLike, itemColorPack);
        }
        for (FluidRegister fluidRegister : allFluidRegister.forAll()) {
            if (fluidRegister.getBucketItem() == null) {
                continue;
            }
            ItemColorPack itemColorPack = new ItemColorPack(fluidRegister.getBucketItem());
            fluidRegister.dyeColor(itemColorPack);
            ITEM_COLOR_PACK_MAP.put(itemColorPack.itemLike.asItem(), itemColorPack);
        }

        for (BlockRegister blockRegister : allBlockRegister.forAll()) {
            BlockColorPack blockColorPack = new BlockColorPack(blockRegister.getBlock());
            blockRegister.dyeColor(blockColorPack);
            BLOCK_COLOR_PACK_MAP.put(blockColorPack.block, blockColorPack);
        }
        for (FluidRegister fluidRegister : allFluidRegister.forAll()) {
            if (fluidRegister.getFlowingFluidBlock() == null) {
                continue;
            }
            BlockColorPack blockColorPack = new BlockColorPack(fluidRegister.getFlowingFluidBlock());
            fluidRegister.dyeColor(blockColorPack);
            BLOCK_COLOR_PACK_MAP.put(blockColorPack.block, blockColorPack);
        }

        for (Map.Entry<Item, ItemColorPack> itemItemColorPackEntry : ITEM_COLOR_PACK_MAP.entrySet()) {
            event.getItemColors().register(itemItemColorPackEntry.getValue(), itemItemColorPackEntry.getKey());
        }
        for (Map.Entry<Block, BlockColorPack> blockBlockColorPackEntry : BLOCK_COLOR_PACK_MAP.entrySet()) {
            event.getBlockColors().register(blockBlockColorPackEntry.getValue(), blockBlockColorPackEntry.getKey());
        }
    }

    public void blockColor(ColorHandlerEvent.Block event) {

    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColorPack implements IItemColor {
        public final Item itemLike;
        public final Map<Integer, Extension.Func_1I<ItemStack, GlowingFireGlowColor>> layerColor = new HashMap<>();

        public ItemColorPack(Item itemLike) {
            this.itemLike = itemLike;
        }

        public ItemColorPack addColor(int layer, Extension.Func_1I<ItemStack, GlowingFireGlowColor> color) {
            layerColor.put(layer, color);
            return this;
        }

        @Override
        public int getColor(@NotNull ItemStack itemStack, int layer) {
            if (layerColor.containsKey(layer)) {
                return layerColor.get(layer).func(itemStack).getRGB();
            }
            return -1;
        }

    }

    @OnlyIn(Dist.CLIENT)
    public static class BlockColorPack implements IBlockColor {
        public final Block block;
        public final Map<Integer, Extension.Func_3I<BlockState, IBlockDisplayReader, BlockPos, GlowingFireGlowColor>> layerColor = new HashMap<>();

        public BlockColorPack(Block block) {
            this.block = block;
        }

        public BlockColorPack addColor(int layer, Extension.Func_3I<BlockState, IBlockDisplayReader, BlockPos, GlowingFireGlowColor> color) {
            layerColor.put(layer, color);
            return this;
        }

        @Override
        public int getColor(@NotNull BlockState blockState, @Nullable IBlockDisplayReader blockAndTintGetter, @Nullable BlockPos blockPos, int layer) {
            if (layerColor.containsKey(layer)) {
                return layerColor.get(layer).func(blockState, blockAndTintGetter, blockPos).getRGB();
            }
            return -1;
        }
    }
}
