package noppes.mpm.client.model.part.leg;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.ClientProxy;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelScaleRenderer;
import noppes.mpm.client.model.part.leg.legs.*;
import noppes.mpm.constants.EnumAnimation;
import org.lwjgl.opengl.GL11;

public class ModelLegs extends ModelScaleRenderer{

	private ModelData data;
	private EntityLivingBase entity;
	private ModelScaleRenderer leg1, leg2;


	private ModelSpiderLegs spiderLegs;
	private ModelHorseLegs horseLegs;
	private ModelNagaLegs naga;
	private ModelDigitigradeLegs digitigrade;
	private ModelMermaidLegs mermaid;
	private ModelMermaidLegs2 mermaid2;

    private ModelMPM base;

	public ModelLegs(ModelMPM base, ModelScaleRenderer leg1, ModelScaleRenderer leg2, int textWidth, int textHeight) {
		super(base);

		this.base = base;
		this.leg1 = leg1;
		this.leg2 = leg2;

		if(base.isArmor)
			return;

		spiderLegs = new ModelSpiderLegs(base);
		this.addChild(spiderLegs);

		horseLegs = new ModelHorseLegs(base);
		this.addChild(horseLegs);

		this.naga = new ModelNagaLegs(base, textWidth, textHeight);
		this.addChild(naga);

		mermaid = new ModelMermaidLegs(base);
		this.addChild(mermaid);

		mermaid2 = new ModelMermaidLegs2(base);
		this.addChild(mermaid2);

		digitigrade = new ModelDigitigradeLegs(base, textWidth, textHeight);
		this.addChild(digitigrade);
	}
	public void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
    {
		ModelPartData part = data.legParts;
    	rotationPointZ = 0;
    	rotationPointY = 0;

		if(base.isArmor)
			return;
		if(part.type == 2){
			spiderLegs.setRotationAngles(data, par1, par2, par3, par4, par5, par6, entity);
		}
		else if(part.type == 3){
			horseLegs.setRotationAngles(data, par1, par2, par3, par4, par5, par6, entity);
		}
		else if(part.type == 1){
			naga.isRiding = base.isRiding;
			naga.isSleeping = base.isSleeping(entity);
			naga.isCrawling = data.animation == EnumAnimation.CRAWLING;
			naga.isSneaking = base.isSneak;
			naga.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
		}
		else if(part.type == 4){
			mermaid.isRiding = base.isRiding;
			mermaid.isSleeping = base.isSleeping(entity);
			mermaid.isCrawling = data.animation == EnumAnimation.CRAWLING;
			mermaid.isSneaking = base.isSneak;
			mermaid.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
		}
		else if(part.type == 5){
			mermaid2.isRiding = base.isRiding;
			mermaid2.isSleeping = base.isSleeping(entity);
			mermaid2.isCrawling = data.animation == EnumAnimation.CRAWLING;
			mermaid2.isSneaking = base.isSneak;
			mermaid2.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
		}
		else if(part.type == 6){
			digitigrade.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
		}
    }
	
	@Override
    public void render(float par1)
    {
		if(!showModel || isHidden)
			return;
		ModelPartData part = data.legParts;
		if(part.type < 0)
			return;
		GL11.glPushMatrix();
		if(part.type == 4)
			part.playerTexture = false;
		if(part.type == 0){
			part.playerTexture = true;
		}
		if(!base.isArmor){
			if(!part.playerTexture){
				ClientProxy.bindTexture(part.getResource());
				base.currentlyPlayerTexture = false;
			}
			else if(!base.currentlyPlayerTexture){
				ClientProxy.bindTexture(((AbstractClientPlayer)entity).getLocationSkin());
	            base.currentlyPlayerTexture = true;
			}
		}
		if(part.type == 0){
			leg1.setConfig(config, x, y, z);
			leg1.render(par1);
			leg2.setConfig(config, -x, y, z);
			leg2.render(par1);
		}

		if(!base.isArmor){
			naga.isHidden = part.type != 1;
			spiderLegs.isHidden = part.type != 2;
			horseLegs.isHidden = part.type != 3;
			mermaid.isHidden = part.type != 4;
			mermaid2.isHidden = part.type != 5;
			digitigrade.isHidden = part.type != 6;
	
			if(!horseLegs.isHidden){
				x = 0;
				y *= 1.8f;
				GL11.glScalef(0.9f, 0.9f, 0.9f);
			}
			else if(!spiderLegs.isHidden){
				x = 0;
				y *= 2f;
			}
			else if(!naga.isHidden){
				x = 0;
				y *= 2f;
			}
			else if(!mermaid.isHidden || !mermaid2.isHidden || !digitigrade.isHidden){
				x = 0;
				y *= 2f;
			}
		}
		boolean bo = entity.hurtTime <= 0 && entity.deathTime <= 0 && !base.isArmor;
    	if(bo){
	    	float red = (data.legParts.color >> 16 & 255) / 255f;
	    	float green = (data.legParts.color >> 8  & 255) / 255f;
	    	float blue = (data.legParts.color & 255) / 255f;
	    	GL11.glColor3f(red, green, blue);
    	}
		super.render(par1);
		if(bo){
			GL11.glColor4f(1, 1, 1, 1);
		}
		GL11.glPopMatrix();
    }
	public void setData(ModelData data, EntityLivingBase entity) {
		this.data = data;
		this.entity = entity;
	}
}
