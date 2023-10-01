package com.til.glowing_fire_glow.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
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
    protected ResourceLocation textureName;

    public DefaultParticle(ClientWorld clientLevel) {
        super(clientLevel, 0, 0, 0);
    }


    /***
     * 设置大小
     */
    public DefaultParticle setSize(float size) {
        this.size = size;
        this.setBoundingBox(new Pos(posX, posY, posZ).axisAlignedBB(size));
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
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
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
    public DefaultParticle setPos(double x, double y, double z) {
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        posX = x;
        posY = y;
        posZ = z;
        this.setBoundingBox(new Pos(x, y, z).axisAlignedBB(size));
        return this;
    }

    /***
     * 设置当前颜色
     */
    public DefaultParticle setColor(GlowingFireGlowColor color) {
        setColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        setAlphaF(color.getAlpha() / 255f);
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
        this.canCollide = particleCollide;
        return this;
    }

    /***
     * 设置材质
     */
    public DefaultParticle setTextureName(@Nullable ResourceLocation textureName) {
        this.textureName = textureName;
        return this;
    }


    @Override
    public void tick() {
        age++;
        if (this.age >= this.maxAge) {
            this.setExpired();
            return;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (canCollide) {
            Vector3d vector3d = Entity.collideBoundingBoxHeuristically(
                    null,
                    new Vector3d(motionX, motionY, motionZ),
                    getBoundingBox(),
                    this.world, ISelectionContext.dummy(),
                    new ReuseableStream<>(Stream.empty()));
            posX += vector3d.x;
            posY += vector3d.y;
            posZ += vector3d.z;
        } else {
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
        }

        if (posX != prevPosX || posY != prevPosY || posZ != prevPosZ) {
            setBoundingBox(new Pos(posX, posY, posZ).axisAlignedBB(size));
        }

        //move(motionX, motionY, motionZ);


        if (moveAttenuation != null) {
            motionX *= moveAttenuation.x;
            motionY *= moveAttenuation.y;
            motionZ *= moveAttenuation.z;
        }

        if (particleGravity != 0) {
            this.motionY -= 0.04D * (double) this.particleGravity;
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
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {

        Vector3d vector3d = renderInfo.getProjectedView();
        Vector3f addPos = new Vector3f(
                (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.posX) - vector3d.getX()),
                (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.posY) - vector3d.getY()),
                (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.posZ) - vector3d.getZ()));

        Quaternion quaternion;
        if (this.particleAngle == 0.0F) {
            quaternion = renderInfo.getRotation();
        } else {
            quaternion = new Quaternion(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.oldRoll, this.roll);
            quaternion.multiply(Vector3f.ZP.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};

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
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(currentSize);
            vector3f.add(addPos);
        }

        int combined = 15 << 20 | 15 << 4;

        buffer.pos(avector3f[0].getX(), avector3f[0].getY(), avector3f[0].getZ()).tex(0, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(combined).endVertex();
        buffer.pos(avector3f[1].getX(), avector3f[1].getY(), avector3f[1].getZ()).tex(0, 1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(combined).endVertex();
        buffer.pos(avector3f[2].getX(), avector3f[2].getY(), avector3f[2].getZ()).tex(1, 1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(combined).endVertex();
        buffer.pos(avector3f[3].getX(), avector3f[3].getY(), avector3f[3].getZ()).tex(1, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(combined).endVertex();

    }

    @Override
    protected int getBrightnessForRender(float partialTick) {
        return super.getBrightnessForRender(partialTick);
    }

    public static enum SizeChangeType {
        SIN,
        SQUARE_SIN,
        COS,
        SQUARE_COS,
        SMOOTH
    }

    @Override
    public IParticleRenderType getRenderType() {
        if (textureName == null) {
            return NULL_TEXTURE;
        }
        if (map.containsKey(textureName)) {
            return map.get(textureName);
        } else {
            IParticleRenderType particleRenderType = new IParticleRenderType() {
                @Override
                public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                    RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
                    RenderSystem.disableLighting();
                    textureManager.bindTexture(textureName);
                    Texture tex = textureManager.getTexture(textureName);
                    tex.setBlurMipmap(true, false);
                    bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                }

                @Override
                public void finishRender(Tessellator tesselator) {
                    tesselator.draw();
                    Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).restoreLastBlurMipmap();
                    RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
                    RenderSystem.disableBlend();
                    RenderSystem.depthMask(true);
                }
            };
            map.put(textureName, particleRenderType);
            return particleRenderType;
        }
    }

    public static final Map<ResourceLocation, IParticleRenderType> map = new HashMap<>();

    public static final IParticleRenderType NULL_TEXTURE = new IParticleRenderType() {

        @Override
        public void beginRender(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableTexture();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
        }

        @Override
        public void finishRender(Tessellator tess) {
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
