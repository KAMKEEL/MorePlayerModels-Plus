package noppes.mpm.client.model;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartConfig;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.ClientProxy;
import noppes.mpm.client.model.animation.AniCrawling;
import noppes.mpm.client.model.animation.AniHug;
import noppes.mpm.client.model.part.ModelLimbWear;
import noppes.mpm.client.model.part.arm.ModelClaws;
import noppes.mpm.client.model.part.body.*;
import noppes.mpm.client.model.part.head.*;
import noppes.mpm.client.model.part.head.snout.ModelSnout;
import noppes.mpm.client.model.part.leg.ModelLegs;
import noppes.mpm.client.model.part.leg.ModelSkirt;
import noppes.mpm.client.model.part.leg.ModelTail;
import noppes.mpm.constants.EnumAnimation;

import noppes.mpm.constants.EnumParts;
import org.lwjgl.opengl.GL11;

public class ModelMPM extends ModelBiped{
	public ModelData data;

	private ModelPartInterface wings;
	private ModelPartInterface mohawk;
	private ModelPartInterface hair;
	private ModelPartInterface beard;
	private ModelPartInterface breasts;
	private ModelPartInterface snout;
	private ModelPartInterface ears;
	private ModelPartInterface fin;
	private ModelPartInterface skirt;
	private ModelPartInterface horns;
	private ModelPartInterface clawsR;
	private ModelPartInterface clawsL;
	private ModelCape cape;
	
	private ModelLegs legs;
	private ModelTail tail;
	public ModelBase entityModel;
	public EntityLivingBase entity;

	public ModelRenderer bipedBodywear;
	public ModelRenderer bipedRightArmWear;
	public ModelRenderer bipedLeftArmwear;
	public ModelRenderer bipedRightLegWear;
	public ModelRenderer bipedLeftLegWear;

	private ModelScaleRenderer headwear;
	private ModelScaleRenderer bodywear;
	private final ModelScaleRenderer solidLeftArmWear;
	private final ModelScaleRenderer solidRightArmWear;
	private final ModelScaleRenderer solidLeftLegWear;
	private final ModelScaleRenderer solidRightLegWear;

	public boolean currentlyPlayerTexture;
	
	public boolean isArmor;
	public boolean isAlexArmor;
	public boolean x64 = false;

	// Steve 64x64 and Alex 64x64
	public ModelMPM(float par1, boolean alex) {

		super(par1);
		isArmor = par1 > 0;
		x64 = true;
		float par2 = 0;

		this.bipedCloak = new ModelRenderer(this, 0, 0);
		this.bipedCloak.setTextureSize(64,32);
		this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, par1);

		this.cape = new ModelCape(this);

		this.bipedEars = new ModelRenderer(this, 24, 0);
		this.bipedEars.setTextureSize(64, 32);
		this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, par1);

		this.bipedHead = (new ModelScaleRenderer(this, 0, 0));
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		this.bipedHeadwear = (new ModelScaleRenderer(this, 32, 0));
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		this.bipedBody = (new ModelScaleRenderer(this, 16, 16));
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		// New Extension
		this.bipedBodywear = (new ModelScaleRenderer(this, 16, 32));
		this.bipedBodywear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1 + 0.5F);
		this.bipedBody.addChild(this.bipedBodywear);
		// this.bipedBodywear.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		bodywear = new ModelBodywear(this, 64, 64);
		this.bipedBody.addChild(bodywear);

		// Steve 64x64 Model or Alex 64x64 Model
		if (alex){
			// Alex Version
			this.bipedRightArm = (new ModelScaleRenderer(this, 40, 16));
			this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, par1);
			// this.bipedRightArm.setRotationPoint(-5.0F, 2.5F + par2, 0.0F);

			this.bipedLeftArm = new ModelScaleRenderer(this, 32, 48);
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, par1);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.5F + par2, 0.0F);

			this.bipedRightArmWear = (new ModelScaleRenderer(this, 40, 32));
			this.bipedRightArmWear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, par1 + 0.25F);
			this.bipedRightArm.addChild(this.bipedRightArmWear);
			this.solidRightArmWear = new ModelLimbWear(this,"arm","right","Alex");
			this.bipedRightArm.addChild(this.solidRightArmWear);

			this.bipedLeftArmwear = new ModelScaleRenderer(this, 48, 48);
			this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, par1 + 0.25F);
			this.bipedLeftArm.addChild(this.bipedLeftArmwear);
			this.solidLeftArmWear = new ModelLimbWear(this,"arm","left","Alex");
			this.bipedLeftArm.addChild(this.solidLeftArmWear);
		}
		else{
			// Steve Version
			this.bipedRightArm = (new ModelScaleRenderer(this, 40, 16));
			this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1);
			this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + par2, 0.0F);

			this.bipedLeftArm = new ModelScaleRenderer(this, 32, 48);
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + par2, 0.0F);

			this.bipedRightArmWear = (new ModelScaleRenderer(this, 40, 32));
			this.bipedRightArmWear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1 + 0.25F);
			this.bipedRightArm.addChild(this.bipedRightArmWear);
			this.solidRightArmWear = new ModelLimbWear(this,"arm","right","Steve");
			this.bipedRightArm.addChild(this.solidRightArmWear);

			this.bipedLeftArmwear = new ModelScaleRenderer(this, 48, 48);
			this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1 + 0.25F);
			this.bipedLeftArm.addChild(this.bipedLeftArmwear);
			this.solidLeftArmWear = new ModelLimbWear(this,"arm","left","Steve");
			this.bipedLeftArm.addChild(this.solidLeftArmWear);
		}

		this.bipedRightLeg = (new ModelScaleRenderer(this, 0, 16));
		this.bipedRightLeg.addBox(-2.08F, 0.0F, -2.0F, 4, 12, 4, par1);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + par2, 0.0F);

		this.bipedLeftLeg = new ModelScaleRenderer(this, 16, 48);
		this.bipedLeftLeg.addBox(-1.92F, 0.0F, -2.0F, 4, 12, 4, par1);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + par2, 0.0F);

		this.bipedRightLegWear = (new ModelScaleRenderer(this, 0, 32));
		this.bipedRightLegWear.addBox(-2.08F, 0.0F, -2.0F, 4, 12, 4, par1 + 0.25F);
		this.bipedRightLeg.addChild(this.bipedRightLegWear);
		this.solidRightLegWear = new ModelLimbWear(this,"leg","right","Steve");
		this.bipedRightLeg.addChild(this.solidRightLegWear);

		this.bipedLeftLegWear = new ModelScaleRenderer(this, 0, 48);
		this.bipedLeftLegWear.addBox(-1.92F, 0.0F, -2.0F, 4, 12, 4, par1 + 0.25F);
		this.bipedLeftLeg.addChild(this.bipedLeftLegWear);
		this.solidLeftLegWear = new ModelLimbWear(this,"leg","left","Steve");
		this.bipedLeftLeg.addChild(this.solidLeftLegWear);


		headwear = new ModelHeadwear(this);
		legs = new ModelLegs(this, (ModelScaleRenderer)bipedRightLeg, (ModelScaleRenderer)bipedLeftLeg, 64, 64);

		this.bipedBody.addChild(breasts = new ModelBreasts(this, 64, 64));
		if(!isArmor){
			this.bipedHead.addChild(ears = new ModelEars(this));
			this.bipedHead.addChild(mohawk = new ModelMohawk(this));
			this.bipedHead.addChild(hair = new ModelHair(this));
			this.bipedHead.addChild(beard = new ModelBeard(this));

			// Completed
			this.bipedHead.addChild(snout = new ModelSnout(this));
			this.bipedHead.addChild(horns = new ModelHorns(this));

			// Completed
			tail = new ModelTail(this);

			this.bipedBody.addChild(wings = new ModelWings(this));
			this.bipedBody.addChild(fin = new ModelFin(this));
			this.bipedBody.addChild(skirt = new ModelSkirt(this));
			this.bipedLeftArm.addChild(clawsL = new ModelClaws(this, false));
			this.bipedRightArm.addChild(clawsR = new ModelClaws(this, true));
		}
	}

	public ModelMPM(float par1, int alexArms) {
		super(par1);
		isArmor = par1 > 0;
		x64 = false;
		if (isArmor && alexArms == 1) {
			isAlexArmor = true;
		}

		float par2 = 0;

		this.bipedCloak = new ModelRenderer(this, 0, 0);
		this.bipedCloak.setTextureSize(64,32);
		this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, par1);

		this.cape = new ModelCape(this);

		this.bipedEars = new ModelRenderer(this, 24, 0);
		this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, par1);

		this.bipedHead = (new ModelScaleRenderer(this, 0, 0));
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		this.bipedHeadwear = (new ModelScaleRenderer(this, 32, 0));
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		this.bipedBody = (new ModelScaleRenderer(this, 16, 16));
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		if (alexArms == 0) {
			this.bipedRightArm = new ModelScaleRenderer(this, 40, 16);
			this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1);
			this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + par2, 0.0F);

			this.bipedLeftArm = new ModelScaleRenderer(this, 40, 16);
			this.bipedLeftArm.mirror = true;
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + par2, 0.0F);
		} else {
			this.bipedRightArm = (new ModelScaleRenderer(this, 40, 16));
			this.bipedLeftArm = new ModelScaleRenderer(this, 40, 16);
			this.bipedLeftArm.mirror = true;

			if (isArmor) {
				this.bipedLeftArm.setRotationPoint(5.0F, 2.5F + par2, 0.0F);
				this.bipedRightArm.addBox(-4.5F, -2.0F, -2.0F, 4, 12, 4, par1);
				this.bipedLeftArm.addBox(0.25F, -2.0F, -2.0F, 4, 12, 4, par1);
			} else {
				this.bipedLeftArm.setRotationPoint(5.0F, 2.5F + par2, 0.0F);
				this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, par1);
				this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, par1);
			}
		}

		this.bipedRightLeg = new ModelScaleRenderer(this, 0, 16);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + par2, 0.0F);

		this.bipedLeftLeg = new ModelScaleRenderer(this, 0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + par2, 0.0F);

		// Body
		this.bipedBodywear = (new ModelScaleRenderer(this, 0, 0));
		this.bipedBody.addChild(this.bipedBodywear);

		bodywear = new ModelBodywear(this, 0, 0);
		this.bipedBody.addChild(bodywear);

		// Arms
		this.bipedRightArmWear = (new ModelScaleRenderer(this, 0, 0));
		this.bipedRightArm.addChild(this.bipedRightArmWear);
		this.solidRightArmWear = (new ModelScaleRenderer(this, 0, 0));
		this.bipedRightArm.addChild(this.solidRightArmWear);
		this.bipedLeftArmwear = new ModelScaleRenderer(this, 0, 0);
		this.bipedLeftArm.addChild(this.bipedLeftArmwear);
		this.solidLeftArmWear = (new ModelScaleRenderer(this, 0, 0));
		this.bipedLeftArm.addChild(this.solidLeftArmWear);

		// Legs
		this.bipedRightLegWear = (new ModelScaleRenderer(this, 0, 0));
		this.bipedRightLeg.addChild(this.bipedRightLegWear);
		this.solidRightLegWear = new ModelScaleRenderer(this, 0, 0);
		this.bipedRightLeg.addChild(this.solidRightLegWear);
		this.bipedLeftLegWear = new ModelScaleRenderer(this, 0, 0);
		this.bipedLeftLeg.addChild(this.bipedLeftLegWear);
		this.solidLeftLegWear = new ModelScaleRenderer(this, 0, 0);
		this.bipedLeftLeg.addChild(this.solidLeftLegWear);

		headwear = new ModelHeadwear(this, true);
		legs = new ModelLegs(this, (ModelScaleRenderer)bipedRightLeg, (ModelScaleRenderer)bipedLeftLeg, 64, 32);

		this.bipedBody.addChild(breasts = new ModelBreasts(this, 64, 32));
		if(!isArmor){
			this.bipedHead.addChild(ears = new ModelEars(this));
			this.bipedHead.addChild(mohawk = new ModelMohawk(this));
			this.bipedHead.addChild(hair = new ModelHair(this));
			this.bipedHead.addChild(beard = new ModelBeard(this));

			// Completed
			this.bipedHead.addChild(snout = new ModelSnout(this));
			this.bipedHead.addChild(horns = new ModelHorns(this));

			// Completed
			tail = new ModelTail(this);
			this.bipedBody.addChild(wings = new ModelWings(this));
			this.bipedBody.addChild(fin = new ModelFin(this));
			this.bipedBody.addChild(skirt = new ModelSkirt(this));
			this.bipedLeftArm.addChild(clawsL = new ModelClaws(this, false));
			this.bipedRightArm.addChild(clawsR = new ModelClaws(this, true));
		}
	}
	
	public void setPlayerData(ModelData data, EntityLivingBase entity){
		this.data = data;
		AbstractClientPlayer player = (AbstractClientPlayer) entity;
		if(!isArmor){
			mohawk.setData(data, player);
			beard.setData(data, player);
			hair.setData(data, player);
			snout.setData(data, player);
			tail.setData(data, player);
			fin.setData(data, player);
			wings.setData(data, player);
			ears.setData(data, player);
			clawsL.setData(data, player);
			clawsR.setData(data, player);
			skirt.setData(data, player);
			horns.setData(data, player);
		}
		breasts.setData(data, player);
		legs.setData(data, player);
	}
	
    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
		if(entityModel != null){
			if(!isArmor){
				entityModel.isChild = entity.isChild();
				entityModel.onGround = onGround;
				entityModel.isRiding = isRiding;
				if(entityModel instanceof ModelBiped){
					ModelBiped biped = (ModelBiped) entityModel;
					biped.aimedBow = aimedBow;
					biped.heldItemLeft = heldItemLeft;
					biped.heldItemRight = heldItemRight;
					biped.isSneak = isSneak;
				}
				entityModel.render(entity, par2, par3, par4, par5, par6, par7);
			}
			return;
		}
    	currentlyPlayerTexture = true;
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);


    	if(data.animation == EnumAnimation.BOW){
    		GL11.glPushMatrix();
    		float ticks = (par1Entity.ticksExisted - data.animationStart) / 10f;
    		if(ticks > 1)
    			ticks = 1;
    		float scale = (2 - data.body.scaleY + data.getLegsY());
    		GL11.glTranslatef(0, 12 * scale * par7, 0);
    		GL11.glRotatef(60 * ticks, 1, 0, 0);
    		GL11.glTranslatef(0, -12 * scale * par7, 0);
    	}
        renderHead(par1Entity, par7);
        renderArms(par1Entity, par7,false);
        renderBody(par1Entity, par7);
		renderCloak(par1Entity, par7);
    	if(data.animation == EnumAnimation.BOW){
    		GL11.glPopMatrix();
    	}
        renderLegs(par1Entity, par7);
    }
    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
    {
		isRiding = data.animation == EnumAnimation.SITTING;
    	
    	if(isSneak && (data.animation == EnumAnimation.CRAWLING || data.isSleeping()))
    		isSneak = false;
    	this.bipedBody.rotationPointZ = 0;
    	this.bipedBody.rotationPointY = 0;
		this.bipedHead.rotateAngleZ = 0;
		this.bipedHeadwear.rotateAngleZ = 0;
		this.bipedLeftLeg.rotateAngleX = 0;
		this.bipedLeftLeg.rotateAngleY = 0;
		this.bipedLeftLeg.rotateAngleZ = 0;
		this.bipedRightLeg.rotateAngleX = 0;
		this.bipedRightLeg.rotateAngleY = 0;
		this.bipedRightLeg.rotateAngleZ = 0;
		this.bipedLeftArm.rotationPointY = 2;
		this.bipedLeftArm.rotationPointZ = 0;
		this.bipedRightArm.rotationPointY = 2;
		this.bipedRightArm.rotationPointZ = 0;
		
    	super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);

		if(!isArmor){
	    	hair.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
	    	beard.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
	    	wings.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
	    	tail.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
	    	skirt.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
		}
    	legs.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
    	
    	if(isSleeping(entity)){
     		if(bipedHead.rotateAngleX < 0){
     			bipedHead.rotateAngleX = 0;
     			bipedHeadwear.rotateAngleX = 0;
     		}
     	}
    	else if(data.animation == EnumAnimation.CRY)
    		bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX = 0.7f;
    	else if(data.animation == EnumAnimation.HUG)
    		AniHug.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
    	else if(data.animation == EnumAnimation.CRAWLING)
    		AniCrawling.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this);
    	else if(data.animation == EnumAnimation.WAVING){
    		bipedRightArm.rotateAngleX = -0.1f;
    		bipedRightArm.rotateAngleY = 0;
    		bipedRightArm.rotateAngleZ = (float) (Math.PI - 1f  - Math.sin(entity.ticksExisted * 0.27f) * 0.5f );
    	}
    	else if(isSneak)
            this.bipedBody.rotateAngleX = 0.5F / data.body.scaleY;
    	
    	
    }

    public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) 
    {
		if(entityModel != null){
			entityModel.setLivingAnimations(entity, par2, par3, par4);
		}
		else if(!isArmor){
	    	ModelPartData partData = data.getPartData(EnumParts.TAIL);
	    	if(partData != null)
	    		tail.setLivingAnimations(partData, par1EntityLivingBase, par2, par3, par4);
		}
    }

    public void loadPlayerTexture(AbstractClientPlayer player){
		if(!isArmor && !currentlyPlayerTexture){
			ClientProxy.bindTexture(player.getLocationSkin());
            currentlyPlayerTexture = true;
		}
    }
    
	private void renderHead(Entity entity, float f) {
		loadPlayerTexture((AbstractClientPlayer) entity);
		
		float x = 0;
		float y = data.getBodyY();
		float z = 0;

		GL11.glPushMatrix();
    	if(data.animation == EnumAnimation.DANCING){
    		float dancing = entity.ticksExisted / 4f;
	        GL11.glTranslatef((float)Math.sin(dancing) * 0.075F, (float)Math.abs(Math.cos(dancing)) * 0.125F - 0.02F, (float)(-Math.abs(Math.cos(dancing))) * 0.075F);   
    	}
		ModelPartConfig head = data.head;

		// Hide Head
		((ModelScaleRenderer)this.bipedHead).isHidden = data.hideHead == 1;
    	
		if(bipedHeadwear.showModel && !bipedHeadwear.isHidden){
			if(data.headwear == 1 || isArmor){
				((ModelScaleRenderer)this.bipedHeadwear).setConfig(head,x,y,z);
				((ModelScaleRenderer)this.bipedHeadwear).render(f);
			}
			else if(data.headwear == 2){
				this.headwear.rotateAngleX = bipedHeadwear.rotateAngleX;
				this.headwear.rotateAngleY = bipedHeadwear.rotateAngleY;
				this.headwear.rotateAngleZ = bipedHeadwear.rotateAngleZ;
				this.headwear.rotationPointX = bipedHeadwear.rotationPointX;
				this.headwear.rotationPointY = bipedHeadwear.rotationPointY;
				this.headwear.rotationPointZ = bipedHeadwear.rotationPointZ;
				this.headwear.setConfig(head,x,y,z);
				this.headwear.render(f);
			}
		}
		((ModelScaleRenderer)this.bipedHead).setConfig(head,x,y,z);
		((ModelScaleRenderer)this.bipedHead).render(f);

		GL11.glPopMatrix();
	}
	
	private void renderBody(Entity entity, float f) {
		loadPlayerTexture((AbstractClientPlayer) entity);
		float x = 0;
		float y = data.getBodyY();
		float z = 0;
		GL11.glPushMatrix();

    	if(data.animation == EnumAnimation.DANCING){
			float dancing = entity.ticksExisted / 4f;
	        GL11.glTranslatef((float)Math.sin(dancing) * 0.015F, 0.0F, 0.0F);
    	}
		
		ModelPartConfig body = data.body;

		// Hide Body
		((ModelScaleRenderer)this.bipedBody).isHidden = data.hideBody == 1;


		// Hide Bodywear
		this.bipedBodywear.isHidden = data.bodywear != 1;

		// Hide Solid Bodywear
		this.bodywear.isHidden = data.bodywear != 2;

//		if(bipedBodywear.showModel && !bipedBodywear.isHidden){
//			if(data.bodywear == 1 || isArmor){
//				((ModelScaleRenderer)this.bipedBodywear).setConfig(data.body,x,y,z);
//				((ModelScaleRenderer)this.bipedBodywear).render(f);
//			}
//			else if(data.bodywear == 2){
//				this.bodywear.rotateAngleX = bipedBodywear.rotateAngleX;
//				this.bodywear.rotateAngleY = bipedBodywear.rotateAngleY;
//				this.bodywear.rotateAngleZ = bipedBodywear.rotateAngleZ;
//				this.bodywear.rotationPointX = bipedBodywear.rotationPointX;
//				this.bodywear.rotationPointY = bipedBodywear.rotationPointY;
//				this.bodywear.rotationPointZ = bipedBodywear.rotationPointZ;
//				this.bodywear.setConfig(data.body,x,y,z);
//				this.bodywear.render(f);
//			}
//		}
    	
		((ModelScaleRenderer)this.bipedBody).setConfig(body,x,y,z);
		((ModelScaleRenderer)this.bipedBody).render(f);
		GL11.glPopMatrix();
		
	}
	public void renderArms(Entity entity, float f, boolean bo){
		loadPlayerTexture((AbstractClientPlayer) entity);
		ModelPartConfig arms = data.arms;

		float x = (1 - data.body.scaleX) * 0.25f + (1 - arms.scaleX) * 0.075f;
		float y = data.getBodyY() + (1 - arms.scaleY) * -0.1f;
		float z = 0;

		GL11.glPushMatrix();

		if (isAlexArmor) {
			GL11.glScalef(0.75F,1.0F,1.0F);
		}

    	if(data.animation == EnumAnimation.DANCING){
			float dancing = entity.ticksExisted / 4f;
	        GL11.glTranslatef((float)Math.sin(dancing) * 0.025F, (float)Math.abs(Math.cos(dancing)) * 0.125F - 0.02F, 0.0F);
    	}

		// Hide Arms
		if(data.hideArms == 1){
			((ModelScaleRenderer)this.bipedRightArm).isHidden = true;
			((ModelScaleRenderer)this.bipedLeftArm).isHidden = true;
		}
		else if(data.hideArms == 2){
			((ModelScaleRenderer)this.bipedRightArm).isHidden = true;
			((ModelScaleRenderer)this.bipedLeftArm).isHidden = false;
		}
		else if(data.hideArms == 3){
			((ModelScaleRenderer)this.bipedRightArm).isHidden = false;
			((ModelScaleRenderer)this.bipedLeftArm).isHidden = true;
		}
		else{
			((ModelScaleRenderer)this.bipedRightArm).isHidden = false;
			((ModelScaleRenderer)this.bipedLeftArm).isHidden = false;
		}

		// Hide Armwear
		if(data.armwear == 1){
			((ModelScaleRenderer)this.bipedRightArmWear).isHidden = data.solidArmwear == 1 || data.solidArmwear == 3;
			((ModelScaleRenderer)this.bipedLeftArmwear).isHidden = data.solidArmwear == 1 || data.solidArmwear == 2;

			this.solidRightArmWear.isHidden = data.solidArmwear == 0 || data.solidArmwear == 2;
			this.solidLeftArmWear.isHidden = data.solidArmwear == 0 || data.solidArmwear == 3;
		}
		else if(data.armwear == 2){
			((ModelScaleRenderer)this.bipedRightArmWear).isHidden = true;
			((ModelScaleRenderer)this.bipedLeftArmwear).isHidden = data.solidArmwear == 1 || data.solidArmwear == 2;

			this.solidRightArmWear.isHidden = true;
			this.solidLeftArmWear.isHidden = data.solidArmwear == 0 || data.solidArmwear == 3;
		}
		else if(data.armwear == 3){
			((ModelScaleRenderer)this.bipedRightArmWear).isHidden = data.solidArmwear == 1 || data.solidArmwear == 3;
			((ModelScaleRenderer)this.bipedLeftArmwear).isHidden = true;

			this.solidRightArmWear.isHidden = data.solidArmwear == 0 || data.solidArmwear == 2;
			this.solidLeftArmWear.isHidden = true;
		}
		else{
			((ModelScaleRenderer)this.bipedRightArmWear).isHidden = true;
			((ModelScaleRenderer)this.bipedLeftArmwear).isHidden = true;
			this.solidRightArmWear.isHidden = true;
			this.solidLeftArmWear.isHidden = true;
		}
		
		if(!bo){
			((ModelScaleRenderer)this.bipedLeftArm).setConfig(arms,-x,y,z);
			((ModelScaleRenderer)this.bipedLeftArm).render(f);
			((ModelScaleRenderer)this.bipedRightArm).setConfig(arms,x,y,z);
			((ModelScaleRenderer)this.bipedRightArm).render(f);
		}
		else{
			((ModelScaleRenderer)this.bipedRightArm).setConfig(arms,0,0,0);
			((ModelScaleRenderer)this.bipedRightArm).render(f);
		}

		GL11.glPopMatrix();
	}
	private void renderLegs(Entity entity, float f) {
		loadPlayerTexture((AbstractClientPlayer) entity);
		ModelPartConfig legs = data.legs;

		float x = (1 - legs.scaleX) * 0.125f;
		float y = data.getLegsY();
		float z = 0;

		GL11.glPushMatrix();

		// Hide Legs
		if(data.hideLegs == 1){
			((ModelScaleRenderer)this.bipedRightLeg).isHidden = true;
			((ModelScaleRenderer)this.bipedLeftLeg).isHidden = true;
		}
		else if(data.hideLegs == 2){
			((ModelScaleRenderer)this.bipedRightLeg).isHidden = true;
			((ModelScaleRenderer)this.bipedLeftLeg).isHidden = false;
		}
		else if(data.hideLegs == 3){
			((ModelScaleRenderer)this.bipedRightLeg).isHidden = false;
			((ModelScaleRenderer)this.bipedLeftLeg).isHidden = true;
		}
		else{
			((ModelScaleRenderer)this.bipedRightLeg).isHidden = false;
			((ModelScaleRenderer)this.bipedLeftLeg).isHidden = false;
		}

		// Hide Legwear
		if(data.legwear == 1){
			((ModelScaleRenderer)this.bipedRightLegWear).isHidden = data.solidLegwear == 1 || data.solidLegwear == 3;
			((ModelScaleRenderer)this.bipedLeftLegWear).isHidden = data.solidLegwear == 1 || data.solidLegwear == 2;

			this.solidRightLegWear.isHidden = data.solidLegwear == 0 || data.solidLegwear == 2;
			this.solidLeftLegWear.isHidden = data.solidLegwear == 0 || data.solidLegwear == 3;
		}
		else if(data.legwear == 2){
			((ModelScaleRenderer)this.bipedRightLegWear).isHidden = true;
			((ModelScaleRenderer)this.bipedLeftLegWear).isHidden = data.solidLegwear == 1 || data.solidLegwear == 2;

			this.solidRightLegWear.isHidden = true;
			this.solidLeftLegWear.isHidden = data.solidLegwear == 0 || data.solidLegwear == 3;
		}
		else if(data.legwear == 3){
			((ModelScaleRenderer)this.bipedRightLegWear).isHidden = data.solidLegwear == 1 || data.solidLegwear == 3;
			((ModelScaleRenderer)this.bipedLeftLegWear).isHidden = true;

			this.solidRightLegWear.isHidden = data.solidLegwear == 0 || data.solidLegwear == 2;
			this.solidLeftLegWear.isHidden = true;
		}
		else{
			((ModelScaleRenderer)this.bipedRightLegWear).isHidden = true;
			((ModelScaleRenderer)this.bipedLeftLegWear).isHidden = true;
			this.solidRightLegWear.isHidden = true;
			this.solidLeftLegWear.isHidden = true;
		}
		
		
		this.legs.setConfig(legs,x,y,z);
		this.legs.render(f);
		if(!isArmor){
			this.tail.setConfig(legs, 0, y, z);
			this.tail.render(f);
		}
		GL11.glPopMatrix();
	}

	public void renderCloak(Entity npc, float f){
		AbstractClientPlayer player = (AbstractClientPlayer) npc;
		if(!player.isInvisible() && !data.cloakUrl.isEmpty() && !isArmor && data.entityClass == null && data.cloak == 1) {
			if(data.cloakTexture != null){
				currentlyPlayerTexture = false;
				Minecraft.getMinecraft().getTextureManager().bindTexture(data.cloakTexture);
				GL11.glPushMatrix();
				GL11.glTranslatef(0.0f, 0.0f, 0.125f);
				final double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * 0.0625 - (player.prevPosX + (player.posX - player.prevPosX) * 0.0625);
				final double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * 0.0625 - (player.prevPosY + (player.posY - player.prevPosY) * 0.0625);
				final double d5 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * 0.0625 - (player.prevPosZ + (player.posZ - player.prevPosZ) * 0.0625);
				final float f4 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * 0.0625f;
				final double d6 = MathHelper.sin(f4 * 3.1415927f / 180.0f);
				final double d7 = -MathHelper.cos(f4 * 3.1415927f / 180.0f);
				float f5 = (float) d4 * 10.0f;
				if (f5 < -6.0f) {
					f5 = -6.0f;
				}
				if (f5 > 32.0f) {
					f5 = 32.0f;
				}
				float f6 = (float) (d3 * d6 + d5 * d7) * 100.0f;
				final float f7 = (float) (d3 * d7 - d5 * d6) * 100.0f;
				if (f6 < 0.0f) {
					f6 = 0.0f;
				}
				final float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * 0.0625f;
				f5 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * 0.0625f) * 6.0f) * 32.0f * f8;
				if (player.isSneaking() && data.animation != EnumAnimation.CRAWLING) {
					f5 += 25.0f;
				}
				GL11.glRotatef(6.0f + f6 / 2.0f + f5, 1.0f, 0.0f, 0.0f);
				GL11.glRotatef(f7 / 2.0f, 0.0f, 0.0f, 1.0f);
				GL11.glRotatef(-f7 / 2.0f, 0.0f, 1.0f, 0.0f);
				GL11.glTranslatef(0.0f, 0.0f, 0.125f);
				GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
				cape.cape.render(0.0625F);
				GL11.glPopMatrix();
			}
		}
	}

	@Override
    public ModelRenderer getRandomModelBox(Random par1Random)
    {
		int random = par1Random.nextInt(5);
		switch(random){
		case 0:
			return bipedRightLeg;
		case 1:
			return bipedHead;
		case 2:
			return bipedLeftArm;
		case 3:
			return bipedRightArm;
		case 4:
			return bipedLeftLeg;
		}

		return bipedBody;
    }

	public boolean isSleeping(Entity entity) {
		if(entity instanceof EntityPlayer && ((EntityPlayer)entity).isPlayerSleeping())
			return true;
		return data.isSleeping();
	}
}
