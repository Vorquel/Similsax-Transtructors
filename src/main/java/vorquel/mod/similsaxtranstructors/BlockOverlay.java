package vorquel.mod.similsaxtranstructors;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class BlockOverlay {

    private final ResourceLocation overlayLocation = new ResourceLocation(SimilsaxTranstructors.MOD_ID.toLowerCase(), "textures/overlay.png");
    private final Vec3[] vs = new Vec3[8];
    {
        for(int i=0; i<8; ++i) {
            int x = (i & 1) == 1 ? 1 : 0;
            int y = (i & 2) == 2 ? 1 : 0;
            int z = (i & 4) == 4 ? 1 : 0;
            vs[i] = new Vec3(x, y, z);
        }
    }
    private final float[][][] uvs = new float[7][4][];
    {
        //arrow 1
        uvs[0][0] = new float[]{0,0};
        uvs[0][1] = new float[]{0,.5f};
        uvs[0][2] = new float[]{.5f,.5f};
        uvs[0][3] = new float[]{.5f,0};
        //arrow 2
        uvs[1][0] = new float[]{0,.5f};
        uvs[1][1] = new float[]{.5f,.5f};
        uvs[1][2] = new float[]{.5f,0};
        uvs[1][3] = new float[]{0,0};
        //arrow 3
        uvs[2][0] = new float[]{.5f,.5f};
        uvs[2][1] = new float[]{.5f,0};
        uvs[2][2] = new float[]{0,0};
        uvs[2][3] = new float[]{0,.5f};
        //arrow 4
        uvs[3][0] = new float[]{.5f,0};
        uvs[3][1] = new float[]{0,0};
        uvs[3][2] = new float[]{0,.5f};
        uvs[3][3] = new float[]{.5f,.5f};
        //cross
        uvs[4][0] = new float[]{.5f,0};
        uvs[4][1] = new float[]{.5f,.5f};
        uvs[4][2] = new float[]{1,.5f};
        uvs[4][3] = new float[]{1,0};
        //bullseye
        uvs[5][0] = new float[]{0,.5f};
        uvs[5][1] = new float[]{0,1};
        uvs[5][2] = new float[]{.5f,1};
        uvs[5][3] = new float[]{.5f,.5f};
        //cancel
        uvs[6][0] = new float[]{.5f,.5f};
        uvs[6][1] = new float[]{.5f,1};
        uvs[6][2] = new float[]{1,1};
        uvs[6][3] = new float[]{1,.5f};
    }
    private final int[][] lookUps = new int[7][6];
    {
        lookUps[0] = new int[]{2, 5, 1, 1, 4, 2};
        lookUps[1] = new int[]{0, 4, 3, 3, 5, 0};
        lookUps[2] = new int[]{1, 2, 5, 2, 1, 4};
        lookUps[3] = new int[]{3, 0, 4, 0, 3, 5};
        lookUps[4] = new int[]{5, 1, 2, 4, 2, 1};
        lookUps[5] = new int[]{4, 3, 0, 5, 0, 3};
        lookUps[6] = new int[]{6, 6, 6, 6, 6, 6};
    }

    @SubscribeEvent
    public void renderOverlay(DrawBlockHighlightEvent event) {
        if(shouldSkip(event))
            return;
        MovingObjectPosition m = event.target;
        BlockPos mPos = m.getBlockPos();
        Vec3 h = m.hitVec;
        int index;
        if(isBadBlock(event))
            index = 6;
        else
            index = ItemSimilsaxTranstructor.getSide(m.sideHit.getIndex(), h.xCoord-mPos.getX(), h.yCoord-mPos.getY(), h.zCoord-mPos.getZ());
        Minecraft.getMinecraft().renderEngine.bindTexture(overlayLocation);
        Vec3 v = getViewerPosition(event.partialTicks);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslated(mPos.getX(), mPos.getY(), mPos.getZ());
        GL11.glTranslated(-v.xCoord, -v.yCoord, -v.zCoord);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, .375f);
        final float P = 1/256f, N = -1/256f;
        final int X = 1, Y = 2, Z = 4;
        GL11.glTranslatef(P, 0, 0);
        drawSide(X, Y, Z, uvs[lookUps[index][0]]);
        GL11.glTranslatef(N, P, 0);
        drawSide(Y, Z, X, uvs[lookUps[index][1]]);
        GL11.glTranslatef(0, N, P);
        drawSide(Z, X, Y, uvs[lookUps[index][2]]);
        GL11.glTranslatef(N, 0, N);
        drawSide(0, Z, Y, uvs[lookUps[index][3]]);
        GL11.glTranslatef(P, N, 0);
        drawSide(0, X, Z, uvs[lookUps[index][4]]);
        GL11.glTranslatef(0, P, N);
        drawSide(0, Y, X, uvs[lookUps[index][5]]);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private boolean shouldSkip(DrawBlockHighlightEvent event) {
        if(event.target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return true;
        if(event.currentItem == null) return true;
        return !(event.currentItem.getItem() instanceof ItemSimilsaxTranstructor);
    }

    private boolean isBadBlock(DrawBlockHighlightEvent event) {
        BlockPos pos = event.target.getBlockPos();
        World world = event.player.worldObj;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block.hasTileEntity(state) || block.isReplaceable(world, pos);
    }

    private Vec3 getViewerPosition(float partialTicks) {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double x = partial(partialTicks, viewer.prevPosX, viewer.posX);
        double y = partial(partialTicks, viewer.prevPosY, viewer.posY);
        double z = partial(partialTicks, viewer.prevPosZ, viewer.posZ);
        return new Vec3(x, y, z);
    }

    private double partial(float partialTicks, double prevPos, double pos) {
        return partialTicks == 1 ? pos : prevPos + partialTicks * (pos - prevPos);
    }

    private void drawSide(int c, int i, int j, float[][] uv) {
        Tessellator.getInstance().getWorldRenderer().func_181668_a(GL11.GL_QUADS, DefaultVertexFormats.field_181707_g);
        addVertex(uv[0][0], uv[0][1], c);
        addVertex(uv[1][0], uv[1][1], c + i);
        addVertex(uv[2][0], uv[2][1], c + i + j);
        addVertex(uv[3][0], uv[3][1], c + j);
        Tessellator.getInstance().draw();
    }

    private void addVertex(double u, double v, int i) {
        Tessellator.getInstance().getWorldRenderer().func_181662_b(vs[i].xCoord, vs[i].yCoord, vs[i].zCoord).func_181673_a(u, v).func_181675_d();
    }
}
