package com.til.glowing_fire_glow.common.capability.time_run;

public class TimerCell {
    /***
     *  计时结束的回调
     */
    protected Runnable run;

    /***
     *  每个周期需要的时间
     */
    public final int timer;

    /***
     * 是不是周期
     */
    public final boolean cycle;

    /***
     * 当前的计时
     */
    protected int time;

    /***
     * 是启用的
     */
    protected boolean _use = true;

    /***
     * 是不是有效值
     */
    protected boolean valid = true;

    /***
     *  优先级
     */
    public int priority;

    public TimerCell(Runnable run, int time, int priority) {
        this(run, time, false, priority);
    }

    public TimerCell(Runnable run, int timer, boolean cycle, int priority) {
        this.run = run;
        this.timer = timer;
        this.cycle = cycle;
        this.priority = priority;
    }

    public void up() {
        if (!_use) {
            return;
        }
        time++;
        if (time >= timer) {
            time = 0;
            run.run();
            if (!cycle) {
                valid = false;
            }
        }
    }

    public void use(boolean nowStart) {
        _use = true;
        time = 0;
        if (nowStart) {
            time = timer;
        }
    }

    public void end() {
        _use = false;
    }

    public void setFail() {
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }
}
