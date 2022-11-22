package noppes.mpm.client.model.part.leg.legs;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import noppes.mpm.ModelData;

public class ModelHorseLegs extends ModelRenderer {
    private ModelRenderer backLeftLeg;
    private ModelRenderer backLeftShin;
    private ModelRenderer backLeftHoof;
    
    private ModelRenderer backRightLeg;
    private ModelRenderer backRightShin;
    private ModelRenderer backRightHoof;
    
    private ModelRenderer frontLeftLeg;
    private ModelRenderer frontLeftShin;
    private ModelRenderer frontLeftHoof;
    
    private ModelRenderer frontRightLeg;
    private ModelRenderer frontRightShin;
    private ModelRenderer frontRightHoof;

	private ModelBiped base;

	public ModelHorseLegs(ModelBiped model) {
		super(model);
		this.base = model;

        float var1 = 0.0F;
        byte var2 = 15;
        int zOffset = 10;
        float yOffset = 7f;
        ModelRenderer body = new ModelRenderer(model, 0, 34);
        body.setTextureSize(128, 128);
        body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
        body.setRotationPoint(0.0F, 11.0F + yOffset, 9.0F + zOffset);
        addChild(body);
        
        this.backLeftLeg = new ModelRenderer(model, 78, 29);
        this.backLeftLeg.setTextureSize(128, 128);
        this.backLeftLeg.addBox(-2F, -2.0F, -2.5F, 4, 9, 5);
        this.backLeftLeg.setRotationPoint(4.0F, 9.0F + yOffset, 11.0F + zOffset);
        addChild(backLeftLeg);
        this.backLeftShin = new ModelRenderer(model, 78, 43);
        this.backLeftShin.setTextureSize(128, 128);
        this.backLeftShin.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3);
        this.backLeftShin.setRotationPoint(0F, 7.0F, 0);
        backLeftLeg.addChild(backLeftShin);
        this.backLeftHoof = new ModelRenderer(model, 78, 51);
        this.backLeftHoof.setTextureSize(128, 128);
        this.backLeftHoof.addBox(-2F, 5.0F, -2.0F, 4, 3, 4);
        this.backLeftHoof.setRotationPoint(0F, 7.0F, 0);
        backLeftLeg.addChild(backLeftHoof);
        
        this.backRightLeg = new ModelRenderer(model, 96, 29);
        this.backRightLeg.setTextureSize(128, 128);
        this.backRightLeg.addBox(-2F, -2.0F, -2.5F, 4, 9, 5);
        this.backRightLeg.setRotationPoint(-4.0F, 9.0F + yOffset, 11.0F + zOffset);
        addChild(backRightLeg);
        this.backRightShin = new ModelRenderer(model, 96, 43);
        this.backRightShin.setTextureSize(128, 128);
        this.backRightShin.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3);
        this.backRightShin.setRotationPoint(0F, 7, 0);
        backRightLeg.addChild(backRightShin);
        this.backRightHoof = new ModelRenderer(model, 96, 51);
        this.backRightHoof.setTextureSize(128, 128);
        this.backRightHoof.addBox(-2F, 5.0F, -2.0F, 4, 3, 4);
        this.backRightHoof.setRotationPoint(0F, 7, 0);
        backRightLeg.addChild(backRightHoof);
        
        this.frontLeftLeg = new ModelRenderer(model, 44, 29);
        this.frontLeftLeg.setTextureSize(128, 128);
        this.frontLeftLeg.addBox(-1.4F, -1.0F, -2.1F, 3, 8, 4);
        this.frontLeftLeg.setRotationPoint(4.0F, 9.0F + yOffset, -8.0F + zOffset);
        addChild(frontLeftLeg);
        this.frontLeftShin = new ModelRenderer(model, 44, 41);
        this.frontLeftShin.setTextureSize(128, 128);
        this.frontLeftShin.addBox(-1.4F, 0.0F, -1.6F, 3, 5, 3);
        this.frontLeftShin.setRotationPoint(0F, 7.0F, 0F);
        frontLeftLeg.addChild(frontLeftShin);
        this.frontLeftHoof = new ModelRenderer(model, 44, 51);
        this.frontLeftHoof.setTextureSize(128, 128);
        this.frontLeftHoof.addBox(-1.9F, 5.0F, -2.1F, 4, 3, 4);
        this.frontLeftHoof.setRotationPoint(.0F, 7.0F, 0F);
        frontLeftLeg.addChild(frontLeftHoof);
        
        this.frontRightLeg = new ModelRenderer(model, 60, 29);
        this.frontRightLeg.setTextureSize(128, 128);
        this.frontRightLeg.addBox(-1.6F, -1.0F, -2.1F, 3, 8, 4);
        this.frontRightLeg.setRotationPoint(-4.0F, 9.0F + yOffset, -8.0F + zOffset);
        addChild(frontRightLeg);
        this.frontRightShin = new ModelRenderer(model, 60, 41);
        this.frontRightShin.setTextureSize(128, 128);
        this.frontRightShin.addBox(-1.6F, 0.0F, -1.6F, 3, 5, 3);
        this.frontRightShin.setRotationPoint(0F, 7, 0);
        frontRightLeg.addChild(frontRightShin);
        this.frontRightHoof = new ModelRenderer(model, 60, 51);
        this.frontRightHoof.setTextureSize(128, 128);
        this.frontRightHoof.addBox(-2.1F, 5.0F, -2.1F, 4, 3, 4);
        this.frontRightHoof.setRotationPoint(0F, 7, 0);
        frontRightLeg.addChild(frontRightHoof);
	}

	public void setRotationAngles(ModelData data, float par1, float par2, float par3, float par4, float par5, float par6, Entity entity){
        this.frontLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * .4F * par2;
        this.frontRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * .4F * par2;
        this.backLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * .4F * par2;
        this.backRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * .4F * par2;
    }

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
