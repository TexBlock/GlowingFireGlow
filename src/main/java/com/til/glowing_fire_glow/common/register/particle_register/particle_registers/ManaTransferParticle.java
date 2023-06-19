package com.til.glowing_fire_glow.common.register.particle_register.particle_registers;

import com.til.dusk.client.particle.DefaultParticle;
import com.til.dusk.common.config.ConfigField;
import com.til.dusk.common.event.EventIO;
import com.til.dusk.common.register.particle_register.AllParticleRegister;
import com.til.dusk.common.register.particle_register.ParticleRegister;
import com.til.dusk.main.world_component.ReflexManage;
import com.til.dusk.util.DuskColor;
import com.til.dusk.util.Extension;
import com.til.dusk.util.Pos;
import com.til.dusk.util.RoutePack;
import com.til.dusk.util.prefab.ColorPrefab;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author til
 */
@ReflexManage.VoluntarilyRegister
public class ManaTransferParticle extends ParticleRegister {
    public static final float MANA_THRESHOLD = 320f;
    protected Random random = new Random();

    @SubscribeEvent
    protected void eventMana(EventIO.Mana event) {
        if (event.routePack.isEmpty()) {
            return;
        }
        List<RoutePack<Long>> list = event.routePack.getAll();
        List<List<RoutePack.RouteCell<Double>>> route = new ArrayList<>(list.size());
        for (RoutePack<Long> longRoutePack : list) {
            List<RoutePack.RouteCell<Double>> cells = new ArrayList<>();
            for (RoutePack.RouteCell<Long> longRouteCell : longRoutePack.routeCellList) {
                if (random.nextDouble() < longRouteCell.data() / MANA_THRESHOLD) {
                    cells.add(new RoutePack.RouteCell<>(longRouteCell.start(), longRouteCell.end(), 1d));
                } else {
                    cells.add(new RoutePack.RouteCell<>(longRouteCell.start(), longRouteCell.end(), 0d));
                }
            }
            if (!cells.isEmpty()) {
                route.add(cells);
            }
        }
        this.add(event.level, route, ColorPrefab.MANA_IO, null);
    }

    @Override
    public Extension.Data_2<Float, List<Particle>> run(ClientLevel world, Pos start, Pos end, DuskColor color, double density, @Nullable ResourceLocation resourceLocation) {
        if (density <= 0) {
            return new Extension.Data_2<>(start.distance(end) * speed, null);
        }
        Pos _end = end.move(Pos.randomPos());
        int dis = (int) (start.distance(_end) * speed);
        new DefaultParticle(world, start, AllParticleRegister.DEFAULT, color, Pos.movePos(start, _end, dis), size, dis);
        return new Extension.Data_2<>(start.distance(end) * 3, List.of(new DefaultParticle(world, start, AllParticleRegister.DEFAULT, color, Pos.movePos(start, _end, dis), size, dis)));
    }

    @Override
    public void defaultConfig() {  super.defaultConfig();
        speed = 3f;
        size = 0.25f;
    }

    @ConfigField
    public float speed;

    @ConfigField
    public float size;
}
