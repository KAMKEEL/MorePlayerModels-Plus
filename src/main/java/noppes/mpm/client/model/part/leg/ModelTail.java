package noppes.mpm.client.model.part.leg;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartConfig;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.ClientProxy;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelScaleRenderer;
import noppes.mpm.client.model.part.leg.tails.*;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumParts;
import org.lwjgl.opengl.GL11;

public class ModelTail extends ModelScaleRenderer {
	public ModelData data;
	private EntityLivingBase entity;
	private ModelMPM base;
	
	private ModelRenderer tail;
	private ModelCanineTail fox;
	private ModelRenderer dragon;
	private ModelRenderer squirrel;
	private ModelRenderer horse;
	private ModelRenderer fin;
	private ModelRenderer rodent;
	private ModelRenderer feather;
	private ModelMonkeyTail monkey;

	private int color = 0xFFFFFF;
	
	private ResourceLocation location = null;
	
	public ModelTail(ModelMPM base) {
		super(base);
		this.base = base;
		this.rotationPointY = 11;

		tail = new ModelRenderer(base, 56, 21);
		tail.setTextureSize(64, 32);
		tail.addBox(-1F, 0F, 0F, 2, 9, 2);
		tail.setRotationPoint(0F, 0, 1F);
		setRotation(tail, 0.8714253F, 0F, 0F);
		this.addChild(tail);
		
		horse = new ModelRenderer(base);
		horse.setTextureSize(32, 32);
		horse.setRotationPoint(0, -1, 1);
		this.addChild(horse);

		ModelRenderer tailBase = new ModelRenderer(base, 0, 26);
		tailBase.setTextureSize(32, 32);
		tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3);
		setRotation(tailBase, -1.134464F, 0.0F, 0.0F);
		horse.addChild(tailBase);
		ModelRenderer tailMiddle = new ModelRenderer(base, 0, 13);
		tailMiddle.setTextureSize(32, 32);
		tailMiddle.addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7);
		setRotation(tailMiddle, -1.134464F, 0.0F, 0.0F);
		horse.addChild(tailMiddle);
		ModelRenderer tailTip = new ModelRenderer(base, 0, 0);
		tailTip.setTextureSize(32, 32);
		tailTip.addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7);
		setRotation(tailTip, -1.40215F, 0.0F, 0.0F);
		horse.addChild(tailTip);
		horse.rotateAngleX = 0.5f;

		this.addChild(dragon = new ModelDragonTail(base));
		this.addChild(squirrel = new ModelSquirrelTail(base));
		this.addChild(fin = new ModelTailFin(base));
		this.addChild(rodent = new ModelRodentTail(base));
		this.addChild(feather = new ModelFeatherTail(base));
		this.addChild(fox = new ModelCanineTail(base));
		this.addChild(monkey = new ModelMonkeyTail(base));
	}

	public void setData(ModelData data, EntityLivingBase entity) {
		this.data = data;
		this.entity = entity;
		initData(data);
	}
	
	public void setRotationAngles(float par1, float par2, float par3,
			float par4, float par5, float par6, Entity entity) {
		ModelPartData part = data.getPartData(EnumParts.LEGS);
		ModelPartData partTail = data.getPartData(EnumParts.TAIL);
		ModelPartConfig config = data.getPartConfig(EnumParts.LEGS);
		float rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.2f * par2;
		float rotateAngleX = MathHelper.sin(par3 * 0.067F) * 0.05F;
		rotationPointZ = 0;
		rotationPointY = 11;
		if(data.animation == EnumAnimation.WAG){
			rotateAngleY = (float) (Math.sin(entity.ticksExisted * 0.55f) * 0.45f );
		}
		if(part.type == 2){
			rotationPointY = 12 + (config.scaleY-1) * 3;
			rotationPointZ = 15 + (config.scaleZ-1) * 10;
			if(data.isSleeping() || data.animation == EnumAnimation.CRAWLING){
				rotationPointY = 12 + 16 * config.scaleZ;
				rotationPointZ = 1f * config.scaleY;

				rotateAngleX = (float) (Math.PI / -4);
			}
		}
		else if(part.type == 3){
			rotationPointY = 10;
			rotationPointZ = 16 + (config.scaleZ-1) * 12;
		}
		else{
			rotationPointZ = (1 - config.scaleZ) * 1;
		}
		if(partTail != null){
			if(partTail.type == 2) {
				rotateAngleX += 0.5;
			}
			if(partTail.type == 0) {
				rotateAngleX += 0.87F;
			}
			if(partTail.type == 7){
				fox.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
			}
			if(partTail.type == 8){
				monkey.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
			}
		}

		rotationPointZ += base.bipedRightLeg.rotationPointZ + 0.5f;
		fox.rotateAngleX = tail.rotateAngleX = feather.rotateAngleX = dragon.rotateAngleX = squirrel.rotateAngleX = horse.rotateAngleX = fin.rotateAngleX = rodent.rotateAngleX = rotateAngleX;
		fox.rotateAngleY = tail.rotateAngleY = feather.rotateAngleY = dragon.rotateAngleY = squirrel.rotateAngleY = horse.rotateAngleY = fin.rotateAngleY = rodent.rotateAngleY = rotateAngleY;
	}

    public void setLivingAnimations(ModelPartData data, EntityLivingBase entity, float par2, float par3, float par4) {
    }

	public void initData(ModelData data) {
		ModelPartData config = data.getPartData(EnumParts.TAIL);
		if(config == null)
		{
			isHidden = true;
			return;
		}
		color = config.color;
		isHidden = false;
		tail.isHidden = config.type != 0;
		dragon.isHidden = config.type != 1;
		horse.isHidden = config.type != 2;
		squirrel.isHidden = config.type != 3;
		fin.isHidden = config.type != 4;
		rodent.isHidden = config.type != 5;
		feather.isHidden = config.type != 6;
		fox.isHidden = config.type != 7;

		monkey.isHidden = config.type != 8;
		monkey.monkey.isHidden = config.pattern != 0;
		monkey.monkey_wrapped.isHidden = config.pattern != 1;
		monkey.monkey_large.isHidden = config.pattern != 2;

		if(!config.playerTexture){
			location = (ResourceLocation) config.getResource();
		}
		else
			location = null;
	}

	@Override
    public void render(float par1)
    {
		if (this.isHidden || !this.showModel)
			return;
		if(!base.isArmor){
	    	if(location != null){
				ClientProxy.bindTexture(location);
	            base.currentlyPlayerTexture = false;
	    	}
	    	else if(!base.currentlyPlayerTexture){
				ClientProxy.bindTexture(((AbstractClientPlayer)entity).getLocationSkin());
	            base.currentlyPlayerTexture = true;
			}
		}
    	boolean bo = entity.hurtTime <= 0 && entity.deathTime <= 0;
    	if(bo){
	    	float red = (color >> 16 & 255) / 255f;
	    	float green = (color >> 8  & 255) / 255f;
	    	float blue = (color & 255) / 255f;
	    	GL11.glColor4f(red, green, blue, 1);
    	}
    	super.render(par1);
    	if(bo){
	    	GL11.glColor4f(1, 1, 1, 1);
    	}
    }

}
