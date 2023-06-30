package com.til.glowing_fire_glow.common.capability.time_run;

import java.util.ArrayList;
import java.util.List;

public interface ITimeRun {

    void tick();

    void addTimerCell(TimerCell timerCell);

    class TimeRun implements ITimeRun {
        protected final List<TimerCell> runList = new ArrayList<>();
        protected final List<TimerCell> beAdded = new ArrayList<>();
        protected boolean isRun;

        @Override
        public void tick() {
            isRun = true;
            if (!beAdded.isEmpty()) {
                upBeAdded();
            }
            if (runList.isEmpty()) {
                return;
            }
            for (int i = 0; i < runList.size(); i++) {
                TimerCell timerCell = runList.get(i);
                timerCell.up();
                if (!isRun) {
                    return;
                }
                if (!timerCell.isValid()) {
                    runList.remove(i);
                    i--;
                }
            }
        }

        @Override
        public void addTimerCell(TimerCell timerCell) {
            if (!timerCell.isValid()) {
                return;
            }
            beAdded.add(timerCell);
        }

        protected void upBeAdded() {
            for (TimerCell timerCell : beAdded) {
                boolean needInsert = true;
                for (int i = 0; i < runList.size(); i++) {
                    TimerCell timerCell1 = runList.get(i);
                    if (timerCell1.priority > timerCell.priority) {
                        continue;
                    }
                    runList.add(i, timerCell);
                    needInsert = false;
                    break;
                }
                if (needInsert) {
                    runList.add(timerCell);
                }
            }
            beAdded.clear();
        }

    }
}
