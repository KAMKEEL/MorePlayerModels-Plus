package noppes.mpm.client.model.part.body;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.model.Model2DRenderer;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelPartInterface;
import noppes.mpm.constants.EnumParts;

public class ModelWings extends ModelPartInterface {

	private Model2DRenderer lWing;
	private Model2DRenderer rWing;
//	private final ModelMonarchWings monarchWings;

	public ModelWings(ModelMPM base) {
		super(base);

		lWing = new Model2DRenderer(base, 48, 32, 16, 32, 64, 32);
		lWing.mirror = true;
		lWing.setRotationPoint(2F, 4, 2F);
		lWing.setRotationOffset(-16, -12);
		setRotation(lWing, 0.7141593F, -0.5235988F, -0.5090659F);
		this.addChild(lWing);

		rWing = new Model2DRenderer(base, 48, 32, 16, 32, 64, 32);
		rWing.setRotationPoint(-2F, 4F, 2F);
		rWing.setRotationOffset(-16, -12);
		setRotation(rWing, 0.7141593F, 0.5235988F, 0.5090659F);
		this.addChild(rWing);

//		monarchWings = new ModelMonarchWings(base);
//		this.addChild(monarchWings);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3,
								  float par4, float par5, float par6, Entity entity) {
		rWing.rotateAngleX = 0.7141593F;
		rWing.rotateAngleZ = 0.5090659F;

		lWing.rotateAngleX = 0.7141593F;
		lWing.rotateAngleZ = -0.5090659F;

//		monarchWings.right_monarch.rotateAngleX = 0.7141593F;
//		monarchWings.right_monarch.rotateAngleZ = 0.5090659F;
//
//		monarchWings.left_monarch.rotateAngleX = 0.7141593F;
//		monarchWings.left_monarch.rotateAngleZ = -0.5090659F;

		float motion = Math.abs(MathHelper.sin(par1 * 0.033F + (float)Math.PI) * 0.4F) * par2;
		if ((!entity.worldObj.isRemote && !entity.onGround) || motion > 0.05D) {
			float speed = (float) (0.55f + 0.5f * motion);
			float y = MathHelper.sin(par3 * 0.67F);

			rWing.rotateAngleZ += y * 0.5f * speed;
			rWing.rotateAngleX += y * 0.5f * speed;

			lWing.rotateAngleZ -= y * 0.5f * speed;
			lWing.rotateAngleX += y * 0.5f * speed;

//			monarchWings.right_monarch.rotateAngleX += y * 0.5f * speed;
//			monarchWings.right_monarch.rotateAngleZ += y * 0.5f * speed;
//
//			monarchWings.left_monarch.rotateAngleX += y * 0.5f * speed;
//			monarchWings.left_monarch.rotateAngleZ -= y * 0.5f * speed;

		} else {
			lWing.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
			rWing.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
			lWing.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
			rWing.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;

//			monarchWings.right_monarch.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
//			monarchWings.right_monarch.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//
//			monarchWings.left_monarch.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
//			monarchWings.left_monarch.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		}
	}

	@Override
	public void initData(ModelData data) {
		ModelPartData config = data.getPartData(EnumParts.WINGS);
		if(config == null)
		{
			isHidden = true;
			return;
		}
		color = config.color;
		isHidden = false;
//		lWing.isHidden = rWing.isHidden = config.type == 15;
//		monarchWings.isHidden = config.type != 15;

		if(!config.playerTexture){
			location = (ResourceLocation) config.getResource();
		}
		else
			location = null;
	}
}
