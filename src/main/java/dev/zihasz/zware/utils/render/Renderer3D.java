package dev.zihasz.zware.utils.render;

import dev.zihasz.zware.utils.Util;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL32;

import java.awt.Color;

import static net.minecraft.client.renderer.GlStateManager.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer3D implements Util, Renderer {
	private final static net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
	private final static BufferBuilder builder = tessellator.getBuffer();

	public static void drawBBFill(AxisAlignedBB bb, Color color) {
		pushMatrix();
		start1();
		width(1);

		final int r = color.getRed();
		final int b = color.getBlue();
		final int g = color.getGreen();
		final int a = color.getAlpha();

		builder.begin(GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		double minX = bb.minX;
		double minY = bb.minY;
		double minZ = bb.minZ;
		double maxX = bb.maxX;
		double maxY = bb.maxY;
		double maxZ = bb.maxZ;
		vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
		tessellator.draw();

		end1();
		popMatrix();
	}
	public static void drawBBOutline(AxisAlignedBB bb, float width, Color color) {
		start(width);

		final int r = color.getRed();
		final int b = color.getBlue();
		final int g = color.getGreen();
		final int a = color.getAlpha();

		builder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		vertex(bb.minX, bb.minY, bb.minZ, r, g, b, a);
		vertex(bb.minX, bb.minY, bb.maxZ, r, g, b, a);
		vertex(bb.maxX, bb.minY, bb.maxZ, r, g, b, a);
		vertex(bb.maxX, bb.minY, bb.minZ, r, g, b, a);
		vertex(bb.minX, bb.minY, bb.minZ, r, g, b, a);
		vertex(bb.minX, bb.maxY, bb.minZ, r, g, b, a);
		vertex(bb.minX, bb.maxY, bb.maxZ, r, g, b, a);
		vertex(bb.minX, bb.minY, bb.maxZ, r, g, b, a);
		vertex(bb.maxX, bb.minY, bb.maxZ, r, g, b, a);
		vertex(bb.maxX, bb.maxY, bb.maxZ, r, g, b, a);
		vertex(bb.minX, bb.maxY, bb.maxZ, r, g, b, a);
		vertex(bb.maxX, bb.maxY, bb.maxZ, r, g, b, a);
		vertex(bb.maxX, bb.maxY, bb.minZ, r, g, b, a);
		vertex(bb.maxX, bb.minY, bb.minZ, r, g, b, a);
		vertex(bb.maxX, bb.maxY, bb.minZ, r, g, b, a);
		vertex(bb.minX, bb.maxY, bb.minZ, r, g, b, a);
		tessellator.draw();

		end();
	}
	public static void drawBBSlabDown(AxisAlignedBB bb, float height, Color color) {
		final int r = color.getRed();
		final int g = color.getGreen();
		final int b = color.getBlue();
		final int a = color.getAlpha();
		double minX = bb.minX;
		double minY = bb.minY + 1;
		double minZ = bb.minZ;
		double maxX = bb.maxX;
		double maxY = bb.maxY + height;
		double maxZ = bb.maxZ;
		pushMatrix();

		disableTexture2D();
		enableBlend();
		disableAlpha();
		glDisable(GL_DEPTH_TEST);
		tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
		disableCull();
		shadeModel(GL_SMOOTH);

		builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, minZ).color(0, 0, 0, 0).endVertex();
		vertex(minX, maxY, maxZ).color(0, 0, 0, 0).endVertex();
		vertex(maxX, maxY, maxZ).color(0, 0, 0, 0).endVertex();
		vertex(maxX, maxY, minZ).color(0, 0, 0, 0).endVertex();
		vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, minZ).color(0, 0, 0, 0).endVertex();
		vertex(maxX, maxY, minZ).color(0, 0, 0, 0).endVertex();
		vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, minZ).color(0, 0, 0, 0).endVertex();
		vertex(maxX, maxY, maxZ).color(0, 0, 0, 0).endVertex();
		vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(maxX, maxY, maxZ).color(0, 0, 0, 0).endVertex();
		vertex(minX, maxY, maxZ).color(0, 0, 0, 0).endVertex();
		vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
		vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
		vertex(minX, maxY, maxZ).color(0, 0, 0, 0).endVertex();
		vertex(minX, maxY, minZ).color(0, 0, 0, 0).endVertex();
		tessellator.draw();

		glEnable(GL_DEPTH_TEST);
		shadeModel(GL_FLAT);
		disableBlend();
		enableCull();
		enableAlpha();
		enableTexture2D();
		popMatrix();
	}
	public static void drawLine(double posX, double posY, double posZ, double posX1, double posY1, double posZ1, Color color, float width) {
		start2();

		width(width);
		disableDepth();
		builder.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		builder.pos(posX1, posY1, posZ1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		builder.pos(posX, posY, posZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		tessellator.draw();
		end2();
	}

	public static void drawTextFromBlock(BlockPos pos, String text, int color, float scale) {
		pushMatrix();
		glBillboardDistanceScaled((float) pos.x + 0.5f, (float) pos.y + 0.5f, (float) pos.z + 0.5f, mc.renderViewEntity, scale);
		disableDepth();
		GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
		mc.fontRenderer.drawString(text, 0, 0, color);
		enableDepth();
		popMatrix();
	}
	public static void drawTextFromBlockWithBackground(BlockPos pos, String text, int color, float scale, Color bColor, boolean border, float width) {
		pushMatrix();
		glBillboardDistanceScaled((float) pos.x + 0.5f, (float) pos.y + 0.5f, (float) pos.z + 0.5f, mc.renderViewEntity, scale);
		disableDepth();
		GlStateManager.translate(-(mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
		if (border)
			Renderer2D.drawBorder(-1, -1, mc.fontRenderer.getStringWidth(text) + 1, mc.fontRenderer.FONT_HEIGHT - 2, width, bColor);
		Renderer2D.drawRect(-1, -1, mc.fontRenderer.getStringWidth(text) + 1, mc.fontRenderer.FONT_HEIGHT - 2, bColor);
		mc.fontRenderer.drawString(text, 0, 0, color);
		enableDepth();
		popMatrix();
	}

	public static void glBillboardDistanceScaled(float x, float y, float z, Entity player, float scale) {
		glBillboard(x, y, z);
		int distance = (int) player.getDistance(x, y, z);
		float scaleDistance = (distance / 2.0f / (2.0f + (2.0f - scale)));

		if (scaleDistance < 1.0f) {
			scaleDistance = 1.0f;
		}

		GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
	}
	public static void glBillboard(float x, float y, float z) {
		final float scale = 0.02666667f;

		GlStateManager.translate(x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ);
		GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-mc.renderViewEntity.rotationYaw, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(mc.renderViewEntity.rotationPitch, (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(-scale, -scale, scale);
	}

	public static void start(float width) {
		GlStateManager.pushMatrix();
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glDepthMask(false);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		width(width);
	}
	public static void start1() {
		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ZERO, GL_ONE);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
	}
	public static void start2() {
		GlStateManager.pushMatrix();
		width(1f);
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL32.GL_DEPTH_CLAMP);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		GlStateManager.disableAlpha();
		GlStateManager.shadeModel(GL_SMOOTH);
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
	}
	public static void end2() {
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(GL_FLAT);
		GlStateManager.enableAlpha();
		GlStateManager.depthMask(true);
		glDisable(GL32.GL_DEPTH_CLAMP);
		glDisable(GL_LINE_SMOOTH);
		GlStateManager.color(1f, 1f, 1f);
		width(1f);
		GlStateManager.popMatrix();
	}
	public static void end1() {
		glDisable(GL_LINE_SMOOTH);
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	public static void end() {
		glDepthMask(true);
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		GlStateManager.popMatrix();
	}

	private static void width(float width) {
		GlStateManager.glLineWidth(width);
	}
	private static void vertex(double x, double y, double z, int r, int g, int b, int a) {
		builder.pos(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ).color(r, g, b, a).endVertex();
	}
	private static void vertex(int r, int g, int b, int a) {
		builder.pos(0, mc.player.getEyeHeight(), 0).color(r, g, b, a).endVertex();
	}
	private static BufferBuilder vertex(double x, double y, double z) {
		return builder.pos(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ);
	}

}
