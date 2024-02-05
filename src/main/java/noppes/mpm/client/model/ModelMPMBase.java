package noppes.mpm.client.model;

import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import noppes.mpm.*;
import noppes.mpm.client.ClientProxy;
import noppes.mpm.client.model.animation.*;
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

import java.util.Random;

public class ModelMPMBase extends ModelPlayerBase implements IModelMPM {
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
	private ModelScaleRenderer solidLeftArmWear;
	private ModelScaleRenderer solidRightArmWear;
	private ModelScaleRenderer solidLeftLegWear;
	private ModelScaleRenderer solidRightLegWear;

	public boolean currentlyPlayerTexture;

	public boolean isArmor;
	public boolean isAlexArmor;
	public boolean x64 = false;

	public ModelMPMBase(ModelPlayerAPI modelPlayerAPI) {
		super(modelPlayerAPI);
	}

	@Override
	public void beforeRender(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		if (!(var1 instanceof EntityPlayer)) {
			return; // Deal with RenderPlayerAPIEnhancer calling this for skeletons etc
		}

		super.beforeRender(var1, var2, var3, var4, var5, var6, var7);

		if(solidLeftArmWear == null){
			setAlex(1, false);
			data = PlayerDataController.instance.getPlayerData((EntityPlayer) var1);
			setPlayerData(data, (EntityPlayer) var1);
		}
	}

	// Steve 64x64 and Alex 64x64
	public void setAlex(float par1, boolean alex) {
		isArmor = par1 > 0;
		x64 = true;
		float par2 = 0;

		this.modelPlayer.bipedCloak = new ModelRenderer(this.modelPlayer, 0, 0);
		this.modelPlayer.bipedCloak.setTextureSize(64,32);
		this.modelPlayer.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, par1);

		this.cape = new ModelCape(this);

		this.modelPlayer.bipedEars = new ModelRenderer(this.modelPlayer, 24, 0);
		this.modelPlayer.bipedEars.setTextureSize(64, 32);
		this.modelPlayer.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, par1);

		this.modelPlayer.bipedHead = (new ModelScaleRenderer(this.modelPlayer, 0, 0));
		this.modelPlayer.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
		this.modelPlayer.bipedHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		this.modelPlayer.bipedHeadwear = (new ModelScaleRenderer(this.modelPlayer, 32, 0));
		this.modelPlayer.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
		this.modelPlayer.bipedHeadwear.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		this.modelPlayer.bipedBody = (new ModelScaleRenderer(this.modelPlayer, 16, 16));
		this.modelPlayer.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
		this.modelPlayer.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		// New Extension
		this.bipedBodywear = (new ModelScaleRenderer(this.modelPlayer, 16, 32));
		this.bipedBodywear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1 + 0.5F);
		this.modelPlayer.bipedBody.addChild(this.bipedBodywear);
		// this.bipedBodywear.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		bodywear = new ModelBodywear(this.modelPlayer, 64, 64);
		this.modelPlayer.bipedBody.addChild(bodywear);

		// Steve 64x64 Model or Alex 64x64 Model
		if (alex){
			// Alex Version
			this.modelPlayer.bipedRightArm = (new ModelScaleRenderer(this.modelPlayer, 40, 16));
			this.modelPlayer.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, par1);
			// this.bipedRightArm.setRotationPoint(-5.0F, 2.5F + par2, 0.0F);

			this.modelPlayer.bipedLeftArm = new ModelScaleRenderer(this.modelPlayer, 32, 48);
			this.modelPlayer.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, par1);
			this.modelPlayer.bipedLeftArm.setRotationPoint(5.0F, 2.5F + par2, 0.0F);

			this.bipedRightArmWear = (new ModelScaleRenderer(this.modelPlayer, 40, 32));
			this.bipedRightArmWear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, par1 + 0.25F);
			this.modelPlayer.bipedRightArm.addChild(this.bipedRightArmWear);
			this.solidRightArmWear = new ModelLimbWear(this.modelPlayer,"arm","right","Alex");
			this.modelPlayer.bipedRightArm.addChild(this.solidRightArmWear);

			this.bipedLeftArmwear = new ModelScaleRenderer(this.modelPlayer, 48, 48);
			this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, par1 + 0.25F);
			this.modelPlayer.bipedLeftArm.addChild(this.bipedLeftArmwear);
			this.solidLeftArmWear = new ModelLimbWear(this.modelPlayer,"arm","left","Alex");
			this.modelPlayer.bipedLeftArm.addChild(this.solidLeftArmWear);
		}
		else{
			// Steve Version
			this.modelPlayer.bipedRightArm = (new ModelScaleRenderer(this.modelPlayer, 40, 16));
			this.modelPlayer.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1);
			this.modelPlayer.bipedRightArm.setRotationPoint(-5.0F, 2.0F + par2, 0.0F);

			this.modelPlayer.bipedLeftArm = new ModelScaleRenderer(this.modelPlayer, 32, 48);
			this.modelPlayer.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1);
			this.modelPlayer.bipedLeftArm.setRotationPoint(5.0F, 2.0F + par2, 0.0F);

			this.bipedRightArmWear = (new ModelScaleRenderer(this.modelPlayer, 40, 32));
			this.bipedRightArmWear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1 + 0.25F);
			this.modelPlayer.bipedRightArm.addChild(this.bipedRightArmWear);
			this.solidRightArmWear = new ModelLimbWear(this.modelPlayer,"arm","right","Steve");
			this.modelPlayer.bipedRightArm.addChild(this.solidRightArmWear);

			this.bipedLeftArmwear = new ModelScaleRenderer(this.modelPlayer, 48, 48);
			this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1 + 0.25F);
			this.modelPlayer.bipedLeftArm.addChild(this.bipedLeftArmwear);
			this.solidLeftArmWear = new ModelLimbWear(this.modelPlayer,"arm","left","Steve");
			this.modelPlayer.bipedLeftArm.addChild(this.solidLeftArmWear);
		}

		this.modelPlayer.bipedRightLeg = (new ModelScaleRenderer(this.modelPlayer, 0, 16));
		this.modelPlayer.bipedRightLeg.addBox(-2.08F, 0.0F, -2.0F, 4, 12, 4, par1);
		this.modelPlayer.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + par2, 0.0F);

		this.modelPlayer.bipedLeftLeg = new ModelScaleRenderer(this.modelPlayer, 16, 48);
		this.modelPlayer.bipedLeftLeg.addBox(-1.92F, 0.0F, -2.0F, 4, 12, 4, par1);
		this.modelPlayer.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + par2, 0.0F);

		this.bipedRightLegWear = (new ModelScaleRenderer(this.modelPlayer, 0, 32));
		this.bipedRightLegWear.addBox(-2.08F, 0.0F, -2.0F, 4, 12, 4, par1 + 0.25F);
		this.modelPlayer.bipedRightLeg.addChild(this.bipedRightLegWear);
		this.solidRightLegWear = new ModelLimbWear(this.modelPlayer,"leg","right","Steve");
		this.modelPlayer.bipedRightLeg.addChild(this.solidRightLegWear);

		this.bipedLeftLegWear = new ModelScaleRenderer(this.modelPlayer, 0, 48);
		this.bipedLeftLegWear.addBox(-1.92F, 0.0F, -2.0F, 4, 12, 4, par1 + 0.25F);
		this.modelPlayer.bipedLeftLeg.addChild(this.bipedLeftLegWear);
		this.solidLeftLegWear = new ModelLimbWear(this.modelPlayer,"leg","left","Steve");
		this.modelPlayer.bipedLeftLeg.addChild(this.solidLeftLegWear);


		headwear = new ModelHeadwear(this.modelPlayer);
		legs = new ModelLegs(this, (ModelScaleRenderer)this.modelPlayer.bipedRightLeg, (ModelScaleRenderer)this.modelPlayer.bipedLeftLeg, 64, 64);

		this.modelPlayer.bipedBody.addChild(breasts = new ModelBreasts(this, 64, 64));
		if(!isArmor){
			this.modelPlayer.bipedHead.addChild(ears = new ModelEars(this));
			this.modelPlayer.bipedHead.addChild(mohawk = new ModelMohawk(this));
			this.modelPlayer.bipedHead.addChild(hair = new ModelHair(this));
			this.modelPlayer.bipedHead.addChild(beard = new ModelBeard(this));

			// Completed
			this.modelPlayer.bipedHead.addChild(snout = new ModelSnout(this));
			this.modelPlayer.bipedHead.addChild(horns = new ModelHorns(this));

			// Completed
			tail = new ModelTail(this);

			this.modelPlayer.bipedBody.addChild(wings = new ModelWings(this));
			this.modelPlayer.bipedBody.addChild(fin = new ModelFin(this));
			this.modelPlayer.bipedBody.addChild(skirt = new ModelSkirt(this));
			this.modelPlayer.bipedLeftArm.addChild(clawsL = new ModelClaws(this, false));
			this.modelPlayer.bipedRightArm.addChild(clawsR = new ModelClaws(this, true));
		}
	}

	public void setSteve(float par1, int alexArms) {
		isArmor = par1 > 0;
		x64 = false;
		if (isArmor && alexArms == 1) {
			isAlexArmor = true;
		}

		float par2 = 0;

		this.modelPlayer.bipedCloak = new ModelRenderer(this.modelPlayer, 0, 0);
		this.modelPlayer.bipedCloak.setTextureSize(64,32);
		this.modelPlayer.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, par1);

		this.cape = new ModelCape(this);

		this.modelPlayer.bipedEars = new ModelRenderer(this.modelPlayer, 24, 0);
		this.modelPlayer.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, par1);

		this.modelPlayer.bipedHead = (new ModelScaleRenderer(this.modelPlayer, 0, 0));
		this.modelPlayer.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
		this.modelPlayer.bipedHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		this.modelPlayer.bipedHeadwear = (new ModelScaleRenderer(this.modelPlayer, 32, 0));
		this.modelPlayer.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
		this.modelPlayer.bipedHeadwear.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		this.modelPlayer.bipedBody = (new ModelScaleRenderer(this.modelPlayer, 16, 16));
		this.modelPlayer.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
		this.modelPlayer.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		if (alexArms == 0) {
			this.modelPlayer.bipedRightArm = new ModelScaleRenderer(this.modelPlayer, 40, 16);
			this.modelPlayer.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1);
			this.modelPlayer.bipedRightArm.setRotationPoint(-5.0F, 2.0F + par2, 0.0F);

			this.modelPlayer.bipedLeftArm = new ModelScaleRenderer(this.modelPlayer, 40, 16);
			this.modelPlayer.bipedLeftArm.mirror = true;
			this.modelPlayer.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1);
			this.modelPlayer.bipedLeftArm.setRotationPoint(5.0F, 2.0F + par2, 0.0F);
		} else {
			this.modelPlayer.bipedRightArm = (new ModelScaleRenderer(this.modelPlayer, 40, 16));
			this.modelPlayer.bipedLeftArm = new ModelScaleRenderer(this.modelPlayer, 40, 16);
			this.modelPlayer.bipedLeftArm.mirror = true;

			if (isArmor) {
				this.modelPlayer.bipedLeftArm.setRotationPoint(5.0F, 2.5F + par2, 0.0F);
				this.modelPlayer.bipedRightArm.addBox(-4.5F, -2.0F, -2.0F, 4, 12, 4, par1);
				this.modelPlayer.bipedLeftArm.addBox(0.25F, -2.0F, -2.0F, 4, 12, 4, par1);
			} else {
				this.modelPlayer.bipedLeftArm.setRotationPoint(5.0F, 2.5F + par2, 0.0F);
				this.modelPlayer.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, par1);
				this.modelPlayer.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, par1);
			}
		}

		this.modelPlayer.bipedRightLeg = new ModelScaleRenderer(this.modelPlayer, 0, 16);
		this.modelPlayer.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
		this.modelPlayer.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + par2, 0.0F);

		this.modelPlayer.bipedLeftLeg = new ModelScaleRenderer(this.modelPlayer, 0, 16);
		this.modelPlayer.bipedLeftLeg.mirror = true;
		this.modelPlayer.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
		this.modelPlayer.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + par2, 0.0F);

		// Body
		this.bipedBodywear = (new ModelScaleRenderer(this.modelPlayer, 0, 0));
		this.modelPlayer.bipedBody.addChild(this.bipedBodywear);

		bodywear = new ModelBodywear(this.modelPlayer, 0, 0);
		this.modelPlayer.bipedBody.addChild(bodywear);

		// Arms
		this.bipedRightArmWear = (new ModelScaleRenderer(this.modelPlayer, 0, 0));
		this.modelPlayer.bipedRightArm.addChild(this.bipedRightArmWear);
		this.solidRightArmWear = (new ModelScaleRenderer(this.modelPlayer, 0, 0));
		this.modelPlayer.bipedRightArm.addChild(this.solidRightArmWear);
		this.bipedLeftArmwear = new ModelScaleRenderer(this.modelPlayer, 0, 0);
		this.modelPlayer.bipedLeftArm.addChild(this.bipedLeftArmwear);
		this.solidLeftArmWear = (new ModelScaleRenderer(this.modelPlayer, 0, 0));
		this.modelPlayer.bipedLeftArm.addChild(this.solidLeftArmWear);

		// Legs
		this.bipedRightLegWear = (new ModelScaleRenderer(this.modelPlayer, 0, 0));
		this.modelPlayer.bipedRightLeg.addChild(this.bipedRightLegWear);
		this.solidRightLegWear = new ModelScaleRenderer(this.modelPlayer, 0, 0);
		this.modelPlayer.bipedRightLeg.addChild(this.solidRightLegWear);
		this.bipedLeftLegWear = new ModelScaleRenderer(this.modelPlayer, 0, 0);
		this.modelPlayer.bipedLeftLeg.addChild(this.bipedLeftLegWear);
		this.solidLeftLegWear = new ModelScaleRenderer(this.modelPlayer, 0, 0);
		this.modelPlayer.bipedLeftLeg.addChild(this.solidLeftLegWear);

		headwear = new ModelHeadwear(this.modelPlayer, true);
		legs = new ModelLegs(this, (ModelScaleRenderer)this.modelPlayer.bipedRightLeg, (ModelScaleRenderer)this.modelPlayer.bipedLeftLeg, 64, 32);

		this.modelPlayer.bipedBody.addChild(breasts = new ModelBreasts(this, 64, 32));
		if(!isArmor){
			this.modelPlayer.bipedHead.addChild(ears = new ModelEars(this));
			this.modelPlayer.bipedHead.addChild(mohawk = new ModelMohawk(this));
			this.modelPlayer.bipedHead.addChild(hair = new ModelHair(this));
			this.modelPlayer.bipedHead.addChild(beard = new ModelBeard(this));

			// Completed
			this.modelPlayer.bipedHead.addChild(snout = new ModelSnout(this));
			this.modelPlayer.bipedHead.addChild(horns = new ModelHorns(this));

			// Completed
			tail = new ModelTail(this);
			this.modelPlayer.bipedBody.addChild(wings = new ModelWings(this));
			this.modelPlayer.bipedBody.addChild(fin = new ModelFin(this));
			this.modelPlayer.bipedBody.addChild(skirt = new ModelSkirt(this));
			this.modelPlayer.bipedLeftArm.addChild(clawsL = new ModelClaws(this, false));
			this.modelPlayer.bipedRightArm.addChild(clawsR = new ModelClaws(this, true));
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
	public void afterRender(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.afterRender(par1Entity, par2, par3, par4, par5, par6, par7);
		if (!(par1Entity instanceof EntityPlayer) || this.solidLeftArmWear == null) {
			return;
		}

		if(entityModel != null){
			if(!isArmor){
				entityModel.isChild = entity.isChild();
				entityModel.onGround = this.modelPlayer.onGround;
				entityModel.isRiding = this.modelPlayer.isRiding;
				if(entityModel instanceof ModelBiped){
					ModelBiped biped = (ModelBiped) entityModel;
					biped.aimedBow = this.modelPlayer.aimedBow;
					biped.heldItemLeft = this.modelPlayer.heldItemLeft;
					biped.heldItemRight = this.modelPlayer.heldItemRight;
					biped.isSneak = this.modelPlayer.isSneak;
				}
				entityModel.render(entity, par2, par3, par4, par5, par6, par7);
			}
			return;
		}
    	currentlyPlayerTexture = true;
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
        renderHead(par1Entity, par7);
        renderArms(par1Entity, par7,false);
        renderBody(par1Entity, par7);
		renderCloak(par1Entity, par7);
        renderLegs(par1Entity, par7);
    }
    @Override
	public void afterSetRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
		super.afterSetRotationAngles(par1, par2, par3, par4, par5, par6, entity);
		if (!(entity instanceof EntityPlayer) || this.solidLeftArmWear == null) {
			return;
		}

		EntityPlayer player = (EntityPlayer) entity;
		data = PlayerDataController.instance.getPlayerData(player);
		// Fixes Sitting Animation when Disabling Sitting
		if(data.didSit && data.animation != EnumAnimation.SITTING){
			this.modelPlayer.isRiding = false;
			data.didSit = false;
		}

		if(!this.modelPlayer.isRiding)
			this.modelPlayer.isRiding = data.animation == EnumAnimation.SITTING;

		if(this.modelPlayer.isSneak && (data.animation == EnumAnimation.CRAWLING || data.isSleeping()))
			this.modelPlayer.isSneak = false;

		this.modelPlayer.bipedBody.rotationPointZ = 0;
		this.modelPlayer.bipedBody.rotationPointY = 0;
		this.modelPlayer.bipedHead.rotateAngleZ = 0;
		this.modelPlayer.bipedHeadwear.rotateAngleZ = 0;

		this.modelPlayer.bipedLeftLeg.rotateAngleX = 0;
		this.modelPlayer.bipedLeftLeg.rotateAngleY = 0;
		this.modelPlayer.bipedLeftLeg.rotateAngleZ = 0;
		this.modelPlayer.bipedRightLeg.rotateAngleX = 0;
		this.modelPlayer.bipedRightLeg.rotateAngleY = 0;
		this.modelPlayer.bipedRightLeg.rotateAngleZ = 0;
		this.modelPlayer.bipedLeftArm.rotationPointY = 2;
		this.modelPlayer.bipedLeftArm.rotationPointZ = 0;
		this.modelPlayer.bipedRightArm.rotationPointY = 2;
		this.modelPlayer.bipedRightArm.rotationPointZ = 0;

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
			if(this.modelPlayer.bipedHead.rotateAngleX < 0){
				this.modelPlayer.bipedHead.rotateAngleX = 0;
				this.modelPlayer.bipedHeadwear.rotateAngleX = 0;
			}
		}
		else if(data.animation == EnumAnimation.CRY)
			this.modelPlayer.bipedHeadwear.rotateAngleX = this.modelPlayer.bipedHead.rotateAngleX = 0.7f;
		else if(data.animation == EnumAnimation.HUG)
			AniHug.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this.modelPlayer);
		else if(data.animation == EnumAnimation.CRAWLING)
			AniCrawling.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this.modelPlayer);
		else if(data.animation == EnumAnimation.WAVING){
			AniWaving.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this.modelPlayer);
		}
		else if(data.animation == EnumAnimation.DANCING){
			AniDancing.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this.modelPlayer);
		}
		else if(data.animation == EnumAnimation.BOW){
			AniBow.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this.modelPlayer, data);
		}
		else if(data.animation == EnumAnimation.YES){
			AniYes.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this.modelPlayer, data);
		}
		else if(data.animation == EnumAnimation.NO){
			AniNo.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this.modelPlayer, data);
		}
		else if(data.animation == EnumAnimation.POINT){
			AniPoint.setRotationAngles(par1, par2, par3, par4, par5, par6, entity, this.modelPlayer);
		}
		else if(this.modelPlayer.isSneak)
			this.modelPlayer.bipedBody.rotateAngleX = 0.5F / data.body.scaleY;
	}

	@Override
    public void afterSetLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
    {
		if (!(par1EntityLivingBase instanceof EntityPlayer) || this.solidLeftArmWear == null) {
			return;
		}

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
		((ModelScaleRenderer)this.modelPlayer.bipedHead).isHidden = data.hideHead == 1;
    	
		if(this.modelPlayer.bipedHeadwear.showModel && !this.modelPlayer.bipedHeadwear.isHidden){
			if(data.headwear == 1 || isArmor){
				((ModelScaleRenderer)this.modelPlayer.bipedHeadwear).setConfig(head,x,y,z);
				((ModelScaleRenderer)this.modelPlayer.bipedHeadwear).render(f);
			}
			else if(data.headwear == 2){
				this.headwear.rotateAngleX = this.modelPlayer.bipedHeadwear.rotateAngleX;
				this.headwear.rotateAngleY = this.modelPlayer.bipedHeadwear.rotateAngleY;
				this.headwear.rotateAngleZ = this.modelPlayer.bipedHeadwear.rotateAngleZ;
				this.headwear.rotationPointX = this.modelPlayer.bipedHeadwear.rotationPointX;
				this.headwear.rotationPointY = this.modelPlayer.bipedHeadwear.rotationPointY;
				this.headwear.rotationPointZ = this.modelPlayer.bipedHeadwear.rotationPointZ;
				this.headwear.setConfig(head,x,y,z);
				this.headwear.render(f);
			}
		}
		((ModelScaleRenderer)this.modelPlayer.bipedHead).setConfig(head,x,y,z);
		((ModelScaleRenderer)this.modelPlayer.bipedHead).render(f);

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
		((ModelScaleRenderer)this.modelPlayer.bipedBody).isHidden = data.hideBody == 1;


		// Hide Bodywear
		this.bipedBodywear.isHidden = data.bodywear != 1;

		// Hide Solid Bodywear
		this.bodywear.isHidden = data.bodywear != 2;
    	
		((ModelScaleRenderer)this.modelPlayer.bipedBody).setConfig(body,x,y,z);
		((ModelScaleRenderer)this.modelPlayer.bipedBody).render(f);
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
			((ModelScaleRenderer)this.modelPlayer.bipedRightArm).isHidden = true;
			((ModelScaleRenderer)this.modelPlayer.bipedLeftArm).isHidden = true;
		}
		else if(data.hideArms == 2){
			((ModelScaleRenderer)this.modelPlayer.bipedRightArm).isHidden = true;
			((ModelScaleRenderer)this.modelPlayer.bipedLeftArm).isHidden = false;
		}
		else if(data.hideArms == 3){
			((ModelScaleRenderer)this.modelPlayer.bipedRightArm).isHidden = false;
			((ModelScaleRenderer)this.modelPlayer.bipedLeftArm).isHidden = true;
		}
		else{
			((ModelScaleRenderer)this.modelPlayer.bipedRightArm).isHidden = false;
			((ModelScaleRenderer)this.modelPlayer.bipedLeftArm).isHidden = false;
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
			((ModelScaleRenderer)this.modelPlayer.bipedLeftArm).setConfig(arms,-x,y,z);
			((ModelScaleRenderer)this.modelPlayer.bipedLeftArm).render(f);
			((ModelScaleRenderer)this.modelPlayer.bipedRightArm).setConfig(arms,x,y,z);
			((ModelScaleRenderer)this.modelPlayer.bipedRightArm).render(f);
		}
		else{
			((ModelScaleRenderer)this.modelPlayer.bipedRightArm).setConfig(arms,0,0,0);
			((ModelScaleRenderer)this.modelPlayer.bipedRightArm).render(f);
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
			((ModelScaleRenderer)this.modelPlayer.bipedRightLeg).isHidden = true;
			((ModelScaleRenderer)this.modelPlayer.bipedLeftLeg).isHidden = true;
		}
		else if(data.hideLegs == 2){
			((ModelScaleRenderer)this.modelPlayer.bipedRightLeg).isHidden = true;
			((ModelScaleRenderer)this.modelPlayer.bipedLeftLeg).isHidden = false;
		}
		else if(data.hideLegs == 3){
			((ModelScaleRenderer)this.modelPlayer.bipedRightLeg).isHidden = false;
			((ModelScaleRenderer)this.modelPlayer.bipedLeftLeg).isHidden = true;
		}
		else{
			((ModelScaleRenderer)this.modelPlayer.bipedRightLeg).isHidden = false;
			((ModelScaleRenderer)this.modelPlayer.bipedLeftLeg).isHidden = false;
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
			if(data.cloakObject != null){
				currentlyPlayerTexture = false;
				Minecraft.getMinecraft().getTextureManager().bindTexture(data.cloakObject);
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
			return this.modelPlayer.bipedRightLeg;
		case 1:
			return this.modelPlayer.bipedHead;
		case 2:
			return this.modelPlayer.bipedLeftArm;
		case 3:
			return this.modelPlayer.bipedRightArm;
		case 4:
			return this.modelPlayer.bipedLeftLeg;
		}

		return this.modelPlayer.bipedBody;
    }

	public boolean isSleeping(Entity entity) {
		if(entity instanceof EntityPlayer && ((EntityPlayer)entity).isPlayerSleeping())
			return true;
		return data.isSleeping();
	}

	@Override
	public boolean getCurrentlyPlayerTexture() {
		return currentlyPlayerTexture;
	}

	@Override
	public void setCurrentlyPlayerTexture(boolean value) {
		currentlyPlayerTexture = value;
	}

	@Override
	public boolean getIsArmor() {
		return isArmor;
	}

	@Override
	public boolean getIsAlexArmor() {
		return isAlexArmor;
	}

	@Override
	public boolean getX64() {
		return x64;
	}

	@Override
	public ModelBiped getBiped() {
		return this.modelPlayer;
	}
}
