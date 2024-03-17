package com.til.glowing_fire_glow.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.ReusableStream;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author til
 */
@OnlyIn(Dist.CLIENT)
public class DefaultParticle extends Particle {


    /***
     * 一般生命的标记
     */
    protected float particleHalfAge;

    /***
     * 力的衰减
     */
    @Nullable
    protected Pos moveAttenuation;

    /***
     * 重力
     */
    protected float particleGravity;

    /***
     * 粒子大小
     */
    protected float size = 1;

    /***
     * 粒子变化类型
     */
    protected SizeChangeType sizeChangeType;

    /***
     * 当前粒子大小
     */
    @Deprecated
    protected float currentSize;

    /***
     * 粒子的旋转
     */
    protected float roll;

    /***
     * 上一刻的旋转
     */
    protected float oldRoll;

    /***
     * 旋转速度
     */
    protected float rollSeep;

    /***
     * 材质名称
     */
    @Nullable
    protected Identifier textureName;

    public DefaultParticle(ClientWorld clientLevel) {
        super(clientLevel, 0, 0, 0);
    }


    /***
     * 设置大小
     */
    public DefaultParticle setSize(float size) {
        this.size = size;
        this.setBoundingBox(new Pos(x, y, z).axisAlignedBB(size));
        return this;
    }

    /***
     * 设置最大生命
     */
    public DefaultParticle setLifeTime(int lifetime) {
        this.setMaxAge(lifetime);
        particleHalfAge = lifetime * 0.5f;
        return this;
    }

    /***
     * 设置运动
     */
    public DefaultParticle setMove(double motionX, double motionY, double motionZ) {
        this.velocityX = motionX;
        this.velocityY = motionY;
        this.velocityZ = motionZ;
        return this;
    }

    /***
     * 设置移动的衰减
     */
    public DefaultParticle setMoveAttenuation(Pos moveAttenuation) {
        this.moveAttenuation = moveAttenuation;
        return this;
    }

    /***
     * 设置当前位置
     */
    public DefaultParticle set(double posX, double posY, double posZ) {
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        x = posX;
        y = posY;
        z = posZ;
        this.setBoundingBox(new Pos(posX, posY, posZ).axisAlignedBB(size));
        return this;
    }

    /***
     * 设置当前颜色
     */
    public DefaultParticle setColor(GlowingFireGlowColor color) {
        setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        setColorAlpha(color.getAlpha() / 255f);
        return this;
    }

    /***
     * 设置重力
     */
    public DefaultParticle setParticleGravity(float particleGravity) {
        this.particleGravity = particleGravity;
        return this;
    }

    public DefaultParticle setSizeChangeType(SizeChangeType sizeChangeType) {
        this.sizeChangeType = sizeChangeType;
        return this;
    }

    public DefaultParticle setParticleCollide(boolean particleCollide) {
        this.collidesWithWorld = particleCollide;
        return this;
    }

    /***
     * 设置材质
     */
    public DefaultParticle setTextureName(@Nullable Identifier textureName) {
        this.textureName = textureName;
        return this;
    }


    @Override
    public void tick() {
        age++;
        if (this.age >= this.maxAge) {
            this.markDead();
            return;
        }

        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;

        if (collidesWithWorld) {
            Vec3d vector3d = Entity.adjustMovementForCollisions(
                    null,
                    new Vec3d(velocityX, velocityY, velocityZ),
                    getBoundingBox(),
                    this.world, ShapeContext.absent(),
                    new ReusableStream<>(Stream.empty()));
            x += vector3d.x;
            y += vector3d.y;
            z += vector3d.z;
        } else {
            x += velocityX;
            y += velocityY;
            z += velocityZ;
        }

        if (x != prevPosX || y != prevPosY || z != prevPosZ) {
            setBoundingBox(new Pos(x, y, z).axisAlignedBB(size));
        }

        //move(motionX, motionY, motionZ);


        if (moveAttenuation != null) {
            velocityX *= moveAttenuation.x;
            velocityY *= moveAttenuation.y;
            velocityZ *= moveAttenuation.z;
        }

        if (particleGravity != 0) {
            this.velocityY -= 0.04D * (double) this.particleGravity;
        }

        oldRoll = roll;
        roll += rollSeep;

        /*if (sizeChange) {
            if (age < particleHalfAge) {
                currentSize = MathUtil.lerp(age / particleHalfAge, size, 0);
            } else {
                currentSize = size - MathUtil.lerp((age - particleHalfAge) / particleHalfAge, size, 0);
            }
        }*/
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {

        Vec3d vector3d = renderInfo.getPos();
        Vec3f addPos = new Vec3f(
                (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.x) - vector3d.getX()),
                (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.y) - vector3d.getY()),
                (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.z) - vector3d.getZ()));

        Quaternion quaternion;
        if (this.angle == 0.0F) {
            quaternion = renderInfo.getRotation();
        } else {
            quaternion = new Quaternion(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.oldRoll, this.roll);
            quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(f3));
        }

        Vec3f vector3f1 = new Vec3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vec3f[] avector3f = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};

        float currentSize = size;
        if (sizeChangeType != null) {
            float timeLife = age / particleHalfAge;
            timeLife = timeLife > 1 ? -timeLife + 2 : timeLife;
            switch (sizeChangeType) {
                case SIN:
                    currentSize = (float) (size * Math.sin(timeLife));
                    break;
                case SQUARE_SIN:
                    currentSize = (float) (size * Math.sin(Math.sqrt(timeLife)));
                    break;
                case COS:
                    currentSize = (float) (size * Math.cos(timeLife));
                    break;
                case SQUARE_COS:
                    currentSize = (float) (size * Math.cos(Math.sqrt(timeLife)));
                    break;
                case SMOOTH:
                    currentSize = size * timeLife;
                    break;
            }
        }

        for (int i = 0; i < 4; ++i) {
            Vec3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.scale(currentSize);
            vector3f.add(addPos);
        }

        int combined = 15 << 20 | 15 << 4;

        buffer.vertex(avector3f[0].getX(), avector3f[0].getY(), avector3f[0].getZ()).texture(0, 0).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(combined).next();
        buffer.vertex(avector3f[1].getX(), avector3f[1].getY(), avector3f[1].getZ()).texture(0, 1).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(combined).next();
        buffer.vertex(avector3f[2].getX(), avector3f[2].getY(), avector3f[2].getZ()).texture(1, 1).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(combined).next();
        buffer.vertex(avector3f[3].getX(), avector3f[3].getY(), avector3f[3].getZ()).texture(1, 0).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(combined).next();

    }

    @Override
    protected int getBrightness(float partialTick) {
        return super.getBrightness(partialTick);
    }

    public static enum SizeChangeType {
        SIN,
        SQUARE_SIN,
        COS,
        SQUARE_COS,
        SMOOTH
    }

    @Override
    public ParticleTextureSheet getType() {
        if (textureName == null) {
            return NULL_TEXTURE;
        }
        if (map.containsKey(textureName)) {
            return map.get(textureName);
        } else {
            ParticleTextureSheet particleRenderType = new ParticleTextureSheet() {
                @Override
                public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                    RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
                    RenderSystem.disableLighting();
                    textureManager.bindTexture(textureName);
                    AbstractTexture tex = textureManager.getTexture(textureName);
                    tex.setBlurMipmap(true, false);
                    bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                }

                @Override
                public void draw(Tessellator tesselator) {
                    tesselator.draw();
                    MinecraftClient.getInstance().textureManager.getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE).restoreLastBlurMipmap();
                    RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
                    RenderSystem.disableBlend();
                    RenderSystem.depthMask(true);
                }
            };
            map.put(textureName, particleRenderType);
            return particleRenderType;
        }
    }

    public static final Map<Identifier, ParticleTextureSheet> map = new HashMap<>();

    public static final ParticleTextureSheet NULL_TEXTURE = new ParticleTextureSheet() {

        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableTexture();
            buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
        }

        @Override
        public void draw(Tessellator tess) {
            tess.draw();
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        @Override
        public String toString() {
            return GlowingFireGlow.MOD_ID + ":" + "null_texture";
        }
    };
}
