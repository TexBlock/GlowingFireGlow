package com.til.glowing_fire_glow.client.register.particle_register;

import com.til.glowing_fire_glow.common.register.RegisterManage;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AllParticleClientRegister extends RegisterManage<ParticleClientRegister<?>> {

    protected Map<ParticleRegister, ParticleClientRegister<?>> relationshipMap;

    @Override
    public void init(InitType initType) {
        super.init(initType);
        switch (initType) {

            case NEW:
                break;
            case FML_COMMON_SETUP:
                break;
            case FML_DEDICATED_SERVER_SETUP:
                break;
            case FML_CLIENT_SETUP:
                relationshipMap = new HashMap<>(registerMap.size());
                for (ParticleClientRegister<?> particleClientRegister : forAll()) {
                    relationshipMap.put(particleClientRegister.getParticleRegister(), particleClientRegister);
                }

                break;
        }
    }

    @Nullable
    public ParticleClientRegister<?> relationship(ParticleRegister particleRegister) {
        return relationshipMap.get(particleRegister);
    }
}
