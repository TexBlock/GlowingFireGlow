package com.til.glowing_fire_glow.common.register.particle_register.data;

import net.minecraft.client.particle.Particle;

import java.util.ArrayList;
import java.util.List;

/***
 *  用来搜集粒子实例 
 */
public class ParticleContext {

    protected List<Particle> particles = new ArrayList<>();
    protected int particleTime;
    /***
     * 自动获取时间
     */
    protected boolean automaticTime = true;

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public void setParticleTime(int time) {
        particleTime = Math.max(time, particleTime);
        automaticTime = false;
    }

    public Iterable<Particle> forParticle() {
        return particles;
    }

    public int getParticleTime() {
        if (automaticTime) {
            for (Particle particle : particles) {
                particleTime = Math.max(particleTime, particle.getMaxAge());
            }
        }
        return particleTime;
    }


}
