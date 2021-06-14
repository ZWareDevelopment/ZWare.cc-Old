package dev.zihasz.zware.utils.render;

import dev.zihasz.zware.utils.Util;
import net.minecraft.client.gui.ScaledResolution;

public class Quad implements Util {
	private float x, y, right, bottom;

	public Quad() {}

	public Quad(float x, float y, float right, float bottom) {
		this.x = x;
		this.y = y;
		this.right = right;
		this.bottom = bottom;
	}

	public boolean isWithinIncluding(float x, float y) {
		return x >= this.x && x <= this.right && y >= this.y && y <= this.bottom;
	}

	public boolean isWithin(float x, float y) {
		return x > this.x && x < this.right && y > this.y && y < this.bottom;
	}

	public boolean insideScreen() {
		return insideScreenX() && insideScreenY();
	}

	public boolean insideScreenX() {
		final ScaledResolution res = new ScaledResolution(mc);

		return (x >= 0 && right <= res.getScaledWidth());
	}

	public boolean insideScreenY() {
		final ScaledResolution res = new ScaledResolution(mc);

		return (y >= 0 && y <= res.getScaledHeight());
	}

	public void shrink(float amount) {
		setX(getX() + amount);
		setY(getY() + amount);
		setRight(getRight() - amount);
		setBottom(getBottom() - amount);
	}

	public Quad clone() { return new Quad(x, y, right, bottom); }
	public float width() { return right - x; }
	public float height() { return bottom - y; }

	public float getX() { return x; }
	public void setX(float x) { this.x = x; }
	public float getY() { return y; }
	public void setY(float y) {  this.y = y;  }
	public float getRight() { return right; }
	public void setRight(float right) { this.right = right; }
	public float getBottom() { return bottom; }
	public void setBottom(float bottom) { this.bottom = bottom; }
}
