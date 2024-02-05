package noppes.mpm.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import noppes.mpm.ModelPartConfig;
import org.lwjgl.opengl.GL11;

public class ModelScaleRenderer extends ModelRenderer {
    protected ModelPartConfig config;
    
    public float x, y, z;

	public ModelScaleRenderer(ModelBase par1ModelBase) {
		super(par1ModelBase);
	}
    public ModelScaleRenderer(ModelBase par1ModelBase, int par2, int par3)
    {
        this(par1ModelBase);
//        if (((IModelMPM)par1ModelBase).getX64()) {
//
//        }
        this.textureHeight = 64;
        this.setTextureOffset(par2, par3);
    }
    
    public void setConfig(ModelPartConfig config, float x, float y, float z){
    	this.config = config;
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }

	public void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

    public void render(float par1)
    {
		if(!showModel || isHidden)
			return;
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        if(config != null)
        	GL11.glTranslatef(config.transX, config.transY, config.transZ);
        if(config != null)
        	GL11.glScalef(config.scaleX, config.scaleY, config.scaleZ);
        super.render(par1);
        GL11.glPopMatrix();
    }
}
