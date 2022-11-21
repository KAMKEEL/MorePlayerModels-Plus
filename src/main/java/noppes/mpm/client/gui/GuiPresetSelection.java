// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.mpm.client.gui;

import java.util.Collections;
import java.util.Vector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MPMEntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.mpm.ModelData;
import noppes.mpm.client.Preset;
import noppes.mpm.client.PresetController;
import noppes.mpm.client.gui.util.GuiInterface;
import noppes.mpm.client.gui.util.GuiListActionListener;
import noppes.mpm.client.gui.util.GuiNPCStringSlot;
import noppes.mpm.client.gui.util.GuiNpcButton;

import org.lwjgl.opengl.GL11;

public class GuiPresetSelection extends GuiInterface implements GuiListActionListener
{
	private GuiNPCStringSlot slot;
	private GuiCreationScreen parent;
	private NBTTagCompound prevData;
	private ModelData playerdata;
	private EntityPlayer player;
	
    public GuiPresetSelection(GuiCreationScreen parent, ModelData playerdata)
    {
    	this.parent = parent;
    	this.playerdata = playerdata;
    	prevData = playerdata.writeToNBT();

		player = Minecraft.getMinecraft().thePlayer;
		
		PresetController.instance.load();
    }
    
    public void initGui()
    {
        super.initGui();
        Vector<String> list = new Vector<String>();
        for(Preset preset : PresetController.instance.presets.values())
        	list.add(preset.name);
        
		Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
        slot = new GuiNPCStringSlot(list,this,false,18);
        slot.registerScrollButtons(4, 5);

    	this.buttonList.add(new GuiNpcButton(2, width / 2 - 100, height - 44,98, 20, "gui.back"));
    	this.buttonList.add(new GuiNpcButton(3, width / 2 + 2, height - 44,98, 20, "gui.load"));
    	this.buttonList.add(new GuiNpcButton(4, width / 2 - 100, height - 22,98, 20, "gui.remove"));
        this.buttonList.add(new GuiNpcButton(5, width / 2 + 2, height - 22,98, 20, "gui.defaults"));
    }


    public void drawScreen(int i, int j, float f)
    {
    	EntityLivingBase player = playerdata.getEntity(mc.theWorld, mc.thePlayer);
    	if(player == null)
    		player = this.player;
    	
    	MPMEntityUtil.Copy(mc.thePlayer, player);
    	
    	int l = (width/2)-180;
    	int i1 =  (height/2) - 90;
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glPushMatrix();
        GL11.glTranslatef(l + 33, i1 + 131, 50F);
        GL11.glScalef(-50, 50, 50);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f2 = player.renderYawOffset;
        float f3 = player.rotationYaw;
        float f4 = player.rotationPitch;
        float f5 = (float)(l + 33) - i;
        float f6 = (float)((i1 + 131) - 50) - j;
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
        RenderManager.instance.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        player.renderYawOffset = f2;
        player.rotationYaw = f3;
        player.rotationPitch = f4;
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
    	Preset preset = PresetController.instance.getPreset(slot.selected);
    	playerdata.readFromNBT(preset.data.writeToNBT());
    }
    public void doubleClicked(){
        close();
    }

    @Override
    public void keyTyped(char par1, int par2)
    {
        if (par2 == 1)
        {
            close();
        }
    }
	private void close() {		
		mc.displayGuiScreen(parent);
	}

	public FontRenderer getFontRenderer() {
		return this.fontRendererObj;
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
    {
		GuiNpcButton guibutton = (GuiNpcButton) button;
		if(guibutton.id == 2){
			playerdata.readFromNBT(prevData);
			close();
		}
		if(guibutton.id == 3){
			close();
		}
		if(guibutton.id == 4){
			PresetController.instance.removePreset(slot.selected);
	        Vector<String> list = new Vector<String>();
	        for(Preset preset : PresetController.instance.presets.values())
	        	list.add(preset.name);
	        
			Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
			slot.setList(list);
			slot.selected = "";
		}
        if(guibutton.id == 5){
            PresetController.instance.addDefaults();
            Vector<String> list = new Vector<String>();
            for(Preset preset : PresetController.instance.presets.values())
                list.add(preset.name);

            Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
            slot.setList(list);
            slot.selected = "";
        }
    }
}
