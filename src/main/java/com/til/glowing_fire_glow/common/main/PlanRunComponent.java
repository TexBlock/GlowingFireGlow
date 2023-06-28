package com.til.glowing_fire_glow.common.main;

import com.til.glowing_fire_glow.util.Extension;

import java.util.ArrayList;
import java.util.List;

/***
 * 计划运行
 */
public abstract class PlanRunComponent implements IWorldComponent {

    protected List<Extension.VariableData_2<Integer, Runnable>> runList = new ArrayList<>();
    protected List<Extension.VariableData_2<Integer, Runnable>> delayedAdd = new ArrayList<>();

    protected void tick() {
        if (!delayedAdd.isEmpty()) {
            runList.addAll(delayedAdd);
            delayedAdd.clear();
        }
        for (int i = 0; i < runList.size(); i++) {
            Extension.VariableData_2<Integer, Runnable> floatRunnableVariableData_2 = runList.get(i);
            floatRunnableVariableData_2.k--;
            if (floatRunnableVariableData_2.k <= 0) {
                floatRunnableVariableData_2.v.run();
                runList.remove(i);
                i--;
            }
        }
    }

    public void add(int time, Runnable runnable) {
        delayedAdd.add(new Extension.VariableData_2<>(time, runnable));
    }
}
