package noppes.mpm.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MPMEntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.ModelData;
import noppes.mpm.PlayerDataController;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiModelInterface extends GuiInterface{
	public ModelData playerdata;
		
	private static int rotation = 0;
	
	private GuiNpcButton left,right,zoom,unzoom;
	
	private EntityPlayer player;
	
	private static float zoomed = 60;
	
	public int xOffset = 0;
	
	public GuiModelInterface(){
		xSize = 380;
		player = Minecraft.getMinecraft().thePlayer;
		playerdata = PlayerDataController.instance.getPlayerData(Minecraft.getMinecraft().thePlayer);
		//player.registerExtendedProperties("MPMData", playerdata);
	}

    @Override
    public void initGui() {
    	super.initGui();

    	addButton(unzoom = new GuiNpcButton(666, guiLeft + 148 + xOffset, guiTop + 200, 20, 20, "-"));
    	addButton(zoom = new GuiNpcButton(667, guiLeft + 214 + xOffset, guiTop + 200, 20, 20, "+"));
    	addButton(left = new GuiNpcButton(668, guiLeft + 170 + xOffset, guiTop + 200, 20, 20, "<"));
    	addButton(right = new GuiNpcButton(669, guiLeft + 192 + xOffset, guiTop + 200, 20, 20, ">"));

    	addButton(new GuiNpcButton(66, width - 22, 2, 20, 20, "X"));
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
    	if(btn.id == 66){
    		close();
    	}
    }
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
    	if(Mouse.isButtonDown(0)){
	    	if(left.mousePressed(mc, par1, par2))
	    		rotation++;
	    	else if(right.mousePressed(mc, par1, par2))
	    		rotation--;
	    	else if(zoom.mousePressed(mc, par1, par2))
	    		zoomed++;
	    	else if(unzoom.mousePressed(mc, par1, par2) && zoomed > 10)
	    		zoomed--;
    	}
        this.drawDefaultBackground();//drawdefault bg
        GL11.glColor4f(1, 1, 1, 1);

    	EntityLivingBase player = playerdata.getEntity(Minecraft.getMinecraft().theWorld, mc.thePlayer);
    	if(player == null)
    		player = this.player;
    	else
    		MPMEntityUtil.Copy(mc.thePlayer, player);
    	
    	int l = guiLeft + 190 + xOffset;
    	int i1 =  guiTop + 180;
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(l, i1, 60F);
        GL11.glScalef(-zoomed, zoomed, zoomed);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f5 = (float)(l) - par1;
        float f6 = (float)(i1 - 50) - par2;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 80F) * 20F, 1.0F, 0.0F, 0.0F);

        float f = player.renderYawOffset;
        float f2 = player.rotationYaw;
        float f3 = player.rotationPitch;
        float f4 = player.prevRotationYawHead;
        player.renderYawOffset = rotation;
        player.rotationYaw = (float)Math.atan(f5 / 80F) * 40F + rotation;
        player.rotationPitch = -(float)Math.atan(f6 / 80F) * 20F;
        player.prevRotationYawHead = player.rotationYawHead = player.rotationYaw;
        GL11.glTranslatef(0.0F, player.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;

        try{
            RenderManager.instance.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        }
        catch(Exception e){
        	playerdata.setEntityClass(null);
        }

        player.renderYawOffset = f;
        player.rotationYaw = f2;
        player.rotationPitch = f3;
        player.prevRotationYawHead = f4;
        
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0f, 500.065F);
    	super.drawScreen(par1, par2, par3);
        GL11.glPopMatrix();
    }
    
    @Override
    public void keyTyped(char par1, int par2)
    {
    	super.keyTyped(par1, par2);
        if (par2 == 1)
        {
        	close();
        }
    }
    public void close(){
        this.mc.displayGuiScreen((GuiScreen)null);
        this.mc.setIngameFocus();
    }
}
