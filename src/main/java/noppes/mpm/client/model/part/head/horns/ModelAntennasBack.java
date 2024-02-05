package noppes.mpm.client.model.part.head.horns;

import net.minecraft.client.model.ModelRenderer;
import noppes.mpm.client.model.IModelMPM;

public class ModelAntennasBack extends ModelRenderer {

	public ModelAntennasBack(IModelMPM base) {
		super(base.getBiped());
		ModelRenderer rightantenna1 = new ModelRenderer(base.getBiped(), 36, 0);
		rightantenna1.addBox(-1F, 0F, 0F, 1, 4, 1);
		rightantenna1.setRotationPoint(3F, -10.9F, 0F);
		setRotation(rightantenna1, -0.7504916F, 0.0698132F, 0.0698132F);
		addChild(rightantenna1);
		
		ModelRenderer leftantenna1 = new ModelRenderer(base.getBiped(), 32, 0);
		leftantenna1.mirror = true;
		leftantenna1.addBox(0F, 0F, 0F, 1, 4, 1);
		leftantenna1.setRotationPoint(-3F, -10.9F, 0F);
		setRotation(leftantenna1, -0.7504916F, -0.0698132F, -0.0698132F);
		addChild(leftantenna1);
		
		ModelRenderer rightantenna2 = new ModelRenderer(base.getBiped(), 36, 0);
		rightantenna2.addBox(-1F, 0F, 0F, 1, 4, 1);
		rightantenna2.setRotationPoint(4.6F, -12.2F, 3.4F);
		setRotation(rightantenna2, -1.22173F, 0.4363323F, 0.0698132F);
		addChild(rightantenna2);
		
		ModelRenderer leftantenna2 = new ModelRenderer(base.getBiped(), 32, 0);
		leftantenna2.mirror = true;
		leftantenna2.addBox(0F, 0F, 0F, 1, 4, 1);
		leftantenna2.setRotationPoint(-4.6F, -12.2F, 3.4F);
		setRotation(leftantenna2, -1.22173F, -0.4363323F, -0.0698132F);
		addChild(leftantenna2);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
