package noppes.mpm.client.model.part.leg.tails;

import net.minecraft.client.model.ModelRenderer;
import noppes.mpm.client.model.IModelMPM;

public class ModelRodentTail extends ModelRenderer {
	ModelRenderer Shape1;
	ModelRenderer Shape2;

	public ModelRodentTail(IModelMPM base) {
		super(base.getBiped());
		Shape1 = new ModelRenderer(base.getBiped(), 0, 0);
		Shape1.addBox(-0.5333334F, -0.4666667F, -1F, 1, 1, 6);
		Shape1.setRotationPoint(0F, 0F, 2F);
		setRotation(Shape1, -0.9294653F, 0F, 0F);
		addChild(Shape1);
		
		Shape2 = new ModelRenderer(base.getBiped(), 1, 1);
		Shape2.addBox(-0.5F, -0.1666667F, 1F, 1, 1, 5);
		Shape2.setRotationPoint(0F, 3F, 4F);
		setRotation(Shape2, -0.4833219F, 0F, 0F);
		addChild(Shape2);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
