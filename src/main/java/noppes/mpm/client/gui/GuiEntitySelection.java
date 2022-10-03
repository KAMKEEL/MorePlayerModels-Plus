package noppes.mpm.client.gui;

import java.util.Collections;
import java.util.Vector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MPMEntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.ModelData;
import noppes.mpm.client.gui.util.GuiListActionListener;
import noppes.mpm.client.gui.util.GuiNPCStringSlot;

import org.lwjgl.opengl.GL11;

public class GuiEntitySelection extends GuiScreen implements GuiListActionListener
{
	private GuiNPCStringSlot slot;
	private GuiCreationScreen parent;
	private Class<? extends EntityLivingBase> prevModel;
	private ModelData playerdata;
	private EntityPlayer player;
	private int width, height;
	
    public GuiEntitySelection(GuiCreationScreen parent, ModelData playerdata)
    {
    	this.parent = parent;
    	this.playerdata = playerdata;
		prevModel = playerdata.getEntityClass();
		
		player = Minecraft.getMinecraft().thePlayer;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        Vector<String> list = new Vector<String>(parent.data.keySet());
        list.add("Player");
		Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
        slot = new GuiNPCStringSlot(list,this,false,18);
        if(playerdata.getEntityClass() != null)
        	slot.selected = (String) EntityList.classToStringMapping.get(playerdata.getEntityClass());
        else{
        	slot.selected = "Player";
        }
        slot.registerScrollButtons(4, 5);
        
    	this.buttonList.add(new GuiButton(2, width / 2 - 100, height - 44,98, 20, "Back"));
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
    	EntityLivingBase player = playerdata.getEntity(mc.theWorld, mc.thePlayer);
    	if(player == null)
    		player = this.player;
    	
    	MPMEntityUtil.Copy(this.mc.thePlayer, player);
    	
    	int l = (width/2)-180;
    	int i1 =  (height/2) - 90;
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glPushMatrix();
        GL11.glTranslatef(l + 233, i1 + 231, 50F);
        float scale = 1;
        if(player.height > 2.4)
        	scale = 2 / player.height;
        scale = 1;
        GL11.glScalef(-50 * scale, 50 * scale, 50 * scale);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f2 = player.renderYawOffset;
        float f3 = player.rotationYaw;
        float f4 = player.rotationPitch;
        float f7 = player.rotationYawHead;
        float f5 = (float)(l + 233) - i;
        float f6 = (float)((i1 + 231) - 50) - j;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        player.renderYawOffset = (float)Math.atan(f5 / 40F) * 20F;
        player.rotationYaw = (float)Math.atan(f5 / 40F) * 40F;
        player.rotationPitch = -(float)Math.atan(f6 / 40F) * 20F;
        player.rotationYawHead = player.rotationYaw;
        GL11.glTranslatef(0.0F, player.yOffset, 0.0F);
    	RenderManager.instance.playerViewY = 180F;
        try{
            RenderManager.instance.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        }
        catch(Exception e){
        	playerdata.setEntityClass(null);
        }
        player.renderYawOffset = f2;
        player.rotationYaw = f3;
        player.rotationPitch = f4;
        player.rotationYawHead = f7;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    	slot.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
    }
    public void elementClicked(){
    	playerdata.setEntityClass(parent.data.get(slot.selected));
    }
    public void doubleClicked(){
        close();
    }

    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1)
        {
            close();
        }
    }
	private void close() {		
		this.mc.displayGuiScreen(parent);
	}

	protected void actionPerformed(GuiButton guibutton)
    {
		close();
    }

}
