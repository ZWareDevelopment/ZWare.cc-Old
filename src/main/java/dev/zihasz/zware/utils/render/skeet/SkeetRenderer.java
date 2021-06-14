package dev.zihasz.zware.utils.render.skeet;

import dev.zihasz.zware.utils.render.Quad;
import dev.zihasz.zware.utils.render.Rainbow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * too lazy to port the entire thing so here :shrug:
 */
public class SkeetRenderer {
	private static final Tessellator tessellator = Tessellator.getInstance();
	private static final BufferBuilder builder = tessellator.getBuffer();

	public static void drawBox(Quad quad, Color color) {
		setup();
		final double x = Math.min(quad.getX(), quad.getRight());
		final double y = Math.min(quad.getY(), quad.getBottom());
		final double x1 = Math.max(quad.getX(), quad.getRight());
		final double y1 = Math.max(quad.getY(), quad.getBottom());
		builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		builder.pos(x1, y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		builder.pos(x, y, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		builder.pos(x, y1, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		builder.pos(x1, y1, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		tessellator.draw();
		end();
	}

	public static void drawRainbowX(Quad quad, float hue, float sat, float bright, float speed, int alpha) {
		drawRainbowX(quad, hue, sat, bright, speed, 0.5f, alpha);
	}

	public static void drawRainbowX(Quad quad, float hue, float sat, float bright, float speed, float pixelSpeed, int alpha) {
		final Rainbow rainbow = new Rainbow();
		rainbow.update(hue);
		for(float a = quad.getX() + pixelSpeed; a <= quad.getRight(); a += pixelSpeed) {
			rainbow.update(speed);
			final Color color = rainbow.getColor(0, sat, bright);
			drawBox(new Quad(a - pixelSpeed, quad.getY(), a, quad.getBottom()), alpha(alpha, color));
		}
	}

	public static Color alpha(int alpha, Color color) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	private static void setup() {
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	}

	private static void setupGradient() {
		glPushMatrix();

		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glDisable(GL_ALPHA);
		glDisable(GL_DEPTH_TEST);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		glDisable(GL_CULL_FACE);
		GlStateManager.shadeModel(GL_SMOOTH);
	}

	private static void endGradient() {
		glEnable(GL_DEPTH_TEST);
		GlStateManager.shadeModel(GL_FLAT);
		glDisable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glEnable(GL_ALPHA);
		glEnable(GL_TEXTURE_2D);

		glPopMatrix();
	}

	private static void end() {
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
	}
}