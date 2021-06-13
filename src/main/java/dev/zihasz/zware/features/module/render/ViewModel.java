package dev.zihasz.zware.features.module.render;

import dev.xdark.ssbus.Listener;
import dev.zihasz.zware.event.events.TransformEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.features.setting.SubSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel extends Module {

	public ViewModel() {
		super("ViewModel", "Change how items are rendered.", Category.RENDER);
	}

	private final Setting<Boolean> noEat = new Setting<>("NoEat", "Cancels eating animations", false);

	private final Setting<Boolean> mainhand = new Setting<>("Mainhand", "Modify how your mainhand renders", true);
	private final SubSetting<Float> mhOffsetX = new SubSetting<>(mainhand,"MHOffsetX", "Offsets your mainhand on the X axis", 0f, -10f, 10f);
	private final SubSetting<Float> mhOffsetY = new SubSetting<>(mainhand,"MHOffsetY", "Offsets your mainhand on the Y axis", 0f, -10f, 10f);
	private final SubSetting<Float> mhOffsetZ = new SubSetting<>(mainhand,"MHOffsetZ", "Offsets your mainhand on the Z axis", 0f, -10f, 10f);
	private final SubSetting<Float> mhScaleX = new SubSetting<>(mainhand,"MHScaleX", "Scales your mainhand on the X axis", 0f, -10f, 10f);
	private final SubSetting<Float> mhScaleY = new SubSetting<>(mainhand,"MHScaleY", "Scales your mainhand on the Y axis", 0f, -10f, 10f);
	private final SubSetting<Float> mhScaleZ = new SubSetting<>(mainhand,"MHScaleZ", "Scales your mainhand on the Z axis", 0f, -10f, 10f);
	private final SubSetting<Float> mhRotateA = new SubSetting<>(mainhand,"MHRotateA", "Mainhand rotation angle", 0f, -360f, 360f);
	private final SubSetting<Float> mhRotateX = new SubSetting<>(mainhand,"MHRotateX", "Rotates your mainhand on the X axis", 0f, -10f, 10f);
	private final SubSetting<Float> mhRotateY = new SubSetting<>(mainhand,"MHRotateY", "Rotates your mainhand on the Y axis", 0f, -10f, 10f);
	private final SubSetting<Float> mhRotateZ = new SubSetting<>(mainhand,"MHRotateZ", "Rotates your mainhand on the Z axis", 0f, -10f, 10f);

	private final Setting<Boolean> offhand = new Setting<>("Offhand", "Modify how your offhand renders", true);
	private final SubSetting<Float> ohOffsetX = new SubSetting<>(offhand,"OHOffsetX", "Offsets your offhand on the X axis", 0f, -10f, 10f);
	private final SubSetting<Float> ohOffsetY = new SubSetting<>(offhand,"OHOffsetY", "Offsets your offhand on the Y axis", 0f, -10f, 10f);
	private final SubSetting<Float> ohOffsetZ = new SubSetting<>(offhand,"OHOffsetZ", "Offsets your offhand on the Z axis", 0f, -10f, 10f);
	private final SubSetting<Float> ohScaleX = new SubSetting<>(offhand,"OHScaleX", "Scales your offhand on the X axis", 0f, -10f, 10f);
	private final SubSetting<Float> ohScaleY = new SubSetting<>(offhand,"OHScaleY", "Scales your offhand on the Y axis", 0f, -10f, 10f);
	private final SubSetting<Float> ohScaleZ = new SubSetting<>(offhand,"OHScaleZ", "Scales your offhand on the Z axis", 0f, -10f, 10f);
	private final SubSetting<Float> ohRotateA = new SubSetting<>(offhand,"OHRotateA", "Offhand rotation angle", 0f, -360f, 360f);
	private final SubSetting<Float> ohRotateX = new SubSetting<>(offhand,"OHRotateX", "Rotates your offhand on the X axis", 0f, -10f, 10f);
	private final SubSetting<Float> ohRotateY = new SubSetting<>(offhand,"OHRotateY", "Rotates your offhand on the Y axis", 0f, -10f, 10f);
	private final SubSetting<Float> ohRotateZ = new SubSetting<>(offhand,"OHRotateZ", "Rotates your offhand on the Z axis", 0f, -10f, 10f);

	@Listener
	public void onTransformSide(TransformEvent.FirstPerson.Side event) {
		if (mainhand.getValue() && event.getHandSide() == EnumHandSide.RIGHT) {
			GlStateManager.translate(mhOffsetX.getValue(), mhOffsetY.getValue(), mhOffsetZ.getValue());
			GlStateManager.scale(mhScaleX.getValue(), mhScaleY.getValue(), mhScaleZ.getValue());
			GlStateManager.rotate(mhRotateA.getValue(), mhRotateX.getValue(), mhRotateY.getValue(), mhRotateZ.getValue());
		}
		if (offhand.getValue() && event.getHandSide() == EnumHandSide.LEFT) {
			GlStateManager.translate(ohOffsetX.getValue(), ohOffsetY.getValue(), ohOffsetZ.getValue());
			GlStateManager.scale(ohScaleX.getValue(), ohScaleY.getValue(), ohScaleZ.getValue());
			GlStateManager.rotate(ohRotateA.getValue(), ohRotateX.getValue(), ohRotateY.getValue(), ohRotateZ.getValue());
		}
	}

	@Listener
	public void onTransformEat(TransformEvent.FirstPerson.Eat event) {
		if (noEat.getValue())
			event.cancel();
	}

}
