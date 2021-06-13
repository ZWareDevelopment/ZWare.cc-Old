package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

public class PenisESP extends Module {

	Setting<Boolean> animation = new Setting<>("Animation", "animate the cock", false);
	Setting<Float> spin = new Setting<>("Spin", "helicopter", 0.1f, 9.0f, 0.0f);

	public PenisESP() {
		super("PenisESP", "pp go brr", Category.RENDER);
	}

	@Override
	public void onRender3D(RenderWorldLastEvent event) {
		for (Entity entity : mc.world.loadedEntityList) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;

				final double n = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks;
				final double n2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks;
				final double n3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks;

				final double x = n - mc.getRenderManager().renderPosX;
				final double y = n2 - mc.getRenderManager().renderPosY;
				final double z = n3 - mc.getRenderManager().renderPosZ;

				GL11.glPushMatrix();
				RenderHelper.disableStandardItemLighting();
				this.drawPenis(player, x, y, z);
				RenderHelper.enableStandardItemLighting();
				GL11.glPopMatrix();

			}

			if (animation.getValue()) {
				spin.setValue(spin.getValue() + 1);
				if (spin.getValue() > 50.0f) {
					spin.setValue(-50.0f);
				} else if (spin.getValue() < -50.0f) {
					spin.setValue(50.0f);
				}
			} else {
				spin.setValue(0f);
			}
		}
	}

	public void drawPenis(final EntityPlayer player, final double x, final double y, final double z) {
		GL11.glDisable(2896);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(2929);
		GL11.glEnable(2848);
		GL11.glDepthMask(true);
		GL11.glLineWidth(1.0f);
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(-player.rotationYaw, 0.0f, player.height, 0.0f);
		GL11.glTranslated(-x, -y, -z);
		GL11.glTranslated(x, y + player.height / 2.0f - 0.22499999403953552, z);
		GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);

		double angle = (player.isSneaking() ? 35 : 0) + spin.getValue();
		GL11.glRotated(angle, 1.0f + spin.getValue(), 0f, 0f);
		GL11.glTranslated(0.0, 0.0, 0.07500000298023224);

		final Cylinder shaft = new Cylinder();
		shaft.setDrawStyle(100013);
		shaft.draw(0.1f, 0.11f, 0.4f, 25, 20);
		GL11.glColor4f(1.38f, 0.85f, 1.38f, 1.0f);
		GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
		GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);

		final Sphere right = new Sphere();
		right.setDrawStyle(100013);
		right.draw(0.14f, 10, 20);
		GL11.glTranslated(0.16000000149011612, 0.0, 0.0);

		final Sphere left = new Sphere();
		left.setDrawStyle(100013);
		left.draw(0.14f, 10, 20);
		GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
		GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);

		final Sphere tip = new Sphere();
		tip.setDrawStyle(100013);
		tip.draw(0.13f, 15, 20);
		GL11.glDepthMask(true);
		GL11.glDisable(2848);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
	}
}
