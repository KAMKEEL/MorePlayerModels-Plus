package noppes.mpm.client.model.part.body;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMonarchWings extends ModelRenderer {
	private final ModelRenderer left;
	private final ModelRenderer upper_left;
	private final ModelRenderer upper_left_r1;
	private final ModelRenderer lower_left;
	private final ModelRenderer right;
	private final ModelRenderer upper_right;
	private final ModelRenderer upper_right_r1;
	private final ModelRenderer lower_right;

	public ModelMonarchWings(ModelBiped base) {
		super(base);

		textureWidth = 64;
		textureHeight = 64;

		left = new ModelRenderer(base);
		left.setRotationPoint(1.0F, 24.0F, 0.0F);

		upper_left = new ModelRenderer(base);
		upper_left.setRotationPoint(0.0F, -3.0F, 0.0F);
		left.addChild(upper_left);

		upper_left_r1 = new ModelRenderer(base, 8, 0);
		upper_left_r1.setRotationPoint(1.0F, 6.0F, 0.0F);
		upper_left_r1.addBox(-1.0F, -8.0F, 0.0F, 16, 8, 0, 0.0F);
		upper_left.addChild(upper_left_r1);
		setRotationAngle(upper_left_r1, 0.0F, -0.0873F, 0.0F);

		lower_left = new ModelRenderer(base, 48, 22);
		lower_left.setRotationPoint(0.0F, 0.0F, 0.0F);
		lower_left.addBox(0.0F, -1.0F, 0.0F, 8, 10, 0, 0.0F);
		left.addChild(lower_left);

		right = new ModelRenderer(base);
		right.setRotationPoint(-1.0F, 24.0F, 0.0F);

		upper_right = new ModelRenderer(base);
		upper_right.setRotationPoint(0.0F, -3.0F, 0.0F);
		right.addChild(upper_right);

		upper_right_r1 = new ModelRenderer(base, 8, 0);
		upper_right_r1.setRotationPoint(-1.0F, 6.0F, 0.0F);
		upper_right_r1.addBox(-15.0F, -8.0F, 0.0F, 16, 8, 0, 0.0F);
		upper_right.addChild(upper_right_r1);
		setRotationAngle(upper_right_r1, 0.0F, 0.0873F, 0.0F);

		lower_right = new ModelRenderer(base, 48, 22);
		lower_right.setRotationPoint(0.0F, 0.0F, 0.0F);
		lower_right.addBox(-8.0F, -1.0F, 0.0F, 8, 10, 0, 0.0F);
		right.addChild(lower_right);

		this.addChild(left);
		this.addChild(right);
	}


	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
