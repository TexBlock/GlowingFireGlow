package com.til.glowing_fire_glow.common.register.world.fluid;

import com.til.glowing_fire_glow.client.ColorProxyManage;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.tag.BlockTagManage;
import com.til.glowing_fire_glow.common.tag.FluidTagManage;
import com.til.glowing_fire_glow.common.tag.ItemTagManage;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public abstract class FluidRegister extends RegisterBasics {

    protected ResourceLocation fluidName;
    protected ResourceLocation fluidStillBlockName;
    protected ResourceLocation fluidFlowingBlockName;


    protected ForgeFlowingFluid.Properties properties;

    /***
     * 静止的
     */
    protected Fluid still;

    /***
     * 流动的
     */
    protected Fluid flowing;

    @Nullable
    protected Item bucketItem;

    @Nullable
    protected FlowingFluidBlock flowingFluidBlock;

    @VoluntarilyAssignment
    protected ItemTagManage itemTagManage;

    @VoluntarilyAssignment
    protected BlockTagManage blockTagManage;

    @VoluntarilyAssignment
    protected FluidTagManage fluidManage;

    @Override
    protected void init() {
        super.init();
        fluidName = ResourceLocationUtil.fuseName(name.getNamespace(), "/", new String[]{"fluid", name.getPath()});
        fluidStillBlockName = ResourceLocationUtil.fuseName(name.getNamespace(), "/", new String[]{"fluid", "still", name.getPath()});
        fluidFlowingBlockName = ResourceLocationUtil.fuseName(name.getNamespace(), "/", new String[]{"fluid", "flowing", name.getPath()});


        properties = createProperties();

        still = createStillFluid();
        still.setRegistryName(fluidStillBlockName);
        flowing = createFlowingFluid();
        flowing.setRegistryName(fluidFlowingBlockName);
        fluidManage.addTag(fluidName, still, flowing);
        fluidManage.addTag(fluidStillBlockName, still);
        fluidManage.addTag(fluidFlowingBlockName, flowing);
        ForgeRegistries.FLUIDS.register(still);
        ForgeRegistries.FLUIDS.register(flowing);

        flowingFluidBlock = createFlowingFluidBlock();
        if (flowingFluidBlock != null) {
            flowingFluidBlock.setRegistryName(fluidName);
            properties.block(() -> flowingFluidBlock);
            blockTagManage.addTag(flowingFluidBlock, fluidName);
            ForgeRegistries.BLOCKS.register(flowingFluidBlock);
        }

        bucketItem = createBanner();
        if (bucketItem != null) {
            bucketItem.setRegistryName(fluidName);
            properties.bucket(() -> bucketItem);
            itemTagManage.addTag(bucketItem, fluidName);
            ForgeRegistries.ITEMS.register(bucketItem);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void dyeColor(ColorProxyManage.ItemColorPack colorPack) {
    }

    @OnlyIn(Dist.CLIENT)
    public void dyeColor(ColorProxyManage.BlockColorPack colorPack) {
    }

    protected ForgeFlowingFluid.Properties createProperties() {
        return new ForgeFlowingFluid.Properties(() -> still, () -> flowing, createPropertiesBuilder());
    }

    protected abstract FluidAttributes.Builder createPropertiesBuilder();

    protected ForgeFlowingFluid.Flowing createFlowingFluid() {
        return new ForgeFlowingFluid.Flowing(properties);
    }

    protected ForgeFlowingFluid.Source createStillFluid() {
        return new ForgeFlowingFluid.Source(properties);
    }


    @Nullable
    public FlowingFluidBlock createFlowingFluidBlock() {
        return null;
    }

    @Nullable
    public Item createBanner() {
        return null;
    }


    @Deprecated
    public Fluid getStill() {
        return still;
    }

    public Fluid getFlowing() {
        return flowing;
    }

    @Nullable
    public Item getBucketItem() {
        return bucketItem;
    }

    @Nullable
    public FlowingFluidBlock getFlowingFluidBlock() {
        return flowingFluidBlock;
    }
}
