package vorquel.mod.similsaxtranstructors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
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
        float lo = 1/32f, hi = 15/32f;
        //arrow 1
        uvs[0][0] = new float[]{lo,lo};
        uvs[0][1] = new float[]{lo,hi};
        uvs[0][2] = new float[]{hi,hi};
        uvs[0][3] = new float[]{hi,lo};
        //arrow 2
        uvs[1][0] = new float[]{lo,hi};
        uvs[1][1] = new float[]{hi,hi};
        uvs[1][2] = new float[]{hi,lo};
        uvs[1][3] = new float[]{lo,lo};
        //arrow 3
        uvs[2][0] = new float[]{hi,hi};
        uvs[2][1] = new float[]{hi,lo};
        uvs[2][2] = new float[]{lo,lo};
        uvs[2][3] = new float[]{lo,hi};
        //arrow 4
        uvs[3][0] = new float[]{hi,lo};
        uvs[3][1] = new float[]{lo,lo};
        uvs[3][2] = new float[]{lo,hi};
        uvs[3][3] = new float[]{hi,hi};
        //cross
        uvs[4][0] = new float[]{.5f + lo,lo};
        uvs[4][1] = new float[]{.5f + lo,hi};
        uvs[4][2] = new float[]{.5f + hi,hi};
        uvs[4][3] = new float[]{.5f + hi,lo};
        //bullseye
        uvs[5][0] = new float[]{lo,.5f + lo};
        uvs[5][1] = new float[]{lo,.5f + hi};
        uvs[5][2] = new float[]{hi,.5f + hi};
        uvs[5][3] = new float[]{hi,.5f + lo};
        //cancel
        uvs[6][0] = new float[]{.5f + lo,.5f + lo};
        uvs[6][1] = new float[]{.5f + lo,.5f + hi};
        uvs[6][2] = new float[]{.5f + hi,.5f + hi};
        uvs[6][3] = new float[]{.5f + hi,.5f + lo};
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
        BlockPos mPos = m.func_178782_a();
        Vec3 h = m.hitVec;
        int index;
        if(isBadBlock(event))
            index = 6;
        else
            index = ItemSimilsaxTranstructor.getSide(m.field_178784_b.getIndex(), h.xCoord-mPos.getX(), h.yCoord-mPos.getY(), h.zCoord-mPos.getZ());
        Minecraft.getMinecraft().renderEngine.bindTexture(overlayLocation);
        Vec3 v = getViewerPosition(event.partialTicks);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslated(mPos.getX(), mPos.getY(), mPos.getZ());
        GL11.glTranslated(-v.xCoord, -v.yCoord, -v.zCoord);
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

    private boolean shouldSkip(DrawBlockHighlightEvent event) {
        if(event.target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return true;
        if(event.currentItem == null) return true;
        return !(event.currentItem.getItem() instanceof ItemSimilsaxTranstructor);
    }

    private boolean isBadBlock(DrawBlockHighlightEvent event) {
        BlockPos pos = event.target.func_178782_a();
        World world = event.player.worldObj;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block.hasTileEntity(state) || block.isReplaceable(world, pos);
    }

    private void drawSide(int c, int i, int j, float[][] uv) {
        double lo = 1/16d, hi = 15/16d;
        Tessellator.getInstance().getWorldRenderer().startDrawingQuads();
        addVertex(uv[0][0], uv[0][1], c, 1d, i, lo, j, lo);
        addVertex(uv[1][0], uv[1][1], c, 1d, i, hi, j, lo);
        addVertex(uv[2][0], uv[2][1], c, 1d, i, hi, j, hi);
        addVertex(uv[3][0], uv[3][1], c, 1d, i, lo, j, hi);
        Tessellator.getInstance().draw();
    }

    private void addVertex(double u, double v, Object... args) {
        double x=0, y=0, z=0;
        for(int i=0; i<args.length; i+=2) {
            int index = (Integer)args[i];
            double weight = (Double)args[i+1];
            x += weight*vs[index].xCoord;
            y += weight*vs[index].yCoord;
            z += weight*vs[index].zCoord;
        }
        Tessellator.getInstance().getWorldRenderer().addVertexWithUV(x, y, z, u, v);
    }
}
