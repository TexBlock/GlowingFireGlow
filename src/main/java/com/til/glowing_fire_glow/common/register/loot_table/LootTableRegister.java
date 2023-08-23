package com.til.glowing_fire_glow.common.register.loot_table;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.loot.LootTable;

public abstract class LootTableRegister extends RegisterBasics {

    @ConfigField
    protected LootTable lootTable;

    protected abstract LootTable mackLootTable();

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        lootTable = mackLootTable();
    }

    public LootTable getLootTable() {
        return lootTable;
    }
}
