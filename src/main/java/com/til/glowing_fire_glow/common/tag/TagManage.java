package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import net.minecraft.tag.Tag;
import net.minecraft.tag.RequiredTagList;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class TagManage<T> implements IWorldComponent {
    protected Map<Identifier, List<T>> map = new HashMap<>();
    protected RequiredTagList<T> tagManager;

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


    public Tag<T> of(Identifier Identifier) {
        return tagManager.add(Identifier.toString());
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

    protected abstract RequiredTagList<T> initTagManager();


    public RequiredTagList<T> getTagManager() {
        return tagManager;
    }


}
