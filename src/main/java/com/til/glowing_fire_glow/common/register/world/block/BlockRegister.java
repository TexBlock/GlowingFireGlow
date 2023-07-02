package com.til.glowing_fire_glow.common.register.world.block;

import com.til.glowing_fire_glow.client.ColorProxyManage;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.tag.BlockTagManage;
import com.til.glowing_fire_glow.common.tag.ItemTagManage;
import com.til.glowing_fire_glow.util.ResourceLocationUtil;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class BlockRegister extends RegisterBasics {
    protected ResourceLocation blockName;
    protected Block block;
    protected BlockItem blockItem;

    @VoluntarilyAssignment
    protected ItemTagManage itemTagManage;

    @VoluntarilyAssignment
    protected BlockTagManage blockTagManage;


    @Override
    public void init() {
        super.init();
        blockName = ResourceLocationUtil.fuseName(this.getName().getNamespace(), "/", new String[]{"block", this.getName().getPath()});
        block = createBlock();
        block.setRegistryName(blockName);
        ForgeRegistries.BLOCKS.register(block);
        blockTagManage.addTag(blockName, block);
        blockItem = createBlockItem();
        blockItem.setRegistryName(blockName);
        itemTagManage.addTag(blockName, blockItem);
        ForgeRegistries.ITEMS.register(blockItem);
    }


    protected abstract Block createBlock();

    protected abstract BlockItem createBlockItem();

    @OnlyIn(Dist.CLIENT)
    public void dyeColor(ColorProxyManage.ItemColorPack colorPack) {
    }

    @OnlyIn(Dist.CLIENT)
    public void dyeColor(ColorProxyManage.BlockColorPack colorPack) {
    }


    public ResourceLocation getBlockName() {
        return blockName;
    }

    public Block getBlock() {
        return block;
    }


    public BlockItem getBlockItem() {
        return blockItem;
    }


    @Override
    public void defaultConfig() {
        super.defaultConfig();
    }


}
