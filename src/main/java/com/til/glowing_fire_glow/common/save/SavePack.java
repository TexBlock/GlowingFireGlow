package com.til.glowing_fire_glow.common.save;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.NBTUtil;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.gson.GsonManage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@StaticVoluntarilyAssignment
public class SavePack<E> {

    @VoluntarilyAssignment
    protected static GsonManage gsonManage;


    protected final List<Field> fields;
    protected final Class<E> clazz;


    public SavePack(Class<E> clazz) {
        fields = new ArrayList<>();
        this.clazz = clazz;
        for (Field allField : ReflexUtil.getAllFields(clazz, false)) {
            if (allField.getAnnotation(SaveField.class) == null) {
                continue;
            }
            fields.add(allField);
        }
    }

    public SavePack(List<Field> fields, Class<E> clazz) {
        this.fields = fields;
        this.clazz = clazz;
    }

    public void write(E e, CompoundNBT compoundNBT) {
        for (Field field : fields) {
            field.setAccessible(true);
            Object obj;

            try {
                obj = field.get(e);
            } catch (IllegalAccessException ex) {
                GlowingFireGlow.LOGGER.error(ex);
                continue;
            }
            if (obj instanceof INBT) {
                compoundNBT.put(field.getName(), (INBT) obj);
                continue;
            }
            compoundNBT.put(field.getName(), NBTUtil.toTag(gsonManage.getGson().toJsonTree(obj, field.getGenericType())));
        }
    }

    public void read(E e, CompoundNBT compoundNBT) {
        for (Field field : fields) {
            if (!compoundNBT.contains(field.getName())) {
                continue;
            }
            Object obj;
            if (INBT.class.isAssignableFrom(field.getType())) {
                obj = compoundNBT.get(field.getName());
                if (!field.getType().isInstance(obj)) {
                    obj = null;
                }
            } else {
                obj = gsonManage.getGson().fromJson(NBTUtil.toJson(compoundNBT.get(field.getName()), false), field.getGenericType());
            }
            field.setAccessible(true);
            try {
                field.set(e, obj);
            } catch (IllegalAccessException ex) {
                GlowingFireGlow.LOGGER.error(ex);
                continue;
            }
        }
    }


}
