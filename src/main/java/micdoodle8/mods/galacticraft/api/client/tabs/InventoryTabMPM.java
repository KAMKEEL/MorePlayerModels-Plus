package micdoodle8.mods.galacticraft.api.client.tabs;

import com.sun.org.apache.xml.internal.security.utils.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.mpm.client.ClientProxy;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InventoryTabMPM extends AbstractTab {
	private static final ModelBiped biped = new ModelBiped(0);
	
	public InventoryTabMPM() {
		super(0, 0, 0, new ItemStack(Items.skull, 1, 3));
		displayString = I18n.translate("menu.mpm");
	}


    @Override
    public void onTabClicked() {
        Thread t = new Thread(){
            @Override
            public void run(){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Minecraft mc = Minecraft.getMinecraft();
                mc.displayGuiScreen(new GuiMPM());
            }
        };
        t.start();
    }


    @Override
	public boolean shouldAddToList() {
		return true;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (!visible){
            super.drawButton(minecraft, mouseX, mouseY);
            return;
        }
        this.renderStack = new ItemStack((Item)null);
        
        if(enabled){
	        Minecraft mc = Minecraft.getMinecraft();
	        boolean hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	        if(hovered){
	        	int x = mouseX + mc.fontRenderer.getStringWidth(displayString);
	        	GL11.glTranslatef(x, yPosition + 2, 0);
	        	drawHoveringText(Arrays.asList(new String[] {displayString}), 0, 0, mc.fontRenderer);
                GL11.glTranslatef(-x, -(yPosition + 2), 0);
	        }
        }
        super.drawButton(minecraft, mouseX, mouseY);
        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 14, (float)yPosition + 22, 150.0F);
        GL11.glScalef(20, 20, 20);
		ClientProxy.bindTexture(minecraft.thePlayer.getLocationSkin());
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glRotatef(135.0F, -1.0F, 1.0F, -1.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, -1.0F, 1.0F, -1.0F);
		biped.bipedHeadwear.rotateAngleX = biped.bipedHead.rotateAngleX = 0.7f;
		biped.bipedHeadwear.rotateAngleY = biped.bipedHead.rotateAngleY = (float) (-Math.PI/4);
		biped.bipedHeadwear.rotateAngleZ = biped.bipedHead.rotateAngleZ = -0.5f;
        biped.bipedHead.render(0.064f);
        biped.bipedHeadwear.render(0.0625F);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    protected void drawHoveringText(List list, int x, int y, FontRenderer font){
        if (list.isEmpty())
        	return;

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH);
        int k = 0;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()){
            String s = (String)iterator.next();
            int l = font.getStringWidth(s);

            if (l > k){
                k = l;
            }
        }

        int j2 = x + 12;
        int k2 = y - 12;
        int i1 = 8;

        if (list.size() > 1){
            i1 += 2 + (list.size() - 1) * 10;
        }

        if (j2 + k > this.width){
            j2 -= 28 + k;
        }

        if (k2 + i1 + 6 > this.height){
            k2 = this.height - i1 - 6;
        }

        this.zLevel = 300.0F;
        itemRenderer.zLevel = 300.0F;
        int j1 = -267386864;
        this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
        this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
        this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
        this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
        this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
        int k1 = 1347420415;
        int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
        this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
        this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
        this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
        this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

        for (int i2 = 0; i2 < list.size(); ++i2){
            String s1 = (String)list.get(i2);
            font.drawStringWithShadow(s1, j2, k2, -1);

            if (i2 == 0){
                k2 += 2;
            }

            k2 += 10;
        }

        this.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
}