package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class TagManage<T> implements IWorldComponent {
    protected Map<Identifier, List<T>> map = new HashMap<>();
    protected TagKey<T> tagManager;

    @SafeVarargs
    public final void addTag(Identifier tTagKey, T... t) {
        List<T> tList;
        if (map.containsKey(tTagKey)) {
            tList = map.get(tTagKey);
        } else {
            tList = new ArrayList<>();
            map.put(tTagKey, tList);
        }
        tList.addAll(Arrays.asList(t));
    }

    public final void addTag(T t, Identifier... tTagKey) {
        for (Identifier tagKey : tTagKey) {
            addTag(tagKey, t);
        }
    }


    public Tag<T> of(Identifier identifier) {
        return tagManager.add(identifier.toString());
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

    protected abstract TagKey<T> initTagManager();


    public TagKey<T> getTagManager() {
        return tagManager;
    }


}
