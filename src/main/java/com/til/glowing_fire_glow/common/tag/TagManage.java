package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagRegistry;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public abstract class TagManage<T> implements IWorldComponent {
    protected Map<ResourceLocation, List<T>> map = new HashMap<>();
    protected TagRegistry<T> tagManager;

    @SafeVarargs
    public final void addTag(ResourceLocation tTagKey, T... t) {
        List<T> tList;
        if (map.containsKey(tTagKey)) {
            tList = map.get(tTagKey);
        } else {
            tList = new ArrayList<>();
            map.put(tTagKey, tList);
        }
        tList.addAll(Arrays.asList(t));
    }

    public final void addTag(T t, ResourceLocation... tTagKey) {
        for (ResourceLocation tagKey : tTagKey) {
            addTag(tagKey, t);
        }
    }


    public ITag<T> of(ResourceLocation resourceLocation) {
        return tagManager.createTag(resourceLocation.toString());
    }


    @Override
    public void initNew() {
        IWorldComponent.super.initNew();
        tagManager = initTagManager();
    }

    @Override
    public int getExecutionOrderList() {
        return 100;
    }

    protected abstract TagRegistry<T> initTagManager();


    public TagRegistry<T> getTagManager() {
        return tagManager;
    }


}
