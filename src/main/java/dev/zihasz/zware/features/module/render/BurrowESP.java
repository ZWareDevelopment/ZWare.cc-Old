package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.entity.EntityUtils;
import dev.zihasz.zware.utils.render.Renderer3D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;

public class BurrowESP extends Module {

	public BurrowESP() {
		super("BurrowESP", "Shows if someone is burrowed or not", Category.RENDER);
	}

	private final Setting<Integer> range = new Setting<>("Range", "Range", 20, 0, 100);

	private final Setting<Boolean> name = new Setting<>("Name", "Name", true);
	private final Setting<Color> nameColor = new Setting<>("NameColor", "NameColor", new Color(0xffffff));
	private final Setting<Float> nameScale = new Setting<>("NameWidth", "NameWidth", 1f, 0.1f, 10f);

	private final Setting<Boolean> fill = new Setting<>("Fill", "Fill", true);
	private final Setting<Color> fillColor = new Setting<>("FillColor", "FillColor", new Color(0xffffff));

	private final Setting<Boolean> outline = new Setting<>("Outline", "Outline", true);
	private final Setting<Color> outlineColor = new Setting<>("OutlineColor", "OutlineColor", new Color(0xffffff));
	private final Setting<Float> outlineWidth = new Setting<>("OutlineWidth", "OutlineWidth", 1.5f, 0f, 5f);

	private final HashMap<EntityPlayer, BlockPos> burrowMap = new HashMap<>();

	@Override
	public void onUpdate() {
		burrowMap.clear();
		mc.world.loadedEntityList.stream()
				.filter(entity -> entity instanceof EntityPlayer)
				.map(entity -> (EntityPlayer) entity)
				.filter(entity -> !entity.isDead)
				.filter(entity -> entity != mc.player)
				.filter(entity -> mc.player.getDistance(entity) <= range.getValue())
				.filter(EntityUtils::isBurrowed)
				.sorted(Comparator.comparingDouble(entity -> mc.player.getDistance(entity)))
				.forEach(player -> burrowMap.put(player, player.getPosition()));
	}

	@Override
	public void onRender3D(RenderWorldLastEvent event) {
		this.burrowMap.entrySet().forEach(entry -> {
			EntityPlayer player = entry.getKey();
			BlockPos blockPos = entry.getValue();
			drawBlock(blockPos);
			drawText(blockPos, player.getGameProfile().getName());
		});
	}

	private void drawBlock(BlockPos pos) {
		AxisAlignedBB bb = new AxisAlignedBB(pos);
		
		if (fill.getValue())
			Renderer3D.drawBBFill(bb, fillColor.getValue());

		if (outline.getValue())
			Renderer3D.drawBBOutline(bb, outlineWidth.getValue(), outlineColor.getValue());
	}
	private void drawText(BlockPos pos, String text) {
		if (name.getValue())
			Renderer3D.drawTextFromBlock(pos, text, nameColor.getValue().getRGB(), nameScale.getValue());
	}

}
