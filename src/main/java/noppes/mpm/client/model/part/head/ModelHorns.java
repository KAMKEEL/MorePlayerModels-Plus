package noppes.mpm.client.model.part.head;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import noppes.mpm.ModelPartData;
import noppes.mpm.ModelData;
import noppes.mpm.client.model.ModelMPM;
import noppes.mpm.client.model.ModelPartInterface;
import noppes.mpm.client.model.part.head.horns.ModelAntennasBack;
import noppes.mpm.client.model.part.head.horns.ModelAntennasFront;
import noppes.mpm.client.model.part.head.horns.ModelAntlerHorns;
import noppes.mpm.client.model.part.head.horns.ModelBullHorns;
import noppes.mpm.constants.EnumParts;

public class ModelHorns extends ModelPartInterface {
	private ModelRenderer bull;
	private ModelRenderer antlers;
	private ModelRenderer antennasBack;
	private ModelRenderer antennasFront;
	
	public ModelHorns(ModelMPM base) {
		super(base);

		this.addChild(bull = new ModelBullHorns(base));
		this.addChild(antlers = new ModelAntlerHorns(base));
		this.addChild(antennasBack = new ModelAntennasBack(base));
		this.addChild(antennasFront = new ModelAntennasFront(base));
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3,
			float par4, float par5, float par6, Entity entity) {

	}

	@Override
	public void initData(ModelData data) {
		ModelPartData config = data.getPartData(EnumParts.HORNS);
		if(config == null)
		{
			isHidden = true;
			return;
		}
		color = config.color;
		isHidden = false;
		
		bull.isHidden = config.type != 0;
		antlers.isHidden = config.type != 1;
		antennasBack.isHidden = config.type != 2;
		antennasFront.isHidden = config.type != 3;
		
		if(!config.playerTexture){
			location = (ResourceLocation) config.getResource();
		}
		else
			location = null;
	}
}
