package com.til.glowing_fire_glow.common.register.villages.villager_profession;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class VillagerProfessionRegister extends RegisterBasics {

    protected VillagerProfession villagerProfession;

    @Override
    protected void init() {
        super.init();
        villagerProfession = initVillagerProfession();
        villagerProfession.setRegistryName(getName());
        ForgeRegistries.PROFESSIONS.register(villagerProfession);
    }

    protected abstract VillagerProfession initVillagerProfession();

    public VillagerProfession getVillagerProfession() {
        return villagerProfession;
    }
}
