package com.til.glowing_fire_glow.client.particle;

import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.Pos;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.*;

@OnlyIn(Dist.CLIENT)
@StaticVoluntarilyAssignment
public class LightningParticle extends Particle {
    protected static final int fadetime = 20;
    protected final int expandTime;
    protected int colorOuter;
    protected int colorInner;

    protected final List<FXLightningSegment> segments;
    protected final int segmentCount;

    @VoluntarilyAssignment
    protected static LightningHandler lightningHandler;

    public LightningParticle(ClientWorld world, Pos sourcevec, Pos targetvec, float speed) {
        super(world, sourcevec.x, sourcevec.y, sourcevec.z);
        double length = targetvec.subtract(sourcevec).mag();
        maxAge = lightningHandler.fadeTime + world.random.nextInt(lightningHandler.fadeTime) - lightningHandler.fadeTime / 2;
        expandTime = (int) (length * speed);
        age = -(int) (length * speed);

        LightningSegmentGenerator gen = new LightningSegmentGenerator();
        Pair<Integer, List<FXLightningSegment>> res = gen.compute(sourcevec, targetvec, length);
        segmentCount = res.getFirst();
        segments = res.getSecond();
    }

    public LightningParticle setColorOuter(int colorOuter) {
        this.colorOuter = colorOuter;
        return this;
    }

    public LightningParticle setColorInner(int colorInner) {
        this.colorInner = colorInner;
        return this;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera camera, float partialTicks) {
        // todo fix this >.>

        // old way (bad position and too thick)
        // LightningHandler.queuedLightningBolts.offer(this);

        // new way (right position but heavy artifacting)
		/*
		Vector3d cameraPos = info.getProjectedView();
		MatrixStack ms = new MatrixStack();
		ms.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());
		renderBolt(ms, buffer, 0, false);
		renderBolt(ms, buffer, 1, true);
		*/

        lightningHandler.queuedLightningBolts.offer(this);
    }

    @Nonnull
    @Override
    public ParticleTextureSheet getType() {
        return LAYER;
    }

    public void renderBolt(MatrixStack ms, VertexConsumer wr, int pass, boolean inner) {
        Matrix4f mat = ms.peek().getPositionMatrix();

        float boltAge = age < 0 ? 0 : (float) age / (float) maxAge;
        float mainAlpha;
        if (pass == 0) {
            mainAlpha = (1 - boltAge) * 0.4F;
        } else {
            mainAlpha = 1 - boltAge * 0.5F;
        }

        int renderstart = (int) ((expandTime / 2 - maxAge + age) / (float) (expandTime / 2) * segmentCount);
        int renderend = (int) ((age + expandTime) / (float) expandTime * segmentCount);

        for (FXLightningSegment rendersegment : segments) {
            if (rendersegment.segmentNo < renderstart || rendersegment.segmentNo > renderend) {
                continue;
            }

            Pos playerVec = getRelativeViewVector(rendersegment.startPoint.point).multiply(-1);

            double width = 0.025F * (playerVec.mag() / 5 + 1) * (1 + rendersegment.light) * 0.5F;

            Pos diff1 = playerVec.crossProduct(rendersegment.prevDiff).normalize().multiply(width / rendersegment.sinPrev);
            Pos diff2 = playerVec.crossProduct(rendersegment.nextDiff).normalize().multiply(width / rendersegment.sinNext);

            Pos startvec = rendersegment.startPoint.point;
            Pos endvec = rendersegment.endPoint.point;

            int color = inner ? colorInner : colorOuter;
            int r = (color & 0xFF0000) >> 16;
            int g = (color & 0xFF00) >> 8;
            int b = color & 0xFF;
            int a = (int) (mainAlpha * rendersegment.light * 0xFF);
            int fullbright = 0xF000F0;

            vertex(endvec.subtract(diff2), mat, wr);
            wr.color(r, g, b, a).texture(0.5F, 0).light(fullbright).next();
            vertex(startvec.subtract(diff1), mat, wr);
            wr.color(r, g, b, a).texture(0.5F, 0).light(fullbright).next();
            vertex(startvec.add(diff1), mat, wr);
            wr.color(r, g, b, a).texture(0.5F, 1).light(fullbright).next();
            vertex(endvec.add(diff2), mat, wr);
            wr.color(r, g, b, a).texture(0.5F, 1).light(fullbright).next();

            if (rendersegment.next == null) {
                Pos roundend = rendersegment.endPoint.point.add(rendersegment.diff.normalize().multiply(width));

                vertex(roundend.subtract(diff2), mat, wr);
                wr.color(r, g, b, a).texture(0, 0).light(fullbright).next();
                vertex(endvec.subtract(diff2), mat, wr);
                wr.color(r, g, b, a).texture(0.5F, 0).light(fullbright).next();
                vertex(endvec.add(diff2), mat, wr);
                wr.color(r, g, b, a).texture(0.5F, 1).light(fullbright).next();
                vertex(roundend.add(diff2), mat, wr);
                wr.color(r, g, b, a).texture(0, 1).light(fullbright).next();
            }

            if (rendersegment.prev == null) {
                Pos roundend = rendersegment.startPoint.point.subtract(rendersegment.diff.normalize().multiply(width));

                vertex(startvec.subtract(diff1), mat, wr);
                wr.color(r, g, b, a).texture(0.5F, 0).light(fullbright).next();
                vertex(roundend.subtract(diff1), mat, wr);
                wr.color(r, g, b, a).texture(0, 0).light(fullbright).next();
                vertex(roundend.add(diff1), mat, wr);
                wr.color(r, g, b, a).texture(0, 1).light(fullbright).next();
                vertex(startvec.add(diff1), mat, wr);
                wr.color(r, g, b, a).texture(0.5F, 1).light(fullbright).next();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void vertex(Pos pos, Matrix4f mat, VertexConsumer buffer) {
        buffer.vertex(mat, (float) pos.x, (float) pos.y, (float) pos.z);
    }

    private static Pos getRelativeViewVector(Pos pos) {
        Entity renderEntity = MinecraftClient.getInstance().getCameraEntity();
        return new Pos((float) renderEntity.getX() - pos.x, (float) renderEntity.getY() - pos.y, (float) renderEntity.getZ() - pos.z);
    }

    private static final ParticleTextureSheet LAYER = new ParticleTextureSheet() {
        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableTexture();
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
        }

        @Override
        public void draw(Tessellator tess) {
            tess.draw();
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    };

    @OnlyIn(Dist.CLIENT)
    public static class FXLightningBoltPoint {

        public final Pos point;
        public final Pos basepoint;
        public final Pos offsetvec;

        public FXLightningBoltPoint(Pos basepoint, Pos offsetvec) {
            point = basepoint.add(offsetvec);
            this.basepoint = basepoint;
            this.offsetvec = offsetvec;
        }

    }

    @OnlyIn(Dist.CLIENT)
    public static class FXLightningSegment {

        public final FXLightningBoltPoint startPoint;
        public final FXLightningBoltPoint endPoint;

        public final Pos diff;

        public FXLightningSegment prev;
        public FXLightningSegment next;

        public Pos nextDiff;
        public Pos prevDiff;

        public float sinPrev;
        public float sinNext;
        public final float light;

        public final int segmentNo;
        public final int splitNo;

        public FXLightningSegment(FXLightningBoltPoint start, FXLightningBoltPoint end, float light, int segmentnumber, int splitnumber) {
            startPoint = start;
            endPoint = end;
            this.light = light;
            segmentNo = segmentnumber;
            splitNo = splitnumber;
            diff = endPoint.point.subtract(startPoint.point);
        }

        public FXLightningSegment(Pos start, Pos end) {
            this(new FXLightningBoltPoint(start, new Pos(0, 0, 0)), new FXLightningBoltPoint(end, new Pos(0, 0, 0)), 1, 0, 0);
        }

        public void calcEndDiffs() {
            if (prev != null) {
                Pos prevdiffnorm = prev.diff.normalize();
                Pos thisdiffnorm = diff.normalize();

                prevDiff = thisdiffnorm.add(prevdiffnorm).normalize();
                sinPrev = (float) Math.sin(thisdiffnorm.angle(prevdiffnorm.multiply(-1)) / 2);
            } else {
                prevDiff = diff.normalize();
                sinPrev = 1;
            }

            if (next != null) {
                Pos nextdiffnorm = next.diff.normalize();
                Pos thisdiffnorm = diff.normalize();

                nextDiff = thisdiffnorm.add(nextdiffnorm).normalize();
                sinNext = (float) Math.sin(thisdiffnorm.angle(nextdiffnorm.multiply(-1)) / 2);
            } else {
                nextDiff = diff.normalize();
                sinNext = 1;
            }
        }

        @Override
        public String toString() {
            return startPoint.point.toString() + " " + endPoint.point.toString();
        }
    }

    @OnlyIn(Dist.CLIENT)

    public static class LightningSegmentGenerator {
        private static final int BRANCH_FACTOR = 2;
        private final Random rand;
        private final Map<Integer, Integer> splitParents = new HashMap<>();
        private int segmentCount = 1;
        private int splitCount = 0;

        public LightningSegmentGenerator() {
            this.rand = new Random();
        }

        public Pair<Integer, List<FXLightningSegment>> compute(Pos start, Pos end, double length) {
            List<FXLightningSegment> segmentsA = new ArrayList<>();
            segmentsA.add(new FXLightningSegment(start, end));

            List<FXLightningSegment> segmentsB = new ArrayList<>();

            // Alternate between two lists to save memory allocations
            fractal(segmentsA, segmentsB, length / 1.5, 0.7F, 0.7F, 45);
            fractal(segmentsB, segmentsA, length / 4, 0.5F, 0.8F, 50);
            fractal(segmentsA, segmentsB, length / 15, 0.5F, 0.9F, 55);
            fractal(segmentsB, segmentsA, length / 30, 0.5F, 1.0F, 60);
            fractal(segmentsA, segmentsB, length / 60, 0, 0, 0);
            fractal(segmentsB, segmentsA, length / 100, 0, 0, 0);
            fractal(segmentsA, segmentsB, length / 400, 0, 0, 0);

            calculateCollisionAndDiffs(segmentsB);
            segmentsB.sort((o1, o2) -> Float.compare(o2.light, o1.light));
            return Pair.of(segmentCount, segmentsB);
        }

        private void fractal(List<FXLightningSegment> oldSegments, List<FXLightningSegment> outputSegments, double amount, double splitChance, double splitLength, double splitAngle) {
            outputSegments.clear();
            FXLightningSegment prev;

            for (FXLightningSegment segment : oldSegments) {
                prev = segment.prev;

                Pos subsegment = segment.diff.multiply(1F / BRANCH_FACTOR);

                FXLightningBoltPoint[] newpoints = new FXLightningBoltPoint[BRANCH_FACTOR + 1];

                Pos startpoint = segment.startPoint.point;
                newpoints[0] = segment.startPoint;
                newpoints[BRANCH_FACTOR] = segment.endPoint;

                for (int i = 1; i < BRANCH_FACTOR; i++) {
                    Pos basepoint = startpoint.add(subsegment.multiply(i));
                    Pos randoff = segment.diff.perpendicular().normalize().rotate(rand.nextFloat() * 360, segment.diff)
                            .multiply((rand.nextFloat() - 0.5F) * amount * 2);
                    newpoints[i] = new FXLightningBoltPoint(basepoint, randoff);
                }

                for (int i = 0; i < BRANCH_FACTOR; i++) {
                    FXLightningSegment next = new FXLightningSegment(newpoints[i], newpoints[i + 1], segment.light, segment.segmentNo * BRANCH_FACTOR + i, segment.splitNo);
                    next.prev = prev;
                    if (prev != null) {
                        prev.next = next;
                    }

                    if (i != 0 && rand.nextFloat() < splitChance) {
                        Pos splitrot = next.diff.xCrossProduct().rotate(rand.nextFloat() * 360, next.diff);
                        Pos diff = next.diff.rotate((rand.nextFloat() * 0.66F + 0.33F) * splitAngle, splitrot).multiply(splitLength);

                        splitCount++;
                        splitParents.put(splitCount, next.splitNo);

                        FXLightningSegment split = new FXLightningSegment(newpoints[i], new FXLightningBoltPoint(newpoints[i + 1].basepoint, newpoints[i + 1].offsetvec.add(diff)), segment.light / 2F, next.segmentNo, splitCount);
                        split.prev = prev;

                        outputSegments.add(split);
                    }

                    prev = next;
                    outputSegments.add(next);
                }

                if (segment.next != null) {
                    segment.next.prev = prev;
                }
            }

            segmentCount *= BRANCH_FACTOR;
        }

        private void calculateCollisionAndDiffs(List<FXLightningSegment> segments) {
            Map<Integer, Integer> lastactivesegment = new HashMap<>();

            segments.sort((o1, o2) -> {
                int comp = Integer.compare(o1.splitNo, o2.splitNo);
                if (comp == 0) {
                    return Integer.compare(o1.segmentNo, o2.segmentNo);
                } else {
                    return comp;
                }
            });

            int lastSplitCalc = 0;
            int lastActiveSegment = 0;// unterminated
            float splitResistance = 0;

            for (FXLightningSegment segment : segments) {
                if (segment.splitNo > lastSplitCalc) {
                    lastactivesegment.put(lastSplitCalc, lastActiveSegment);
                    lastSplitCalc = segment.splitNo;
                    lastActiveSegment = lastactivesegment.get(splitParents.get(segment.splitNo));
                    splitResistance = lastActiveSegment < segment.segmentNo ? 50 : 0;
                }

                if (splitResistance >= 40 * segment.light) {
                    continue;
                }

                splitResistance = rayTraceResistance(segment.startPoint.point, segment.endPoint.point, splitResistance);
                lastActiveSegment = segment.segmentNo;
            }
            lastactivesegment.put(lastSplitCalc, lastActiveSegment);

            lastSplitCalc = 0;
            lastActiveSegment = lastactivesegment.get(0);
            for (Iterator<FXLightningSegment> iterator = segments.iterator(); iterator.hasNext(); ) {
                FXLightningSegment segment = iterator.next();
                if (lastSplitCalc != segment.splitNo) {
                    lastSplitCalc = segment.splitNo;
                    lastActiveSegment = lastactivesegment.get(segment.splitNo);
                }

                if (segment.segmentNo > lastActiveSegment) {
                    iterator.remove();
                }
                segment.calcEndDiffs();
            }
        }

        private float rayTraceResistance(Pos start, Pos end, float prevresistance) {
            World world = MinecraftClient.getInstance().world;
            RaycastContext ctx = new RaycastContext(start.vector3d(), end.vector3d(), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, null);
            BlockHitResult ray = world.raycast(ctx);

            if (ray.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ray.getBlockPos();
                BlockState state = world.getBlockState(pos);

                if (state.isAir()) {
                    return prevresistance;
                }

                return prevresistance + state.getBlock().getBlastResistance() + 0.3F;
            } else {
                return prevresistance;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class LightningHandler implements IWorldComponent {
        @ConfigField
        protected int batchThreshold;
        @ConfigField
        protected int fadeTime;
        protected Identifier outsideResource;
        protected Identifier insideResource;

        protected final Deque<LightningParticle> queuedLightningBolts = new ArrayDeque<>();

        @Override
        public void initNew() {
            IWorldComponent.super.initNew();
            outsideResource = new Identifier(GlowingFireGlow.MOD_ID, String.join("/", new String[]{"particle", "lightning", "wisp_large.png"}));
            insideResource = new Identifier(GlowingFireGlow.MOD_ID, String.join("/", new String[]{"particle", "lightning", "wisp_small.png"}));
        }

        @Override
        public void defaultConfig() {
            IWorldComponent.super.defaultConfig();
            batchThreshold = 200;
            fadeTime = 20;
        }

        @SubscribeEvent
        protected void onRenderWorldLast(RenderLevelStageEvent event) {
            MatrixStack ms = event.getPoseStack();
            Profiler profiler = MinecraftClient.getInstance().getProfiler();

            profiler.push("lightning");

            float frame = event.getPartialTick();
            Entity entity = MinecraftClient.getInstance().player;
            TextureManager render = MinecraftClient.getInstance().textureManager;

            double interpPosX = entity.lastRenderX + (entity.getX() - entity.lastRenderX) * frame;
            double interpPosY = entity.lastRenderY + (entity.getY() - entity.lastRenderY) * frame;
            double interpPosZ = entity.lastRenderZ + (entity.getZ() - entity.lastRenderZ) * frame;

            ms.push();
            ms.translate(-interpPosX, -interpPosY, -interpPosZ);

            Tessellator tessellator = Tessellator.getInstance();

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            render.bindTexture(outsideResource);
            int counter = 0;

            tessellator.getBuffer().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
            for (LightningParticle bolt : queuedLightningBolts) {
                bolt.renderBolt(ms, tessellator.getBuffer(), 0, false);
                if (counter % batchThreshold == batchThreshold - 1) {
                    tessellator.draw();
                    tessellator.getBuffer().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
                }
                counter++;
            }
            tessellator.draw();

            render.bindTexture(insideResource);
            counter = 0;

            tessellator.getBuffer().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
            for (LightningParticle bolt : queuedLightningBolts) {
                bolt.renderBolt(ms, tessellator.getBuffer(), 1, true);
                if (counter % batchThreshold == batchThreshold - 1) {
                    tessellator.draw();
                    tessellator.getBuffer().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
                }
                counter++;
            }
            tessellator.draw();

            queuedLightningBolts.clear();

            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);

            ms.pop();

            profiler.pop();

        }
    }


}
