package noppes.mpm.client.gui;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.mpm.ModelData;
import noppes.mpm.ModelPartData;
import noppes.mpm.client.controller.ClientPermController;
import noppes.mpm.client.gui.util.*;
import noppes.mpm.client.model.part.head.ModelHalo;
import noppes.mpm.constants.EnumParts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class GuiCreationParts extends GuiCreationScreenInterface implements ITextfieldListener, ICustomScrollListener{
	private GuiCustomScroll scroll;
	private ArrayList<GuiPart> partList = new ArrayList<GuiPart>();

	private static int selected = 0;

	public GuiCreationParts(){
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_BREAST)){
			partList.add(new GuiPartBreasts());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_WINGS)){
			partList.add(new GuiPart(EnumParts.WINGS).setTypes(new String[]{"gui.none","1","2","3","4","5","6","7","8","9",
					"10","11","12","13","14","15"}));
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_CAPE)){
			partList.add(new GuiPartCape());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_FIN)){
			partList.add(new GuiPart(EnumParts.FIN).setTypes(new String[]{"gui.none", "1","2","3","4","5","6"}));
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_PARTICLES)){
			partList.add(new GuiPartParticles());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_LEGS)){
			partList.add(new GuiPartLegs());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_TAIL)){
			partList.add(new GuiPartTail());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_SNOUT)){
			partList.add(new GuiPartSnout());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_EARS)){
			partList.add(new GuiPart(EnumParts.EARS).setTypes(new String[]{"gui.none", "gui.normal", "ears.bunny"}));
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_HORNS)){
			partList.add(new GuiPartHorns());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_HALO)){
			partList.add(new GuiPartHalo().setTypes(new String[]{"gui.none", "gui.halo.base", "gui.halo.thin"}));
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_HAIR)){
			partList.add(new GuiPartHair());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_MOHAWK)){
			partList.add(new GuiPart(EnumParts.MOHAWK).setTypes(new String[]{"gui.none", "1", "2"}).noPlayerOptions());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_BEARD)){
			partList.add(new GuiPartBeard());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_SKIRT)){
			partList.add(new GuiPart(EnumParts.SKIRT).setTypes(new String[]{"gui.none", "gui.normal"}).noPlayerOptions());
		}
		if(ClientPermController.hasPermission(MorePlayerModelsPermissions.PARTS_CLAWS)){
			partList.add(new GuiPartClaws());
		}

		active = 2;
		partList.sort(new Comparator<GuiPart>(){
			@Override
			public int compare(GuiPart o1, GuiPart o2) {
				String s1 = StatCollector.translateToLocal("part." + o1.part.name);
				String s2 = StatCollector.translateToLocal("part." + o2.part.name);
				return s1.compareToIgnoreCase(s2);
			}

		});
	}

	@Override
	public void initGui() {
		super.initGui();
		if(entity != null){
			openGui(new GuiCreationExtra());
			return;
		}

		if(scroll == null){
			List<String> list = new ArrayList<String>();
			for(GuiPart part : partList)
				list.add(StatCollector.translateToLocal("part." + part.part.name));
			scroll = new GuiCustomScroll(this, 0);
			scroll.setUnsortedList(list);
		}
		scroll.guiLeft = guiLeft;
		scroll.guiTop = guiTop + 46;
		scroll.setSize(100, ySize - 74);

		addScroll(scroll);


		if(partList.get(selected) != null){
			scroll.setSelected(StatCollector.translateToLocal("part." + partList.get(selected).part.name));
			partList.get(selected).initGui();
		}
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		super.actionPerformed(btn);
		if(partList.get(selected) != null){
			partList.get(selected).actionPerformed(btn);
		}
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 23){

		}
		if(partList.get(selected) != null){
			partList.get(selected).unFocused(textfield);
		}
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if(scroll.selected >= 0){
			selected = scroll.selected;
			initGui();
		}
	}
	class GuiPart{
		EnumParts part;
		protected String[] types = {"gui.none"};
		protected ModelPartData data;
		protected boolean hasPlayerOption = true;
		protected boolean noPlayerTypes = false;
		protected boolean noPlayerTextures = false;
		protected boolean canBeDeleted = true;

		public GuiPart(EnumParts part){
			this.part = part;
			data = playerdata.getPartData(part);
		}

		public int initGui(){
			data = playerdata.getPartData(part);
			int y = guiTop + 50;
			if(data == null || !data.playerTexture || !noPlayerTypes){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(20, "gui.type", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(20, guiLeft + 145, y, 100, 20, types, data == null?0:data.type + 1));
				y += 25;
			}
			if(data != null && hasPlayerOption){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(21, "gui.playerskin", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiNpcButtonYesNo(21, guiLeft + 170, y, data.playerTexture));
				y += 25;
			}
			if(data != null && !data.playerTexture && !noPlayerTextures){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(23, "gui.color", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiColorButton(23, guiLeft + 170, y, data.color));
				y += 25;
			}
			return y;
		}

		protected void actionPerformed(GuiButton btn) {
			if(btn.id == 20){
				int i = ((GuiNpcButton)btn).getValue();
				if(i == 0 && canBeDeleted)
					playerdata.removePart(part);
				else{
					data = playerdata.getOrCreatePart(part);
					data.setCustomResource("");
					data.playerTexture = false;
					data.pattern = 0;
					data.setType(i - 1);
				}
				GuiCreationParts.this.initGui();
			}
			if(btn.id == 22){
				data.pattern = (byte) ((GuiNpcButton)btn).getValue();
			}
			if(btn.id == 21){
				data.playerTexture = ((GuiNpcButtonYesNo)btn).getBoolean();
				if(data.playerTexture){
					data.setCustomResource("");
				}
				data.color = 0xFFFFFF;
				GuiCreationParts.this.initGui();
			}
			if(btn.id == 23){
				setSubGui(new GuiModelColor(GuiCreationParts.this, () -> data.color, v -> data.color = v));
			}
		}
		public GuiPart noPlayerOptions(){
			hasPlayerOption = false;
			return this;
		}

		public GuiPart noPlayerTypes(){
			noPlayerTypes = true;
			return this;
		}

		public GuiPart setTypes(String[] types){
			this.types = types;
			return this;
		}

		protected void unFocused(GuiNpcTextField textfield) {

		}
	}
	class GuiPartTail extends GuiPart{
		public GuiPartTail() {
			super(EnumParts.TAIL);
			types = new String[]{"gui.none", "part.tail", "tail.dragon",
					"tail.horse", "tail.squirrel", "tail.fin", "tail.rodent", "tail.feather", "tail.fox", "tail.monkey"};
		}

		@Override
		public int initGui(){
			data = playerdata.getPartData(part);
			hasPlayerOption = data != null && (data.type == 0 || data.type == 1 || data.type == 6 || data.type == 7);
			int y = super.initGui();
			if(data != null && data.type == 0){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(22, "gui.pattern", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(22, guiLeft + 145, y, 100, 20, new String[]{"tail.wolf", "tail.cat"}, data.pattern));
			}
			if(data != null && (data.type == 8 || data.type == 9)){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(22, "gui.pattern", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(22, guiLeft + 145, y, 100, 20, new String[]{"tail.normal", "tail.wrapped", "tail.large"}, data.pattern));
			}
			return y;
		}

		@Override
		protected void actionPerformed(GuiButton btn) {
			super.actionPerformed(btn);
			if(btn.id == 22){
				if(data != null){
					if(data.type == 0){
						if(data.pattern == 1){
							data.setCustomResource("0-1");
						} else {
							data.setCustomResource("");
						}
						data.updateTextureLocation();
					}
				}
			}
		}
	}

	class GuiPartBreasts extends GuiPart{
		public GuiPartBreasts() {
			super(EnumParts.BREASTS);
			hasPlayerOption = false;
			types = new String[]{"gui.none", "1", "2", "3"};
		}

		@Override
		public int initGui(){
			data = playerdata.getPartData(part);
			int y = guiTop + 50;
			if(data == null || !data.playerTexture || !noPlayerTypes){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(20, "gui.type", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(20, guiLeft + 145, y, 100, 20, types, data == null?0:data.type + 1));
				y += 25;
			}
			return y;
		}

		@Override
		protected void actionPerformed(GuiButton btn) {
			if(btn.id == 20){
				int i = ((GuiNpcButton)btn).getValue();
				playerdata.breasts = (byte)i;
			}
			super.actionPerformed(btn);
		}
	}

	class GuiPartParticles extends GuiPart{
		public GuiPartParticles() {
			super(EnumParts.PARTICLES);
			hasPlayerOption = data != null && data.type != 3;
			types = new String[]{"gui.none", "1", "2", "Rainbow", "3", "4", "5", "6", "7"};
		}

		@Override
		public int initGui(){
			int y = super.initGui();
			if(data == null)
				return y;
			return y;
		}
	}

	class GuiPartHorns extends GuiPart{
		public GuiPartHorns() {
			super(EnumParts.HORNS);
			types = new String[]{"gui.none", "horns.bull", "horns.antlers", "horns.antenna"};
		}

		@Override
		public int initGui(){
			int y = super.initGui();
			if(data != null && data.type == 2){
				GuiCreationParts.this.addLabel(new GuiNpcLabel(22, "gui.pattern", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(22, guiLeft + 145, y, 100, 20, new String[]{"1","2"}, data.pattern));
			}
			return y;
		}
	}
	class GuiPartHalo extends GuiPart{
		private int page = 0;
		public GuiPartHalo() {
			super(EnumParts.HALO);
			noPlayerOptions();
		}

		@Override
		public int initGui() {
			data = playerdata.getPartData(part);
			int y = guiTop + 50;
			if(data == null || !data.playerTexture || !noPlayerTypes) {
				GuiCreationParts.this.addLabel(new GuiNpcLabel(20, "gui.type", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(20, guiLeft + 145, y, 100, 20, types, data == null ? 0 : data.type + 1));
				y += 24;
			}
			if(data != null && !data.playerTexture && !noPlayerTextures) {
				GuiCreationParts.this.addLabel(new GuiNpcLabel(23, "gui.color", guiLeft + 102, y + 5, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiColorButton(23, guiLeft + 150, y, data.color));
				y += 24;
			}
			if(data != null && data.type >= 1) {
				GuiCreationParts.this.addLabel(new GuiNpcLabel(58, "gui.page", guiLeft + 102, guiTop + 55 + 48 + 4 * 23, 0xFFFFFF));
				GuiCreationParts.this.addButton(new GuiButtonBiDirectional(58, guiLeft + 155, guiTop + 50 + 48 + 4 * 23, 80, 20, new String[] {"1", "2", "3"}, page));

				int btnWidth = fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.halo.reset")) + 10;
				switch (page) {
					case 0: {
						GuiCreationParts.this.addButton(new GuiButtonBiDirectional(57, guiLeft + 215, y - 25, 60, 20, new String[]{"Off", "Lit", "Solid"}, ModelHalo.getMaterial(data)));

						GuiNpcSlider slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 49, guiLeft + 112, y, 120, 20, ModelHalo.getWidth(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.width"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(50, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;

						slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 53, guiLeft + 112, y, 120, 20, ModelHalo.getRotationX(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.rotationX"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(54, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;

						slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 51, guiLeft + 112, y, 120, 20, ModelHalo.getRotationY(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.rotationY"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(52, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;

						slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 55, guiLeft + 112, y, 120, 20, ModelHalo.getRotationZ(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.rotationZ"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(56, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;
						break;
					}
					case 1: {
						GuiNpcSlider slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 62, guiLeft + 112, y, 120, 20, ModelHalo.getFloatingSpeed(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.floating_speed"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(63, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;

						slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 64, guiLeft + 112, y, 120, 20, ModelHalo.getFloatingDistance(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.floating_distance"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(65, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;

						// Avoid 66 id, it closes gui
						slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 67, guiLeft + 112, y, 120, 20, ModelHalo.getSpinSpeed(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.spin_speed"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(68, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;

						break;
					}
					case 2: {
						GuiNpcSlider slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 69, guiLeft + 112, y, 120, 20, ModelHalo.getOffsetX(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.offsetX"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(70, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;

						slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 71, guiLeft + 112, y, 120, 20, ModelHalo.getOffsetY(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.offsetY"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(72, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;

						slider = new GuiNpcSlider(new FakeGui(this::actionPerformed), 73, guiLeft + 112, y, 120, 20, ModelHalo.getOffsetZ(data));
						slider.setString(StatCollector.translateToLocal("gui.halo.offsetZ"));
						GuiCreationParts.this.addSlider(slider);
						GuiCreationParts.this.addButton(new GuiNpcButton(74, guiLeft + 132 + 105, y, btnWidth, 20, "gui.halo.reset"));
						y += 23;
						break;
					}
				}
			}
			return y;
		}

		@Override
		protected void actionPerformed(GuiButton btn) {
            switch (btn.id) {
                case 49:
					ModelHalo.setWidth(data, ((GuiNpcSlider) btn).sliderValue);
                    break;
                case 50:
					ModelHalo.setWidth(data, 0.5F);
					GuiCreationParts.this.getSlider(49).sliderValue = ModelHalo.getWidth(data);
                    break;
                case 51:
					ModelHalo.setRotationY(data, ((GuiNpcSlider) btn).sliderValue);
                    break;
                case 52:
					ModelHalo.setRotationY(data, 0.5F);
					GuiCreationParts.this.getSlider(51).sliderValue = ModelHalo.getRotationY(data);
                    break;
				case 53:
					ModelHalo.setRotationX(data, ((GuiNpcSlider) btn).sliderValue);
					break;
				case 54:
					ModelHalo.setRotationX(data, 0.5F);
					GuiCreationParts.this.getSlider(53).sliderValue = ModelHalo.getRotationX(data);
					break;
				case 55:
					ModelHalo.setRotationZ(data, ((GuiNpcSlider) btn).sliderValue);
					break;
				case 56:
					ModelHalo.setRotationZ(data, 0.5F);
					GuiCreationParts.this.getSlider(55).sliderValue = ModelHalo.getRotationZ(data);
					break;
				case 57:
					ModelHalo.setMaterial(data, (byte) ((GuiNpcButton) btn).getValue());
					break;
				case 58:
					page = ((GuiNpcButton)btn).getValue();
					GuiCreationParts.this.initGui();
					break;
				case 62:
					ModelHalo.setFloatingSpeed(data, ((GuiNpcSlider) btn).sliderValue);
					break;
				case 63:
					ModelHalo.setFloatingSpeed(data, 0.5F);
					GuiCreationParts.this.getSlider(62).sliderValue = ModelHalo.getFloatingSpeed(data);
					break;
				case 64:
					ModelHalo.setFloatingDistance(data, ((GuiNpcSlider) btn).sliderValue);
					break;
				case 65:
					ModelHalo.setFloatingDistance(data, 0.5F);
					GuiCreationParts.this.getSlider(64).sliderValue = ModelHalo.getFloatingDistance(data);
					break;
				case 67:
					ModelHalo.setSpinSpeed(data, ((GuiNpcSlider) btn).sliderValue);
					break;
				case 68:
					ModelHalo.setSpinSpeed(data, 0.5F);
					GuiCreationParts.this.getSlider(67).sliderValue = ModelHalo.getSpinSpeed(data);
					break;
				case 69:
					ModelHalo.setOffsetX(data, ((GuiNpcSlider) btn).sliderValue);
					break;
				case 70:
					ModelHalo.setOffsetX(data, 0.5F);
					GuiCreationParts.this.getSlider(69).sliderValue = ModelHalo.getOffsetX(data);
					break;
				case 71:
					ModelHalo.setOffsetY(data, ((GuiNpcSlider) btn).sliderValue);
					break;
				case 72:
					ModelHalo.setOffsetY(data, 0.5F);
					GuiCreationParts.this.getSlider(71).sliderValue = ModelHalo.getOffsetY(data);
					break;
				case 73:
					ModelHalo.setOffsetZ(data, ((GuiNpcSlider) btn).sliderValue);
					break;
				case 74:
					ModelHalo.setOffsetZ(data, 0.5F);
					GuiCreationParts.this.getSlider(73).sliderValue = ModelHalo.getOffsetZ(data);
					break;
                case 20:
                    int i = ((GuiNpcButton) btn).getValue();
                    if (i == 0 && canBeDeleted)
                        playerdata.removePart(part);
                    else {
                        data = playerdata.getOrCreatePart(part);
                        data.setCustomResource("");
                        data.playerTexture = false;
						data.customData = new NBTTagCompound();
                        data.setType(i - 1);
                    }
                    GuiCreationParts.this.initGui();
                    break;
				default:
					super.actionPerformed(btn);
					break;
            }
		}

		class FakeGui extends GuiScreen implements ISliderListener {
			private final Consumer<GuiNpcSlider> listener;
			private FakeGui(Consumer<GuiNpcSlider> listener) {
				this.listener = listener;
			}
			@Override
			public void mouseDragged(GuiNpcSlider guiNpcSlider) {
				listener.accept(guiNpcSlider);
			}

			@Override
			public void mousePressed(GuiNpcSlider guiNpcSlider) {}

			@Override
			public void mouseReleased(GuiNpcSlider guiNpcSlider) {}
		}
	}
	class GuiPartHair extends GuiPart{
		public GuiPartHair() {
			super(EnumParts.HAIR);
			types = new String[]{"gui.none", "1", "2", "3", "4"};
			noPlayerTypes();
		}
	}
	class GuiPartSnout extends GuiPart{
		public GuiPartSnout() {
			super(EnumParts.SNOUT);
			types = new String[]{"gui.none", "snout.small", "snout.medium", "snout.large", "snout.bunny", "snout.beak"};
		}
	}
	class GuiPartBeard extends GuiPart{
		public GuiPartBeard() {
			super(EnumParts.BEARD);
			types = new String[]{"gui.none", "1", "2", "3", "4"};
			noPlayerTypes();
		}
	}
	class GuiPartClaws extends GuiPart{
		public GuiPartClaws() {
			super(EnumParts.CLAWS);
			types = new String[]{"gui.none", "gui.show"};
		}

		@Override
		public int initGui(){
			int y = super.initGui();
			if(data == null)
				return y;
			GuiCreationParts.this.addLabel(new GuiNpcLabel(22, "gui.pattern", guiLeft + 102, y + 5, 0xFFFFFF));
			GuiCreationParts.this.addButton(new GuiButtonBiDirectional(22, guiLeft + 145, y, 100, 20, new String[]{"gui.both","gui.left","gui.right"}, data.pattern));
			return y;
		}
	}
	class GuiPartCape extends GuiPart{
		public GuiPartCape() {
			super(EnumParts.CAPE);
			types = new String[]{"gui.none", "gui.show"};
			hasPlayerOption = false;
			noPlayerTextures = true;
		}

		@Override
		public int initGui(){
			int y = super.initGui();
			if(data == null)
				return y;
			y += 5;
			GuiCreationParts.this.addLabel(new GuiNpcLabel(300, "config.capeurl", guiLeft + 102, y + 5, 0xFFFFFF));
			GuiCreationParts.this.addTextField(new GuiNpcTextField(300, GuiCreationParts.this, guiLeft + 155, y, 120, 20, playerdata.cloakUrl));
			return y;
		}

		@Override
		protected void actionPerformed(GuiButton btn) {
			if(btn.id == 20){
				int i = ((GuiNpcButton)btn).getValue();
				playerdata.cloakUrl = "";
				playerdata.cloak = (byte)i;
			}
			super.actionPerformed(btn);
		}

		@Override
		public void unFocused(GuiNpcTextField guiNpcTextField) {
			if(guiNpcTextField.id == 300){
				playerdata.cloakUrl = guiNpcTextField.getText();
			}
		}
	}
	class GuiPartLegs extends GuiPart{
		public GuiPartLegs() {
			super(EnumParts.LEGS);
			types = new String[]{"gui.none", "gui.normal", "legs.naga", "legs.spider",
					"legs.horse", "legs.mermaid", "legs.two_mermaid", "legs.digitigrade"};

			canBeDeleted = false;
		}
		@Override
		public int initGui(){
			hasPlayerOption = data.type == 1 || data.type == 6;
			return super.initGui();
		}

		@Override
		protected void actionPerformed(GuiButton btn) {
			if(btn.id == 20){
				int i = ((GuiNpcButton)btn).getValue();
				if(i == 0 && canBeDeleted)
					playerdata.removePart(part);
				else{
					data = playerdata.getOrCreatePart(part);
					data.setCustomResource("");
					data.playerTexture = false;
					data.pattern = 0;
					data.setType(i - 1);
					fixPlayerSkinLegs(playerdata);
				}
				GuiCreationParts.this.initGui();
			} else if(btn.id == 21){
				data.playerTexture = ((GuiNpcButtonYesNo)btn).getBoolean();
				if(data.playerTexture){
					data.setCustomResource("");
				} else {
					fixPlayerSkinLegs(playerdata);
				}
				data.color = 0xFFFFFF;
				GuiCreationParts.this.initGui();
			} else {
				super.actionPerformed(btn);
			}
		}
	}

	protected static void fixPlayerSkinLegs(ModelData playerdata){
		ModelPartData data = playerdata.getPartData(EnumParts.LEGS);
		if(data.type == 1 || data.type == 6){
			if(playerdata.modelType >= 1){
				data.setCustomResource(data.type + "-0");
			} else {
				data.setCustomResource("");
			}
			data.updateTextureLocation();
		}
	}
}
