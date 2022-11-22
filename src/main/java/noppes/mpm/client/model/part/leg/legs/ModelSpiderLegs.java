package noppes.mpm.client.model.part.leg.legs;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import noppes.mpm.ModelData;
import noppes.mpm.constants.EnumAnimation;
import noppes.mpm.constants.EnumParts;

public class ModelSpiderLegs extends ModelRenderer {
	private ModelRenderer spiderLeg1;
    private ModelRenderer spiderLeg2;
    private ModelRenderer spiderLeg3;
    private ModelRenderer spiderLeg4;
    private ModelRenderer spiderLeg5;
    private ModelRenderer spiderLeg6;
    private ModelRenderer spiderLeg7;
    private ModelRenderer spiderLeg8;
    private ModelRenderer spiderBody;
    private ModelRenderer spiderNeck;

	private ModelBiped base;

	public ModelSpiderLegs(ModelBiped base) {
		super(base);
		this.base = base;
        float var1 = 0.0F;
        byte var2 = 15;
        spiderNeck = new ModelRenderer(base, 0, 0);
        spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, var1);
        spiderNeck.setRotationPoint(0.0F, (float)var2, 2.0F);
        this.addChild(spiderNeck);
        
        spiderBody = new ModelRenderer(base, 0, 12);
        spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, var1);
        spiderBody.setRotationPoint(0.0F, (float)var2, 11.0F);
        this.addChild(spiderBody);
        
        this.spiderLeg1 = new ModelRenderer(base, 18, 0);
        this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
        this.spiderLeg1.setRotationPoint(-4.0F, (float)var2, 4.0F);
        this.addChild(spiderLeg1);
        
        this.spiderLeg2 = new ModelRenderer(base, 18, 0);
        this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
        this.spiderLeg2.setRotationPoint(4.0F, (float)var2, 4.0F);
        this.addChild(spiderLeg2);
        
        this.spiderLeg3 = new ModelRenderer(base, 18, 0);
        this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
        this.spiderLeg3.setRotationPoint(-4.0F, (float)var2, 3.0F);
        this.addChild(spiderLeg3);
        
        this.spiderLeg4 = new ModelRenderer(base, 18, 0);
        this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
        this.spiderLeg4.setRotationPoint(4.0F, (float)var2, 3.0F);
        this.addChild(spiderLeg4);
        
        this.spiderLeg5 = new ModelRenderer(base, 18, 0);
        this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
        this.spiderLeg5.setRotationPoint(-4.0F, (float)var2, 2.0F);
        this.addChild(spiderLeg5);
        
        this.spiderLeg6 = new ModelRenderer(base, 18, 0);
        this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
        this.spiderLeg6.setRotationPoint(4.0F, (float)var2, 2.0F);
        this.addChild(spiderLeg6);
        
        this.spiderLeg7 = new ModelRenderer(base, 18, 0);
        this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
        this.spiderLeg7.setRotationPoint(-4.0F, (float)var2, 1.0F);
        this.addChild(spiderLeg7);
        
        this.spiderLeg8 = new ModelRenderer(base, 18, 0);
        this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
        this.spiderLeg8.setRotationPoint(4.0F, (float)var2, 1.0F);
        this.addChild(spiderLeg8);
	}

	public void setRotationAngles(ModelData data, float par1, float par2, float par3, float par4, float par5, float par6, Entity entity){
		this.rotateAngleX = 0;
		spiderBody.rotationPointY = 15;
		spiderBody.rotationPointZ = 11;
		spiderNeck.rotateAngleX = 0;
		
        float var8 = ((float)Math.PI / 4F);
        this.spiderLeg1.rotateAngleZ = -var8;
        this.spiderLeg2.rotateAngleZ = var8;
        this.spiderLeg3.rotateAngleZ = -var8 * 0.74F;
        this.spiderLeg4.rotateAngleZ = var8 * 0.74F;
        this.spiderLeg5.rotateAngleZ = -var8 * 0.74F;
        this.spiderLeg6.rotateAngleZ = var8 * 0.74F;
        this.spiderLeg7.rotateAngleZ = -var8;
        this.spiderLeg8.rotateAngleZ = var8;
        float var9 = -0.0F;
        float var10 = 0.3926991F;
        this.spiderLeg1.rotateAngleY = var10 * 2.0F + var9;
        this.spiderLeg2.rotateAngleY = -var10 * 2.0F - var9;
        this.spiderLeg3.rotateAngleY = var10 * 1.0F + var9;
        this.spiderLeg4.rotateAngleY = -var10 * 1.0F - var9;
        this.spiderLeg5.rotateAngleY = -var10 * 1.0F + var9;
        this.spiderLeg6.rotateAngleY = var10 * 1.0F - var9;
        this.spiderLeg7.rotateAngleY = -var10 * 2.0F + var9;
        this.spiderLeg8.rotateAngleY = var10 * 2.0F - var9;
        float var11 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 0.0F) * 0.4F) * par2;
        float var12 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * par2;
        float var13 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * par2;
        float var14 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + ((float)Math.PI * 3F / 2F)) * 0.4F) * par2;
        float var15 = Math.abs(MathHelper.sin(par1 * 0.6662F + 0.0F) * 0.4F) * par2;
        float var16 = Math.abs(MathHelper.sin(par1 * 0.6662F + (float)Math.PI) * 0.4F) * par2;
        float var17 = Math.abs(MathHelper.sin(par1 * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * par2;
        float var18 = Math.abs(MathHelper.sin(par1 * 0.6662F + ((float)Math.PI * 3F / 2F)) * 0.4F) * par2;
        this.spiderLeg1.rotateAngleY += var11;
        this.spiderLeg2.rotateAngleY += -var11;
        this.spiderLeg3.rotateAngleY += var12;
        this.spiderLeg4.rotateAngleY += -var12;
        this.spiderLeg5.rotateAngleY += var13;
        this.spiderLeg6.rotateAngleY += -var13;
        this.spiderLeg7.rotateAngleY += var14;
        this.spiderLeg8.rotateAngleY += -var14;
        this.spiderLeg1.rotateAngleZ += var15;
        this.spiderLeg2.rotateAngleZ += -var15;
        this.spiderLeg3.rotateAngleZ += var16;
        this.spiderLeg4.rotateAngleZ += -var16;
        this.spiderLeg5.rotateAngleZ += var17;
        this.spiderLeg6.rotateAngleZ += -var17;
        this.spiderLeg7.rotateAngleZ += var18;
        this.spiderLeg8.rotateAngleZ += -var18;

        
        if(base.isSneak){
        	rotationPointZ = 5;
        	rotationPointY = -1;
			spiderBody.rotationPointY = 16;
			spiderBody.rotationPointZ = 10;
			spiderNeck.rotateAngleX = (float) (Math.PI / -8);
        }
        if(((EntityPlayer)entity).isPlayerSleeping() || data.isSleeping() || data.animation == EnumAnimation.CRAWLING){
        	rotationPointY = 12 * data.getPartConfig(EnumParts.LEGS).scaleY;
        	rotationPointZ = 15 * data.getPartConfig(EnumParts.LEGS).scaleY;

			this.rotateAngleX = (float) (Math.PI / -2);
        }
    }

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
